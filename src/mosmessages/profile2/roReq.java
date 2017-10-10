package mosmessages.profile2;
import java.util.ArrayList;
import org.w3c.dom.Element;

import mosmessages.MosMessage;
import mossimulator.Model;

public class roReq extends MosMessage{
	protected roReq() {
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
			
			Element x = xmlDoc.createElement("x");
			x.appendChild(xmlDoc.createTextNode("x"));
			mos.appendChild(x);
		}
}
