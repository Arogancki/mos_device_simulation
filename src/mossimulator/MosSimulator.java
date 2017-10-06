package mossimulator;

import mosmessages.profile1.MosAck;

public class MosSimulator{
	public static void main(String[] args) {
		try{
			//String[] tab = {"heartbeat"};
			//console.Console.start(tab);
			console.Console.start(args);
			}
		finally{
			Model.Exit();
		}
	}
}