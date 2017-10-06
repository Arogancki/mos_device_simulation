package mosmessages.profile1;

import java.util.ArrayList;

import mosmessages.MosMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class MosObj extends MosMessage {
	private mossimulator.MosObj mosObj = null;
	
	public MosObj() {
		super(Model.getLowerPort());
	}

	public MosObj setMosObj(mossimulator.MosObj _mosObj){
		mosObj = _mosObj;
		return this;
	}
	
	// @Override
	public static void AfterReceiving(Model.MessageInfo message,ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message,m);
		mossimulator.MosObj newObj = new mossimulator.MosObj(message);
		new MosAck().setStatusDescription(newObj.getObjID()).Send(m);
	}

	public void AfterSending() {
		/*Model.MessageInfo recived = getResponse();
		if (recived == null || !recived.getMosType().toLowerCase().equals(MosAck.class.getSimpleName().toLowerCase())){
			System.out.println("Receiving not acknowledged");
		}*/
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