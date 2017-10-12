package mosmessages;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mosmessages.profile1.MosAck;
import mossimulator.Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import connection.ClientConnection;

public abstract class MosMessage {
	protected Document xmlDoc;
	protected String name="";
	protected int messageID;
	protected int port=Model.TARGETPORT;
	protected String host=Model.TARGETHOST;
	protected MosMessage(int _port){
		messageID = Model.takeMessageId();
		name = this.getClass().getSimpleName() + ":" + messageID;
		port = _port;
		BuildXmlDoc();
	}
	public MosMessage setPort(int _port){
		port=_port;
		return this;
	}
	public MosMessage setHost(String _port){
		host=_port;
		return this;
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
	public void Send(ArrayList<MosMessage> list){
		PrepareToSend();
		String info = "Sending - " + getClass().getSimpleName();
		System.out.println(info + ":\n" + MosMessage.PrintXML(xmlDoc));
		list.add(this);
	};
	public void Send(){
		if (host.equals("")||port<0){
			System.out.println("Error: Host or port is not set");
		}
		else{
			PrepareToSend();
			String info = "Sending - " + getClass().getSimpleName();
			System.out.println(info + ":\n" + MosMessage.PrintXML(xmlDoc));
			if (!Model.SendToHosted(host, port, this)){
				if (Model.clients.containsKey(host)){
					Hashtable<Integer, ClientConnection> ht = Model.clients.get(host);
					if (ht.containsKey(port)){
						ht.get(port).newMessage(this);
					}
					else{
						ht.put(port, new connection.ClientConnection(host, port, this));
					}
				}
				else{
					Hashtable<Integer, ClientConnection> ht = new Hashtable<Integer, ClientConnection>();
					ht.put(port, new connection.ClientConnection(host, port, this));
					Model.clients.put(host, ht);
				}
			}
		}
	};
	@Override
	public String toString(){
		 try{
		       StringWriter writer = new StringWriter();
		       TransformerFactory.newInstance().newTransformer().transform(new DOMSource(xmlDoc), new StreamResult(writer));
		       return writer.toString();
		    }
		    catch(TransformerException e) {
		       System.out.println(e.getMessage());
		       return "";
		    }
	}
	public abstract void PrepareToSend();
	public void AfterSending(){}
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		System.out.println("Recived - " + message.getMosType() + ":\n" + MosMessage.PrintXML(message.getDocument()));
		/*
		String id = message.GetFromXML("messageID");
		if (id!=null){
			new MosAck().setObjectUID(id).setStatus(mosmessages.defined.Status.ACK).setStatusDescription(id).Send(m);
		}
		else{
			System.out.println("Received corrupted message - no messageID - MOSmEssage");
		}
		*/
	}
	public Document getDocument(){return xmlDoc;}
}



/*

MESSAGE CLASS TEMPLATE

public class [messageName] extends MosMessage{
	protected [messageName]() {
		super([option]); options: Model.getUpperPort() or Model.getLowerPort() // set default port 
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);// run to print that is received
		
	}
	@Override
	public void AfterSending(){
	// behavior after sending - for example delete some items or something
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		
		Element x = xmlDoc.createElement("XML_ELEMENT_NAME");
		x.appendChild(xmlDoc.createTextNode("Value_of_element"));
		mos.appendChild(x); // save element as child of an other element
	}
}

*/