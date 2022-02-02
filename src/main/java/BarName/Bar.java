package BarName;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Frames.GeneralJFrame;

import javax.swing.JLabel;

//Display the background of the bar
public class Bar extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblBackground = new JLabel();
	private int posX, posY;
	private boolean isLeft, isShine = false;

	public Bar(boolean isLeft,int posX, int posY) {
		//Indicates if the chat will be on the left side of screen
		this.isLeft = isLeft;
		
		//Upper Left corner of chat.
		this.posX = posX;
		this.posY = posY;
		setOpaque(false);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));

		//Display components
		draw();
		add(lblBackground, BorderLayout.CENTER);
	}
	
	//Display the bar without shining frame.
	public void standardBar() {
		isShine = false;
		if(isLeft)
			lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource("/media/game/BarLeft/barleftoff.png")),
					(int)(523 * GeneralJFrame.widthProp), (int)(233 * GeneralJFrame.heightProp) ));
		else
			lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource("/media/game/BarRIGHT/barrightoff.png")),
					(int)(523 * GeneralJFrame.widthProp), (int)(233 * GeneralJFrame.heightProp) ));			

	}

	//Display the bar with shining frame - indicates that this is the user turn.
	public void shineBar() {
		isShine = true;
		if(isLeft)
			lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource("/media/game/BarLeft/barleftyourturn.png")),
					(int)(523 * GeneralJFrame.widthProp), (int)(233 * GeneralJFrame.heightProp) ));
		else
			lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource("/media/game/BarRIGHT/barrightyourturn.png")),
					(int)(523 * GeneralJFrame.widthProp), (int)(233 * GeneralJFrame.heightProp) ));			

	}
	
	//Display the background
	public void draw() {
		setBounds((int)(posX * GeneralJFrame.widthProp) , (int)(posY * GeneralJFrame.heightProp), (int)(530 * GeneralJFrame.widthProp),
				(int)(240 * GeneralJFrame.heightProp));
		if(isShine)
			shineBar();
		else
			standardBar();
	}
	
	//Scale 'icon' to the requested width, height
	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	
}
