package Chat;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import Frames.GeneralJFrame;
import JMS.JmsReceiver;

public class Chat extends JPanel implements MessageListener {

	private static final long serialVersionUID = 1L;
	private ChatPressed pressEvent;
	private ChatBackground chatBackground;
	private ChatMinimized chatMinimized;
	private ChatClientName chatClientName;
	private ChatContent chatContent;
	private boolean isChatOpen = true; 
	public static double compProp = 0.72;
	public static int width = (int) (410 * compProp), height = (int) (550 * compProp);
	private int originalWidth= (int) (410 * compProp), originalHeight = (int) (550 * compProp);

	public Chat(int posX, int posY, String opponentQueue, String myQueue, JLayeredPane lp)  throws RemoteException, MalformedURLException, NotBoundException {
		setBounds(posX, posY, 410, 550);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setOpaque(false);
		
		new JmsReceiver(this, "jms/" + opponentQueue);
		
	    chatBackground = new ChatBackground();
	    chatMinimized = new ChatMinimized();
	    chatMinimized.setVisible(false);
	    chatMinimized.setEnabled(false);
	    
	    pressEvent = new  ChatPressed(chatBackground, chatMinimized);

	    chatClientName = new ChatClientName(pressEvent, opponentQueue);
	    chatClientName.setEnabled(false);
	    chatClientName.setVisible(false);
	    chatContent = new ChatContent(pressEvent, myQueue, opponentQueue);
	    pressEvent.setChat(chatContent);
	    pressEvent.setChatClientName(chatClientName);

	    draw();
	    lp.add(chatBackground, new Integer(4));
	    lp.add(chatMinimized, new Integer(4));
	    lp.add(chatClientName, new Integer(5));
	    lp.add(chatContent, new Integer(5));
		
	}
	
	public void draw() {
		width = (int) (originalWidth * GeneralJFrame.widthProp);
		height = (int) (originalHeight * GeneralJFrame.heightProp);
	    chatBackground.scale((int)((this.getX() ) * compProp), (int)((this.getY() ) * compProp));
	    chatContent.scale((int)(this.getX() * compProp), (int)(this.getY() * compProp));
	    chatMinimized.scale((int)((this.getX() ) * compProp), (int)(this.getY() * compProp));
	    chatClientName.scale((int)((this.getX() )* compProp), (int)((this.getY() ) * compProp));
	}
	
	public ChatContent getChatContent() {
		return chatContent;
	}

	public void setChatContent(ChatContent chatContent) {
		this.chatContent = chatContent;
	}
	
	private class ChatPressed extends MouseAdapter {
		
		private ChatBackground chatBackground;
		private ChatContent chatContent;
		private ChatMinimized chatMinimized;
		private ChatClientName chatClientName;
		
		public ChatPressed(ChatBackground chatBackground, ChatMinimized chatMinimized) {
			this.chatBackground = chatBackground;
			this.chatMinimized = chatMinimized;
		}
		
		public void setChat(ChatContent chatContent) {
			this.chatContent = chatContent;
		}
		
		public void setChatClientName(ChatClientName chatClientName) {
			this.chatClientName = chatClientName;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource() instanceof JLabel) {
				if(((JLabel)e.getSource()).equals(chatContent.getUserNameHeader())){ //need to minimize
					this.chatBackground.setEnabled(false);
					this.chatBackground.setVisible(false);
					this.chatContent.setEnabled(false);
					this.chatContent.setVisible(false);
					this.chatMinimized.setEnabled(true);
					this.chatMinimized.setVisible(true);
					this.chatMinimized.closedChatBlack();
					this.chatClientName.setEnabled(true);
					this.chatClientName.setVisible(true);
					isChatOpen = false;
				}else {
					if(((JLabel)e.getSource()).getParent().equals(chatClientName)) {  //opens the chat
						this.chatBackground.openChatBlack();
						this.chatBackground.setEnabled(true);
						this.chatBackground.setVisible(true);
						this.chatContent.setEnabled(true);
						this.chatContent.setVisible(true);
						this.chatMinimized.setEnabled(false);
						this.chatMinimized.setVisible(false);
						this.chatClientName.setEnabled(false);
						this.chatClientName.setVisible(false);
						isChatOpen = true;
					}
				}
			}
			if(e.getSource() instanceof JTextArea &&  
					((JTextArea)e.getSource()).equals(chatContent.getText())) 
				chatBackground.openChatBlack();
		}

	}
	
	@Override
	public void onMessage(Message arg0) {
		try {
			TextMessage textMessage  = (TextMessage) arg0;
			String message = textMessage.getText();
			JTextArea textBoard = chatContent.getTextBoard();
			if(!textBoard.getText().equals(""))
				textBoard.setText(textBoard.getText() +"\n"+ message);
			else
				textBoard.setText(textBoard.getText() + message);
			if(isChatOpen) 
				chatBackground.openChatGreen();
			else 
				chatMinimized.closedChatGreen();
			
		} catch (JMSException e) { e.printStackTrace(); }
	}

}
