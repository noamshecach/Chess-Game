package RMI;

import java.awt.Point;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.noam.jpa_project.Server.UserAccount;
import SentObjects.AvailableMoves;
import SentObjects.InitialSetup;
import SentObjects.Movement;

// ------------This is the server API------------------
// This interface includes methods that the client can use 
// in order to request perform actions or get data from the server.

public interface IServerService extends Remote {
	
	AvailableMoves getPossibleMoves(int col, int row, int game, int player) throws RemoteException;
	
	Movement performeMove(Point curSq, Point toSq, int game, int player) throws RemoteException;
	
	void register(String firstname, String surname, String username
			, String password, String email, String picture) throws RemoteException;
	
	boolean validateUser(String username, String password) throws RemoteException;
	
	boolean isUsernameExists(String username) throws RemoteException;
	
	String getClientImage(String username) throws RemoteException;
	
	String getOpponentImage(int game, int player) throws RemoteException;
	
	List<UserAccount> getHighScoreFromDB() throws RemoteException;
	
	UserAccount getUser(String username) throws RemoteException;
	
	UserAccount getOpponentUser(String username, int gameIdx) throws RemoteException;
	
	int getCoinsAmount(String username) throws RemoteException;
	
	long lastFreeCoinsPickup(String username) throws RemoteException;
	
	long updateFreeCoinsPickUp(String username) throws RemoteException;
	
	void addToCoinsAmount(String username, int amount) throws RemoteException;
	
	public void updateWin(String username, int amount) throws RemoteException;
	
	public void updateLose(String username, int amount) throws RemoteException;
	
	public void updateDraw(String username) throws RemoteException;
	
//	public boolean isMyTool(int gameIdx, int playerIdx, int col, int row) throws RemoteException;

	public boolean login(String username, String password) throws RemoteException;

	public boolean logout(String username) throws RemoteException;

	boolean takeSeat(String username, int amount) throws RemoteException;
	
	boolean isEnoughCoins(String username, int amount) throws RemoteException;

	public void retire(String username, int gameIdx) throws RemoteException;

	InitialSetup findOpponent(String username, int tableNumber) throws RemoteException, InterruptedException, ParserConfigurationException, SAXException, IOException;

	Movement requestOpponentMove(int gameIdx, int playerIdx) throws RemoteException, InterruptedException;

	boolean isOponnentRetire(String myUsername, int gameIdx) throws RemoteException, InterruptedException;
	
}
