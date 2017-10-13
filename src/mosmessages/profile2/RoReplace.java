package mosmessages.profile2;
import mosmessages.MosMessage;
import mossimulator.Model;
import java.util.ArrayList;
import org.w3c.dom.Element;

public class RoReplace extends MosMessage{
	private String roID = "";
	public RoReplace setRoID(String roID){
		this.roID = roID;
		return this;
	}
	protected RoReplace() {
		super(Model.getUpperPort());
	}
	//@Override
		public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
			MosMessage.AfterReceiving(message, m);		
			String requestedRoID = message.GetFromXML("roID");
			if (requestedRoID!=null && !requestedRoID.equals("")){
				new mossimulator.RunningOrder(message.GetFromElement(message.getDocument().getChildNodes().item(0), "roReplace"));
				new RoAck().setRoID(requestedRoID).addRoAckInner(mosmessages.defined.Status.OK).Send(m);
			}else{
				System.out.println("Error during parsing the message! no roID - Received roReplace");
				new RoAck().setRoID(requestedRoID).addRoAckInner(mosmessages.defined.Status.NACK).Send(m);
			}
		}
		@Override
		public void AfterSending(){
		}
		@Override
		public void PrepareToSend() {
			Element mos = xmlDoc.getDocumentElement();
			
			if (!this.roID.equals("")){
				Element roReplace = xmlDoc.createElement("roReplace");
				mossimulator.RunningOrder.getRunningOrderObj(this.roID).BuildXml(roReplace, xmlDoc);
				mos.appendChild(roReplace);
			}
		}
}
