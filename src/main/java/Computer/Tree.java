package Computer;

import java.awt.Point;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import Frames.Check;
import Frames.Mate;
import Logic.Board;
import Logic.Colors;
import Logic.Vector;
import Logic.Zobrist;
import Tools.*;

public class Tree
{
	private int nodesNum; 
	private final int futureMoves = 4;
//	private Stack <Board> boardStack;
	private List<Point> middleBoard = new ArrayList<Point>();
	//private Hashtable<Long, Integer> knownPositions = new Hashtable<>((int)Math.pow(2,20));
	
	public Tree()
	{
		nodesNum = 0;
//		boardStack = new Stack<Board>();
		middleBoard.add(new Point(5,5));
		middleBoard.add(new Point(5,6));
		middleBoard.add(new Point(6,5));
		middleBoard.add(new Point(6,6));
	}
	
	//-----------------------Used for finding bug------------------------------//
//	public boolean checkPawn(Tool[][] tool, List<Tool> tools) {
//		for(Tool t: tools) {
//			if(t instanceof Pawn)
//				if(((Pawn)t).irRegularities(tool))
//					return true;
//		}
//		return false;
//	}
	//-------------------------------------------------------------------------//
	
	public void setNumberOfNodes(int num) {
		this.nodesNum = num;
	}
		
	//Calculates the next step of computer.
	public TreeNode minMax(Board board, int level, double alpha, double beta, boolean maximizingPlayer) {
		Colors turn = board.getCurrentPlayerColor();
		
		//If we reached to leaf or mate - evaluate the position and return it.
		if(level == futureMoves || board.getMate() != Mate.NONE) {
			TreeNode node = new TreeNode(maximizingPlayer);
			node.setEvaluation(evaluationFunction(board));
			return node;
		}
		
		//Get the list of all tools with the color of the current turn
		List<Tool> tools = new ArrayList<Tool>(board.getTools(turn));
		int row, col;
		TreeNode currNode = new TreeNode(maximizingPlayer);
			
//		boardStack.push(board); 

		for(Tool t: tools) {
			row = t.getLocation().x;
			col = t.getLocation().y;
			
//			try { board = (Board) boardStack.peek().clone(); } 
//			catch (CloneNotSupportedException e) { e.printStackTrace(); } 
			
			List<Point> canMoveTo = new ArrayList<Point>(board.getRestrictedCanMoveToList(row - Board.WALL_SIZE,col - Board.WALL_SIZE));
			
			//For each available move of the tool t:
			for(Point destSquareCoordinates: canMoveTo){		
				nodesNum++;
				Tool eatenTool = board.getToolExec(destSquareCoordinates.x, destSquareCoordinates.y);
				Tool source = board.getToolExec(row, col);
				
//				System.out.println("\n\n\n\nperform " + "(" + row + "," + col + ") to (" + destSquareCoordinates.x + "," + destSquareCoordinates.y + ")");
				
				//Make move
				board.makeMove(t.getLocation() ,destSquareCoordinates);
				
				//Calculate Zobrist hash
				long hash = Zobrist.hash;
				
//				Check check = board.getCheck();
//				Mate mate = board.getMate();
				
				//Change turn
				board.setCurrentPlayerColor(board.getCurrentPlayerColor() == Colors.WHITE ? Colors.BLACK:Colors.WHITE);
				
				//Call next tree level
				TreeNode son = minMax(board,level + 1,alpha, beta, !maximizingPlayer);
				
				//change turn
				board.setCurrentPlayerColor(board.getCurrentPlayerColor() == Colors.WHITE ? Colors.BLACK:Colors.WHITE);
				
//				System.out.println("\n\n\n\nrestore " + "(" + row + "," + col + ") to (" + destSquareCoordinates.x + "," + destSquareCoordinates.y + ")");
				
				//Undo move
				board.restoreMove(destSquareCoordinates, new Point(row, col), source, eatenTool, hash); 
				
				
				// Min-Max decision & pruning
				if(maximizingPlayer) {
					if(currNode.getEvaluation() < son.getEvaluation()) {
						currNode.setToolIdx(new Point(row ,col));
						currNode.setToIdx(destSquareCoordinates);
						currNode.setEvaluation(son.getEvaluation());
					}
					alpha = Math.max(alpha,son.getEvaluation());
					if(beta <= alpha) {
//						board = boardStack.pop();
						return currNode;
					}
				}else {
					if(currNode.getEvaluation() > son.getEvaluation()) {
						currNode.setToolIdx(new Point(row ,col));
						currNode.setToIdx(destSquareCoordinates);
						currNode.setEvaluation(son.getEvaluation());
					}
					beta = Math.min(beta, son.getEvaluation());
					if(beta <= alpha) {
//						board = boardStack.pop();
						return currNode;
					}

				}
				
//				try { board = (Board) boardStack.peek().clone(); } 
//				catch (CloneNotSupportedException e) { e.printStackTrace(); } 
			}
		}
//		board = boardStack.pop();
		return currNode;
	}
	
