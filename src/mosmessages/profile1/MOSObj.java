package mosmessages.profile1;

import org.w3c.dom.Element;

import mosmessages.MOSMessage;
import mossimulator.Model;

public class MOSObj extends MOSMessage {
	private mossimulator.MosObj mosObj = null;
	
	public MOSObj() {
		super(Model.getLowerPort());
	}

	public MOSObj setMosObj(mossimulator.MosObj _mosObj){
		mosObj = _mosObj;
		return this;
	}
	
	// @Override
	public static void AfterReceiving(Model.MessageInfo message){
		MOSMessage.AfterReceiving(message);
		//TODO  dodac do kolekcji zawierajacej wszystkie obiekty kierowanne objID - najlepiej zeby nadpisywala kolekcja kiedy bedzie kopia sortowane po tagu
		
	}

	public void AfterSending() {
		mosObj.checkAsSemt();
		Model.MessageInfo recived = getResponse();
		if (recived == null || !recived.getMosType().toLowerCase().equals(MOSACK.class.getSimpleName().toLowerCase())){
			System.out.println("Receiving not acknowledged");
		}
	}

	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();

		Element mosAck = xmlDoc.createElement("mosAck");
		mos.appendChild(mosAck);

		Element objID = xmlDoc.createElement("objID");
		objID.appendChild(xmlDoc.createTextNode(getObjectUID()));
		mosAck.appendChild(objID);

		Element objRev = xmlDoc.createElement("objRev");
		objRev.appendChild(xmlDoc.createTextNode(getObjRev()));
		mosAck.appendChild(objID);

		Element status = xmlDoc.createElement("status");
		status.appendChild(xmlDoc.createTextNode(getStatus().toString()));
		mosAck.appendChild(status);
		
		Element statusDescription = xmlDoc.createElement("statusDescription");
		statusDescription.appendChild(xmlDoc.createTextNode(getStatusDescription()));
		mosAck.appendChild(statusDescription);
		
	}
}

// TODO zrobic nowe menu dajace mozliwosc edycji Mos obiektow:
// 1 dodwanie
// 2 usuwanie
// edytowanie
// pamietac ze po kazdym updacie trzeba wyslac message
// pracuja one na tej samej kolejce mos obiektow
// objID jest super zajebisie unikalny user ma go nie wprawadzac a musze sobie "dedukowac z odebranych od mosa"
// przed wejsciem w tryb wysylam prosbe o inicjacyjnie odebranie wszystkich
// nastepnie przed uzyciem kazdego obiektu najpier odbieram jego najnowsza wersje