package twistodometry;

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
public class Navigator extends Task implements TaskInterface
{
	Message msg;
	TopicObject twistTopic = new TopicObject("TWIST");
	TopicObject odometryTopic = new TopicObject("ODOMETRY");
	
	@Override
	public void initialize()
	{
		subscribe(odometryTopic);
		publish(twistTopic);
	}
	
	@Override
	public void execute()
	{
		OdometryMessage odometrymsg;
		
		Iterator<Message> iterator=null;
		try {
			iterator = receive(odometryTopic);
		} catch (TopicNotFoundException e) {e.printStackTrace();}
		
		while(iterator.hasNext())
		{
			msg=iterator.next();
			odometrymsg=(OdometryMessage)msg.getMessage();
			System.out.println(getID()+" - Message from topic: "+msg.getTopic().getTopicName());
			System.out.println(odometrymsg.toString()+"\n");
		}
		try {
			send(twistTopic,new TwistMessage(Math.random(),Math.random(),Math.random(),Math.random(),Math.random(),Math.random()));
		} catch (TopicNotFoundException e) {e.printStackTrace();}
	}
}