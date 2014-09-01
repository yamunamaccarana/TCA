package exceptions;

public class NotYetConfiguredException extends Exception{
	private static final long serialVersionUID = -9126233544060417961L;
	private String errorMessage;
	
	public NotYetConfiguredException(){
		super();
		this.errorMessage = "RosGate not yet configured";
	}
	
	public String toString(){
		return this.errorMessage;
	}

}
