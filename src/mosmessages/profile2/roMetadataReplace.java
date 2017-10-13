package mosmessages.profile2;
import mosmessages.MosMessage;
import mosmessages.defined.Trigger;
import mossimulator.Model;
import mossimulator.RunningOrder;

import java.util.ArrayList;

import org.w3c.dom.Element;

public class RoMetadataReplace extends MosMessage{
	private String roID = "";
	private String roSlug = "";
	private String roChannel = "";
	private String roEdStart = "";
	private String roEdDur = "";
	private mosmessages.defined.Trigger roTrigger = null;
	private int trigerChainedValue = 0;
	private String macroIn = "";
	private String macroOut = "";
	public RoMetadataReplace() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		String key = message.GetFromXML("roID");
		if (key!=null && mossimulator.RunningOrder.Contains(key)){
			mossimulator.RunningOrder ro = mossimulator.RunningOrder.getRunningOrderObj(key);
			String roSlug = message.GetFromXML("roSlug");
			String roChannel = message.GetFromXML("roChannel");
			String roEdStart = message.GetFromXML("roEdStart");
			String roEdDur = message.GetFromXML("roEdDur");
			String roTrigger = message.GetFromXML("roTrigger");
			String macroIn = message.GetFromXML("macroIn");
			String macroOut = message.GetFromXML("macroOut");
			//String mosExternalMetadata = message.GetFromXML("mosExternalMetadata");
			if (roSlug!=null){
				ro.setRoSlug(roSlug);
			}
			if (roChannel!=null){
				ro.setRoChannel(roChannel);
			}
			if (roEdStart!=null){
				ro.setRoEdStart(roEdStart);
			}
			if (roEdDur!=null){
				ro.setRoEdDur(roEdDur);
			}
			if (roTrigger!=null){
				ro.setRoTrigger(roTrigger);
			}
			if (macroIn!=null){
				ro.setMacroIn(macroIn);
			}
			if (macroOut!=null){
				ro.setMacroOut(macroOut);
			}
			new RoAck().setRoID(key).addRoAckInner(mosmessages.defined.Status.OK).Send(m);
		}
		else{
			new RoAck().setRoID(key).addRoAckInner(mosmessages.defined.Status.NACK).Send(m);
		}
	}
	@Override
	public void AfterSending(){
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		
		Element roMetadataReplace = xmlDoc.createElement("roMetadataReplace");
		mos.appendChild(roMetadataReplace);
		
		Element roID = xmlDoc.createElement("roID");
		roID.appendChild(xmlDoc.createTextNode(this.roID));
		roMetadataReplace.appendChild(roID);
		
		Element roSlug = xmlDoc.createElement("roSlug");
		roSlug.appendChild(xmlDoc.createTextNode(this.roSlug));
		roMetadataReplace.appendChild(roSlug);
		
		if (!this.roChannel.equals("")){
			Element roChannel = xmlDoc.createElement("roChannel");
			roChannel.appendChild(xmlDoc.createTextNode(this.roChannel));
			roMetadataReplace.appendChild(roChannel);
		}
		
		if (!this.roEdStart.equals("roEdStart")){
			Element roEdStart = xmlDoc.createElement("roEdStart");
			roEdStart.appendChild(xmlDoc.createTextNode(this.roEdStart));
			roMetadataReplace.appendChild(roEdStart);
		}
		
		if (!this.roEdDur.equals("")){
			Element roEdDur = xmlDoc.createElement("roEdDur");
			roEdDur.appendChild(xmlDoc.createTextNode(this.roEdDur));
			roMetadataReplace.appendChild(roEdDur);
		}
		
		if (this.roTrigger!=null){
			Element roTrigger = xmlDoc.createElement("");
			roTrigger.appendChild(xmlDoc.createTextNode(this.getItemTrigger()));
			roMetadataReplace.appendChild(roTrigger);
		}
		
		if (!this.macroIn.equals("")){
			Element macroIn = xmlDoc.createElement("macroIn");
			macroIn.appendChild(xmlDoc.createTextNode(this.macroIn));
			roMetadataReplace.appendChild(macroIn);
		}
		
		if (!this.macroOut.equals("")){
			Element macroOut = xmlDoc.createElement("macroOut");
			macroOut.appendChild(xmlDoc.createTextNode(this.macroOut));
			roMetadataReplace.appendChild(macroOut);
		}
	}
	public String getItemTrigger() {
		if (this.roTrigger==mosmessages.defined.Trigger.CHAINED)
			return roTrigger.toString()+" "+(this.trigerChainedValue>=0?"+"+this.trigerChainedValue:this.trigerChainedValue);
		return roTrigger.toString();
	}
	public RoMetadataReplace setRoID(String roID) {
		this.roID = roID;
		return this;
	}
	public RoMetadataReplace setRoSlug(String roSlug) {
		this.roSlug = roSlug;
		return this;
	}
	public RoMetadataReplace setRoChannel(String roChannel) {
		this.roChannel = roChannel;
		return this;
	}
	public RoMetadataReplace setRoEdStart(String roEdStart) {
		this.roEdStart = roEdStart;
		return this;
	}
	public RoMetadataReplace setRoEdDur(String roEdDur) {
		this.roEdDur = roEdDur;
		return this;
	}
	public RoMetadataReplace setRoTrigger(mosmessages.defined.Trigger roTrigger) {
		this.roTrigger = roTrigger;
		return this;
	}
	public RoMetadataReplace setRoTrigger(String roTrigger) {
		if (roTrigger.startsWith("CHAINED")){
			String[] chainedArray = roTrigger.split(" ");
			if (chainedArray.length >= 2){
				chainedArray[1] = chainedArray[1].startsWith("+") ? chainedArray[1].substring(1) : chainedArray[1];
				mosmessages.defined.Trigger backup = this.roTrigger;
				try {
					setRoTrigger(mosmessages.defined.Trigger.CHAINED);
					setTrigerChainedvalue(Integer.valueOf(chainedArray[1]));
				}
				catch(NumberFormatException e){
					setRoTrigger(backup);
					System.out.println("Wrong format - setRoTrigger: excepted "+mosmessages.defined.Trigger.values());
				}
			}
			else{
				System.out.println("Wrong format - setRoTrigger: excepted "+mosmessages.defined.Trigger.values());
			}
		}
		else{
			Trigger parsed = mosmessages.defined.Trigger.getFromString(roTrigger);
			if (parsed!=null)
				setRoTrigger(parsed);
			else
				System.out.println("Wrong format - setRoTrigger: excepted "+mosmessages.defined.Trigger.values());
		}
		return this;
	}
	public RoMetadataReplace setMacroIn(String macroIn) {
		this.macroIn = macroIn;
		return this;
	}
	public RoMetadataReplace setMacroOut(String macroOut) {
		this.macroOut = macroOut;
		return this;
	}
	public RoMetadataReplace setTrigerChainedvalue(int trigerChainedvalue) {
		if (this.roTrigger==mosmessages.defined.Trigger.CHAINED){
			this.trigerChainedValue = trigerChainedvalue;
		}
		else{
			System.out.println("ItemTrigger has to be CHAINED.");
		}
		return this;
	}
}
