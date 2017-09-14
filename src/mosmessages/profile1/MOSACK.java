package mosmessages.profile1;
import mosmessages.MOSMessage;
import mossimulator.Model;

public class MOSACK extends MOSMessage {
	public MOSACK() {
		super(Model.getLowerPort());
	}
	@Override
	public void PrepareToSend() {
		
	}
	public static void AfterReceiving(Model.MessageInfo message){
		System.out.println("Reciving confirmed");
	}
}
