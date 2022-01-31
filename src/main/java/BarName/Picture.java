package BarName;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Client.ContactServer;
import Frames.GeneralJFrame;
import Frames.Statistics;
import javax.swing.JLabel;

public class Picture extends JPanel {

	private static final long serialVersionUID = 1L;
	private ContactServer contactServer;
	private GeneralJFrame previousFrame;
	private JLabel lblBackground;
	private int posX, posY;
	private boolean isLeft;
	private String picture;
	private String username;

	public Picture(boolean isLeft, String picture,int posX, int posY, ContactServer contactServer, GeneralJFrame previousFrame, String username) {
		this.username = username;
		this.lblBackground = new JLabel();
		this.isLeft = isLeft;
		this.posX = posX;
		this.posY = posY;
		this.picture = picture;
		this.contactServer = contactServer;
		this.previousFrame = previousFrame;
		setOpaque(false);
		draw();
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		
		MouseHandler mouseHandler = new MouseHandler();
		lblBackground.addMouseListener(mouseHandler); 
		add(lblBackground, BorderLayout.CENTER);
	}
	
	public void draw() {
		if(isLeft)
			setBounds((int)((posX + 70) * GeneralJFrame.widthProp) , (int)((posY + 30 ) * GeneralJFrame.heightProp), (int)(322 * 0.4 * GeneralJFrame.widthProp),
					(int)(321 * 0.4 * GeneralJFrame.heightProp));
		else
			setBounds((int)((posX + 315) * GeneralJFrame.widthProp) , (int)((posY + 30 ) * GeneralJFrame.heightProp), (int)(330 * 0.4 * GeneralJFrame.widthProp),
					(int)(330 * 0.4 * GeneralJFrame.heightProp));
		lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource(picture)),
				(int)(322 * 0.4 * GeneralJFrame.widthProp), (int)(321 * 0.4 * GeneralJFrame.heightProp) ));
	}

	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	
	private class MouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			Statistics  s = new Statistics(contactServer, previousFrame, username);
			previousFrame.setVisible(false);
		
		}
	}
	
}
