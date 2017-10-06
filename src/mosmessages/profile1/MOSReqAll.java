package mosmessages.profile1;

import java.util.ArrayList;

import mosmessages.MosMessage;
import mosmessages.defined.Status;
import mossimulator.Model;
import mossimulator.Model.MessageInfo;

import org.w3c.dom.Element;

public class MosReqAll extends MosMessage {
	private long pause = 0L; // delay between messages responses containing obj, if ==0 get mostListAll
	public MosReqAll() {
		super(Model.getLowerPort());
	}

	// @Override
	public static void AfterReceiving(Model.MessageInfo message,ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message,m);
		new MosAck().setStatus(mosmessages.defined.Status.OK).Send(m);
		int pause = 0;
		try {
			pause = Integer.parseInt(message.GetFromXML("pause"));
		}
		catch(NumberFormatException|NullPointerException e){
		}
		finally{
			if (pause > 0){
				MosAck ack = new MosAck();
				ack.setStatus(Status.OK);
				ack.Send(m);
				for (String key : mossimulator.MosObj.GetKeys()) {
					new MosObj().setMosObj(mossimulator.MosObj.getMosObj(key)).Send(m);
				}
			}
			else if (pause == 0){
				new MosListAll().Send(m);
			}
			else
				System.out.println("Wrong pasue value: " + pause);
		}
	}

	public void AfterSending() {
		/*MessageInfo response = getResponse();
		if (response.getMosType().equals("mosack")){
			if (pause <= 0){
				getResponse();
			}
			else{
				Model.SECTOWAIT += pause;
				while (getResponse() != null){
				}
				Model.SECTOWAIT -= pause;
			}
		}*/
	}

	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();

		Element mosReqAll = xmlDoc.createElement("mosReqAll");
		mos.appendChild(mosReqAll);

		Element pause = xmlDoc.createElement("pause");
		pause.appendChild(xmlDoc.createTextNode(Long.toString(getPause())));
		mosReqAll.appendChild(pause);
	}

	public long getPause() {
		return pause;
	}

	public MosReqAll setPause(long pause) {
		//max 0xFFFF FFFF
		if (pause > 4294967295L)
			pause = 4294967295L;
		this.pause = pause;
		return this;
	}	
	
	public MosReqAll resetPause(){
		this.pause = 0;
		return this;
	}
}