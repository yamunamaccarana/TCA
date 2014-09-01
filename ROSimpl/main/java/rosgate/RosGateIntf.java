package rosgate;
import java.util.Observer;

import org.ros.internal.message.Message;

import exceptions.AlreadyConfiguredNodeException;
import exceptions.NotYetConfiguredException;
import exceptions.TopicAlreadyExistingException;
import exceptions.TopicNotExistingException;


public interface RosGateIntf {

	//Topic subscribing methods
	void addObserverElement(String observedTopicName, Observer rosGateObs) throws TopicNotExistingException, AlreadyConfiguredNodeException;
	void createListener(String topicName, String listenedType) throws TopicAlreadyExistingException, AlreadyConfiguredNodeException;
	
	//Topic publishing methods
	public void createPublisher(String publishedTopicName, String publishedType) throws TopicAlreadyExistingException, AlreadyConfiguredNodeException;
	public void publish(Message msg, String targeTopicName) throws TopicNotExistingException, NotYetConfiguredException;
	
	//Configure phase	
	public boolean isConfigured();
	
	//Debug
	public String showYourself();
	
	
}
