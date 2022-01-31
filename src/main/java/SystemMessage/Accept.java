package SystemMessage;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Frames.GeneralJFrame;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

public class Accept extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Accept(MouseAdapter mouseHandler, JFrame jf) {
		setOpaque(false);
		setBounds((int)(100 * GeneralJFrame.widthProp) , (int)(100 * GeneralJFrame.heightProp), (int)(840 * GeneralJFrame.widthProp),
				(int)(420 * GeneralJFrame.heightProp));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		
		JLabel ok = new JLabel();
		ok.setHorizontalAlignment(SwingConstants.CENTER);
		MouseHandler messageHandler = new MouseHandler(jf);
		ok.addMouseListener(mouseHandler); 
		ok.addMouseListener(messageHandler);
		ok.setOpaque(false);
		ok.setIcon(scaleImage(new ImageIcon(Background.class.getResource("/media/messages/warning/ok.png")),
				(int)(190 * GeneralJFrame.widthProp), (int)(96 * GeneralJFrame.heightProp) ));
		
		add(ok, BorderLayout.SOUTH);
		
		
	}
	
	private  ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}

	private class MouseHandler extends MouseAdapter {

		private JFrame jf;
		
		public MouseHandler(JFrame jf) {
			this.jf = jf;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			jf.dispose();
		}
	}
	
	
}
