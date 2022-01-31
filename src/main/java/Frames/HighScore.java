package Frames;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import com.noam.jpa_project.Server.UserAccount;

import Client.ContactServer;

public class HighScore extends GeneralJFrame {

	private static final long serialVersionUID = 1L;
	private MouseHandler mouseHandler = new MouseHandler();
	private JLabel exit, back, leftArrow, rightArrow;
	private JLabel[][] highScoreJlabel;
	private ImageIcon rightArrowImg = new ImageIcon(getClass().getResource( "/media/highscore/rightArrow.png"));
	private ImageIcon leftArrowImg = new ImageIcon(getClass().getResource( "/media/highscore/leftArrow.png"));
	private Tables tables;
	private int page = 0;
	private List<UserAccount> highScoreDB;
	private ContactServer contactServer;
	private String username;
	private boolean firstTime = true;
	
	public HighScore(Tables tabels, ContactServer contactServer, String username) {
		
		super("/media/highscore/background.png");
		this.username = username;
		this.contactServer = contactServer;
		this.tables = tabels;		
		initalize();
	    
	    addLabel(exit, 766, 946, 191,97,null,mouseHandler);
	    addLabel(back, 970, 946, 191,97,null,mouseHandler);
		addLabel(leftArrow, 241, 846, leftArrowImg,mouseHandler);
		addLabel(rightArrow, 1579 , 846, rightArrowImg,mouseHandler);
	    
	    highScoreDB = contactServer.getHighScoreFromDB();
	    initiateLabelsArray();
	    displayOnScreen();
	    setVisible(true);
	}
	
	private void initalize() {
	    exit = new JLabel();
	    back = new JLabel();
		leftArrow = new JLabel();
		rightArrow = new JLabel();
		highScoreJlabel = new JLabel[5][4];
	}
	
	private void initiateLabelsArray() {
		for(int cols = 0; cols < highScoreJlabel.length ; cols++ ) 
			for(int row = 0; row < highScoreJlabel[0].length; row++)
				highScoreJlabel[cols][row] = new JLabel();
	}
	
	private void displayOnScreen() {
		Font f = new Font("bebas",Font.PLAIN,16);
		UserAccount user;
		int cols;
		for(cols = 0; cols < highScoreJlabel.length 
				&& highScoreDB.size() > cols + page * 5 ; cols++ ) {
			user = highScoreDB.get(cols + page * 5);
			
			componentsArray.add(new MyComponent(highScoreJlabel[cols][0], null,445, 330 + cols * 122, 98, 98));
			changeLabelSize(highScoreJlabel[cols][0], null ,445, 330 + cols * 122, 98, 98);
			highScoreJlabel[cols][0].setText(cols + 1 + "");
			highScoreJlabel[cols][0].setFont(f);
			highScoreJlabel[cols][0].setVisible(true);
			
			ImageIcon img = new ImageIcon(getClass().getResource(user.getPicture()));
			componentsArray.add(new MyComponent(highScoreJlabel[cols][1], img,578, 330 + cols * 122,98,98));
			highScoreJlabel[cols][1].setIcon(img);
			highScoreJlabel[cols][1].setVisible(true);
			changeLabelSize(highScoreJlabel[cols][1], img ,578, 330 + cols * 122,98,98);

			componentsArray.add(new MyComponent(highScoreJlabel[cols][2], null,743, 330 + cols * 122, 250, 98));
			changeLabelSize(highScoreJlabel[cols][2], null ,743, 330 + cols * 122, 250, 98);
			highScoreJlabel[cols][2].setText(user.getUserName());
			highScoreJlabel[cols][2].setFont(f);
			highScoreJlabel[cols][2].setVisible(true);
		    
			componentsArray.add(new MyComponent(highScoreJlabel[cols][3], null,1230, 330 + cols * 122, 250, 98));
			changeLabelSize(highScoreJlabel[cols][3], null ,1230, 330 + cols * 122, 250, 98);
			highScoreJlabel[cols][3].setText(user.getNumberOfWins() + "");
			highScoreJlabel[cols][3].setFont(f);
			highScoreJlabel[cols][3].setVisible(true);
			for(int i = 0; i < 4 && firstTime; i++)
				getLayeredPane().add(highScoreJlabel[cols][i], new Integer(3));
		}
		firstTime = false;
		for(; cols < 5 * (page + 1); cols++)
			for(int i =0; i< 4; i++)
				highScoreJlabel[cols][i].setVisible(false);
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
			if(back == arg0.getSource()) {
				tables.setVisible(true);
				setVisible(false);
			}
			if(rightArrow == arg0.getSource()) {
				if(highScoreDB.size() > 5 + (page * 5)) {
					page++;
					displayOnScreen();
				}
			}
			if(leftArrow == arg0.getSource()) {
				if(page > 0) {
					page--;
					displayOnScreen();
				}
			}
			System.out.println("x: " + arg0.getXOnScreen() + "y: " + arg0.getYOnScreen());
		
		}
	}
	
}
