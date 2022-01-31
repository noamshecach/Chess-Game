package SentObjects;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AvailableMoves implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Point> canMoveTo;
	private List<Point> canEat;
	
	public AvailableMoves( ) {
		this.canMoveTo = new ArrayList<Point>();
		this.canEat = new ArrayList<Point>();
	}
	
	public void addCanMoveTo(Point square) {
		canMoveTo.add(square);
	}
	
	public void addCanEat(Point square) {
		canEat.add(square);
	}
	
	public List<Point> getCanMoveTo() {
		return this.canMoveTo;
	}
	
	public List<Point> getCanEat(){
		return this.canEat;
	}
	
	public void setCanMoveTo(List<Point> canMoveTo) {
		this.canMoveTo = canMoveTo;
	}
	
	public void setCanEat(List<Point> canEat) {
		this.canEat = canEat;
	}
}
