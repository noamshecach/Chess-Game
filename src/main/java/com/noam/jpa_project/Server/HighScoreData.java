package com.noam.jpa_project.Server;

import java.io.Serializable;

//This class includes the necessary fields for making high score table.
//Therefore when accessing the database only those fields get back to the user.
public class HighScoreData implements Serializable {

	private static final long serialVersionUID = 1L;
	String userName;
	String picture;
	int numberOfWins;

	public HighScoreData(String username, String picture, int numberOfWins) {
		this.userName = username;
		this.picture = picture;
		this.numberOfWins = numberOfWins;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getNumberOfWins() {
		return numberOfWins;
	}

	public void setNumberOfWins(int numberOfWins) {
		this.numberOfWins = numberOfWins;
	}
}
