// MinimaxPlayer.java

 
/** 
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
	
	// explore each move in turn
	for(int i = 0; i < moves.length; i++)
	    {
		if(board.move(moves[i]))    // move was legal (column was not full)
		    {
			moveCount++;  // global variable
			
			// *************************************
			//              CODE NEEDED 
			//             (mostly here)  
			// *************************************

			board.undoLastMove();   // undo exploratory move
		    }  //end if move made
		
		// if the thread has been interrupted, return immediately.
		if(Thread.currentThread().isInterrupted())
		    {
			return null;
		    }
		
	    }//end for all moves
	
	long stopTime = System.currentTimeMillis();
	System.out.println("Number of moves tried = " + moveCount + 
			   "  Time = " + (stopTime -  startTime) + " milliseconds");
	
	// maxIndex is the index of the move to be made
	return moves[maxIndex];
	
    }
    
    /**
     * A max node returns the maximum score of its descendents.
     */
    private int expandMaxNode(int depth)
    {
	// if cutoff test is satisfied
	    {
		return board.getBoardStats().getStrength(maxPlayer);
	    }
	
	// if not
	Move[] moves = board.getPossibleMoves(maxPlayer);
	
	// explore each move in turn
	for(int i = 0; i < moves.length; i++)
	    {
		if(board.move(moves[i]))    // move was legal (column was not full)
		    {
			moveCount++;  // global variable

			// *************************************
			//              CODE NEEDED 
			//             (mostly here)  
			// *************************************

			board.undoLastMove();   // undo exploratory move
		    }  //end if move made
		
	    }//end for all moves
	
	return maxValue;
	
    }//end expandMaxNode
    
    


    /**
     * A max node returns the minimum score of its descendents.
     */
    private int expandMinNode(int depth)
    {
	// if cutoff test is satisfied
	    {
		return board.getBoardStats().getStrength(maxPlayer);
	    }
	
	// if not
	Move[] moves = board.getPossibleMoves(minPlayer);

	// explore each move in turn
	for(int i = 0; i < moves.length; i++)
	    {
		if(board.move(moves[i]))    // move was legal (column was not full)
		    {
			moveCount++;  // global variable

			// *************************************
			//              CODE NEEDED 
			//             (mostly here)  
			// *************************************

			board.undoLastMove();   // undo exploratory move
		    }  //end if move made
		
	    }//end for all moves
	
	return minValue;
	
    }//end expandMaxNode
    
}