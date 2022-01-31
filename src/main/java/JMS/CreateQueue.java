package JMS;

import java.io.IOException;


public class CreateQueue {

	private String jmsStart = "jms/";
	
	public CreateQueue(String[] usernames) {
		createQueue(usernames[0]);
		createQueue(usernames[1]);
	}
	
	public void createQueue(String name) {
		
		try {
			Runtime rt =  Runtime.getRuntime();
			rt.exec( "cmd /c  c:\\glassfish\\glassfish5\\bin\\asadmin.bat create-jms-resource --restype javax.jms.Queue " +
			jmsStart + name);
		} 
		catch (IOException e) { e.printStackTrace(); }
	}
}
