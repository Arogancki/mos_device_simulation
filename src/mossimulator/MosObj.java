package mossimulator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.Hashtable;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MosObj implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final String MOSFILE = "MosObjs.ser";
	static private Hashtable<String, MosObj> mosObjects = null;
	static {
		try (FileInputStream fis = new FileInputStream(MOSFILE);
	            ObjectInputStream ois = new ObjectInputStream(fis)){
			mosObjects = (Hashtable<String, MosObj>) ois.readObject();
        }
		catch(ClassNotFoundException|IOException c){
        	  System.out.println("Warrning: MosObjects save file wasn't read. Creating a new, empty save file.");
        	  mosObjects = new Hashtable<String, MosObj>();
        	  Serialize();
        }
	}
	private String objID = null;
	private String objSlug = "Slug";
	private String mosAbstract = "mosAbstract";
	private String objGroup = "objGroup";
	private mosmessages.defined.ObjType objType = mosmessages.defined.ObjType.VIDEO;
	private long objTB = 50L;
	private String objRev = "1";
	private long objDur = 2L;
	private mosmessages.defined.Status status = mosmessages.defined.Status.NEW;
	private mosmessages.defined.ObjAir objAir = mosmessages.defined.ObjAir.NOT_READY;
	// objPaths
	private final String DEFAULT_USER = "MOS SIMULATOR";
	private String createdBy = DEFAULT_USER;
	private String created = "00:00:00";
	private String changedBy = "DEFAULT_USER";
	private String changed ="00:00:00";
	private String description = "description";
	//mosExternalMetadata
	private static long lastUnique = 1;
	
	public String toString(){
		StringBuffer result = new StringBuffer("");
		if (objID!=null && !objID.equals(""))
			result.append("objID: \"" + objID + "\"\n");
		if (objSlug!=null && !objSlug.equals(""))
			result.append("objSlug: \"" + objSlug + "\"\n");
		if (mosAbstract!=null && !mosAbstract.equals(""))
			result.append("mosAbstract: \"" + mosAbstract + "\"\n");
		if (objGroup!=null && !objGroup.equals(""))
			result.append("objGroup: \"" + objGroup + "\"\n");
		if (objType!=null)
			result.append("objType: \"" + objType.toString() + "\"\n");
		try{
			result.append("objTB: \"" + String.valueOf(objTB) + "\"\n");
		}
		catch(NumberFormatException e){}
		if (objRev!=null && !objRev.equals(""))
			result.append("objRev: \"" + objRev + "\"\n");
		try{
			result.append("objDur: \"" + String.valueOf(objDur) + "\"\n");
		}
		catch(NumberFormatException e){}
		if (status!=null)
			result.append("status: \"" + status.toString() + "\"\n");
		if (objAir!=null)
			result.append("objAir: \"" + objAir.toString() + "\"\n");
		if (createdBy!=null && !createdBy.equals(""))
			result.append("createdBy: \"" + createdBy + "\"\n");
		if (created!=null && !created.equals(""))
			result.append("created: \"" + created + "\"\n");
		if (changedBy!=null && !changedBy.equals(""))
			result.append("changedBy: \"" + changedBy + "\"\n");
		if (changed!=null && !changed.equals(""))
			result.append("changed: \"" + changed + "\"\n");
		if (description!=null && !description.equals(""))
			result.append("description: \"" + description + "\"\n");
		return result.toString();
	}
	public static Set<String> GetKeys(){
		return mosObjects.keySet();
	}
	public void BuildXml(Element mosObj, Document xmlDoc){
		Element objID = xmlDoc.createElement("objID");
		objID.appendChild(xmlDoc.createTextNode(this.getObjID()));
		mosObj.appendChild(objID);
		
		Element objSlug = xmlDoc.createElement("objSlug");
		objSlug.appendChild(xmlDoc.createTextNode(this.getObjSlug()));
		mosObj.appendChild(objSlug);
		
		Element mosAbstract = xmlDoc.createElement("mosAbstract");
		mosAbstract.appendChild(xmlDoc.createTextNode(this.getMosAbstract()));
		mosObj.appendChild(mosAbstract);
		
		Element objGroup = xmlDoc.createElement("objGroup");
		objGroup.appendChild(xmlDoc.createTextNode(this.getObjGroup()));
		mosObj.appendChild(objGroup);
		
		Element objType = xmlDoc.createElement("objType");
		objType.appendChild(xmlDoc.createTextNode(this.getObjType().toString()));
		mosObj.appendChild(objType);
		
		try {
			Element objTB = xmlDoc.createElement("objTB");
			objTB.appendChild(xmlDoc.createTextNode(String.valueOf(this.getObjTB())));
			mosObj.appendChild(objTB);
		}catch (NumberFormatException e){}
		
		Element objRev = xmlDoc.createElement("objRev");
		objRev.appendChild(xmlDoc.createTextNode(this.getObjRev()));
		mosObj.appendChild(objRev);
		
		try {
			Element objDur = xmlDoc.createElement("objDur");
			objDur.appendChild(xmlDoc.createTextNode(String.valueOf(this.getObjDur())));
			mosObj.appendChild(objDur);
		}catch (NumberFormatException e){}
		
		Element status = xmlDoc.createElement("status");
		status.appendChild(xmlDoc.createTextNode(this.getStatus().toString()));
		mosObj.appendChild(status);
		
		Element objAir = xmlDoc.createElement("objAir");
		objAir.appendChild(xmlDoc.createTextNode(this.getObjAir().toString()));
		mosObj.appendChild(objAir);
		
		Element createdBy = xmlDoc.createElement("createdBy");
		createdBy.appendChild(xmlDoc.createTextNode(this.getCreatedBy()));
		mosObj.appendChild(createdBy);
		
		Element created = xmlDoc.createElement("created");
		created.appendChild(xmlDoc.createTextNode(this.getCreated()));
		mosObj.appendChild(created);
		
		Element changedBy = xmlDoc.createElement("changedBy");
		changedBy.appendChild(xmlDoc.createTextNode(this.getChangedBy()));
		mosObj.appendChild(changedBy);
		
		Element changed = xmlDoc.createElement("changed");
		changed.appendChild(xmlDoc.createTextNode(this.getChanged()));
		mosObj.appendChild(changed);
		
		Element description = xmlDoc.createElement("description");
		description.appendChild(xmlDoc.createTextNode(this.getDescription()));
		mosObj.appendChild(description);
	}
	public static boolean Contains(String key){
		return mosObjects.containsKey(key);
	}
	public static boolean isEmpty(){
		return mosObjects.isEmpty();
	}
	public  static String printObj(String objKey){
		return mosObjects.get(objKey).toString();
	}
	public static String printAllObj(){
		StringBuffer result = new StringBuffer("");
		for (String key : mosObjects.keySet()) {
			result.append("UID: " + key + ":\n" + printObj(key)+"\n");
		}
		return result.toString();
	}
	public static MosObj getMosObj(String key){
		if (Contains(key))
			return mosObjects.get(key);
		else
			return null;
	}
	private static void Serialize(){
		try (FileOutputStream fileOut = new FileOutputStream(MOSFILE);
				ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
	         out.writeObject(mosObjects);
	      }catch(IOException i) {
	    	  System.out.println("MosObj serialize Error: Coudln't find save file!");
	      }
	}
	private void AddMosObj(){
		mosObjects.put(this.getObjID(), this);
		Serialize();
	}
	private long getUniqueId(){
		for (int i=0; i<2; i++){
			while (lastUnique < 4294967295L){
				if (!mosObjects.containsKey(String.valueOf(lastUnique)))
					return lastUnique++;
				lastUnique++;
			}
			lastUnique = 1;
		}
		return 0; // all id are already taken
	}
	public MosObj(Model.MessageInfo message){
		String result;
		result = message.GetFromXML("objID");
		if (result != null){
			objID = result;
		}
		
		result = message.GetFromXML("objSlug");
		if (result != null){
			objSlug = result;
		}
		
		result = message.GetFromXML("mosAbstract");
		if (result != null){
			mosAbstract = result;
		}
		
		result = message.GetFromXML("objGroup");
		if (result != null){
			objGroup = result;
		}
		
		result = message.GetFromXML("objType");
		if (result != null){
			mosmessages.defined.ObjType res = mosmessages.defined.ObjType.getFromString(result);
			if (res != null){
				objType = res;
			}
		}
		
		result = message.GetFromXML("objTB");
		if (result != null){
			try{
				objTB = Long.valueOf(result);
			}
			catch(NumberFormatException  e){}
		}
		
		result = message.GetFromXML("objRev");
		if (result != null){
			objRev = result;
		}
		
		result = message.GetFromXML("objDur");
		if (result != null){
			try{
				objDur = Long.valueOf(result);
			}
			catch(NumberFormatException  e){}
		}
		
		result = message.GetFromXML("status");
		if (result != null){
			mosmessages.defined.Status res = mosmessages.defined.Status.getFromString(result);
			if (res != null){
				status = res;
			}
		}
		
		result = message.GetFromXML("objAir");
		if (result != null){
			mosmessages.defined.ObjAir res = mosmessages.defined.ObjAir.getFromString(result);
			if (res != null){
				objAir = res;
			}
		}
		
		result = message.GetFromXML("createdBy");
		if (result != null){
			createdBy = result;
		}
		
		result = message.GetFromXML("created");
		if (result != null){
			created = result;
		}
		
		result = message.GetFromXML("changedBy");
		if (result != null){
			changedBy = result;
		}
		
		result = message.GetFromXML("changed");
		if (result != null){
			changed = result;
		}
		
		result = message.GetFromXML("description");
		if (result != null){
			description = result;
		}
		if (objID!=null && !objID.equals(""))
		    AddMosObj();
		else
			System.out.println("Uncorrect UID.");
		Serialize();
	}
	public MosObj(String _objID){
		objID = _objID;
		AddMosObj();
		Serialize();
	}
	public MosObj(){
		try{
			Long key = getUniqueId();
			if (key != 0){
				objID = String.valueOf(key);
				created = Instant.now().toString(); 
				changed = Instant.now().toString(); 
				createdBy = DEFAULT_USER;
		        AddMosObj();
			}
		}
		catch(NumberFormatException e){
			System.out.println("Couldn't create UID");
		}
	}
	public void Change(){
		changed = Instant.now().toString();
		Serialize();
	}
	public void Change(String _changedBy){
		Change();
		changed = _changedBy;
	}
	public String getObjSlug() {
		if (objSlug==null)
			return "";
		return objSlug;
	}
	public MosObj setObjSlug(String objSlug) {
		if (objSlug.length() > 127 )
			return this;
		this.objSlug = objSlug;
		Change();
		return this;
	}
	public String getMosAbstract() {
		if (mosAbstract==null)
			return "";
		return mosAbstract;
	}
	public MosObj setMosAbstract(String mosAbstract) {
		this.mosAbstract = mosAbstract;
		Change();
		return this;
	}
	public String getObjGroup() {
		if (objGroup==null)
			return "";
		return objGroup;
	}
	public MosObj setObjGroup(String objGroup) {
		this.objGroup = objGroup;
		Change();
		return this;
	}
	public mosmessages.defined.ObjType getObjType() {
		return objType;
	}
	public MosObj setObjType(mosmessages.defined.ObjType objtype) {
		if (objtype==null)
			return this;
		this.objType = objtype;
		Change();
		return this;
	}
	public long getObjTB() {
		return objTB;
	}
	public MosObj setObjTB(long objTB) {
		if (objTB <= 0)
			return this;
		//max 0xFFFF FFFF
		if (objTB > 4294967295L)
			objTB = 4294967295L;
		this.objTB = objTB;
		Change();
		return this;
	}
	public String getObjRev() {
		if (objRev==null)
			return "";
		return objRev;
	}
	public MosObj setObjRev(String objRev) {
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
		Change();
		return this;
	}
	public String getObjID() {
		if (objID==null)
			return "";
		return objID;
	}
	public long getObjDur() {
		return objDur;
	}
	public MosObj setObjDur(long objDur) {
		if (objDur > 4294967295L)
			objDur = 4294967295L;
		this.objDur = objDur;
		Change();
		return this;
	}
	public mosmessages.defined.Status getStatus() {
		return status;
	}
	public MosObj setStatus(mosmessages.defined.Status status) {
		if (status==null)
			return this;
		this.status = status;
		Change();
		return this;
	}
	public mosmessages.defined.ObjAir getObjAir() {
		return objAir;
	}
	public MosObj setObjAir(mosmessages.defined.ObjAir objAir) {
		if (objAir==null)
			return this;
		this.objAir = objAir;
		Change();
		return this;
	}
	public String getCreatedBy() {
		if (createdBy==null)
			return "";
		return createdBy;
	}
	public MosObj setCreatedBy(String createdBy) {
		if (createdBy.length() > 127 )
			return this;
		this.createdBy = createdBy;
		Change();
		return this;
	}
	public String getCreated() {
		if (created==null)
			return "";
		return created;
	}
	public String getChangedBy() {
		if (changedBy==null)
			return "";
		return changedBy;
	}
	public MosObj setCreated(String _created) {
		this.created = _created;
		Change();
		return this;
	}
	public MosObj setChangedBy(String changedBy) {
		if (changedBy.length() > 127 )
			return this;
		this.changedBy = changedBy;
		Change();
		return this;
	}
	public String getDescription() {
		if (description==null)
			return "";
		return description;
	}
	public MosObj setDescription(String description) {
		this.description = description;
		Change();
		return this;
	}
	public String getChanged() {
		if (changed==null)
			return "";
		return changed;
	}
	public MosObj setObjID(String id) {
		if (id!=null)
			this.objID=id;
		Change();
		return this;
	}
	public MosObj setChanged(String id) {
		if (id!=null)
			this.changed=id;
		Change();
		return this;
	}
}
