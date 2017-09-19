package mosmessages.profile1;

import mosmessages.MOSMessage;
import mossimulator.Model;
import mossimulator.Model.MessageInfo;

import org.w3c.dom.Element;

public class MOSReqObj extends MOSMessage {
	private String objID = ""; 
	public MOSReqObj() {
		super(Model.getLowerPort());
	}

	// @Override
	public static void AfterReceiving(Model.MessageInfo message){
		MOSMessage.AfterReceiving(message);
		// TODO wyciaganac obiekt z kolekcji i wyslac
	}

	public void AfterSending() {
		MessageInfo response = getResponse();
		if (response.getMosType().toLowerCase().equals(MOSObj.class.getSimpleName().toLowerCase())){
			String objID = response.GetFromXML("objID");
			if (objID != null && objID.equals(objID)){
				System.out.println("Received requested object.");
				// TODO dodac do kolekcji obiketow
			}
		}
		else if (response.getMosType().toLowerCase().equals(MOSACK.class.getSimpleName().toLowerCase())) {
			System.out.println("Object wasn't found.");
		}
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

	public MOSReqObj setObjID(String objID) {
		// max 128 chars
		if (objID.length() > 127)
			objID = objID.substring(0, 128);
		this.objID = objID;
		return this;
	}
}