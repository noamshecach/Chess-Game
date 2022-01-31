package Logic;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import Computer.Tree;
import Computer.TreeNode;
import Frames.Check;
import Frames.Mate;
import SentObjects.AvailableMoves;
import SentObjects.Movement;
import Tools.*;

public class Board implements Cloneable {

	public static final int WALL_SIZE = 2, SIZE = 8;
	private Tool[][] tool;
	private Colors currPlayerColor = Colors.WHITE;	
	private Check check = Check.NONE;
	private Mate mate = Mate.NONE;
	private int numberOfMoves = 0;
	private List<Tool[][]> boardArray = new ArrayList<Tool[][]>(); 
	private Point blackKing, whiteKing;
	// # of white tools, # of white bishops, # of white knights, # of black tools, # of black bishops, # of black knights, # moves without using pawn, #last value of drawInfo[6]
	private byte[] drawInfo = {16,2,2,16,2,2,0,0}; 
	private List<Tool> whiteTools = new ArrayList<Tool>();
	private List<Tool> blackTools = new ArrayList<Tool>();
	private boolean computerRun = false;
	private Stack<Byte> movesWithoutPawn = new Stack<Byte>();
	private Hashtable<Long, Integer> positionHistory = new Hashtable<>(100);
	private Tree computerMoves;
		
	public Board() {
		
	}
	
	public Board(Tree tree) {
		this.computerMoves = tree;
	}
	
	public Tool[][] getToolBoard(){
		return tool;
	}
	
	public List<Tool> getTools(Colors toolsColor){
		if(toolsColor == Colors.WHITE)
			return getWhiteTools();
		return getBlackTools();
	}
	
	public List<Tool> getWhiteTools() {
		return whiteTools;
	}
	
	public List<Tool> getBlackTools() {
		return blackTools;
	}
	
	public void setCurrentPlayerColor(Colors currentPlayerColor) {
		this.currPlayerColor = currentPlayerColor;
	}
	
	public Colors getCurrentPlayerColor() {
		return this.currPlayerColor;
	}
	
	public void updateKingLocation(King king, Colors color) {
		if(king.getColor() == Colors.WHITE)
			whiteKing = king.getLocation();
		else
			blackKing = king.getLocation();
	}
	
	public King getKing(Colors color) {
		if(color == Colors.BLACK) 
			return (King) tool[blackKing.x][blackKing.y];
		return (King) tool[whiteKing.x][whiteKing.y];
	}
	
	public void setToolsArray(Tool[][] tools) {
		this.tool = tools;
	}
	
	public Movement requestComputerMove(Player[] players, Game game) {
		//Tree tree = new Tree();
		computerRun = true;
		computerMoves.setNumberOfNodes(0);
		TreeNode node = computerMoves.minMax(this,0, -Double.MAX_VALUE, Double.MAX_VALUE, computerMoves.getFutureMoves() % 2 ==0 ? false: true);
		computerRun = false;
		System.out.print(computerMoves.getNodesNum() + " nodes \n" );
		System.out.println("Comp: From [" + (node.getToolIdx().x - 2) + "," + (node.getToolIdx().y - 2) + "]");
		System.out.println("      To [" + (node.getToIdx().x - 2) + "," + (node.getToIdx().y - 2) + "]");
		return makeMove(node.getToolIdx(),node.getToIdx());
	}
	
	public void printBoard() {	
		System.out.println("\n");
		for(int row = 0; row < SIZE ; row++) {
			for(int col = 0; col < SIZE  ; col++) {
				System.out.print(tool[row+ WALL_SIZE][col+ WALL_SIZE]);
			}
			System.out.println("");
		}
		System.out.println("\n");
	}
	
	public Tool[][] getToolsArray() {
		return tool;
	}
	
