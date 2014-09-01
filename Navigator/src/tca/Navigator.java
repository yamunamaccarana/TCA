package tca;

import java.util.Iterator;

import unibg.robotics.tca.Message;
import unibg.robotics.tca.messages.OdometryMessage;
import unibg.robotics.tca.Task;
import unibg.robotics.tca.TaskInterface;
import unibg.robotics.tca.TopicNotFoundException;
import unibg.robotics.tca.TopicObject;
import unibg.robotics.tca.messages.TwistMessage;
import org.osoa.sca.annotations.Scope;

@Scope("COMPOSITE")
public class Navigator extends Task implements TaskInterface {
	
	//TCA Topics
	Message msg;
	TopicObject twistTopic = new TopicObject("TWIST");
	TopicObject odometryTopic = new TopicObject("ODOMETRY");
	TopicObject stateTopic = new TopicObject("STATE");
	TopicObject commandTopic = new TopicObject("COMMAND");
	
	@Override
	public void initialize() {
		// Init TCA Communication
		subscribe(odometryTopic);
		subscribe(stateTopic);
		
		publish(twistTopic);
		publish(commandTopic);
	}
	
	@Override
	public void execute() {
		
		// Receive and print ODOMETRY messages
		OdometryMessage odometrymsg;
		Iterator<Message> iteratorOdometry=null;
		try {
			iteratorOdometry = receive(odometryTopic);
		} catch (TopicNotFoundException e) {e.printStackTrace();}
		
		while(iteratorOdometry.hasNext()) {
			msg=iteratorOdometry.next();
			odometrymsg=(OdometryMessage)msg.getMessage();
			System.out.println(getID()+" - Message from topic: "+msg.getTopic().getTopicName());
			System.out.println(odometrymsg.toString()+"\n");
		}
		
		// Receive and print STATE messages
		String stateMsg;		
		Iterator<Message> iteratorState=null;
		try {
			iteratorState = receive(stateTopic);
		} catch (TopicNotFoundException e) {e.printStackTrace();}
		
		while(iteratorState.hasNext()) {
			msg=iteratorState.next();
			stateMsg=(String)msg.getMessage();
			System.out.println(getID()+" - Message from topic: "+msg.getTopic().getTopicName());
			System.out.println(stateMsg.toString()+"\n");
		}
		
		
		// Send a TWIST
		try {
			send(twistTopic,new TwistMessage(1.0, 2.0, 3.0, 4.0, 5.0, 6.0));
		} catch (TopicNotFoundException e) {e.printStackTrace();}
		
		// Send a COMMAND
		try {
			send(commandTopic,new String("START_MOTORS"));
		} catch (TopicNotFoundException e) {e.printStackTrace();}
	}
}