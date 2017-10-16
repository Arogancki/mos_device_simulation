package mossimulator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;

import mosmessages.defined.Trigger;
import mosmessages.profile2.RoMetadataReplace;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RunningOrder implements Serializable{
	public static void init(){
		new RunningOrder(true);
	}
	private RunningOrder(boolean init){
		
	}
	private static final long serialVersionUID = 1L;
	static private Hashtable<String, RunningOrder> runningOrders = null;
	private static final String ROFILE = "RunningOrders.ser";
	static {
		try (FileInputStream fis = new FileInputStream(ROFILE);
	            ObjectInputStream ois = new ObjectInputStream(fis)){
			runningOrders = (Hashtable<String, RunningOrder>) ois.readObject();
        }
		catch(ClassNotFoundException|IOException c){
        	  System.out.println("Warrning: RunningOrders save file wasn't read. Creating a new, empty save file.");
        	  runningOrders = new Hashtable<String, RunningOrder>();
        	  Serialize();
        }
	}
	private static long lastUnique = 1;
	private String roID = "";
	private String roSlug = "";
	private String roChannel = "";
	private String roEdStart = "";
	private String roEdDur = "";
	private mosmessages.defined.Trigger roTrigger = null;
	private int trigerChainedValue=0;
	private String macroIn = "";
	private String macroOut = "";
	//mosExternalMetadata*
	private ArrayList<Story> stories = new ArrayList<Story>();
	private static void Serialize(){
		try (FileOutputStream fileOut = new FileOutputStream(ROFILE);
				ObjectOutputStream out = new ObjectOutputStream(fileOut)){
	         out.writeObject(runningOrders);
	      }catch(IOException i) {
	    	  System.out.println("RunningOrders serialize Error: Coudln't find save file!");
	      }
	}
	public RunningOrder(){
		this.roID = String.valueOf(getUniqueId());
		runningOrders.put(this.roID, this);
		Serialize();
	}
	public RunningOrder(Node message){
		this.roID = Model.MessageInfo.GetNodeContext(message, "roID");
		this.roSlug = Model.MessageInfo.GetNodeContext(message, "roSlug");
		this.roChannel = Model.MessageInfo.GetNodeContext(message, "roChannel");
		this.roEdStart = Model.MessageInfo.GetNodeContext(message, "roEdStart");
		this.roEdDur = Model.MessageInfo.GetNodeContext(message, "roEdDur");
		String triggerStr = Model.MessageInfo.GetNodeContext(message, "roTrigger");
		if (triggerStr.startsWith(mosmessages.defined.Trigger.CHAINED.toString())){
			String[] trigerStrArray = triggerStr.split(" ");
			mosmessages.defined.Trigger trigger = mosmessages.defined.Trigger.getFromString(trigerStrArray[0]);
			if (trigger!=null){
				this.roTrigger = trigger;
			}
			if (trigerStrArray.length>=2){
				try {
					this.trigerChainedValue = Integer.valueOf(trigerStrArray[1].charAt(0)=='+' ? trigerStrArray[1].substring(1) : trigerStrArray[1]);
				}
				catch(NumberFormatException e){}
			}
		}
		else{
			mosmessages.defined.Trigger trigger = mosmessages.defined.Trigger.getFromString(triggerStr);
			if (trigger!=null){
				this.roTrigger = trigger;
			}
		}
		this.macroIn = Model.MessageInfo.GetNodeContext(message, "macroIn");
		this.macroOut = Model.MessageInfo.GetNodeContext(message, "macroOut");
		
		NodeList nodeList = message.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equalsIgnoreCase("story")){
				String key = mossimulator.Model.MessageInfo.GetNodeContext(node, "storyID");
				if (!key.equals("")){
					if (Story.Contains(key)){
						stories.add(Story.getItemObj(key));
					}
					else{
						stories.add(new Story(node));
					}
				}
			}
		}
		runningOrders.put(this.roID, this);
		Serialize();
	}
	private long getUniqueId(){
		for (int i=0; i<2; i++){
			while (lastUnique < 4294967295L){
				if (!runningOrders.containsKey(String.valueOf(lastUnique)))
					return lastUnique++;
				lastUnique++;
			}
			lastUnique = 1;
		}
		return 0; // all id are already taken
	}
	public static Set<String> GetKeys(){
		return runningOrders.keySet();
	}
	public static boolean Contains(String key){
		return runningOrders.containsKey(key);
	}
	public static void remove(String key){
		runningOrders.remove(key);
	}
	public static boolean isEmpty(){
		return runningOrders.isEmpty();
	}
	public static RunningOrder getRunningOrderObj(String key){
		if (Contains(key))
			return runningOrders.get(key);
		else
			return null;
	}
	public void BuildXml(Element element, Document xmlDoc){
		Element roID = xmlDoc.createElement("roID");
		roID.appendChild(xmlDoc.createTextNode(this.roID));
		element.appendChild(roID);

		Element roSlug = xmlDoc.createElement("roSlug");
		roSlug.appendChild(xmlDoc.createTextNode(this.roSlug));
		element.appendChild(roSlug);

		if (!this.roChannel.equals("")){
			Element roChannel = xmlDoc.createElement("roChannel");
			roChannel.appendChild(xmlDoc.createTextNode(this.roChannel));
			element.appendChild(roChannel);
		}

		if (!this.roEdStart.equals("")){
			Element roEdStart = xmlDoc.createElement("roEdStart");
			roEdStart.appendChild(xmlDoc.createTextNode(this.roEdStart));
			element.appendChild(roEdStart);
		}
		
		if (!this.roEdDur.equals("")){
			Element roEdDur = xmlDoc.createElement("roEdDur");
			roEdDur.appendChild(xmlDoc.createTextNode(this.roEdDur));
			element.appendChild(roEdDur);
		}
		
		if (this.roTrigger!=null){
			Element roTrigger = xmlDoc.createElement("roTrigger");
			roTrigger.appendChild(xmlDoc.createTextNode(this.getItemTriggerString()));
			element.appendChild(roTrigger);
		}
		
		if (!this.macroIn.equals("")){
			Element macroIn = xmlDoc.createElement("macroIn");
			macroIn.appendChild(xmlDoc.createTextNode(this.macroIn));
			element.appendChild(macroIn);
		}
		
		if (!this.macroOut.equals("")){
			Element macroOut = xmlDoc.createElement("macroOut");
			macroOut.appendChild(xmlDoc.createTextNode(this.macroOut));
			element.appendChild(macroOut);
		}
		
		for (Story story : stories){
			Element itemElement = xmlDoc.createElement("story");
			story.BuildXml(itemElement, xmlDoc);
			itemElement.appendChild(itemElement);
		}
	}
	public String getRoID() {
		return roID;
	}
	public Story getStory(String storyID){
		for (Story story : stories){
			if (story.getStoryID().equalsIgnoreCase(storyID))
				return story;
		}
		return null;
	}
	public RunningOrder setRoID(String roID) {
		if (roID.length()<=128){
			runningOrders.remove(this.roID);
			this.roID = roID;
			runningOrders.put(this.roID, this);
			Serialize();
		}
		return this;
	}
	public String getRoSlug() {
		return roSlug;
	}
	public RunningOrder setRoSlug(String roSlug) {
		if (roSlug.length()<=128){
			this.roSlug = roSlug;
			Serialize();
		}
		return this;
	}
	public String getRoChannel() {
		return roChannel;
	}
	public RunningOrder setRoChannel(String roChannel) {
		if (roChannel.length()<=128){
			this.roChannel = roChannel;
			Serialize();
		}
		return this;
	}
	public String getRoEdStart() {
		return roEdStart;
	}
	public RunningOrder setRoEdStart(String roEdStart) {
		if (roEdStart.length()<=128){
			this.roEdStart = roEdStart;
			Serialize();
		}
		return this;
	}
	public String getRoEdDur() {
		return roEdDur;
	}
	public RunningOrder setRoEdDur(String roEdDur) {
		if (roEdDur.length()<=128){
			this.roEdDur = roEdDur;
			Serialize();
		}
		return this;
	}
	public mosmessages.defined.Trigger getRoTrigger() {
		return roTrigger;
	}
	public RunningOrder setRoTrigger(mosmessages.defined.Trigger roTrigger) {
		this.roTrigger = roTrigger;
		Serialize();
		return this;
	}
	public RunningOrder setRoTrigger(String roTrigger) {
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
	public String getMacroIn() {
		return macroIn;
	}
	public RunningOrder setMacroIn(String macroIn) {
		if (macroIn.length()<=128){
			this.macroIn = macroIn;
			Serialize();
		}
		return this;
	}
	public String getMacroOut() {
		return macroOut;
	}
	public RunningOrder setMacroOut(String macroOut) {
		if (macroOut.length()<=128){
			this.macroOut = macroOut;
			Serialize();
		}
		return this;
	}
	public int getTrigerChainedvalue() {
		return trigerChainedValue;
	}
	public RunningOrder setTrigerChainedvalue(int trigerChainedvalue) {
		if (this.roTrigger==mosmessages.defined.Trigger.CHAINED){
			this.trigerChainedValue = trigerChainedvalue;
			Serialize();
		}
		else{
			System.out.println("ItemTrigger has to be CHAINED.");
		}
		return this;
	}
	public String getItemTriggerString() {
		if (this.roTrigger==mosmessages.defined.Trigger.CHAINED)
			return roTrigger.toString()+" "+(this.trigerChainedValue>=0?"+"+this.trigerChainedValue:this.trigerChainedValue);
		return roTrigger.toString();
	}
	public ArrayList<Story> getStoryArray(){
		return this.stories;
	}
	public void AddStories(Story e){
		this.stories.add(e);
		Serialize();
	}
	public void addItem(int ix, Story i){
		this.stories.add(ix, i);
		Serialize();
	}
	public void remove(int i) {
		stories.remove(i);
		Serialize();
	}
	public void swap(int index1, int index2) {
		Collections.swap(this.stories, index1, index2);
	}
}
