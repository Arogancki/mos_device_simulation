package menu;

import java.util.Iterator;
import java.util.Scanner;

import org.w3c.dom.Document;

import mosmessages.MosMessage;
import mossimulator.Model;
import mossimulator.Model.MessageInfo;

public class PrintMessages extends Menu {
	PrintMessages() {
		super("Print messages", null);
	}
	protected void Active(){
		if (Model.messages.isEmpty())
			System.out.println("Nothing to print.");
		else
		{
			int printIndex = 0, amountOnOnePage = 5;
			Iterator<MessageInfo> i = Model.messages.iterator();
			while (i.hasNext()){
		    	for (int j=0; j<amountOnOnePage && i.hasNext(); j++)
		    		System.out.println(++printIndex + ". " + i.next());
		    	
		    	while (true){
		    		System.out.print("Enter: " + "1/" + Model.messages.size() + " - message preview, ");
			    	if (i.hasNext())
			    		System.out.print("n - next, ");
			    	System.out.print("e - exit.\n");
			    	
			    	String input = (new Scanner(System.in)).nextLine().trim().toLowerCase();
			    	try {
			    		System.out.println(MosMessage.PrintXML(Model.messages.get(Integer.parseInt(input)-1).getDocument()));
			    		System.out.println(Model.messages.get(Integer.parseInt(input)-1).getString());
			    	}catch (NumberFormatException e){}
			    	catch (IndexOutOfBoundsException e){
			    		System.out.println("Invalid index.");
			    	}
			    	char inputChar = input.charAt(0);
			    	if (inputChar == 'n' && i.hasNext()){
			    		System.out.println();
			    		break;
			    		}
			    	else if (inputChar == 'e'){
			    		while (i.hasNext())
			    			i.next();
			    		break;
			    	}
		    	}
			}
		}
	}
}
