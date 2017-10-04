package connection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;

import mosmessages.MosMessage;
import mossimulator.Model;
import mossimulator.Model.Port;

public class Connection extends Thread{
	private volatile Hashtable<Port, Socket> socket = new Hashtable<Port, Socket>();
	private volatile ServerSocket serverSocket = null;
	private volatile static Hashtable<Integer, ServerSocket> serverSockets = new Hashtable<Integer, ServerSocket>();
	private volatile Hashtable<Port,Semaphore> mutex = new Hashtable<Port,Semaphore>();
	private volatile Hashtable<Port,Semaphore> mutexSecond = new Hashtable<Port,Semaphore>();
	private volatile Hashtable<Port,Semaphore> mutexInner = new Hashtable<Port,Semaphore>();
	private Port port;
	private volatile boolean powerSwitch = true;
	public Connection(Port _receiver){
		port = _receiver;
		socket.put(port, new Socket());
		mutex.put(_receiver, new Semaphore(1));
		mutexSecond.put(_receiver, new Semaphore(1));
		mutexInner.put(_receiver, new Semaphore(1));
	}
	public void TurnOff(){
		powerSwitch = false;
		for (Port key : socket.keySet()){
			if (!socket.get(key).isClosed()){
				try {
					socket.get(key).close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	public void run(){
		while(true){
			ServerSocket serverSocket = null;
			try {
				mutexSecond.get(port).acquire();
				mutex.get(port).acquire();
				mutexSecond.get(port).release();
				serverSocket = new ServerSocket(port.getPortNumber());
				serverSockets.put(port.getPortNumber(), serverSocket);
				System.out.println("Waiting for connevtion on "+port.getPortNumber()+":");
				socket.put(port, serverSocket.accept());
				mutexInner.get(port).acquire();
				DataInputStream  socketIn = new DataInputStream(socket.get(port).getInputStream());
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
				if (readSocket!=""){
					System.out.println(port.getPortNumber()+": New message received:");
					MosMessage.setIsListening(true);
					try {
						new Model.MessageInfo(Model.MessageInfo.Direction.IN, readSocket).CallReceiveFunction(port);
						// aby zrobic reagowanie tutej trzeba zrobic druga funkcje caalreceivefuncion
						// ktora bedzie wykorzystywala otwarty juz socket i z niego brala in
						// bedzie uzywac sendwithoutclosiing i close
						// aby zrobic odbieranie wysylanie musze stworzyc 4 potry (dla przyhodch jesli sa ruzne)
						// i tylko dla wysylacjacych nie startowac run
						// aby zrobic to w comand line aplikacjia musi miec przelacznik z server i wtedy dzialac
						// w trybie interaktywnym 
						// wtedy ni emozna zmieniac ustawien
					} catch (Throwable e) {
						System.out.println("Received broken message. ");
					}
					MosMessage.setIsListening(false);
				}
				mutexInner.get(port).release();
				mutex.get(port).release();
			}
			catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			catch(SocketException e){
				mutex.get(port).release();
			}
			catch(IOException e){
				System.out.println("Unable to connect " + 
						mossimulator.Model.TARGETHOST + ":" + port.getPortNumber() 
						+ ".\n" + e.getMessage());
			}
			finally {
				try {
					if (serverSocket!= null && !serverSocket.isClosed())
						serverSocket.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			if (!powerSwitch)
				break;
		}
	}
	public String GetFromSocket(){
		if (socket.get(port).isClosed())
			return "";
		try {
			DataInputStream  socketIn = new DataInputStream(socket.get(port).getInputStream());
			long startTime = System.currentTimeMillis();
			String readSocket="";
			while (socketIn.available()<=0 && (System.currentTimeMillis()-startTime)<Model.SECTOWAIT*1000){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
				socketIn = new DataInputStream(socket.get(port).getInputStream());
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
	public boolean SendOnOpenSocket(MosMessage message){
		boolean result = false;
		if (socket.get(port).isClosed())
			return result;
		int attempts = 0;
		do{
			DataOutputStream socketInput;
			try {
				socketInput = new DataOutputStream(socket.get(port).getOutputStream());
				String content = message.toString();
				byte[] toByte = content.getBytes(StandardCharsets.UTF_16BE);
				socketInput.write(toByte, 0 , content.length() * 2);
				socketInput.flush();
				new Model.MessageInfo(Model.MessageInfo.Direction.OUT, content, message.getDocument());
				System.out.println("Sent.");
				message.AfterSending();
				result=true;
			} catch (IOException e) {
				System.out.println("Unable to get output stream from socket.");
			}
		}while(Model.RETRANSMISSON > attempts++ && !result && powerSwitch);
		return result;
	}
	public void Close(){
		try {
			if (!socket.get(port).isClosed())
				socket.get(port).close();	
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		mutexInner.get(port).release();
		mutexSecond.get(port).release();
		mutex.get(port).release();
	}
	public boolean SendWithoutClosing(MosMessage message){
		int attempts = 0;
		boolean result = false;
		try{
			mutexInner.get(port).acquire();
			mutexSecond.get(port).acquire();
			try {
				if (!serverSocket.isClosed()){
					serverSocket.close();	
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			mutex.get(port).acquire();
			do{
				try{
					socket.put(port, new Socket(mossimulator.Model.TARGETHOST, port.getPortNumber()));
					DataOutputStream socketInput = new DataOutputStream(socket.get(port).getOutputStream());
					String content = message.toString();
					byte[] toByte = content.getBytes(StandardCharsets.UTF_16BE);
					socketInput.write(toByte, 0 , content.length() * 2);
					socketInput.flush();
					new Model.MessageInfo(Model.MessageInfo.Direction.OUT, content, message.getDocument());
					System.out.println("Sent.");
					message.AfterSending();
					result = true;
				}
				catch(IOException e){
					System.out.println("Unable to connect " +
							mossimulator.Model.TARGETHOST + ":" + port.getPortNumber()
							+ ".\n" + e.getMessage());
				}
			}while(Model.RETRANSMISSON > attempts++ && !result && powerSwitch);
			}
		catch(InterruptedException e){
			System.out.println(e.getMessage());
		}
		return result;
	}
	public boolean Send(MosMessage message){
		int attempts = 0;
		boolean result = false;
		try{
			mutexInner.get(port).acquire();
			mutexSecond.get(port).acquire();
			try {
				serverSocket = serverSockets.get(port.getPortNumber());
				if (serverSocket!=null && !serverSocket.isClosed()){
					serverSockets.remove(port.getPortNumber());
					serverSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			mutex.get(port).acquire();
			do{
				try{
					socket.put(port, new Socket(mossimulator.Model.TARGETHOST, port.getPortNumber()));
					DataOutputStream socketInput = new DataOutputStream(socket.get(port).getOutputStream());
					String content = message.toString();
					byte[] toByte = content.getBytes(StandardCharsets.UTF_16BE);
					socketInput.write(toByte, 0 , content.length() * 2);
					socketInput.flush();
					new Model.MessageInfo(Model.MessageInfo.Direction.OUT, content, message.getDocument());
					System.out.println("Sent.");
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
						if (!socket.get(port).isClosed())
							socket.get(port).close();	
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}while(Model.RETRANSMISSON > attempts++ && !result && powerSwitch);
			mutexInner.get(port).release();
			mutexSecond.get(port).release();
			mutex.get(port).release();
			}
		catch(InterruptedException e){
			System.out.println(e.getMessage());
		}
		return result;
	}
}
