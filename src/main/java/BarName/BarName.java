package BarName;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.noam.jpa_project.Server.UserAccount;
import Client.ContactServer;
import Frames.GeneralJFrame;
import javax.swing.JLayeredPane;

//Display bar component on screen
public class BarName extends JPanel {

	private static final long serialVersionUID = 1L;
	private Bar bar;
	private Amount amount;
	private Name name;
	private Picture pic;
	private int posX, posY;
	
	//Constructor for Machine opponent
	public BarName(boolean isLeft, int posX, int posY, ContactServer contactServer, GeneralJFrame previousFrame, JLayeredPane lp) {
		//Upper Left corner.
		this.posX = posX;
		this.posY = posY;
		String username = "Computer";
		String picture = "/media/playersImage/01.png";
		
		setOpaque(false);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//bar's background
		bar = new Bar(isLeft, posX, posY);
		
		//user name tag
		name = new Name(isLeft, username, posX, posY);
		
		//user name image
		pic = new Picture(isLeft, picture, posX, posY, contactServer,previousFrame, "Computer");
		
		//user name coins label
		String useramount = "99999$";
		amount = new Amount(isLeft, useramount, posX, posY);
		
		setBounds((int)(posX * GeneralJFrame.widthProp) , (int)(posY * GeneralJFrame.heightProp), (int)(530 * GeneralJFrame.widthProp),
				(int)(240 * GeneralJFrame.heightProp));
		
		lp.add(bar, new Integer(10));
		lp.add(pic, new Integer(11));
		lp.add(amount, new Integer(11));
		lp.add(name, new Integer(12));
	}
	
	//Constructor for human opponent
	public BarName(boolean isLeft, UserAccount user, int posX, int posY, ContactServer contactServer, GeneralJFrame previousFrame, JLayeredPane lp) {
		//upper left corner
		this.posX = posX;
		this.posY = posY;
		String username = user.getUserName();
		String picture = user.getPicture();
		
		setOpaque(false);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		bar = new Bar(isLeft, posX, posY);
		name = new Name(isLeft, username, posX, posY);
		pic = new Picture(isLeft, picture, posX, posY, contactServer,previousFrame, username);
		
		String useramount = "" + user.getNumberOfCoins();
		amount = new Amount(isLeft, useramount, posX, posY);
		
		setBounds((int)(posX * GeneralJFrame.widthProp) , (int)(posY * GeneralJFrame.heightProp), (int)(530 * GeneralJFrame.widthProp),
				(int)(240 * GeneralJFrame.heightProp));
		
		lp.add(bar, new Integer(10));
		lp.add(pic, new Integer(11));
		lp.add(amount, new Integer(11));
		lp.add(name, new Integer(12));
	}
	
	public void setStandardBar() {
		bar.standardBar();
	}
	
	public void setShineBar() {
		bar.shineBar();
	}
	
	//Change amount label
	public void changeAmount(String amountValue) {
		amount.changeAmount(amountValue);
		amount.paintImmediately(amount.getVisibleRect());
		this.revalidate();
		this.repaint();
	}
	
	//Display bar
	public void draw() {
		setBounds((int)(posX * GeneralJFrame.widthProp) , (int)(posY * GeneralJFrame.heightProp), (int)(530 * GeneralJFrame.widthProp),
				(int)(240 * GeneralJFrame.heightProp));
		amount.draw();
		bar.draw();
		name.draw();
		pic.draw();
	}
	

}

