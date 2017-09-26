package console;

import mosmessages.profile0.Heartbeat;
import mosmessages.profile0.ListMachInfo;
import mosmessages.profile0.ReqMachInfo;
import mosmessages.profile1.MosAck;
import mosmessages.profile1.MosListAll;
import mosmessages.profile1.MosObj;
import mosmessages.profile1.MosReqAll;
import mosmessages.profile1.MosReqObj;

public class Message {
	private static String[] args;
	static void start(String input){
		args = input.toLowerCase().split(" ");
		for (int index = 0; index < args.length; index++){
			if (args[index].equalsIgnoreCase("heartbeat")){
				new Heartbeat().Send();
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
			else {
				System.out.println("Unsupported option: " + args[index] + "!");
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
					obj.setChanged(args[index]);
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
}
