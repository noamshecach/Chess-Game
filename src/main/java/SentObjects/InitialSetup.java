package SentObjects;

import java.io.Serializable;
import java.util.List;

import javax.swing.ImageIcon;
import Logic.Colors;

public class InitialSetup implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<AbstractTool> tools;
	private ImageIcon opponentPic;
	private String opponentName;
	private Colors myColor;
	private boolean isMyTurn;
	private int gameIdx, playerIdx;
	
	public InitialSetup(List<AbstractTool> tools, boolean turn, Colors color, ImageIcon opponentPic, String opponentName, int gameIdx, int playerIdx) {
		this.isMyTurn = turn;
		this.tools = tools;
		this.myColor = color;
		this.opponentPic = opponentPic;
		this.opponentName = opponentName;
		this.gameIdx = gameIdx;
		this.playerIdx = playerIdx;
	}
	
	public int getGameIdx() {
		return this.gameIdx;
	}
	
	public int getPlayerIdx() {
		return this.playerIdx;
	}
	
	public ImageIcon getOpponentImage() {
		return this.opponentPic;
	}
	
	public String getOpponentName() {
		return this.opponentName;
	}
	
	public List<AbstractTool> getTools(){
		return this.tools;
	}
	
	public boolean getTurn() {
		return this.isMyTurn;
	}
	
	public Colors getColor() {
		return this.myColor;
	}
	
}
