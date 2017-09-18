package menu;

import java.util.Iterator;
import java.util.Scanner;

import mossimulator.Model;
import mossimulator.Model.MessageInfo;

public class PrintMessages extends Menu {
	PrintMessages() {
		super("Print messages", null);
	}
	protected void Active(){
		if (Model.messages.isEmpty())
			System.out.println("Nothing to print\n");
		else
		{
			int z = 0, amountOnOnePage = 5;
			Iterator<MessageInfo> i = Model.messages.iterator();
			while (i.hasNext()){
		    	for (int j=0; j<amountOnOnePage && i.hasNext(); j++)
		    		System.out.println(++z + ". " + i.next());
		    	while (i.hasNext()){
		    		System.out.println("(n)ext - (e)xit");
			    	char input = (new Scanner(System.in)).nextLine().trim().toLowerCase().charAt(0);
			    	if (input == 'n'){
			    		System.out.println();
			    		break;
			    		}
			    	else if (input == 'e')
			    		while (i.hasNext())
			    			i.next();
			    }
			}
		}
	}
}
