package mosmessages.profile0;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import mosmessages.MOSMessage;
import mossimulator.Model;

public class Heartbeat extends MOSMessage {
	public Heartbeat() {
		super(Model.getLowerPort());
	}
	//@Override
	public static void AfterReceiving(){
		System.out.println("Heartbeat received.");
		if (!expectingForHeartbeat)
			new Heartbeat().Send();
	}
	@Override
	public void AfterSending(){
		expectingForHeartbeat = true;
		if (getResponse().getMosType()=="hearthbeat");
			expectingForHeartbeat = false;
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
	private static boolean expectingForHeartbeat = false;
}
