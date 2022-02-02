package Tools;

import java.awt.Point;

import Logic.Colors;

public abstract class LinearTool extends Tool implements Cloneable {

	private static final long serialVersionUID = 1L;

	public LinearTool(Colors color, Point location)
	{
		super(color, location);
	}
	
	//Updating the list of the tool.
	public void updateLists(Tool[][] tools) {
		for(Point dir: directions) {
			
			//Adds to the tool linear movement in the specified direction.
			//Changing the lists of this tool and to other tools on the board.
			if(!addingLinearMovement(tools,this, this, dir))
				continue;
		}
	}
	
	public LinearTool clone() throws CloneNotSupportedException {
		LinearTool t = (LinearTool) super.clone();
		return t;
	}
	
}
