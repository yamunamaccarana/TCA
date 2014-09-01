import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



import org.apache.tuscany.sca.assembly.ComponentReference;
import org.apache.tuscany.sca.assembly.ComponentService;
import org.apache.tuscany.sca.host.embedded.SCADomain;
import org.apache.tuscany.sca.host.embedded.management.ComponentManager;

public class LaunchBartSCAGUI {
	public static String CONTRIBUTION_NAME = "gui_ros_sca"; 

	public static void main(String[] args) {
		launchSystem();
	}



	public static void launchSystem(){
     
		// Starts the message broker
		System.out.println("Starting broker domain...");
		SCADomain scaBrokerDomain = SCADomain.newInstance("twistodometrybroker.composite");
		System.out.println("Broker domain test...");
		domainAnalysis(scaBrokerDomain);
		System.out.println("Broker domain test done.");
		System.out.println("Starting SCA domain");
		
		try {Thread.sleep(5000);} catch (InterruptedException e1) {e1.printStackTrace();}

		// Starts the SCA domain with components
		SCADomain scaDomain = SCADomain.newInstance(CONTRIBUTION_NAME + ".composite");
		System.out.println("SCA domain test");
		domainAnalysis(scaDomain);
		System.out.println("SCA domain test done");
		
		
		BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
		String input = "";
		while(input.compareTo("quit")!= 0){
			System.out.println("enter 'quit' to stop SCA domain");
			try {
				input = br.readLine();
			} catch (IOException e) {
				System.err.println("Error reading from user!");
			}
		}

		System.out.println("Stopping SCA domain...");
		//Close the domain
		scaDomain.close();
		
		System.out.println("Stopping Broker domain...");
		//Close the broker domain
		scaBrokerDomain.close();
		
		System.out.println("All SCA domains stopped, press ctrl+c to close the ROS node");

	}

	public static void domainAnalysis(SCADomain d){
		int compCtr = 0;
		//Get the test Manager
		ComponentManager cm = d.getComponentManager();
		//Get the components by names
		Set<String> components = cm.getComponentNames();
		Iterator<String> iter = components.iterator();

		//Explore components and print data
		while(iter.hasNext()){
			compCtr++;
			String currName = iter.next();
			System.out.print(compCtr + ") component " + currName);
			if(cm.isComponentStarted(currName)){
				System.out.println(" is started");
			}else{
				System.out.println(" is not started");
			}

			List<ComponentService> servs = cm.getComponent(currName).getServices();
			System.out.println("  Services:");
			if(servs.size() != 0){
				for(int i = 0; i < servs.size(); i++){
					System.out.println("    " + servs.get(i).getName());
				}
			}else{
				System.out.println("    none");	
			}

			List<ComponentReference> refs = cm.getComponent(currName).getReferences();
			System.out.println("  References:");
			if(refs.size() != 0){
				for(int i = 0; i < refs.size(); i++){
					System.out.println("    " + refs.get(i).getName());
				}
			}else{
				System.out.println("    none");	
			}
		}
	}
}