	public void buildBoard() {
		tool = new Tool[SIZE + WALL_SIZE * 2][SIZE + WALL_SIZE * 2];
		try {
			File inputFile = new File(System.getProperty("user.dir") + "///tools.txt");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLRead userhandler = new XMLRead(this, currPlayerColor);
			saxParser.parse(inputFile, userhandler);
		}catch(Exception e ) { e.printStackTrace();}
		
		for(int row = 0; row < SIZE + WALL_SIZE * 2; row++) {
			for(int col = 0; col < SIZE + WALL_SIZE * 2 ; col++) {
				if (row < WALL_SIZE || col < WALL_SIZE || col > SIZE + 1 || row > SIZE + 1 )
					tool[row][col] = new Wall(new Point(row, col));
				else if(tool[row][col] == null)
					tool[row][col] = new Empty(new Point(row, col));
			}
		}
		Zobrist.initiateKeys();
		positionHistory.put( Zobrist.getHash(whiteTools,blackTools), 1);
		calculatePossibleMoves();
		//printMoves();
	}
	
	private void calculatePossibleMoves() {
		
		for (int row = 2; row < SIZE + 2; row++) {
			for (int col = 2; col < SIZE + 2; col++) {
				if(!(tool[row][col] instanceof Empty))
					tool[row][col].updateLists(tool);
			}
		}
	}
	
	public Tool getTool(int row, int col) {
		return tool[row + WALL_SIZE][col + WALL_SIZE];
	}
	
	//without adding walls
	public Tool getToolExec(int row, int col) {
		return tool[row][col];
	}
	
	public <T extends Tool> void  setTool(T t, Colors color) {
		tool[t.getLocation().x][t.getLocation().y] =  t;
		if(color == Colors.WHITE)
			whiteTools.add(tool[t.getLocation().x][t.getLocation().y]);
		else
			blackTools.add(tool[t.getLocation().x][t.getLocation().y]);
	}
	
	public List<Point> getRestrictedCanMoveToList(int col, int row) {
		Tool pressedTool = getTool(col, row);
		
		if(pressedTool instanceof King)
			return pressedTool.canMoveTo;
		
		// Returns only the moves that are not exposing the king.
		List<Point> canMoveTo = pressedTool.getPossibleMoves(tool, getKing(pressedTool.getColor()).getLocation());
		
		if(canMoveTo.size() == 0)
			return canMoveTo;
		
		if(check != Check.NONE ) {
			// Restriction list contains only the squares that can be considered as a next move to go out from check status.
			List<Point> restrictions = new ArrayList<Point>();
			King king = getKing(pressedTool.getColor());
			if(king.threatsOnMe.size() > 1)
				return new ArrayList<Point>();
			//Adding the squares between the king to his threat included, to the restriction list.
			for(Vector v: king.threatsOnMe) {
				Point dir = v.getDirection();
				int i = 1;
				while(tool[king.getLocation().x + dir.x * i][king.getLocation().y + dir.y * i] instanceof Empty ) {
					restrictions.add(new Point(king.getLocation().x + dir.x * i, king.getLocation().y + dir.y * i));
					i++;
				}
				restrictions.add(new Point(king.getLocation().x + dir.x * i, king.getLocation().y + dir.y * i));
			}
			//Returns only the squares that can be accessed via the tool and are found in the restriction list
			canMoveTo = pressedTool.getPossibleMoves(canMoveTo, getKing(pressedTool.getColor()).getLocation()  , restrictions);
		}
		return canMoveTo;
	}

	public AvailableMoves getPossibleMoves(int col, int row)
	{
		AvailableMoves moves =  new AvailableMoves();
		List<Point> canMoveTo = getRestrictedCanMoveToList(col, row);
		
		//Dividing the possible destinations to empty squares and adversary squares.
		for(Point p : canMoveTo){
			if( !(tool[p.x][p.y] instanceof Empty) ) {
				if(currPlayerColor == Colors.WHITE)
					moves.addCanEat(new Point(p.x - WALL_SIZE, p.y - WALL_SIZE));
				else
					moves.addCanEat(new Point(7- (p.x - WALL_SIZE), p.y - WALL_SIZE));
			}
			else {
				if(currPlayerColor == Colors.WHITE)
					moves.addCanMoveTo(new Point(p.x - WALL_SIZE, p.y - WALL_SIZE));
				else
					moves.addCanMoveTo(new Point(7 - (p.x - WALL_SIZE), p.y - WALL_SIZE));
			}
		}
		return moves;	
	}
	

