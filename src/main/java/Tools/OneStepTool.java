package Tools;

import java.awt.Point;
import Logic.Colors;
import Logic.Vector;

public abstract class OneStepTool extends Tool implements Cloneable {

	private static final long serialVersionUID = 1L;

	public OneStepTool(Colors color, Point location)
	{
		super(color, location);
	}
	
	//Update the lists of this tool.
	public void updateLists(Tool[][] tools) {
		Point locationClone = new Point(location.x, location.y);
		//Try to look on each possible direction of movement
		for(Point dir: directions) {
			Point toolCoordinates = new Point(locationClone.x + dir.x, locationClone.y + dir.y);
			Tool destinationSquare = tools[locationClone.x + dir.x] [locationClone.y + dir.y];
			
			//If the destination is wall - continue to the next direction.
			if(destinationSquare instanceof Wall)
				continue;
			
			//If the destination is empty - add to canMoveTo list, update the empty square that it has threat/protect.
			if(destinationSquare instanceof Empty ) {
				addMovement(this, toolCoordinates); 
				((Empty)tools[toolCoordinates.x][toolCoordinates.y]).addRelationship(tools, new Point (dir.x * -1, dir.y * -1), locationClone, color);
				continue;
			}
			//If the destination is tool in the same color - update Iprotect list , and the protectedByOthers list of the destination.
			if(destinationSquare.getColor() == color) {
				Vector newProtection = new Vector(toolCoordinates, dir);
				addIprotect(this, newProtection);
				addProtectedByOthers(destinationSquare, new Vector(locationClone, newProtection.getOppositeDirection()));
			}
			//If the destination is tool is the opposite color - update canMoveTo and Ithreat lists of this tool and threatsOnMe list of the destination.
			if(destinationSquare.oppositeColor() == color) {
				Vector newThreat = new Vector(toolCoordinates, dir);
				addIthreat(this, newThreat);
				addMovement(this,toolCoordinates); 
				addThreatOnMe(destinationSquare, new Vector(locationClone, newThreat.getOppositeDirection()));
			}
		}
	}
	
	public OneStepTool clone() throws CloneNotSupportedException {
		OneStepTool t = (OneStepTool) super.clone();
		return t;
	}
	
}
