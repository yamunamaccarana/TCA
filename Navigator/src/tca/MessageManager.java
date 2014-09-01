package tca;

import transferObjects.OdometryTO;
import transferObjects.StateTO;


public class MessageManager {
	
	StateTO stateMemory;
	OdometryTO odometryMemory;
	
	
	public MessageManager(){
		this.stateMemory = new StateTO();
		this.odometryMemory = new OdometryTO();
	}
	
	
	public synchronized void addOdometryMessage(OdometryTO inMessage){
		this.odometryMemory = inMessage;
	}
	
	public synchronized OdometryTO getOdometryMessage(){
		return this.odometryMemory;
	}
	
	public synchronized void addStateMessage(StateTO inState){
		this.stateMemory = inState;
	}
	
	public synchronized StateTO getStateMessage(){
		return this.stateMemory;
	}
	
}
