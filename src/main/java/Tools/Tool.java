package Tools;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Logic.Colors;
import Logic.Vector;

abstract public class Tool implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	protected  Colors color;
	protected Point location;
	protected Point[] directions;
	protected double myValue = 0;
	protected int numberOfMoves = 0;
	protected int id;

	public List<Point> canMoveTo = new ArrayList<Point>();
	public List<Vector> threatsOnMe = new ArrayList<Vector>();
	public List<Vector> Iprotect = new ArrayList<Vector>();
	public List<Vector> protectedByOthers = new ArrayList<Vector>();
	public List<Vector> Ithreat = new ArrayList<Vector>();
	public List<Point> pawnsCanReach = new ArrayList<Point>();
	
	public int getId() {
		return id;
	}
	
	public void addMovement(Tool t, Point dest) {
//		if(pointListContains(t.canMoveTo, dest))
//			System.out.println("adding twice");
//		
//		System.out.println(t + "canMoveTo.add " + dest.x + " " + dest.y);
		t.canMoveTo.add(dest);
	}
	
	public void addPawnsCanReach(Tool t, Point dest) {
//		if(pointListContains(t.pawnsCanReach, dest))
//			System.out.println("adding twice");
//		
//		System.out.println(t + "PawnCanReach.add " + dest.x + " " + dest.y);
		t.pawnsCanReach.add(dest);
	}
	
	public void addThreatOnMe(Tool t, Vector v) {
//		if(vectorListContains(t.threatsOnMe, v.getSource()))
//			System.out.println("adding twice");
//		
//		Point src = v.getSource();
//		System.out.println(t + "threatsOnMe.add " + src.x + " " + src.y);
		t.threatsOnMe.add(v);
	}
	
	public void addIprotect(Tool t, Vector v) {
//		if(vectorListContains(t.Iprotect, v.getSource()))
//			System.out.println("adding twice");
//		
//		Point src = v.getSource();
//		System.out.println(t + "Iprotect.add " + src.x + " " + src.y);
		t.Iprotect.add(v);
	}
	
	public void addProtectedByOthers(Tool t, Vector v) {
//		if(vectorListContains(t.protectedByOthers, v.getSource()))
//			System.out.println("adding twice");
//		
//		Point src = v.getSource();
//		System.out.println(t + "ProtectedByOthers.add " + src.x + " " + src.y);
		t.protectedByOthers.add(v);
	}
	
	public void addIthreat(Tool t, Vector v) {
//		if(vectorListContains(t.Ithreat, v.getSource()))
//			System.out.println("adding twice");
//		
//		Point src = v.getSource();
//		System.out.println(t + "Ithreat.add " + src.x + " " + src.y);
		t.Ithreat.add(v);
	}
	
	public void removePawnsCanReach(Tool t, Point dest) { 
		t.pawnsCanReach.remove(dest);
//			System.out.println(t + "pawnsCanReach.remove " + dest.x + " " + dest.y );
	}
	
	
	private boolean removeFromList(List<Vector> list, Point toolCoordinates) {
		// Removing threat from a list.
		Vector ptr = null;
		for(Vector v: list) {
			Point coordinates = v.getSource();
			if(coordinates.x == toolCoordinates.x && coordinates.y == toolCoordinates.y) {
				ptr = v;
				break;
			}
		}
		return list.remove(ptr);
	}
	
	public void removeThreatOnMe(Tool t, Point toolCoordinates) {
		removeFromList(t.threatsOnMe, toolCoordinates);
//			System.out.println(t + "threatsOnMe.remove " + toolCoordinates.x + " " + toolCoordinates.y);
	}
	
	public void removeProtectedByOthers(Tool t, Point toolCoordinates) {
		removeFromList(t.protectedByOthers, toolCoordinates);
//			System.out.println(t + "protectedByOthers.remove " + toolCoordinates.x + " " + toolCoordinates.y);
	}
	
	public void removeIprotect(Tool t, Point toolCoordinates) {
		removeFromList(t.Iprotect, toolCoordinates);
//			System.out.println(t + "Iprotect.remove " + toolCoordinates.x + " " + toolCoordinates.y);
	}
	
	public void removeIthreat(Tool t, Point toolCoordinates) {
		removeFromList(t.Ithreat, toolCoordinates);
//			System.out.println(t + "Ithreat.remove " + toolCoordinates.x + " " + toolCoordinates.y);
	}
	
	public boolean removeMove(Tool t, Point toDelete) {
		if(!(t.canMoveTo.remove(toDelete))) {
			Point ptr= null;
			for(Point move: t.canMoveTo) {
				if(move.x == toDelete.x && move.y == toDelete.y) {
					ptr = move;
					break;
				}
			}
			if(ptr == null)
				return false;
			t.canMoveTo.remove(ptr);
		}
		System.out.println(t + "canMoveTo.remove " + toDelete.x + " " + toDelete.y);
		return true;
	}
	
	protected boolean pointListContains(List<Point> list, Point move) {
		if(!(list.contains(move))) {
			for(Point p: list) 
				if(p.x == move.x && p.y == move.y)
					return true;
			return false;
		}
		return true;
	}
	
	protected void removeAllMoves() {
		int size = canMoveTo.size();
		for(int i = 0; i < size; i++)
			removeMove(this, canMoveTo.get(0));
	}
	
	protected void removeAllIprotect() {
		int size = Iprotect.size();
		for(int i = 0; i < size; i++) {
			removeIprotect(this, Iprotect.get(0).getSource());
		}
	}
	
	protected void removeAllIthreat() {
		int size = Ithreat.size();
		for(int i = 0; i < size; i++)
			removeIthreat(this, Ithreat.get(0).getSource());
	}
	
	protected void removeAllPawnsCanReach(Tool t) {
		int size = pawnsCanReach.size();
		for(int i = 0; i < size; i++)
			removePawnsCanReach(t, pawnsCanReach.get(0));
	}
	
	public Tool(Colors color, Point loc)
	{
		this.color = color;
		this.location = loc;
	}
	
	public Tool() { }
	
	public double getMyValue() {
		return myValue;
	}
	
	public int getNumberOfMoves() {
		return numberOfMoves;
	}
	
	public boolean hasThreats(Colors srcColor) {
		if(color == srcColor) 
			return threatsOnMe.size() > 0 ? true: false;
		else
			return protectedByOthers.size() > 0 ? true: false;
	}
	
	public int getThreatCount(Colors srcColor) {
		if(color == srcColor) 
			return threatsOnMe.size();
		else
			return protectedByOthers.size();
	}
	

	
	protected boolean vectorListContains(List<Vector> list, Point move) {
		for(Vector v: list) {
			Point p = v.getSource();
			if(p.x == move.x && p.y == move.y)
				return true;
		}
		return false;
	}
	
	// I used the term 'threat' but it can also be for protection relationship
	public void diminishImpactOnList(Tool[][] tools,Tool currTool, List<Vector> list, boolean isThreat) {
		for(Vector v: list) {
			Point vectorSrcCoor = v.getSource();
			Tool vectorSrc = tools[vectorSrcCoor.x][vectorSrcCoor.y];
			
			//removing this tool from  'Ithreat' list of 'threatSource' tool.
			if(isThreat) {
				removeIthreat(vectorSrc, currTool.location);	
				if(vectorSrc instanceof Pawn) {
					removeMove(vectorSrc, currTool.location);
				}
			}
			else {
				removeIprotect(vectorSrc, currTool.location);
				if(!(vectorSrc instanceof Pawn))
					addMovement(vectorSrc, new Point(currTool.location.x, currTool.location.y));
			}
			
			if(vectorSrc instanceof LinearTool) {
				//If the threat source is from type 'LinearTool' this section fixes it 'canMoveTo','Iprotect', 'Ithreat' lists. 
				Point dir = v.getOppositeDirection();
				
				if(!addingLinearMovement(tools,currTool, vectorSrc, dir))
					continue;
			}
			// There is no need for fixing to 'OneStepTool' except from removing this tool from it 'Ithreat' list.
		}
	}
	
	public boolean addingLinearMovement(Tool[][] tools,Tool currTool, Tool threatSource ,Point dir) {
		int i = 1;
		Point threatSrcLocation = new Point(threatSource.location.x , threatSource.location.y);
		while(tools[currTool.location.x + dir.x * i][currTool.location.y + dir.y* i] instanceof Empty) {
			addMovement(threatSource, new Point(currTool.location.x + dir.x * i, currTool.location.y + dir.y* i));
			((Empty)tools[currTool.location.x + dir.x * i][currTool.location.y + dir.y* i]).addRelationship(tools, new Point(dir.x * -1, dir.y * -1), threatSrcLocation, threatSource.color);
			i++;
		}
		Point toolCoordinates = new Point(currTool.location.x + dir.x * i, currTool.location.y + dir.y * i);
		Tool destinationSquare = tools[currTool.location.x + dir.x * i] [currTool.location.y + dir.y * i];
		if(destinationSquare instanceof Wall)
			return false;
		
		if(destinationSquare.getColor() == threatSource.color) {
			Vector newProtection = new Vector(toolCoordinates, dir);
			addIprotect(threatSource, newProtection);
			addProtectedByOthers(destinationSquare, new Vector(threatSrcLocation, newProtection.getOppositeDirection()));
		}else {
			Vector newThreat = new Vector(toolCoordinates, dir);
			addIthreat(threatSource, newThreat);
			addMovement(threatSource, toolCoordinates); 
			addThreatOnMe(destinationSquare, new Vector(threatSrcLocation, newThreat.getOppositeDirection()));
		}	
		return true;
	}
	
	public void removeFromBoard(Tool[][] tools) {
		diminishToolImpact(tools, this);
		tools[location.x][location.y] = new Empty(new Point(location.x,location.y), "", threatsOnMe, protectedByOthers, pawnsCanReach, color);
		//Update pawn forward movement
		for(Point p: pawnsCanReach) 
			((Pawn)tools[p.x][p.y]).addForwardMovement(tools, location);
	}
	
	public void moveTo(Tool[][] tools, Point destination) {
//		System.out.println("removeFromBoard:");
		removeFromBoard(tools);
//		System.out.println("placeAt: ");
		placeAt(tools, destination);
		this.numberOfMoves++;
	}
	
	public void goBackTo(Tool[][] tools, Point destination) {
//		System.out.println("removeFromBoard:");
		removeFromBoard(tools);
//		System.out.println("placeAt: ");
		placeAt(tools, destination);
		this.numberOfMoves--;
	}
	