	public int getFutureMoves() {
		return futureMoves;
	}
	
	private double openingEvaluation(List<Tool> tools, Colors turn, Board board) {
		double score = 0;
		Mate mate = board.getMate();
		
		for(Tool t: tools) {
			score += t.getMyValue();
			if(t instanceof Pawn) {
				if(t.protectedByOthers.size() > 0)
					score += 5;
			}
			if(t instanceof Knight) {
				score += (t.canMoveTo.size() + t.Iprotect.size() - 2);
			}
			if(t instanceof Bishop) {
				score += t.canMoveTo.size() + t.Iprotect.size();
			}
			if(t instanceof King && ((King)t).isDidRooking()) 
				score += 10;
			if(middleBoard.contains(t.getLocation()))
				score += 2;		
			if(t.threatsOnMe.size() > 0 && t.protectedByOthers.size() == 0)
				score -= t.getMyValue();
			for(Vector v: t.Ithreat) {
				Point victimCoor = v.getSource();
				Tool victim = board.getToolExec(victimCoor.x,victimCoor.y);
				if(victim.getMyValue() > t.getMyValue() && t.protectedByOthers.size() > 0)
					score += victim.getMyValue();
			}

		}
		for(Point p: middleBoard)
			score += board.getToolExec(p.x, p.y).getThreatCount(turn == Colors.WHITE? Colors.BLACK:Colors.WHITE) ;
		if(mate == Mate.onWHITE && turn == Colors.BLACK)
			score += 10000;
		if(mate == Mate.onBLACK && turn == Colors.WHITE)
			score += 10000;
		return score;
	}
	
	private double middleGameEvaluation(List<Tool> myTools, List<Tool> opponentTools, Colors turn, Board board) {
		double score = 0;
		Mate mate = board.getMate();
		
		for(Tool t: myTools) {
			score += t.getMyValue();
			
			if(middleBoard.contains(t.getLocation()))
				score += 2;		
			if(t.protectedByOthers.size() > 0)
				score += 5;
			if(t.threatsOnMe.size() == 0)
				score += (t.canMoveTo.size() + t.Iprotect.size()) ;
			if(t.threatsOnMe.size() > 0 && t.protectedByOthers.size() == 0)
				score -= t.getMyValue();
			for(Vector v: t.Ithreat) {
				Point victimCoor = v.getSource();
				Tool victim = board.getToolExec(victimCoor.x,victimCoor.y);
				if(victim.getMyValue() > t.getMyValue() && t.protectedByOthers.size() > 0)
					score += victim.getMyValue();
			}
			
			score += (t.canMoveTo.size() + t.Ithreat.size()) ;
		}
		for(Tool t: opponentTools) {
			if(t.threatsOnMe.size() >= 2) 
				score += t.getMyValue() ;
		}
		for(Point p: middleBoard)
			score += board.getToolExec(p.x, p.y).getThreatCount(turn == Colors.WHITE? Colors.BLACK:Colors.WHITE) ;
		if(mate == Mate.onWHITE && turn == Colors.BLACK)
			score += 10000;
		if(mate == Mate.onBLACK && turn == Colors.WHITE)
			score += 10000;
		return score;
	}
	
