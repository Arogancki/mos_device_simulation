package mosmessages.profile2;
import java.util.ArrayList;

import org.w3c.dom.Element;

import mosmessages.MosMessage;
import mossimulator.Model;

public class RoAck extends MosMessage{
	class RoStatus{
		private String storyID = "";
		private String itemID = "";
		private String objID = "";
		private String itemChannel = "";
		private mosmessages.defined.Status status = null;
		public RoStatus(String storyID, String itemID, String objID, mosmessages.defined.Status status){
			this.storyID=storyID;
			this.itemID=itemID;
			this.objID=objID;
			this.status=status;
		}
		public RoStatus(mosmessages.defined.Status status){
			this.status = status;
		}
		public RoStatus setItemChannel(String itemChannel){
			this.itemChannel = itemChannel;
			return this;
		}
	}
	private ArrayList<RoStatus> roStatuses = new ArrayList<RoStatus>();
	private String roID="";
	public String getRoID(){
		return this.roID;
	}
	public RoAck setRoID(String roID){
		if (roID.length()<=128){
			this.roID=roID;
		}
		return this;
	}
	public RoAck addRoAckInner(String storyID, String itemID, String objID, mosmessages.defined.Status status, String itemChannel){
		roStatuses.add(new RoStatus(storyID, itemID, objID, status).setItemChannel(itemChannel));
		return this;
	}
	public RoAck addRoAckInner(String storyID, String itemID, String objID, mosmessages.defined.Status status){
		roStatuses.add(new RoStatus(storyID, itemID, objID, status));
		return this;
	}
	public RoAck addRoAckInner(mosmessages.defined.Status status){
		roStatuses.add(new RoStatus(status));
		return this;
	}
	public RoAck() {
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
		
		Element roAck = xmlDoc.createElement("roAck");
		mos.appendChild(roAck);
		
		if (!this.roID.equals("")){
			Element roID = xmlDoc.createElement("roID");
			roID.appendChild(xmlDoc.createTextNode(this.roID));
			roAck.appendChild(roID);
		}
		
		Element roStatusElement = xmlDoc.createElement("roStatus");
		roAck.appendChild(roStatusElement);
		
		for (RoStatus roStatus : roStatuses){
			
			Element storyID = xmlDoc.createElement("storyID");
			storyID.appendChild(xmlDoc.createTextNode(roStatus.storyID));
			roStatusElement.appendChild(storyID);
			
			Element itemID = xmlDoc.createElement("itemID");
			itemID.appendChild(xmlDoc.createTextNode(roStatus.itemID));
			roStatusElement.appendChild(itemID);
			
			Element objID = xmlDoc.createElement("objID");
			objID.appendChild(xmlDoc.createTextNode(roStatus.objID));
			roStatusElement.appendChild(objID);
			
			if (!roStatus.itemChannel.equals("")){
				Element itemChannel = xmlDoc.createElement("itemChannel");
				itemChannel.appendChild(xmlDoc.createTextNode(roStatus.itemChannel));
				roStatusElement.appendChild(itemChannel);	
			}
			
			Element status = xmlDoc.createElement("status");
			status.appendChild(xmlDoc.createTextNode(roStatus.status.toString()));
			roStatusElement.appendChild(status);
		}
	}
}
