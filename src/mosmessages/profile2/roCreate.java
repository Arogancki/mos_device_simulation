package mosmessages.profile2;
import java.util.ArrayList;

import org.w3c.dom.Element;

import mosmessages.MosMessage;
import mossimulator.Model;
import mossimulator.RunningOrder;

public class roCreate extends MosMessage{
	private String roID="";
	protected roCreate() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		new RunningOrder(Model.MessageInfo.GetFromElement(message.getDocument().getChildNodes().item(0), "roCreate"));
		new roAck().Send(m);
	}
	@Override
	public void AfterSending(){
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		
		Element roCreate = xmlDoc.createElement("roCreate");
		mos.appendChild(roCreate);
		
		if (roID!=""){
			RunningOrder ro = RunningOrder.getRunningOrderObj(roID);
			ro.BuildXml(roCreate, xmlDoc);
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
