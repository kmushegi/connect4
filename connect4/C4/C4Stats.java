//C4Stats.java
 


/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 */
public final class C4Stats implements BoardStats
{

//------------------------------------------------
	//class variables
	
	//to save time, we shift rather than multiply to find the scores
	//multipliers are 64, 16,1
	//which correspond to 6,4, 1
	//dont bother shifting by 1
	private final static int SHIFT_3_IN_A_ROW = 5; //32
	private final static int SHIFT_2_IN_A_ROW = 2; //4	

	private final static int PLAYER1 = C4Board.FIRST_PLAYER_NUMBER;
	private final static int PLAYER2 = C4Board.SECOND_PLAYER_NUMBER;

//-----------------------------------------------
	//instance variables
	protected static int p1_4InARow;
	protected static int p1_3InARow;
	protected static int p1_2InARow;
	protected static int p1_1InARow;
		
	protected static int p2_4InARow;
	protected static int p2_3InARow;
	protected static int p2_2InARow;
	protected static int p2_1InARow;
	

	
//------------------------------------------------
	//Constructor
	
	/** Creates new C4Stats */
	public C4Stats() 
	{
 	}

//------------------------------------------------
	//Instance methods
	
		

	public final void p2_4InARowInc()
	{
		p2_4InARow++;
	}
	
	public final void p2_3InARowInc()
	{
		p2_3InARow++;
	}

	public final void p2_2InARowInc()
	{
		p2_2InARow++;
	}
	
	public final void p2_1InARowInc()
	{
		p2_1InARow++;
	}

	public final void p1_4InARowInc()
	{
		p1_4InARow++;
	}
	
	public final  void p1_3InARowInc()
	{
		p1_3InARow++;
	}

	public final  void p1_2InARowInc()
	{
		p1_2InARow++;
	}
	
	public final void p1_1InARowInc()
	{
		p1_1InARow++;
	}
	

	
	public final void p2_4InARowDec()
	{
		p2_4InARow--;
	}
	
	public final void p2_3InARowDec()
	{
		p2_3InARow--;
	}

	public final void p2_2InARowDec()
	{
		p2_2InARow--;
	}
	
	public final void p2_1InARowDec()
	{
		p2_1InARow--;
	}

	public final void p1_4InARowDec()
	{
		p1_4InARow--;
	}
	
	public final void p1_3InARowDec()
	{
		p1_3InARow--;
	}

	public final void p1_2InARowDec()
	{
		p1_2InARow--;
	}
	
	public final void p1_1InARowDec()
	{
		p1_1InARow--;
	}

	
//------------------------------------------------
	//evaluation
	private int firstPlayerStrength()
	{
		if(p1_4InARow != 0)
		{
			return Integer.MAX_VALUE;
		}
		else if(p2_4InARow != 0)
		{
			return 0;
		}
		else
		{
			return (p1_2InARow << SHIFT_2_IN_A_ROW) + 
			       (p1_3InARow << SHIFT_3_IN_A_ROW) +
			       p1_1InARow;
		}
	}

	private int secondPlayerStrength()
	{
		if(p2_4InARow != 0)
		{
			return Integer.MAX_VALUE;
		}
		else if(p1_4InARow != 0)
		{
			return 0;
		}
		else
		{
			return (p2_2InARow << SHIFT_2_IN_A_ROW) + 
			       (p2_3InARow << SHIFT_3_IN_A_ROW) +
			       p2_1InARow;
		}
	}
	
	
//------------------------------------------------
	//BoardStats methods
	
	
	
	/** Get the score for the given player.
	 * A score is an indisputable thing, such as the number of hits in battleship.
	 */
	public int getScore(Player aPlayer)
	{
		if(aPlayer.getNumber() == PLAYER1)
		{
			//Connect 4 the score only goes to 1
			if(p1_4InARow != 0)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		else //second player
		{
			//Connect 4 the score only goes to 1
			if(p2_4InARow != 0)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	} 
	
	/**
	 * Get an evaluation of how well a player is doing.
	 * The number returned could be debabtable, such as a measure
	 * of how strong a chess players position is.
	 */
	public int getStrength(Player aPlayer)
	{
		if(aPlayer.getNumber() == PLAYER1)
		{
			return firstPlayerStrength() - secondPlayerStrength();
		}
		else
		{
			return secondPlayerStrength() - firstPlayerStrength();
		}
	}
	
	/** Returns the maximum possible hueristic.
	 */
	public int getMaxStrength()
	{
		return Integer.MAX_VALUE;
	}
	/** Returns the minimum possible hueristic.
	 */
	public int getMinStrength()
	{
		return Integer.MIN_VALUE;
	}
	
	/**
	 * Return wether or not a player has won the game
	 */
	public final boolean hasSomeoneWon()
	{
		return (p1_4InARow != 0) | (p2_4InARow != 0);
	}
	
	public String toString()
	{
		return "P1Score:" + p1_4InARow + " P2Score:" + p2_4InARow + "\n" +
		       "p1Strength:" + firstPlayerStrength() + " p2Strength:" + secondPlayerStrength() + "\n" +
				"p1_4:" + p1_4InARow + " p1_3:" + p1_3InARow +  " p1_2:" + p1_2InARow + " p1_1:" + p1_1InARow + "\n" +
				"p2_4:" + p2_4InARow + " p2_3:" + p2_3InARow +  " p2_2:" + p2_2InARow + " p2_1:" + p2_1InARow;
		
	}
}