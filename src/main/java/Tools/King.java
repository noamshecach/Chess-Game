package Tools;
import java.awt.Point;
import Frames.Check;
import Frames.Mate;
import Logic.Colors;
import Logic.Vector;
import MoveTypes.LongRooking;
import MoveTypes.MoveAndStop;
import MoveTypes.ShortRooking;

public class King extends OneStepTool implements MoveAndStop {
	
	private static final long serialVersionUID = 1L;
	private boolean alreadyMoved = false;
	private boolean didRooking = false;
	private Point opponentKing = null;
	
	public King(Colors color, Point location)
	{
		super(color, location);
		directions = new Point[10];
		directions[0] = new Point(0,1);
		directions[1] = new Point(1,1);
		directions[2] = new Point(1,0);
		directions[3] = new Point(1,-1);
		directions[4] = new Point(0,-1);
		directions[5] = new Point(-1,-1);
		directions[6] = new Point(-1,0);
		directions[7] = new Point(-1,1);
		directions[8] = new ShortRooking(0,2);
		directions[9] = new LongRooking(0,-2);
		setId();
	}
	
	public void setId() {
		if(alreadyMoved == false) {
			if(color == Colors.WHITE)
				id = 11; // white king & castling is possible
			else
				id = 12; //black king & castling is possible
		}
		else{
			if(color == Colors.WHITE)
				id = 13; // white king & castling is not possible
			else
				id = 14; //black king & castling is not possible
		}
	}
	
	public void updateLists(Tool[][] tools, Point opponentKing) {
		this.opponentKing = opponentKing;
		updateLists(tools);
	}
	
	public void updateLists(Tool[][] tools) {
		Point locationClone = new Point(location.x, location.y);
		for(Point dir: directions) {
			Point toolCoordinates = new Point(locationClone.x + dir.x, locationClone.y + dir.y);
			Tool destinationSquare = tools[toolCoordinates.x] [toolCoordinates.y];			
			if(destinationSquare instanceof Wall)
				continue;

			boolean canMoveAlready = pointListContains(canMoveTo, toolCoordinates);
			boolean protectAlready = vectorListContains(Iprotect, toolCoordinates);
			
			if(opponentKing != null && Math.abs(toolCoordinates.x - opponentKing.x) <=1 && Math.abs(toolCoordinates.y - opponentKing.y) <=1 ) {
				if(canMoveAlready)
					removePotenialMove(tools, toolCoordinates, destinationSquare, canMoveAlready, protectAlready);
				continue;
			}
			if(destinationSquare.hasThreats(color)) {
				if(canMoveAlready || protectAlready)
					removePotenialMove(tools, toolCoordinates, destinationSquare, canMoveAlready, protectAlready);
				continue;
			}
			if(threatsOnMe.size() > 0 ) {
				boolean conditionMet = false;
				for(Vector v: threatsOnMe) {
					Point threatDir = v.getOppositeDirection();
					if(threatDir.x == dir.x && threatDir.y == dir.y) {
						if(canMoveAlready)
							removePotenialMove(tools, toolCoordinates, destinationSquare, canMoveAlready, protectAlready);
						conditionMet = true;
						continue;
					}
				}
				if(conditionMet)
					continue;
			}
			if(dir instanceof ShortRooking) 
				if(!(  !alreadyMoved && tools[locationClone.x][9] instanceof Rook && !((Rook)tools[locationClone.x][9]).isAlreadyMove() 
						&& tools[locationClone.x][8] instanceof Empty && tools[locationClone.x][7] instanceof Empty) ) {
					if(canMoveAlready || protectAlready)
						removePotenialMove(tools, toolCoordinates, destinationSquare, canMoveAlready, protectAlready);
					continue;
				}
			if(dir instanceof LongRooking) 
				if(!(  !alreadyMoved && tools[locationClone.x][2] instanceof Rook && !((Rook)tools[locationClone.x][2]).isAlreadyMove()
						&& tools[locationClone.x][3] instanceof Empty && tools[locationClone.x][4] instanceof Empty && tools[locationClone.x][5] instanceof Empty)) {
					if(canMoveAlready || protectAlready)
						removePotenialMove(tools, toolCoordinates, destinationSquare, canMoveAlready, protectAlready);
					continue;
				}
			
			if(destinationSquare instanceof Empty ) {
				if(!canMoveAlready) {
					addMovement(this, toolCoordinates); 
					((Empty)destinationSquare).addRelationship(tools, new Point(dir.x * -1 , dir.y * -1), locationClone, color);
				}
				continue;
			}
			if(!(dir instanceof ShortRooking || dir instanceof LongRooking)) {
				if(destinationSquare.getColor() == color) {
					if(!protectAlready) {
						Vector newProtection = new Vector(toolCoordinates, dir);
						addIprotect(this, newProtection);
						addProtectedByOthers(destinationSquare, new Vector(locationClone, newProtection.getOppositeDirection()));
					}
					continue;
				}
				if(destinationSquare.oppositeColor() == color) {
					if(!canMoveAlready) {
						Vector newThreat = new Vector(toolCoordinates, dir);
						addIthreat(this,newThreat);
						addMovement(this,toolCoordinates); 
						addThreatOnMe(destinationSquare, new Vector(locationClone, newThreat.getOppositeDirection()));
					}
					continue;
				}
			}
			removePotenialMove(tools, toolCoordinates, destinationSquare, canMoveAlready, protectAlready);
		}
	}
	
