package mosmessages.profile1;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mosmessages.MosMessage;
import mossimulator.Model;

public class MosListAll extends MosMessage {
	
	public MosListAll() {
		super(Model.getLowerPort());
	}

	// @Override
	public static void AfterReceiving(Model.MessageInfo message){
		MosMessage.AfterReceiving(message);
		NodeList nodeList = message.getDocument().getElementsByTagName("mosListAll");
		for (int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				// wyciagac wartosci i wrzucac do stworzonego mosa przez set
				String objID = ((Element) node).getElementsByTagName("objID").item(0).getFirstChild().getTextContent();
				if (objID.length()<=0){
					System.out.print("Counldn't receive MOSObj");
					break;
				}
				
			}
		}
	}

	public void AfterSending() {
		Model.MessageInfo recived = getResponse();
		if (recived == null || !recived.getMosType().toLowerCase().equals(MosAck.class.getSimpleName().toLowerCase())){
			System.out.println("Receiving not acknowledged");
		}
	}

	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		Element mosListAll = xmlDoc.createElement("mosListAll");
		mos.appendChild(mosListAll);
		
		for (String key : mossimulator.MosObj.GetKeys()) {
			Element mosObj = xmlDoc.createElement("mosObj");
			mosListAll.appendChild(mosObj);
			mossimulator.MosObj.getMosObj(key).BuildXml(mosObj, xmlDoc);
		}
	}
}