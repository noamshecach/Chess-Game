package Chat;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Frames.GeneralJFrame;
import net.miginfocom.swing.MigLayout;

public class ChatMinimized extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel background;
	private URL imgURL;
	private int bgHeight = (int)(65 * Chat.compProp), originalBgHeight= (int)(65 * Chat.compProp);
	
	public ChatMinimized() {
		setOpaque(false);
		setLayout(new MigLayout("", "0lp![grow]0lp!", "0lp![88%,grow]0lp![12%,grow]0lp!"));
		background = new JLabel("");
		closedChatBlack();
		add(background, "cell 0 1, grow");
	}
	
	public void scale(int posX, int posY) {
		setBounds(posX, posY, Chat.width, Chat.height);
		background.setIcon(scaleImage(new ImageIcon(imgURL), Chat.width, bgHeight));
	}
	
	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	
	public void closedChatBlack() {
		bgHeight = (int)(originalBgHeight * GeneralJFrame.heightProp);
		imgURL = ChatBackground.class.getResource("/media/game/closechatblack.png");
		background.setIcon(scaleImage(new ImageIcon(imgURL), Chat.width, bgHeight));
	}
	
	public void closedChatGreen() {
		bgHeight = (int)(originalBgHeight * GeneralJFrame.heightProp);
		imgURL = ChatBackground.class.getResource("/media/game/closechatgreen.png");
		background.setIcon(scaleImage(new ImageIcon(imgURL), Chat.width , bgHeight));
	}

}
