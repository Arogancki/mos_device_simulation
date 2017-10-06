package mosmessages.profile1;
import java.util.ArrayList;

import org.w3c.dom.Element;

import mosmessages.MosMessage;
import mossimulator.Model;

public class MosAck extends MosMessage {
	
	private String objectUID = "";
	private String objRev = "";
	private String StatusDescription = "";
	private mosmessages.defined.Status status = mosmessages.defined.Status.OK;
	
	public MosAck() {
		super(Model.getLowerPort());
	}
	@Override
	public void PrepareToSend(){
		Element mos = xmlDoc.getDocumentElement();

		Element mosAck = xmlDoc.createElement("mosAck");
		mos.appendChild(mosAck);

		Element objID = xmlDoc.createElement("objID");
		objID.appendChild(xmlDoc.createTextNode(getObjectUID()));
		mosAck.appendChild(objID);

		Element objRev = xmlDoc.createElement("objRev");
		objRev.appendChild(xmlDoc.createTextNode(getObjRev()));
		mosAck.appendChild(objRev);

		Element status = xmlDoc.createElement("status");
		status.appendChild(xmlDoc.createTextNode(getStatus().toString()));
		mosAck.appendChild(status);
		
		Element statusDescription = xmlDoc.createElement("statusDescription");
		statusDescription.appendChild(xmlDoc.createTextNode(getStatusDescription()));
		mosAck.appendChild(statusDescription);
	}
	public static void AfterReceiving(Model.MessageInfo message,ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
	}
	@Override
	public void AfterSending(){
		
	}
	public String getObjectUID() {
		return objectUID;
	}
	public MosAck setObjectUID(String objectUID) {
		// 128 chars max
		if (objectUID.length() > 127){
			objectUID = objectUID.substring(0, 128);
		}
		this.objectUID = objectUID;
		return this;
	}
	public String getObjRev() {
		return objRev;
	}
	public MosAck setObjRev(String objRev) {
		int toInt;
		try {
			toInt = Integer.parseInt(objRev);
			if (toInt > 999)
				toInt = 999;
			this.objRev = Integer.toString(toInt);
		}
		catch (NumberFormatException e){
			this.objRev = "";
		}
		return this;
	}
	public String getStatusDescription() {
		return StatusDescription;
	}
	public MosAck setStatusDescription(String statusDescription) {
		// 128 chars max
		if (statusDescription.length() > 127){
			statusDescription = statusDescription.substring(0, 128);
		}
		StatusDescription = statusDescription;
		return this;
	}
	public mosmessages.defined.Status getStatus() {
		return status;
	}
	public MosAck setStatus(mosmessages.defined.Status status) {
		this.status = status;
		return this;
	}
}