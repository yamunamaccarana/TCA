package unibg.robotics.tca;

import java.io.Serializable;

/**
 * 
 * @author Andrea Fernandes da Fonseca
 * @author Yamuna Maccarana
 * 
 */
public class Message
{
	private Serializable message;
	private TopicObject topic;
	
	/**
	 * 
	 * @param top message topic
	 * @param obj message body
	 */
	public Message(TopicObject topic, Serializable obj)
	{
		setTopic(topic);
		setMessage(obj);
	}
	
	/**
	 * This method is used to set a new message body after the object have already been instantiated
	 * @param msg new message body
	 */
	private void setMessage(Serializable msg)
	{
		message=msg;
	}
	
	/**
	 * This method is used to set a new topic after the object have already been instantiated
	 * @param msg new message topic
	 */
	private void setTopic(TopicObject top)
	{
		topic=top;
	}
	
	/**
	 * Getter method for message body
	 * @return returns the message body
	 */
	public Serializable getMessage()
	{
		return message;
	}
	
	/**
	 * Getter method for message topic
	 * @return returns the message topic
	 */
	public TopicObject getTopic()
	{
		return topic;
	}
}
