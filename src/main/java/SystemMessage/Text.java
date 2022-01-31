package SystemMessage;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Frames.GeneralJFrame;
import javax.swing.JTextArea;

public class Text extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Text(String text) {
		setOpaque(false);
		setBounds(100,100,840,400);
		setBounds((int)(280 * GeneralJFrame.widthProp) , (int)(150 * GeneralJFrame.heightProp), (int)(840 * GeneralJFrame.widthProp),
				(int)(300 * GeneralJFrame.heightProp));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea();
		textArea.setText("                                           ERROR\n" +text);
		textArea.setEditable(false);
		textArea.setBorder(null);
		textArea.setFont(new Font("Bebas", Font.PLAIN, 30));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setOpaque(false);
		add(textArea, BorderLayout.CENTER);


		
		
	}

}
