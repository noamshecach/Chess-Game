package Chat;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Frames.GeneralJFrame;
import net.miginfocom.swing.MigLayout;

//This class dispalys the chat closed - with black dot or green dot.
public class ChatMinimized extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel background;
	private URL imgURL;
	private int bgHeight = (int)(65 * Chat.compProp), originalBgHeight= (int)(65 * Chat.compProp);
	
	//Default representation - closed with black dot.
	public ChatMinimized() {
		setOpaque(false);
		setLayout(new MigLayout("", "0lp![grow]0lp!", "0lp![88%,grow]0lp![12%,grow]0lp!"));
		background = new JLabel("");
		closedChatBlack();
		add(background, "cell 0 1, grow");
	}
	
	//Scale background image size to Chat.width, bgHeight.
	public void scale(int posX, int posY) {
		setBounds(posX, posY, Chat.width, Chat.height);
		background.setIcon(scaleImage(new ImageIcon(imgURL), Chat.width, bgHeight));
	}
	
	//Scales the image 'icon' to the specified width and height
	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	
	//Changing background image to closed chat image, with black dot that indicates that no messages are waiting.
	public void closedChatBlack() {
		bgHeight = (int)(originalBgHeight * GeneralJFrame.heightProp);
		imgURL = ChatBackground.class.getResource("/media/game/closechatblack.png");
		background.setIcon(scaleImage(new ImageIcon(imgURL), Chat.width, bgHeight));
	}
	
	//Changing background image to closed chat image, with green dot that indicates that there is new message.
	public void closedChatGreen() {
		bgHeight = (int)(originalBgHeight * GeneralJFrame.heightProp);
		imgURL = ChatBackground.class.getResource("/media/game/closechatgreen.png");
		background.setIcon(scaleImage(new ImageIcon(imgURL), Chat.width , bgHeight));
	}

}
