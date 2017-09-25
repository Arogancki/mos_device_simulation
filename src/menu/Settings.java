package menu;

import java.util.Scanner;

import mossimulator.Model;

public class Settings extends Menu{
	Settings() {
		super("Settings", createSubMenu());
	}
	private static Menu[] createSubMenu(){
		Menu[] result = {new ChangeIp() ,new ChangeMOSID(), new ChangeNCSID(), new ChangeSecToWait(), new ChangeRETRANSMISSON() };
		return result;
	}
	private static class ChangeIp extends Menu{
		ChangeIp(){
			super("Change target host", null);
		}
		protected void Active(){
			if (Model.TARGETHOST != "")
				System.out.println("Current target host: \"" + Model.TARGETHOST + "\"\nEnter new: ");
			String input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				Model.setTARGETHOST(input);
				System.out.println("Changed to: \"" + Model.TARGETHOST + "\"");
			}
			else{
				System.out.println("Nothing changed");
			}
		}
	}
	private static class ChangeMOSID extends Menu{
		ChangeMOSID(){
			super("Change MOS ID", null);
		}
		protected void Active(){
			if (Model.MOSID != "")
				System.out.println("Current MOS ID: \"" + Model.MOSID + "\"\nEnter new: ");
			String input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				Model.setMOSID(input);
				System.out.println("Changed to: \"" + Model.MOSID + "\"");
			}
			else{
				System.out.println("Nothing changed");
			}
		}
	}
	private static class ChangeNCSID extends Menu{
		ChangeNCSID(){
			super("Change NCS ID", null);
		}
		protected void Active(){
			if (Model.NCSID != "")
				System.out.println("Current NCS ID: \"" + Model.NCSID + "\"\nEnter new: ");
			String input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				Model.setNCSID(input);
				System.out.println("Changed to: \"" + Model.NCSID + "\"");
			}
			else{
				System.out.println("Nothing changed");
			}
		}
	}
	private static class ChangeSecToWait extends Menu{
		ChangeSecToWait(){
			super("Change time to wait", null);
		}
		protected void Active(){
			if (Model.NCSID != "")
				System.out.println("Current sec to wait for response: \"" + Model.SECTOWAIT + "\"\nEnter new: ");
			String input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				try{
					Model.setSECTOWAIT(Long.valueOf(input).longValue());
					System.out.println("Changed to: \"" + Model.SECTOWAIT + "\"");
				}
				catch(NumberFormatException e){
					System.out.println("Wrong format.");
				}
			}
			else{
				System.out.println("Nothing changed");
			}
		}
	}
	private static class ChangeRETRANSMISSON extends Menu{
		ChangeRETRANSMISSON(){
			super("Change amonth of retransmissions", null);
		}
		protected void Active(){
			if (Model.NCSID != "")
				System.out.println("Current amount of retransmissions: \"" + Model.RETRANSMISSON + "\"\nEnter new: ");
			String input = (new Scanner(System.in)).nextLine().trim();
			if (input.length()>0){
				try{
					Model.setRETRANSMISSON(Integer.parseInt(input));
					System.out.println("Changed to: \"" + Model.RETRANSMISSON + "\"");
				}
				catch(NumberFormatException e){
					System.out.println("Wrong format.");
				}
			}
			else{
				System.out.println("Nothing changed");
			}
		}
	}
}
