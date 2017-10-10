package mosmessages.profile2;
import java.util.ArrayList;

import org.w3c.dom.Element;

import mosmessages.MosMessage;
import mossimulator.Model;

public class roAck extends MosMessage{
	class RoAckInner{
		private String storyID = "";
		private String itemID = "";
		private String objID = "";
		private String itemChannel = "";
		private mosmessages.defined.Status status = null;
		public RoAckInner(String storyID, String itemID, String objID, mosmessages.defined.Status status){
			this.storyID=storyID;
			this.itemID=itemID;
			this.objID=objID;
			this.status=status;
		}
		public RoAckInner setItemChannel(String itemChannel){
			this.itemChannel = itemChannel;
			return this;
		}
	}
	private ArrayList<RoAckInner> roAckInners = new ArrayList<RoAckInner>();
	private String roID="";
	private String roStatus="";
	public String getRoID(){
		return this.roID;
	}
	public String getRoStatus(){
		return this.roStatus;
	}
	public roAck setRoID(String roID){
		if (roID.length()<=128){
			this.roID=roID;
		}
		return this;
	}
	public roAck setRoStatus(String roStatus){
		if (roStatus.length()<=128){
			this.roStatus=roStatus;
		}
		return this;
	}
	public void addRoAckInner(String storyID, String itemID, String objI, mosmessages.defined.Status status){
		roAckInners.add(new RoAckInner(storyID, itemID, objI, status));
	}
	public void addAckInner(String storyID, String itemID, String objI, mosmessages.defined.Status status, String itemChannel){
		roAckInners.add(new RoAckInner(storyID, itemID, objI, status).setItemChannel(itemChannel));
	}
	protected roAck() {
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
		
		Element roID = xmlDoc.createElement("roID");
		roID.appendChild(xmlDoc.createTextNode(this.roID));
		roAck.appendChild(roID);
		
		Element roStatus = xmlDoc.createElement("roStatus");
		roStatus.appendChild(xmlDoc.createTextNode(this.roStatus));
		roAck.appendChild(roStatus);
		
		for (RoAckInner roAckInner : roAckInners){
			Element storyID = xmlDoc.createElement("storyID");
			storyID.appendChild(xmlDoc.createTextNode(roAckInner.storyID));
			roAck.appendChild(storyID);
			
			Element itemID = xmlDoc.createElement("itemID");
			itemID.appendChild(xmlDoc.createTextNode(roAckInner.itemID));
			roAck.appendChild(itemID);
			
			Element objID = xmlDoc.createElement("objID");
			objID.appendChild(xmlDoc.createTextNode(roAckInner.objID));
			roAck.appendChild(objID);
			
			if (!roAckInner.itemChannel.equals("")){
				Element itemChannel = xmlDoc.createElement("itemChannel");
				itemChannel.appendChild(xmlDoc.createTextNode(roAckInner.itemChannel));
				roAck.appendChild(itemChannel);	
			}
			
			Element status = xmlDoc.createElement("status");
			status.appendChild(xmlDoc.createTextNode(roAckInner.status.toString()));
			roAck.appendChild(status);
		}
	}
}
