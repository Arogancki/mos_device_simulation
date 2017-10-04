package mosmessages.profile1;

import mosmessages.MosMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class MosListAll extends MosMessage {
	
	public MosListAll() {
		super(Model.getLowerPort());
	}
	// @Override
	public static void AfterReceiving(Model.MessageInfo message,Model.Port _port){
		MosMessage.AfterReceiving(message,_port);
		String[] objs = message.getString().split("<mosObj>");
		for (int i=1; i<objs.length; i++){
			new mossimulator.MosObj(new mossimulator.Model.MessageInfo(mossimulator.Model.MessageInfo.Direction.IN, "<mosObj>"+objs[i].split("</mosObj>")[0]+"</mosObj>", true));
		}
		System.out.println(objs.length-1 + " objects added.");
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
		
		for (String key : mossimulator.MosObj.GetKeys()){
			Element mosObj = xmlDoc.createElement("mosObj");
			mosListAll.appendChild(mosObj);
			mossimulator.MosObj.getMosObj(key).BuildXml(mosObj, xmlDoc);
		}
	}
}