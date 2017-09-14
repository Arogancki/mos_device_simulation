package mosmessages.profile0;
import mosmessages.MOSMessage;
import mossimulator.Model;

public class Heartbeat extends MOSMessage {
	public Heartbeat() {
		super(Model.getLowerPort());
	}
	@Override
	public void PrepareToSend() {
		
	}
}
