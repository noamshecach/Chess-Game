package Tools;
import java.awt.Point;
import Logic.Colors;
import MoveTypes.BishopDir;
/**
 * this class (bishop) extends from the class tool.
 * all the functions are realization of the abstract 
 * functions on the tool class.
 * @author Administrator
 *
 */


public class Bishop extends LinearTool{

	private static final long serialVersionUID = 1L;

	public Bishop(Colors color, Point location)
	{
		super(color, location);
		directions  = new Point[4];
		directions[0] =	new BishopDir(1,1);
		directions[1] =	new BishopDir(1,-1);
		directions[2] =	new BishopDir(-1,1);
		directions[3] =	new BishopDir(-1,-1);
		myValue = 35;
		if(color == Colors.WHITE)
			id = 5;
		else
			id = 6;
	}
	
	@Override
	public Bishop clone() throws CloneNotSupportedException {
		Bishop b =  (Bishop) super.clone();
		return b;
	}
	
//	public String details() {
//		String s = "\nBishop location" + "[" + (location.x - 2) + "," + (location.y - 2) + "]  my color: " + (color==Colors.WHITE? "white":"black") + "\npossible Moves: ";
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
//		return s;
//	}
	
	@Override
	public String toString() {
		return "|Bi| " + location.x + " " + location.y;
	}
}
