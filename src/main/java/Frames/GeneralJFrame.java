package Frames;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import BarName.BarName;
import Chat.Chat;
import Client.ContactServer;
import Timer.Timer;

// All the windows on this application shares properties.
// Therefore this class defines the shared properties and being the parent of all the windows on the app.

public abstract class GeneralJFrame extends JFrame  {

	private static final long serialVersionUID = 1L;
	protected final int DEAFAULT_WIDTH = 1920, DEAFAULT_HEIGHT = 1080;
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected JLabel backgroundLabel, foregroundLabel;
	public static double widthProp = 0, heightProp = 0; 
	protected ContactServer contactServer;
	protected List<MyComponent> componentsArray = new ArrayList<MyComponent>();
	protected ImageIcon exitImg = new ImageIcon(getClass().getResource("/media/login/exit.png"));
	
	public GeneralJFrame(String forgroundImgPath) {
			
	// Set the bounds of the windows    and exit on close
		JLayeredPane lp = getLayeredPane();
		setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setUndecorated (true);

	// Set background image
		backgroundLabel = new JLabel();
		backgroundLabel.setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		ImageIcon backgroundImg = new ImageIcon(getClass().getResource( "/media/login/giff.gif"));
		backgroundLabel.setIcon(scaleImage(backgroundImg, (int) screenSize.getWidth(),(int)screenSize.getHeight() ));
	    lp.add(backgroundLabel, new Integer(1));
	    
	// Set foreground image and its event handler
	    foregroundLabel = new JLabel();  
	    foregroundLabel.setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
	    ImageIcon foregroundImg = new ImageIcon(getClass().getResource(forgroundImgPath));
		ComponentHandler comHandler = new ComponentHandler(foregroundImg, backgroundImg);
		addComponentListener(comHandler);
		foregroundLabel.setIcon(scaleImage(foregroundImg, (int) screenSize.getWidth(), (int) screenSize.getHeight()));
	    lp.add(foregroundLabel, new Integer(2));

	//Calculate the proportion of the window in relation to the screen size.
		widthProp = (double)getWidth() / (double)DEAFAULT_WIDTH; 
		heightProp = (double)getHeight() / (double)DEAFAULT_HEIGHT;
	}
	
	// Foreground image handler - responsible to change size if the window is resized
	protected class ComponentHandler extends ComponentAdapter{

		ImageIcon forgroundImg, backgroundImg;
		public ComponentHandler(ImageIcon forgroundImg, ImageIcon backgroundImg) {
			this.forgroundImg = forgroundImg;
			this.backgroundImg = backgroundImg;
		}
		
		@Override
		public void componentResized(ComponentEvent e) {
			compResized(componentsArray, forgroundImg, backgroundImg);
		}
	}
	
	
	//Changing the component size in proportion to the window size.
	protected void changeLabelSize(Component comp, ImageIcon image, int originalX, int originalY, int originalWidth, int originalHeight) {
		
		// Calculate the new dimensions  and setting them
		int width = (int)(Math.round(originalWidth * widthProp));
		int height = (int)(Math.round(originalHeight * heightProp));
		comp.setBounds((int)Math.round(originalX * widthProp), (int)Math.round(originalY * heightProp),  width, height);
		
		// Scale image if exists
		if(comp instanceof JLabel )
			if(image != null)
				((JLabel) comp).setIcon(scaleImage(image, width, height));
			else
				((JLabel) comp).setIcon(null);
		
		//If the component is a Timer - use private implementation of scale
		if(comp instanceof Timer)
			((Timer) comp).changeTimerSize();

		//If the component is a BarName - use private implementation of scale
		if(comp instanceof BarName)
			((BarName) comp).draw();
		
		//If the component is a Chat - use private implementation of scale
		if(comp instanceof Chat)
			((Chat) comp).draw();
	}
		
	//This function returns new image with the specified dimensions
	protected ImageIcon scaleImage(ImageIcon icon, int width, int height) {		
	    Image img = icon.getImage();
	    Image newImg = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    ImageIcon image = new ImageIcon(newImg);
	    return image;
	}
	 
