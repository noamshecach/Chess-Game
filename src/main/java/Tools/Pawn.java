package Tools;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Logic.Colors;
import Logic.Vector;
import MoveTypes.MoveAndStop;
import MoveTypes.PawnCaptureDir;
import MoveTypes.PawnExtraStartMove;
import MoveTypes.PawnOneStepMove;

public class Pawn extends OneStepTool implements MoveAndStop, Cloneable {

	private static final long serialVersionUID = 1L;
	private boolean alreadyMoved = false;
	public List<Vector> threatenedEmptySquares = new ArrayList<Vector>();
	
	public Pawn( Colors pawnColor,Point location)
	{
		super(pawnColor, location);
		directions = new Point[4];
		if(pawnColor == Colors.WHITE) {
			directions[0] = new PawnOneStepMove(-1,0);  //WHITE CLOSE TO U
			directions[1] = new PawnExtraStartMove(-2,0); 
			directions[2] = new PawnCaptureDir(-1,-1); 
			directions[3] = new PawnCaptureDir(-1,1); 
			id = 1;
		}else {
			directions[0] = new PawnOneStepMove(1,0);  // BLACK CLOSE TO U
			directions[1] = new PawnExtraStartMove(2,0); 
			directions[2] = new PawnCaptureDir(1,-1); 
			directions[3] = new PawnCaptureDir(1,1); 
			id = 2;
		}
		myValue = 10;	
	}
	
	public void addForwardMovement(Tool[][] tools, Point srcSquare) {
		// basis assumption: there was a tool in front of the pawn - thereby there is a pointer to the pawn at least in one square.

		if(srcSquare.x == location.x + directions[0].x && srcSquare.y == location.y + directions[0].y) {
			addMovement(this, new Point(location.x + directions[0].x, location.y + directions[0].y));
			
			if(!alreadyMoved) {
				addPawnsCanReach(tools[location.x + directions[1].x][location.y + directions[1].y], location);
			
				if(tools[location.x + directions[1].x][location.y + directions[1].y] instanceof Empty) 
					addMovement(this, new Point(location.x + directions[1].x, location.y + directions[1].y));
			}
		}
		if(srcSquare.x == location.x + directions[1].x && srcSquare.y == location.y + directions[1].y) 
			addMovement(this, new Point(location.x + directions[1].x, location.y + directions[1].y));

	}
	
//	public boolean irRegularities(Tool[][] tools) {
//		if(!(tools[location.x + directions[0].x][location.y + directions[0].y] instanceof Empty) &&
//				pointListContains(tools[location.x + directions[1].x][location.y + directions[1].y].pawnsCanReach, location) )
//			return true;
//		return false;
//	}
	
	public boolean irRegularities(Tool[][] tools) {
		if(tools[location.x + directions[0].x][location.y + directions[0].y] instanceof Empty &&
				tools[location.x + directions[1].x][location.y + directions[1].y] instanceof Empty &&
				canMoveTo.size() == 0 )
			return true;
		return false;
	}
	
	public void removeForwardMovement(Tool[][] tools, Point test) {
		
		//Basis assumption: there is a tool in front of this pawn.
		if(!(tools[location.x + directions[0].x][location.y + directions[0].y] instanceof Empty)) {
			// there is a tool one step ahead of the pawn
			if(!alreadyMoved) //if the tool have the ability to walk two steps - remove his pointer from the second square
				removePawnsCanReach(tools[location.x + directions[1].x][location.y + directions[1].y], location);
			//remove the ability to move
			removeMove(this, new Point(location.x + directions[0].x, location.y + directions[0].y));
			removeMove(this, new Point(location.x + directions[1].x, location.y + directions[1].y));
		}else {
			//there is a tool two step ahead of the pawn, between them empty square
			//empty square has a pointer to the pawn in pawnsCanReach and the pawn can move to the square
			//the tool has a pointer to the pawn in pawnCanReach
			removeMove(this, new Point(location.x + directions[1].x, location.y + directions[1].y));
		}
		
	}
	
	public void addThreatenedEmptySquares(Vector v) {
		Point dest = v.getSource();
		threatenedEmptySquares.add(v);
		System.out.println(this + "threatenedEmptySquares.add " + dest.x + " " + dest.y);
	}
	
	public void removeAllThreatenedEmptySquares() {
		int size = threatenedEmptySquares.size();
		for(int i = 0; i < size; i++) {
			Point dest = threatenedEmptySquares.get(0).getSource();
			if(threatenedEmptySquares.remove(threatenedEmptySquares.get(0)))
				System.out.println(this + "threatenedEmptySquares.remove " + dest.x + " " + dest.y);
		}
	}
	
