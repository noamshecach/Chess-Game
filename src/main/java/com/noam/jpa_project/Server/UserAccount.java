package com.noam.jpa_project.Server;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity(name = "Users")
@Table(name = "Users")
@NamedQueries({
	@NamedQuery(
			name = "orderByNumberOfWins",
			query = "SELECT u "
				  + "FROM Users u "
				+   "ORDER BY u.numberOfWins DESC")
	
	
	
})
public class UserAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//personal information

	@Column(name="FIRST_NAME")
	private String firstname;
	@Column(name= "SURNAME")
	private String surName;
	@Id
	@Column(name= "USER_NAME")
	private String userName;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="E_MAIL")
	private String email;
	@Column(name = "PICTURE")
	private String picture;
	
	//statistics
	@Column(name = "NUMBER_OF_WINS")
	private int numberOfWins;
	@Column(name = "NUMBER_OF_DRAWS")
	private int numberOfDraws;
	@Column(name = "NUMBER_OF_LOSES")
	private int numberOfLoses;
	@Column(name = "NUMBER_OF_GAMES")
	private int numberOfGames;	
	@Column(name = "NUMBER_OF_COINS")
	private int numberOfCoins;	
	
	@Column(name ="LAST_TIME_OF_FREE_COINS")
	private long lastFreeCoinsPickup;
	
	@Column(name = "IS_LOGGED_IN")
	private boolean isLoggedIn = false;



	public UserAccount() {
		
	}
	
	public UserAccount(String firstname, String surname, String username
			, String password, String email, String picture) {
		this.firstname = firstname;
		this.surName = surname;
		this.userName = username;
		this.password = password;
		this.email = email;
		this.picture = picture;
		this.numberOfWins = 0;
		this.numberOfGames = 0;
		this.numberOfDraws = 0;
		this.numberOfLoses = 0;
		this.numberOfCoins = 500;
		Date date = new Date();
		this.lastFreeCoinsPickup = date.getTime();
		this.isLoggedIn = false;
	}
	
	public void setIsLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public int getNumberOfWins() {
		return numberOfWins;
	}

	public void setNumberOfWins(int numberOfWins) {
		this.numberOfWins = numberOfWins;
	}

	public int getNumberOfDraws() {
		return numberOfDraws;
	}

	public void setNumberOfDraws(int numberOfDraws) {
		this.numberOfDraws = numberOfDraws;
	}

	public int getNumberOfLoses() {
		return numberOfLoses;
	}

	public void setNumberOfLoses(int numberOfLoses) {
		this.numberOfLoses = numberOfLoses;
	}

	public int getNumberOfGames() {
		return numberOfGames;
	}

	public void setNumberOfGames(int numberOfGames) {
		this.numberOfGames = numberOfGames;
	}

	public int getNumberOfCoins() {
		return numberOfCoins;
	}

	public void setNumberOfCoins(int numberOfCoins) {
		this.numberOfCoins = numberOfCoins;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public long getLastFreeCoinsPickup() {
		return lastFreeCoinsPickup;
	}

	public void setLastFreeCoinsPickup(long lastFreeCoinsPickup) {
		this.lastFreeCoinsPickup = lastFreeCoinsPickup;
	}
}
