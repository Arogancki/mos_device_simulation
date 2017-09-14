package mosmessages.profile0;
import mosmessages.MOSMessage;
import mossimulator.Model;

public class ReqMachInfo extends MOSMessage {
	public ReqMachInfo() {
		super(Model.getLowerPort());
	}
	@Override
	public void PrepareToSend() {
		
	}
}