//	public void checkIfClean(Tool[][] tools, Point deletedPoint) {
//		for(Tool[] row : tools) {
//			for(Tool element: row) {
//				for(Vector v: element.Ithreat) {
//					Point source = v.getSource();
//					if(source.x == deletedPoint.x && source.y == deletedPoint.y)
//						System.out.println("not clean 1");
//				}
//				for(Vector v: element.Iprotect) {
//					Point source = v.getSource();
//					if(source.x == deletedPoint.x && source.y == deletedPoint.y)
//						System.out.println("not clean 2");
//				}
//				for(Vector v: element.threatsOnMe) {
//					Point source = v.getSource();
//					if(source.x == deletedPoint.x && source.y == deletedPoint.y)
//						System.out.println("not clean 3");
//				}
//				for(Vector v: element.protectedByOthers) {
//					Point source = v.getSource();
//					if(source.x == deletedPoint.x && source.y == deletedPoint.y)
//						System.out.println("not clean 4");
//				}
//				
//			}
//		}
//	}
	
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
		//Removing my threat of the empty squares
		for(Point dest: tool.canMoveTo) {
			Tool destSquare = tools[dest.x][dest.y];
			if(destSquare instanceof Empty)
				((Empty)destSquare).removeRelationship(tools, tool.location, tool.color);
		}
		removeAllMoves();
		removeAllIprotect();
		removeAllIthreat();
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
		//checkIfClean(tools, destination);
		
		//boolean isEmpty = tools[destination.x][destination.y] instanceof Empty ? true:false;
		tools[destination.x][destination.y] = this;	
		for(Point p: pawnsCanReach) 
			((Pawn)tools[p.x][p.y]).removeForwardMovement(tools,location);
		updateLists(tools);
		
		fixAffectedTools(tools);	
	}
	
	public void fixAffectedTools(Tool[][] tools) {
		updateRemoteLists(tools,threatsOnMe);
		updateRemoteLists(tools,protectedByOthers);	
	}
	
	public List<Vector> getThreats(Colors srcColor){
		if(srcColor == color)
			return threatsOnMe;
		else
			return protectedByOthers;
	}
	
	public List<Vector> getProtects(Colors srcColor){
		if(srcColor == color)
			return protectedByOthers;
		else
			return threatsOnMe;
	}
	
	public void updateRemoteLists(Tool[][] tools, List<Vector> list) {
		Point locationClone = new Point(location.x, location.y);
		for(Vector v: list) {
			Point threatCoordinate = v.getSource();
			Tool threatSource = tools[threatCoordinate.x][threatCoordinate.y];
			
			Point dir = v.getOppositeDirection();
			if(color == threatSource.getColor()) {
				addIprotect(threatSource, new Vector(locationClone, dir));
				removeMove(threatSource, locationClone);
			}
			else {
				addIthreat(threatSource, new Vector(locationClone, dir));
				if(threatSource instanceof Pawn) 
					addMovement(threatSource, locationClone);
			}

			if(threatSource instanceof LinearTool) {
				int i = 1;
				while(removeMove(threatSource, tools[location.x + dir.x * i][location.y + dir.y * i].getLocation()) )
				{
					if(tools[location.x + dir.x * i][location.y + dir.y * i] instanceof Empty)
						((Empty)tools[location.x + dir.x * i][location.y + dir.y * i]).removeRelationship(tools, threatCoordinate, threatSource.getColor());
					i++;
				} 
				if(i > 1 && !(tools[location.x + dir.x * (i - 1)][location.y + dir.y * (i - 1)] instanceof Empty))
					i--;
				
				if(!(tools[location.x + dir.x * i][location.y + dir.y * i] instanceof Wall || tools[location.x + dir.x * i][location.y + dir.y * i] instanceof Empty)) {
					if(tools[location.x + dir.x * i][location.y + dir.y * i].getColor() == threatSource.color) {
						removeIprotect(threatSource, tools[location.x + dir.x * i][location.y + dir.y * i].getLocation());
						removeProtectedByOthers(tools[location.x + dir.x * i][location.y + dir.y * i], threatCoordinate);
					}
					else {
						removeIthreat(threatSource, tools[location.x + dir.x * i][location.y + dir.y * i].getLocation());
						removeThreatOnMe(tools[location.x + dir.x * i][location.y + dir.y * i], threatCoordinate);
					}
				}
			}

		}
	}

	// You should use this method only if there is no check.
	public List<Point> getPossibleMoves(Tool[][] tools, Point king) {
		//Moving from square that has no threats can't expose the king to a threat.
		if(threatsOnMe.size() == 0)
			return canMoveTo;	
		
		// Deciding if one of my threats can expose the king to a threat
		for(Vector v: threatsOnMe) {
			// If the king has the same threat , I'm not exposing him to new threat.
			if(tools[king.x][king.y].threatsOnMe.contains(v))
				continue;
			Tool threatTool = tools[v.getSource().x][v.getSource().y];
			// Threats from tools that are not linear doesn't affect the king.
			if(!(threatTool instanceof Rook || threatTool instanceof Queen || threatTool instanceof Bishop))
				continue;

			//If I have straight line of empty squares between me and the king in the same attacking vector of my threat - this move will expose the king. 
			int i = 1;
			Point dir = v.getOppositeDirection();
			List<Point> restriction = new ArrayList<Point>();
			while(tools[location.x + dir.x * i][location.y + dir.y * i] instanceof Empty ) {  i++; restriction.add(new Point (location.x + dir.x * i, location.y + dir.y * i));}
			if(location.x + dir.x * i == king.x && location.y + dir.y * i == king.y) {
				dir = v.getDirection();
				i = 1;
				while(tools[location.x + dir.x * i][location.y + dir.y * i] instanceof Empty ) {  i++; restriction.add(new Point (location.x + dir.x * i, location.y + dir.y * i));}
				restriction.add(new Point (location.x + dir.x * i, location.y + dir.y * i));
				return getPossibleMoves(canMoveTo, king, restriction);
				
			}
		}
		return canMoveTo;
	}
	
	
	// You should use this method only in check mode.
	public List<Point> getPossibleMoves(List<Point> canMoveTo, Point king, List<Point> restriction) {
		// returns the common subset of the list 'restriction' and 'canMoveTo'.
		List<Point> availableMoves = new ArrayList<Point>();
		//if(getPossibleMoves(tools, king) != null) {
			for(Point res: restriction) {
				if(canMoveTo.contains(res))
					availableMoves.add(res);
			}
		//}
		return availableMoves;
	}
	
	public abstract void updateLists(Tool[][] tools);
	
	public Point[] getDirections() {
		return directions;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	public Colors getColor() { return color; }
	public Point getLocation() { return location; } 
	
	public Colors oppositeColor() {
		return color == Colors.BLACK ? Colors.WHITE : Colors.BLACK;
	}
	
	public boolean isFinalRow() {
		if(color == Colors.WHITE && location.x == 2) 
			return true;
		if(color == Colors.BLACK && location.x == 9) 
			return true;
		return false;
	}
	
	//public abstract String details();
	
	@Override
	public  Tool clone() throws CloneNotSupportedException {
		Tool t = (Tool) super.clone();
		t.color = color;
		t.location = new Point(location.x, location.y);
		t.directions = directions;
		t.canMoveTo = new ArrayList<Point>();
		for(Point p: canMoveTo) 
			addMovement(t,p);
		
		t.threatsOnMe = new ArrayList<Vector>();
		for(Vector v: threatsOnMe)
			addThreatOnMe(t, v);
		
		t.Iprotect = new ArrayList<Vector>();
		for(Vector v: Iprotect)
			addIprotect(t, v);
		
		t.protectedByOthers = new ArrayList<Vector>();
		for(Vector v: protectedByOthers)
			addProtectedByOthers(t, v);
		
		t.Ithreat = new ArrayList<Vector>();
		for(Vector v: Ithreat)
			addIthreat(t, v);
		
		t.pawnsCanReach = new ArrayList<Point>();
		for(Point p: pawnsCanReach)
			addPawnsCanReach(t, p);
		
		return t;	
	}

}
