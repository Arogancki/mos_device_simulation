package mosmessages.profile0;
import java.text.SimpleDateFormat;
import java.util.Date;

import mosmessages.MosMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class Heartbeat extends MosMessage {
	public Heartbeat() {
		super(Model.getLowerPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message){
		MosMessage.AfterReceiving(message);
		if (!expectingForHeartbeat)
			new Heartbeat().Send();
	}
	@Override
	public void AfterSending(){
		expectingForHeartbeat = true;
		Model.MessageInfo recived = getResponse();
		if (recived != null && recived.getMosType().toLowerCase().equals(this.getClass().getSimpleName().toLowerCase()));
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
