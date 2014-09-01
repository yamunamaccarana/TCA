package unibg.robotics.tca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.osoa.sca.annotations.*;

/**
 * 
 * @author Andrea Fernandes da Fonseca
 * @author Yamuna Maccarana
 * 
 * This is the main class of the project, which represents a Task component
 */

@EagerInit //see SCA documentation
@AllowsPassByReference //see SCA documentation
@Scope("COMPOSITE") //see SCA documentation
public abstract class Task implements TaskInterface
{
	//----JMS variables-------------------------------------------------
	
	private Connection connection; //connection to the JMS broker
	private Map<MessageProducer, Session> producerSessions; //used to bind each MessageProducer to its Session
	private Map<TopicObject, MessageProducer> mapProducerFromTopic; //used to retrieve a MessageProducer given a String which identifies the topic
	private Map<TopicObject, MessageConsumer> mapConsumerFromTopic; //used to retrieve a MessageConsummer given a String which identifies the topic
	private Map<String, TopicObject> topicsAsPublisher; //contains all the topics in which the Task publishes messages
	private Map<String, TopicObject> topicsAsSubscriber; //contains all the topics subscribed by the Task
	private Map<TopicObject, ArrayList<Message>> inboxes; //contains all messages arrived from the last execution to the current one
	private ArrayList<Message> outbox; //contains all the messages that must be sent at the end of the current execution
	
	//----Properties----------------------------------------------------
	
	@Property(required=true) //see SCA documentation
	protected boolean runOnStartup;
	@Property(required=true) //see SCA documentation
	protected boolean mayInterruptIfRunning;
	@Property(required=true) //see SCA documentation
	protected boolean useJMS;
	@Property(required=false) //see SCA documentation
	protected String user;
	@Property(required=false) //see SCA documentation
	protected String password;
	@Property(required=false) //see SCA documentation
	protected String url;
	@Property(required=false)
	protected String topicRename;
	
	private String taskID;
	private int period;
	private int delay;

	//----Task variables------------------------------------------------
	
	private ScheduledExecutorService scheduler;
	private ScheduledFuture<?> future;
	private boolean running;

	//----Abstract methods----------------------------------------------
	
	/**
	 * This method is to be overwritten by the user, runs only one time before the first execution
	 */
	public abstract void initialize();
	
	/**
	 * This method is to be overwritten by the user, it contains the instructions run by the Task at each execution
	 */
	public abstract void execute();
	
	//----Task methods--------------------------------------------------
	
	/**
	 * This method will be executed periodically after the Task is scheduled and until it is unscheduled
	 */
	public final void run()
	{
		if(useJMS) //if this flag is set to false those operations are meaningless
		{
			Iterator<ArrayList<Message>> i=inboxes.values().iterator();
			while(i.hasNext())
			{
				ArrayList<Message> a = i.next();
				a.removeAll(a);
			}
			outbox.removeAll(outbox); //empties outbox at the beginning of each execution cycle
			receivePrivate();
		}
		execute();
		if(useJMS) //if this flag is set to false this operation is meaningless
			try{sendPrivate();}catch(Exception e){System.err.println(e.getMessage());}
	}
	
	/**
	 * This method is called automatically when the component is read in the composite file
	 */
	@Init //see SCA documentation
	public final void init()
	{
		inboxes=new HashMap<TopicObject, ArrayList<Message>>(); //instantiate this object
		outbox=new ArrayList<Message>(); //instantiate this object
		topicsAsSubscriber=new HashMap<String, TopicObject>(); //instantiate this object
		topicsAsPublisher=new HashMap<String, TopicObject>(); //instantiate this object
		running=false; //initializes the variable to false, since the Task in still not running
		if(useJMS) //if this flag is set to true the Task must setup a JMS connection, if it's false this step will be skipped
			setupJMS();
		initialize();
		if(!topicRename.isEmpty())
			renameTopics();
		if(useJMS)
		{
			for(TopicObject t : topicsAsPublisher.values())
				try {
					becomePublisher(t);
				} catch (Exception e) {e.printStackTrace();}
			for(TopicObject t : topicsAsSubscriber.values())
				try {
					becomeSubscriber(t);
				} catch (Exception e) {e.printStackTrace();}
		}
		if(runOnStartup) //if this flag is set to true the Task will be started automatically, otherwise the schedule method must be called
			scheduleTask();
	}

	/**
	 * This method is called if the user specified the topicRename property in the composite file.
	 * It is used to refactor topic names already present in a compiled .class file without having to access the source code
	 */
	private void renameTopics()
	{
		String temp[] = null;
		ArrayList<String> oldNames = new ArrayList<String>();
		ArrayList<String> newNames = new ArrayList<String>();
		StringTokenizer strTok = new StringTokenizer(topicRename, "::");
		while(strTok.hasMoreTokens())
		{
			temp = strTok.nextToken().split(",");
			oldNames.add(temp[0]);
			newNames.add(temp[1]);
		}
		for(String s : oldNames)
		{
			if(topicsAsPublisher.containsKey(s))
				topicsAsPublisher.get(s).setTopicName(newNames.get(oldNames.indexOf(s)));
			if(topicsAsSubscriber.containsKey(s))
				topicsAsSubscriber.get(s).setTopicName(newNames.get(oldNames.indexOf(s)));
		}
	}
	
