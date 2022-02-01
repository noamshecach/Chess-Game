package com.noam.jpa_project.Server;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

//This class access the database.
public class DataBaseAccess implements Serializable {

	private static final long serialVersionUID = 1L;
	EntityManagerFactory emf;
	
	public DataBaseAccess() {
		this.emf = Persistence.createEntityManagerFactory("my-pu");		
	}
	
	//Adds user name to the DB
	public void addToDB(UserAccount user) {
		synchronized(user) {
			EntityManager em = emf.createEntityManager();
			EntityTransaction entityTransaction = null;
			try {
				entityTransaction = em.getTransaction();
				entityTransaction.begin();
				em.persist(user);
				entityTransaction.commit();
				em.close();
				
			}catch(RuntimeException e) {
				if(entityTransaction.isActive())
					entityTransaction.rollback();
				throw e;
			}
		}
	}
	
	//Gets the image of the username from the database (String) and returns it. 
	public String getClientImage(String username) {
		UserAccount user = getUser(username);
		return user.getPicture();
	}
	
	//Checks if the user name exists in the database.
	//Return true if it exists or false otherwise.
	public boolean isUsernameExists(String username) {
		UserAccount user = getUser(username);
		if(user != null)
			return true;
		return false;
	}
	
	//Checks if the username and password are correct.
	//Return true if it is or false otherwise.
	public boolean validateUser(String username, String password) {
		UserAccount user = getUser(username);
		if(user != null && user.getPassword().equals(password) ) 
			return true;
		return false;
	}
		
	//Checks if the username and password are correct.
	//If it is then log the user in and return true.
	//Otherwise, returns false.
	public boolean login(String username, String password) {
		UserAccount user = getUser(username);
		if(user != null && user.getPassword().equals(password) && !user.getIsLoggedIn() && setIsLoggedIn(username, true))
			return true;
		return false;
	}
	
	//Checks if the username is exists and logged in.
	//If it is then log him out and returns true.
	//Otherwise, returns false.
	public boolean logout(String username) {
		UserAccount user = getUser(username);
		if(user != null  && user.getIsLoggedIn() && setIsLoggedIn(username, false)) 
			return true;
		return false;
	}
	
	//The search in the DB is case sensitive.
	//This functions look for 'username' in the database.
	//if the search succeeded then it returns true. 
	//otherwise, it returns false.
	private UserAccount getUserCaseSensetive(String username) {
		synchronized(username) {
			EntityManager em = emf.createEntityManager();
			EntityTransaction entityTransaction = null;
			
			try {
				entityTransaction = em.getTransaction();
				entityTransaction.begin();
				UserAccount user = em.find(UserAccount.class, username );
				entityTransaction.commit();
				em.close();
				return user;
			}catch(RuntimeException e) {
				if(entityTransaction.isActive())
					entityTransaction.rollback();
				return null;
			}
		}
	}
	
	//This function checks if the user exists in the database (in upper case or in lower case)
	//Returns the user if exists or null otherwise.
	public UserAccount getUser(String username) {
		UserAccount userUpperCase = getUserCaseSensetive(username.toUpperCase());
		UserAccount userLowerCase = getUserCaseSensetive(username.toLowerCase());
		if(userUpperCase != null )
			return userUpperCase;
		if(userLowerCase != null)
			return userLowerCase;
		return null;
	}
	
	//Returns the number of coins the user have.
	public int getCoinsAmount(String username) {
		UserAccount user = getUser(username);
		return user.getNumberOfCoins();
	}
	
	//This function returns the relevant data for making high score table.
	//The return type is a list of HighScoreData objects.
	public List<HighScoreData> getHighScoreFromDB() {
		List<HighScoreData> highScore;
		synchronized(emf) {
			EntityManager em = emf.createEntityManager(); 
			
			 String queryStr =
			 "SELECT NEW com.noam.jpa_project.Server.HighScoreData(c.userName, c.picture, c.numberOfWins) " +
			 "FROM Users AS c "
			+"ORDER BY c.numberOfWins DESC";
			 TypedQuery<HighScoreData> query = em.createQuery(queryStr, HighScoreData.class);

			 highScore = query.getResultList();
		}
		return highScore;
	}
	
	//Returns the time (cast to long) of which the user last got free coins.
	public long lastFreeCoinsPickup(String username) {
		UserAccount user = getUser(username);
		return user.getLastFreeCoinsPickup();
	}
	
	//Returns true if the user has more coins than 'amount'.
	//Returns false otherwise.
	public boolean isEnoughCoins(String username, int amount) {
		UserAccount user = getUser(username);
		if(user != null && user.getNumberOfCoins() >= amount) 
			return true;
		return false;
	}
	
	//If the user has more than 'amount' coins then decrease 'amount' from his coins and return true.
	//Otherwise return false.
	public boolean takeSeat(String username, int amount) {
		UserAccount user = getUser(username);
		if(user != null && user.getNumberOfCoins() >= amount && decreaseCoins(username, amount)) 
			return true;
		return false;
	}
	
