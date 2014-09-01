package unibg.robotics.tca;

/**
 * 
 * @author Andrea Fernandes da Fonseca
 * @author Yamuna Maccarana
 * 
 * This exeption is to be thrown when a Task object tries to retrieve messages from a topic which is not subscribed.
 */

public class TopicNotFoundException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public TopicNotFoundException(String topic)
	{
		super("ERROR: topic \""+topic+"\" is not subscribed");
	}
}
