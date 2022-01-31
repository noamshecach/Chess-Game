package Frames;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MyComponent extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Component comp;
	private ImageIcon image;
	private int originalWidth, originalHeight, originalX, originalY;
	
	public MyComponent(Component comp, ImageIcon image, int originalX, int originalY, int originalWidth, int originalHeight) {
		this.comp = comp;
		this.image = image;
		this.originalWidth = originalWidth;
		this.originalHeight = originalHeight;
		this.originalX = originalX;
		this.originalY = originalY;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public Component getComp() {
		return comp;
	}
	
	public int getOriginalWidth() {
		return this.originalWidth;
	}
	
	public int getOriginalHeight() {
		return this.originalHeight;
	}
	
	public int getOriginalX() {
		return this.originalX;
	}
	
	public int getOriginalY() {
		return this.originalY;
	}
	
}
