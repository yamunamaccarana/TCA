package exceptions;

public class TopicAlreadyExistingException extends Exception{
	private static final long serialVersionUID = 4872208384491480211L;
	private String errorMessage;
	
	public TopicAlreadyExistingException(String topicName){
		super();
		this.errorMessage = "Topic " + topicName + " already existing";
	}
	
	public String toString(){
		return this.errorMessage;
	}

}
