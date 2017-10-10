package mosmessages.profile3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mosmessages.MosMessage;
import mosmessages.profile0.Heartbeat;
import mossimulator.Model;

import org.w3c.dom.Element;

public class roReqAll extends MosMessage{
	public roReqAll() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		new roListAll().Send(m);
	}
	@Override
	public void AfterSending(){
		
	}
	@Override
	public void PrepareToSend() {
			Element mos = xmlDoc.getDocumentElement();
			
			Element roReqAll = xmlDoc.createElement("roReqAll");
			mos.appendChild(roReqAll);		
	}
}
