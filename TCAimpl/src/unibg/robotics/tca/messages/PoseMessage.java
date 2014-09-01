package unibg.robotics.tca.messages;

import java.io.Serializable;

public class PoseMessage implements Serializable
{
	/**
	 * @author Andrea Fernandes da Fonseca
	 * @author Yamuna Maccarana
	 */
	private static final long serialVersionUID = 5808839220427674061L;
	
	public double x, y, z;
	
	public PoseMessage()
	{
		
	}
	
	public PoseMessage(double x, double y, double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public String toString()
	{
		return "Position: x="+x+" y="+y+" z="+z;
	}
}