	private double endGameEvaluation(List<Tool> myTools, List<Tool> opponentTools, Colors turn, Board board) {
		double score = 0;
		Mate mate = board.getMate();
		
		for(Tool t: myTools) {
			score += t.getMyValue();
	
			if(t.protectedByOthers.size() > 0)
				score += 5;
			if(t.threatsOnMe.size() == 0)
				score += (t.canMoveTo.size() + t.Iprotect.size()) ;
			if(t instanceof Pawn && t.getColor() == Colors.WHITE && turn == Colors.WHITE) 
				score += t.getLocation().x ;
			if(t instanceof Pawn && t.getColor() == Colors.BLACK && turn == Colors.BLACK) 
				score += (11 - t.getLocation().x) ;
			if(t.threatsOnMe.size() > 0 && t.protectedByOthers.size() == 0)
				score -= t.getMyValue();
			for(Vector v: t.Ithreat) {
				Point victimCoor = v.getSource();
				Tool victim = board.getToolExec(victimCoor.x,victimCoor.y);
				if(victim.getMyValue() > t.getMyValue() && t.protectedByOthers.size() > 0)
					score += victim.getMyValue();
			}
			
		}
		for(Tool t: opponentTools) {
			if(t.threatsOnMe.size() >= 2) 
				score += t.getMyValue() ;
		}
		if(mate == Mate.onWHITE && turn == Colors.BLACK)
			score += 10000;
		if(mate == Mate.onBLACK && turn == Colors.WHITE)
			score += 10000;
		return score;
	}
	
	//This function evaluate the current position and returns it's evaluation.
	double evaluationFunction(Board board)
	{
		double whiteScore = 0, blackScore = 0;
		Colors turn = board.getCurrentPlayerColor();
		int move = board.getNumberOfMoves();
		List<Tool> whiteTools = board.getWhiteTools();
		List<Tool> blackTools = board.getBlackTools();
		
		//Opening game
		if(move < 20)
		{
			for(int i = 0; i < 2; i++) {
				if(turn == Colors.WHITE)
					whiteScore = openingEvaluation(whiteTools, turn, board);
				else
					blackScore = openingEvaluation(blackTools, turn, board);
				turn =(turn == Colors.WHITE ? Colors.BLACK : Colors.WHITE);
			}
		}
		
		//Middle game
		if(move >= 20 && move <= 40)
		{
			for(int i = 0; i < 2; i++) {
				if(turn == Colors.WHITE)
					whiteScore = middleGameEvaluation(whiteTools, blackTools, turn, board);
				else
					blackScore = middleGameEvaluation(blackTools, whiteTools, turn, board);
				turn =(turn == Colors.WHITE ? Colors.BLACK : Colors.WHITE);		
			}
		}
		
		//End game
		if(move > 40)
		{
			for(int i = 0; i < 2; i++) {
				if(turn == Colors.WHITE)
					whiteScore = endGameEvaluation(whiteTools, blackTools, turn, board);
				else
					blackScore = endGameEvaluation(blackTools, whiteTools, turn, board);
				turn =(turn == Colors.WHITE ? Colors.BLACK : Colors.WHITE);	
			}
		}		
		
		//Add draw handling
		

		turn =(turn == Colors.WHITE ? Colors.BLACK : Colors.WHITE);	
		if(board.getMate() == Mate.Draw) {
			if(turn == Colors.WHITE && whiteScore > blackScore)
				whiteScore -=1000;
			if(turn == Colors.BLACK && whiteScore < blackScore)
				blackScore -=1000;
		}
		if(turn == Colors.WHITE)
			return whiteScore - blackScore;
		return blackScore - whiteScore;
	}

	public int getNodesNum() {
		return nodesNum;
	}
}
