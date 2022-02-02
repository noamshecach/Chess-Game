package Logic;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import SentObjects.AbstractTool;
import Tools.*;

//This class reads the XML representation of the opening
public class XMLRead extends DefaultHandler {
	
	private Board board;
	private String temp;
	private String tool;
	private Colors color;
	private int X,Y;
	private boolean isBlack = true, isForClientSide = false;
	private List<AbstractTool> aTools;
	
	//Constructor for the server calculation 
	public XMLRead(Board b, Colors myColor) {
		this.board = b;
		if(myColor == Colors.WHITE)
			isBlack = false;
	}
	
	//Constructor for client response that the server sent to the client
	public XMLRead(Colors color) {
		if(color == Colors.WHITE)
			isBlack = false;
		this.aTools = new ArrayList<AbstractTool>();
		//-----------------------------//
		this.isForClientSide = true;
		//----------------------------//
	}
	
	//Called for each element start
	@Override
	public void startElement(String uri, String localName, 
			String qName, Attributes atts) {
		if(qName.equals("Rook") || qName.equals("Bishop") || qName.equals("Knight") ||
				qName.equals("King") || qName.equals("Queen") || qName.equals("Pawn")	) { 
			tool = qName;
		}
	}
	
	//Called for each element end
	@Override
	public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("color")) {
        		setColor();
     } else 
    	 if (qName.equalsIgnoreCase("Xposition")) {
           	X =  Integer.parseInt(temp);  //column

     } 
     else 
    	 if (qName.equalsIgnoreCase("Yposition")) {
    	 	if(isBlack)
    	 		Y =  Integer.parseInt(temp);  //row
    	 	else
    	 		Y =  7 - Integer.parseInt(temp);
	 		if(!isForClientSide)
	 			setTool();
	 		else
	 			aTools.add(new AbstractTool(tool, color, Y, X));
     	}
	}
	
	private void  setColor() {
        if(temp.equals("white"))
        	color = Colors.WHITE;
        else
        	color = Colors.BLACK;
	}
	
	//Creates Tool according to the fields values 
	private void setTool() {
		X += Board.WALL_SIZE;
		Y += Board.WALL_SIZE;
 		if(tool.equals("Rook"))
 			board.setTool(new Rook(color, new Point(Y, X)), color);
 		if(tool.equals("Queen"))
 			board.setTool(new Queen(color, new Point(Y, X)), color);
 		if(tool.equals("Pawn"))
 			board.setTool(new Pawn(color, new Point(Y, X)), color);
 		if(tool.equals("Knight"))
 			board.setTool(new Knight(color, new Point(Y, X)), color);
 		if(tool.equals("Bishop"))
 			board.setTool(new Bishop(color, new Point(Y, X)), color);
 		if(tool.equals("King")) {
 			King king = new King(color, new Point(Y, X));
 			board.setTool(king, color);
 			board.updateKingLocation(king, color);
 		}
	}
	
	@Override
	public void characters(char[] buffer, int start, int length) {
		
		temp = new String(buffer, start, length);
	}
	
	public List<AbstractTool> getAbstractToolList() {
		return this.aTools;
	}
}
