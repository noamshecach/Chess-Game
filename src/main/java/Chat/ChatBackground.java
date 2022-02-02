package Chat;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

//Display the chat in its open state.
public class ChatBackground extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel background;
	private URL imgURL;
	
	public ChatBackground() {	
		setOpaque(false);
		background = new JLabel("");
		openChatBlack();
		add(background);
	}
	
	//Scale background image size to Chat.width, Chat.height
	public void scale(int posX, int posY) {
		setBounds(posX, posY, Chat.width, Chat.height);
		background.setIcon(scaleImage(new ImageIcon(imgURL), (int)(Chat.width ), (int)(Chat.height  )));
	}
	
	//Scale the image 'icon' to the requested width and height
	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	
	//Changes the background image to open chat with black dot - indicates that there is no new messages.
	public void openChatBlack() {
		imgURL = ChatBackground.class.getResource("/media/game/openchatblack.png");
		background.setIcon(scaleImage(new ImageIcon(imgURL), (int)(Chat.width ), (int)(Chat.height )));
		
	}
	
	//Changes the background image to open chat with green dot - indicates that there is new message.
	public void openChatGreen() {
		imgURL = ChatBackground.class.getResource("/media/game/openchatgreen.png");
		background.setIcon(scaleImage(new ImageIcon(imgURL), (int)(Chat.width ), (int)(Chat.height )));
	}

}
