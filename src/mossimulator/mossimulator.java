package mossimulator;

import tests.RoStoryItemPadding;
import mosmessages.profile1.MosAck;

public class MosSimulator{
	public static void main(String[] args) {
		try{
			Model.init();
			//String[] tab = {"heartbeat"};
			//String[] tab = {"s", "host", "10.105.250.217", "mosid", "tester Artur"};
			//console.Console.start(tab);
			console.Console.start(args);
		}
		catch(Exception e){
			e.getMessage();
		}
		finally{
			Model.Exit();
		}
		RoStoryItemPadding.ShowRo();
	}
}