	public void updateLists(Tool[][] tools) {
		Point locationClone = new Point(location.x, location.y);
		boolean twoStepMove = false;
		for(Point dir: directions) {
			Point toolCoordinates = new Point(locationClone.x + dir.x, locationClone.y + dir.y);
			Tool destinationSquare = tools[locationClone.x + dir.x] [locationClone.y + dir.y];
			if(destinationSquare instanceof Wall)
				continue;
			if(dir instanceof PawnCaptureDir) {
				Vector newThreat = new Vector(toolCoordinates, dir);
				addThreatenedEmptySquares(newThreat);
				
				if(destinationSquare instanceof Empty) 
					((Empty)destinationSquare).addRelationship(tools, new Point (dir.x * -1, dir.y * -1), locationClone, color);
				
				if(!(destinationSquare instanceof Empty) && destinationSquare.oppositeColor() == color) {
					addIthreat(this, newThreat);
					addMovement(this, toolCoordinates); 
					addThreatOnMe(destinationSquare, new Vector(locationClone, newThreat.getOppositeDirection()));	continue;
				}
				if(!(destinationSquare instanceof Empty) && destinationSquare.getColor() == color) {
					addIprotect(this,newThreat);
					addProtectedByOthers(destinationSquare, new Vector(locationClone, newThreat.getOppositeDirection())); continue;
				}
			}else {
				if(dir instanceof PawnOneStepMove) {
					addPawnsCanReach(destinationSquare, locationClone);
					if(destinationSquare instanceof Empty) {
						addMovement(this,toolCoordinates); 
						twoStepMove = true;
					}	
				}

				if(destinationSquare instanceof Empty  && dir instanceof PawnExtraStartMove && !alreadyMoved && twoStepMove) {
					addMovement(this, toolCoordinates); 
					addPawnsCanReach(destinationSquare, location);
				}
			}
		}
	}
	
	public void moveTo(Tool[][] tools, Point destination) {
		
		alreadyMoved = true;		
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
			if(numberOfMoves == 0)
				alreadyMoved = false;
//			System.out.println("removeFromBoard:");
			removeFromBoard(tools);
//			System.out.println("placeAt: ");
			placeAt(tools, destination);
		}
	}
 	
 	public void decNumberOfMoves() {
 		this.numberOfMoves--;
		if(numberOfMoves == 0)
			alreadyMoved = false;
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
	
	public void diminishToolImpact(Tool[][] tools, Tool tool) {

		diminishImpactOnList(tools, tool, tool.threatsOnMe, true);
		diminishImpactOnList(tools, tool, tool.protectedByOthers, false);
		
		// Removing my threats from other's tools lists.
		for(Vector threatenedByme : tool.Ithreat) {
			Point toolCoordinates = threatenedByme.getSource();
			Tool threatenedTool = tools[toolCoordinates.x][toolCoordinates.y];
			removeThreatOnMe(threatenedTool, tool.location);
		}
		//Removing my protection from other's tools lists.
		for(Vector protect: tool.Iprotect) {
			Point protectedToolCoordination = protect.getSource();
			Tool protectedTool = tools[protectedToolCoordination.x][protectedToolCoordination.y];
			removeProtectedByOthers(protectedTool, tool.location);
		}
		//Removing pointer from the empty squares that I could move to them.
		for(int i = 0; i < 2; i++) {
			Tool destSquare = tools[location.x + directions[i].x][location.y + directions[i].y];
			removePawnsCanReach(destSquare, tool.location);
		}
		
		//Removing my threat of the empty diagonal squares.
		for(Vector v: threatenedEmptySquares) {
			Point squareCoor = v.getSource();
			Tool square = tools[squareCoor.x][squareCoor.y];
			if(square instanceof Empty)
				((Empty)square).removeRelationship(tools, tool.location, tool.color);
				
		}
		removeAllMoves();
		removeAllIprotect();
		removeAllIthreat();
		removeAllThreatenedEmptySquares();
	}
	
//	public String details() {
//		String s = "\nPawn location" + "[" + (location.x - 2) + "," + (location.y - 2) + "]  my color: " + (color==Colors.WHITE? "white":"black") + "\npossible Moves: ";
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
		
	public Pawn clone() throws CloneNotSupportedException {
		Pawn p =  (Pawn) super.clone();
		p.alreadyMoved = alreadyMoved;
		
		p.threatenedEmptySquares = new ArrayList<Vector>();
		for(Vector v: threatenedEmptySquares)
			p.addThreatenedEmptySquares( v);
		
		return p;
	}

	@Override
	public String toString() {
		if(color == Colors.WHITE)
			return "|WP|"+ location.x + " " + location.y;
		return "|BP|"+ location.x + " " + location.y;
	}

}
