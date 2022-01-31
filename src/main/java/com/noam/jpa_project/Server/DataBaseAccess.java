package com.noam.jpa_project.Server;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DataBaseAccess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EntityManagerFactory emf;
	
	public DataBaseAccess() {
		this.emf = Persistence.createEntityManagerFactory("my-pu");		
	}
	
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
	
	public String getClientImage(String username) {
		UserAccount user = getUser(username);
		return user.getPicture();
	}
	
	public boolean isUsernameExists(String username) {
		UserAccount user = getUser(username);
		if(user != null)
			return true;
		return false;
	}
	
	public boolean validateUser(String username, String password) {
		UserAccount user = getUser(username);
		if(user != null && user.getPassword().equals(password) ) 
			return true;
		return false;
	}
		
	public boolean login(String username, String password) {
		UserAccount user = getUser(username);
		if(user != null && user.getPassword().equals(password) && !user.getIsLoggedIn() && setIsLoggedIn(username, true))
			return true;
		return false;
	}
	
	public boolean logout(String username) {
		UserAccount user = getUser(username);
		if(user != null  && user.getIsLoggedIn() && setIsLoggedIn(username, false)) 
			return true;
		return false;
	}
	
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
	
	public UserAccount getUser(String username) {
		UserAccount userUpperCase = getUserCaseSensetive(username.toUpperCase());
		UserAccount userLowerCase = getUserCaseSensetive(username.toLowerCase());
		if(userUpperCase != null )
			return userUpperCase;
		if(userLowerCase != null)
			return userLowerCase;
		return null;
	}
	
	public int getCoinsAmount(String username) {
		UserAccount user = getUser(username);
		return user.getNumberOfCoins();
	}
	
	public List<UserAccount> getHighScoreFromDB() {
		List<UserAccount> highScore;
		synchronized(emf) {
			EntityManager em = emf.createEntityManager(); 
			 TypedQuery<UserAccount> query = em.createNamedQuery("orderByNumberOfWins", UserAccount.class);
			 highScore = query.getResultList();
			for(UserAccount user: highScore)
				System.out.println(user);
		}
		return highScore;
	}
	
	public long lastFreeCoinsPickup(String username) {
		UserAccount user = getUser(username);
		return user.getLastFreeCoinsPickup();
	}
	
	public boolean isEnoughCoins(String username, int amount) {
		UserAccount user = getUser(username);
		if(user != null && user.getNumberOfCoins() >= amount) 
			return true;
		return false;
	}
	
	public boolean takeSeat(String username, int amount) {
		UserAccount user = getUser(username);
		if(user != null && user.getNumberOfCoins() >= amount && decreaseCoins(username, amount)) 
			return true;
		return false;
	}
	
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