	//----TaskInterface methods-----------------------------------------
	
	/**
	 * This method is used to schedule the Task component using the parameters given in the composite file, if the Task is already scheduled no operation will be done
	 */
	public final void scheduleTask()
	{
		scheduler=Executors.newScheduledThreadPool(1);
		if(!running)
		{
			future=scheduler.scheduleAtFixedRate(this, delay, period, TimeUnit.MILLISECONDS);
			running=true;
		}
	}
	
	/**
	 * This method is used to unschedule the Task component, if the Task is not scheduled yet or have already been unscheduled no operation will be done
	 */
	public final void unscheduleTask()
	{
		if(running)
		{
			future.cancel(mayInterruptIfRunning);
			scheduler.shutdown();
			running=false;
		}
	}
	
	/**
	 * Getter method for the isRunning variable
	 * @return returns true if the Task is scheduled, false otherwise
	 */
	public final boolean isRunning()
	{
		return running;
	}
	
	//----Getter/Setter methods for properties--------------------------
	
	/**
	 * Setter method for the taskID variable
	 * @param id ID (String) of the Task
	 */
	@Property(name="ID", required=true) //see SCA documentation
	public final void setID(String id)
	{
		taskID=id;
	}
	
	/**
	 * Getter method for the taskID variable
	 * @return returns the value of taskID
	 */
	public final String getID()
	{
		return taskID;
	}
	
	/**
	 * Setter method for the period variable, first used by the Tuscany runtime can be subsequently called by the user.
	 * If the user sets a new value for period the Task must be unscheduled and the scheduled again for the change to take effect.
	 * @param p period of execution of the Task
	 */
	@Property(name="period", required=true) //see SCA documentation
	public final void setPeriod(int p)
	{
		period=p;
	}

	/**
	 * Getter method for the period variable
	 * @return the value of the period variable
	 */
	public final int getPeriod()
	{
		return period;
	}

	/**
	 * Setter method for the delay variable, first used by the Tuscany runtime can be subsequently called by the user.
	 * If the user sets a new value for delay the Task must be unscheduled and the scheduled again for the change to take effect.
	 * @param d delay of execution of the Task
	 */
	@Property(name="delay", required=true) //see SCA documentation
	public final void setDelay(int d)
	{
		delay=d;
	}

	/**
	 * Getter method for the delay variable
	 * @return the value of the delay variable
	 */
	public final int getDelay()
	{
		return delay;
	}
	
	//----JMS Section - private methods---------------------------------
	
	/**
	 * This method is called if the useJMS flag is set to true.
	 * It sets a JMS connection with the parameters given in the composite file, the parameters unspecified will be set to the default value
	 */
	private final void setupJMS()
	{
		producerSessions=new HashMap<MessageProducer, Session>();
		mapProducerFromTopic=new HashMap<TopicObject, MessageProducer>();
		mapConsumerFromTopic=new HashMap<TopicObject, MessageConsumer>();
		if(user==null)
			user=ActiveMQConnection.DEFAULT_USER;
		if(password==null)
			password = ActiveMQConnection.DEFAULT_PASSWORD;
		if(url==null)
			url = ActiveMQConnection.DEFAULT_BROKER_URL;
		else
			url = "failover://tcp://"+url; //adds the protocols to the URL specified by the user in the composite file
		connection=connect();
	}
	
	/**
	 * This method is used by the Task object to connect to a JMS server
	 * @return returns the connection object that identifies the connection between the Task and the JMS server
	 */
	private final Connection connect()
	{
		Connection con=null;
		try
		{
			ConnectionFactory factory = new ActiveMQConnectionFactory(user, password, url);
		    con = factory.createConnection();
		    con.start();
		} catch(Exception e) {e.printStackTrace();}
		return con;
	}
	
