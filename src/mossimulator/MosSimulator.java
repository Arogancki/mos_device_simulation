package mossimulator;

import mosmessages.profile1.MosAck;

public class MosSimulator{
	public static void main(String[] args) {
		//String[] tab = {"s", "host", "w2012cmd2"};
		//console.Console.start(tab);
		new MosAck(); // to iniciate all static field etc
		console.Console.start(args);
		Model.Exit();
	}
}