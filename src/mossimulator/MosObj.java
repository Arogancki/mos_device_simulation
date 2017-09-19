package mossimulator;

import java.util.Hashtable;

public class MosObj {
	static private Hashtable<String, MosObj> mosObjects = new Hashtable<String, MosObj>();
	// auto generated 
	private String objID;
	private String objSlug = "Slug";
	private String mosAbstract = null;
	private String objGroup = null;
	private mosmessages.defined.ObjType objtype = mosmessages.defined.ObjType.VIDEO;
	private long objTB = 50L;
	private String objRev = "";
	private long objDur = 0L;
	private mosmessages.defined.Status status = mosmessages.defined.Status.NEW;
	private mosmessages.defined.ObjAir objAir = mosmessages.defined.ObjAir.NOT_READY;
	// objPaths
	private final String DEFAULT_USER = "MOS SIMULATOR";
	private String createdBy = DEFAULT_USER;
	private String created;
	private String changedBy;
	private String changed = "";
	private String description = "";
	
	public void checkAsSemt(){
		published = true;
	}
	private boolean published = false;
	private void AddMosObj(MosObj mosObj){
		mosObjects.put(mosObj.getObjID(), mosObj);
	}
	public MosObj(Model.MessageInfo message){
		// parse into this 
		
		mosObjects.put(getObjID(), this);
	}
	public MosObj(){
		// stworzyc unikalny objID
		
		// YYYY-MM-DD'T'hh:mm:ss[,ddd]['Z'], e.g. 
		// 2009-04-11T14:22:07,125Z 
		
		// fractional time 
		
		// set created
		// set changed (time)
        changed = DEFAULT_USER;
		mosObjects.put(getObjID(), this);
	}
	public void Change(){
		if (published)
			return;
		// set changed (time)
		changed = DEFAULT_USER;
		status = mosmessages.defined.Status.UPDATED;
		// Wyslac zmiane
	}
	public void Change(String _changedBy){
		Change();
		changed = _changedBy;
	}
	public String getObjSlug() {
		return objSlug;
	}
	public MosObj setObjSlug(String objSlug) {
		if (objSlug.length() > 127 )
			return this;
		this.objSlug = objSlug;
		return this;
	}
	public String getMosAbstract() {
		return mosAbstract;
	}
	public MosObj setMosAbstract(String mosAbstract) {
		this.mosAbstract = mosAbstract;
		return this;
	}
	public String getObjGroup() {
		return objGroup;
	}
	public MosObj setObjGroup(String objGroup) {
		this.objGroup = objGroup;
		return this;
	}
	public mosmessages.defined.ObjType getObjtype() {
		return objtype;
	}
	public MosObj setObjtype(mosmessages.defined.ObjType objtype) {
		this.objtype = objtype;
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
		return this;
	}
	public String getObjRev() {
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
		return this;
	}
	public String getObjID() {
		return objID;
	}
	public long getObjDur() {
		return objDur;
	}
	public MosObj setObjDur(long objDur) {
		if (objDur > 4294967295L)
			objDur = 4294967295L;
		this.objDur = objDur;
		return this;
	}
	public mosmessages.defined.Status getStatus() {
		return status;
	}
	public MosObj setStatus(mosmessages.defined.Status status) {
		this.status = status;
		return this;
	}
	public mosmessages.defined.ObjAir getObjAir() {
		return objAir;
	}
	public MosObj setObjAir(mosmessages.defined.ObjAir objAir) {
		this.objAir = objAir;
		return this;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public MosObj setCreatedBy(String createdBy) {
		if (this.createdBy.equals("")){
			System.out.println("Unable to change the creator");
			return this;
		}
		if (createdBy.length() > 127 )
			return this;
		this.createdBy = createdBy;
		return this;
	}
	public String getCreated() {
		return created;
	}
	public String getChangedBy() {
		return changedBy;
	}
	public MosObj setChangedBy(String changedBy) {
		if (changedBy.length() > 127 )
			return this;
		this.changedBy = changedBy;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public MosObj setDescription(String description) {
		this.description = description;
		return this;
	}
}