	/**
	 * This method is used to obtain a MessageConsumer object for a given topic
	 * @param topicStr the topic for which to obtain the MessageConsumer object
	 * @return returns the MessageConsumer object for the topic given as parameter
	 * @throws JMSException
	 */
	private final MessageConsumer getMessageConsumer(TopicObject topicObj) throws JMSException
	{
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicObj.getTopicName());
        MessageConsumer consumer = session.createConsumer(topic);
        return consumer;
	}
	
	/**
	 * This method is used to obtain a MessageProducer object for a given topic
	 * @param topicStr the topic for which to obtain the MessageProducer object
	 * @return returns the MessageProducer object for the topic given as parameter
	 * @throws JMSException
	 */
	private final MessageProducer getMessageProducer(TopicObject topicObj) throws JMSException
	{
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicObj.getTopicName());
        MessageProducer producer = session.createProducer(topic);
        producerSessions.put(producer, session);
		return producer;
	}
	
	/**
	 * This method is used to obtain a obtain the Session object correspondent to a MessageProducer
	 * @param prod the MessageProducer for which to obtain the Session object
	 * @return returns the Session object for the MessageProducer given as parameter, if the parameter is not bound to a Session null will be returned
	 */
	private final Session getProducerSession(MessageProducer prod)
	{
		if(producerSessions.containsKey(prod))
			return producerSessions.get(prod);
		else
			return null;
	}

	/**
	 * This method must be called by the user inside the initialize method.
	 * It is used to publish to a topic given as parameter.
	 * @param topic the topic to publish into
	 * @throws JMSException
	 * @throws JMSNotEnabledException
	 */
	private final void becomePublisher(TopicObject topic) throws JMSException, JMSNotEnabledException
	{
		if(useJMS)
			mapProducerFromTopic.put(topic, getMessageProducer(topic));
		else
			throw new JMSNotEnabledException();
	}
	
	/**
	 * This method must be called by the user inside the initialize method.
	 * It is used to subscribe to a topic given as parameter.
	 * @param topic the topic to which subscribe
	 * @throws JMSException
	 * @throws JMSNotEnabledException
	 */
	private final void becomeSubscriber(TopicObject topic) throws JMSException, JMSNotEnabledException
	{
		if(useJMS)
			mapConsumerFromTopic.put(topic, getMessageConsumer(topic));
		else
			throw new JMSNotEnabledException();
	}
	
	/**
	 * This method is used by Task to retrieve a message from the JMS server.
	 * It uses an asynchronous reception method, so the Task will not get stuck waiting for a message and the periodicity will be maintained.
	 * @param topic : the topic from which to retrieve the message
	 * @return returns an ObjectMessage object retrieved from the JMS server or null if no message is available at the time 
	 * @throws JMSException
	 */
	private final ObjectMessage receiveJMS(TopicObject topic) throws JMSException
	{
		return (ObjectMessage)mapConsumerFromTopic.get(topic).receiveNoWait();
	}
	
	/**
	 * This method is used by Task to send a message to the JMS server.
	 * @param topic the topic to which the message will be sent
	 * @param o body of the message
	 * @throws JMSException
	 */
	private final void sendJMS(TopicObject topic, Serializable o) throws JMSException
	{
		mapProducerFromTopic.get(topic).send(getProducerSession(mapProducerFromTopic.get(topic)).createObjectMessage(o));
	}
	
	/**
	 * This method is called by Task at the beginning of each execution cycle if useJMS is set to true to populate the inbox list.
	 * It also converts JMS ObjectMessages to a simpler Message object.
	 */
	private final void receivePrivate()
	{
		Iterator<TopicObject> iter = topicsAsSubscriber.values().iterator();
		TopicObject topic;
		ObjectMessage msg;
		while(iter.hasNext())
		{
			topic=iter.next();
			try {
				while((msg=receiveJMS(topic))!=null)
					inboxes.get(topic).add(new Message(topic,msg.getObject()));
			} catch (JMSException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * This method is called by Task at the end of each execution cycle if useJMS is set to true to send the messages in outbox to the JMS server.
	 * It retrieves the topic and body from each Message in outbox object and uses sendJMS method.
	 */
	private final void sendPrivate()
	{
		for(Message msg : outbox)
		{
			try {
				sendJMS(msg.getTopic(),msg.getMessage());
			} catch (JMSException e) {e.printStackTrace();}
		}
	}
	
	//----JMS Section - public methods----------------------------------
	
	/**
	 * This method is called by the user inside the initialize method to become a publisher in the topic specified by the TopicObject used as parameter.
	 * @param topic the topic to which the message will be sent
	 */
	public final void publish(TopicObject topic)
	{
		topicsAsPublisher.put(topic.getTopicName(), topic);
	}
	
	/**
	 * This method is called by the user inside the initialize method to become a subscriber in the topic specified by the TopicObject used as parameter.
	 * @param topic the topic to which the message will be sent
	 */
	public final void subscribe(TopicObject topic)
	{
		ArrayList<Message> inbox = new ArrayList<Message>();
		inboxes.put(topic, inbox);
		topicsAsSubscriber.put(topic.getTopicName(), topic);
	}
	
	/**
	 * This method is called by the user to send a message to the topic specified as parameter
	 * @param topic the topic from which receive the messages
	 * @param message body of the message
	 * @throws TopicNotFoundException
	 */
	public final void send(TopicObject topic, Serializable message) throws TopicNotFoundException
	{
		if(!topicsAsPublisher.containsValue(topic))
			throw new TopicNotFoundException(topic.getTopicName());
		outbox.add(new Message(topic,message));
	}
	
	/**
	 * This method is called by the user to retrieve an Iterator object which refers to the messages received in the topic used as parameter
	 * @param topic the topic from which receive the messages
	 * @return returns an Iterator<Message> that can be used to access the messages received from the last excecution to the current one
	 * @throws TopicNotFoundException 
	 */
	public final Iterator<Message> receive(TopicObject topic) throws TopicNotFoundException
	{
		if(!topicsAsSubscriber.containsValue(topic))
			throw new TopicNotFoundException(topic.getTopicName());
		return inboxes.get(topic).iterator();
	}
}