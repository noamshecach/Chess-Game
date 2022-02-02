package Timer;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.util.TimerTask;
import javax.swing.JPanel;

import Frames.GeneralJFrame;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

//View component - display timer on screen
public class Timer extends JPanel {

	private static final long serialVersionUID = 1L;
	private final TimerLogic logic;
	private final JLabel lblTimer;
	private Font f;
	
	public Timer(long startTime) {

	    int delay = 1000;
	    int period = 1000;
		
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		//Set the dimensions of timer label
		lblTimer = new JLabel("Timer");
		changeTimerSize();

		//TimerLogic cast startTime to adequate string.
		logic = new TimerLogic(startTime);
		
		//Set text of timer label
		lblTimer.setText(logic.toString());
		
		//Set the location of the label
		lblTimer.setHorizontalTextPosition(JLabel.CENTER);
		lblTimer.setVerticalTextPosition(JLabel.CENTER);
		
		//The timer ticking function
	    java.util.Timer timer = new java.util.Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {

	        public void run() {
	        	logic.dropSecond();
	        	lblTimer.setText(logic.toString());

	        }
	    }, delay, period);
		
		add(lblTimer, BorderLayout.SOUTH);
	}
	
	//Scale 'icon' dimensions to [width,height]
	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	
	//Returns true if the timer is over or false otherwise.
	public boolean isTimeOver() {
		return this.logic.isTimeOver();
	}
	
	//Start the count down again
	public void startOver(long startTime) {
		logic.startOver(startTime);
		lblTimer.setText(logic.toString());
	}
	
	//Scale timer size
	public void changeTimerSize() {
		setBounds((int)(850 * GeneralJFrame.widthProp) , (int)(953 * GeneralJFrame.heightProp), (int)(480 * GeneralJFrame.widthProp),
				(int)(120 * GeneralJFrame.heightProp));
		lblTimer.setIcon(scaleImage(new ImageIcon(Timer.class.getResource("/media/tables/buttonCollectYourBonusREK.png")),
				(int)(454 * GeneralJFrame.widthProp), (int)(96 * GeneralJFrame.heightProp) ));
		this.f = new Font("bebas",Font.PLAIN,(int) Math.floor(28 * GeneralJFrame.widthProp));
		lblTimer.setFont(f);
	}

}
