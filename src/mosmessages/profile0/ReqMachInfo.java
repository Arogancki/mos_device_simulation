package mosmessages.profile0;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

import mosmessages.MOSMessage;
import mossimulator.Model;

public class ReqMachInfo extends MOSMessage {
	public ReqMachInfo() {
		super(Model.getLowerPort());
	}
	//@Override
		public static void AfterReceiving(Model.MessageInfo message){
			System.out.println("Received ReqMachInfo.\nAutoresponding: ListMachInfo.");
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
