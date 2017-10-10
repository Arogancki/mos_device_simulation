package mosmessages.profile2;
import mosmessages.MosMessage;
import mossimulator.Model;
import java.util.ArrayList;
import org.w3c.dom.Element;

public class roReadyToAir extends MosMessage{
	protected roReadyToAir() {
		super(Model.getUpperPort());
	}
	//@Override
		public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
			MosMessage.AfterReceiving(message, m);
			
		}
		@Override
		public void AfterSending(){
		}
		@Override
		public void PrepareToSend() {
			Element mos = xmlDoc.getDocumentElement();
			
			Element x = xmlDoc.createElement("x");
			x.appendChild(xmlDoc.createTextNode("x"));
			mos.appendChild(x);
		}
}
