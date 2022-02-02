package Frames;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class ChooseCharacter extends GeneralJFrame {

	private static final long serialVersionUID = 1L;
	private MouseHandler mouseHandler = new MouseHandler();
	private ImageIcon[] colorImgs;
	private ImageIcon[] greyImgs;
	private JLabel[] players;
	private Register register;
	
	public ChooseCharacter(Register register) {
		// Calling parent class, set background image
		super("/media/playersImage/background.png");
		
		//Initialize 2 arrays of images. The first includes colorful images and the second grey images.
		
		this.register = register;
		colorImgs = new ImageIcon[6];
		greyImgs = new ImageIcon[6];
		players = new JLabel[6];
		int[][] locations = {{472, 802, 1132, 472,802,1132}, {628,628,628,298,298,298}};
		for(int i = 0; i < colorImgs.length; i++) {
			players[i] = new JLabel();
			colorImgs[i] = new ImageIcon(getClass().getResource("/media/playersImage/0"+ (i + 1) + ".png"));
			greyImgs[i] = new ImageIcon(getClass().getResource("/media/playersImage/0"+ (i + 1) + "-bw.png"));
			addLabel(players[i], locations[0][i], locations[1][i], greyImgs[i],mouseHandler);
		}
	    setVisible(true);
	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			//Get the index of the chosen image
			String s = "";
			for(int i =0; i< players.length; i++) {
				if(players[i] == arg0.getSource()) {
					s = "/media/playersImage/0"+ (i + 1) +".png";
					break;
				}
			}
			//Set the chosen image as the user image
			if(!s.equals("")) {
				register.setAvatar(s);
				ChooseCharacter.this.setVisible(false);
				register.setVisible(true);
			}
		}
		
		//Change the image to colorful when entered
		public void mouseEntered(MouseEvent ev) {
			for(int i =0; i< players.length; i++) {
				if(players[i] == ev.getSource()) {
					players[i].setIcon(scaleImage(colorImgs[i], players[i].getWidth(), players[i].getHeight()));
					break;
				}
			}

		}
		
		//Change the image to grey when exited
		public void mouseExited(MouseEvent ev) {
			for(int i = 0; i < players.length; i++) {
				if(players[i] == ev.getSource()) {
					players[i].setIcon(scaleImage(greyImgs[i], players[i].getWidth(), players[i].getHeight()));
					break;
				}
			}
		}
	}
}
