package tca;

import java.util.Observable;
import java.util.Observer;

import transferObjects.StateTO;

public class BARTStateMessageObs implements Observer {

	MessageManager msgMan;

	public BARTStateMessageObs(MessageManager msgMan){
		this.msgMan = msgMan;
	}

	@Override
	public void update(Observable o, Object arg) {
		//Get the message
		std_msgs.String msg = (std_msgs.String) arg;
		
		//Set fields to TO
		StateTO state = new StateTO();
		state.setState(msg.getData());		
		
		//Add TO to memory
		msgMan.addStateMessage(state);
	}

}
