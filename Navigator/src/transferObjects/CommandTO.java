package transferObjects;

public class CommandTO {
	private String commandPayload = "";
	
	public CommandTO(){
	}
	
	public void setCommand(String commandRec){
		this.commandPayload = commandRec;
	}
	
	public String getCommand(){
		return this.commandPayload;
	}
}
