package RMI;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.List;
import javax.swing.ImageIcon;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import com.noam.jpa_project.Server.DataBaseAccess;
import com.noam.jpa_project.Server.UserAccount;
import JMS.CreateQueue;
import Logic.Board;
import Logic.Colors;
import Logic.Game;
import Logic.Player;
import Logic.XMLRead;
import SentObjects.AbstractTool;
import SentObjects.AvailableMoves;
import SentObjects.InitialSetup;
import SentObjects.Movement;

public class ServerService extends UnicastRemoteObject implements IServerService, Serializable {

	private static final long serialVersionUID = 1L;
	private int[] gamesWaitingToStart;
	private Hashtable<Integer, Game> games = new Hashtable<Integer, Game>();
	private final int numberOfTables = 10;
	private int index = 1;
	private DataBaseAccess db;
	
	public ServerService(Registry registry, String name) throws RemoteException {
		
		// Bind this class to the String 'name' on the registry instance.
		registry.rebind(name, this);
		
		// db object can access the database
		this.db = new DataBaseAccess();
		
		// Defining waiting queues  (tables array) 
		this.gamesWaitingToStart = new int[numberOfTables]; 
	}

	@Override
	public InitialSetup findOpponent(String username, int tableNumber) throws InterruptedException, ParserConfigurationException, SAXException, IOException {
		Game game;
		Colors color1 , color2;
		int gameIdx;
		if(gamesWaitingToStart[tableNumber] != 0) {
			synchronized(games) {
				gameIdx = gamesWaitingToStart[tableNumber];
				game = games.get(gameIdx);
				gamesWaitingToStart[tableNumber] = 0;
				if(username.equals("Computer")) {
					game.startGameVSComputer();
					game.setAgainstComputer(true);
					games.notify();
					return null;
				}
				game.startGame(username);
				color1 = game.getPlayer(1).getColor();
				new CreateQueue(game.getUsernames());
				games.notify();
			}
			return new InitialSetup(getAbstractToolList(color1), !game.getTurn(), color1, 
					new ImageIcon(getClass().getResource(db.getClientImage(game.getPlayer(0).getUsername()))), game.getPlayer(0).getUsername(), gameIdx, 1);
			
		}else {
			synchronized(games) {
				game = new Game(username, index, (tableNumber + 1) * 100);
				games.put(index, game);
				gameIdx = index;
				gamesWaitingToStart[tableNumber] = index++;
				games.wait();
				color2 = game.getPlayer(0).getColor();
			}
			if(game.getPlayer(1).getUsername().equals("Computer"))
				return new InitialSetup(getAbstractToolList(color2), game.getTurn(), color2, new ImageIcon(getClass().getResource("/media/playersImage/02.png")), "Computer", gameIdx, 0);
			else
				return new InitialSetup(getAbstractToolList(color2), game.getTurn(), color2, 
						new ImageIcon(getClass().getResource(db.getClientImage(game.getPlayer(1).getUsername()))), game.getPlayer(1).getUsername(), gameIdx, 0);
		}
	}
	
	private List<AbstractTool> getAbstractToolList(Colors color) throws ParserConfigurationException, SAXException, IOException {
		File inputFile = new File(System.getProperty("user.dir") + "///tools.txt");
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		XMLRead userhandler = new XMLRead(color);
		saxParser.parse(inputFile, userhandler);
		return userhandler.getAbstractToolList();
	}

	@Override
	public AvailableMoves getPossibleMoves(int col, int row, int gameIdx, int player) throws RemoteException {
		if(games.get(gameIdx).getCurrentPlayerColor() == Colors.WHITE)
			return games.get(gameIdx).getBoard().getPossibleMoves(col, row);
		else
			return games.get(gameIdx).getBoard().getPossibleMoves(7 - col, row);
	}

	@Override
	public Movement performeMove(Point curSq, Point toSq, int gameIdx, int player) throws RemoteException {
		Movement move;
		synchronized(games.get(gameIdx)) {
			if(games.get(gameIdx).getCurrentPlayerColor() == Colors.WHITE)
				move = games.get(gameIdx).getBoard().makeMove(new Point(curSq.x + Board.WALL_SIZE, curSq.y + Board.WALL_SIZE)
						, new Point(toSq.x + Board.WALL_SIZE, toSq.y + Board.WALL_SIZE));
			else
				move = games.get(gameIdx).getBoard().makeMove(new Point((7 - curSq.x) + Board.WALL_SIZE, curSq.y + Board.WALL_SIZE)
						, new Point((7- toSq.x) + Board.WALL_SIZE, toSq.y + Board.WALL_SIZE));
				
			//games.get(gameIdx).getBoard().printBoard();
			games.get(gameIdx).setLastMove(move);
			games.get(gameIdx).changeTurn();
			games.get(gameIdx).notify();
		}
		return move;
	}

	
	@Override
	public Movement requestOpponentMove(int gameIdx, int playerIdx) throws RemoteException, InterruptedException {
		synchronized(games.get(gameIdx)) {
			if(games.get(gameIdx).isAgainstComputer()) {
				
				Movement move = games.get(gameIdx).getBoard().requestComputerMove(games.get(gameIdx).getPlayers(), games.get(gameIdx));
				//games.get(gameIdx).getBoard().printBoard();
				//customizeCoordinatesToUser(move, gameIdx);
				games.get(gameIdx).setLastMove(move);
				games.get(gameIdx).changeTurn();
				
			}else 
			{
				if(!  ((games.get(gameIdx).getTurn() && playerIdx == 0) ||
						(!games.get(gameIdx).getTurn() && playerIdx == 1)	))
					games.get(gameIdx).wait();
			}
		}
		
		return 	customizeCoordinatesToUser(games.get(gameIdx).getLastMove(), gameIdx);
	}
	
