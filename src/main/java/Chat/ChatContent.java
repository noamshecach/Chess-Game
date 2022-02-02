package Chat;

import javax.swing.JPanel;
import JMS.JmsSender;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import net.miginfocom.swing.MigLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import javax.swing.SwingConstants;

//Display the chat interaction, send messages
public class ChatContent extends JPanel  {

	private static final long serialVersionUID = 1L;
	private JTextArea textBoard;
	private JScrollPane scrollPane;
	private JLabel userNameHeader;
	private JTextArea text;
	private JScrollPane scrollPane_1;
	private JmsSender jmsSender;
	private String myName;
	
	public ChatContent(MouseAdapter mouseAdapter, String myQueue, String opponentName) throws RemoteException, MalformedURLException, NotBoundException {

		setOpaque(false);
		setLayout(new MigLayout("", "0lp![grow]0lp!", "0lp![12%,grow]0lp![72%,grow]0lp![16%,grow]0lp!"));
		
		this.myName = myQueue;
		//Initialize connection to server in order to send messages to opponent
		jmsSender = new JmsSender("jms/" + myQueue);
		
		//Add chat heading label
		addNameLabel(opponentName, mouseAdapter);
		
		//Main chat pane - includes the history of messages between users
		addMainChatPane();
		
		//Text box for writing messages. listen to 'Enter' key press.
		addWritingBox(mouseAdapter);
		
	}
	
	//Text box for writing messages. listen to 'Enter' key press.
	public void addWritingBox(MouseAdapter mouseAdapter) {
		text = new JTextArea("");
		text.addMouseListener(mouseAdapter);
		text.setOpaque(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println(text.getText().trim().length());
				if(e.getKeyCode() == KeyEvent.VK_ENTER && text.getText().trim().length() > 0) 
					sendMessage();
			}
		});
		addScrollPaneWritingBox();
	}
	
	//Add scroll pane for the text box for new messages.
	public void addScrollPaneWritingBox() {
		scrollPane_1 = new JScrollPane(text);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBorder(null);
		scrollPane_1.getViewport().setOpaque(false);
		scrollPane_1.setOpaque(false);

		add(scrollPane_1, "cell 0 2,grow");
	}
	
	//Add chat heading label
	public void addNameLabel(String opponentName, MouseAdapter mouseAdapter) {
		userNameHeader = new JLabel(opponentName);
		userNameHeader.setBackground(new Color(0,0,0));
		userNameHeader.setHorizontalAlignment(SwingConstants.CENTER);
		userNameHeader.addMouseListener(mouseAdapter);
		add(userNameHeader, "cell 0 0,grow");
	}
	
	//Main chat pane - includes the history of messages between users
	public void addMainChatPane() {
		textBoard = new JTextArea();
		textBoard.setCaretColor(Color.BLACK);
		textBoard.setBorder(null);
		textBoard.setBackground(new Color(0, 0, 0,0));
		textBoard.setOpaque(false);
		textBoard.setEditable(false);
		textBoard.setLineWrap(true);
		textBoard.setWrapStyleWord(true);
		addScrollPaneMainPane();
	}
	
	//Adding a scroll pane to the main pane.
	public void addScrollPaneMainPane() {
		scrollPane = new JScrollPane(textBoard);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		add(scrollPane, "cell 0 1 ,grow");
	}
	
	public JLabel getUserNameHeader() {
		return userNameHeader;
	}

	public void setUserNameHeader(JLabel userNameHeader) {
		this.userNameHeader = userNameHeader;
	}

	public JTextArea getTextBoard() {
		return textBoard;
	}

	public void setTextBoard(JTextArea textBoard) {
		this.textBoard = textBoard;
	}

	public JTextArea getText() {
		return text;
	}

	public void setText(JTextArea text) {
		this.text = text;
	}

	//Sent the message 'message' to the opponent.
	public void sendMessage() {
		String message = text.getText().trim();
		jmsSender.send(myName + ": " + message);
		displayNewMessage(myName + ": " + message);
		text.setText("");
	}
	
	//Display new message in the main pane.
	private void displayNewMessage(String message) {
		if(!textBoard.getText().equals(""))
			textBoard.setText(textBoard.getText() +"\n"+ message);
		else
			textBoard.setText(textBoard.getText() + message);
	}
	
	public void scale(int posX, int posY) {
		setBounds(posX, posY , Chat.width, Chat.height);
		
	}

}
