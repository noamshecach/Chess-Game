package Frames;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import com.noam.jpa_project.Server.HighScoreData;

import Client.ContactServer;

//View component - displays high score table screen.
public class HighScore extends GeneralJFrame {

	private static final long serialVersionUID = 1L;
	private JLabel exit, back, leftArrow, rightArrow;
	private JLabel[][] highScoreJlabel;
	private Tables tables;
	private int page = 0;
	private List<HighScoreData> highScoreDB;
	private String username;
	private boolean isTableDisplayed = false;
	
	public HighScore(Tables tabels, ContactServer contactServer, String username) {
		
		// Calling parent class, set background image
		super("/media/highscore/background.png");
		
		//Initialize arguments
		this.username = username;
		this.contactServer = contactServer;
		this.tables = tabels;
		
		// Initialize graphic components - labels, buttons, etc.
		initalizeComponents();
	    
		ImageIcon rightArrowImg = new ImageIcon(getClass().getResource( "/media/highscore/rightArrow.png"));
		ImageIcon leftArrowImg = new ImageIcon(getClass().getResource( "/media/highscore/leftArrow.png"));
		
		//Adding labels with mouse events
		MouseHandler mouseHandler = new MouseHandler();
	    addLabel(exit, 766, 946, 191,97,null,mouseHandler);
	    addLabel(back, 970, 946, 191,97,null,mouseHandler);
		addLabel(leftArrow, 241, 846, leftArrowImg,mouseHandler);
		addLabel(rightArrow, 1579 , 846, rightArrowImg,mouseHandler);
	    
		//Reading data from database
	    highScoreDB = contactServer.getHighScoreFromDB();
	    
		//Display high score table
	    initiateLabelsArray();
	    displayOnScreen();
	    setVisible(true);
	}
	
	// Initialize graphic components - labels, buttons, etc.
	private void initalizeComponents() {
	    exit = new JLabel();
	    back = new JLabel();
		leftArrow = new JLabel();
		rightArrow = new JLabel();
		highScoreJlabel = new JLabel[5][4];
	}
	
	//Initialize high score array
	private void initiateLabelsArray() {
		for(int col = 0; col < highScoreJlabel.length ; col++ ) 
			for(int row = 0; row < highScoreJlabel[0].length; row++)
				highScoreJlabel[col][row] = new JLabel();
	}
	
	//Displays  high score table on screen
	// 5 rows each contains number, image, name, number of wins
	private void displayOnScreen() {
		Font f = new Font("bebas",Font.PLAIN,16);
		HighScoreData user;
		int[] originalX = {445, 578, 743, 1230};
		int[] originalWidth = {98, 98, 250, 250};
		
		int col;
		for(col = 0; col < highScoreJlabel.length  
				&& highScoreDB.size() > col + page * 5 ; col++ ) {
			
			user = highScoreDB.get(col + page * 5);
			ImageIcon userImg = new ImageIcon(getClass().getResource(user.getPicture()));
			String[] text = {col + 1 + page * 5 + "", "", user.getUserName(), user.getNumberOfWins() + ""};
			
			for(int row = 0; row < 4; row++) {
				if(row != 1) {
					componentsArray.add(new MyComponent(highScoreJlabel[col][row], null,originalX[row] , 330 + col * 122, originalWidth[row], 98));
					highScoreJlabel[col][row].setText(text[row]);
					highScoreJlabel[col][row].setFont(f);
					changeLabelSize(highScoreJlabel[col][row], null ,originalX[row] , 330 + col * 122, originalWidth[row], 98);
				}else {
					componentsArray.add(new MyComponent(highScoreJlabel[col][row], userImg,originalX[row] , 330 + col * 122, originalWidth[row], 98));
					highScoreJlabel[col][row].setIcon(userImg);
					changeLabelSize(highScoreJlabel[col][row], userImg ,originalX[row] , 330 + col * 122, originalWidth[row], 98);
				}
					
				highScoreJlabel[col][row].setVisible(true);	
				if(!isTableDisplayed)
					getLayeredPane().add(highScoreJlabel[col][row], new Integer(3));
			}

		}
		isTableDisplayed = true;
		for(; col < 5 ; col++)
			for(int i =0; i < 4; i++)
				highScoreJlabel[col][i].setVisible(false);
	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			//QUIT
			if(exit == arg0.getSource()) {
				if(contactServer.logout(username))
					System.exit(0);
				else
					System.out.println("Error in logout");
			}
			
			//back to previous screen
			if(back == arg0.getSource()) {
				tables.setVisible(true);
				setVisible(false);
			}
			
			//Request to see the next rows of the table
			if(rightArrow == arg0.getSource()) {
				if(highScoreDB.size() > 5 + (page * 5)) {
					page++;
					displayOnScreen();
				}
			}
			
			//Request to see the previous rows of the table
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