	private void removePotenialMove(Tool[][] tools, Point toolCoordinates, Tool destinationSquare, boolean canMoveAlready, boolean protectAlready) {
		if(canMoveAlready) {
			removeMove(this, toolCoordinates);
			if(destinationSquare instanceof Empty )
				((Empty)destinationSquare).removeRelationship(tools, location, color);
			else {
				removeIthreat(this, toolCoordinates);
				removeThreatOnMe(destinationSquare, location);
			}
		}
		if(protectAlready) {
			removeIprotect(this, toolCoordinates);
			removeProtectedByOthers(destinationSquare, location);	
		}
	}
	
 	public void moveTo(Tool[][] tools, Point destination) {
		
 		alreadyMoved = true;
 		setId();
		handleRooking(tools, destination);
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
			abortRooking(tools, destination);
//			System.out.println("removeFromBoard:");
			removeFromBoard(tools);
//			System.out.println("placeAt: ");
			placeAt(tools, destination);
		}
	}
	
	public void placeAt(Tool[][] tools, Point destination) {
		
		Tool destSquare = tools[destination.x][destination.y];	
		if(!(destSquare instanceof Empty)) {
			destSquare.diminishToolImpact(tools, destSquare);
			removeThreatOnMe(this, destination);
		}
		
		threatsOnMe =  tools[destination.x][destination.y].getThreats(color);
		protectedByOthers =  tools[destination.x][destination.y].getProtects(color);
		location = new Point(destination.x, destination.y);
		removeAllMoves();
		removeAllIprotect();
		removeAllIthreat();
		pawnsCanReach = tools[destination.x][destination.y].pawnsCanReach;
//		checkIfClean(tools, destination);

		//boolean isEmpty = tools[destination.x][destination.y] instanceof Empty ? true:false;
		tools[destination.x][destination.y] = this;	
		for(Point p: pawnsCanReach) 
			((Pawn)tools[p.x][p.y]).removeForwardMovement(tools, location);

		updateLists(tools);		
		updateRemoteLists(tools,threatsOnMe);
		updateRemoteLists(tools,protectedByOthers);
	}
		
	public Check isCheck() {
		if(threatsOnMe.size() > 0 && color == Colors.WHITE)
			return Check.onWHITE;
		if(threatsOnMe.size() > 0 && color == Colors.BLACK)
			return Check.onBLACK;
		return Check.NONE;
	}
	
	public Mate isMate(Tool[][] tools) {
		if(canMoveTo.size() != 0)
			return Mate.NONE;
		if(threatsOnMe.size() == 0)
			return Mate.NONE;
		if(threatsOnMe.size() >= 2) {
			if(color == Colors.WHITE)
				return Mate.onWHITE;
			else
				return Mate.onBLACK;
		}	
		for(Vector v: threatsOnMe) {
			Point threatCoordinates = v.getSource();
			Tool threateningTool = tools[threatCoordinates.x][threatCoordinates.y];
			if(threateningTool.threatsOnMe.size() > 0)
				return Mate.NONE;
			if(threateningTool instanceof LinearTool) {
				Point dir = v.getOppositeDirection();
				int i = 1;
				threateningTool = tools[threatCoordinates.x + dir.x * i][threatCoordinates.y + dir.y * i];
				while(threateningTool.location.x != location.x  && threateningTool.location.y != location.y) {
					if(((Empty)threateningTool).hasThreats(color == Colors.WHITE? Colors.BLACK:Colors.WHITE) )
						return Mate.NONE;
					i++;
					threateningTool = tools[threatCoordinates.x + dir.x * i][threatCoordinates.y + dir.y * i];
				}
			}
		}

		if(color == Colors.WHITE)
			return Mate.onWHITE;
		else
			return Mate.onBLACK;
	}
	
	public void abortRooking(Tool[][] tools, Point dest) {
		if(dest.y - location.y == 2)
		{
			didRooking = false;
			if(color == Colors.BLACK) 
				tools[2][5].goBackTo(tools, new Point(2,2));
			else
				tools[9][5].goBackTo(tools, new Point(9,2));
		}
		if(dest.y - location.y == -2)
		{
			didRooking = false;
			if(color == Colors.BLACK) 
				tools[2][7].goBackTo(tools, new Point(2,9));
			else
				tools[9][7].goBackTo(tools, new Point(9,9));
		}
	}
	
	public void handleRooking(Tool[][] tools, Point dest) {
		if(dest.y - location.y == 2)
		{
			didRooking = true;
			if(color == Colors.BLACK) 
				tools[2][9].moveTo(tools, new Point(2,7));
			else
				tools[9][9].moveTo(tools, new Point(9,7));
		}
		if(dest.y - location.y == -2)
		{
			didRooking = true;
			if(color == Colors.BLACK) 
				tools[2][2].moveTo(tools, new Point(2,5));
			else
				tools[9][2].moveTo(tools, new Point(9,5));
		}
	}
	
	public boolean isDidRooking() {
		return didRooking;
	}
	
	public King clone() throws CloneNotSupportedException {
		King k =  (King) super.clone();
		k.alreadyMoved = alreadyMoved;
		k.didRooking = didRooking;
		return k;
	}
	
//	public String details() {
//		String s = "\nKing location" + "[" + (location.x - 2) + "," + (location.y - 2) + "]  my color: " + (color==Colors.WHITE? "white":"black") + "\npossible Moves: ";
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
		return "|Ki|"+ location.x + " " + location.y;
	}
}
