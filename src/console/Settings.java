package console;

import mossimulator.Model;

public class Settings {
	static void start(String input){
		String[] args = input.split(" ");
		if (args.length < 2){
			System.out.println("No enough arguments!");
		}
		else{
			for (int index=0; index < args.length; index += 2){
				String type = args[index].toLowerCase();
				type = type.charAt(0)=='-' ? type.substring(1) : type;
				int optionIndex=index+1;
				if (optionIndex >= args.length){
					System.out.println("Missing option form argument: " + type + ".");
				}
				else{
					String option = args[optionIndex];
					if (type.equals("h") || type.equals("host")){
						Model.setTARGETHOST(option);
						System.out.println("Target host " + Model.TARGETHOST + " changed to " + option + ".");	
					}
					else if (type.equals("mosid") || type.equals("mid")){
						Model.setMOSID(option);
						System.out.println("MOSID " + Model.MOSID + " changed to " + option + ".");
					}
					else if (type.equals("ncsid") || type.equals("nid")){
						Model.setMOSID(option);
						System.out.println("NCSID " + Model.NCSID + " changed to " + option + ".");
					}
					else if (type.equals("wait") || type.equals("w")){
						try {
							Model.setSECTOWAIT(Long.valueOf(option));
							System.out.println("Maximum waiting time for response " + Model.SECTOWAIT + " changed to " + option + "(seconds).");
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + ".");
						}
					}
					else if (type.equals("retransmissions") || type.equals("retransmission") || type.equals("r")){
						try {
							Model.setRETRANSMISSON(Integer.parseInt(option));
							System.out.println("Maximum retransmissions " + Model.RETRANSMISSON + " changed to " + option + ".");
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + ".");
						}
					}
					else if (type.equals("messageid") || type.equals("mes") || type.equals("mesid")){
						try {
							Model.setRETRANSMISSON(Integer.parseInt(option));
							System.out.println("MessageID " + Model.messageID + " changed to " + option + ".");
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + ".");
						}
					}
					else if (type.equals("lp") || type.equals("lower")){
						try {
							Model.setLower(Integer.parseInt(option));
							System.out.println("Lower port " + Model.getLowerNu() + " changed to " + option + ".");
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + ".");
						}
					}
					else if (type.equals("up") || type.equals("upper")){
						try {
							Model.setUpper(Integer.parseInt(option));
							System.out.println("Upper port " + Model.getUpperNu() + " changed to " + option + ".");
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + ".");
						}
					}
					else if (type.equals("resettime") || type.equals("reset_time") || type.equals("restart") || type.equals("res") || type.equals("reset") || type.equals("time")){
						Model.STARTDATE = System.currentTimeMillis();
						System.out.println("Operational time reseted.");
					}
				}
			}
		}
	}
}
