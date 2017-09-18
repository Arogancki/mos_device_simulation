package mosmessages.profile1;

import mosmessages.MOSMessage;
import mossimulator.Model;

public class MOSListAll extends MOSMessage {
	
	public MOSListAll() {
		super(Model.getLowerPort());
	}

	// @Override
	public static void AfterReceiving(Model.MessageInfo message){
		MOSMessage.AfterReceiving(message);
	}

	public void AfterSending() {
		
	}

	@Override
	public void PrepareToSend() {
		
	}
}