package console;

import java.util.Iterator;

import mosmessages.MosMessage;
import mossimulator.Model;
import mossimulator.Model.MessageInfo;

public class Print {
	static void start(String[] args){
		if (!Model.LoadList()){
			
		}
		else if (Model.messages.size()==0){
			System.out.println("Message list is empty.");
		}
		else{
			boolean deleteFlag = false;
			boolean bodyFlag = false;
			boolean readFlag = false;
			for (int index = 1; index < args.length; index++){
				if (args[index].equalsIgnoreCase("d") || args[index].equalsIgnoreCase("-d") || args[index].equalsIgnoreCase("delete") || args[index].equalsIgnoreCase("-delete")){
					deleteFlag = true;
				}
				else if (args[index].equalsIgnoreCase("b") || args[index].equalsIgnoreCase("-b") || args[index].equalsIgnoreCase("body") || args[index].equalsIgnoreCase("-body")){
					bodyFlag = true;
				}
				else if (args[index].equalsIgnoreCase("r") || args[index].equalsIgnoreCase("-r") || args[index].equalsIgnoreCase("read") || args[index].equalsIgnoreCase("-read") || args[index].equalsIgnoreCase("readable") || args[index].equalsIgnoreCase("-readable") || args[index].equalsIgnoreCase("readable") || args[index].equalsIgnoreCase("-readable")){
					readFlag = true;
				}
				else{
					String[] range = args[index].split(":");
					try {
						int start = range.length > 0 && range[0].length() > 0 ? Integer.parseInt(range[0]) : 1;
						int end = range.length > 1 ? Integer.parseInt(range[1]) : (args[index].contains(":")) ? Model.messages.size() : start;
						start = start 	< 0 ? start + Model.messages.size() 	: start-1;
						end = 	end 	< 0 ? end 	+ Model.messages.size() + 1 : end;
						if (start >= end){
							System.out.println("Starting index mustn't be grater than ending index!");
							continue;
						}
						for (Iterator<MessageInfo> iterator = Model.messages.listIterator(start); start<end && iterator.hasNext(); start++){
							Model.MessageInfo message = iterator.next();
							if (deleteFlag){
								Model.RemoveFromList(start);
								System.out.print("Deleted: \n");
							}
							System.out.print(message.toString());
							if (readFlag){
								System.out.print(":\n" + MosMessage.PrintXML(message.getDocument()));
							}
							if (bodyFlag){
								System.out.print("\n" + message.getString());
							}
							System.out.println();
						}
					}
					catch(NumberFormatException e){
						System.out.println("Wrong argument: " + args[index] + ". Excepted int.");
						break;
					}
					catch (IndexOutOfBoundsException e){
			    		System.out.println("Invalid index: " + args[index] + ".");
			    		break;
			    	}
				}
			}
		}
	}
}
