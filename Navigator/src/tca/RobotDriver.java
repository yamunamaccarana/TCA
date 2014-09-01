package tca;

import java.util.Iterator;

import rosgate.RosGate;
import transferObjects.OdometryTO;
import transferObjects.StateTO;
import unibg.robotics.tca.Message;
import unibg.robotics.tca.messages.OdometryMessage;
import unibg.robotics.tca.Task;
import unibg.robotics.tca.TaskInterface;
import unibg.robotics.tca.TopicNotFoundException;
import unibg.robotics.tca.TopicObject;
import unibg.robotics.tca.messages.TwistMessage;
import unibg.robotics.tca.messages.PoseMessage;

import org.osoa.sca.annotations.Property;
import org.osoa.sca.annotations.Scope;


@Scope("COMPOSITE")
public class RobotDriver extends Task implements TaskInterface {
	
	
	// Ros configuration and setup
	private RosGate rosgate;
	private MessageManager msgMan;
	private BARTOdometryMessageObs odoObs;
	private BARTStateMessageObs stateObs;
	
	// Read topics name from properties
	@Property(name="OdometryTopicName", required=true)
	protected String ODOMETRY_TOPIC;
	
	@Property(name="StateTopicName", required=true)
	protected String STATE_TOPIC;
	
	@Property(name="TwistTopicName", required=true)
	protected String TWIST_TOPIC;
	
	@Property(name="CommandTopicName", required=true)
	protected String COMMAND_TOPIC;
	
	private geometry_msgs.Twist twistMessage;
	private geometry_msgs.Vector3 vecLinear; 
	private geometry_msgs.Vector3 vecAngular;

	private std_msgs.String commandMessage;

	@Property(name="RosMasterURI", required=true)
	protected String RosMasterURI;
	
	
	// TCA configuration
	Message msg;
	TopicObject twistTopic = new TopicObject("TWIST");
	TopicObject odometryTopic = new TopicObject("ODOMETRY");
	TopicObject commandTopic = new TopicObject("COMMAND");
	TopicObject stateTopic = new TopicObject("STATE");

	@Override
	public void initialize() {
		
		// Setup ROS subsystem
		initROS();
		
		publish(odometryTopic);
		publish(stateTopic);
		
		subscribe(twistTopic);
		subscribe(commandTopic);
	}

	@Override
	public void execute() {

		// Receive a TWIST and send it to the robot
		TwistMessage twistmsg;
		Iterator<Message> iteratorTwist=null;
		
		try {
			iteratorTwist = receive(twistTopic);
		} catch (TopicNotFoundException e) {e.printStackTrace();}

		while(iteratorTwist.hasNext()) {
			msg=iteratorTwist.next();
			twistmsg=(TwistMessage)msg.getMessage();
			System.out.println(getID()+" - Message from topic: "+msg.getTopic().getTopicName());
			System.out.println(twistmsg.toString()+"\n");
			
			this.vecLinear.setX(twistmsg.lin_x);
			this.vecLinear.setY(twistmsg.lin_y);
			this.vecLinear.setZ(twistmsg.lin_z);
			this.twistMessage.setLinear(this.vecLinear);

			this.vecAngular.setX(twistmsg.ang_x);
			this.vecAngular.setY(twistmsg.ang_y);
			this.vecAngular.setZ(twistmsg.ang_z);
			this.twistMessage.setAngular(this.vecAngular);
			this.rosgate.publishMessage(TWIST_TOPIC, this.twistMessage);
		}
		
		// Receive a Command message and send it to the robot
		String stringMessage;
		Iterator<Message> iteratorString=null;
		
		try {
			iteratorString = receive(commandTopic);
		} catch (TopicNotFoundException e) {e.printStackTrace();}
		
		while(iteratorString.hasNext()) {
			msg=iteratorString.next();
			stringMessage=(String)msg.getMessage();
			System.out.println(getID()+" - Message from topic: "+msg.getTopic().getTopicName());
			System.out.println(stringMessage.toString()+"\n");
			
			this.commandMessage.setData(stringMessage);
			this.rosgate.publishMessage(COMMAND_TOPIC, this.commandMessage);
		}

		// Receive an ODOMETRY message and send it back to the navigator
		try {
			OdometryTO odo = this.msgMan.getOdometryMessage();
     		send(odometryTopic,new OdometryMessage(new TwistMessage(odo.getTwistLinearX(), odo.getTwistLinearY(), odo.getTwistLinearZ(), odo.getTwistAngularX(), odo.getTwistAngularY(), odo.getTwistAngularZ()), 
					new PoseMessage(odo.getPoseLinearX(), odo.getPoseLinearY(), odo.getPoseLinearZ())));
			
		} catch (TopicNotFoundException e) {e.printStackTrace();}
		
		// Receive a STATE message and send it back to the navigator
		try {
			StateTO state = this.msgMan.getStateMessage();
			send (stateTopic, new String(state.getState()));
		} catch (TopicNotFoundException e) {e.printStackTrace();}
		
	}
	
	
	
	public void initROS(){
		
		System.out.println("Connecting to Ros master uri: " + RosMasterURI);
		rosgate = new RosGate(RosMasterURI);
		this.msgMan = new MessageManager();
		this.odoObs = new BARTOdometryMessageObs(msgMan);
		this.stateObs = new BARTStateMessageObs(msgMan);

		//Input topics
		//Odometry topic
		this.rosgate.createListener(ODOMETRY_TOPIC, nav_msgs.Odometry._TYPE);
		this.rosgate.addObserver(ODOMETRY_TOPIC, this.odoObs);
		//State topic
		this.rosgate.createListener(STATE_TOPIC, std_msgs.String._TYPE);
		this.rosgate.addObserver(STATE_TOPIC, this.stateObs);

		//Output topics
		//Twist topic
		this.rosgate.createPublisher(TWIST_TOPIC, geometry_msgs.Twist._TYPE);
		//Command topic
		this.rosgate.createPublisher(COMMAND_TOPIC, std_msgs.String._TYPE);

		try {
			this.rosgate.setup();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(this.rosgate.getRosGateDescription());


		//Create messages
		//Create twist message
		this.twistMessage = (geometry_msgs.Twist) rosgate.createMessage( geometry_msgs.Twist._TYPE);
		this.vecLinear = (geometry_msgs.Vector3) rosgate.createMessage( geometry_msgs.Vector3._TYPE);
		this.vecAngular = (geometry_msgs.Vector3) rosgate.createMessage( geometry_msgs.Vector3._TYPE);

		//Create string output message
		this.commandMessage = (std_msgs.String)rosgate.createMessage(std_msgs.String._TYPE);

	}
}