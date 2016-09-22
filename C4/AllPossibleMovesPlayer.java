// AllPossibleMovesPlayer.java


/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 *
 * The AllPossibleMovesPlayer tries every possible move, or a fraction
 * of the possible moves.
 * It makes the move that gives it the best strength.
 *
 * You can give the allPossibleMoves a number from 0 to 1, which represents
 * the proportion of the moves that it tries.  The default is 1, try all moves.
 * The player will always return a move if their are moves to be made.
 */
public class AllPossibleMovesPlayer extends DefaultPlayer {

//-----------------------------------------
    //instance variables
    
	private double fractionOfMovesTried = 1.0;


//-----------------------------------------
	//constructors

	/** 
	* Creates new AllPossibleMovesPlayer 
	*
	* Equivalent to AllPossibleMovesPlayer(name, number, 1.0)
	*/
	public AllPossibleMovesPlayer(String name, int number) 
	{
		super(name,number);
	}


	/** Creates new AllPossibleMovesPlayer 
	 *
	 *  fractionOfMovesTried represents the fraction of moves that 
	 *  the player will try.  Should be a double from 0 to 1.
	 *  If 0 the player will return a move at random from the
	 *  possible moves.
	 */
	public AllPossibleMovesPlayer(String name, int number, double fractionOfMovesTried) 
	{
		super(name,number);
		this.fractionOfMovesTried = fractionOfMovesTried;
	}

//-----------------------------------------
	//instance methods
	
	public void setFractionOfMovesTried(double f)
	{
		fractionOfMovesTried = f;
	}

	public double getFractionOfMovesTried()
	{
		return fractionOfMovesTried;
	}

	/**
 	 * Tries fractionOfMovesTried moves.
	 * Throws an ArrayIndexOutOfBoundsException if the board returns
	 * no possible moves.
	 */
	public Move getMove(Board b) 
	{
		Move[] moves = b.getPossibleMoves(this);

		if(moves.length == 0)
		{
			System.err.println("All possibleMovesPlayer queried board, but board says no moves possible");
			throw new ArrayIndexOutOfBoundsException("0 moves possible");
		}
    
		int maxScore = b.getBoardStats().getMinStrength();
		int maxIndex = 0;
    
		int currentScore;
		boolean triedOne = false;
    
		for(int i = 0; i < moves.length; i++)
		{
        	if( Math.random() < fractionOfMovesTried )
			{
				triedOne = true;
				if(b.move(moves[i]))
				{
					currentScore = b.getBoardStats().getStrength(this);
					if(currentScore > maxScore)
					{   
						maxScore = currentScore;
						maxIndex = i;
					}
					b.undoLastMove();
				}
            
			}//end if
		}//end for
    
		//if we havent tried any of the moves, return one at random.
		if(!triedOne)
		{
			return moves[ (int) (moves.length * Math.random())];
		}
		else
		{
			return moves[maxIndex];
		}
    

	}//end getMove

}// end class AllPossibleMovesPlayer
