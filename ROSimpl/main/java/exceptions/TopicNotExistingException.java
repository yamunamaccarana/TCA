package exceptions;

public class TopicNotExistingException extends Exception {
	private static final long serialVersionUID = -1579494952980130520L;
	private String errorMessage;
	
	public TopicNotExistingException(String topicName){
		super();
		this.errorMessage = "Topic " + topicName + " not existing";
	}
	
	public String toString(){
		return this.errorMessage;
	}
}
