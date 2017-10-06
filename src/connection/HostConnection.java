package connection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;

import mosmessages.MosMessage;
import mosmessages.profile0.Heartbeat;
import mossimulator.Model;

class HandleConnection extends Thread{
	private HostConnection parent;
	private boolean connectionCheck = false;
	private long lastConnectionCheckTime = 0L;
	private String host = null;
	private int port = -1;
	private ArrayList<MosMessage> messages = new ArrayList<MosMessage>();
	private Socket socket = null;
	private Semaphore semaphore = new Semaphore(1);
	private volatile boolean powerSwitch = true;
	HandleConnection(HostConnection Parent, Socket _socket){
		socket=_socket;
		parent=Parent;
		this.setName("HostHandling: " + socket.getLocalSocketAddress());
		this.start();
	}
	public boolean isThis(String _host, int _port){
		return host.equalsIgnoreCase(_host) && port == _port;
	}
	public void newMessage(MosMessage mes){
		try {
			semaphore.acquire();
			messages.add(mes);
			semaphore.release();
		} catch (InterruptedException e) {
		}
	}
	public void TurnOff(){
		powerSwitch = false;
		try {
			if (socket!=null)
				socket.close();
		} catch (IOException e) {}
	}
	private boolean isMosMessage(String string){
		return string!=null && !string.equalsIgnoreCase("") && !string.matches("<\\?xml.*>");
	}
	public void run(){
		host = (socket.getRemoteSocketAddress().toString().split(":"))[0].substring(1);
		try{
			port = Integer.parseInt((socket.getLocalSocketAddress().toString().split(":")[1]));
		}catch (NumberFormatException e){}
		System.out.println("Client connected: " + host + ":" + port);
		lastConnectionCheckTime = System.currentTimeMillis();
		while (powerSwitch){
			try{
				//get
				if (socket.isClosed()){
					System.out.println(host+":"+port+"Connection closed.");
					powerSwitch=false;
					break;
				}
				DataInputStream  socketIn = new DataInputStream(socket.getInputStream());
				String readSocket="";
				long startTime = System.currentTimeMillis();
				while (socketIn.available()<=0 && ( (System.currentTimeMillis()-startTime) < Model.SECTOWAIT * 1000 )){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
				}
				while(socketIn.available()>0) {
		        	readSocket += socketIn.readChar();
				}
				String[] messagesArray = readSocket.split("<mos>");
				for (String  oneMessage: messagesArray)
				{
					if (isMosMessage(oneMessage)){
						readSocket = "<mos>"+oneMessage;
						try {
							Model.MessageInfo mi = new Model.MessageInfo(Model.MessageInfo.Direction.IN, readSocket);
							lastConnectionCheckTime = System.currentTimeMillis();
							connectionCheck=false;
							semaphore.acquire();
							mi.CallReceiveFunction(messages);
							semaphore.release();
						} catch (Throwable e) {
							System.out.println("Receiving corrupted message:\n\"\n"+messages+"\n\"\n");
						}
					}
				}
				if ((System.currentTimeMillis()-lastConnectionCheckTime) >= Model.heartbeatSpace && Model.heartbeatSpace>0){
					if (connectionCheck){
						powerSwitch=false;
						System.out.println(host+":"+port+"Connection closed - heartbeat response wasn't received.");
						break;
					}else{
						connectionCheck = true;
						new Heartbeat().activeExpectingReply().Send(messages);
						lastConnectionCheckTime=System.currentTimeMillis();
					}
				}
				//send
				if (socket.isClosed()){
					System.out.println(host+":"+port+"Connection closed.");
					powerSwitch=false;
					break;
				}
				try {
					semaphore.acquire();
					if (messages.size()>0){
						DataOutputStream socketInput = new DataOutputStream(socket.getOutputStream());
						MosMessage message = messages.get(0);
						messages.remove(0);
						semaphore.release();
						String content = message.toString();
						byte[] toByte = content.getBytes(StandardCharsets.UTF_16BE);
						socketInput.write(toByte, 0 , content.length() * 2);
						socketInput.flush();
						System.out.println("Message sent: "+new Model.MessageInfo(Model.MessageInfo.Direction.OUT, content, message.getDocument()).getMosType());
						message.AfterSending();
					}
					else{
						semaphore.release();
					}
				} catch (InterruptedException e){}
			}
			catch(IOException e){}
		}
		parent.removeConnection(this);
	}
}

