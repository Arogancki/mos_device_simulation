package mossimulator;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import connection.Connection;
import mosmessages.MOSMessage;

public class Model {
	private static boolean powerSwitch = true;
	public static String TARGETHOST = "10.105.250.217";
	public static String MOSID = "Mos Simulator";
	public static String NCSID = "Mos Simulator";
	public static int retransmission = 3;
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
			this(direction, _message, DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(_message));
		}
		public Document getDocument(){return xmlDoc;}
		public String getString(){return message;}
		@Override
		public String toString(){
			return ( direction==Direction.IN ? "<- " : "-> " ) + time + "::" + message;
		}
	}
	public static class Port{
		private int number;
		private Thread connection;
		Port(int _number){
			number = _number;
			connection = new Connection(this);
			connection.start();
		}
		public int getPortNumber(){return number;}
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