	//This function decrease the number 'amount' from 'numberOfCoins' of the user.
	//Returns true if succeeded or false otherwise.
	private boolean decreaseCoins(String username, int amount) {
		synchronized(username) {
			EntityManager em = emf.createEntityManager();
			EntityTransaction entityTransaction = null;
			try {
				entityTransaction = em.getTransaction();
				entityTransaction.begin();
				UserAccount user = em.find(UserAccount.class, username);
				user.setNumberOfCoins(user.getNumberOfCoins() - amount);
				
				entityTransaction.commit();
				em.close();
				return true;
			}catch(RuntimeException e) {
				if(entityTransaction.isActive())
					entityTransaction.rollback();
				return false;
			}
		}
	}
	
	//Checks if the user name is logged in.
	//Returns true if he yes or false otherwise.
	private boolean setIsLoggedIn(String username, boolean isLoggedIn) {
		synchronized(username) {
			EntityManager em = emf.createEntityManager();
			EntityTransaction entityTransaction = null;
			try {
				entityTransaction = em.getTransaction();
				entityTransaction.begin();
				UserAccount user = em.find(UserAccount.class, username);
				user.setIsLoggedIn(isLoggedIn);
				
				entityTransaction.commit();
				em.close();
				return true;
			}catch(RuntimeException e) {
				if(entityTransaction.isActive())
					entityTransaction.rollback();
				return false;
			}
		}
	}
	
	//The user got free coins therefore this function updates this field in the DB to the current time.
	//Returns the new time.
	public long updateFreeCoinsPickUp(String username) {
		synchronized(username) {
			EntityManager em = emf.createEntityManager();
			EntityTransaction entityTransaction = null;
			try {
				entityTransaction = em.getTransaction();
				entityTransaction.begin();
				UserAccount user = em.find(UserAccount.class, username);
				// change
				Date date = new Date();
				user.setLastFreeCoinsPickup(date.getTime());
				
				entityTransaction.commit();
				em.close();
				return date.getTime();
			}catch(RuntimeException e) {
				if(entityTransaction.isActive())
					entityTransaction.rollback();
				throw e;
			}
		}
	}
	
	//Adds coins to the account of the user.
	public void addToCoinsAmount(String username, int amount) {
		synchronized(username) {
			EntityManager em = emf.createEntityManager();
			EntityTransaction entityTransaction = null;
			try {
				entityTransaction = em.getTransaction();
				entityTransaction.begin();
				UserAccount user = em.find(UserAccount.class, username);
				// change
				user.setNumberOfCoins(user.getNumberOfCoins() + amount);
				
				entityTransaction.commit();
				em.close();
				
			}catch(RuntimeException e) {
				if(entityTransaction.isActive())
					entityTransaction.rollback();
				throw e;
			}
		}
	}
	
	//This function is triggered when the user has lost.
	//It updates the coins amount, games played and games lost fields.
	public void updateLose(String username, int amount) {
		synchronized(username){
			EntityManager em = emf.createEntityManager();
			EntityTransaction entityTransaction = null;
			try {
				entityTransaction = em.getTransaction();
				entityTransaction.begin();
				UserAccount user = em.find(UserAccount.class, username);
				// change
				user.setNumberOfCoins(user.getNumberOfCoins() - amount);
				user.setNumberOfGames(user.getNumberOfGames() + 1);
				user.setNumberOfLoses(user.getNumberOfLoses() + 1);
				
				entityTransaction.commit();
				em.close();
				
			}catch(RuntimeException e) {
				if(entityTransaction.isActive())
					entityTransaction.rollback();
				throw e;
			}
		}
	}
	
	//This function is triggered when the user has won.
	//It updates the coins amount, games played and games won fields.
	public void updateWin(String username, int amount) {
		synchronized(username){
			EntityManager em = emf.createEntityManager();
			EntityTransaction entityTransaction = null;
			try {
				entityTransaction = em.getTransaction();
				entityTransaction.begin();
				UserAccount user = em.find(UserAccount.class, username);
				// change
				user.setNumberOfCoins(user.getNumberOfCoins() + amount);
				user.setNumberOfGames(user.getNumberOfGames() + 1);
				user.setNumberOfWins(user.getNumberOfWins() + 1);
				
				entityTransaction.commit();
				em.close();
				
			}catch(RuntimeException e) {
				if(entityTransaction.isActive())
					entityTransaction.rollback();
				throw e;
			}
		}
	}
	
	//This function is triggered when the user has finished in draw.
	//It updates the coins amount, games played and numberOfDraw fields.
	public void updateDraw(String username) {
		synchronized(username){
			EntityManager em = emf.createEntityManager();
			EntityTransaction entityTransaction = null;
			try {
				entityTransaction = em.getTransaction();
				entityTransaction.begin();
				UserAccount user = em.find(UserAccount.class, username);
				// change
				user.setNumberOfGames(user.getNumberOfGames() + 1);
				user.setNumberOfDraws(user.getNumberOfDraws() + 1);
				
				entityTransaction.commit();
				em.close();
				
			}catch(RuntimeException e) {
				if(entityTransaction.isActive())
					entityTransaction.rollback();
				throw e;
			}
		}
	}
}
