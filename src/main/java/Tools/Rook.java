package Tools;
import java.awt.Point;

import Logic.Colors;
import Logic.Vector;

public class Rook extends LinearTool implements Cloneable  {
	
	private static final long serialVersionUID = 1L;
	private boolean alreadyMoved = false;
	
	public Rook(Colors color, Point location)
	{
		super(color, location);
		directions = new Point[4];
		directions[0] = new Point(0,1);
		directions[1] = new Point(0,-1);
		directions[2] = new Point(-1,0);
		directions[3] = new Point(1,0);
		myValue = 50;
		setId();
	}
	
	public void setId() {
		if(alreadyMoved == false) {
			if(color == Colors.WHITE)
				id = 3; // white rook & castling is possible
			else
				id = 4; //black rook & castling is possible
		}
		else{
			if(color == Colors.WHITE)
				id = 15; // white rook & castling is not possible
			else
				id = 16; //black rook & castling is not possible
		}
	}
	
	public void moveTo(Tool[][] tools, Point destination) {
		
		alreadyMoved = true;
		setId();
//		System.out.println("removeFromBoard:");
		removeFromBoard(tools);
//		System.out.println("placeAt: ");
		placeAt(tools, destination);
		this.numberOfMoves++;
	}
 	
	@Override
 	public void goBackTo(Tool[][] tools, Point destination) {
		if(numberOfMoves > 0) {
			this.numberOfMoves--;
			if(numberOfMoves == 0) {
				alreadyMoved = false;
				setId();
			}
//			System.out.println("removeFromBoard:");
			removeFromBoard(tools);
//			System.out.println("placeAt: ");
			placeAt(tools, destination);
		}
	}
	
	public boolean isAlreadyMove() {
		return alreadyMoved;
	}
	public void setAlreadyMove(boolean alreadyMove) {
		alreadyMoved = alreadyMove;
	}
	
	@Override
	public Rook clone() throws CloneNotSupportedException {
		Rook r =  (Rook) super.clone();
		r.alreadyMoved = alreadyMoved;
		return r;
	}
	
	@Override
	public String toString() {
		return "|Ro|"+ location.x + " " + location.y;
	}
	
//	public String details() {
//		String s = "\nRook location" + "[" + (location.x - 2) + "," + (location.y - 2)  + "]  my color: " + (color==Colors.WHITE? "white":"black") + "\npossible Moves: ";;
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
}
