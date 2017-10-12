package mosmessages.profile2;
import mosmessages.MosMessage;
import mossimulator.Model;
import java.util.ArrayList;
import org.w3c.dom.Element;

public class roList extends MosMessage{
	private String roID="";
	protected roList() {
		super(Model.getUpperPort());
	}
	public roList setRoID(String roID){
		this.roID=roID;
		return this;
	}
	//@Override
		public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
			MosMessage.AfterReceiving(message, m);
			new mossimulator.RunningOrder(message.GetFromElement(message.getDocument().getChildNodes().item(0), "roList"));
		}
		@Override
		public void AfterSending(){
		}
		@Override
		public void PrepareToSend() {
			Element mos = xmlDoc.getDocumentElement();
			
			if (!this.roID.equals("")){
				Element roList = xmlDoc.createElement("roList");
				mossimulator.RunningOrder.getRunningOrderObj(this.roID).BuildXml(roList, xmlDoc);
				mos.appendChild(roList);
			}
		}
}
