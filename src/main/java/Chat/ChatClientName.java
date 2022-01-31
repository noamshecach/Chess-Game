package Chat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Frames.GeneralJFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.Dimension;

public class ChatClientName extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel userNameMinimized;
	private int minTabWidth = (int)(59 * Chat.compProp), minTabHeight = (int) (45 * Chat.compProp);
	
	public ChatClientName(MouseAdapter mouseAdapter, String myName) {
		setOpaque(false);;
		
		setLayout(new MigLayout("", "0lp![grow]0lp!", "0lp![88%,grow]0lp![12%,grow]0lp!"));
		userNameMinimized = new JLabel(myName + " \n");
		userNameMinimized.setPreferredSize(new Dimension(59, 65));
		userNameMinimized.setMinimumSize(new Dimension(59, 20));
		userNameMinimized.setSize(100, 100);
		userNameMinimized.addMouseListener(mouseAdapter);
		userNameMinimized.setFont(new Font("Tahoma", Font.PLAIN, 17));
		userNameMinimized.setHorizontalAlignment(SwingConstants.CENTER);
		add(userNameMinimized, "cell 0 1, grow");
	}
	
	public void scale(int posX, int posY) {
		setBounds(posX, posY, Chat.width, Chat.height);
		userNameMinimized.setPreferredSize(new Dimension((int)(minTabWidth* GeneralJFrame.widthProp),(int)(minTabHeight* GeneralJFrame.heightProp)));
	}

}
