package mossimulator;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;

import mosmessages.MOSMessage;
import mosmessages.profile0.Heartbeat;
import mosmessages.profile0.ListMachInfo;
import mosmessages.profile0.ReqMachInfo;
import mosmessages.profile1.MOSACK;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import connection.Connection;

public class Model {
	private static boolean powerSwitch = true;
	public static int SECTOWAIT = 3;
	public static String TARGETHOST = "10.105.250.217";
	public static String MOSID = "Mos Simulator";
	public static String NCSID = "Mos Simulator";
	public static int RETRANSMISSON = 3;
	public static long STARTDATE = System.currentTimeMillis();
	private static int messageID = 0;
	private static Port lower = new Port(10540);
	private static Port upper = new Port(10541);
	public static Port getLowerPort(){return lower;};
	public static Port getUpperPort(){return upper;};
	public static LinkedList<MessageInfo> messages = new LinkedList<MessageInfo>();
	public static class MessageInfo{
		public enum Direction{
			IN,OUT;
		}
		private Direction direction;
		private String time;
		private String message;
		private Document xmlDoc;
		public MessageInfo(Direction _direction, String _message, Document _xmlDoc){
			time =  (new SimpleDateFormat("yy/MM/dd HH:mm:ss.SSS")).format(new Date());
			message = _message;
			xmlDoc = _xmlDoc;
			messages.add(this);
			direction = _direction;
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
		public void CallReceiveFunction(){
			switch (getMosType().toLowerCase()) {
			case "heartbeat":
				Heartbeat.AfterReceiving(this);
            	break;
			case "reqmachinfo":
				ReqMachInfo.AfterReceiving(this);
            	break;
			case "listmachinfo":
				ListMachInfo.AfterReceiving(this);
            	break;
			case "mosack":
				MOSACK.AfterReceiving(this);
            	break;
			default:
				System.out.println("Unsupported MOS message recived.");
				break;
			}
		}
		@Override
		public String toString(){
			return time + ( direction==Direction.IN ? " <- " : " -> " ) + getMosType() + " " + message;
		}
	}
	public static class Port{
		private int number;
		private Thread connection;
		Port(int _number){
			number = _number;
			connection = new Connection(this);
			//connection.start();
		}
		public int getPortNumber(){return number;}
		public String GetMessage(){
			return ((Connection)connection).GetFromSocket();
		}
		public boolean Send(MOSMessage message){
			return ((Connection)connection).Send(message);
		}
	}
	public static int takeMessageId(){
		return ++messageID;
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
}
