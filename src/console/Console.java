package console;

public class Console {
	public static void start(String[] args){
		if (args.length < 1){
			printManual();
		}
		else{
			String type = args[0].toLowerCase();
			type = type.charAt(0)=='-' ? type.substring(1) : type;
			if (type.equals("h") || type.equals("help") || type.equals("man") || type.equals("manual")){
				printManual();
			}
			else if(type.equals("m") || type.equals("message") || type.equals("mes") || type.equals("mos")){
				if (args.length < 2){
					System.out.println("No enough arguments!");
				}
				else
					Message.start(args[1]);
			}
			else if(type.equals("p") || type.equals("-print") || type.equals("prn") || type.equals("prt")){
				if (args.length < 2){
					System.out.println("No enough arguments!");
				}
				else
					Print.start(args[1]);
			}
			else if(type.equals("s") || type.equals("settings") || type.equals("set")){
				if (args.length < 2){
					System.out.println("No enough arguments!");
				}
				else 
					Settings.start(args[1]);
			}
			else{
				System.out.println("Unsupported option: " + type);
			}
		}
	}
	private static void printManual(){
		System.out.println("Manual page: https://avid-ondemand.atlassian.net/wiki/spaces/AMPEG/pages/216043547/MOS+simulator+implementation+-+Command+line+utility");
	}
}
