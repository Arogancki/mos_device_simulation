package console;

import mosmessages.profile0.Heartbeat;
import mosmessages.profile0.ListMachInfo;
import mosmessages.profile0.ReqMachInfo;
import mosmessages.profile1.MosAck;
import mosmessages.profile1.MosListAll;
import mosmessages.profile1.MosObj;
import mosmessages.profile1.MosReqAll;
import mosmessages.profile1.MosReqObj;
import mosmessages.profile2.RoAck;
import mosmessages.profile2.RoCreate;
import mosmessages.profile2.RoDelete;
import mosmessages.profile2.RoElementAction;
import mosmessages.profile2.RoElementStat;
import mosmessages.profile2.RoList;
import mosmessages.profile2.RoMetadataReplace;
import mosmessages.profile2.RoReadyToAir;
import mosmessages.profile2.RoReplace;
import mosmessages.profile2.RoReq;

public class Message {
	private static String[] args;
	static void start(String[] input){
		args = input;
		for (int index = 0; index < args.length; index++){
			//profile 1
			if (args[index].equalsIgnoreCase("heartbeat")){
				new Heartbeat().activeExpectingReply().Send();
			}
			else if (args[index].equalsIgnoreCase("reqmachinfo")){
				new ReqMachInfo().Send();
			}
			else if (args[index].equalsIgnoreCase("listmachinfo")){
				new ListMachInfo().Send();
			}
			else if (args[index].equalsIgnoreCase("mosack")){
				index = mosack(index);
			}
			//profile 2
			else if (args[index].equalsIgnoreCase("moslistall")){
				new MosListAll().Send();
			}
			else if (args[index].equalsIgnoreCase("mosobj")){
				index = mosobj(index);
			}
			else if (args[index].equalsIgnoreCase("mosreqall")){
				index = mosreqall(index);
			}
			else if (args[index].equalsIgnoreCase("mosreqobj")){
				index = mosreqobj(index);
			}
			//profile 3
			else if (args[index].equalsIgnoreCase("roack")){
				index = roack(index);
			}
			else if (args[index].equalsIgnoreCase("rocreate")){
				index = rocreate(index);
			}
			else if (args[index].equalsIgnoreCase("roreplace")){
				index = roreplace(index);
			}
			else if (args[index].equalsIgnoreCase("rodelete")){
				index = rodelete(index);
			}
			else if (args[index].equalsIgnoreCase("roreq")){
				index = roreq(index);
			}
			else if (args[index].equalsIgnoreCase("rolist")){
				index = rolist(index);
			}
			else if (args[index].equalsIgnoreCase("rometadatareplace")){
				index = rometadatareplace(index);
			}
			else if (args[index].equalsIgnoreCase("roelementstat")){
				index = roelementstat(index);
			}
			else if (args[index].equalsIgnoreCase("roelementaction")){
				index = roelementaction(index);
			}
			else if (args[index].equalsIgnoreCase("roreadytoair")){
				index = roreadytoair(index);
			}
			//profile 4
			//profile 5
			//profile 6
			//profile 7
			else {
				System.out.println("Unsupported option: " + args[index] + ".");
				break;
			}
		}
	}
	private static int mosack(int index) {
		MosAck mess = new MosAck();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("objrev") || args[index].equalsIgnoreCase("-objrev")){
				if (++index < args.length){
					mess.setObjRev(args[index]);
				}
				else{
					System.out.println("Missing option for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("objid") || args[index].equalsIgnoreCase("-objid")){
				if (++index < args.length){
					mess.setObjectUID(args[index]);
				}
				else{
					System.out.println("Missing option for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("statusdescription") || args[index].equalsIgnoreCase("-statusdescription")){
				if (++index < args.length){
					mess.setStatusDescription(args[index]);
				}
				else{
					System.out.println("Missing option for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("status") || args[index].equalsIgnoreCase("-status")){
				if (++index < args.length){
					mosmessages.defined.Status status = mosmessages.defined.Status.getFromString(args[index]);
					if (status!=null){
						mess.setStatus(status);
					}
					else{
						System.out.println("Invalid status: " + status + "!");
					}
				}
				else{
					System.out.println("Missing option for " + args[index-1] + "!");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int mosobj(int index){
		MosObj mess = new MosObj();
		mossimulator.MosObj obj = new mossimulator.MosObj();
		mess.setMosObj(obj);
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("objid") || args[index].equalsIgnoreCase("-objid")){
				if (++index < args.length){
					if (mossimulator.MosObj.Contains(args[index])){
						obj = mossimulator.MosObj.getMosObj(args[index]);
					}
					else{
						obj.setObjID(args[index]);
					}
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("objslug") || args[index].equalsIgnoreCase("-objslug")){
				if (++index < args.length){
					obj.setObjSlug(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("mosabstract") || args[index].equalsIgnoreCase("-mosabstract")){
				if (++index < args.length){
					obj.setMosAbstract(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("objgroup") || args[index].equalsIgnoreCase("-objgroup")){
				if (++index < args.length){
					obj.setObjGroup(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("objrev") || args[index].equalsIgnoreCase("-objrev")){
				if (++index < args.length){
					obj.setObjRev(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("createdby") || args[index].equalsIgnoreCase("-createdby")){
				if (++index < args.length){
					obj.setCreatedBy(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("created") || args[index].equalsIgnoreCase("-created")){
				if (++index < args.length){
					obj.setCreated(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("changedby") || args[index].equalsIgnoreCase("-changedby")){
				if (++index < args.length){
					obj.setChangedBy(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("changed") || args[index].equalsIgnoreCase("-changed")){
				if (++index < args.length){
					obj.setChanged(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("description") || args[index].equalsIgnoreCase("-description")){
				if (++index < args.length){
					obj.setDescription(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("objtb") || args[index].equalsIgnoreCase("-objtb")){
				if (++index < args.length){
					try {
						obj.setObjTB(Long.valueOf(args[index]));
					}
					catch(NumberFormatException e){
						System.out.println("Wrong argument: " + args[index] + ". Excepted Long.");
					}
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("objdur") || args[index].equalsIgnoreCase("-objdur")){
				if (++index < args.length){
					try {
						obj.setObjDur(Long.valueOf(args[index]));
					}
					catch(NumberFormatException e){
						System.out.println("Wrong argument: " + args[index] + ". Excepted Long.");
					}
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("status") || args[index].equalsIgnoreCase("-status")){
				if (++index < args.length){
					mosmessages.defined.Status status = mosmessages.defined.Status.getFromString(args[index]);
					if (status!=null){
						obj.setStatus(status);
					}
					else{
						System.out.println("Invalid status: " + status + "!");
					}
				}
				else{
					System.out.println("Missing option for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("objair") || args[index].equalsIgnoreCase("-objair")){
				if (++index < args.length){
					mosmessages.defined.ObjAir objAir = mosmessages.defined.ObjAir.getFromString(args[index]);
					if (objAir!=null){
						obj.setObjAir(objAir);
					}
					else{
						System.out.println("Invalid status: " + objAir + "!");
					}
				}
				else{
					System.out.println("Missing option for " + args[index-1] + "!");
				}
			}
			else if (args[index].equalsIgnoreCase("objtype") || args[index].equalsIgnoreCase("-objtype")){
				if (++index < args.length){
					mosmessages.defined.ObjType objType = mosmessages.defined.ObjType.getFromString(args[index]);
					if (objType!=null){
						obj.setObjType(objType);
					}
					else{
						System.out.println("Invalid status: " + objType + "!");
					}
				}
				else{
					System.out.println("Missing option for " + args[index-1] + "!");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int mosreqobj(int index){
		MosReqObj mess = new MosReqObj();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("objid") || args[index].equalsIgnoreCase("-objid")){
				if (++index < args.length){
					mess.setObjID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + "!");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int mosreqall(int index){
		MosReqAll mess = new MosReqAll();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("pause") || args[index].equalsIgnoreCase("-pause")){
				if (++index < args.length){
					try {
						mess.setPause(Long.valueOf(args[index]));
					}
					catch (NumberFormatException e){
						System.out.println("Wrong argument: " + args[index] + ". Excepted Long.");
					}
					
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int roack(int index){
		RoAck mess = new RoAck();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int rocreate(int index){
		RoCreate mess = new RoCreate ();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int roreplace(int index){
		RoReplace mess = new RoReplace();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int rodelete(int index){
		RoDelete mess = new RoDelete();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int roreq(int index){
		RoReq mess = new RoReq();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int rolist(int index){
		RoList mess = new RoList();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int rometadatareplace(int index){
		RoMetadataReplace mess = new RoMetadataReplace();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("roSlug") || args[index].equalsIgnoreCase("-roSlug")){
				if (++index < args.length){
					mess.setRoSlug(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("roChannel") || args[index].equalsIgnoreCase("-roChannel")){
				if (++index < args.length){
					mess.setRoChannel(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("roEdStart") || args[index].equalsIgnoreCase("-roEdStart")){
				if (++index < args.length){
					mess.setRoEdStart(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("roEdDur") || args[index].equalsIgnoreCase("-roEdDur")){
				if (++index < args.length){
					mess.setRoEdDur(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("macroOut") || args[index].equalsIgnoreCase("-macroOut")){
				if (++index < args.length){
					mess.setMacroOut(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("roTrigger") || args[index].equalsIgnoreCase("-roTrigger")){
				if (++index < args.length){
					mess.setRoTrigger(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("macroIn") || args[index].equalsIgnoreCase("-macroIn")){
				if (++index < args.length){
					mess.setMacroIn(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int roelementstat(int index){
		RoElementStat mess = new RoElementStat ();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("storyID") || args[index].equalsIgnoreCase("-storyID")){
				if (++index < args.length){
					mess.setStoryID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("itemID") || args[index].equalsIgnoreCase("-itemID")){
				if (++index < args.length){
					mess.setItemID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("objID") || args[index].equalsIgnoreCase("-objID")){
				if (++index < args.length){
					mess.setObjID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("itemChannel") || args[index].equalsIgnoreCase("-itemChannel")){
				if (++index < args.length){
					mess.setItemChannel(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("status") || args[index].equalsIgnoreCase("-status")){
				if (++index < args.length){
					mess.setStatus(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("time") || args[index].equalsIgnoreCase("-time")){
				mess.setTimeNow();
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int roelementaction(int index){
		RoElementAction mess = new RoElementAction();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("operation") || args[index].equalsIgnoreCase("-operation")){
				if (++index < args.length){
					mess.setRoElementAction(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("storyID") || args[index].equalsIgnoreCase("-storyID")){
				if (++index < args.length){
					mess.setTargetStoryID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("itemID") || args[index].equalsIgnoreCase("-itemID")){
				if (++index < args.length){
					mess.setTargetItemID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("addStory") || args[index].equalsIgnoreCase("-addStory")){
				if (++index < args.length){
					mess.addStory(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else if (args[index].equalsIgnoreCase("addItem") || args[index].equalsIgnoreCase("-addItem")){
				if (++index < args.length){
					mess.addItem(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
	private static int roreadytoair(int index){
		RoReadyToAir mess = new RoReadyToAir();
		while(true){
			index++;
			if (index >= args.length){
				mess.Send();
				break;
			}
			if (args[index].equalsIgnoreCase("roID") || args[index].equalsIgnoreCase("-roID")){
				if (++index < args.length){
					mess.setRoID(args[index]);
				}
				else{
					System.out.println("Missing pause value for " + args[index-1] + ".");
				}
			}
			else{
				mess.Send();
				break;
			}
		}
		return index;
	}
}
