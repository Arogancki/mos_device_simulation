package menu;

import java.util.Scanner;

public class SendMOSMessage extends Menu{
	SendMOSMessage() {
		super("Send MOS message", createSubMenu());
	}
	private static Menu[] createSubMenu(){
		Menu[] result = {new Profile0(), new Profile1(), new Profile2(), new Profile3(), new Profile4(), new Profile5(), new Profile6(), new Profile7()};
		return result;
	}
	private static class Profile0 extends Menu{
		Profile0() {
			super("Profile 0 – Basic Communication", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {new Heartbeat(), new ReqMachInfo(), new ListMachInfo()};
			return result;
		}
		private static class Heartbeat extends Menu{
			Heartbeat() {
				super("heartbeat", null);
			}
			protected void Active(){
				new mosmessages.profile0.Heartbeat().Send();
			}
		}
		private static class ReqMachInfo extends Menu{
			ReqMachInfo() {
				super("reqMachInfo", null);
			}
			protected void Active(){
				new mosmessages.profile0.ReqMachInfo().Send();
			}
		}
		private static class ListMachInfo extends Menu{
			ListMachInfo() {
				super("listMachInfo", null);
			}
			protected void Active(){
				new mosmessages.profile0.ListMachInfo().Send();
			}
		}
	}
	private static class Profile1 extends Menu{
		Profile1() {
			super("Profile 1 – Basic Object Workflow", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {new MOSACK(), new MOSObj(),new MOSReqObj(), new MOSReqAll(), new MOSListAll()};
			return result;
		}
		private static class MOSACK extends Menu{
			MOSACK() {
				super("mosAck", null);
			}
			protected void Active(){
				new mosmessages.profile1.MosAck().Send();
			}
		}
		private static class MOSObj extends Menu{
			MOSObj() {
				super("mosObj", null);
			}
			protected void Active(){
				if (mossimulator.MosObj.isEmpty()){
					System.out.println("MOSObj not fount.");
				}
				else{
					System.out.println("Enter MOSObj UID:");
					String input = (new Scanner(System.in)).nextLine().trim();
					if (input.length()>0){
						if (mossimulator.MosObj.Contains(input)){
							new mosmessages.profile1.MosObj().setMosObj(mossimulator.MosObj.getMosObj(input)).Send();						
						}
						else{
							System.out.println("MOSObj not fount");
						}
					}
				}
			}
		}
		private static class MOSReqObj extends Menu{
			MOSReqObj() {
				super("mosReqObj", null);
			}
			protected void Active(){
				System.out.println("Enter MOSObj UID:");
				String input = (new Scanner(System.in)).nextLine().trim();
				if (input.length()>0){
						new mosmessages.profile1.MosReqObj().setObjID(input).Send();	
				}
			}
		}
		private static class MOSReqAll extends Menu{
			MOSReqAll() {
				super("mosReqAll", null);
			}
			protected void Active(){
				new mosmessages.profile1.MosReqAll().Send();
			}
		}
		private static class MOSListAll extends Menu{
			MOSListAll() {
				super("mosListAll", null);
			}
			protected void Active(){
				new mosmessages.profile1.MosListAll().Send();
			}
		}
	}
	private static class Profile2 extends Menu{
		Profile2() {
			super("Profile 2 – Basic Running Order / Content List Workflow", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {};
			return result;
		}
	}
	private static class Profile3 extends Menu{
		Profile3() {
			super("Profile 3 – Advanced Object Based Workflow", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {new MosObjCreate()};
			return result;
		}
		private static class MosObjCreate extends Menu{
			MosObjCreate() {
				super("MosObjCreate", null);
			}
			protected void Active(){
				mosmessages.profile3.MosObjCreate message = new mosmessages.profile3.MosObjCreate();
				String input = null;
				System.out.println("Enter objSlug:");
				input = (new Scanner(System.in)).nextLine().trim();
				if (input.length()>0){
					message.setObjSlug(input);
				}
				System.out.println("Enter objGroup:");
				input = (new Scanner(System.in)).nextLine().trim();
				message.setObjGroup(input);
				System.out.println("Enter objType:");
				input = (new Scanner(System.in)).nextLine().trim();
				if (input.length()>0){
					message.setObjType(mosmessages.defined.ObjType.getFromString(input.toUpperCase()));
				}
				System.out.println("Enter objTB:");
				input = (new Scanner(System.in)).nextLine().trim();
				if (input.length()>0){
					try{
						message.setObjTB(Long.valueOf(input));
					}
					catch(NumberFormatException e){
						System.out.println("Wrong format.");
					}
				}
				System.out.println("Enter objDur:");
				input = (new Scanner(System.in)).nextLine().trim();
				if (input.length()>0){
					try{
						message.setObjDur(Long.valueOf(input));
					}
					catch(NumberFormatException e){
						System.out.println("Wrong format.");
					}
				}
				message.setTimeAuto();
				System.out.println("Enter createdBy:");
				input = (new Scanner(System.in)).nextLine().trim();
				if (input.length()>0){
					message.setCreatedBy(input);
				}
				System.out.println("Enter description:");
				input = (new Scanner(System.in)).nextLine().trim();
				if (input.length()>0){
					message.setDescription(input);
				}
				message.Send();
			}
		}
	}
	private static class Profile4 extends Menu{
		Profile4() {
			super("Profile 4 – Advanced RO/Content List Workflow", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {};
			return result;
		}
	}
	private static class Profile5 extends Menu{
		Profile5() {
			super("Profile 5 – Item Control", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {};
			return result;
		}
	}
	private static class Profile6 extends Menu{
		Profile6() {
			super("Profile 6 – MOS Redirection", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {};
			return result;
		}
	}
	private static class Profile7 extends Menu{
		Profile7() {
			super("Profile 7 – MOS RO/Content List Modification", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {};
			return result;
		}
	}
}