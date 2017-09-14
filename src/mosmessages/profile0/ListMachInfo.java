package mosmessages.profile0;
import mosmessages.MOSMessage;
import mossimulator.Model;

public class ListMachInfo extends MOSMessage {
	public ListMachInfo() {
		super(Model.getLowerPort());
	}
	@Override
	public void PrepareToSend() {
	
	}
}
