package unibg.robotics.tca.messages;

import java.io.Serializable;

public class OdometryMessage implements Serializable
{
	/**
	 * @author Andrea Fernandes da Fonseca
	 * @author Yamuna Maccarana
	 */
	private static final long serialVersionUID = -2851855958217165646L;

	public TwistMessage twist;
	
	public PoseMessage pose;

	public OdometryMessage()
	{
		
	}
	
	public OdometryMessage(TwistMessage tmsg, PoseMessage pmsg)
	{
		setPose(pmsg);
		setTwist(tmsg);
	}
	
	public void setPose(PoseMessage pmsg)
	{
		pose=pmsg;
	}
	
	public void setTwist(TwistMessage tmsg)
	{
		twist=tmsg;
	}
	
	public String toString()
	{
		return pose.toString()+"\n"+twist.toString();
	}
}
