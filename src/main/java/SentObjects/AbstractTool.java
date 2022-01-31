package SentObjects;
import java.io.Serializable;

import Logic.Colors;

public class AbstractTool implements Serializable {

	private static final long serialVersionUID = 1L;
	private String type;
	private Colors color;
	private int X, Y;
	
	public AbstractTool(String type, Colors color, int X, int Y) {
		this.setType(type);
		this.setColor(color);
		this.setX(X);
		this.setY(Y);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Colors getColor() {
		return color;
	}

	public void setColor(Colors color) {
		this.color = color;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
	
}
