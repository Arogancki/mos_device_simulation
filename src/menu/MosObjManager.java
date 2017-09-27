package menu;

import java.util.Scanner;

public class MosObjManager extends Menu{
	MosObjManager() {
		super("MOSObj manager", createSubMenu());
	}
	private static Menu[] createSubMenu(){
		Menu[] result = { new Add(), new Print() };
		return result;
	}
	private static class Add extends Menu{
		Add() {
			super("Add new", null);
		}
		protected void Active(){
			
			mossimulator.MosObj mosObj = new mossimulator.MosObj();
			String input;
			
			System.out.println("Enter objSlug:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				mosObj.setObjSlug(input);
			}
			
			System.out.println("Enter mosAbstract:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				mosObj.setMosAbstract(input);
			}
			
			System.out.println("Enter objGroup:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				mosObj.setObjGroup(input);
			}
			
			System.out.println("Enter objType:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				mosObj.setObjType(mosmessages.defined.ObjType.getFromString(input.toUpperCase()));
			}
			
			System.out.println("Enter objTB:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				try{
					mosObj.setObjTB(Long.valueOf(input));
				}
				catch(NumberFormatException e){
					System.out.println("Wrong format.");
				}
			}
			
			System.out.println("Enter objRev:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				mosObj.setObjRev(input);
			}
			
			System.out.println("Enter objDur:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				try{
					mosObj.setObjDur(Long.valueOf(input));
				}
				catch(NumberFormatException e){
					System.out.println("Wrong format.");
				}
			}
			
			System.out.println("Enter Status:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				mosObj.setStatus(mosmessages.defined.Status.getFromString(input.toUpperCase()));
			}
			
			System.out.println("Enter objAir:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				mosObj.setObjAir(mosmessages.defined.ObjAir.getFromString(input.toUpperCase()));
			}
			
			System.out.println("Enter createdBy:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				mosObj.setCreatedBy(input);
			}
			
			System.out.println("Enter description:");
			input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				mosObj.setDescription(input);
			}
			
			System.out.println("New MosObj created.");
		}
	}
	private static class Print extends Menu{
		Print() {
			super("Print", null);
		}
		protected void Active(){
			if (mossimulator.MosObj.isEmpty())
				System.out.println("MOSObj not found.");
			else{
				System.out.print(mossimulator.MosObj.printAllObj());
			}
		}
	}
}
