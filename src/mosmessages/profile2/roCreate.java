package mosmessages.profile2;
import java.util.ArrayList;

import org.w3c.dom.Element;

import mosmessages.MosMessage;
import mossimulator.Model;
import mossimulator.RunningOrder;

public class RoCreate extends MosMessage{
	private String roID="";
	public RoCreate() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		new RunningOrder(Model.MessageInfo.GetFromElement(message.getDocument().getChildNodes().item(0), "roCreate"));
		new RoAck().Send(m);
	}
	@Override
	public void AfterSending(){
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		
		Element roCreate = xmlDoc.createElement("roCreate");
		mos.appendChild(roCreate);
		
		if (!roID.equals("")){
			RunningOrder.getRunningOrderObj(roID).BuildXml(roCreate, xmlDoc);
		}
	}
	public boolean setRoID(String roID){
		if (mossimulator.RunningOrder.Contains(roID)){
			this.roID=roID;
			return true;
		}
		return false;
	}
}
