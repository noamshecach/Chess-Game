package BarName;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Frames.GeneralJFrame;

import javax.swing.JLabel;

public class Bar extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblBackground = new JLabel();
	private int posX, posY;
	private boolean isLeft, isShine = false;

	public Bar(boolean isLeft,int posX, int posY) {
		this.isLeft = isLeft;
		this.posX = posX;
		this.posY = posY;
		setOpaque(false);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));

		draw();
		add(lblBackground, BorderLayout.CENTER);
	}
	
	public void standardBar() {
		isShine = false;
		if(isLeft)
			lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource("/media/game/BarLeft/barleftoff.png")),
					(int)(523 * GeneralJFrame.widthProp), (int)(233 * GeneralJFrame.heightProp) ));
		else
			lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource("/media/game/BarRIGHT/barrightoff.png")),
					(int)(523 * GeneralJFrame.widthProp), (int)(233 * GeneralJFrame.heightProp) ));			

	}

	public void shineBar() {
		isShine = true;
		if(isLeft)
			lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource("/media/game/BarLeft/barleftyourturn.png")),
					(int)(523 * GeneralJFrame.widthProp), (int)(233 * GeneralJFrame.heightProp) ));
		else
			lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource("/media/game/BarRIGHT/barrightyourturn.png")),
					(int)(523 * GeneralJFrame.widthProp), (int)(233 * GeneralJFrame.heightProp) ));			

	}
	
	public void draw() {
		setBounds((int)(posX * GeneralJFrame.widthProp) , (int)(posY * GeneralJFrame.heightProp), (int)(530 * GeneralJFrame.widthProp),
				(int)(240 * GeneralJFrame.heightProp));
		if(isShine)
			shineBar();
		else
			standardBar();
	}
	
	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	
}