	// Adds component to the screen
	protected void addLabel(Component label, int posX, int posY, ImageIcon image, MouseListener frame) {  // adds Timer and Label
		
		// Adds the label to the component array 
		componentsArray.add(new MyComponent(label, image, posX, posY,image.getIconWidth(), image.getIconHeight() ));
		
		//Set the components bounds
		changeLabelSize(label, image, posX, posY, image.getIconWidth(), image.getIconHeight());
		
		//Define mouse listener if specified
		if(frame != null)
			label.addMouseListener(frame); 
		
		//Adds the component to the screen
		getLayeredPane().add(label, new Integer(3));
	}

	// Adds component to the screen - with specific width and height
	protected void addLabel(JLabel label, int posX, int posY, int width, int height,ImageIcon image, MouseListener frame) {
		
		// Adds the barName to the component array 
		componentsArray.add(new MyComponent(label, image, posX, posY,width, height ));
		
		//Set the components bounds
		changeLabelSize(label, image, posX, posY, width, height);
		
		//Define mouse listener if specified
		if(frame != null)
			label.addMouseListener(frame); 
		
		//Adds the component to the screen
		getLayeredPane().add(label, new Integer(3));
	}
	
	protected void addBarName(BarName barName, int posX, int posY) {	
		//Adds barName to the screen
	    getLayeredPane().add(barName, new Integer(5));
	    
	    // Adds the barName to the component array 
	    componentsArray.add(new MyComponent(barName, null,posX,posY,530,240));
	}
	
	//Adds text field
	protected void addTextField(JTextField textf, String text, MouseAdapter mouseHandler,
			int xPos, int yPos, int width, int height) {
	    Font f = new Font("bebas",Font.PLAIN,16);
	    componentsArray.add(new MyComponent(textf, null, xPos, yPos, width, height));
	    changeLabelSize(textf, null, xPos, yPos, width, height);
	    textf.setBorder(null);
	    textf.setText(text);
	    textf.setOpaque(false);
	    textf.setForeground(Color.WHITE);
	    textf.setFont(f);
	    textf.addMouseListener(mouseHandler); 
	    getLayeredPane().add(textf, new Integer(2));
	}
	
	// Adds password field 
	protected void addPasswordField(JPasswordField password,MouseAdapter mouseHandler,int xPos, int yPos, int width, int height) {
		Font f = new Font("bebas",Font.PLAIN,16);
		componentsArray.add(new MyComponent(password, null, xPos, yPos, width, height));
	    changeLabelSize(password, null, xPos, yPos, width, height);
	    password.setBorder(null);
	    password.setText("Write your password");
	    password.setForeground(Color.WHITE);
	    password.setFont(f);
	    password.setOpaque(false);
	    password.addMouseListener(mouseHandler); 
	    password.setEchoChar((char) 0);
	    getLayeredPane().add(password, new Integer(2));
	}
	
	// Loads new font to the app
	protected void loadFont() {
    	try {
			InputStream fontLocation = getClass().getResourceAsStream("/media/login/BEBAS.ttf");
			Font fontRaw = Font.createFont(Font.TRUETYPE_FONT, fontLocation); 
			Font bebas = fontRaw.deriveFont(Font.PLAIN, 16);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(bebas);
			
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// Changing the size of all the components on the window due to window resize
	protected void compResized(List<MyComponent> components, ImageIcon layer1Img, ImageIcon backgroundImg) {

		if(foregroundLabel != null) {
			int width = (int)getWidth();
			int height = (int)getHeight();
			foregroundLabel.setBounds(0, 0, width, height);
			foregroundLabel.setIcon(scaleImage(layer1Img, width, height));
			backgroundLabel.setBounds(0, 0, width, height);
			backgroundLabel.setIcon(scaleImage(backgroundImg, width, height));
		    
			widthProp = (double)width / (double)DEAFAULT_WIDTH; 
			heightProp = (double)height / (double)DEAFAULT_HEIGHT; 

			for(int i = 0; i < components.size(); i++) {
				changeLabelSize(components.get(i).getComp(), components.get(i).getImage(), components.get(i).getOriginalX(), components.get(i).getOriginalY(),
						components.get(i).getOriginalWidth(), components.get(i).getOriginalHeight());
			}
		}
	}
	
}
