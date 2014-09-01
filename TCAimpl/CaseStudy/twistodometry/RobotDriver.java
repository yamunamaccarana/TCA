package twistodometry;

import java.util.Iterator;

import unibg.robotics.tca.Message;
import unibg.robotics.tca.messages.OdometryMessage;
import unibg.robotics.tca.Task;
import unibg.robotics.tca.TaskInterface;
import unibg.robotics.tca.TopicNotFoundException;
import unibg.robotics.tca.TopicObject;
import unibg.robotics.tca.messages.TwistMessage;
import unibg.robotics.tca.messages.PoseMessage;
import org.osoa.sca.annotations.Scope;

@Scope("COMPOSITE")
public class RobotDriver extends Task implements TaskInterface
{
	Message msg;
	TopicObject twistTopic = new TopicObject("TWIST");
	TopicObject odometryTopic = new TopicObject("ODOMETRY");
	
	@Override
	public void initialize()
	{
		publish(odometryTopic);
		subscribe(twistTopic);
	}

	@Override
	public void execute()
	{
		TwistMessage twistmsg;
		
		Iterator<Message> iterator=null;
		try {
			iterator = receive(twistTopic);
		} catch (TopicNotFoundException e) {e.printStackTrace();}
		
		while(iterator.hasNext())
		{
			msg=iterator.next();
			twistmsg=(TwistMessage)msg.getMessage();
			System.out.println(getID()+" - Message from topic: "+msg.getTopic().getTopicName());
			System.out.println(twistmsg.toString()+"\n");
		}
		try {
			send(odometryTopic,new OdometryMessage(
					new TwistMessage(Math.random(),Math.random(),Math.random(),Math.random(),Math.random(),Math.random()),
					new PoseMessage(Math.random(),Math.random(),Math.random())
					));
		} catch (TopicNotFoundException e) {e.printStackTrace();}
	}
}