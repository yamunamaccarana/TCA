package run;


import bartGUI.BartConsole;
import observers.OdometryMessageObserver;
import observers.StringMessageObserver;
import rosgate.RosGate;
import simulatorDemo.YoubotSimulatorDemo;


public class MyRosRun {

	public static void main(String[] argv) {
		System.out.println("*********************");
		System.out.println("** Custom RosRun 2 **");
		System.out.println("*********************");


		RosGate rosgate = new RosGate();

//		launchYoubotSimulatorDemo(rosgate);

//		launchTest(rosgate);
		
		launchBARTConsole(rosgate);



	}

	
	public static void launchBARTConsole(RosGate rg){
		BartConsole console;
		console = new BartConsole(rg);
		System.out.println(rg.getRosGateDescription());
		Thread t = new Thread(console);
		t.start();
	}
	
	
	public static void launchYoubotSimulatorDemo(RosGate rg){
		YoubotSimulatorDemo demo;
		demo = new YoubotSimulatorDemo(rg);
		System.out.println(rg.getRosGateDescription());
		Thread t = new Thread(demo);
		t.start();
	}

	private static void launchTest(RosGate rg){

		StringMessageObserver stringObserver = new StringMessageObserver("first");
		OdometryMessageObserver odometryObserver = new OdometryMessageObserver("second");

		rg.createListener("odometry_input_topic", nav_msgs.Odometry._TYPE);
		rg.createListener("string_input_topic", std_msgs.String._TYPE);
		rg.createListener("string_input_topic", std_msgs.String._TYPE);

		rg.addObserver("odometry_input_topic", odometryObserver);
		rg.addObserver("dummy", odometryObserver);
		rg.addObserver("string_input_topic", stringObserver);

		rg.createPublisher("twist_output_topic", geometry_msgs.Twist._TYPE);
		rg.createPublisher("string_output_topic", std_msgs.String._TYPE);
		rg.createPublisher("string_output_topic", std_msgs.String._TYPE);

		try {
			rg.setup();
		} catch (InterruptedException e) {
			System.err.println("There were errors during node setup");
			e.printStackTrace();
		}

		geometry_msgs.Twist twistMessage = (geometry_msgs.Twist) rg.createMessage( geometry_msgs.Twist._TYPE);
		std_msgs.String stringMessage =  (std_msgs.String) rg.createMessage(std_msgs.String._TYPE);


		geometry_msgs.Vector3 vecLinear = (geometry_msgs.Vector3) rg.createMessage( geometry_msgs.Vector3._TYPE);
		geometry_msgs.Vector3 vecAngular = (geometry_msgs.Vector3) rg.createMessage( geometry_msgs.Vector3._TYPE);

		System.out.println(rg.getRosGateDescription());

		for(int i = 0; i < 100; i++){
			stringMessage.setData("data output test " + i);
			rg.publishMessage("string_output_topic", stringMessage);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

			vecLinear.setX(1.1);
			vecLinear.setY(1.2);
			vecLinear.setZ(1.3);
			twistMessage.setLinear(vecLinear);
			vecAngular.setX(2.1);
			vecAngular.setY(2.2);
			vecAngular.setZ(2.3);
			twistMessage.setAngular(vecAngular);
			rg.publishMessage("twist_output_topic", twistMessage);
			rg.publishMessage("dummy_output", twistMessage);

			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
}

