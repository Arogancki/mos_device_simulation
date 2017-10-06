package console;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mosmessages.profile1.MosAck;

//import java.util.Scanner;

public class Console {
	public static boolean IS_INTERACTIVE_MODE_ON = false;
	public static void start(String[] args){
		boolean powerSwitch = true;
		if (args.length < 1){
			IS_INTERACTIVE_MODE_ON = true;
		}
		System.out.println("type 'q' to quit.");
		new MosAck(); // to iniciate all static field etc
		do{
			if (args.length < 1){
				do {
					ArrayList<String> list = new ArrayList<String>();
					Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher((new Scanner(System.in)).nextLine());
					while (m.find())
						list.add(m.group(1).replace("\"", "")); 
					args = new String[list.size()];
					args = list.toArray(args);
				}while(args[0].length() <= 0);
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
	private static final String manual = "Mos Simulator - sends mos protocol (v.2.8.4) messages using web sockets and the XML format. The supported character for encoding messages is ISO 10646 (Unicode) in UCS-2, \"big endian\". After sending a message, it holds the socket open till all expected messages are sent/received or gets timeout. The simulator uses .ser files for storing messages history and settings.\n"
			+"Requirements for MOS message exchange:\n"
			+"\n"
			+"1. Must be able to \"Ping\" the remote machine hosting the application with which you wish to communicate.\n"
			+"2. Must be able to establish a socket connection with the remote application.\n"
			+"\n"
			+"-s, -set, -settings [attributes]\n"
			+"\tchanges the simulator settings\n"
			+"\tattributes:\n"
			+"\t\thost [value]\n"
			+"\t\t\tchanges a target host to [value].\n"
			+"\t\tmosid [value]\n"
			+"\t\t\tchanges the simulator MosID to [value].\n"
			+"\t\tncsid [value]\n"
			+"\t\t\tchanges the simulator NcsID to [value].\n"
			+"\t\twait [value]\n"
			+"\t\t\tchanges a max waiting time for response to [value].\n"
			+"\t\tretransmissions [value]\n"
			+"\t\t\tchanges a max amount of retransmissions to [value].\n"
			+"\t\tmessageid [value]\n"
			+"\t\t\tchanges a messageID to value.\n"
			+"\t\tlower [value]\n"
			+"\t\t\tchanges a Mos lower port to [value].\n"
			+"\t\tupper [value]\n"
			+"\t\t\tchanges a Mos upper port to [value].\n"
			+"\t\tresettime\n"
			+"\t\t\tresets the simulator operational time.\n"
			+"\t\tshow\n"
			+"\t\t\tshows the simulator current settings.\n"
			+"\tExample:\n"
			+"\t\tsimulator.bat -settings host 10.105.250.217 mosid \"Mos Simulator\"\n"
			+"\n"
			+"-p, -prn, -print [attributes] [n:m] [i]\n"
			+"\tprints selected messages from a message 'n' to message 'm' or just the message with index 'i'. If 'n' isn't specified, starts from the first message. If 'm' isn't specified, ends on the last message. If a given number is negative, index messages from the end.\n"
			+"\tattributes:\n"
			+"\t-delete, -d\n"
			+"\t\tdeletes printed messages from the history.\n"
			+"\t-body, -b\n"
			+"\t\tprints messages body.\n"
			+"\t-read, -r\n"
			+"\t\tprints the well formed, user friendly messages body.\n"
			+"\tExample:\n"
			+"\t\tsimulator.bat -print -b -r :2 4:6 8 -2: :\n"
			+"\n"
			+"-[message]\n"
			+"\tSends message with given attributes and receives specific response. The message is printed and added to history after sending/receiving. Attributes for all messages are optional. If they are not speified, default values are used.\n"
			+"\tCurrently supported messages:\n"
			+"\tprofile 0:\n"
			+"\t\theartbeat\n"
			+"\t\t\tverifies network and application continuity.\n"
			+"\t\t\tResponse: heartbeat\n"
			+"\t\treqMachInfo\n"
			+"\t\t\tdetermines informations about an NCS or MOS.\n"
			+"\t\t\tResponse: listMachInfo\n"
			+"\t\tlistMachInfo\n"
			+"\t\t\tsends informations about the Simulator.\n"
			+"\t\t\tResponse: None\n"
			+"\tprofile 1:\n"
			+"\t\tmosAck [attributes]\n"
			+"\t\t\tan acknowledgement response to various types of messages.\n"
			+"\t\t\tattributes:\n"
			+"\t\t\t-objid [value]\n"
			+"\t\t\t\tObject UID, 128 chars max.\n"
			+"\t\t\t-objrev [value]\n"
			+"\t\t\t\tObject Revision Number, 999 max.\n"
			+"\t\t\t-status [value]\n"
			+"\t\t\t\tOptions are \"ACK\", \"NACK\", \"OK\", \"NEW\", \"UPDATED\", \"MOVED\", \"BUSY\", \"DELETED\", \"NCS CTRL\", \"MANUAL CTRL\", \"READY\", \"NOT READY\", \"PLAY,\" \"STOP\".\n"
			+"\t\t\t-statusdescription [value]\n"
			+"\t\t\t\tTextual description of status, 128 chars max.\n"
			+"\t\t\tResponse: None\n"
			+"\t\tmosListAll\n"
			+"\t\t\tsends all MOS objects descriptions\n"
			+"\t\t\tResponse: mosAck\n"
			+"\t\tmosObj [attributes]\n"
			+"\t\t\tinformations that describes a unique MOS Object.\n"
			+"\t\t\tattributes:\n"
			+"\t\t\t-objid [value]\n"
			+"\t\t\t\tObject UID, 128 chars max.\n"
			+"\t\t\t-objslug [value]\n"
			+"\t\t\t\tTextual object description, 128 chars max.\n"
			+"\t\t\t-mosabstract [value]\n"
			+"\t\t\t\tAbstract of the Object intended for display by the NCS.\n"
			+"\t\t\t-objgroup [value]\n"
			+"\t\t\t\tDefinition of the values for objGroup is left to the configuration and agreement of MOS connected equipment. The intended use is for site configuration of a limited number of locally named storage folders in the NCS or MOS.\n"
			+"\t\t\t-objrev [value]\n"
			+"\t\t\t\tObject Revision Number: 999 max.\n"
			+"\t\t\t-createdby [value]\n"
			+"\t\t\t\tName of the person or process that created the object, 128 chars max.\n"
			+"\t\t\t-created [value]\n"
			+"\t\t\t\tCreation Time/Date.\n"
			+"\t\t\t-changedby [value]\n"
			+"\t\t\t\tName of the person or process that changed the object, 128 chars max.\n"
			+"\t\t\t-changed [value]\n"
			+"\t\t\t\tChanged Time/Date.\n"
			+"\t\t\t-description [value]\n"
			+"\t\t\t\tText description of the MOS object.\n"
			+"\t\t\t-objtb [value]\n"
			+"\t\t\t\tThe sampling rate of the object in samples per second, 0XFFFFFFFF MAX\n"
			+"\t\t\t-objdur [value]\n"
			+"\t\t\t\tThe number of samples contained in the object. 0XFFFFFFFF MAX\n"
			+"\t\t\t-status [value]\n"
			+"\t\t\t\tOptions are \"ACK\", \"NACK\", \"OK\", \"NEW\", \"UPDATED\", \"MOVED\", \"BUSY\", \"DELETED\", \"NCS CTRL\", \"MANUAL CTRL\", \"READY\", \"NOT READY\", \"PLAY,\" \"STOP\".\n"
			+"\t\t\t-objair [value]\n"
			+"\t\t\t\tChoices are \"READY\", \"NOT READY\".\n"
			+"\t\t\t-objtype [value]\n"
			+"\t\t\t\tChoices are \"STILL\", \"AUDIO\", \"VIDEO\".\n"
			+"\t\t\tResponse: mosAck\n"
			+"\t\tmosReqAll [attributes]\n"
			+"\t\t\trequest the MOS to send a mosObj message for every Object in the MOS. \n"
			+"\t\t\tattributes:\n"
			+"\t\t\t-pause [value]\n"
			+"\t\t\t\twhen greater than zero, indicates the number of seconds to pause between individual mosObj messages. Otherwise all objects will be sent using the mosListAll message.\n"
			+"\t\t\tResponse: mosAck then mosListAll or mosObj\n"
			+"\t\tmosReqObj [attributes]\n"
			+"\t\t\tthe request of description of an object. If objID is found gets mosObj, otherwise mosAck.\n"
			+"\t\t\tattributes:\n"
			+"\t\t\t-objid [value]\n"
			+"\t\t\t\tThe requested object UID, 128 chars max.\n"
			+"\t\t\tResponse: mosObj or mosAck\n"
			+"\tExample:\n"
			+"\t\tsimulator.bat mosack -objrev 123 -status NACK -statusdescription \"example description\"\n"
			+"\n"
			+"Usefull links:\n"
			+"MOS protocol v.2.8.4 specification: http://mosprotocol.com/wp-content/MOS-Protocol-Documents/MOS-Protocol-2.8.4-Current.htm";
}
