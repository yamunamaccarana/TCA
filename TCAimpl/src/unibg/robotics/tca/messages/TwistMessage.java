package unibg.robotics.tca.messages;

import java.io.Serializable;

public class TwistMessage implements Serializable
{
	/**
	 * @author Andrea Fernandes da Fonseca
	 * @author Yamuna Maccarana
	 */
	private static final long serialVersionUID = -8296207730565419391L;
	
	public Double lin_x, lin_y, lin_z, ang_x, ang_y, ang_z;
	
	public TwistMessage()
	{
		
	}
	
	public TwistMessage(Double lx, Double ly, Double lz, Double ax, Double ay, Double az)
	{
		lin_x=lx;
		lin_y=ly;
		lin_z=lz;
		ang_x=ax;
		ang_y=ay;
		ang_z=az;
	}
	
	public String toString()
	{
		return "Linear: x="+lin_x+" y="+lin_y+" z="+lin_z+"\n"+"Angular: x="+ang_x+" y="+ang_y+" z="+ang_z;
	}
}
