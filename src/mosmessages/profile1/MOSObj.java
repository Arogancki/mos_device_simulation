package mosmessages.profile1;

import mosmessages.MOSMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

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
		new mossimulator.MosObj(message);
		
	}

	public void AfterSending() {
		Model.MessageInfo recived = getResponse();
		if (recived == null || !recived.getMosType().toLowerCase().equals(MOSACK.class.getSimpleName().toLowerCase())){
			System.out.println("Receiving not acknowledged");
		}
	}

	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();

		Element e_mosObj = xmlDoc.createElement("mosObj");
		mos.appendChild(e_mosObj);

		if (this.mosObj != null){
			mosObj.BuildXml(e_mosObj, xmlDoc);
		}
		
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