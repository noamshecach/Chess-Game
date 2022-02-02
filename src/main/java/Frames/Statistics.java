package Frames;

import javax.swing.JLabel;
import com.noam.jpa_project.Server.UserAccount;
import BarName.BarName;
import Client.ContactServer;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Statistics extends GeneralJFrame {

	private static final long serialVersionUID = 1L;
	private MouseHandler mouseHandler = new MouseHandler();
	private JLabel exit, back, games, draws, loses, coins, wins;
	private BarName barleft;
	
	private GeneralJFrame previousFrame;
	private ContactServer contactServer;
	private String username;
	
	public Statistics(ContactServer contactServer, GeneralJFrame previousFrame, String username) {
		// Calling parent class, set background image
		super("/media/statistics/background.png");
		
		this.username = username;
		this.contactServer = contactServer;
		this.previousFrame = previousFrame;
	    UserAccount user = contactServer.getUser();
	    
	    //Initialize graphical components.
	    initializeComponents();
	    
	    //Add components to the screen
	    addLabel(exit, 766, 946,199,97 ,null,mouseHandler);
	    addLabel(back, 970, 946,199,97 ,null,mouseHandler);
	    addCustomLabel(games,451,715, user.getNumberOfGames()+ "");
	    addCustomLabel(wins,701,715 , user.getNumberOfWins()+ "");
	    addCustomLabel(draws,949,715 , user.getNumberOfDraws()+ "");
	    addCustomLabel(loses,1195,715 , user.getNumberOfLoses()+ "");
	    addCustomLabel(coins,1435,715 , user.getNumberOfCoins() + "");
	    addBarName(barleft, 710, 330); 
	    
	    this.setVisible(true);
	}
	
	//Initialize graphical components.
	private void initializeComponents() {
		exit = new JLabel();
		back = new JLabel();
		games = new JLabel();
		wins = new JLabel();
		draws = new JLabel();
		loses = new JLabel();
		coins = new JLabel();
		barleft = new BarName(true, contactServer.getUser(),710,330, contactServer, this, getLayeredPane());
	}
	
	//Adds label to the screen
	private void addCustomLabel(JLabel label, int posX, int posY, String text) {
		addLabel(label, posX, posY,98,98 ,null,null);
		label.setText(text + "");
		label.setFont(new Font("bebas",Font.PLAIN,16));
	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			//EXIT
			if(exit == arg0.getSource()) {
				if(contactServer.logout(username))
					System.exit(0);
				else
					System.out.println("Error in logout");
			}
			//BACK
			if(back == arg0.getSource()) {
				previousFrame.setVisible(true);
				setVisible(false);
			}
		}
	}

}