	private Movement customizeCoordinatesToUser(Movement move, int gameIdx) {
		Point[] lastMove = move.getLastMove();
		Point[] fixed = new Point[2];
		if(games.get(gameIdx).getCurrentPlayerColor() == Colors.WHITE) {
			fixed[0] = new Point((lastMove[0].x - 2), (lastMove[0].y - 2));
			fixed[1] = new Point((lastMove[1].x - 2), (lastMove[1].y - 2));
		}else {
			fixed[0] = new Point(7 - (lastMove[0].x - 2), (lastMove[0].y - 2));
			fixed[1] = new Point(7 - (lastMove[1].x - 2), (lastMove[1].y - 2));					
		}
		move.setLastMove(fixed);
		return move;
	}

	private Colors getMyColor(int game, int player)  throws RemoteException{
		return games.get(game).getPlayer(player).getColor();
		
	}

	@Override
	public void register(String firstname, String surname, String username, String password, String email,
			String picture) throws RemoteException {
		UserAccount user = new UserAccount(firstname, surname, username, password, email, picture);
		this.db.addToDB(user);

	}

	@Override
	public boolean validateUser(String username, String password) throws RemoteException {
		return this.db.validateUser(username, password);
		
	}
	
	@Override
	public boolean login(String username, String password) {
		return this.db.login(username, password);
	}
	
	@Override
	public boolean logout(String username) {
		return this.db.logout(username);
	}

	@Override
	public boolean isUsernameExists(String username) throws RemoteException {
		return this.db.isUsernameExists(username);
	}

	@Override
	public String getClientImage(String username) throws RemoteException {
		return this.db.getClientImage(username);
	}

	@Override
	public String getOpponentImage(int gameIdx, int playerIdx) throws RemoteException {
		Game game = games.get(gameIdx);
		return this.db.getClientImage(game.getPlayer(playerIdx == 0? 1:0).getUsername());
	}

	@Override
	public List<UserAccount> getHighScoreFromDB() throws RemoteException {
		
		return this.db.getHighScoreFromDB();
	}

	@Override
	public UserAccount getUser(String username) throws RemoteException {
		
		return this.db.getUser(username);
	}
	
	@Override
	public UserAccount getOpponentUser(String username, int gameIdx) throws RemoteException {

		return this.db.getUser(games.get(gameIdx).getOpponentUserName(username));
	}

	@Override
	public int getCoinsAmount(String username) throws RemoteException {
		
		return this.db.getCoinsAmount(username);
	}

	@Override
	public long lastFreeCoinsPickup(String username) throws RemoteException {
		return this.db.lastFreeCoinsPickup(username);
	}
	
	@Override
	public long updateFreeCoinsPickUp(String username) throws RemoteException {
		return this.db.updateFreeCoinsPickUp(username);
	}

	@Override
	public void addToCoinsAmount(String username, int amount) throws RemoteException {
		this.db.addToCoinsAmount(username, amount);
		
	}
	
	@Override
	public void updateLose(String username, int gameIdx) throws RemoteException{
		this.db.updateLose(username, games.get(gameIdx).getAmount());
		
	}
	
	@Override
	public void updateWin(String username, int gameIdx) throws RemoteException{
		this.db.updateWin(username, games.get(gameIdx).getAmount());
		
	}
	
	@Override
	public void retire(String username, int gameIdx) throws RemoteException {
		synchronized(games.get(gameIdx).getPlayers()) {
			games.get(gameIdx).setRetiredPlayer(username);
			games.get(gameIdx).getPlayers().notifyAll();
		}
	}
	
	@Override
	public boolean isOponnentRetire(String myUsername, int gameIdx) throws InterruptedException {
		synchronized(games.get(gameIdx).getPlayers()) {
			games.get(gameIdx).getPlayers().wait();
			if(games.get(gameIdx).getRetiredPlayer().equals(myUsername))
				return false;
			return true;
		}
	}
	
	@Override
	public void updateDraw(String username) throws RemoteException{
		this.db.updateDraw(username);
		
	}

//	@Override
//	public boolean isMyTool(int gameIdx, int playerIdx, int col, int row) throws RemoteException {
//		Player player = games.get(gameIdx).getPlayer(playerIdx);
//		if(player.getBoard().getTool(col, row).getColor() == player.getColor())
//			return true;
//		return false;
//	}
	
	@Override
	public boolean takeSeat(String username, int amount) {
		return this.db.takeSeat(username, amount);
	}

	@Override
	public boolean isEnoughCoins(String username, int amount) throws RemoteException {
		return this.db.isEnoughCoins(username, amount);
	}
	
}