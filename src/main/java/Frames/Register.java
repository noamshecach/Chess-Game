package Frames;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import Client.ContactServer;
import SystemMessage.Message;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Register extends GeneralJFrame {

	private static final long serialVersionUID = 1L;
	private MouseHandler mouseHandler = new MouseHandler();
	private JLabel browse, chooseCharacter, signup;
	private JTextField firstname, surname, username, email,avatar;
	private JPasswordField password;
	private Login login;
	private ContactServer contactServer;
	
	private ImageIcon browseImg = new ImageIcon(getClass().getResource("/media/register/browse.png"));
	private ImageIcon chooseCharacterImg = new ImageIcon(getClass().getResource( "/media/register/choosecharacter.png"));
	private ImageIcon reg_signupImg = new ImageIcon(getClass().getResource( "/media/register/signup.png"));

	
	public Register(Login login, ContactServer contactServer) {
		super("/media/register/background.png");
		
		this.login = login;
    	initialize();
    	avatar.setEditable(false);
    	
	    addLabel(browse, 1146, 842, browseImg,mouseHandler);
	    addLabel(chooseCharacter, 584, 946, chooseCharacterImg,mouseHandler);
	    addLabel(signup, 1146, 946, reg_signupImg,mouseHandler);
	    addTextField(firstname, "", mouseHandler, 784, 334, 307, 65);
	    addTextField(surname, "", mouseHandler, 784, 440, 307, 65);
	    addTextField(username, "", mouseHandler, 784, 543, 307, 65);
	    addPasswordField(password, mouseHandler, 784, 649, 307, 65);
	    addTextField(email, "", mouseHandler, 784, 753, 307, 65);
	    addTextField(avatar, "", mouseHandler, 784, 859, 307, 65);
	    
	    setVisible(true);
		this.contactServer = contactServer;
	}
	
	private void initialize() {
		browse = new JLabel();
		chooseCharacter = new JLabel();
		signup = new JLabel();
		firstname = new JTextField();
		surname = new JTextField();
		username = new JTextField();
		password = new JPasswordField();
		email = new JTextField();
		avatar = new JTextField();
	}

	private class MouseHandler extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(username.getText() != null && !arg0.getSource().equals(username))
			{
				if(contactServer.isUsernameExists(username.getText()))
					Message.displayWarning("\nusername  already  exists!");
			}
			if(signup == arg0.getSource()) {
				if(!contactServer.isUsernameExists(username.getText())) {
					contactServer.register(firstname.getText(), surname.getText(), username.getText(),
							password.getPassword().toString(), email.getText(), avatar.getText());
					setVisible(false);
					login.setVisible(true);
				}
				else
					Message.displayWarning("\nusername  already  exists!");
			}
			if(browse == arg0.getSource()) {
		        JFileChooser chooser = new JFileChooser();
		        FileNameExtensionFilter filter = new FileNameExtensionFilter(
		                "JPG & GIF Images", "jpg", "gif");
		        chooser.setFileFilter(filter);
		        int returnVal = chooser.showOpenDialog(null);
		        if(returnVal == JFileChooser.APPROVE_OPTION) {
		        	avatar.setText(chooser.getSelectedFile().getPath());
		        }//else
		        	//JOptionPane.showMessageDialog(null, "The extension is not supported. Choose another file.");
			}
			if(chooseCharacter == arg0.getSource()) {
				setVisible(false);
				ChooseCharacter character = new ChooseCharacter(Register.this);	
			}
		}
	}
	
	public void setAvatar(String path) {
		this.avatar.setText(path);
	}
}
