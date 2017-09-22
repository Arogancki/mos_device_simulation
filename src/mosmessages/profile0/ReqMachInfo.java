package mosmessages.profile0;
import mosmessages.MosMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class ReqMachInfo extends MosMessage {
	public ReqMachInfo() {
		super(Model.getLowerPort());
	}
	//@Override
		public static void AfterReceiving(Model.MessageInfo message){
			MosMessage.AfterReceiving(message);
			new ListMachInfo().Send();
		}
		@Override
		public void AfterSending(){
			getResponse();
		}
		@Override
		public void PrepareToSend() {
				Element mos = xmlDoc.getDocumentElement();
				
				Element reqMachInfo = xmlDoc.createElement("reqMachInfo");
				mos.appendChild(reqMachInfo);
		}
}
