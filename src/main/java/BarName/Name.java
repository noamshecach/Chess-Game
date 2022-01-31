package BarName;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Frames.GeneralJFrame;

import javax.swing.JLabel;

public class Name extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblBackground;
	private boolean isLeft;
	private int posX, posY;

	public Name(boolean isLeft, String name,int posX, int posY) {
		this.isLeft = isLeft;
		this.posX = posX;
		this.posY = posY;
		this.lblBackground = new JLabel(name);
		setOpaque(false);
		draw();
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		lblBackground.setHorizontalTextPosition(JLabel.CENTER);
		lblBackground.setVerticalTextPosition(JLabel.CENTER);
		add(lblBackground, BorderLayout.CENTER);
	}

	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	
	public void draw() {
		if(isLeft)
			setBounds((int)((posX + 52) * GeneralJFrame.widthProp) ,(int)((posY + 130) * GeneralJFrame.heightProp), (int)(160 * GeneralJFrame.widthProp),
					(int)(60 * GeneralJFrame.heightProp));
		else
			setBounds((int)((posX + 307)* GeneralJFrame.widthProp) ,(int)((posY + 130) * GeneralJFrame.heightProp), (int)(160 * GeneralJFrame.widthProp),
					(int)(60 * GeneralJFrame.heightProp));
		lblBackground.setIcon(scaleImage(new ImageIcon(Bar.class.getResource("/media/game/BarLeft/barnameCropped.png")),
				(int)(148 * GeneralJFrame.widthProp), (int)(48 * GeneralJFrame.heightProp) ));
	}
	
}
