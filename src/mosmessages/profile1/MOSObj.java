package mosmessages.profile1;

import mosmessages.MOSMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class MOSObj extends MOSMessage {
	private mossimulator.MosObj mosObj = null;
	
	public MOSObj() {
		super(Model.getLowerPort());
	}

	public MOSObj setMosObj(mossimulator.MosObj _mosObj){
		mosObj = _mosObj;
		return this;
	}
	
	// @Override
	public static void AfterReceiving(Model.MessageInfo message){
		MOSMessage.AfterReceiving(message);
		mossimulator.MosObj newObj = new mossimulator.MosObj(message);
		new MOSACK().setStatusDescription(newObj.getObjID()).SendOnOpenSocket();
	}

	public void AfterSending() {
		Model.MessageInfo recived = getResponse();
		if (recived == null || !recived.getMosType().toLowerCase().equals(MOSACK.class.getSimpleName().toLowerCase())){
			System.out.println("Receiving not acknowledged");
		}
	}

	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();

		Element e_mosObj = xmlDoc.createElement("mosObj");
		mos.appendChild(e_mosObj);

		if (this.mosObj != null){
			mosObj.BuildXml(e_mosObj, xmlDoc);
		}
		
	}
}