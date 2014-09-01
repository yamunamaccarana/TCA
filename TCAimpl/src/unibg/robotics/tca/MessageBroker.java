package unibg.robotics.tca;

import org.osoa.sca.annotations.Remotable;

/**
 * 
 * @author Andrea Fernandes da Fonseca
 * @author Yamuna Maccarana
 * 
 * This interface is used by Tuscany to map the services of MessageBrokerImpl
 */

@Remotable //see SCA documentation
public interface MessageBroker
{
	public void startBroker() throws Exception;
}
