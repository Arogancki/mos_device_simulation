package mosmessages.profile0;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mosmessages.MosMessage;
import mossimulator.Model;

import org.w3c.dom.Element;

public class ListMachInfo extends MosMessage {
	public ListMachInfo() {
		super(Model.getLowerPort());
	}

	// @Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
	}
	public void AfterSending() {
		
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();

		Element listMachInfo = xmlDoc.createElement("listMachInfo");
		mos.appendChild(listMachInfo);

		Element manufacturer = xmlDoc.createElement("manufacturer");
		manufacturer.appendChild(xmlDoc.createTextNode("Avid Technology"));
		listMachInfo.appendChild(manufacturer);

		Element model = xmlDoc.createElement("model");
		model.appendChild(xmlDoc.createTextNode("1.0"));
		listMachInfo.appendChild(model);

		Element hwRev = xmlDoc.createElement("hwRev");
		hwRev.appendChild(xmlDoc.createTextNode("1.0"));
		listMachInfo.appendChild(hwRev);

		Element swRev = xmlDoc.createElement("swRev");
		swRev.appendChild(xmlDoc.createTextNode("1.0"));
		listMachInfo.appendChild(swRev);

		Element DOM = xmlDoc.createElement("DOM");
		DOM.appendChild(xmlDoc.createTextNode("2017.09"));
		listMachInfo.appendChild(DOM);

		Element SN = xmlDoc.createElement("SN");
		SN.appendChild(xmlDoc.createTextNode("0"));
		listMachInfo.appendChild(SN);

		Element ID = xmlDoc.createElement("ID");
		ID.appendChild(xmlDoc.createTextNode(Model.NCSID));
		listMachInfo.appendChild(ID);

		Date now = new Date();
		Element time = xmlDoc.createElement("time");
		time.appendChild(xmlDoc.createTextNode((new SimpleDateFormat("EEE",
				Locale.US)).format(now)
				+ " "
				+ (new DateFormatSymbols()).getMonths()[(Calendar.getInstance())
						.get(Calendar.MONTH)].substring(0, 3)
				+ " "
				+ (new SimpleDateFormat("dd hh:mm:ss")).format(now)
				+ " "
				+ (new SimpleDateFormat("YYYY")).format(now)));
		listMachInfo.appendChild(time);

		long nowMilli = System.currentTimeMillis() - Model.STARTDATE;
		long miliseconds = nowMilli % 1000;
		long seconds = (nowMilli / 1000) % 100;
		long minutes = (nowMilli / 60000) % 60;
		long hours = (nowMilli / 3600000) % 24;
		long days = nowMilli / 86400000;

		Element opTime = xmlDoc.createElement("opTime");
		opTime.appendChild(xmlDoc.createTextNode((days == 0 ? "" : days + "d ")
				+ (hours == 0 ? "" : hours + "h ")
				+ (minutes == 0 ? "" : minutes + "m ") + seconds + "."
				+ miliseconds + "s"));
		listMachInfo.appendChild(opTime);

		Element mosRev = xmlDoc.createElement("mosRev");
		mosRev.appendChild(xmlDoc.createTextNode("2.8"));
		listMachInfo.appendChild(mosRev);
	}
}
