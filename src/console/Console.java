package console;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mosmessages.profile1.MosAck;

//import java.util.Scanner;

public class Console {
	public static void start(String[] args){
		boolean powerSwitch = true;
		if (args.length<1){
			mossimulator.Model.HostConnections();			
		}
		System.out.println("type 'q' to quit.");
		do{
			if (args.length < 1){
				do {
					ArrayList<String> list = new ArrayList<String>();
					Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher((new Scanner(System.in)).nextLine());
					while (m.find())
						list.add(m.group(1).replace("\"", "")); 
					args = new String[list.size()];
					args = list.toArray(args);
				}while(args.length<=0 || args[0].length() <= 0);
				if (args[0].equalsIgnoreCase("q")){
					break;
				}
			}
			String type = args[0].toLowerCase();
			type = type.charAt(0)=='-' ? type.substring(1) : type;
			if (type.equals("h") || type.equals("help") || type.equals("man") || type.equals("manual")){
				printManual();
			}
			else if(type.equals("p") || type.equals("print") || type.equals("prn") || type.equals("prt")){
				if (args.length < 2){
					System.out.println("No enough arguments for the print!");
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
		System.out.println(manual);
	}
	private static final String manual = "man: https://avid-ondemand.atlassian.net/wiki/spaces/AMPEG/pages/216043547/MOS+simulator+implementation+-+Command+line+utility";
}
