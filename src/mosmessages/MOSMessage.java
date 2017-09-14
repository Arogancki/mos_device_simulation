package mosmessages;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mossimulator.Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class MOSMessage {
	protected Document xmlDoc;
	protected String name="";
	protected int messageID;
	protected Model.Port port;
	protected MOSMessage(Model.Port _port){
		messageID = Model.takeMessageId();
		name = this.getClass().getSimpleName() + ":" + messageID;
		port = _port;
		BuildXmlDoc();
	}
	private void BuildXmlDoc(){
		try {
			xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element mos = xmlDoc.createElement("mos");
			xmlDoc.appendChild(mos);
			
			Element mosID = xmlDoc.createElement("mosID");
			mosID.appendChild(xmlDoc.createTextNode(Model.MOSID));
			mos.appendChild(mosID);
			
			Element ncsID = xmlDoc.createElement("ncsID");
			ncsID.appendChild(xmlDoc.createTextNode(Model.NCSID));
			mos.appendChild(ncsID);
			
			Element messageID = xmlDoc.createElement("messageID");
			messageID.appendChild(xmlDoc.createTextNode(Integer.toString(this.messageID)));
			mos.appendChild(messageID);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	public void Send(){
		PrepareToSend();
		port.Send(this);
	};
	@Override
	public String toString(){
		 try{
		       StringWriter writer = new StringWriter();
		       TransformerFactory.newInstance().newTransformer().transform(new DOMSource(xmlDoc), new StreamResult(writer));
		       return writer.toString();
		    }
		    catch(TransformerException e)
		    {
		       System.out.println(e.getMessage());
		       return "";
		    }
	}
	public abstract void PrepareToSend();
	public void AfterSending(){}
	public static void AfterReceiving(){}
	public Document getDocument(){return xmlDoc;}
	protected Model.MessageInfo getResponse(){
		String message = port.GetMessage();
		Model.MessageInfo result = null;
		if (message!=""){
			try {
				result = new Model.MessageInfo(Model.MessageInfo.Direction.IN, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+message);
				result.CallReceiveFunction();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Response not received");
		}
		return result;
	}
}