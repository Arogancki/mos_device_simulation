package menu;

import mossimulator.Model;
import mossimulator.Model.MessageInfo;

import java.util.Iterator;
import java.util.Scanner;

public class PrintMessages extends Menu {
	PrintMessages() {
		super("Print messages", null);
	}
	protected void Active(){
		if (Model.messages.isEmpty())
			System.out.println("Nothing to print\n");
		else
		{
			Iterator<MessageInfo> i = Model.messages.iterator();
			outer:
		    while (i.hasNext()) {
		    	System.out.println(i.next());
		    	while (true){
		    		System.out.println("(n)ext - (e)xit");
			    	char input = (new Scanner(System.in)).nextLine().trim().toLowerCase().charAt(0);
			    	if (input == 'n')
			    		break;
			    	else if (input == 'e')
			    		break outer;
		    	}
		    		
		    }
		}
	}
}
