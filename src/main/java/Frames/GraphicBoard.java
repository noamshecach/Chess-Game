package Frames;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import BarName.BarName;
import Chat.Chat;
import Logic.Colors;
import SentObjects.AbstractTool;
import SentObjects.AvailableMoves;
import SentObjects.InitialSetup;
import SentObjects.Movement;
import SystemMessage.Message;
import Client.ContactServer;


public class GraphicBoard extends GeneralJFrame  {

	private static final long serialVersionUID = 1L;
	private JLabel exit, retire;
	private int lastPressedRow = 2, lastPressedCol = 2, size = 8, startX = 572, startY = 208;
	private ContactServer contactServer;
	private AvailableMoves lastOptionalMoves;
	private JLabel[][] background = new JLabel[size][size];
	private JLabel[][] foreground = new JLabel[size][size];
    private BarName barleft, barright;
    private Chat chat;
    private Hashtable<ImageIcon, Integer> fixToolPosition = new Hashtable<>();
	
	private ImageIcon retireImg = new ImageIcon(getClass().getResource("/media/game/retire.png"));
	private ImageIcon blackSqImg = new ImageIcon(getClass().getResource("/media/game/Parts/OriginalBlack.png"));
	private ImageIcon whiteSqImg = new ImageIcon(getClass().getResource("/media/game/Parts/OriginalWhite.png"));
	private ImageIcon whiteRollOver = new ImageIcon(getClass().getResource("/media/game/Parts/whiterollover.png"));
	private ImageIcon blackRollOver = new ImageIcon(getClass().getResource("/media/game/Parts/Blackrollover.png"));
	private ImageIcon squareClickedImg = new ImageIcon(getClass().getResource("/media/game/Parts/frame.png"));
	private ImageIcon possibleMovesImg = new ImageIcon(getClass().getResource("/media/game/Parts/possMov.png"));
	private ImageIcon eatCaseImg = new ImageIcon(getClass().getResource("/media/game/Parts/eatCase.png"));
    private int square_size = whiteSqImg.getIconWidth() + 1 ;
    private String username;
    private boolean isMyTurn;
    private Colors myColor;

	public GraphicBoard(final ContactServer contactServer, String username, InitialSetup init) {
		super("/media/game/GameScreen7.png");
		try {
			System.out.println("-------" + username);
			this.username = username; 
			this.contactServer = contactServer;
			this.isMyTurn = init.getTurn();
			this.myColor = init.getColor();
			boolean isAgainstComputer = (init.getOpponentName().equals("Computer"));
			
			initalize(isAgainstComputer);

			ButtonPressHandler mouseHandler = new ButtonPressHandler(this);
			addLabel(exit, 49, 222, exitImg,mouseHandler);
			addLabel(retire, 254,222, retireImg,mouseHandler);
			addBarName(barleft, 0, 0); 
	    	addBarName(barright, 1410, 0); 		    
		    if(!isAgainstComputer)
		    	addChat(30, 900, username, init.getOpponentName());
			
		    if(isMyTurn)
		    	barleft.setShineBar();
		    else
		    	barright.setShineBar();
			
			showBoard(init.getTools());
			setVisible(true);
			if(!isMyTurn) {
				Thread t = new Thread() {
					public void run() {
						displayOpponentMove(contactServer.requestOpponentMove());
					}
				};
				t.start();
			}
			
			isRetirement();
		} 
		catch (RemoteException e) { e.printStackTrace(); } 
		catch (MalformedURLException e) { e.printStackTrace(); } 
		catch (NotBoundException e) {  e.printStackTrace();  }
	
	}	
	
	private void isRetirement() {
		Thread t = new Thread() {
			public void run() {
				try {  contactServer.isOpponentRetire();  } 
				catch (InterruptedException | RemoteException e) { e.printStackTrace(); }
			}
		};
		t.start();
	}
	
	private void initalize(boolean isAgainstComputer) {
		exit = new JLabel();
		retire = new JLabel();
		barleft = new BarName(true, contactServer.getUser(),0,0, contactServer, this, getLayeredPane());
		if(isAgainstComputer)
			barright = new BarName(false,1410,0,contactServer, this, getLayeredPane());
		else
			barright = new BarName(false, contactServer.getOpponentUser(),1410,0, contactServer, this, getLayeredPane());
	}
	
