package Tools;

import java.awt.Point;
import java.util.List;
import Logic.Colors;
import Logic.Vector;


public class Empty extends Tool implements Cloneable
{
	
	private static final long serialVersionUID = 1L;

	public Empty(Point location)
	{
		super(Colors.WHITE, location);
	}
	
	public Empty(Point location, String picName, List<Vector> threats, List<Vector> protects,List<Point> pawnsCanReach, Colors srcColor)
	{
		super(srcColor, location);
		threatsOnMe = threats;
		protectedByOthers = protects;
		this.pawnsCanReach = pawnsCanReach;
		
	}
	
	public void addRelationship(Tool[][] tools, Point dir, Point threat, Colors srcColor) { 
		if(srcColor == color)
			addProtectedByOthers(tools[location.x][location.y], new Vector(threat, dir)); 
		else
			addThreatOnMe(tools[location.x][location.y], new Vector(threat, dir));  
	}
	
	public void removeRelationship(Tool[][] tools, Point threatIdx, Colors srcColor) { 
		if(srcColor == color) 
			removeProtectedByOthers(this, threatIdx);
		else
			removeThreatOnMe(this, threatIdx);
	}
	
	public boolean hasThreats(Colors srcColor) {
		if(srcColor == color) 
			return threatsOnMe.size() > 0 ? true: false;
		else
			return protectedByOthers.size() > 0 ? true: false;
	}
	
	public Empty clone() throws CloneNotSupportedException {
		Empty e = (Empty) super.clone();
		return e;
	}
	
	public void updateLists(Tool[][] tools) {
		
	}
	
//	public String details() {
//		String s = "\nEmpty location" + "[" + (location.x - 2) + "," + (location.y - 2) + "]  my color: " + (color==Colors.WHITE? "white":"black") + "\npossible Moves: ";
//		s+= "\nThreats on me: ";
//		for(Vector v: threatsOnMe) {
//			s += "[" + (v.getSource().x - 2) + "," + (v.getSource().y - 2) + "] ";
//		}
//		s+= "\nProteted by: ";
//		for(Vector v: protectedByOthers) {
//			s += "[" + (v.getSource().x - 2) + "," + (v.getSource().y - 2) + "] ";
//		}
//		return s;
//	}
	
	@Override
	public String toString() {
		return "|Em|"+ location.x + " " + location.y;
	}
}
