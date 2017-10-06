package console;

import mossimulator.Model;

public class Settings {
	static void start(String[] args){
		for (int index=1; index < args.length; index += 2){
			String type = args[index];
			type = type.charAt(0)=='-' ? type.substring(1) : type;
			if (type.equalsIgnoreCase("resettime") || type.equalsIgnoreCase("reset_time") || type.equalsIgnoreCase("restart") || type.equalsIgnoreCase("res") || type.equalsIgnoreCase("reset") || type.equalsIgnoreCase("time")){
				Model.resetTime();
				System.out.println("Operational time reseted.");
				index--;
			}
			else if (type.equalsIgnoreCase("s") || type.equalsIgnoreCase("show") ){
				System.out.println("Target host: " + Model.TARGETHOST + ".");	
				System.out.println("Host port: " + Model.TARGETPORT + ".");	
				System.out.println("MOSID: " + Model.MOSID + ".");
				System.out.println("NCSID: " + Model.NCSID + ".");
				System.out.println("Maximum waiting time for response: " + Model.SECTOWAIT + " (seconds).");	
				System.out.println("Send checking heartbeat after : " + Model.heartbeatSpace + " (miliseconds).");	
				System.out.println("Maximum retransmissions: " + Model.RETRANSMISSON + ".");
				System.out.println("MessageID: " + Model.messageID + ".");
				System.out.println("Lower port: " + Model.getLowerPort() + ".");
				System.out.println("Upper port: " + Model.getUpperPort() + ".");
				long nowMilli = System.currentTimeMillis() - Model.STARTDATE;
				long miliseconds = nowMilli % 1000;
				long seconds = (nowMilli / 1000) % 100;
				long minutes = (nowMilli / 60000) % 60;
				long hours = (nowMilli / 3600000) % 24;
				long days = nowMilli / 86400000;
				System.out.println(
					"Operational Time: "	
					+ (days == 0 ? "" : days + "d ")
					+ (hours == 0 ? "" : hours + "h ")
					+ (minutes == 0 ? "" : minutes + "m ") + seconds + "."
					+ miliseconds + "s"
				);
				index--;
			}			
			else {
				int optionIndex=index+1;
				if (optionIndex >= args.length ){
					System.out.println("Missing option form argument: " + type + ".");
					break;
				}
				else{
					String option = args[optionIndex];
					if (type.equalsIgnoreCase("h") || type.equalsIgnoreCase("host")){
						System.out.println("Target host " + Model.TARGETHOST + " changed to " + option + ".");
						Model.setTARGETHOST(option);
					}
					else if (type.equalsIgnoreCase("hp") || type.equalsIgnoreCase("hostport")){
						try {
							int intt = Integer.valueOf(option);
							System.out.println("Target host's port " + Model.TARGETPORT + " changed to " + option + ".");
							Model.setTARGETPORT(intt);
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + " Excepted Int.");
							break;
						}
					}
					else if (type.equalsIgnoreCase("ms") || type.equalsIgnoreCase("messagespace")){
						try {
							int intt = Integer.valueOf(option);
							System.out.println("Send checking heartbeat time " + Model.heartbeatSpace + " changed to " + option + ".");
							Model.setHeartbeatSpace(intt);
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + " Excepted Int.");
							break;
						}
					}
					else if (type.equalsIgnoreCase("mosid") || type.equalsIgnoreCase("mid")){
						System.out.println("MOSID " + Model.MOSID + " changed to " + option + ".");
						Model.setMOSID(option);
					}
					else if (type.equalsIgnoreCase("ncsid") || type.equalsIgnoreCase("nid")){
						System.out.println("NCSID " + Model.NCSID + " changed to " + option + ".");
						Model.setNCSID(option);
					}
					else if (type.equalsIgnoreCase("wait") || type.equalsIgnoreCase("w")){
						try {
							Long lon = Long.valueOf(option);
							System.out.println("Maximum waiting time for response " + Model.SECTOWAIT + " changed to " + option + " (seconds).");
							Model.setSECTOWAIT(lon);
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + " Excepted Long.");
							break;
						}
					}
					else if (type.equalsIgnoreCase("retransmissions") || type.equalsIgnoreCase("retransmission") || type.equalsIgnoreCase("r")){
						try {
							int in = Integer.parseInt(option);
							System.out.println("Maximum retransmissions " + Model.RETRANSMISSON + " changed to " + option + ".");
							Model.setRETRANSMISSON(in);
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + " Excepted int.");
							break;
						}
					}
					else if (type.equalsIgnoreCase("messageid") || type.equalsIgnoreCase("mes") || type.equalsIgnoreCase("mesid")){
						try {
							int in = Integer.parseInt(option);
							System.out.println("MessageID " + Model.messageID + " changed to " + option + ".");
							Model.setmessageID(in);
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + " Excepted int.");
							break;
						}
					}
					else if (type.equalsIgnoreCase("lp") || type.equalsIgnoreCase("lower")){
						try {
							int in = Integer.parseInt(option);
							System.out.println("Lower port " + Model.getLowerPort() + " changed to " + option + ".");
							Model.setLower(in);
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + " Excepted int.");
							break;
						}
					}
					else if (type.equalsIgnoreCase("up") || type.equalsIgnoreCase("upper")){
						try {
							int in =Integer.parseInt(option);
							System.out.println("Upper port " + Model.getUpperPort() + " changed to " + option + ".");
							Model.setUpper(in);
						}
						catch (NumberFormatException e){
							System.out.println("Wrong format for argument: " + type + " Excepted int.");
							break;
						}
					}
					else{
						System.out.println("Unsupported option for Settings: " + type);
						break;
					}
				}
			}
		}
	}
}
