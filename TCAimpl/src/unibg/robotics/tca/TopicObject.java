package unibg.robotics.tca;

public class TopicObject
{
	/**
	 * @author Andrea Fernandes da Fonseca
	 * @author Yamuna Maccarana
	 * 
	 * This class is used to store a Topic name and make it easier for the user to use it and edit it in the code.
	 */
	private String topic;
	
	/**
	 * 
	 * @param t topic name
	 */
	public TopicObject(String t)
	{
		setTopicName(t);
	}

	/**
	 * Getter method for the topic name
	 * @return a String containing the topic name
	 */
	public String getTopicName()
	{
		return topic;
	}

	/**
	 * Setter method for the topic name
	 * @param t topic name
	 */
	public void setTopicName(String t)
	{
		topic = t;
	}
}
