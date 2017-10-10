package mosmessages.profile3;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mosmessages.MosMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class roListAll extends MosMessage{
	public roListAll() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
	}
	@Override
	public void AfterSending(){
		
	}
	@Override
	public void PrepareToSend() {
			Element mos = xmlDoc.getDocumentElement();
			
			Element roListAll = xmlDoc.createElement("roListAll");
			mos.appendChild(roListAll);
	}
}
