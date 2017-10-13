package mosmessages.profile2;
import java.util.ArrayList;
import org.w3c.dom.Element;
import mosmessages.MosMessage;
import mossimulator.Model;

public class RoDelete extends MosMessage{
	private String roID="";
	protected RoDelete() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		String key = message.GetFromXML("roID");
		if (key!=null && mossimulator.RunningOrder.Contains(key)){
				new RoAck().addRoAckInner(mosmessages.defined.Status.OK).Send(m);
				mossimulator.RunningOrder.remove(key);
		}
		else{
			System.out.println("Received corrupted message - no roID - roDelete");
			new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).Send(m);
		}
	}
	@Override
	public void AfterSending(){
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		
		Element roDelete = xmlDoc.createElement("roDelete");
		mos.appendChild(roDelete);

		if (!this.roID.equals("")){
			Element roID = xmlDoc.createElement("roID");
			roID.appendChild(xmlDoc.createTextNode(this.roID));
			roDelete.appendChild(roID);
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
