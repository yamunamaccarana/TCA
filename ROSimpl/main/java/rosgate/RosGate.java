package rosgate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observer;

import org.ros.exception.RosRuntimeException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.internal.message.Message;
import org.ros.message.MessageFactory;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import exceptions.AlreadyConfiguredNodeException;
import exceptions.NotYetConfiguredException;
import exceptions.TopicAlreadyExistingException;
import exceptions.TopicNotExistingException;

public class RosGate {

	private CommandLineLoader loader; 
	private String nodeClassName;
	private NodeConfiguration nodeConfiguration;
	private NodeMain nodeMain = null;
	private RosGateIntf rosGateInterface;
	MessageFactory topicMessageFactory;
	String[] nodeName = new String[1];


	public RosGate(){
		this("http://localhost:11311");
	}
	
	public RosGate(String masterURI){
		nodeName[0] = "rosgate.RosGateNode";
		
		loader = new CommandLineLoader(Lists.newArrayList(nodeName));
		nodeClassName = loader.getNodeClassName();
		System.out.println("Loading node class: " + loader.getNodeClassName());
		nodeConfiguration = loader.build();
		try {
			nodeConfiguration.setMasterUri(new URI(masterURI));
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
	
		try {
			nodeMain = loader.loadClass(nodeClassName);
		} catch (ClassNotFoundException e) {
			throw new RosRuntimeException("Unable to locate node: " + nodeClassName, e);
		} catch (InstantiationException e) {
			throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
		} catch (IllegalAccessException e) {
			throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
		}
		
		
		rosGateInterface = (RosGateIntf) nodeMain;
		
		System.out.println("Using RosGate Version 1.6.3");

	}

	public void createListener(String topicName, String type){
		try {
			this.rosGateInterface.createListener(topicName, type);
		} catch (TopicAlreadyExistingException e) {
//			e.printStackTrace();
			System.err.println(e.toString()+", listener not created!");
		} catch (AlreadyConfiguredNodeException e){
			System.err.println(e.toString() + " cannot create listener on topic " + topicName + "!");
		}
	}

	public void addObserver(String topicName, Observer obs){
		try {
			this.rosGateInterface.addObserverElement(topicName, obs);
		} catch (TopicNotExistingException e) {
//			e.printStackTrace();
			System.err.println(e.toString()+", observer not added!");
		} catch(AlreadyConfiguredNodeException e){
			System.err.println(e.toString() + " cannot add observer for topic " + topicName + "!");
		}
	}

	public void createPublisher(String topicName, String type){
		try {
			this.rosGateInterface.createPublisher(topicName, type);
		} catch (TopicAlreadyExistingException e) {
//			e.printStackTrace();
			System.err.println(e.toString()+", publisher not created!");
		}catch(AlreadyConfiguredNodeException e ){
			System.err.println(e.toString() + " cannot create publisher for topic " + topicName + "!");
		}
	}

	public Message createMessage(String type){
		return this.topicMessageFactory.newFromType(type);
	}

	public void publishMessage(String topicName, Message message){
		try {
			this.rosGateInterface.publish(message, topicName);
		} catch (TopicNotExistingException e) {
//			e.printStackTrace();
			System.err.println(e.toString()+", cannot publish on this topic!");
		} catch(NotYetConfiguredException e){
			System.err.println(e.toString() + " cannot publish on topic " + topicName + "!");
		}
	}

	public String getRosGateDescription(){
		return this.rosGateInterface.showYourself();
	}

	public void setup() throws InterruptedException{

		this.topicMessageFactory = nodeConfiguration.getTopicMessageFactory();

		Preconditions.checkState(nodeMain != null);
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		nodeMainExecutor.execute(nodeMain, nodeConfiguration);


		while(!rosGateInterface.isConfigured()){
			Thread.sleep(100);	
			System.out.println("....");
		}

	}
}