public class HostConnection extends Thread{
	private int port;
	private Semaphore semaphore = new Semaphore(1);
	private Semaphore hashSemaphore = new Semaphore(1);
	private ServerSocket serverSocket;
	private ArrayList<HandleConnection> connections = new ArrayList<HandleConnection>();
	private Hashtable<String,Hashtable<Integer,HandleConnection>> hashConnections = new Hashtable<String,Hashtable<Integer,HandleConnection>>();
	private volatile boolean powerSwitch = true;
	public HostConnection(int _port){
		port = _port;
		try {
			serverSocket = new ServerSocket(port);
			this.start();
		} catch (IOException e) {
			System.out.println("Cannot host connection on port: " + port + ".");
		}
	}
	public boolean newMessage(String host, int port, MosMessage mes){
		try {
			hashSemaphore.acquire();
			if (hashConnections.containsKey(host)){
				Hashtable<Integer,HandleConnection> ht = hashConnections.get(host);
				if (ht.containsKey(port)){
					ht.get(port).newMessage(mes);
					hashSemaphore.release();
					return true;
				}
			}
			hashSemaphore.release();
			try {
				semaphore.acquire();
				for(int i=0; i<connections.size(); i++){
					HandleConnection hc = connections.get(i);
					if (hc.isThis(host,port)){
						hc.newMessage(mes);
						hashSemaphore.acquire();
						if (hashConnections.containsKey(host)){
							Hashtable<Integer,HandleConnection> ht = hashConnections.get(host);
							ht.put(port, hc);
						}
						else{
							Hashtable<Integer,HandleConnection> ht = new Hashtable<Integer,HandleConnection>();
							ht.put(port, hc);
							hashConnections.put(host, ht);
						}
						hashSemaphore.release();
						connections.remove(i);
						semaphore.release();
						return true;
					}
				}
				semaphore.release();
			} catch (InterruptedException e) {}
		} catch (InterruptedException e1) {}
		return false;
	}
	boolean removeConnection(HandleConnection hc){
		try{
			hashSemaphore.acquire();
			for (String key: hashConnections.keySet()){
				if (hashConnections.get(key).contains(hc)){
					hashConnections.get(key).remove(hc);
					hashSemaphore.release();
					return true;
				}
			}
			hashSemaphore.release();
			if (connections.contains(hc)){
				connections.remove(hc);
				semaphore.release();
				return true;
			}
			semaphore.release();
			return removeConnection(hc);
		}
		catch(InterruptedException e){
			
		}
		return false;
	}
	public void TurnOff(){
		powerSwitch = false;
		try {
			semaphore.acquire();
			connections.forEach((v)->v.TurnOff());
			semaphore.release();
			hashSemaphore.acquire();
			hashConnections.forEach((k,v)->v.forEach((kk,vv)->vv.TurnOff()));
			hashSemaphore.release();
		} catch (InterruptedException e1) {}
		try {
			serverSocket.close();
		} catch (IOException e) {}
	}
	public void run(){
		System.out.println("Listening for clients on: "+port+"...");
			while(powerSwitch){
				try {
					if (powerSwitch){
						HandleConnection hc = new HandleConnection(this, serverSocket.accept());
						semaphore.acquire();
						connections.add(hc);
						semaphore.release();
					}
				} catch (InterruptedException | IOException e) {}
			}
	}
}
		