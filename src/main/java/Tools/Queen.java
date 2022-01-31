package Tools;
import java.awt.Point;
import java.util.List;

import Logic.Colors;
import Logic.Vector;

/**
 * this class (queen) extends from the class tool.
 * all the functions are realization of the abstract 
 * functions on the tool class.
 * @author Administrator
 *
 */

public class Queen extends LinearTool implements Cloneable {
	
	private static final long serialVersionUID = 1L;

	public Queen(Colors color, Point location)
	{
		super(color, location);
		directions = new Point[8];
		directions[0] = new Point(0,1);
		directions[1] = new Point(1,1);
		directions[2] = new Point(1,0);
		directions[3] = new Point(1,-1);
		directions[4] = new Point(0,-1);
		directions[5] = new Point(-1,-1);
		directions[6] = new Point(-1,0);
		directions[7] = new Point(-1,1);
		myValue = 90;
		if(color == Colors.WHITE)
			id = 9;
		else
			id = 10;
	}
	
	public Queen(Colors color, Point location, List<Vector> threatsOnMe, List<Vector> protectedByOthers, int numberOfMoves)
	{
		this(color,location);
		this.threatsOnMe = threatsOnMe;
		this.protectedByOthers = protectedByOthers;
		this.numberOfMoves = numberOfMoves;
	}
	
	@Override
	public Queen clone() throws CloneNotSupportedException {
		Queen q =  (Queen) super.clone();
		return q;
	}
	
//	public String details() {
//		String s = "\nQueen location" + "[" + (location.x - 2) + "," + (location.y - 2) +"]  my color: " + (color==Colors.WHITE? "white":"black") + "\npossible Moves: ";
//		for(Point p: canMoveTo) {
//			s+= "[" + (p.x - 2) + "," + (p.y - 2) + "] ";
//		}
//		s += "\nI Threat: ";
//		for(Vector v: Ithreat) {
//			s += "[" + (v.getSource().x - 2) + "," + (v.getSource().y - 2) + "] ";
//		}
//		s+= "\nI protect: ";
//		for(Vector v: Iprotect) {
//			s += "[" + (v.getSource().x - 2) + "," + (v.getSource().y - 2) + "] ";
//		}
//		s+= "\nThreats on me: ";
//		for(Vector v: threatsOnMe) {
//			s += "[" + (v.getSource().x - 2) + "," + (v.getSource().y - 2) + "] ";
//		}
//		s+= "\nProteted by: ";
//		for(Vector v: protectedByOthers) {
//			s += "[" + (v.getSource().x - 2) + "," + (v.getSource().y - 2) + "] ";
//		}
//		
//		return s;
//	}
	
	@Override
	public String toString() {
		return "|Qu|"+ location.x + " " + location.y;
	}
}
