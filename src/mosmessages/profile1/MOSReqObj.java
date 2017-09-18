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
		// TODO send description of the object
	}

	public void AfterSending() {
		MessageInfo response = getResponse();
		if (response.getMosType().toLowerCase().equals("mosobj")){
			// TODO recived description of the object 
		}
		else if (response.getMosType().toLowerCase().equals("mosack")) {
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