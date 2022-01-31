package Chat;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

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
	
	public void scale(int posX, int posY) {
		setBounds(posX, posY, Chat.width, Chat.height);
		background.setIcon(scaleImage(new ImageIcon(imgURL), (int)(Chat.width ), (int)(Chat.height  )));
	}
	
	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	
	public void openChatBlack() {
		imgURL = ChatBackground.class.getResource("/media/game/openchatblack.png");
		background.setIcon(scaleImage(new ImageIcon(imgURL), (int)(Chat.width ), (int)(Chat.height )));
		
	}
	
	public void openChatGreen() {
		imgURL = ChatBackground.class.getResource("/media/game/openchatgreen.png");
		background.setIcon(scaleImage(new ImageIcon(imgURL), (int)(Chat.width ), (int)(Chat.height )));
	}

}
