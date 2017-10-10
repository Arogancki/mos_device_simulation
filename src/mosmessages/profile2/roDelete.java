package mosmessages.profile2;
import java.util.ArrayList;
import org.w3c.dom.Element;
import mosmessages.MosMessage;
import mossimulator.Model;

public class roDelete extends MosMessage{
	private String roID="";
	protected roDelete() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		String key = message.GetFromXML("roID");
		if (mossimulator.RunningOrder.Contains(key)){
			mossimulator.RunningOrder.remove(key);
		}
		new roAck().Send(m);
	}
	@Override
	public void AfterSending(){
		if (mossimulator.RunningOrder.Contains(roID)){
			mossimulator.RunningOrder.remove(roID);
		}
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		
		Element roDelete = xmlDoc.createElement("roDelete");
		mos.appendChild(roDelete);

		Element roID = xmlDoc.createElement("roID");
		roID.appendChild(xmlDoc.createTextNode(this.roID));
		mos.appendChild(roID);
	}
	public boolean setRoID(String roID){
		if (mossimulator.RunningOrder.Contains(roID)){
			this.roID=roID;
			return true;
		}
		return false;
	}
}
