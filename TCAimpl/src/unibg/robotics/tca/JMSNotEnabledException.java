package unibg.robotics.tca;

public class JMSNotEnabledException extends Exception
{
	private static final long serialVersionUID = 427074398875878904L;

	public JMSNotEnabledException()
	{
		super("ERROR: JMS is not enabled");
	}
}
