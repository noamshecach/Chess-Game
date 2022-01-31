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
import com.noam.jpa_project.Server.UserAccount;
import SentObjects.AvailableMoves;
import SentObjects.InitialSetup;
import SentObjects.Movement;
import RMI.*;

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
	
	public Movement requestOpponentMove() {
		try {
			return serverService.requestOpponentMove(gameIdx, playerIdx);
		} catch (RemoteException | InterruptedException e) { e.printStackTrace(); return null;}
	}
	
	public AvailableMoves getPossibleMoves(int col, int row){
		try {
			return serverService.getPossibleMoves(col, row, gameIdx, playerIdx);
		} catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
	public Movement performeMove(Point curSq, Point toSq) {
		try {
			return serverService.performeMove(curSq,toSq, gameIdx, playerIdx);
		} catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
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
	
	public boolean logout(String username) {
		try {
			return serverService.logout(username);
		} catch (RemoteException e) { e.printStackTrace(); return false; }
	}
	
	public boolean takeSeat(String username, int amount) {
		try {
			return serverService.takeSeat(username, amount);
		} catch (RemoteException e) { e.printStackTrace(); return false; }
	}
	
	public boolean isEnoughCoins(String username, int amount) {
		try {
			return serverService.isEnoughCoins(username, amount);
		} catch (RemoteException e) { e.printStackTrace(); return false; }
	}
	
	public void register(String firstname, String surname, String username, String password, 
			String email,String picture) {
		 try {
			serverService.register(firstname, surname, username, password, email, picture);
		} catch (RemoteException e) { e.printStackTrace(); }
	}
	
	public boolean isUsernameExists(String username) {
		try {
			return serverService.isUsernameExists(username);
		} catch (RemoteException e) { e.printStackTrace(); return false; }
	}
	
	public String getClientImage() {
		try {
			return serverService.getClientImage(username);
		} catch (RemoteException e) { e.printStackTrace();  return "";}
	}
	
	public String getOpponentImage() {
		try {
			return serverService.getOpponentImage(gameIdx, playerIdx);
		} 
		catch (RemoteException e) { e.printStackTrace();  return null;}
	}
	
	public List<UserAccount> getHighScoreFromDB() {
		try {
			return serverService.getHighScoreFromDB();
		} 
		catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
	public UserAccount getUser() {
		try {
			return serverService.getUser(username);
		} 
		catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
	public UserAccount getOpponentUser() {
		try {
			return serverService.getOpponentUser(username, gameIdx);
		} 
		catch (RemoteException e) { e.printStackTrace(); return null;}
	}
	
	public int getCoinsAmount() {
		try {
			return serverService.getCoinsAmount(username);
		} catch (RemoteException e) { e.printStackTrace(); return 0;}
	}
	
	public long lastFreeCoinsPickup() {
		try {
			return serverService.lastFreeCoinsPickup(username);
		} 
		catch (RemoteException e) { e.printStackTrace(); return 0;}
	}
	
	public long updateFreeCoinsPickUp() {
		try {
			return serverService.updateFreeCoinsPickUp(username);
		} 
		catch (RemoteException e) { e.printStackTrace(); return 0;}
	}
	
	public void addToCoinsAmount(int amount) {
		try {
			serverService.addToCoinsAmount(username, amount);
		} catch (RemoteException e) {e.printStackTrace(); }
	}

	public void displayChessMateMessage(boolean isWin) throws RemoteException {
		if(isWin) 
			serverService.updateWin(username, gameIdx);
		else
			serverService.updateLose(username, gameIdx);	
	}
	
	public void retire() throws RemoteException{
		serverService.retire(username, gameIdx);
	}
	
	public void isOpponentRetire() throws InterruptedException, RemoteException {
		serverService.isOponnentRetire(username, gameIdx);
	}

	public void displayDrawMessage() throws RemoteException {
		serverService.updateDraw(username);
	}
	
}
