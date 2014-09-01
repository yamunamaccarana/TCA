package transferObjects;

public class StateTO {
	public String statePayload = "";
	
	public StateTO(){
	}
	
	public void setState(String stateRec){
		this.statePayload = stateRec;
	}
	
	public String getState(){
		return statePayload;
	}
}
