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
	
	public void updateLists(Tool[][] tools) {
		Point locationClone = new Point(location.x, location.y);
		for(Point dir: directions) {
			Point toolCoordinates = new Point(locationClone.x + dir.x, locationClone.y + dir.y);
			Tool destinationSquare = tools[locationClone.x + dir.x] [locationClone.y + dir.y];
			if(destinationSquare instanceof Wall)
				continue;
			if(destinationSquare instanceof Empty ) {
				addMovement(this, toolCoordinates); 
				((Empty)tools[toolCoordinates.x][toolCoordinates.y]).addRelationship(tools, new Point (dir.x * -1, dir.y * -1), locationClone, color);
				continue;
			}
			if(destinationSquare.getColor() == color) {
				Vector newProtection = new Vector(toolCoordinates, dir);
				addIprotect(this, newProtection);
				addProtectedByOthers(destinationSquare, new Vector(locationClone, newProtection.getOppositeDirection()));
			}
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
