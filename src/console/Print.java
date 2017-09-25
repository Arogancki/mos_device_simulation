package console;

import java.util.Iterator;

import mosmessages.MosMessage;
import mossimulator.Model;
import mossimulator.Model.MessageInfo;

public class Print {
	static void start(String input){
		if (!Model.LoadList()){
			
		}
		else if (Model.messages.size()==0){
			System.out.println("Message list is empty.");
		}
		else{
			String[] args = input.toLowerCase().split(" ");
			boolean removeFlag = false;
			for (int index = 0; index < args.length; index++){
				if (args[index].equals("r") || args[index].equals("-r") || args[index].equals("remove") || args[index].equals("-remove")){
					removeFlag = true;
				}
				else{
					String[] range = args[index].split(":");
					try {
						int start = range.length > 0 && range[0].length() > 0 ? Integer.parseInt(range[0]) : 1;
						int end = range.length > 1 ? Integer.parseInt(range[1]) : (args[index].contains(":")) ? Model.messages.size() : start;
						start = start 	< 0 ? start + Model.messages.size() 	: start-1;
						end = 	end 	< 0 ? end 	+ Model.messages.size() + 1 : end;
						if (start > end){
							System.out.println("Starting index mustn't be grater than ending index!");
							continue;
						}
						if (removeFlag){
							for (int i=start; i<end; i++){
								 Model.messages.remove(i);
							}
						}
						else{
							for (Iterator<MessageInfo> iterator = Model.messages.listIterator(start); start<end && iterator.hasNext(); start++){
								Model.MessageInfo message = iterator.next();
								System.out.println(MosMessage.PrintXML(message.getDocument()) + "\n" + message.getString());
							}
						}
					}
					catch(NumberFormatException e){
						System.out.println("Wrong argument format: " + args[index] + ".");
					}
					catch (IndexOutOfBoundsException e){
			    		System.out.println("Invalid index: " + args[index] + ".");
			    	}
				}
			}
		}
	}
}
