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
}
