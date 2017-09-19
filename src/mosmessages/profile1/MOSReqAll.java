package mosmessages.profile1;

import org.w3c.dom.Element;

import mosmessages.MOSMessage;
import mosmessages.profile0.Heartbeat;
import mossimulator.Model;
import mossimulator.Model.MessageInfo;

public class MOSReqAll extends MOSMessage {
	private long pause = 1L; // delay between messages responses containing obj, if ==0 get mostListAll
	public MOSReqAll() {
		super(Model.getLowerPort());
	}

	// @Override
	public static void AfterReceiving(Model.MessageInfo message){
		MOSMessage.AfterReceiving(message);
		new MOSACK().setStatus(MOSACK.Status.OK).Send();
		int pause = 0;
		try {
			pause = Integer.parseInt(message.GetFromXML("pause"));
		}
		catch(NumberFormatException|NullPointerException e){
		}
		finally{
			if (pause > 0){
				// TODO wysylanie pojedynczo kadego objektu co okreslony w pasue czas
			}
			else if (pause == 0){
				new MOSListAll().Send();
			}
			else
				System.out.println("Wrong pasue value: " + pause);
		}
	}

	public void AfterSending() {
		MessageInfo response = getResponse();
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
		}
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

	public MOSReqAll setPause(long pause) {
		//max 0xFFFF FFFF
		if (pause > 4294967295L)
			pause = 4294967295L;
		this.pause = pause;
		return this;
	}	
	
	public MOSReqAll resetPause(){
		this.pause = 0;
		return this;
	}
}