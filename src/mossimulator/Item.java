package mossimulator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import mosmessages.defined.Trigger;
import mossimulator.Model.MessageInfo;

public class Item implements Serializable{
	private static final long serialVersionUID = 1L;
	private static long lastUnique = 1;
	static private Hashtable<String, Item> items = null;
	private static final String ITEMSFILE = "Items.ser";
	private String itemID = "";
	private String itemSlug = "";
	private String objID = "";
	private String mosID = "";
	private String mosAbstract = "";
	// objPaths?
	private String itemChannel = "";
	private Long itemEdStart = -1L;
	private Long itemEdDur = -1L;
	private Long itemUserTimingDur = -1L;
	private mosmessages.defined.Trigger itemTrigger = null;
	private int trigerChainedValue = 0;
	private String macroIn = "";
	private String macroOut = "";
	//mosExternalMetadata
	static {
		try (FileInputStream fis = new FileInputStream(ITEMSFILE);
	            ObjectInputStream ois = new ObjectInputStream(fis)){
			items = (Hashtable<String, Item>) ois.readObject();
        }
		catch(ClassNotFoundException|IOException c){
        	  System.out.println("Warrning: Items save file wasn't read. Creating a new, empty save file.");
        	  items = new Hashtable<String, Item>();
        	  Serialize();
        }
	}
	public Item(){
		this.itemID = String.valueOf(getUniqueId());
		items.put(this.itemID, this);
		Serialize();
	}
	public Item(Node message){
		this.itemID = Model.MessageInfo.GetNodeContext(message, "itemID");
		this.itemSlug = Model.MessageInfo.GetNodeContext(message, "itemSlug");
		this.objID = Model.MessageInfo.GetNodeContext(message, "objID");
		this.mosID = Model.MessageInfo.GetNodeContext(message, "mosID");
		this.mosAbstract = Model.MessageInfo.GetNodeContext(message, "mosAbstract");
		this.itemChannel = Model.MessageInfo.GetNodeContext(message, "itemChannel");
		String longStr;
		longStr = Model.MessageInfo.GetNodeContext(message, "itemEdStart");
		try {
			this.itemEdStart = Long.valueOf(longStr);
		}
		catch(NumberFormatException e){}
		longStr = Model.MessageInfo.GetNodeContext(message, "itemEdDur");
		try {
			this.itemEdDur = Long.valueOf(longStr);
		}
		catch(NumberFormatException e){}
		longStr = Model.MessageInfo.GetNodeContext(message, "itemUserTimingDur");
		try {
			this.itemUserTimingDur = Long.valueOf(longStr);
		}
		catch(NumberFormatException e){}
		String triggerStr = Model.MessageInfo.GetNodeContext(message, "itemTrigger");
		if (triggerStr.startsWith(mosmessages.defined.Trigger.CHAINED.toString())){
			String[] trigerStrArray = triggerStr.split(" ");
			mosmessages.defined.Trigger trigger = mosmessages.defined.Trigger.getFromString(trigerStrArray[0]);
			if (trigger!=null){
				this.itemTrigger = trigger;
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
				this.itemTrigger = trigger;
			}
		}
		this.macroIn = Model.MessageInfo.GetNodeContext(message, "macroIn");
		this.macroOut = Model.MessageInfo.GetNodeContext(message, "macroOut");
		items.put(this.getItemID(), this);
		Serialize();
	}
	private static void Serialize(){
		try (FileOutputStream fileOut = new FileOutputStream(ITEMSFILE);
				ObjectOutputStream out = new ObjectOutputStream(fileOut)){
	         out.writeObject(items);
	      }catch(IOException i) {
	    	  System.out.println("Items serialize Error: Coudln't find save file!");
	      }
	}
	private long getUniqueId(){
		for (int i=0; i<2; i++){
			while (lastUnique < 4294967295L){
				if (!items.containsKey(String.valueOf(lastUnique)))
					return lastUnique++;
				lastUnique++;
			}
			lastUnique = 1;
		}
		return 0; // all id are already taken
	}
	public static Set<String> GetKeys(){
		return items.keySet();
	}
	public static boolean Contains(String key){
		return items.containsKey(key);
	}
	public static boolean isEmpty(){
		return items.isEmpty();
	}
	public static Item getItemObj(String key){
		if (Contains(key))
			return items.get(key);
		else
			return null;
	}
	public void BuildXml(Element element, Document xmlDoc){
		Element itemID = xmlDoc.createElement("itemID");
		itemID.appendChild(xmlDoc.createTextNode(this.getItemID()));
		element.appendChild(itemID);
		
		if (!this.itemSlug.equals("")){
			Element itemSlug = xmlDoc.createElement("itemSlug");
			itemSlug.appendChild(xmlDoc.createTextNode(this.getItemSlug()));
			element.appendChild(itemSlug);
		}
		
		Element objID = xmlDoc.createElement("objID");
		objID.appendChild(xmlDoc.createTextNode(this.getObjID()));
		element.appendChild(objID);
		
		Element mosID = xmlDoc.createElement("mosID");
		mosID.appendChild(xmlDoc.createTextNode(this.getMosID()));
		element.appendChild(mosID);
		
		if (!this.mosAbstract.equals("")){
			Element mosAbstract = xmlDoc.createElement("mosAbstract");
			mosAbstract.appendChild(xmlDoc.createTextNode(this.getMosAbstract()));
			element.appendChild(mosAbstract);
		}
		
		if (!this.itemChannel.equals("")){
			Element itemChannel = xmlDoc.createElement("itemChannel");
			itemChannel.appendChild(xmlDoc.createTextNode(this.getItemChannel()));
			element.appendChild(itemChannel);
		}
		
		if (this.itemEdStart>=0L){
			Element itemEdStart = xmlDoc.createElement("itemEdStart");
			itemEdStart.appendChild(xmlDoc.createTextNode(this.getItemEdStart().toString()));
			element.appendChild(itemEdStart);
		}
		
		if (this.itemEdDur>=0L){
			Element itemEdDur = xmlDoc.createElement("itemEdDur");
			itemEdDur.appendChild(xmlDoc.createTextNode(this.getItemEdDur().toString()));
			element.appendChild(itemEdDur);
		}
		
		if (this.itemUserTimingDur>=0L){
			Element itemUserTimingDur = xmlDoc.createElement("itemUserTimingDur");
			itemUserTimingDur.appendChild(xmlDoc.createTextNode(this.getItemUserTimingDur().toString()));
			element.appendChild(itemUserTimingDur);
		}
		
		if (this.itemTrigger!=null){
			Element itemTrigger = xmlDoc.createElement("itemTrigger");
			itemTrigger.appendChild(xmlDoc.createTextNode(this.getItemTriggerString()));
			element.appendChild(itemTrigger);
		}
		
		if (!this.macroIn.equals("")){
			Element macroIn = xmlDoc.createElement("macroIn");
			macroIn.appendChild(xmlDoc.createTextNode(this.getMacroIn()));
			element.appendChild(macroIn);
		}
		
		if (!this.macroOut.equals("")){
			Element macroOut = xmlDoc.createElement("macroOut");
			macroOut.appendChild(xmlDoc.createTextNode(this.getMacroOut()));
			element.appendChild(macroOut);
		}
	}
	public String getItemID() {
		return itemID;
	}
	public Item setItemID(String itemID) {
		if (itemID.length()<=128)
			this.itemID = itemID;
		Serialize();
		return this;
	}
	public String getItemSlug() {
		return itemSlug;
	}
	public Item setItemSlug(String itemSlug) {
		if (itemSlug.length()<=128){
			this.itemSlug = itemSlug;
			Serialize();
		}
		return this;
	}
	public String getObjID() {
		return objID;
	}
	public Item setObjID(String objID) {
		if (objID.length()<=128){
			this.objID = objID;
			Serialize();
		}
		return this;
	}
	public String getMosID() {
		return mosID;
	}
	public Item setMosID(String mosID) {
		this.mosID = mosID;
		Serialize();
		return this;
	}
	public String getMosAbstract() {
		return mosAbstract;
	}
	public Item setMosAbstract(String mosAbstract) {
		this.mosAbstract = mosAbstract;
		Serialize();
		return this;
	}
	public String getItemChannel() {
		return itemChannel;
	}
	public Item setItemChannel(String itemChannel) {
		if (itemChannel.length()<=128){
			this.itemChannel = itemChannel;
			Serialize();
		}
		return this;
	}
	public Long getItemEdStart() {
		return itemEdStart;
	}
	public Item setItemEdStart(Long itemEdStart) {
		if (itemEdStart<=4294967295L){
			this.itemEdStart = itemEdStart;
			Serialize();
		}
		return this;
	}
	public Item setItemEdStart(String itemEdStart) {
		try {
			setItemEdStart(Long.parseLong(itemEdStart));
		}
		catch (NumberFormatException e){
			System.out.println("Wrong format - setiteamEdStart: excepted Long.");
		}
		return this;
	}
	public Long getItemEdDur() {
		return itemEdDur;
	}
	public Item setItemEdDur(Long itemEdDur) {
		if (itemEdDur<=4294967295L){
			this.itemEdDur = itemEdDur;
			Serialize();
		}
		return this;
	}
	public Item setItemEdDur(String itemEdStart) {
		try {
			setItemEdDur(Long.parseLong(itemEdStart));
		}
		catch (NumberFormatException e){
			System.out.println("Wrong format - setItemEdDur: excepted Long.");
		}
		return this;
	}
	public Long getItemUserTimingDur() {
		return itemUserTimingDur;
	}
	public Item setItemUserTimingDur(Long itemUserTimingDur) {
		if (itemUserTimingDur<=4294967295L){
			this.itemUserTimingDur = itemUserTimingDur;
			Serialize();
		}
		return this;
	}
	public Item setItemUserTimingDur(String itemUserTimingDur) {
		try {
			setItemUserTimingDur(Long.parseLong(itemUserTimingDur));
		}
		catch (NumberFormatException e){
			System.out.println("Wrong format - setItemUserTimingDur: excepted Long.");
		}
		return this;
	}
	public mosmessages.defined.Trigger getItemTrigger() {
		return itemTrigger;
	}
	public String getItemTriggerString() {
		if (this.itemTrigger==mosmessages.defined.Trigger.CHAINED)
			return itemTrigger.toString()+" "+(this.trigerChainedValue>=0?"+"+this.trigerChainedValue:this.trigerChainedValue);
		return itemTrigger.toString();
	}
	public Item setItemTrigger(mosmessages.defined.Trigger itemTrigger) {
		this.itemTrigger = itemTrigger;
		Serialize();
		return this;
	}
	public Item setRoTrigger(String roTrigger) {
		if (roTrigger.startsWith("CHAINED")){
			String[] chainedArray = roTrigger.split(" ");
			if (chainedArray.length >= 2){
				chainedArray[1] = chainedArray[1].startsWith("+") ? chainedArray[1].substring(1) : chainedArray[1];
				mosmessages.defined.Trigger backup = this.itemTrigger;
				try {
					setItemTrigger(mosmessages.defined.Trigger.CHAINED);
					setTrigerChainedvalue(Integer.valueOf(chainedArray[1]));
				}
				catch(NumberFormatException e){
					setItemTrigger(backup);
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
				setItemTrigger(parsed);
			else
				System.out.println("Wrong format - setRoTrigger: excepted "+mosmessages.defined.Trigger.values());
		}
		return this;
	}
	public int getTrigerChainedvalue() {
		return trigerChainedValue;
	}
	public Item setTrigerChainedvalue(int trigerChainedvalue) {
		if (this.itemTrigger==mosmessages.defined.Trigger.CHAINED){
			this.trigerChainedValue = trigerChainedvalue;
			Serialize();
		}
		else{
			System.out.println("ItemTrigger has to be CHAINED.");
		}
		return this;
	}
	public String getMacroIn() {
		return macroIn;
	}
	public Item setMacroIn(String macroIn) {
		if (macroIn.length()<=128){
			this.macroIn = macroIn;
			Serialize();
		}
		return this;
	}
	public String getMacroOut() {
		return macroOut;
	}
	public Item setMacroOut(String macroOut) {
		if (macroOut.length()<=128){
			this.macroOut = macroOut;
			Serialize();
		}
		return this;
	}
}