	public Tool[][] getTool() {
		return tool;
	}	

	private void updateMovesWithoutUsingPawn(Point curSq) {
		movesWithoutPawn.push(drawInfo[6]);
		if(!(tool[curSq.x][curSq.y] instanceof Pawn)) 
			drawInfo[6]++;
		else 
			drawInfo[6] = 0;	
	}
	
	public void updateDrawInfo(Tool destSquare, int value) {
		if(currPlayerColor == Colors.WHITE) {
			drawInfo[3] += value;  // # of black tools decreased by 1
			if(destSquare instanceof Bishop)
				drawInfo[4] += value;
			if(destSquare instanceof Knight)
				drawInfo[5]+= value;
		}
		if(currPlayerColor == Colors.BLACK) {
			drawInfo[0] += value;  // # of black tools decreased by 1
			if(destSquare instanceof Bishop)
				drawInfo[1] += value;
			if(destSquare instanceof Knight)
				drawInfo[2] += value;
		}
	}
	
	
	public Movement makeMove(Point curSq, Point toSq) {
		numberOfMoves++;
				
		if (check != Check.NONE)
			check = Check.NONE;	
		
		// Update Draw Information
		updateMovesWithoutUsingPawn(curSq);
		if(!(tool[toSq.x][toSq.y] instanceof Empty) &&   tool[toSq.x][toSq.y].getColor() != currPlayerColor ) {
			updateDrawInfo(tool[toSq.x][toSq.y], -1);
			if(currPlayerColor == Colors.WHITE) 
				blackTools.remove(tool[toSq.x][toSq.y]);
			else 
				whiteTools.remove(tool[toSq.x][toSq.y]);
		}
		
		long hash = Zobrist.getHash(curSq, toSq, tool[curSq.x][curSq.y].getId());
		if(positionHistory.get(hash) == null)
			positionHistory.put(hash, 1);
		else
			positionHistory.put(hash, positionHistory.get(hash) + 1);
		
		// Pawn Promotion    or   execute regular movement
		boolean isPromotion = false;
		if(tool[curSq.x][curSq.y] instanceof Pawn && tool[toSq.x][toSq.y].isFinalRow()) {
			System.out.println("Promotion");
			tool[toSq.x][toSq.y] = new Queen(tool[curSq.x][curSq.y].getColor(), toSq, tool[toSq.x][toSq.y].threatsOnMe, tool[toSq.x][toSq.y].protectedByOthers, tool[curSq.x][curSq.y].getNumberOfMoves() + 1);
			tool[curSq.x][curSq.y].removeFromBoard(tool);
			tool[toSq.x][toSq.y].updateLists(tool);
			tool[toSq.x][toSq.y].fixAffectedTools(tool);
			isPromotion = true;
		}else
			tool[curSq.x][curSq.y].moveTo(tool, toSq);
		
		
		//Update King Location
		if(tool[toSq.x][toSq.y] instanceof King)
			updateKingLocation((King)tool[toSq.x][toSq.y], currPlayerColor);
	
		// Update King possible Moves
		if(currPlayerColor == Colors.WHITE)
			((King)tool[blackKing.x][blackKing.y]).updateLists(tool, whiteKing);
		else
			((King)tool[whiteKing.x][whiteKing.y]).updateLists(tool, blackKing);
		
		//Check Win
		King examinedKing;
		if(currPlayerColor == Colors.BLACK)
			examinedKing = (King)tool[whiteKing.x][whiteKing.y];
		else
			examinedKing = (King)tool[blackKing.x][blackKing.y];
		
		mate = examinedKing.isMate(tool);
		if(mate == Mate.NONE)
			check = examinedKing.isCheck();
		if(mate == Mate.NONE && check == Check.NONE)
			mate = isDraw();

		//printMoves();
		
		if(!computerRun) {
			Point[] lastMove = {curSq, toSq};
			return new Movement(lastMove, mate, check, 
					(tool[toSq.x][toSq.y] instanceof King && Math.abs(curSq.y - toSq.y) == 2)? true:false, isPromotion);
		}else return null;
		
	}
	
