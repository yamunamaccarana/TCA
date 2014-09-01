package exceptions;

public class AlreadyConfiguredNodeException extends Exception{
	private static final long serialVersionUID = 1L;
	private String errMessage;
	
	public AlreadyConfiguredNodeException(){
		super(); 
		this.errMessage = "RosGate already configured";
	}
	
	public String toString(){
		return this.errMessage;
	}

}
