package connection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Semaphore;

import mosmessages.MOSMessage;
import mossimulator.Model;
import mossimulator.Model.Port;

public class Connection extends Thread{
	private Socket socket = null;
	private Semaphore mutex = new Semaphore(1);
	private Semaphore mutexInner = new Semaphore(1);
	private Port port;
	private boolean powerSwitch = true;
	public Connection(Port _receiver){
		port = _receiver;
		socket = new Socket();
	}
	public void TurnOff(){
		powerSwitch = false;
		if (!socket.isClosed()){
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	public void run(){
		while(true){
			try {
				mutex.acquire();
				socket = new ServerSocket(port.getPortNumber()).accept();
				mutexInner.acquire();
				DataInputStream  socketIn= new DataInputStream(socket.getInputStream());
				String content="";
				while(socketIn.available()>0) {
					content += socketIn.readChar();
				}
				System.out.println("mam to:" + content);
				try {
					new Model.MessageInfo(Model.MessageInfo.Direction.IN, content);
				} catch (Throwable e) {
					System.out.println("Received broken message. " + content);
				}
				mutexInner.release();
				mutex.release();
			}
			catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			catch(SocketException e){
				mutex.release();
			}
			catch(IOException e){
				System.out.println("Unable to connect " + 
						mossimulator.Model.TARGETHOST + ":" + port.getPortNumber() 
						+ ".\n" + e.getMessage());
			}
			finally {
				try {
					if (!socket.isClosed())
						socket.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e){}
			if (!powerSwitch)
				break;
		}
	}
	public String GetFromSocket(){
		if (socket.isClosed())
			return "";
		try {
			DataInputStream  socketIn = new DataInputStream(socket.getInputStream());
			long startTime = System.currentTimeMillis();
			String readSocket="";
			while (socketIn.available()<=0 && (System.currentTimeMillis()-startTime)<Model.SECTOWAIT*1000){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
			}
			while(socketIn.available()>0) {
		        	readSocket += socketIn.readChar();
			}
			return readSocket;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
	public boolean Send(MOSMessage message){
		int attempts = 0;
		boolean result = false;
		try{
			mutexInner.acquire();
			mutex.acquire();
			try {
				if (!socket.isClosed())
					socket.close();	
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			do{
				try{
					socket = new Socket(mossimulator.Model.TARGETHOST, port.getPortNumber());
					DataOutputStream socketInput = new DataOutputStream(socket.getOutputStream());
					String content = message.toString();
					socketInput.writeChars(content);
					socketInput.flush();
					new Model.MessageInfo(Model.MessageInfo.Direction.OUT, content, message.getDocument());
					message.AfterSending();
					result = true;
				}
				catch(IOException e){
					System.out.println("Unable to connect " + 
							mossimulator.Model.TARGETHOST + ":" + port.getPortNumber() 
							+ ".\n" + e.getMessage());
				}
				finally {
					try {
						if (!socket.isClosed())
							socket.close();	
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}while(Model.RETRANSMISSON > attempts++ && !result && powerSwitch);
			mutexInner.release();
			mutex.release();
			}
		catch(InterruptedException e){
			System.out.println(e.getMessage());
		}
		return result;
	}
}
