package SystemMessage;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import javax.swing.JFrame;
import Frames.GeneralJFrame;


public class Message {
	
	public static JFrame generalMessage() {
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBounds(300, 100,(int) (1000 * GeneralJFrame.widthProp), (int)(600 * GeneralJFrame.heightProp));
		jf.setUndecorated (true);
		jf.setVisible(false);
		jf.getContentPane().setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
		jf.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
		return jf;
	}

	public static void displayWarning(String text) {

		JFrame jf = generalMessage();
		Background background = new Background("/media/messages/warning/errorbackground.png");
		//MouseHandler mouseHandler = new MouseHandler(jf) ;
		Accept accept = new Accept(null, jf);
		
		jf.getLayeredPane().add(background, new Integer(1));
		jf.getLayeredPane().add(accept, new Integer(3));
		
		Text textPanel = new Text(text);
		jf.getLayeredPane().add(textPanel, new Integer(2));
		jf.setVisible(true);
	}
	
	public static void displayCheckMate(MouseAdapter handler, boolean isWin) {
		JFrame jf = generalMessage();
		Background background;
		if(isWin)
			background = new Background("/media/messages/mate/matewin.png");
		else
			background = new Background("/media/messages/mate/matelose.png");
		Accept accept = new Accept(handler,jf);
		
		jf.getLayeredPane().add(background, new Integer(1));
		jf.getLayeredPane().add(accept, new Integer(2));
		jf.setVisible(true);
	}
	
	public static void displayCheck() {
		JFrame jf = generalMessage();
		
		Background background = new Background("/media/messages/chess/check.png");
		//MouseHandler mouseHandler = new MouseHandler(jf);
		Accept accept = new Accept(null, jf);
		
		jf.getLayeredPane().add(background, new Integer(1));
		jf.getLayeredPane().add(accept, new Integer(2));
		jf.setVisible(true);
	}
	
	public static void displayDraw(MouseAdapter handler) {
		JFrame jf = generalMessage();
		
		Background background = new Background("/media/messages/draw/draw.png");
		Accept accept = new Accept(handler, jf);
		
		jf.getLayeredPane().add(background, new Integer(1));
		jf.getLayeredPane().add(accept, new Integer(2));
		jf.setVisible(true);
	}
		
}
