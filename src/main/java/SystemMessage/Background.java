package SystemMessage;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Frames.GeneralJFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Background extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Background(String img) {
		setOpaque(false);
		setBounds((int)(100 * GeneralJFrame.widthProp) , (int)(100 * GeneralJFrame.heightProp), (int)(840 * GeneralJFrame.widthProp),
				(int)(450 * GeneralJFrame.heightProp));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblBackground = new JLabel("background");
		
		System.out.println(Background.class.getResource(img));
		lblBackground.setIcon(scaleImage(new ImageIcon(Background.class.getResource(img)),
				(int)(822 * GeneralJFrame.widthProp), (int)(432 * GeneralJFrame.heightProp) ));
		add(lblBackground, BorderLayout.CENTER);

	}
	
	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    System.out.println(width + " " + height);
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}

}