	private void addChat(int posX, int posY, String username, String opponentName) throws RemoteException, MalformedURLException, NotBoundException {
		chat = new Chat(posX, posY,  opponentName,  username, getLayeredPane());
		getLayeredPane().add(chat, new Integer(4));
		componentsArray.add(new MyComponent(chat, null,posX , posY , Chat.width, Chat.height));
	}
	
	 public BarName getLeftBar() {
		return barleft;
	}

	public BarName getRightBar() {
		return barright;
	}

	/**
	   * this function opens the window and shows 
	   * the chess board.
	   */
	  void showBoard(List<AbstractTool> tools)
	  {
		    boolean flag = false;
			int posX, posY;
			String toolDescription;
			Colors toolColor;
			
			for(AbstractTool tool: tools) {
				posX = tool.getX();
				posY= tool.getY();
				toolDescription = tool.getType();
				toolColor = tool.getColor();
				BoardMouseHoverHandler hoverHandler = new BoardMouseHoverHandler(posX, posY);
				ImageIcon pic = new ImageIcon(getClass().getResource("/media/game/Parts/"+ toolColor +"/"+ toolDescription +".png"));
				
				foreground[posX][posY] = new JLabel();
				setToolPic(foreground[posX][posY], (startX) + posY * square_size, (startY + fixToolsOffset(toolDescription)) + posX * square_size, 
						pic.getIconWidth(), pic.getIconHeight(), pic,hoverHandler);
				addFixToolPosition(toolDescription, (ImageIcon)foreground[posX][posY].getIcon());
				if(toolColor == Colors.WHITE)
					foreground[posX][posY].setDisplayedMnemonic('W');
				else
					foreground[posX][posY].setDisplayedMnemonic('B');
			}
			for(int i = 0; i < 8; i++){
				for(int z = 0; z < 8; z++){
					background[i][z] = new JLabel();
					addLabel(background[i][z], startX + z * square_size , startY + i * square_size,
								flag? whiteSqImg:blackSqImg ,null);
					if(foreground[i][z]  == null) {
						foreground[i][z] = new JLabel();
						BoardMouseHoverHandler hoverHandler = new BoardMouseHoverHandler(i, z);
						setToolPic(foreground[i][z], (startX) + z * square_size, startY + i * square_size,square_size,square_size, null ,hoverHandler);
					}
					flag = !flag;
				}
				flag = !flag;
			}						
	  }
	  
	  private int fixToolsOffset(String tool) {
			if(tool.equals("King")) 
				return -50;
			if(tool.equals("Queen")) 
				return -25;		
			if(tool.equals("Bishop")) 
				return -15;
			if(tool.equals("Pawn")) 
				return 20;	
			return 0;	
	  }
	  
	  private void addFixToolPosition(String tool, ImageIcon pic) {
			if(tool.equals("King")) 
				fixToolPosition.put(pic, -50);
			if(tool.equals("Queen")) 
				fixToolPosition.put(pic, -25);	
			if(tool.equals("Bishop")) 
				fixToolPosition.put(pic, -15);
			if(tool.equals("Pawn")) 
				fixToolPosition.put(pic, 20);	
	  }
	  
		public void setToolPic(JLabel label, int posX, int posY,int width, int height, ImageIcon image, 
				MouseListener frame) {
					
			//comp.add(new MyComponent(label, image, posX, posY,square_size, square_size ));
			changeLabelSize(label, image, posX, posY, width, height);
			label.addMouseListener(frame); 
			getLayeredPane().add(label, new Integer(4));
				
		}
		
		private class BoardMouseHoverHandler extends MouseAdapter {
		
			private int col, row;
		
			public BoardMouseHoverHandler(int col, int row) {
				this.col = col;
				this.row = row;
			}
			
			public void mouseEntered(MouseEvent ev) {
				if (ev.getSource() instanceof JLabel 
						&& !(col == lastPressedCol && row == lastPressedRow) 
						&& (lastOptionalMoves == null ||
						( !lastOptionalMoves.getCanMoveTo().contains(new Point(col, row))  
						&& !lastOptionalMoves.getCanEat().contains(new Point(col, row)))))
				{
					if((col + row) % 2 != 0) 
						changeBackground(whiteRollOver, col, row);
					else 
						changeBackground(blackRollOver, col, row);
				}
			}
			
