package mossimulator;

import tests.RoStoryItemPadding;
import mosmessages.profile1.MosAck;

public class MosSimulator{
	public static void main(String[] args) {
		try{
			Model.init();
			//String[] tab = {"s", "ms", "-1"};
			//console.Console.start(tab);
			console.Console.start(args);
		}
		catch(Exception e){
			e.getMessage();
		}
		finally{
			Model.Exit();
		}
	}
}