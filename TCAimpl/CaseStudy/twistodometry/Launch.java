package twistodometry;

import org.apache.tuscany.sca.host.embedded.SCADomain;
import unibg.robotics.tca.TaskInterface;

public class Launch extends Thread{
    public static void main(String[] args) throws Exception
    {
        Launch launch = new Launch();
        launch.start();
    }

	public void run()
	{
		System.out.println("Starting...");
		SCADomain scaDomain = SCADomain.newInstance("prova12tasks.composite");
		TaskInterface navigator=scaDomain.getService(TaskInterface.class, "NavigatorComponent/TaskInterface");
		TaskInterface robotDriver=scaDomain.getService(TaskInterface.class, "RobotDriverComponent/TaskInterface");
		navigator.scheduleTask();
		robotDriver.scheduleTask();
	}
}