package Tools;

import java.awt.Point;

import Logic.Colors;

/**
 * this class extends from the class pawn.
 * she treat the situation of dummy pawn (special move: en-passent).
 * @author Administrator
 *
 */

public class DummyPawn extends Pawn {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point realPawn;
	
	public DummyPawn(Colors color, Point location) {
		super(color,location);
		this.realPawn = new Point(location.x - 1, location.y );
		
	}
	
//	public DummyPawn(DummyPawn tool) {
//		super(tool);
//	}
//	
//	public DummyPawn(DummyPawn tool, String s) {
//		color = tool.color;
//		location = new Point(11 - tool.location.x, 11- tool.location.y);
//		directions = tool.directions;
//		for(Point p : tool.canMoveTo) 
//			this.canMoveTo.add(new Point(11 - p.x, 11 - p.y));
//		for(Point p : tool.threatsOnMe) 
//			this.threatsOnMe.add(new Point(11 - p.x, 11 - p.y));
//		for(Point p : tool.Iprotect) 
//			this.Iprotect.add(new Point(11 - p.x, 11 - p.y));
//		if(tool.realPawn != null)
//			realPawn = new Point(11 - tool.realPawn.x, 11 - tool.realPawn.y);
//	}

	public Point getRealPawn() {
		return realPawn;
	}
	public void setRealPawn(Point realPawn) {
		this.realPawn = realPawn;
	}
	
//	@Override
//	public Tool clone() {
//		return new DummyPawn(this);
//	}
//
//	@Override
//	public Tool reflect() {
//		return new DummyPawn(this, "");
//	}
	
	@Override
	public String toString() {
		return "|Du|";
	}
}
