package unibg.robotics.tca;

import org.apache.activemq.broker.BrokerService;
import org.osoa.sca.annotations.AllowsPassByReference;
import org.osoa.sca.annotations.EagerInit;
import org.osoa.sca.annotations.Init;
import org.osoa.sca.annotations.Property;
import org.osoa.sca.annotations.Scope;

/**
 * 
 * @author Andrea Fernandes da Fonseca
 * @author Yamuna Maccarana
 * 
 * This class is used to create a new BrokerService based on the parameters given in the composite file.
 * The broker can reference to a remote JMS server or it can use ApacheMQ locally 
 *
 */

@EagerInit //see SCA documentation
@AllowsPassByReference //see SCA documentation
@Scope("COMPOSITE") //see SCA documentation
public class MessageBrokerImpl implements MessageBroker
{
	@Property //see SCA documentation
	protected String IP; //IP address of the JMS server
	@Property //see SCA documentation
	protected int port; //port address of the JMS server
	
	/**
	 * This method is used to start a new broker using the parameters given in the composite file
	 */
	@Init //see SCA documentation
	public void startBroker() throws Exception
	{
        BrokerService broker = new BrokerService();
        broker.setUseJmx(true);
        broker.addConnector("tcp://"+IP+":"+port);
        broker.start();
    }
}
