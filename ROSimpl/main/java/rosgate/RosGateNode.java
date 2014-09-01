package rosgate;
import java.util.ArrayList;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.ros.internal.message.Message;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;

import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import exceptions.AlreadyConfiguredNodeException;
import exceptions.NotYetConfiguredException;
import exceptions.TopicAlreadyExistingException;
import exceptions.TopicNotExistingException;

public class RosGateNode<T> extends AbstractNodeMain implements RosGateIntf {

	//Publisher objects
	private ArrayList<Publisher<T>> publishersList = new ArrayList<Publisher<T>>();
	private ArrayList<String> publishersTopicNamesList = new ArrayList<String>();
	private ArrayList<String> publishersTopicTypesList = new ArrayList<String>();

	//Subscriber objects
	private ArrayList<Subscriber<T>> subscribersList = new ArrayList<Subscriber<T>>();   
	private ArrayList<TopicMessageListener> messageListenerList = new ArrayList<TopicMessageListener>();
	private ArrayList<String> listenersTopicTypesList = new ArrayList<String>();

	private boolean confFinished = false;


	@SuppressWarnings("unchecked")
	@Override
	public void onStart(ConnectedNode connectedNode) {
		//Create a log object
		@SuppressWarnings("unused")
		final Log log = connectedNode.getLog();

		//PUBLISHER
		//Create the publisher
		for(int i = 0; i < this.publishersTopicNamesList.size(); i++){
			Publisher<T> pub = connectedNode.newPublisher(this.publishersTopicNamesList.get(i), this.publishersTopicTypesList.get(i));
			this.publishersList.add(pub);
		}

		//SUBSCRIBER
		//Create subscriber to topics and add
		for(int i = 0; i < this.messageListenerList.size(); i++){
			String topicName = this.messageListenerList.get(i).getTopicName();
			Subscriber<T> s = connectedNode.newSubscriber(topicName, this.listenersTopicTypesList.get(i));
			this.subscribersList.add(s);
			TopicMessageListener topicMsgListener = this.messageListenerList.get(i);
			this.subscribersList.get(i).addMessageListener((MessageListener<T>) topicMsgListener);
		}

		this.setConfigurationFinished();
	}

	@Override
	public void addObserverElement(String observedTopicName, Observer rosGateObs) throws TopicNotExistingException, AlreadyConfiguredNodeException {
		boolean foundedTopic = false;
		if(!isConfigured()){
			for(int i = 0; i < messageListenerList.size(); i++){
				if(observedTopicName.compareTo(this.messageListenerList.get(i).getTopicName()) == 0){
					this.messageListenerList.get(i).addObserver(rosGateObs);
					foundedTopic = true;
				}
			}

			if (!foundedTopic){				
				throw new TopicNotExistingException(observedTopicName);
			}
		}else{
			throw new AlreadyConfiguredNodeException();
		}
	}

	@Override
	public void createListener(String listenedTopicName, String listenedType) throws TopicAlreadyExistingException, AlreadyConfiguredNodeException {
		if(!isConfigured()){
			boolean toAdd = true;
			for(int i = 0; i < this.messageListenerList.size(); i++){
				if(listenedTopicName.compareTo(this.messageListenerList.get(i).getTopicName()) == 0){
					toAdd = false;
				}
			}

			if(toAdd){
				this.messageListenerList.add((new TopicMessageListener(listenedTopicName)));
				this.listenersTopicTypesList.add(listenedType);
			}else{
				throw new TopicAlreadyExistingException(listenedTopicName);
			}
		}else{
			throw new AlreadyConfiguredNodeException();
		}
	}


	@Override
	public void createPublisher(String publishedTopicName, String publishedType) throws TopicAlreadyExistingException, AlreadyConfiguredNodeException {
		if(!isConfigured()){
			boolean toAdd = true;

			for(int i = 0; i < this.publishersTopicNamesList.size(); i++){
				if(publishedTopicName.compareTo(this.publishersTopicNamesList.get(i)) == 0){
					toAdd = false;
					break;
				}
			}

			if(toAdd){
				this.publishersTopicNamesList.add(publishedTopicName);
				this.publishersTopicTypesList.add(publishedType);
			}else{
				throw new TopicAlreadyExistingException(publishedTopicName);
			}
		}else{
			throw new AlreadyConfiguredNodeException();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void publish(Message msg, String targeTopicName) throws TopicNotExistingException, NotYetConfiguredException {
		if(isConfigured()){
			boolean foundedTopic = false;

			for(int i = 0; i < publishersList.size(); i++){
				if(("/" + targeTopicName).compareTo(this.publishersList.get(i).getTopicName().toString()) == 0){
					foundedTopic = true;
					this.publishersList.get(i).publish((T) msg);
				}
			}

			if(!foundedTopic){
				throw new TopicNotExistingException(targeTopicName);
			}
		}else{
			throw new NotYetConfiguredException();
		}
	}


	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("RosGate");
	}

	@Override
	public String showYourself() {
		String returnedString = "";

		returnedString += "Listeners (" + this.messageListenerList.size() +"): \n";

		for(int i = 0; i < this.messageListenerList.size(); i++){
			returnedString += " " + i + ":\n";
			returnedString += "   topic name: " + messageListenerList.get(i).getTopicName()+"\n";
			returnedString += "    data type: " + listenersTopicTypesList.get(i)+"\n";
		}

		returnedString += "\nPublishers (" + this.publishersList.size() +"): \n";
		for(int i = 0; i < this.publishersList.size(); i++){
			returnedString +=" " + i + ":\n";
			returnedString +="   topic name: " + publishersList.get(i).getTopicName().toString()+"\n";
			returnedString +="    data type: " + publishersTopicTypesList.get(i)+"\n";
		}

		return returnedString;

	}

	private synchronized void setConfigurationFinished(){
		this.confFinished = true;
	}

	public synchronized boolean isConfigured(){
		return this.confFinished;
	}


}
