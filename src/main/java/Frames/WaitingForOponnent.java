package Frames;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import Client.ContactServer;

public class WaitingForOponnent extends GeneralJFrame {
	
	private static final long serialVersionUID = 1L;
	private MouseHandler mouseHandler = new MouseHandler();
	private JLabel myPicture, VS, searching, vsComputer, waitSign;
	private ContactServer contactServer;
	private int tableNumber;
	
	private ImageIcon vsComputerImg = new ImageIcon(getClass().getResource( "/media/WaitingForOpponent/vsComputer.png"));
	private ImageIcon vsImg = new ImageIcon(getClass().getResource( "/media/WaitingForOpponent/VS.png"));
	private ImageIcon searchingImg = new ImageIcon(getClass().getResource( "/media/WaitingForOpponent/searching.png"));
	private ImageIcon profileLocImg = new ImageIcon(getClass().getResource( "/media/WaitingForOpponent/profileLoc.png"));
	private ImageIcon waitSignImg = new ImageIcon(getClass().getResource( "/media/WaitingForOpponent/LOADING.gif"));
	private String username;
	
	public WaitingForOponnent(JLabel PlayerImage, ContactServer contactServer, int tableNumber, String username) {
		
		super("/media/WaitingForOpponent/background1.png");
		this.username = username;
		this.toFront();
		this.tableNumber = tableNumber;
		this.contactServer = contactServer;
		
		initalize();
	
	    addLabel(myPicture, 370, 222, profileLocImg,mouseHandler);
	    addLabel(VS, 845, 317, vsImg,mouseHandler);
	    addLabel(waitSign, 1190, 260, waitSignImg,mouseHandler);
	    addLabel(searching, 592, 648, searchingImg,mouseHandler);
	    addLabel(vsComputer, 760, 735, vsComputerImg,mouseHandler);
	    
	    getLayeredPane().add(PlayerImage, new Integer(4));
	    setVisible(true);
	}
	
	private void initalize() {
		myPicture = new JLabel();
		VS = new JLabel();
		searching = new JLabel();
		vsComputer = new JLabel();
		waitSign = new JLabel();
	}
	
	public void setOpponentImage(ImageIcon opponentImage) {
		try {
			searching.setVisible(false);
			waitSign.setVisible(false);
			vsComputer.setVisible(false);
			this.toFront();
			this.setAlwaysOnTop(true);
			//ImageIcon playerImageImg = new ImageIcon(getClass().getResource(s));
			
			JLabel opponent = new JLabel();
			opponent.setBounds((int)(1190 * widthProp), (int)(222 * heightProp), (int)(opponentImage.getIconWidth() * 1.22 * widthProp), (int)(opponentImage.getIconHeight() * 1.22 * heightProp));
			opponent.setIcon(scaleImage(opponentImage, (int)(opponentImage.getIconWidth() * 1.22 * widthProp), (int)(opponentImage.getIconHeight() * 1.22 * heightProp)));
			getLayeredPane().add(opponent, new Integer(4));
			
			//contactServer.takeSeat(username,tableNumber * 100);

			Thread.sleep(5000);
			this.dispose();
		} 
		catch (InterruptedException e) { e.printStackTrace();}
	}

	private class MouseHandler extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(vsComputer == arg0.getSource()) {
				contactServer.findOpponent("Computer", tableNumber);
			}		
		}
	}
	
}