			public void mouseExited(MouseEvent ev) {
				if (ev.getSource() instanceof JLabel 
						&& !(col == lastPressedCol && row == lastPressedRow) 
						&& (lastOptionalMoves == null ||
						( !lastOptionalMoves.getCanMoveTo().contains(new Point(col, row))  
						&& !lastOptionalMoves.getCanEat().contains(new Point(col, row)))))
					getPreviousBackground(col, row);
			}
			
			@Override
			public void mouseClicked(MouseEvent ev) {
				if(isMyTurn) {
			    	barright.setStandardBar();
			    	barleft.setShineBar();
					deleteMarkSquares();
					getPreviousBackground(lastPressedCol, lastPressedRow);
					if(col == lastPressedCol && row == lastPressedRow) {
						lastPressedCol = 1; lastPressedRow = 1;
						return;
					}
					if(lastOptionalMoves != null &&
							(lastOptionalMoves.getCanMoveTo().contains(new Point(col, row)) ||
						lastOptionalMoves.getCanEat().contains(new Point(col, row))	)) { 
						Movement move = contactServer.performeMove(new Point(lastPressedCol,lastPressedRow ), new Point(col,row));
						performeMove(lastPressedRow,lastPressedCol, row, col, move);
						if(move.isRooking())
							executeRooking(row, move);
						checkEndingFlags(move);
						isMyTurn = false;
						getRightBar().setShineBar();
						getLeftBar().setStandardBar();
				    	lastOptionalMoves = null;
				    	Thread t = new Thread() {
				    		public void run() {
						    	displayOpponentMove(contactServer.requestOpponentMove());	
				    		}
				    	};
				    	t.start();
					}else {
//						if((myColor == Colors.WHITE && ((JLabel)ev.getSource()).getDisplayedMnemonic() == 'W')  || 
//								(myColor == Colors.BLACK && ((JLabel)ev.getSource()).getDisplayedMnemonic() == 'B')) {
							changeBackground(squareClickedImg, col, row);
							AvailableMoves moves = contactServer.getPossibleMoves(col, row );
							markSquares(moves);
							lastPressedCol = col;
							lastPressedRow = row;
//						}
					}
				}
			}
			
		}
		
		private void displayOpponentMove(Movement move) {
			Point[] lastMove = move.getLastMove(); 
			performeMove((int)(lastMove[0].getY()),(int)(lastMove[0].getX()), (int)(lastMove[1].getY()),(int)(lastMove[1].getX()), move);
			if(move.isRooking())
				executeOpponentRooking((int)(lastMove[1].getY()), move);
			checkEndingFlags(move);
			isMyTurn = true;
			getRightBar().setStandardBar();
			getLeftBar().setShineBar();
		}
		
		private void executeOpponentRooking(int xPos, Movement movement) {
			if(xPos == 2)
				performeMove(0,0,3,0, movement);
			else
				performeMove(7,0,5,0, movement);
		}
		
		private void executeRooking(int xPos, Movement movement) {
			if(xPos == 2)
				performeMove(0,7,3,7, movement);
			else
				performeMove(7,7,5,7, movement);
		}
		
		private void performeMove(int srcRow, int srcCol, int destRow, int destCol, Movement movement) {
			foreground[destCol][destRow].setDisplayedMnemonic(foreground[srcCol][srcRow].getDisplayedMnemonic());
			foreground[srcCol][srcRow].setDisplayedMnemonic('O');
			
			ImageIcon pic = (ImageIcon)foreground[srcCol][srcRow].getIcon();
			changeLabelSize(foreground[srcCol][srcRow], null, (startX) + srcRow * square_size, startY + srcCol * square_size, 
					square_size, square_size);
			
			int fix;
			if(fixToolPosition.get(pic) == null)
				fix = 0;
			else {
				fix = fixToolPosition.get(pic);
				fixToolPosition.remove(pic);
			}
			
			if(movement.isPromotion()){
				if(isMyTurn)
					pic = new ImageIcon(getClass().getResource("/media/game/Parts/"+ myColor +"/queen.png"));
				else
					pic = new ImageIcon(getClass().getResource("/media/game/Parts/"+ (myColor == Colors.WHITE? Colors.BLACK: Colors.WHITE) + "/queen.png"));
				fix = -25;
				changeLabelSize(foreground[destCol][destRow], pic, (startX) + destRow * square_size, (startY + fix) + destCol * square_size, 
						pic.getIconWidth(), pic.getIconHeight());
			}else {
				changeLabelSize(foreground[destCol][destRow], pic, (startX) + destRow * square_size, (startY + fix) + destCol * square_size, 
						pic.getIconWidth(), pic.getIconHeight());
			}
			
			if(fix != 0)
				fixToolPosition.put((ImageIcon)foreground[destCol][destRow].getIcon(), fix);
		}
		
