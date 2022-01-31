package Tools;

import java.awt.Point;
import Logic.Colors;

/**
 * this class (wall) extends from the class tool.
 * all the functions are realization of the abstract 
 * functions on the tool class.
 * empty is also design as a tool because its more compterbale 
 * to know that all the squares on the board are tools.
 * @author Administrator
 *
 */

public class Wall extends Tool {

	private static final long serialVersionUID = 1L;

	public Wall(Point location)
	{
		super(Colors.NONE, location);
	}

	
	public void updateLists(Tool[][] tools) {
		
	}
	
	public String details() {
		return "";
	}
	
	@Override
	public String toString() {
		return "|Wa|";
	}
}
