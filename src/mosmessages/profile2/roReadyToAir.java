package mosmessages.profile2;
import mosmessages.MosMessage;
import mossimulator.Model;

import java.util.ArrayList;

import org.w3c.dom.Element;

public class roReadyToAir extends MosMessage{
	private String roID="";
	public roReadyToAir setRoID(String roID){
		this.roID=roID;
		return this;
	}
	protected roReadyToAir() {
		super(Model.getUpperPort());
	}
	//@Override
		public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
			MosMessage.AfterReceiving(message, m);
			String key = message.GetFromXML("roID");
			if (key!=null && !key.equals("")){
				System.out.println("RO: "+key+" is ready to Air!");
				new roAck().addRoAckInner(mosmessages.defined.Status.OK).Send(m);
			}
			else{
				System.out.println("Received corrupted message - no roID - roDelete");
				new roAck().addRoAckInner(mosmessages.defined.Status.NACK).Send(m);
			}
		}
		@Override
		public void AfterSending(){
		}
		@Override
		public void PrepareToSend() {
			Element mos = xmlDoc.getDocumentElement();
			
			Element roReadyToAir = xmlDoc.createElement("roReadyToAir");
			roReadyToAir.appendChild(xmlDoc.createTextNode(""));
			mos.appendChild(roReadyToAir);
			
			if (!this.roID.equals("")){
				Element roID = xmlDoc.createElement("roID");
				roID.appendChild(xmlDoc.createTextNode(this.roID));
				roReadyToAir.appendChild(roID);
			}
			
			Element roAir = xmlDoc.createElement("roAir");
			roAir.appendChild(xmlDoc.createTextNode("READY"));
			roReadyToAir.appendChild(roAir);
			
		}
}
