package Logic;

import java.io.Serializable;

//Represents player in the game
public class Player implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private Colors myColor;
	private String username;
	private boolean isComputer = false;
	
	public Player(String username, boolean isComputer){
		this.username = username;
		this.isComputer = isComputer;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public boolean isCompterPlayer() {
		return isComputer;
	}

	public void setCompterPlayer(boolean compterPlayer) {
		this.isComputer = compterPlayer;
	}
	
	public void setColor(Colors color) {
		this.myColor = color;
	}
	
	public Colors getColor() {
		return myColor;
	}
	
	public Colors oppositeColor() {
		if(myColor == Colors.WHITE)
			return Colors.BLACK;
		else
			return Colors.WHITE;
	}
	
	public Object clone() throws CloneNotSupportedException {
		Player p = (Player) super.clone();

		p.myColor = myColor;
		p.username = username; 
		p.isComputer = isComputer;
		
		return p;
	}
}
