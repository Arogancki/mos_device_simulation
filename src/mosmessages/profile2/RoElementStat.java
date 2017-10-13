package mosmessages.profile2;
import mosmessages.MosMessage;
import mossimulator.Model;
import mossimulator.Story;

import java.time.Instant;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RoElementStat extends MosMessage{
	private mosmessages.defined.RoElementStat roElementStat = null;
	private String roID = "";
	private String storyID = "";
	private String itemID = "";
	private String objID = "";
	private String itemChannel = "";
	private mosmessages.defined.Status status = null;
	private String time = "";
	protected RoElementStat() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		Node roElementStat = mossimulator.Model.MessageInfo.GetFromElement(message.getDocument().getChildNodes().item(0), "roElementStat");
		mosmessages.defined.RoElementStat roElementStatElement = mosmessages.defined.RoElementStat.getFromString(((org.w3c.dom.Element)roElementStat).getAttribute("element"));
		String roID = message.GetFromXML("roID");
		String status = message.GetFromXML("status");
		if (status==null){
			status="";
		}
		String time = message.GetFromXML("time");
		if (time==null){
			time="";
		}
		if (roID!=null){
			if (roElementStatElement==mosmessages.defined.RoElementStat.RO){
				if (mossimulator.RunningOrder.Contains(roID)){
					System.out.println("roElementStat: RO: "+time+": "+roID+" = " + status+ ".");
					new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
				}
				else{
					System.out.println("RO not found.");
					new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
				}
			}
			else if (roElementStatElement==mosmessages.defined.RoElementStat.STORY){
				String storyID = message.GetFromXML("storyID");
				if (storyID==null){
					storyID="";
				}
				if (mossimulator.RunningOrder.Contains(roID)){
					ArrayList<Story> al = mossimulator.RunningOrder.getRunningOrderObj(roID).getStoryArray();
					if (al.contains(storyID)){
						System.out.println("roElementStat: STORY: "+time+": "+roID+"-"+storyID+" = " + status+ ".");
						new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
					}
					else{
						System.out.println("STORY not found.");
						new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
					}
				}
				else{
					System.out.println("RO not found.");
					new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
				}
			}
			else if (roElementStatElement==mosmessages.defined.RoElementStat.ITEM){
				String storyID = message.GetFromXML("storyID");
				if (storyID==null){
					storyID="";
				}
				if (mossimulator.RunningOrder.Contains(roID)){
					Story story = mossimulator.RunningOrder.getRunningOrderObj(roID).getStory(storyID);
					if (story!=null){
						String itemID = message.GetFromXML("itemID");
						if (itemID==null){
							itemID="";
						}
						mossimulator.Item item = story.getItem(itemID);
						if (item!=null){
							String objID = message.GetFromXML("objID");
							if (objID==null){
								objID="";
							}
							String itemChannel = message.GetFromXML("itemChannel");
							if (itemChannel==null){
								itemChannel="";
							}
							System.out.println("roElementStat: STORY: "+time+": "+roID+"-"+storyID+"-"+itemID+" ("+objID+"/"+itemChannel+") "+" = " + status+ ".");
							new RoAck().addRoAckInner(storyID, itemID, objID, mosmessages.defined.Status.OK).setRoID(roID).Send(m);
						}
						else{
							System.out.println("Item not found.");
							new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
						}
					}
					else{
						System.out.println("STORY not found.");
						new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
					}
				}
				else{
					System.out.println("RO not found.");
					new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
				}
			}
			else{
				System.out.println("Not supported option: "+((org.w3c.dom.Element)roElementStat).getAttribute("element") + " - in roElementStat receiving" );
				new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
			}
		}
	}
	@Override
	public void AfterSending(){
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		
		Element roElementStat = xmlDoc.createElement("roElementStat");
		if (this.roElementStat!=null){
			roElementStat.setAttribute("element", this.roElementStat.toString());
		}
		else{
			System.out.println("Warrning: roElementStat:element is not set!");
		}
		mos.appendChild(roElementStat);
		
		Element roID = xmlDoc.createElement("roID");
		roID.appendChild(xmlDoc.createTextNode(this.roID));
		roElementStat.appendChild(roID);
		
		if (!this.storyID.equals("")){
			Element storyID = xmlDoc.createElement("storyID");
			storyID.appendChild(xmlDoc.createTextNode(this.storyID));
			roElementStat.appendChild(storyID);
		}
		
		if (!this.itemID.equals("")){
			Element itemID = xmlDoc.createElement("itemID");
			itemID.appendChild(xmlDoc.createTextNode(this.itemID));
			roElementStat.appendChild(itemID);
		}
		
		if (!this.objID.equals("")){
			Element objID = xmlDoc.createElement("objID");
			objID.appendChild(xmlDoc.createTextNode(this.objID));
			roElementStat.appendChild(objID);
		}
		
		if (!this.itemChannel.equals("")){
			Element itemChannel = xmlDoc.createElement("itemChannel");
			itemChannel.appendChild(xmlDoc.createTextNode(this.itemChannel));
			roElementStat.appendChild(itemChannel);
		}
		
		Element status = xmlDoc.createElement("status");
		if (this.status!=null)
			status.appendChild(xmlDoc.createTextNode(this.status.toString()));
		roElementStat.appendChild(status);
		
		Element time = xmlDoc.createElement("time");
		if (this.time.equals(""))
			setTimeNow();
		time.appendChild(xmlDoc.createTextNode(this.time));
		roElementStat.appendChild(time);
		
	}
	public void setRoElementStat(mosmessages.defined.RoElementStat roElementStat) {
		this.roElementStat = roElementStat;
	}
	public void setRoElementStat(String roElementStat) {
		mosmessages.defined.RoElementStat parsed = mosmessages.defined.RoElementStat.getFromString(roElementStat);
		if (parsed!=null){
			setRoElementStat(parsed);
		}
		else{
			System.out.println("Wrong format - setRoElementStat: excepted "+mosmessages.defined.RoElementStat.values());
		}
	}
	public void setRoID(String roID) {
		this.roID = roID;
	}
	public void setStoryID(String storyID) {
		this.storyID = storyID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	public void setObjID(String objID) {
		this.objID = objID;
	}
	public void setItemChannel(String itemChannel) {
		this.itemChannel = itemChannel;
	}
	public void setStatus(mosmessages.defined.Status status) {
		this.status = status;
	}
	public void setStatus(String status) {
		mosmessages.defined.Status parsed = mosmessages.defined.Status.getFromString(status);
		if (parsed!=null){
			setStatus(parsed);
		}
		else{
			System.out.println("Wrong format - setStatus: excepted "+mosmessages.defined.Status.values());
		}
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setTimeNow(){
		this.time = Instant.now().toString();
	}
}
