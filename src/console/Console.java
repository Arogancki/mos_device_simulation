package console;

public class Console {
	static void start(String[] args){
		if (args.length < 1){
			System.out.println("No enough arguments!");
		}
		else{
			String type = args[0].toLowerCase();
			type = type.charAt(0)=='-' ? type.substring(1) : type;
			if (type.equals("h") || type.equals("help") || type.equals("man") || type.equals("manual")){
				//TODO manual page
				System.out.println("Manual in progress");
			}
			else{
				if (args.length < 2){
					System.out.println("No enough arguments!");
				}
				else{
					if(type.equals("m") || type.equals("message") || type.equals("mes") || type.equals("mos")){
						Message.start(args[1]);
					}
					else if(type.equals("p") || type.equals("-print") || type.equals("prn") || type.equals("prt")){
						Print.start(args[1]);
					}
					else if(type.equals("s") || type.equals("settings") || type.equals("set")){
						Settings.start(args[1]);
					}
				}
			}
		}
	}
}
