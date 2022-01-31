package Frames;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import BarName.BarName;
import Client.ContactServer;
import SentObjects.InitialSetup;
import SystemMessage.Message;
import Timer.Timer;

public class Tables extends GeneralJFrame  {

	private static final long serialVersionUID = 1L;
	private JLabel exit, highScore, originalPlayerImage,
			leftArrow, rightArrow;
	private Timer collectCoins;
	private BarName barName;
	private JLabel[] tables;
	private ImageIcon[] tableImgs;
	
	private MouseHandler mouseHandler;
	private ImageIcon highScoreImg = new ImageIcon(getClass().getResource( "/media/tables/highscore.png"));
	private ImageIcon collectCoinsImg = new ImageIcon(getClass().getResource( "/media/tables/buttonCollectYourBonusREK.png"));
	private ImageIcon rightArrowImg = new ImageIcon(getClass().getResource( "/media/tables/rightArrow.png"));
	private ImageIcon leftArrowImg = new ImageIcon(getClass().getResource( "/media/tables/leftArrow.png"));
	private int screen = 0;
	private String username;
	
	public Tables(ContactServer contactServer, String username) {
		
		super("/media/tables/background.png");
		this.mouseHandler = new MouseHandler();
		this.username = username;
	    this.contactServer = contactServer;
	    initialize();
	    	    
	    int[] locations = {660,934,1208,1480};
	    for(int i = 0; i < tables.length; i++) {
	    	tables[i] = new JLabel();
	    	addLabel(tables[i], locations[i], 286, tableImgs[i],mouseHandler);
	    }
		
		addLabel(leftArrow, 555, 599, leftArrowImg,mouseHandler);
		addLabel(rightArrow, 1754, 599, rightArrowImg,mouseHandler);
		addLabel(exit, 1517, 969, exitImg,mouseHandler);
	    addLabel(highScore, 1318, 969, highScoreImg,mouseHandler);	    	    
	    addLabel(collectCoins, 850, 953, collectCoinsImg, mouseHandler);    
	    addBarName(barName, 0, 0);
	    
	    getPlayerImage();
		setVisible(true);
	}
	
	public void initialize() {
		exit = new JLabel();
		highScore = new JLabel();
		originalPlayerImage = new JLabel();
		leftArrow = new JLabel();
		rightArrow = new JLabel();
		collectCoins = new Timer(contactServer.lastFreeCoinsPickup());
		barName = new BarName(true, contactServer.getUser(),0,0, contactServer, this, getLayeredPane());		
		tables = new JLabel[4]; 
		tableImgs = new ImageIcon[10];
		
	    for(int i = 0; i < tableImgs.length; i++) 
	    	tableImgs[i] = new ImageIcon(getClass().getResource("/media/tables/table"+ (i + 1) +"00.png"));
	}
	
	public void getPlayerImage() {
		String s = contactServer.getClientImage();
		ImageIcon playerImageImg = new ImageIcon(getClass().getResource(s));
	    originalPlayerImage = new JLabel();
	    originalPlayerImage.setBounds((int)Math.floor(370 * widthProp), (int)Math.floor(222 * heightProp), (int)(playerImageImg.getIconWidth() * 1.22 * widthProp), (int)(playerImageImg.getIconHeight() * 1.22 * heightProp));
	    originalPlayerImage.setIcon(scaleImage(playerImageImg, (int)(playerImageImg.getIconWidth() * 1.22 * widthProp), (int)(playerImageImg.getIconHeight() * 1.22 * heightProp)));
	}
	
	public void refreshWindow() {
		barName.changeAmount("" + contactServer.getCoinsAmount());
		barName.paintImmediately(barName.getVisibleRect());
	}
	
	private class MouseHandler extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(exit == arg0.getSource()) {
				if(contactServer.logout(username))
					System.exit(0);
				else
					System.out.println("Error in logout");
			}
			if(highScore == arg0.getSource()) {
				HighScore highscore = new HighScore(Tables.this, contactServer, username);
				setVisible(false);
			}	
			if(collectCoins == arg0.getSource()) {
				if(collectCoins.isTimeOver()) {
					contactServer.addToCoinsAmount(400);
					barName.changeAmount("" + contactServer.getCoinsAmount());
					barName.revalidate();
					barName.repaint();
					long date = contactServer.updateFreeCoinsPickUp();
					collectCoins.startOver(date);
				}
			}
			for(int i = 0; i < tables.length; i++) {
				if(tables[i] == arg0.getSource() ) {
					if(contactServer.isEnoughCoins(username,(i + 1 + (4 * screen)) * 100  )) {
						final WaitingForOponnent wfo = new WaitingForOponnent(originalPlayerImage, contactServer, i + 4 * screen, username);
						final int tableIdx = i + 4 * screen;
						setVisible(false);
						Thread t = new Thread() {
							public void run() {
								InitialSetup init = contactServer.findOpponent(username, tableIdx);
								wfo.setOpponentImage(init.getOpponentImage());
								new GraphicBoard(contactServer, username, init);
								dispose();
							}
						};
						t.start();

					}else {
						Message.displayWarning("\nYou don't have enough coins for this table.");
					}
				}
			}

			if(rightArrow == arg0.getSource()) {
				if(screen == 0 || screen == 1)
					screen++;
				if(screen == 1 || screen == 2) 
					displayTables();
			}
			if(leftArrow == arg0.getSource()) {
				if(screen == 2 || screen == 1)
					screen--;
				if(screen == 0 || screen == 1) 
					displayTables();
			}
		}
		
		private void displayTables() {
			for(int i = 0; i < tables.length; i++) {
				if(i + (4 * screen) < 10) {
					tables[i].setIcon(scaleImage(tableImgs[i + (4 * screen)], tables[i].getWidth(), tables[i].getHeight()));
					tables[i].setVisible(true);
				}else
					tables[i].setVisible(false);
			}
		}
	}
}