	public void restoreMove(Point curSq, Point toSq, Tool srcBeforeMove, Tool deletedTool, long hash) {
		numberOfMoves--;
		drawInfo[6] = movesWithoutPawn.pop();
		
		if(!(deletedTool instanceof Empty) &&   deletedTool.getColor() != currPlayerColor ) {
			updateDrawInfo(deletedTool, 1);
			if(currPlayerColor == Colors.WHITE)
				blackTools.add(deletedTool);
			else
				whiteTools.add(deletedTool);
		}
		
		int value = positionHistory.get(hash);
		if(value == 1)
			positionHistory.remove(hash);
		else
			positionHistory.put(hash, value - 1);
		
		// Pawn Promotion   or    restore movement
		if(srcBeforeMove instanceof Pawn && tool[toSq.x][toSq.y] instanceof Queen) {
			tool[toSq.x][toSq.y].removeFromBoard(tool);
			srcBeforeMove.placeAt(tool, srcBeforeMove.getLocation());
		}else{
			tool[curSq.x][curSq.y].goBackTo(tool, toSq);
			
			if(!(deletedTool instanceof Empty)) {
				deletedTool.placeAt(tool, deletedTool.getLocation());
			}
		}
		
		//Update King Location
		if(tool[toSq.x][toSq.y] instanceof King)
			updateKingLocation((King)tool[toSq.x][toSq.y], currPlayerColor);

	
		// Update King possible Moves
		if(currPlayerColor == Colors.BLACK)
			((King)tool[blackKing.x][blackKing.y]).updateLists(tool, whiteKing);
		else
			((King)tool[whiteKing.x][whiteKing.y]).updateLists(tool, blackKing);
		
		//Check Win
		King examinedKing;
		if(currPlayerColor == Colors.BLACK)
			examinedKing = (King)tool[blackKing.x][blackKing.y];
		else
			examinedKing = (King)tool[whiteKing.x][whiteKing.y];
		
		mate = examinedKing.isMate(tool);
		if(mate == Mate.NONE)
			check = examinedKing.isCheck();
//		if(mate == Mate.NONE && check == Check.NONE)
//			mate = isDraw();
	}

	
	/**
	 * this function checks if there is a draw situation.
	 * @param turn - the current turn.
	 * @return true if there is a draw or false if not.
	 */
	private Mate isDraw()
	{
		if(check == Check.NONE && checkPat()  )
			return Mate.Draw;
		if(notEnoughMatireal())
			return Mate.Draw;
		if(tooMuchMoves())
			return Mate.Draw;
		if(isRepeatedBoard())
			return Mate.Draw;
		return Mate.NONE;
	}
	
	private boolean isRepeatedBoard() {
		if(positionHistory.get(Zobrist.hash) == 3)
			return true;
		return false;
	}
	
//	Tool[][] cpyBoard()
//	{
//		Tool[][] mat = new Tool[8][8];
//		for (int row = 2; row < SIZE ; row++)
//			for (int col = 2; col < SIZE ; col++) {
//				try { mat[row][col] = tool[row][col].clone(); } 
//				catch (CloneNotSupportedException e) { e.printStackTrace(); }
//			}
//		return mat;
//	}

	/**
	 * this function checks if there is situation of pat draw.
	 * @param turn - the current turn.
	 * @return true if there is a draw or flase if not.
	 */
	boolean checkPat()
	{
		List<Tool> tools = getTools(currPlayerColor == Colors.WHITE? Colors.BLACK: Colors.WHITE);
		for(Tool t: tools) 
			if(t.canMoveTo.size() > 0)
				return false;
		return true;
	}
	
