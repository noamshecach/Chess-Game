package Tools;
import java.awt.Point;
import Logic.Colors;
import MoveTypes.MoveAndStop;

/**
 * this class (knight) extends from the class tool.
 * all the functions are realization of the abstract 
 * functions on the tool class.
 * @author Administrator
 *
 */

public class Knight extends OneStepTool implements MoveAndStop, Cloneable {
	

	private static final long serialVersionUID = 1L;

	public Knight(Colors color, Point location)
	{
		super(color, location);
		directions = new Point[8];
		directions[0] = new Point(2,1);
		directions[1] = new Point(2,-1);
		directions[2] = new Point(-2,1);
		directions[3] = new Point(-2,-1);
		directions[4] = new Point(1,2);
		directions[5] = new Point(1,-2);
		directions[6] = new Point(-1,2);
		directions[7] = new Point(-1,-2);
		myValue = 30;
		if(color == Colors.WHITE)
			id = 7;
		else
			id = 8;
	}
	
	@Override
	public Knight clone() throws CloneNotSupportedException {
		Knight k =  (Knight) super.clone();
		return k;
	}
	
//	public String details() {
//		String s = "\nKnight location" + "[" + (location.x - 2) + "," + (location.y - 2) + "]  my color: " + (color==Colors.WHITE? "white":"black") + "\npossible Moves: ";
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
		return "|Kn|"+ location.x + " " + location.y;
	}
}
