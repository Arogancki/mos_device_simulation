package mosmessages.profile0;
import mosmessages.MOSMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class ReqMachInfo extends MOSMessage {
	public ReqMachInfo() {
		super(Model.getLowerPort());
	}
	//@Override
		public static void AfterReceiving(Model.MessageInfo message){
			MOSMessage.AfterReceiving(message);
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
