package Logic;

import java.awt.Point;

//This object represents an remote tool.
//It's location is 'source' and it's direction is 'direction'.

public class Vector {
	private Point source;
	private Point direction;
	
	public Vector (Point source, Point direction) {
		this.setSource(source);
		this.setDirection(direction);
	}

	public Point getSource() {
		return source;
	}

	public void setSource(Point source) {
		this.source = source;
	}

	public Point getDirection() {
		return direction;
	}

	public void setDirection(Point direction) {
		this.direction = direction;
	}
	
	public Point getOppositeDirection() {
		return new Point (direction.x * -1, direction.y * -1);
	}
}
