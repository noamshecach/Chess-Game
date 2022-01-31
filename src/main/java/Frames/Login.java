package Frames;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import Client.ContactServer;
import SystemMessage.Message;

// View component - displays login screen.
public class Login extends GeneralJFrame {
		
	private static final long serialVersionUID = 1L;
	private JLabel exit, lblLogin, signup;
	private JTextField username;
	private JPasswordField password;
	private MouseHandler mouseHandler;
	private ImageIcon loginImg = new ImageIcon(getClass().getResource("/media/login/login.png"));
	private ImageIcon signupImg = new ImageIcon(getClass().getResource("/media/login/signup.png"));
	private ContactServer contactServer;
	
	public Login() {	
		
		// Calling parent class, set background image
		super("/media/login/openScreen.png");
		
		// Loads the font of this game
	    loadFont();
	    
	    //Define mouse handler
		this.mouseHandler = new MouseHandler(this);
		
		// Initialize graphic components - labels, buttons, etc.
		initialize();
		
		// Adding graphic components to the screen
	    addLabel(exit, 666, 865, exitImg,mouseHandler);
	    addLabel(lblLogin, 865, 865, loginImg,mouseHandler);
	    addLabel(signup, 1065, 865, signupImg,mouseHandler);
	    addTextFields();
		setVisible(true);
		
		// Connecting to server
		this.contactServer = new ContactServer();
	}
	
	// Initialize graphic components - labels, buttons, etc.
	private void initialize() {
		exit = new JLabel();
		lblLogin = new JLabel();
		signup = new JLabel();
		password = new JPasswordField();
		username = new JTextField();
		
	}
	
	//Adding text fields for user name and password
	private void addTextFields() {	 
	    addTextField(username, "Write your username", mouseHandler, 886, 664, 307, 65);
		username.setFocusTraversalKeysEnabled(false);
		username.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent event) {
		       if (event.getKeyCode() == 9) {
		    	   password.setEchoChar('*');
		    	   password.setText("");
		    	   password.requestFocus();
		        }
			}

			@Override
			public void keyReleased(KeyEvent arg0) {  }

			@Override
			public void keyTyped(KeyEvent arg0) {  }
	    	
	    });

	    addPasswordField(password, mouseHandler,886, 770, 307, 65);
	}
	
	
	// Handling click events
	private class MouseHandler extends MouseAdapter {
		private Login login;
		public MouseHandler(Login login) {
			this.login = login;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			//EXIT button
			if(exit == arg0.getSource()) {
				System.exit(0);
			}
			
			//LOGIN button
			if(lblLogin == arg0.getSource()) {
				if(contactServer.login(username.getText(), new String(password.getPassword()))) {
					new Tables(contactServer, username.getText());
					login.dispose();
				}
				else 
					Message.displayWarning("\nWrong  user  name  or  password."+" \n\n" +"Please  Try  again.");
			}
			
			//SIGNUP button
			if(signup == arg0.getSource()) {
				new Register(Login.this,contactServer);
				setVisible(false);
			}
			
			//USERNAME text field
			if(username == arg0.getSource()) {
				username.setText("");
			}
			
			//PASSWORD text field
			if(password == arg0.getSource()) {
				password.setText("");
				password.setEchoChar('*');
			}
		}
	}
	
}
