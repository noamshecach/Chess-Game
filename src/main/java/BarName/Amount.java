package BarName;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Frames.GeneralJFrame;
import javax.swing.JLabel;

public class Amount extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblBackground;
	private int posX, posY;
	private String amount;
	private boolean isLeft;

	public Amount(boolean isLeft, String amount,int posX, int posY) {
		setOpaque(false);
		this.posX = posX;
		this.posY = posY;
		this.amount = amount;
		this.isLeft = isLeft;
		draw();
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		
		Font f = new Font("bebas",Font.PLAIN,26);
		lblBackground.setFont(f);
		lblBackground.setHorizontalTextPosition(JLabel.CENTER);
		lblBackground.setVerticalTextPosition(JLabel.CENTER);
		add(lblBackground, BorderLayout.CENTER);
	}
	
	public void changeAmount(String amount) {
		this.amount = amount;
		lblBackground.setText(amount + " $");
		lblBackground.paintImmediately(lblBackground.getVisibleRect());
		this.revalidate();
		this.repaint();
	}
	
	public void draw() {
		if(isLeft) {
			setBounds((int)((posX + 270) * GeneralJFrame.widthProp) , (int)((posY + 75) * GeneralJFrame.heightProp), (int)(300 * GeneralJFrame.widthProp),
					(int)(60 * GeneralJFrame.heightProp));
			 lblBackground = new JLabel(amount + " $");
		}else {
			setBounds((int)((posX + 160) * GeneralJFrame.widthProp) , (int)((posY + 75) * GeneralJFrame.heightProp), (int)(300 * GeneralJFrame.widthProp),
					(int)(60 * GeneralJFrame.heightProp));
			 lblBackground = new JLabel(" $" + amount);
		}
	}

	
}
