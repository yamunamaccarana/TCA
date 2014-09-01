package twistodometry;

import org.apache.tuscany.sca.host.embedded.SCADomain;

public class LaunchBroker extends Thread{
    public static void main(String[] args) throws Exception
    {
        LaunchBroker launch = new LaunchBroker();
        launch.start();
    }

	public void run()
	{
		System.out.println("Starting...");
		@SuppressWarnings("unused")
		SCADomain scaDomain = SCADomain.newInstance("prova12broker.composite");
	}
}