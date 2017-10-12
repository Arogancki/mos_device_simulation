package mossimulator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mosmessages.MosMessage;

public class Story implements Serializable{
	private static final long serialVersionUID = 1L;
	private static long lastUnique = 1;
	private String storyID = "";
	private String storySlug = "";
	private String storyNum = "";
	public ArrayList<Item> items = new ArrayList<Item>();
	public Story(){
		storyID = String.valueOf(getUniqueId());
		stories.put(this.storyID, this);
		Serialize();
	}
	public Story(Node message){
		this.storyID = Model.MessageInfo.GetNodeContext(message, "storyID");
		this.storySlug = Model.MessageInfo.GetNodeContext(message, "storySlug");
		this.storyNum = Model.MessageInfo.GetNodeContext(message, "storyNum");
	
		NodeList nodeList = message.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equalsIgnoreCase("item")){
				String key = mossimulator.Model.MessageInfo.GetNodeContext(node, "itemID");
				if (!key.equals("")){
					if (mossimulator.Item.Contains(key)){
						items.add(mossimulator.Item.getItemObj(key));
					}
					else{
						items.add(new mossimulator.Item(node));
					}
				}
			}
		}
		stories.put(this.getStoryID(), this);
		Serialize();
	}
	public void addItem(Item item){
		items.add(item);
		Serialize();
	}
	public void removeItem(Item item){
		items.remove(item);
		Serialize();
	}
	public void removeItem(String storyID){
		for (Item item : items) {
			if (item.getItemID().equalsIgnoreCase(storyID)){
				removeItem(item);
				Serialize();
				break;
			}
		}
	}
	public boolean containsItem(Item item){
		return items.contains(item);
	}
	public boolean containsItem(String itemID){
		for (Item item : items) {
			if (item.getItemID().equalsIgnoreCase(itemID)){
				return true;
			}
		}
		return false;
	}
	public Item getItem(String itemID){
		for (Item item : items) {
			if (item.getItemID().equalsIgnoreCase(itemID)){
				return item;
			}
		}
		return null;
	}
	static private Hashtable<String, Story> stories = null;
	private static final String STORIESFILE = "Stories.ser";
	static {
		try (FileInputStream fis = new FileInputStream(STORIESFILE);
	            ObjectInputStream ois = new ObjectInputStream(fis)){
			stories = (Hashtable<String, Story>) ois.readObject();
        }
		catch(ClassNotFoundException|IOException c){
        	  System.out.println("Warrning: Stories save file wasn't read. Creating a new, empty save file.");
        	  stories = new Hashtable<String, Story>();
        	  Serialize();
        }
	}
	private static void Serialize(){
		try (FileOutputStream fileOut = new FileOutputStream(STORIESFILE);
				ObjectOutputStream out = new ObjectOutputStream(fileOut)){
	         out.writeObject(stories);
	      }catch(IOException i) {
	    	  System.out.println("Stories serialize Error: Coudln't find save file!");
	      }
	}
	private long getUniqueId(){
		for (int i=0; i<2; i++){
			while (lastUnique < 4294967295L){
				if (!stories.containsKey(String.valueOf(lastUnique)))
					return lastUnique++;
				lastUnique++;
			}
			lastUnique = 1;
		}
		return 0; // all id are already taken
	}
	public static Set<String> GetKeys(){
		return stories.keySet();
	}
	public static boolean Contains(String key){
		return stories.containsKey(key);
	}
	public static boolean isEmpty(){
		return stories.isEmpty();
	}
	public static Story getItemObj(String key){
		if (Contains(key))
			return stories.get(key);
		else
			return null;
	}
	public void BuildXml(Element elemet, Document xmlDoc){
		Element storyID = xmlDoc.createElement("storyID");
		storyID.appendChild(xmlDoc.createTextNode(this.getStoryID()));
		elemet.appendChild(storyID);
		
		if (!storySlug.equalsIgnoreCase("")){
			Element storySlug = xmlDoc.createElement("storySlug");
			storySlug.appendChild(xmlDoc.createTextNode(this.storySlug));
			elemet.appendChild(storySlug);
		}
		
		if (!storyNum.equalsIgnoreCase("")){
			Element storyNum = xmlDoc.createElement("storyNum");
			storyNum.appendChild(xmlDoc.createTextNode(this.storyNum));
			elemet.appendChild(storyNum);
		}
		
		for (Item item : items){
			Element itemElement = xmlDoc.createElement("item");
			item.BuildXml(itemElement, xmlDoc);
			elemet.appendChild(itemElement);
		}		
	}
	public String getStoryID() {
		return storyID;
	}
	public Story setStoryID(String storyID) {
		if (storyID.length()<=128){
			this.storyID = storyID;
			Serialize();
		}
		return this;
	}
	public String getStorySlug() {
		return storySlug;
	}
	public Story setStorySlug(String storySlug) {
		if (storyID.length()<=128){
			this.storySlug = storySlug;
			Serialize();
		}
		return this;
	}
	public String getStoryNum() {
		return storyNum;
	}
	public Story setStoryNum(String storyNum) {
		if (storyID.length()<=128){
			this.storyNum = storyNum;
			Serialize();
		}
		return this;
	}
	public ArrayList<Item> getItemsArray(){
		return this.items;
	}
}
