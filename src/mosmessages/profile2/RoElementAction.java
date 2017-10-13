package mosmessages.profile2;
import java.util.ArrayList;
import java.util.Collections;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mosmessages.MosMessage;
import mossimulator.Item;
import mossimulator.Model;
import mossimulator.Story;

public class RoElementAction extends MosMessage{
	private mosmessages.defined.RoElementAction roElementAction = null;
	private String roID = "";
	private String TargetStoryID = "";
	private String TargetItemID = "";
	private ArrayList<String> storyIDs = new ArrayList<String>();
	private ArrayList<String> itemIDs = new ArrayList<String>();
	public RoElementAction() {
		super(Model.getUpperPort());
	}
	//@Override
	public static void AfterReceiving(Model.MessageInfo message, ArrayList<MosMessage> m){
		MosMessage.AfterReceiving(message, m);
		Node root = message.getDocument().getChildNodes().item(0);
		Node roElementActionNode = mossimulator.Model.MessageInfo.GetFromElement(root, "roElementAction");
		String roID = mossimulator.Model.MessageInfo.GetNodeContext(roElementActionNode, "roID");
		if (roElementActionNode!=null && roID!=null){
			String operation = ((Element)roElementActionNode).getAttribute("operation");
			mosmessages.defined.RoElementAction roElementAction = mosmessages.defined.RoElementAction.getFromString(operation);
			if (roElementAction!=null){
				if (roElementAction==mosmessages.defined.RoElementAction.INSERT){
					Node element_targetNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_target");
					Node element_sourceNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_source");
					if (element_targetNode!=null && element_sourceNode!=null){
						Node storyIDNode = mossimulator.Model.MessageInfo.GetFromElement(element_targetNode, "storyID");
						if (storyIDNode!=null){
							String storyID = mossimulator.Model.MessageInfo.GetNodeContext(storyIDNode, "storyID");
							Node itemIDNode = mossimulator.Model.MessageInfo.GetFromElement(element_targetNode, "itemID");
							if (itemIDNode!=null){
								String itemID = mossimulator.Model.MessageInfo.GetNodeContext(itemIDNode, "itemID");
								NodeList nodes = element_sourceNode.getChildNodes();
								if(mossimulator.RunningOrder.Contains(roID)){
									mossimulator.RunningOrder ro = mossimulator.RunningOrder.getRunningOrderObj(roID);
									ArrayList<Story> stories = ro.getStoryArray();
									if (stories.contains(storyID)){
										for (Story story : stories){
											if (story.getStoryID().equals(storyID)){
												ArrayList<mossimulator.Item> items = story.getItemsArray();
												int start=0;
												if (story.containsItem(itemID)){
													for (mossimulator.Item item : story.getItemsArray()){
														if (item.getItemID().equals(itemID)){
															start=stories.indexOf(item);
															break;
														}
													}
												}
												for (int i=nodes.getLength()-1; i>=0; i--){
													items.add(start,new mossimulator.Item(nodes.item(i)));
												}
												new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
												break;
											}
										}
									}
								}
							}
							else{
								NodeList nodes = element_sourceNode.getChildNodes();
								if(mossimulator.RunningOrder.Contains(roID)){
									mossimulator.RunningOrder ro = mossimulator.RunningOrder.getRunningOrderObj(roID);
									ArrayList<Story> stories = ro.getStoryArray();
									int start=0;
									for (Story story : stories){
										if (story.getStoryID().equals(storyID)){
											start=stories.indexOf(story);
											break;
										}
									}
									for (int i=nodes.getLength()-1; i>=0; i--){
										stories.add(start,new Story(nodes.item(i)));
									}
									new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
								}
							}
						}
						else{
							System.out.println("Recived message without storyID - reciving roElementAction/INSERT");
							new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
						}
					}
					else{
						System.out.println("Recived message without element_target or element_source - reciving roElementAction/INSERT");
						new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
					}
				}
				else if (roElementAction==mosmessages.defined.RoElementAction.REPLACE){
					Node element_targetNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_target");
					Node element_sourceNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_source");
					if (element_targetNode!=null && element_sourceNode!=null){
						Node storyIDNode = mossimulator.Model.MessageInfo.GetFromElement(element_targetNode, "storyID");
						if (storyIDNode!=null){
							String storyID = mossimulator.Model.MessageInfo.GetNodeContext(storyIDNode, "storyID");
							Node itemIDNode = mossimulator.Model.MessageInfo.GetFromElement(element_targetNode, "itemID");
							if (itemIDNode!=null){
								String itemID = mossimulator.Model.MessageInfo.GetNodeContext(itemIDNode, "itemID");
								NodeList nodes = element_sourceNode.getChildNodes();
								if(mossimulator.RunningOrder.Contains(roID)){
									mossimulator.RunningOrder ro = mossimulator.RunningOrder.getRunningOrderObj(roID);
									ArrayList<Story> stories = ro.getStoryArray();
									if (stories.contains(storyID)){
										for (Story story : stories){
											if (story.getStoryID().equals(storyID)){
												ArrayList<mossimulator.Item> items = story.getItemsArray();
												int start=0;
												if (story.containsItem(itemID)){
													for (mossimulator.Item item : story.getItemsArray()){
														if (item.getItemID().equals(itemID)){
															start=stories.indexOf(item);
															break;
														}
													}
												}
												for (int i=nodes.getLength()-1; i>=0; i--){
													items.add(start,new mossimulator.Item(nodes.item(i)));
												}
												items.remove(start+nodes.getLength());
												new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
												break;
											}
										}
									}
								}
							}
							else{
								NodeList nodes = element_sourceNode.getChildNodes();
								if(mossimulator.RunningOrder.Contains(roID)){
									mossimulator.RunningOrder ro = mossimulator.RunningOrder.getRunningOrderObj(roID);
									ArrayList<Story> stories = ro.getStoryArray();
									int start=0;
									for (Story story : stories){
										if (story.getStoryID().equals(storyID)){
											start=stories.indexOf(story);
											break;
										}
									}
									for (int i=nodes.getLength()-1; i>=0; i--){
										stories.add(start,new Story(nodes.item(i)));
									}
									stories.remove(start+nodes.getLength());
									new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
								}
							}
						}
						else{
							System.out.println("Recived message without storyID - reciving roElementAction/REPLACE");
							new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
						}
					}
					else{
						System.out.println("Recived message without element_target or element_source - reciving roElementAction/REPLACE");
						new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
					}
				}
				else if (roElementAction==mosmessages.defined.RoElementAction.MOVE){
					Node element_sourceNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_source");
					Node element_targetNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_target");
					if (element_targetNode!=null && element_sourceNode!=null){
						String storyID = mossimulator.Model.MessageInfo.GetNodeContext(element_targetNode, "storyID");
						if (storyID!=null && !storyID.equals("")){
							String itemID = mossimulator.Model.MessageInfo.GetNodeContext(element_targetNode, "itemID");
							if (itemID!=null  && !itemID.equals("")){
								//itemy
								mossimulator.RunningOrder ro =mossimulator.RunningOrder.getRunningOrderObj(roID);
								ArrayList<Story> al = ro.getStoryArray();
								for (Story story : al){
									if (story.getStoryID().equals(storyID)){
										ArrayList<Item> alI = story.getItemsArray();
										NodeList nodelist = element_sourceNode.getChildNodes();
										int where = -1;
										for (int i=0; i<alI.size();i++){
											if (alI.get(i).getItemID().equals(itemID)){
												where=i;
												break;
											}
										}
										for (int i=0; i<nodelist.getLength(); i++){
											String id = mossimulator.Model.MessageInfo.GetNodeContext(nodelist.item(i));
											if (id!=null){
												for (int ii=0; ii<alI.size(); ii++){
													Item itemToMove = alI.get(ii);
													if (itemToMove.getItemID().equals(id)){
														alI.remove(ii);
														alI.add(where, itemToMove);
														break;
													}
												}
											}
										}
										new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
										break;
									}
								}
							}else{
								//storysy
								mossimulator.RunningOrder ro =mossimulator.RunningOrder.getRunningOrderObj(roID);
								ArrayList<Story> al = ro.getStoryArray();
								int where = -1;
								for (int i=0; i<al.size();i++){
									if (al.get(i).getStoryID().equals(storyID)){
										where=i;
										break;
									}
								}
								if (where!=-1){
									NodeList nodelist = element_sourceNode.getChildNodes();
									for (int i=nodelist.getLength()-1; i>=0; i--){
										String id = mossimulator.Model.MessageInfo.GetNodeContext(nodelist.item(i));
										if (id!=null){
											for (int ii=0; ii<al.size();ii++){
												Story storyToMove = al.get(ii);
												if (storyToMove.getStoryID().equals(id)){
													al.remove(ii);
													al.add(where, storyToMove);
													break;
												}
											}
										}
									}
									new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
								}
								else{
									System.out.println("Target didn't find - reciving roElementAction/MOVE");
									new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
								}
							}
						}else{
							System.out.println("Recived message without storyID - reciving roElementAction/MOVE");
							new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
						}
					}
					else{
						System.out.println("Recived message without element_target or element_source - reciving roElementAction/MOVE");
						new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
					}
				}
				else if (roElementAction==mosmessages.defined.RoElementAction.DELETE){
					Node element_sourceNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_source");
					if (element_sourceNode!=null){
						Node element_targetNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_target");
						if (element_targetNode!=null){
							Node storyIDNode = mossimulator.Model.MessageInfo.GetFromElement(element_targetNode, "storyID");
							if (storyIDNode!=null){
								String storyID = mossimulator.Model.MessageInfo.GetNodeContext(storyIDNode, "storyID");
								if (storyID!=null && !storyID.equals("")){
									mossimulator.RunningOrder ro =mossimulator.RunningOrder.getRunningOrderObj(roID);
									ArrayList<Story> al = ro.getStoryArray();
									for (Story story : al){
										if (story.getStoryID().equals(storyID)){
											ArrayList<Item> alI = story.getItemsArray();
											NodeList nodelist = element_sourceNode.getChildNodes();
											for (int i=0; i<nodelist.getLength(); i++){
												String id = mossimulator.Model.MessageInfo.GetNodeContext(nodelist.item(i));
												if (id!=null){
													alI.removeIf(x->x.getItemID().equals(id));
												}
											}
											new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
											break;
										}
									}
								}
								else{
									System.out.println("Recived message withou storyID - reciving roElementAction/DELETE");
									new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
								}
							}
							else{
								mossimulator.RunningOrder ro =mossimulator.RunningOrder.getRunningOrderObj(roID);
								ArrayList<Story> al = ro.getStoryArray();
								NodeList nodelist = element_sourceNode.getChildNodes();
								for (int i=0; i<nodelist.getLength(); i++){
									String id = mossimulator.Model.MessageInfo.GetNodeContext(nodelist.item(i));
									if (id!=null){
										al.removeIf(x->x.getStoryID().equals(id));
									}
								}
								new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
							}
						}
						else{
							System.out.println("Recived message withou element_source - reciving roElementAction/DELETE");
							new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
						}
					}
					else{
						System.out.println("Recived message withou element_source - reciving roElementAction/DELETE");
						new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
					}
				}
				else if (roElementAction==mosmessages.defined.RoElementAction.SWAP){
					Node element_sourceNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_source");
					Node element_targetNode = mossimulator.Model.MessageInfo.GetFromElement(root,"element_target");
					if (element_targetNode!=null && element_sourceNode!=null){
						String storyID = mossimulator.Model.MessageInfo.GetNodeContext(element_targetNode, "storyID");
						if (storyID!=null && !storyID.equals("")){
							// itemy
							mossimulator.RunningOrder ro =mossimulator.RunningOrder.getRunningOrderObj(roID);
							NodeList nl = element_sourceNode.getChildNodes();
							if (nl.getLength()<2){
								String itemID1 = mossimulator.Model.MessageInfo.GetNodeContext(nl.item(0), "itemID");
								String itemID2 = mossimulator.Model.MessageInfo.GetNodeContext(nl.item(1), "itemID");
								if (itemID1!=null && !itemID1.equals("") && itemID2!=null && !itemID2.equals("")){
									ArrayList<Story> al = ro.getStoryArray();
									for (Story story : al){
										if (story.getStoryID().equals(storyID)){
											ArrayList<Item> alI = story.getItemsArray();
											int index1 =-1;
											int index2 =-1;
											for (int i=0; i<alI.size(); i++){
												Item item = alI.get(i);
												if (item.getItemID().equals(itemID1)){
													index1=i;
												}else if(story.getStoryID().equals(itemID2)){
													index2=i;
												}
												if (index1!=-1 && index2!=-1){
													Collections.swap(alI, index1, index2);
													new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
												}
											}
											break;
										}  
									}
								}
								else{
									System.out.println("Recived message with to less itemIDs  - reciving roElementAction/Swap");
									new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
								}
							}
							else{
								System.out.println("Recived message with to less itemIDs  - reciving roElementAction/Swap");
								new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
							}
						}
						else{
							//storysy
							mossimulator.RunningOrder ro =mossimulator.RunningOrder.getRunningOrderObj(roID);
							NodeList nl = element_sourceNode.getChildNodes();
							if (nl.getLength()<2){
								String storyID1 = mossimulator.Model.MessageInfo.GetNodeContext(nl.item(0), "storyID");
								String storyID2 = mossimulator.Model.MessageInfo.GetNodeContext(nl.item(1), "storyID");
								if (storyID1!=null && !storyID1.equals("") && storyID2!=null && !storyID2.equals("")){
									ArrayList<Story> al = ro.getStoryArray();
									int index1=-1;
									int index2=-1;
									for (int i=0; i<al.size(); i++){
										Story story = al.get(i);
										if (story.getStoryID().equals(storyID1)){
											index1=i;
										}else if(story.getStoryID().equals(index2)){
											index2=i;
										}
										if (index1!=-1 && index2!=-1){
											Collections.swap(al, index1, index2);
											new RoAck().addRoAckInner(mosmessages.defined.Status.OK).setRoID(roID).Send(m);
										}
									}
								}
								else{
									System.out.println("Recived message with to less storyIDs  - reciving roElementAction/Swap");
									new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
								}
							}
							else{
								System.out.println("Recived message with to less storyIDs  - reciving roElementAction/Swap");
								new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
							}
						}
					}
					else{
						System.out.println("Recived message without element_target or element_source - reciving roElementAction/Swap");
						new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
					}
				}
			}
			else
			{
				System.out.println("Recived not supported roElementAction - "+roElementAction+" - reciving roElementAction");
				new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).setRoID(roID).Send(m);
			}
		}
		else{
			System.out.println("Recived message without roElementAction or roID - reciving roElementAction");
			new RoAck().addRoAckInner(mosmessages.defined.Status.NACK).Send(m);
		}
	}
	@Override
	public void AfterSending(){
	}
	@Override
	public void PrepareToSend() {
		Element mos = xmlDoc.getDocumentElement();
		
		Element roElementAction = xmlDoc.createElement("roElementAction");
		if (this.roElementAction!=null){
			roElementAction.setAttribute("operation", this.roElementAction.toString());
		}
		else{
			System.out.println("Warrning: operation not set for roElementAction.");
		}
		mos.appendChild(roElementAction);
		
		Element roID = xmlDoc.createElement("roID");
		roID.appendChild(xmlDoc.createTextNode(this.roID));
		roElementAction.appendChild(roID);
		
		if (!this.TargetStoryID.equals("")){
			Element element_target = xmlDoc.createElement("element_target");
			
			Element TargetStoryID = xmlDoc.createElement("storyID");
			TargetStoryID.appendChild(xmlDoc.createTextNode(this.TargetStoryID));
			element_target.appendChild(TargetStoryID);
			
			if (!this.TargetItemID.equals("")){
				Element TargetItemID = xmlDoc.createElement("itemID");
				TargetItemID.appendChild(xmlDoc.createTextNode(this.TargetItemID));
				element_target.appendChild(TargetItemID);
			}
			
			roElementAction.appendChild(element_target);
		}
		
		Element element_source = xmlDoc.createElement("element_source");
		if (this.roElementAction==mosmessages.defined.RoElementAction.REPLACE || this.roElementAction==mosmessages.defined.RoElementAction.INSERT){
			for (String storyID : storyIDs){
				mossimulator.Story story = mossimulator.Story.getItemObj(storyID);
				if (story!=null){
					Element storyElement = xmlDoc.createElement("story");
					story.BuildXml(storyElement, xmlDoc);
					element_source.appendChild(storyElement);
				}
			}
			for (String itemID : itemIDs){
				mossimulator.Item item = mossimulator.Item.getItemObj(itemID);
				if (item!=null){
					Element itemElement = xmlDoc.createElement("item");
					item.BuildXml(itemElement, xmlDoc);
					element_source.appendChild(itemElement);
				}
			}
		}
		else{
			for (String storyID : storyIDs){
				Element storyIDElement = xmlDoc.createElement("storyID");
				storyIDElement.appendChild(xmlDoc.createTextNode(storyID));
				element_source.appendChild(storyIDElement);
			}
			for (String itemID : itemIDs){
				Element itemIDElement = xmlDoc.createElement("itemID");
				itemIDElement.appendChild(xmlDoc.createTextNode(itemID));
				element_source.appendChild(itemIDElement);
			}
		}
		roElementAction.appendChild(element_source);
	}
	public RoElementAction setRoElementAction(mosmessages.defined.RoElementAction roElementAction) {
		this.roElementAction = roElementAction;
		return this;
	}
	public RoElementAction setRoElementAction(String roElementAction) {
		mosmessages.defined.RoElementAction parsed = mosmessages.defined.RoElementAction.getFromString(roElementAction);
		if (parsed!=null){
			setRoElementAction(parsed);
		}
		return this;
	}
	public RoElementAction setRoID(String roID) {
		this.roID = roID;
		return this;
	}
	public RoElementAction setTargetStoryID(String storyID) {
		this.TargetStoryID = storyID;
		return this;
	}
	public RoElementAction setTargetItemID(String itemID) {
		this.TargetItemID = itemID;
		return this;
	}
	public RoElementAction addItem(String roID){
			if (mossimulator.Item.Contains(roID)){
				this.itemIDs.add(roID);
			}
			else{
				System.out.println("Not known roID for Item on roElementAction-add Item.");
			}
			return this;
		}
	public RoElementAction addStory(String roID){
		if (mossimulator.Story.Contains(roID)){
			this.storyIDs.add(roID);
		}
		else{
			System.out.println("Not known roID for Story on roElementAction-add Story.");
		}
		return this;
	}
}