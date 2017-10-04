package mosmessages.profile0;
import java.text.SimpleDateFormat;
import java.util.Date;

import mosmessages.MosMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class Heartbeat extends MosMessage {
	private static boolean expectingForHeartbeat = true;
	public Heartbeat() {
		super(Model.getLowerPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, Model.Port _port){
		MosMessage.AfterReceiving(message, _port);
		if (expectingForHeartbeat){
			expectingForHeartbeat=false;
			new Heartbeat().setPort(_port).Send();
			expectingForHeartbeat=true;
		}
	}
	@Override
	public void AfterSending(){
		if (expectingForHeartbeat){
			expectingForHeartbeat = false;
			Model.MessageInfo recived = getResponse();
			if (recived != null && recived.getMosType().toLowerCase().equals(this.getClass().getSimpleName().toLowerCase()))
				System.out.println("Connection is ok.");
			expectingForHeartbeat = true;
		}
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
