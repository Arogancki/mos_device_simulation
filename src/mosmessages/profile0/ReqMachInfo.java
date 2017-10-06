package mosmessages.profile0;
import java.util.ArrayList;

import mosmessages.MosMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class ReqMachInfo extends MosMessage {
	public ReqMachInfo() {
		super(Model.getLowerPort());
	}
	//@Override
		public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
			MosMessage.AfterReceiving(message,m);
			new ListMachInfo().Send(m);
		}
		@Override
		public void AfterSending(){
			
		}
		@Override
		public void PrepareToSend() {
				Element mos = xmlDoc.getDocumentElement();
				
				Element reqMachInfo = xmlDoc.createElement("reqMachInfo");
				mos.appendChild(reqMachInfo);
		}
}
