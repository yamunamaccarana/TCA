package rosgate;
import java.util.Observable;

import org.ros.internal.message.Message;
import org.ros.message.MessageListener;

public class TopicMessageListener extends Observable implements MessageListener<Message>{

	private String topicName = "";
	
	public TopicMessageListener(String topicName){
		super();
		this.topicName = topicName;
	}
	
	@Override
	public void onNewMessage(Message inputMessage) {
		 setChanged();
		 notifyObservers(inputMessage);
	}
	
	public String getTopicName(){
		return this.topicName;
	}
	
	
}
