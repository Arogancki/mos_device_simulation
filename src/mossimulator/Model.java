package mossimulator;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import mosmessages.MosMessage;
import mosmessages.profile0.Heartbeat;
import mosmessages.profile0.ListMachInfo;
import mosmessages.profile0.ReqMachInfo;
import mosmessages.profile1.MosAck;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import connection.Connection;

public class Model {
	private final static String SAVEFILE = "settings.ser";
	private static final String MESSAVE = "messages.ser";
	private static boolean powerSwitch = true;
	public static long SECTOWAIT;
	public static String TARGETHOST;
	public static String MOSID;
	public static String NCSID;
	public static int RETRANSMISSON;
	public static long STARTDATE;
	public static int messageID;
	public static int getLowerNu(){return lower.getPortNumber();}
	public static int getUpperNu(){return upper.getPortNumber();}
	private static Port lower;
	private static Port upper;
	private static SaveState saveState;
	private static class SaveState implements Serializable{
		private static final long serialVersionUID = 1L;
		public long SECTOWAIT= 3L;
		public String TARGETHOST= "10.105.250.217";
		public String MOSID = "COMMAND";
		public String NCSID= "NCSSimulator";
		public int RETRANSMISSON= 3;
		public long STARTDATE= System.currentTimeMillis();
		public int messageID = 0;
		public int lower = 10540;
		public int upper = 10541;
		public void serialize(){
			try (FileOutputStream fileOut = new FileOutputStream(SAVEFILE);
					ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
		         out.writeObject(this);
		      }catch(IOException i) {
		    	  System.out.println("Error: Coudln't find save file!");
		      }
		}
	}
	static {
		try (FileInputStream fileIn = new FileInputStream(SAVEFILE);
			ObjectInputStream ois = new ObjectInputStream(fileIn)){
	         saveState = (SaveState) ois.readObject();
	         SECTOWAIT = saveState.SECTOWAIT;
	         TARGETHOST = saveState.TARGETHOST;
	         MOSID = saveState.MOSID;
			 NCSID = saveState.NCSID;
			 RETRANSMISSON = saveState.RETRANSMISSON;
			 STARTDATE = saveState.STARTDATE;
			 messageID = saveState.messageID;
			 lower =  new Port(saveState.lower);
			 upper =  new Port(saveState.upper);
		}
		catch (IOException | ClassNotFoundException e){
			saveState = new SaveState();
			SECTOWAIT = 3L;
			TARGETHOST = "0.0.0.1";
			MOSID = "MOSSimulator";
			NCSID = "NCSSimulator";
			RETRANSMISSON = 3;
			STARTDATE = System.currentTimeMillis();
			messageID = 0;
			lower = new Port(10540);
			upper = new Port(10541);
			System.out.println("Warrning: Save wasn't read! Creating a new save file with default values.");
			saveState.serialize();
		}
	}
	public static Port getLowerPort(){return lower;};
	public static Port getUpperPort(){return upper;};
	public static LinkedList<MessageInfo> messages = null;
	public static boolean LoadList(){
		try (FileInputStream fis = new FileInputStream(MESSAVE);
	            ObjectInputStream ois = new ObjectInputStream(fis)){
			messages = (LinkedList) ois.readObject();
			return true;
        }
		catch(ClassNotFoundException|IOException c){
        	  System.out.println("Warrning: Messages wasn't read! Creating a new, empty save file.");
        	  messages = new LinkedList<MessageInfo>();
        	  Serialize();
        	  return false;
        }
	}
	private static void Serialize(){
		try (FileOutputStream fos= new FileOutputStream(MESSAVE);
		         ObjectOutputStream oos= new ObjectOutputStream(fos)){
	         oos.writeObject(messages);
	       }catch(IOException ioe){
	            System.out.println("Warrning: Message hasn't been saved.");
	        }
	}
	private static void AddToList(MessageInfo el){
		if (messages==null){
			LoadList();
		}
		messages.add(el);
		Serialize();
	}
	public static class MessageInfo implements Serializable{
		private static final long serialVersionUID = 1L;
		public enum Direction{
			IN,OUT;
		}
		private Direction direction;
		private String time;
		private String message;
		private Document xmlDoc;
		// create incognito message
		public MessageInfo(Direction _direction, String _message, boolean x){
			message = _message;
			try {
				xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(_message)));
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			direction = _direction;
		}
		public MessageInfo(Direction _direction, String _message, Document _xmlDoc){
			time =  (new SimpleDateFormat("yy/MM/dd HH:mm:ss.SSS")).format(new Date());
			message = _message;
			xmlDoc = _xmlDoc;
			direction = _direction;
			AddToList(this);
		}
		public MessageInfo(Direction direction, String _message) throws Throwable{
			this(direction, _message, DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(_message))));
		}
		public Document getDocument(){return xmlDoc;}
		public String getString(){return message;}

		public String getMosType(){
			NodeList nodeList = xmlDoc.getDocumentElement().getChildNodes();
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node node = nodeList.item(i);
		        if (node.getNodeType() == Node.ELEMENT_NODE) {
		        	String nodeName = node.getNodeName().toLowerCase();
		            if (!nodeName.equals("mosid") && !nodeName.equals("ncsid") && !nodeName.equals("messageid"))
		            	return nodeName;
		        }
		    }
		    return "";
		}
		public String GetFromXML(String tagname){
			NodeList nodeList = xmlDoc.getElementsByTagName(tagname);
			if (nodeList.getLength() > 0)
				return nodeList.item(0).getTextContent();
			return null;
		}
		public void CallReceiveFunction(){
			switch (getMosType().toLowerCase()) {
			// profile 0
			case "heartbeat":
				Heartbeat.AfterReceiving(this); break;
			case "reqmachinfo":
				ReqMachInfo.AfterReceiving(this); break;
			case "listmachinfo":
				ListMachInfo.AfterReceiving(this); break;
            //profile 1
			case "mosack":
				MosAck.AfterReceiving(this);break;
			case "mosobj":
				MosAck.AfterReceiving(this); break;
			case "mosreqobj":
				MosAck.AfterReceiving(this); break;
			case "mosreqall":
				MosAck.AfterReceiving(this); break;
			case "moslistall":
				MosAck.AfterReceiving(this); break;
			//profile 2
			//profile 3
			//profile 4
			//profile 5
			//profile 6
			//profile 7
			default:
				System.out.println("Unsupported MOS message recived."); break;
			}
		}
		@Override
		public String toString(){
			return ( direction==Direction.IN ? "<- " : "-> " ) + getMosType() + " (" + time + ")";
		}
	}
	public static class Port{
		private int number;
		private Thread connection;
		Port(int _number){
			number = _number;
			connection = new Connection(this);
			//connection.start(); // run simulator also as an receiver (mos server)
		}
		public int getPortNumber(){return number;}
		public String GetMessage(){
			return ((Connection)connection).GetFromSocket();
		}
		public boolean Send(MosMessage message){
			return ((Connection)connection).Send(message);
		}
		public boolean SendOnOpenSocket(MosMessage message){
			return ((Connection)connection).SendOnOpenSocket(message);
		}
		public boolean SendWithoutClosing(MosMessage message){
			return ((Connection)connection).SendWithoutClosing(message);
		}
		public void CloseSocket(){
			((Connection)connection).Close();
		}
	}
	public static int takeMessageId(){
		messageID+=1;
		saveState.messageID=messageID;
		saveState.serialize();
		return messageID;
	}
	public static void ListPorts(){
		System.out.println("Lower:" + lower.getPortNumber());
		System.out.println("Upper:" + upper.getPortNumber());
	}
	public static boolean getPowerSwitch(){
		return powerSwitch;
	}
	public static void Exit(){
		powerSwitch = false;
		((Connection)lower.connection).TurnOff();
		((Connection)upper.connection).TurnOff();
	}
	protected void finalize() throws Throwable{
		Exit();
		super.finalize();
	}
	public static void setSECTOWAIT(long _SECTOWAIT){
		saveState.SECTOWAIT = _SECTOWAIT;
		SECTOWAIT = _SECTOWAIT;
		saveState.serialize();
	}
	public static void setTARGETHOST(String _TARGETHOST){
		saveState.TARGETHOST = _TARGETHOST;
		TARGETHOST = _TARGETHOST;
		saveState.serialize();
	}
	public static void setMOSID(String _MOSID){
		saveState.MOSID = _MOSID;
		MOSID = _MOSID;
		saveState.serialize();
	}
	public static void setNCSID(String _NCSID){
		saveState.NCSID = _NCSID;
		NCSID = _NCSID;
		saveState.serialize();
	}
	public static void setRETRANSMISSON(int _RETRANSMISSON){
		saveState.RETRANSMISSON = _RETRANSMISSON;
		RETRANSMISSON = _RETRANSMISSON;
		saveState.serialize();
	}
	public static void setmessageID(int _messageID){
		saveState.messageID = _messageID;
		messageID = _messageID;
		saveState.serialize();
	}
	public static void setLower(int _port){
		saveState.lower=_port;
		saveState.serialize();
	}
	public static void setUpper(int _port){
		saveState.upper=_port;
		saveState.serialize();
	}
	public static void resetTime(){
		saveState.STARTDATE = System.currentTimeMillis();
		STARTDATE = System.currentTimeMillis();
		saveState.serialize();
	}
}
