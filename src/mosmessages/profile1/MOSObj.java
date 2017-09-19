package mosmessages.profile1;

import mosmessages.MOSMessage;
import mossimulator.Model;

public class MOSObj extends MOSMessage {
	public MOSObj() {
		super(Model.getLowerPort());
	}

	// @Override
	public static void AfterReceiving(Model.MessageInfo message){
		MOSMessage.AfterReceiving(message);
		//TODO  dodac do kolekcji zawierajacej wszystkie obiekty kierowanne objID - najlepiej zeby nadpisywala kolekcja kiedy bedzie kopia sortowane po tagu
	}

	public void AfterSending() {
		Model.MessageInfo recived = getResponse();
		if (recived == null || !recived.getMosType().toLowerCase().equals(MOSACK.class.getSimpleName().toLowerCase())){
			System.out.println("Receiving not acknowledged");
		}
	}

	@Override
	public void PrepareToSend() {
		// wyciaganc z kolekcji i wyslac 
	}
}