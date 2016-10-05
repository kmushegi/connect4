// MinimaxPlayer.java

/*
 *
 * @author  Sean Bridges
 * @version 1.0
 *
 * The MinimaxPlayer uses the minimax algorithm to determine what move it should 
 * make.  Looks ahead depth moves.  The number of moves will be on the order
 * of n^depth, where n is the number of moves possible, and depth is the number
 * of moves the engine is searching.  Because of this the minimax player periodically
 * polls the thread that calls getMove(..) to see if it was interrupted.  If the thread
 * is interrupted, the player returns null after it checks.  
 */

/*
 * Adapted by Grace Handler and Kote Mushegian for Professor Majercik's 
 * Artificial Intellgience class at Bowdoin College. Minimax player can
 * run minimax or optimized minimax with alpha-beta pruning by setting 
 * the boolean variable ALPHA_BETA_PRUNING. Where the code was edited
 * is noted by comments that begin with EDITED. To clarify the variables
 * called maxValue/minValue, maxValue is beta and minValue is alpha.
 */


public class MinimaxPlayer extends DefaultPlayer
{
	
//----------------------------------------------
    //instance variables
    
    //the number of levels minimax will look ahead
    private int depth = 1;
    private Player minPlayer;
    
//----------------------------------------------
    //constructors
    
    /** Creates new MinimaxPlayer */
    public MinimaxPlayer(String name, int number, Player minPlayer) 
    {
	super(name, number);
	
	this.minPlayer = minPlayer;
    }
    
//----------------------------------------------
    //instance methods
    
    /**
     * Get the number of levels that the Minimax Player is currently looking 
     * ahead.
     */
    public int getDepth()
    {
	return depth;
    }
    
    /**
     * Set the number of levels that the Minimax Player will look ahead 
     * when getMove is next called
     */
    public void setDepth(int anInt)
    {
	depth = anInt;
    }
    
    /** Passed a copy of the board, asked what move it would like to make.
     *
     * The MinimaxPlayer periodically polls the thread that makes this call to 
     * see if it is interrupted.  If it is the player returns null.
     *
     * Looks ahead depth moves.
     */
    public Move getMove(Board b)
    {
	MinimaxCalculator calc = new MinimaxCalculator(b,this,minPlayer);
	return calc.calculateMove(depth);
    }
    
    
}//end MinimaxPlayer



/**
 * The MinimaxCalculator does the actual work of finding the minimax move.
 * A new calculator should be created each time a move is to be made.
 * A calculator should only be used once.
 */
final class MinimaxCalculator
{

//-------------------------------------------------------
    //instance variables
    
    //the number of moves we have tried
    private int moveCount = 0;
    private long startTime;
    
    private Player minPlayer;
    private Player maxPlayer;
    private Board board;
    
    private final int MAX_POSSIBLE_STRENGTH;
    private final int MIN_POSSIBLE_STRENGTH;

    private int maxIndex = -1; //EDITED
    private int maxValue = Integer.MAX_VALUE; //EDITED
    private int minValue = Integer.MIN_VALUE; //EDITED

    private final boolean ALPHA_BETA_PRUNING = true; //EDITED
    
//-------------------------------------------------------
    //constructors
    MinimaxCalculator(Board b, Player max, Player min)
    {
	board = b;
	maxPlayer = max;
	minPlayer = min;
	
	MAX_POSSIBLE_STRENGTH = board.getBoardStats().getMaxStrength();  // Integer.MAX_VALUE
	MIN_POSSIBLE_STRENGTH = board.getBoardStats().getMinStrength();  // Integer.MIN_VALUE
    }
    
//-------------------------------------------------------
    //instance methods
    
    /** 
     * Calculate the move to be made.
     */
    public Move calculateMove(int depth)
    {
		startTime = System.currentTimeMillis();
		
		// we have a problem, Houston...
		if(depth == 0)
		{
			System.out.println("Error, 0 depth in minumax player");
			Thread.dumpStack();
			return null;
		}
		
		Move[] moves = board.getPossibleMoves(maxPlayer);

		int maxV = Integer.MIN_VALUE; //EDITED
		int currentV = 0; //EDITED

		// explore each move in turn
		for(int i = 0; i < moves.length; i++) {

			if(board.move(moves[i])) {  // move was legal (column was not full
			    //EDITED THE FOLLOWING CODE through the end of the if-statement

				moveCount++;  // global variable
				
				currentV = expandMinNode(depth); //begin recursive call
				
				if(currentV > maxV) { //update maxIndex accordingly (best move)
					maxV = currentV;
					maxIndex = i;
				}

				board.undoLastMove();   // undo exploratory move

	 		}  //end if move made
			
			// if the thread has been interrupted, return immediately.
			if(Thread.currentThread().isInterrupted()) {
				return null;
			}
			
		}//end for all moves
		
		long stopTime = System.currentTimeMillis();
		// System.out.println("Number of moves tried = " + moveCount + 
		// 		   "  Time = " + (stopTime -  startTime) + " milliseconds");

		System.out.println(moveCount + 
				   "  "+(stopTime -  startTime)); //EDITED for purposes of data collection
		
		// maxIndex is the index of the move to be made
		return moves[maxIndex];
    } //end calculateMove
    
    /**
     * A max node returns the maximum score of its descendents.
     */
    private int expandMaxNode(int depth) {
		
		//EDITED to call terminal test method/cutoff test method
	   	if(terminalTest(depth)) {
			return board.getBoardStats().getStrength(maxPlayer);
		}
		
		// if not
		Move[] moves = board.getPossibleMoves(maxPlayer);
		
		int v = Integer.MIN_VALUE;
		// explore each move in turn

		//EDITED the following code 
		for(int i = 0; i < moves.length; i++)
		    {
			if(board.move(moves[i])) {  // move was legal (column was not full)
			   
				moveCount++;  // global variable

				v = Math.max(v,expandMinNode(depth-1)); // compares v to recursive call on min

				if(ALPHA_BETA_PRUNING) {
					if(v >= maxValue) { //when v is larger than beta
						return v; //nothing is better, so return
					}
					minValue = Math.max(minValue,v); //otherwise, update alpha
				}

				board.undoLastMove();   // undo exploratory move
			    
			    }  //end if move made
			
		    }//end for all moves

		return v;
	
    }//end expandMaxNode
    
    


    /**
     * A max node returns the minimum score of its descendents.
     */
    private int expandMinNode(int depth) {
    
		//EDITED to call terminal test method/cutoff test method
	    if(terminalTest(depth)) {
			return board.getBoardStats().getStrength(maxPlayer);
		}
		
		// if not
		Move[] moves = board.getPossibleMoves(minPlayer);

		//EDITED below code snippet, very similar to expandMaxNode

		int v = Integer.MAX_VALUE;

		// explore each move in turn
		for(int i = 0; i < moves.length; i++)
		    {
			if(board.move(moves[i])) {    // move was legal (column was not full)
				moveCount++;  // global variable

				v = Math.min(v,expandMaxNode(depth-1)); //recursive call

				if(ALPHA_BETA_PRUNING) {
					if(v <= minValue) { //compare v to alpha
						return v; //if nothing better, return
					}
					maxValue = Math.min(maxValue,v); //compare v to beta
				}

				board.undoLastMove();   // undo exploratory move
			}  //end if move made
			
		}//end for all moves
		return v;
	
    }//end expandMaxNode

    //EDITED: added this method to detect when terminal test/cutoff test is true
    private boolean terminalTest(int depth) {
    	if(board.isGameOver() || depth == 0) { //game is over or depth is exhausted
    		return true;
    	}
    	return false;
    }
    
}