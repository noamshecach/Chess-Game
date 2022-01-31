package JMS;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsSender {

	private ConnectionFactory connectionFactory;
	private MessageProducer messageProducer;
	private Connection connection;
	private Session session;
	private TextMessage textMessage;
	
	public JmsSender(String queueName) {
		createSession(queueName);
	}
	
	private void createSession(String queueName) {
		
		try
		{
		    System.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
		    System.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
			
			InitialContext ic = new InitialContext();
			connectionFactory = (ConnectionFactory) ic.lookup ("jms/mydomain");
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("queue:" + queueName);
			Destination destPTP = (Destination) ic.lookup(queueName); 
	
			messageProducer = session.createProducer(destPTP);
			textMessage = session.createTextMessage();
		}
		catch(JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) { e.printStackTrace(); }
	}
	
	public void send(String message) {
		try {
			textMessage.setText(message);
			System.out.println("Sending the following message:" + message);
			messageProducer.send(textMessage);
		} catch (JMSException e) { e.printStackTrace(); }
	}
	
	public void endSession() {
		try {
			messageProducer.close();
			session.close();
			connection.close();
		} catch (JMSException e) { e.printStackTrace(); }
	}
	
}
