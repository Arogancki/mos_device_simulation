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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class MosMessage {
	private static boolean isListening = false;
	protected Document xmlDoc;
	protected String name="";
	protected int messageID;
	protected Model.Port port;
	protected MosMessage(Model.Port _port){
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
	public static String PrintXML(Document xmlDoc){
		class PrintNode{
			private String result;
			private String getLineEntry(int i){
				if (i <= 0) return "";
				return "\t"+getLineEntry(i-1);
			}
 			public PrintNode(Element element){
				result = get(element, 0);
			}
			private String get(Element element, int lineEntry){
				StringBuffer result = new StringBuffer(getLineEntry(lineEntry)).append(element.getNodeName());
				NodeList nodeList = element.getChildNodes();
				if (nodeList.getLength() > 0)
				{
					if (nodeList.getLength() == 1 && nodeList.item(0).getNodeType() != Node.ELEMENT_NODE){
						result.append(" : \"" + element.getTextContent() + "\"");
					}
					else {
						result.append(" {");
						for (int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							if (node.getNodeType() == Node.ELEMENT_NODE)
								result.append("\n").append(get((Element) node, lineEntry+1));
						}
						result.append("\n").append(getLineEntry(lineEntry)).append("}");
					}
				}
				return result.toString();
			}
		    public String toString(){
				return result;
		    }
		 }
		 return new PrintNode(xmlDoc.getDocumentElement()).toString();
	}
	public static void setIsListening(boolean v){
		isListening=v;
	}
	public void Send(){
		PrepareToSend();
		String info = "Sending - " + getClass().getSimpleName();
		System.out.println(info + ":\n" + MosMessage.PrintXML(xmlDoc));
		if (isListening && !port.SendOnOpenSocket(this)){
			System.out.println("Coudln't send the message.");
		}
		else if (!port.Send(this)){
			System.out.println("Coudln't send the message.");
		}
	};
	public boolean SendWithouClosing(){
		PrepareToSend();
		String info = "Sending - " + getClass().getSimpleName();
		System.out.println(info + ":\n" + MosMessage.PrintXML(xmlDoc));
		boolean result = port.SendWithoutClosing(this);
		if (!result){
			System.out.println("Coudln't send the message.");
		}
		return result;
	}
	public void CloseSocket(){
		port.CloseSocket();
	}
	public boolean SendOnOpenSocket(){
		PrepareToSend();
		String info = "Sending - " + getClass().getSimpleName();
		System.out.println(info + ":\n" + MosMessage.PrintXML(xmlDoc));
		boolean result = port.SendOnOpenSocket(this);
		if (!result){
			System.out.println("Coudln't send the message.");
		}
		return result;
	}
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
	public static void AfterReceiving(Model.MessageInfo message){
		System.out.println("Recived - " + message.getMosType() + ":\n" + MosMessage.PrintXML(message.getDocument()));
	}
	public Document getDocument(){return xmlDoc;}
	protected Model.MessageInfo getResponse(){
		String message = port.GetMessage();
		Model.MessageInfo result = null;
		if (message!=""){
			try {
				result = new Model.MessageInfo(Model.MessageInfo.Direction.IN, message);
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