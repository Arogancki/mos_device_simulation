package connection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;
import java.lang.Integer;

import mosmessages.MOSMessage;
import mossimulator.Model;
import mossimulator.Model.Port;

public class Connection extends Thread{
	private volatile Socket socket = null;
	private volatile ServerSocket serverSocket = null;
	private volatile static Hashtable<Integer, ServerSocket> serverSockets = new Hashtable<Integer, ServerSocket>();
	private volatile Semaphore mutex = new Semaphore(1);
	private volatile Semaphore mutexSecond = new Semaphore(1);
	private volatile Semaphore mutexInner = new Semaphore(1);
	private Port port;
	private volatile boolean powerSwitch = true;
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
			ServerSocket serverSocket = null;
			try {
				mutexSecond.acquire();
				mutex.acquire();
				mutexSecond.release();
				serverSocket = new ServerSocket(port.getPortNumber());
				serverSockets.put(port.getPortNumber(), serverSocket);
				socket = serverSocket.accept();
				mutexInner.acquire();
				System.out.println(socket.getRemoteSocketAddress());
				DataInputStream  socketIn = new DataInputStream(socket.getInputStream());
				long startTime = System.currentTimeMillis();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				String readSocket="";
				while (socketIn.available()<=0 && (System.currentTimeMillis()-startTime)<Model.SECTOWAIT*1000){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
				}
				while(socketIn.available()>0) {
		        	readSocket += socketIn.readChar();
				}
				System.out.println("New message received: " + readSocket);
				try {
					new Model.MessageInfo(Model.MessageInfo.Direction.IN, readSocket).CallReceiveFunction();
				} catch (Throwable e) {
					System.out.println("Received broken message. ");
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
	public boolean SendOnOpenSocket(MOSMessage message){
		boolean result = false;
		if (socket.isClosed())
			return result;
		int attempts = 0;
		do{
			DataOutputStream socketInput;
			try {
				socketInput = new DataOutputStream(socket.getOutputStream());
				String content = message.toString();
				socketInput.writeChars(content);
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
			if (!socket.isClosed())
				socket.close();	
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		mutexInner.release();
		mutexSecond.release();
		mutex.release();
	}
	public boolean SendWithoutClosing(MOSMessage message){
		int attempts = 0;
		boolean result = false;
		try{
			mutexInner.acquire();
			mutexSecond.acquire();
			try {
				if (!serverSocket.isClosed()){
					serverSocket.close();	
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			mutex.acquire();
			do{
				try{
					socket = new Socket(mossimulator.Model.TARGETHOST, port.getPortNumber());
					DataOutputStream socketInput = new DataOutputStream(socket.getOutputStream());
					String content = message.toString();
					socketInput.writeChars(content);
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
	public boolean Send(MOSMessage message){
		int attempts = 0;
		boolean result = false;
		try{
			mutexInner.acquire();
			mutexSecond.acquire();
			try {
				serverSocket = serverSockets.get(port.getPortNumber());
				if (serverSocket!=null && !serverSocket.isClosed()){
					serverSockets.remove(port.getPortNumber());
					serverSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			mutex.acquire();
			do{
				try{
					socket = new Socket(mossimulator.Model.TARGETHOST, port.getPortNumber());
					DataOutputStream socketInput = new DataOutputStream(socket.getOutputStream());
					String content = message.toString();
					socketInput.writeChars(content);
					//socketInput.flush();
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
						if (!socket.isClosed())
							socket.close();	
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}while(Model.RETRANSMISSON > attempts++ && !result && powerSwitch);
			mutexInner.release();
			mutexSecond.release();
			mutex.release();
			}
		catch(InterruptedException e){
			System.out.println(e.getMessage());
		}
		return result;
	}
}
