package console;

//import java.util.Scanner;

public class Console {
	public static void start(String[] args){
		boolean powerSwitch = false;
		do{
			if (args.length < 1){
				printManual();
				break;
				/*
				if (!powerSwitch){
					System.out.println("Interactive mode (type 'q' to quit).");
					powerSwitch = true;
				}
				do {
					args = (new Scanner(System.in)).nextLine().split(" ");
				}while(args[0].length() <= 0);
				if (args[0].equalsIgnoreCase("q")){
					break;
				}
				*/
			}
			String type = args[0].toLowerCase();
			type = type.charAt(0)=='-' ? type.substring(1) : type;
			if (type.equals("h") || type.equals("help") || type.equals("man") || type.equals("manual")){
				printManual();
			}
			else if(type.equals("p") || type.equals("-print") || type.equals("prn") || type.equals("prt")){
				if (args.length < 2){
					System.out.println("No enough arguments for print!");
				}
				else
					Print.start(args);
			}
			else if(type.equals("s") || type.equals("settings") || type.equals("set")){
				if (args.length < 2){
					System.out.println("No enough arguments for settings!");
				}
				else 
					Settings.start(args);
			}
			else{
				Message.start(args);
			}
			String[] empty = {};
			args = empty;
		}while(powerSwitch);
	}
	private static void printManual(){
		System.out.println("Manual page: https://avid-ondemand.atlassian.net/wiki/spaces/AMPEG/pages/216043547/MOS+simulator+implementation+-+Command+line+utility");
	}
}