		private void checkEndingFlags(Movement movement) {
			if(movement.isMate() == Mate.Draw) {
				try {  
					contactServer.displayDrawMessage(); 
					RetireButtonHandler handler = new RetireButtonHandler(this);
					Message.displayDraw(handler);

				} 
				catch (RemoteException e) { e.printStackTrace(); }
			}
			if((movement.isMate() == Mate.onWHITE && myColor == Colors.BLACK) ||
					(movement.isMate() == Mate.onBLACK && myColor == Colors.WHITE)) {
				try {  
					contactServer.displayChessMateMessage(true); 
					RetireButtonHandler handler = new RetireButtonHandler(this);
					Message.displayCheckMate(handler, true);
				} 
				catch (RemoteException e) { e.printStackTrace(); }
			}
			if((movement.isMate() == Mate.onWHITE && myColor == Colors.WHITE) ||
					(movement.isMate() == Mate.onBLACK && myColor == Colors.BLACK) ) {
				try {  
					contactServer.displayChessMateMessage(false); 
					RetireButtonHandler handler = new RetireButtonHandler(this);
					Message.displayCheckMate(handler, false);
				} 
				catch (RemoteException e) { e.printStackTrace(); }
			}
			if(movement.isChess() != Check.NONE) 
				Message.displayCheck();
		}
				
		private void getPreviousBackground(int col, int row) {
			if((col + row) % 2 != 0) 
				changeBackground(whiteSqImg, col, row);
			else 
				changeBackground(blackSqImg, col, row);
		}
		
		private void changeBackground(ImageIcon picture, int col, int row) {
			background[col][row].setIcon(scaleImage(picture, 
					(int)(picture.getIconWidth()  * widthProp), 
					(int)(picture.getIconHeight() * heightProp)));
		}
		
		public void deleteMarkSquares() {
			if(lastOptionalMoves == null)
				return;
			for (Point dir : lastOptionalMoves.getCanMoveTo()) {
				if((dir.x + dir.y) % 2 != 0) 
					changeBackground(whiteSqImg, dir.x, dir.y);
				else 
					changeBackground(blackSqImg, dir.x, dir.y);
			}
			for (Point dir : lastOptionalMoves.getCanEat()) {
				if((dir.x + dir.y) % 2 != 0) 
					changeBackground(whiteSqImg, dir.x, dir.y);
				else 
					changeBackground(blackSqImg, dir.x, dir.y);
			}
		}
		
		public void markSquares(AvailableMoves moves) {
			for (Point dir : moves.getCanMoveTo()) 
				changeBackground(possibleMovesImg, dir.x, dir.y);
			for (Point dir : moves.getCanEat()) 
				changeBackground(eatCaseImg, dir.x, dir.y);
			lastOptionalMoves = moves;
		}

		private class ButtonPressHandler extends MouseAdapter {
			
			private GraphicBoard gb;
			
			public ButtonPressHandler(GraphicBoard gb) {
				this.gb = gb;
			}
				
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(exit == arg0.getSource()) {
					try {  
						contactServer.retire(); 
						ExitButtonHandler handler = new ExitButtonHandler();
						Message.displayCheckMate(handler,false);
					} 
					catch (RemoteException e) {e.printStackTrace();} 
				}
				if(retire == arg0.getSource()) {
					try {  
						contactServer.retire(); 
						RetireButtonHandler handler = new RetireButtonHandler(gb);
						Message.displayCheckMate(handler,false);
					} 
					catch (RemoteException e) {e.printStackTrace();} 
				}
			}
		}
		
		private class RetireButtonHandler extends MouseAdapter {

			private JFrame jf;
			
			public RetireButtonHandler(JFrame jf) {
				this.jf = jf;
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				new Tables(contactServer, username);
				jf.dispose();
			}
		}

		private class ExitButtonHandler extends MouseAdapter {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		}
}
