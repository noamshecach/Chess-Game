package Client;

import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.noam.jpa_project.Server.HighScoreData;
import com.noam.jpa_project.Server.UserAccount;
import SentObjects.AvailableMoves;
import SentObjects.InitialSetup;
import SentObjects.Movement;
import RMI.*;

//This class gets the required data from the server via API calls.
public class ContactServer {

	private IServerService serverService;
	private Integer gameIdx, playerIdx;
	private String username;
	
	//Server details
	private String serverIP = "127.0.0.1", service = "chess";
	private int serverPort = 8080; 
	
	public ContactServer()  {
		//Establishing connection to the server
		try { 
			this.serverService = (IServerService) Naming.lookup(MessageFormat.format("rmi://{0}:{1}/{2}", 
					serverIP, Integer.toString(serverPort), service));
		} 
		catch (MalformedURLException e) { e.printStackTrace(); } 
		catch (RemoteException e) { e.printStackTrace(); } 
		catch (NotBoundException e) { e.printStackTrace(); }
	}
	
	//This function notify the server that a user asked to join game table.
	//If there is waiting opponent the game will start right away.
	//Otherwise, the player will wait until another player will join.
	//Returns object of InitialSetup with game details.
	public InitialSetup findOpponent(String username, int tableNumber) {
		try {
			InitialSetup init = serverService.findOpponent(username, tableNumber);
			if(init != null) {
				this.gameIdx = init.getGameIdx();
				this.playerIdx = init.getPlayerIdx();
			}
			return init;
		} 
		catch (RemoteException e) { e.printStackTrace();  return null; } 
		catch (InterruptedException e) { e.printStackTrace(); return null;} 
		catch (ParserConfigurationException e) { e.printStackTrace(); return null; } 
		catch (SAXException e) {e.printStackTrace();  return null; } 
		catch (IOException e) { e.printStackTrace(); return null; }
	}
	
	// This function request the opponent to move and gets his movement details in the Movement object.
	public Movement requestOpponentMove() {
		try {
			return serverService.requestOpponentMove(gameIdx, playerIdx);
		} catch (RemoteException | InterruptedException e) { e.printStackTrace(); return null;}
	}
	
	// Ask the server to get the available moves of the tool that locates in [col, row].
	public AvailableMoves getPossibleMoves(int col, int row){
		try {
			return serverService.getPossibleMoves(col, row, gameIdx, playerIdx);
		} catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
	//This function asks the server to make a move on it's behalf.
	//The influence parameters of this move are returned in the Movement object.
	public Movement performeMove(Point curSq, Point toSq) {
		try {
			return serverService.performeMove(curSq,toSq, gameIdx, playerIdx);
		} catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
	//This function ask the server to validate the user name in the arguments 
	//and returns his response.
	public boolean validateUser(String username, String password) {
		try {
			this.username = username;
			return serverService.validateUser(username, password);
		} catch (RemoteException e) { e.printStackTrace();  return false;}
	}
	
	// Sends login details to the server
	// Returns true if succeeded or false otherwise
	public boolean login(String username, String password) {
		try {
			this.username = username;
			return serverService.login(username, password);
		} catch (RemoteException e) { e.printStackTrace(); return false;}
	}
	
	//This function asks the server to logout the user in the arguments.
	public boolean logout(String username) {
		try {
			return serverService.logout(username);
		} catch (RemoteException e) { e.printStackTrace(); return false; }
	}
	
	//This function asks the server to update the coins amount of the user due to table join.
	public boolean takeSeat(String username, int amount) {
		try {
			return serverService.takeSeat(username, amount);
		} catch (RemoteException e) { e.printStackTrace(); return false; }
	}
	
	//This function checks if the user has enough coins to join to the requested table.
	public boolean isEnoughCoins(String username, int amount) {
		try {
			return serverService.isEnoughCoins(username, amount);
		} catch (RemoteException e) { e.printStackTrace(); return false; }
	}
	
	//This function asks to register user to the application.
	public void register(String firstname, String surname, String username, String password, 
			String email,String picture) {
		 try {
			serverService.register(firstname, surname, username, password, email, picture);
		} catch (RemoteException e) { e.printStackTrace(); }
	}
	
	//Checks if the user name exists in the DB.
	//Returns true if exists or false otherwise.
	public boolean isUsernameExists(String username) {
		try {
			return serverService.isUsernameExists(username);
		} catch (RemoteException e) { e.printStackTrace(); return false; }
	}
	
	// Requests from the server the client image
	// Returns the client image
	public String getClientImage() {
		try {
			return serverService.getClientImage(username);
		} catch (RemoteException e) { e.printStackTrace();  return "";}
	}
	
	//Request the opponent image from the server and returns it.
	public String getOpponentImage() {
		try {
			return serverService.getOpponentImage(gameIdx, playerIdx);
		} 
		catch (RemoteException e) { e.printStackTrace();  return null;}
	}
	
	//Request from the server the high score table details.
	//Returns a list of the users and their data.
	public List<HighScoreData> getHighScoreFromDB() {
		try {
			return serverService.getHighScoreFromDB();
		} 
		catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
	//The user asks for his information from the server.
	//Returns UserAccount object.
	public UserAccount getUser() {
		try {
			return serverService.getUser(username);
		} 
		catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
	//Asks the server for the opponent information.
	public UserAccount getOpponentUser() {
		try {
			return serverService.getOpponentUser(username, gameIdx);
		} 
		catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
	//Asks the server for the amount of coins and returns it.
	public int getCoinsAmount() {
		try {
			return serverService.getCoinsAmount(username);
		} catch (RemoteException e) { e.printStackTrace(); return 0;}
	}
	
	//Asks the server when the last time the user get free coins.
	public long lastFreeCoinsPickup() {
		try {
			return serverService.lastFreeCoinsPickup(username);
		} 
		catch (RemoteException e) { e.printStackTrace(); return 0;}
	}
	
	//Updates the server that the user picked up free coins.
	public long updateFreeCoinsPickUp() {
		try {
			return serverService.updateFreeCoinsPickUp(username);
		} 
		catch (RemoteException e) { e.printStackTrace(); return 0;}
	}
	
	//Request to add 'amount' coins to the user.
	public void addToCoinsAmount(int amount) {
		try {
			serverService.addToCoinsAmount(username, amount);
		} catch (RemoteException e) {e.printStackTrace(); }
	}

	//Ask the server to update wins or lose columns for the user.
	public void displayChessMateMessage(boolean isWin) throws RemoteException {
		if(isWin) 
			serverService.updateWin(username, gameIdx);
		else
			serverService.updateLose(username, gameIdx);	
	}
	
	//Announce the server of user retirement.
	public void retire() throws RemoteException{
		serverService.retire(username, gameIdx);
	}
	
	//Asks the server if the opponent retired.
	public boolean isOpponentRetire() throws InterruptedException, RemoteException {
		return serverService.isOponnentRetire(username, gameIdx);
	}

	//Asks the server to update draw result in the DB for the user.
	public void displayDrawMessage() throws RemoteException {
		serverService.updateDraw(username);
	}
	
}