	/**
	 * this function checks if there is enough material on the board to get
	 * to chess - mate.
	 * @return true if there is not enough material or false if not.
	 */
	boolean notEnoughMatireal()
	{
		if(drawInfo[0] <=2 && drawInfo[3] <= 2) {
			if(drawInfo[0] == 1  && drawInfo[3] == 1) // King vs King
				return true;
			if(drawInfo[0] == 2 && (drawInfo[1] == 1 || drawInfo[2] == 1) && drawInfo[3] == 1 ) //King and (Bishop or Knight) vs King
				return true;
			if(drawInfo[3] == 2 && (drawInfo[4] == 1 || drawInfo[5] == 1) && drawInfo[0] == 1 ) //King and (Bishop or Knight) vs King
				return true;
			if(drawInfo[3] == 2 && drawInfo[4] == 1  && drawInfo[0] == 2 && drawInfo[1] == 1 ) //King and Bishop vs King and Bishop
				return true;
		}
		return false;

	}
	
	/**
	 * this function checks if you did fifty moves without moving pawns.
	 * @return true if you did or false if not.
	 */
	boolean tooMuchMoves()
	{
		if(drawInfo[6] >= 50)
			return true;
		return false;
	}
	

	
	/**
	 * this function checks equality between two boards:
	 * the current board and the receive board.
	 * @param mat
	 * @return true if there is equality or false if not.
	 */
	boolean IsEqual(Tool mat[][])
	{
		for(int z = 2; z < 10; z++)
			for(int r = 2; r < 10; r++)
				if(!((mat[z][r] instanceof  Pawn && tool[z][r] instanceof  Pawn) ||
						(mat[z][r] instanceof  Knight && tool[z][r] instanceof  Knight) ||
						(mat[z][r] instanceof  Rook && tool[z][r] instanceof  Rook) ||
						(mat[z][r] instanceof  Bishop && tool[z][r] instanceof  Bishop) ||
						(mat[z][r] instanceof  Empty && tool[z][r] instanceof  Empty) ||
						(mat[z][r] instanceof  Queen && tool[z][r] instanceof  Queen) ||
						(mat[z][r] instanceof  King && tool[z][r] instanceof  King)))
					return false;
		return true;
	}
		
	public Check getCheck() {
		return check;
	}

	public void setCheck(Check check) {
		this.check = check;
	}

	public Mate getMate() {
		return mate;
	}

	public void setMate(Mate mate) {
		this.mate = mate;
	}

	public int getNumberOfMoves() {
		return this.numberOfMoves;
	}
	
//	public void printMoves() {
//		for(Tool[] row: tool) {
//			for(Tool t: row) {
//				if( t instanceof Wall)
//					continue;
//				System.out.println(t.details());
//			}
//			
//		}
//		System.out.println("---------------------------------------------------------------------------");
//	}

	public Object clone() throws CloneNotSupportedException {
		Board b = (Board) super.clone();
		
		b.tool = new Tool[SIZE + WALL_SIZE * 2][SIZE + WALL_SIZE * 2];
		b.whiteTools = new ArrayList<Tool>();
		b.blackTools = new ArrayList<Tool>();
		
		for(int row = 0; row < SIZE + WALL_SIZE * 2; row++) {
			for(int col = 0; col < SIZE + WALL_SIZE * 2 ; col++) {
				b.tool[row][col] = tool[row][col].clone();
				if(!(tool[row][col] instanceof Empty) &&  tool[row][col].getColor() == Colors.WHITE)
					b.whiteTools.add(b.tool[row][col]);
				if(!(tool[row][col] instanceof Empty) &&  tool[row][col].getColor() == Colors.BLACK)
					b.blackTools.add(b.tool[row][col]);
			}
		}
		b.currPlayerColor = currPlayerColor;
		b.check = check;
		b.mate = mate;
		b.numberOfMoves = numberOfMoves;
		
		b.boardArray = new ArrayList<Tool[][]>();
		for(Tool[][] boardElement: boardArray)
			b.boardArray.add(boardElement);
		
		b.blackKing = new Point(blackKing);
		b.whiteKing = new Point( whiteKing );
		
		b.drawInfo = new byte[8]; 
		for(int i = 0; i < drawInfo.length; i++) 
			b.drawInfo[i] = drawInfo[i];
		
		return b;
	}

}


