package SentObjects;

import java.awt.Point;
import java.io.Serializable;
import Frames.Check;
import Frames.Mate;

public class Movement implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 1L;
	private Mate isMate = Mate.NONE;
	private Check isChess = Check.NONE;
	private boolean isRooking = false;
	private boolean isPromotion = false;
	private Point[] lastMove;
	
	public Movement(Point[] lastMove, Mate isMate, Check isChess, boolean isRooking, boolean isPromotion) {
		this.isMate = isMate;
		this.isChess = isChess;
		this.lastMove = lastMove;
		this.isRooking = isRooking;
		this.isPromotion = isPromotion;
	}
	
	public boolean isPromotion() {
		return isPromotion;
	}
	
	public boolean isRooking() {
		return isRooking;
	}

	public Check isChess() {
		return isChess;
	}

	public Mate isMate() {
		return isMate;
	}
	
	public Point[] getLastMove() {
		return lastMove;
	}
	
	public void setLastMove(Point[] lastMove) {
		this.lastMove = lastMove;
	}
	
	public Object clone() throws CloneNotSupportedException {
		Movement m = (Movement) super.clone();
		m.isMate = isMate;
		m.isChess = isChess;
		m.lastMove = new Point[2];
		for(int i = 0; i < lastMove.length; i++) {
			m.lastMove[i] = new Point();
			m.lastMove[i].x = lastMove[i].x;
			m.lastMove[i].y = lastMove[i].y;
		}
		
		return m;
	}

}
