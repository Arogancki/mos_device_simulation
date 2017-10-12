package mosmessages.profile1;

import java.util.ArrayList;

import mosmessages.MosMessage;
import mosmessages.defined.Status;
import mossimulator.Model;
import mossimulator.Model.MessageInfo;

import org.w3c.dom.Element;

public class MosReqObj extends MosMessage {
	private String objID = ""; 
	public MosReqObj() {
		super(Model.getLowerPort());
	}

	// @Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message,  m);
		String key = message.GetFromXML("objID");
		if (key==null){
			System.out.println("Received coruppted message: no objID - mosreqobj");
		}
		else{
			if (mossimulator.MosObj.Contains(key)){
				new MosObj().setMosObj(mossimulator.MosObj.getMosObj(key)).Send(m);
			}
			else{
				new MosAck().setObjectUID(key).setStatus(Status.OK).setStatusDescription("MOSObj not found").Send(m);
			}
		}
		
	}
	
	public void AfterSending() {
		/*MessageInfo response = getResponse();
		if (response.getMosType().toLowerCase().equals(MosObj.class.getSimpleName().toLowerCase())){
			String objID = response.GetFromXML("objID");
			if (objID != null && objID.equals(objID)){
				System.out.println("Received requested object.");
			}
		}
		else if (response.getMosType().toLowerCase().equals(MosAck.class.getSimpleName().toLowerCase())) {
			System.out.println("Object wasn't found.");
		}*/
	}

	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();

		Element mosReqObj = xmlDoc.createElement("mosReqObj");
		mos.appendChild(mosReqObj);

		Element objID = xmlDoc.createElement("objID");
		objID.appendChild(xmlDoc.createTextNode(getObjID()));
		mosReqObj.appendChild(objID);
	}

	public String getObjID() {
		return objID;
	}

	public MosReqObj setObjID(String objID) {
		// max 128 chars
		if (objID.length() > 127)
			objID = objID.substring(0, 128);
		this.objID = objID;
		return this;
	}
}