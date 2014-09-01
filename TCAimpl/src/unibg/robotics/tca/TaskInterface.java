package unibg.robotics.tca;

import org.osoa.sca.annotations.*;

/**
 * 
 * @author Andrea Fernandes da Fonseca
 * @author Yamuna Maccarana
 * 
 * This interface is used by Tuscany to map the services of Task
 */

@Remotable
public interface TaskInterface extends Runnable
{
	public void scheduleTask();
	public void unscheduleTask();
	public boolean isRunning();
}