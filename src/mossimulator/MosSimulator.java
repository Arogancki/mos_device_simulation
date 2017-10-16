package mossimulator;

import mosmessages.profile1.MosAck;

public class MosSimulator{
	public static void main(String[] args) {
		try{
			//String[] tab = {"heartbeat"};
			//String[] tab = {"s", "host", "10.105.250.217", "mosid", "tester Artur"};
			//console.Console.start(tab);
			//console.Console.start(args);
			
			
			boolean s= RunningOrder.Contains("Jestem tu nowy");
			RunningOrder ro = RunningOrder.getRunningOrderObj("Jestem tu nowy");
			
			int i=0;
			i=0+1;
		}
		catch(Exception e){
			e.getMessage();
		}
		finally{
			Model.Exit();
		}
	}
}