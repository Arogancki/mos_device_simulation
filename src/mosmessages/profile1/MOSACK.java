package mosmessages.profile1;
import org.w3c.dom.Element;

import mosmessages.MOSMessage;
import mossimulator.Model;

public class MOSACK extends MOSMessage {
	
	private String objectUID = "";
	private String objRev = "";
	private String StatusDescription = "";
	private Status status = Status.ACK;
	public enum Status {
		ACK ("ACK"),
		OK ("OK"),
		UPDATED ("UPDATED"),
		MOVED ("MOVED"),
		BUSY  ("BUSY "),
		DELETED ("DELETED"),
		NCS_CTRL ("NCS CTRL"),
		MANUAL_CTRL ("MANUAL CTRL"),
		READY ("READY"),
		NOT_READY ("NOT READY"),
		PLAY ("PLAY"),
		STOP ("STOP");
		
		private final String status;
		private Status(String _status){
			status = _status;
		}
		public String toString(){
			return status;
		}
	}
	
	public MOSACK() {
		super(Model.getLowerPort());
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();

		Element mosAck = xmlDoc.createElement("mosAck");
		mos.appendChild(mosAck);

		Element objID = xmlDoc.createElement("objID");
		objID.appendChild(xmlDoc.createTextNode(getObjectUID()));
		mosAck.appendChild(objID);

		Element objRev = xmlDoc.createElement("objRev");
		objRev.appendChild(xmlDoc.createTextNode(getObjRev()));
		mosAck.appendChild(objID);

		Element status = xmlDoc.createElement("status");
		status.appendChild(xmlDoc.createTextNode(getStatus().toString()));
		mosAck.appendChild(status);
		
		Element statusDescription = xmlDoc.createElement("statusDescription");
		statusDescription.appendChild(xmlDoc.createTextNode(getStatusDescription()));
		mosAck.appendChild(statusDescription);
	}
	public static void AfterReceiving(Model.MessageInfo message){
		MOSMessage.AfterReceiving(message);
	}
	public String getObjectUID() {
		return objectUID;
	}
	public MOSACK setObjectUID(String objectUID) {
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
	public MOSACK setObjRev(String objRev) {
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
	public MOSACK setStatusDescription(String statusDescription) {
		// 128 chars max
		if (statusDescription.length() > 127){
			statusDescription = statusDescription.substring(0, 128);
		}
		StatusDescription = statusDescription;
		return this;
	}
	public Status getStatus() {
		return status;
	}
	public MOSACK setStatus(Status status) {
		this.status = status;
		return this;
	}
}