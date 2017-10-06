package connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import mosmessages.MosMessage;
import mossimulator.Model;

public class ClientConnection extends Thread{
	private String host;
	private volatile boolean powerSwitch = true;
	private int port;
	private ArrayList<MosMessage> messages = new ArrayList<MosMessage>();
	private Semaphore semaphore = new Semaphore(1);
	private Socket socket;
	public ClientConnection(String h, int p, MosMessage InitMessage){
		host = h;
		port = p;
		messages.add(InitMessage);
		this.start();
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
	}
	private boolean isMosMessage(String string){
		return string!=null && !string.equalsIgnoreCase("") && !string.matches("<\\?xml.*>");
	}
	public void run(){
		try{
			socket = new Socket(host, port);
			while(powerSwitch){
				// sending
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
						System.out.println("Sent message: "+new Model.MessageInfo(Model.MessageInfo.Direction.OUT, content, message.getDocument()).getMosType());
						message.AfterSending();
					}
					else
						semaphore.release();
				} catch (InterruptedException e){}
				// receiving
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
							new Model.MessageInfo(Model.MessageInfo.Direction.IN, readSocket).CallReceiveFunction(messages);
						} catch (Throwable e) {
							System.out.println("Receiving corrupted message:\n\"\n"+messages+"\n\"\n");
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Connection broken");
		}
	}
}
