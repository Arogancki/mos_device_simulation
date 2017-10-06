package mosmessages.profile0;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import mosmessages.MosMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class Heartbeat extends MosMessage {
	private static boolean expectingReply = false; // jesli wiadomosc jest zainicjowana ze strony clienta zmienic na true
	public Heartbeat() {
		super(Model.getLowerPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		if (!expectingReply){
			new Heartbeat().Send(m);
		}
	}
	@Override
	public void AfterSending(){
	}
	public Heartbeat activeExpectingReply() {
		expectingReply=true;
		return this;
	}
	@Override
	public void PrepareToSend() {
			Element mos = xmlDoc.getDocumentElement();
			
			Element heartbeat = xmlDoc.createElement("heartbeat");
			mos.appendChild(heartbeat);
			
		    Date now = new Date();
		    Element time = xmlDoc.createElement("time");
		    time.appendChild(xmlDoc.createTextNode(new SimpleDateFormat("yyyy-MM-dd").format(now) 
					+ "T" + new SimpleDateFormat("hh:mm:ss").format(now)));
		    heartbeat.appendChild(time);
	}
}
