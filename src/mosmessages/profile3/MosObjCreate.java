package mosmessages.profile3;

import java.time.Instant;

import mosmessages.MosMessage;
import mosmessages.defined.ObjAir;
import mosmessages.defined.Status;
import mosmessages.profile1.MosAck;
import mossimulator.Model;
import mossimulator.Model.MessageInfo;
import mossimulator.MosObj;

import org.w3c.dom.Element;

public class MosObjCreate extends MosMessage {
	private String objSlug = "Slug";
	private String objGroup = ""; // optional
	private mosmessages.defined.ObjType objType = mosmessages.defined.ObjType.VIDEO;
	private long objTB = 50L;
	private long objDur = -1L;  // optional
	private String time = ""; // optional  Format is YYYY-MM-DD'T'hh:mm:ss[,ddd]['Z'],
	private String createdBy = ""; // optional
	private String description = ""; // optional
	public MosObjCreate() {
		super(Model.getLowerPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message){
		mossimulator.MosObj newObj = new MosObj();
		if (!newObj.getObjID().equals("")){
			// ustawic wartosci
			String result = null;
			result = message.GetFromXML("objSlug");
			if (result != null){
				newObj.setObjSlug(result);
			}
			result = message.GetFromXML("objGroup");
			if (result != null){
				newObj.setObjGroup(result);
			}
			result = message.GetFromXML("objType");
			if (result != null){
				mosmessages.defined.ObjType objType = mosmessages.defined.ObjType.getFromString(result);
				if (objType!=null)
					newObj.setObjType(objType);
			}
			result = message.GetFromXML("objTB");
			if (result != null){
				try {
					newObj.setObjTB(Long.valueOf(result));
				}
				catch (NumberFormatException e){}
			}
			result = message.GetFromXML("objDur");
			if (result != null){
				try {
					newObj.setObjDur(Long.valueOf(result));
				}
				catch (NumberFormatException e){}
			}
			result = message.GetFromXML("time");
			if (result != null){
				newObj.setCreated(result);
			}
			result = message.GetFromXML("createdBy");
			if (result != null){
				newObj.setCreatedBy(result);
				newObj.setChangedBy(result);
			}
			result = message.GetFromXML("description");
			if (result != null){
				newObj.setDescription(result);
			}
			newObj.setObjAir(ObjAir.NOT_READY);
			result = message.GetFromXML("objRev");
			if (result != null){
				newObj.setObjRev(result);
			}
			 new MosAck().setStatus(Status.OK).setStatusDescription(newObj.getObjID());
		}
		else{
			new MosAck().setStatus(Status.NACK).setStatusDescription("No objIdD available");
		}
	}
	@Override
	public void AfterSending(){
		/*MessageInfo response = getResponse();
		if (response != null){
			String responseType = response.getMosType().toLowerCase();
			if (responseType.equals("mosack")){
				String statusDescription = response.GetFromXML("statusDescription");
				String statusStr = response.GetFromXML("status");
				mosmessages.defined.Status status = null;
				if (!statusStr.equals("")){
					status = mosmessages.defined.Status.getFromString(statusStr);
					if (status!= null && status==mosmessages.defined.Status.NACK){
						System.out.print("Object couldn't be created.");
						if (!statusDescription.equals("")){
							System.out.print(" Cause: "+ statusDescription + ".");
						}
						System.out.print("\n");
					}
				}
				if (!statusDescription.equals("")){
					mossimulator.MosObj newObj = new MosObj(statusDescription);
					newObj.setObjSlug(getObjSlug()).setObjGroup(getObjGroup()).setObjType(getObjType()).setObjTB(getObjTB()).setObjDur(getObjDur())
					.setCreated(getTime()).setCreatedBy(getCreatedBy()).setChangedBy(getCreatedBy()).setDescription(getDescription());
					new mosmessages.profile1.MosObj().setMosObj(newObj).SendOnOpenSocket();
					System.out.println("Object created. objID:"+ statusDescription + ".");
				}}
			else if (responseType.equals("mosobj")){
				//TODO czy to bedzdie kiedykollewiek mozeliwe
				System.out.println("Object created. ObjID: " + ".");
			}	
		}*/
	}
	@Override
	public void PrepareToSend() {
			Element mos = xmlDoc.getDocumentElement();
			
			Element mosObjCreate = xmlDoc.createElement("mosObjCreate");
			mos.appendChild(mosObjCreate);
			
			Element objSlug = xmlDoc.createElement("objSlug");
		    objSlug.appendChild(xmlDoc.createTextNode(getObjSlug()));
		    mosObjCreate.appendChild(objSlug);
		    
		    if (getObjGroup().equals("")){
		    	Element objGroup = xmlDoc.createElement("objGroup");
		    	objGroup.appendChild(xmlDoc.createTextNode(getObjGroup()));
			    mosObjCreate.appendChild(objGroup);
		    }
		    
		    Element objType = xmlDoc.createElement("objType");
		    objType.appendChild(xmlDoc.createTextNode(getObjType().toString()));
		    mosObjCreate.appendChild(objType);
		    
		    Element objTB = xmlDoc.createElement("objTB");
		    objTB.appendChild(xmlDoc.createTextNode(String.valueOf(getObjTB())));
		    mosObjCreate.appendChild(objTB);
		    
		    if (getObjDur() != -1L){
		    	Element ObjDur = xmlDoc.createElement("ObjDur");
		    	ObjDur.appendChild(xmlDoc.createTextNode(String.valueOf(getObjDur())));
			    mosObjCreate.appendChild(ObjDur);
		    }
		        
		    if (getTime().equals("")){
		    	Element time = xmlDoc.createElement("time");
		    	time.appendChild(xmlDoc.createTextNode(getTime()));
			    mosObjCreate.appendChild(time);
		    }
		    
		    if (getCreatedBy().equals("")){
		    	Element createdBy = xmlDoc.createElement("createdBy");
		    	createdBy.appendChild(xmlDoc.createTextNode(getCreatedBy()));
			    mosObjCreate.appendChild(createdBy);
		    }
		    
		    if (getDescription().equals("")){
		    	Element description = xmlDoc.createElement("description");
		    	description.appendChild(xmlDoc.createTextNode(getDescription()));
			    mosObjCreate.appendChild(description);
		    }
	}
	public String getObjSlug(){return objSlug;}
	public String getObjGroup(){return objGroup;}
	public String getTime(){return time;}
	public String getCreatedBy(){return createdBy;}
	public String getDescription(){return description;}
	public long getObjTB(){return objTB;}
	public mosmessages.defined.ObjType getObjType(){return objType;}
	public long getObjDur(){return objDur;}
	public MosObjCreate setObjSlug(String arg){
		if ( arg != null ){
			objSlug = arg;
		}
		return this;
	}
	public MosObjCreate setObjGroup(String arg){
		if ( arg != null ){
			objGroup = arg;
		}
		return this;
	}
	public MosObjCreate setTime(String arg){
		if ( arg != null ){
			time = arg;
		}
		return this;
	}
	public MosObjCreate setCreatedBy(String arg){
		if ( arg != null ){
			createdBy = arg;
		}
		return this;
	}
	public MosObjCreate setDescription(String arg){
		if ( arg != null ){
			description = arg;
		}
		return this;
	}
	public MosObjCreate setObjDur(long arg){
		objDur = arg;
		return this;
	}
	public MosObjCreate setObjType(mosmessages.defined.ObjType arg){
		objType = arg;
		return this;
	}
	public MosObjCreate setObjTB(long arg){
		objTB = arg;
		return this;
	}
	public MosObjCreate setTimeAuto(){
		time = Instant.now().toString(); 
		return this;
	}
}
