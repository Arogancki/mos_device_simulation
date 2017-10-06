package mossimulator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import mosmessages.MosMessage;
import mosmessages.profile0.Heartbeat;
import mosmessages.profile0.ListMachInfo;
import mosmessages.profile0.ReqMachInfo;
import mosmessages.profile1.MosAck;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import connection.ClientConnection;
import connection.HostConnection;

public class Model {
	private static Semaphore semaphoreHashtable = new Semaphore(1);
	public static Hashtable<String, Hashtable<Integer, ClientConnection>> clients=new  Hashtable<String, Hashtable<Integer, ClientConnection>>();
	private final static String SAVEFILE = "Settings.ser";
	private static final String MESSAVE = "Messages.ser";
	private static boolean powerSwitch = true;
	public static long SECTOWAIT;
	public static String TARGETHOST;
	public static int TARGETPORT;
	public static String MOSID;
	public static String NCSID;
	public static int RETRANSMISSON;
	public static long STARTDATE;
	public static int messageID;
	public static int heartbeatSpace;
	public static ArrayList<MosMessage> toSendMessagesLower = new ArrayList<MosMessage>();
	public static ArrayList<MosMessage> toSendMessagesUpper = new ArrayList<MosMessage>();
	private static HostConnection Hostlower = null;
	private static HostConnection Hostupper = null;
	private static int lower;
	private static int upper;
	private static SaveState saveState;
	private static class SaveState implements Serializable{
		private static final long serialVersionUID = 1L;
		public long SECTOWAIT= 3L;
		public String TARGETHOST= "10.105.250.217";
		public int TARGETPORT=10540;
		public String MOSID = "COMMAND";
		public String NCSID= "NCSSimulator";
		public int RETRANSMISSON= 3;
		public long STARTDATE= System.currentTimeMillis();
		public int messageID = 0;
		public int heartbeatSpace= 30000;
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
	         TARGETPORT = saveState.TARGETPORT;
	         MOSID = saveState.MOSID;
			 NCSID = saveState.NCSID;
			 RETRANSMISSON = saveState.RETRANSMISSON;
			 STARTDATE = saveState.STARTDATE;
			 messageID = saveState.messageID;
			 lower = saveState.lower;
			 upper = saveState.upper;
			 heartbeatSpace = saveState.heartbeatSpace;
		}
		catch (IOException | ClassNotFoundException e){
			saveState = new SaveState();
			SECTOWAIT = 3L;
			TARGETHOST = "0.0.0.1";
			 TARGETPORT = 10540;
			MOSID = "MOSSimulator";
			NCSID = "NCSSimulator";
			RETRANSMISSON = 3;
			STARTDATE = System.currentTimeMillis();
			messageID = 0;
			lower = 10540;
			upper = 10541;
			heartbeatSpace = 30000;
			System.out.println("Warrning: Settings save file wasn't read! Creating new save file with default values.");
			saveState.serialize();
		}
		if (console.Console.IS_INTERACTIVE_MODE_ON){
			Hostlower = new HostConnection(lower);
			Hostupper = new HostConnection(upper);
		}
	}
	public static boolean SendToHosted(String _host, int _port, MosMessage _mes){
		if (Hostlower!=null)
			if (Hostlower.newMessage(_host, _port, _mes)){
				return true;
			}
		if (Hostupper!=null)
			if (Hostupper.newMessage(_host, _port, _mes)){
				return true;
			}
		return false;
	}
	public static int getLowerPort(){return lower;};
	public static int getUpperPort(){return upper;};
	public static LinkedList<MessageInfo> messages = null;
	public static boolean removeClientConnection(ClientConnection cc){
		try {
			semaphoreHashtable.acquire();
			for (String key : clients.keySet()){
				if (clients.contains(cc)){
					clients.remove(cc);
					semaphoreHashtable.release();
					return true;
				}
			}
			semaphoreHashtable.release();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static boolean LoadList(){
		try (FileInputStream fis = new FileInputStream(MESSAVE);
	            ObjectInputStream ois = new ObjectInputStream(fis)){
			messages = (LinkedList<MessageInfo>) ois.readObject();
			return true;
        }
		catch(ClassNotFoundException|IOException c){
        	  System.out.println("Warrning: Messages save file wasn't read! Creating a new, empty save file.");
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
	public static void RemoveFromList(int key){
		Model.messages.remove(key);
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
		// creates incognito message
		public MessageInfo(Direction _direction, String _message, boolean x){
			message = _message;
			try {
				xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(_message)));
			} catch (SAXException|IOException|ParserConfigurationException e) {
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
		public void CallReceiveFunction(ArrayList<MosMessage> m){
			switch (getMosType().toLowerCase()) {
			// profile 0
			case "heartbeat":
				Heartbeat.AfterReceiving(this, m); break;
			case "reqmachinfo":
				ReqMachInfo.AfterReceiving(this, m); break;
			case "listmachinfo":
				ListMachInfo.AfterReceiving(this, m); break;
            //profile 1mm
			case "mosack":
				MosAck.AfterReceiving(this, m);break;
			case "mosobj":
				mosmessages.profile1.MosObj.AfterReceiving(this, m); break;
			case "mosreqobj":
				mosmessages.profile1.MosReqObj.AfterReceiving(this, m); break;
			case "mosreqall":
				mosmessages.profile1.MosReqAll.AfterReceiving(this, m); break;
			case "moslistall":
				mosmessages.profile1.MosListAll.AfterReceiving(this, m); break;
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
	public static int takeMessageId(){
		messageID+=1;
		saveState.messageID=messageID;
		saveState.serialize();
		return messageID;
	}
	public static boolean getPowerSwitch(){
		return powerSwitch;
	}
	public static void Exit(){
		powerSwitch = false;
		if (Hostlower!=null)
			Hostlower.TurnOff();
		if (Hostupper!=null)
			Hostupper.TurnOff();
		try{
			semaphoreHashtable.acquire();
			for (String keys1 : clients.keySet()){
				Hashtable<Integer, ClientConnection> client = clients.get(keys1);
				for (int keys2 : client.keySet()){
					client.get(keys2).TurnOff();
				}
			}
			semaphoreHashtable.release();
		}
		catch(InterruptedException e){}
		//System.out.println("Closed succesfully.");
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
	public static void setTARGETPORT(int intt){
		saveState.TARGETPORT =  intt;
		TARGETPORT =  intt;
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
	public static void setHeartbeatSpace(int _hs){
		saveState.heartbeatSpace = _hs;
		STARTDATE = _hs;
		saveState.serialize();
	}
}
