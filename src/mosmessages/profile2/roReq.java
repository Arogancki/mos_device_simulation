package mosmessages.profile2;
import java.util.ArrayList;
import org.w3c.dom.Element;

import mosmessages.MosMessage;
import mossimulator.Model;

public class roReq extends MosMessage{
	private String roID="";
	public roReq setRoReq(String roID){
		this.roID = roID;
		return this;
	}
	protected roReq() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		String requestedRoID = message.GetFromXML("roID");
		if (requestedRoID!=null && !requestedRoID.equals("")){
			if (mossimulator.RunningOrder.Contains(requestedRoID)){
				new roList().setRoID(requestedRoID).Send(m);
			}
			else{
				new roAck().setRoID(requestedRoID).addRoAckInner(mosmessages.defined.Status.NACK).Send(m);
			}
		}else{
			System.out.println("Error during parsing the message! no roID - Received roReq");
			new roAck().setRoID(requestedRoID).addRoAckInner(mosmessages.defined.Status.NACK).Send(m);
		}
	}
	@Override
	public void AfterSending(){
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		
		Element roReq = xmlDoc.createElement("roReq");
		mos.appendChild(roReq);
		
		if (!this.roID.equals("")){
			Element roID = xmlDoc.createElement("roID");
			roID.appendChild(xmlDoc.createTextNode(this.roID));
			roReq.appendChild(roID);
		}
	}
}
