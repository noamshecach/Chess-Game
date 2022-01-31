package JMS;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import Chat.Chat;

public class JmsReceiver {

	private ConnectionFactory connectionFactory;
	private Connection connection;;
	private MessageConsumer messageConsumer;
	private Session session;
	private Chat chat;
	
	public JmsReceiver(Chat chat, String queueName) {
		this.chat = chat;
		getMessage(queueName);
	}
	
	private void getMessage(String queueName) {
		try {
		    System.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
		    System.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
		    
		    InitialContext ic = new InitialContext();
		    connectionFactory = (ConnectionFactory) ic.lookup ("jms/mydomain");
			
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destPTP = (Destination) ic.lookup(queueName); 
			messageConsumer = session.createConsumer(destPTP);
			messageConsumer.setMessageListener(chat);
			connection.start();
		}
		catch(JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public void endSession() {
		try {
			messageConsumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) { e.printStackTrace(); }
	}
	
}
