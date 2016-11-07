//C4Row.java
 
/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 *
 * The C4Row contains 4 slots in a row.  It listens to those slots, and
 * counts the number of slots each player occupies.
 * It informs the stat object when a player has significant combinations
 * of the stats.
 */
public final class C4Row implements C4SlotListener
{
	
//-----------------------------------------------
	//instance variables
	
	//number of each type contained in my slots
	int numPlayer1 = 0;
	int numPlayer2 = 0;
	
	
	C4Stats stats;
	

//-----------------------------------------------
	//constructors

	/** Creates new C4Row 
	 *
	 * It is assumed that all the slots are empty.
	 */
	public C4Row(C4Slot s1, C4Slot s2, C4Slot s3, C4Slot s4, C4Stats stats) 
	{
		s1.addSlotListener(this);
		s2.addSlotListener(this);
		s3.addSlotListener(this);
		s4.addSlotListener(this);
		
		this.stats = stats;
	}
  
	public void contentsChanged(int oldContents,int newContents)
	{		
		//case 1, moving from null to first player
		if(oldContents == C4Board.NULL_PLAYER_NUMBER &
		   newContents == C4Board.FIRST_PLAYER_NUMBER)
		{
			//if we had no second player tokens, we now have a better score
			//for the first player, tell the stats.
			if(numPlayer2 == 0)
			{
				switch(numPlayer1)
				{
					case 0: stats.p1_1InARowInc();
					break;
					
					case 1: stats.p1_1InARowDec();
					        stats.p1_2InARowInc();
					break;
					
					case 2: stats.p1_2InARowDec();
					        stats.p1_3InARowInc();
					break;
					
					case 3: stats.p1_3InARowDec();
					        stats.p1_4InARowInc();
					break;
					
					default: errorPrint();
				}//end switch player 1
			}//end if numPlayer2 = 0
			
			//if we have some second player tokens, and
			//we had no first player tokens, we are no longer populated
			//exclusively by second player tokens, change the stats
			else if(numPlayer1 == 0)
			{
				switch(numPlayer2)
				{
					case 0: 
					break;
					
					case 1: stats.p2_1InARowDec();
					break;
					
					case 2: stats.p2_2InARowDec();
					break;
					
					case 3: stats.p2_3InARowDec();
					break;
					
					default: errorPrint();
				}//end switch player 1
			}
			
		}//end if old = null, new = player 1

		
		//case 2, moving from null to second player
		else if(oldContents == C4Board.NULL_PLAYER_NUMBER &
		   newContents == C4Board.SECOND_PLAYER_NUMBER)
		{
			//if we had no first player tokens, we now have a better score
			//for the second player, tell the stats.
			if(numPlayer1 == 0)
			{
				switch(numPlayer2)
				{
					case 0: stats.p2_1InARowInc();
					break;
					
					case 1: stats.p2_1InARowDec();
					        stats.p2_2InARowInc();
					break;
					
					case 2: stats.p2_2InARowDec();
					        stats.p2_3InARowInc();
					break;
					
					case 3: stats.p2_3InARowDec();
					        stats.p2_4InARowInc();
					break;
					
					default: errorPrint();
				}//end switch player 2
			}//end if numPlayer1 = 0
			
			//if we have some first player tokens, 
			//and we had no second player tokens, then we are no
			//longer populated exclusively by first player tokens
			else if(numPlayer2 == 0)
			{
				switch(numPlayer1)
				{
					case 0: 
					break;
					
					case 1: stats.p1_1InARowDec();
					break;
					
					case 2: stats.p1_2InARowDec();
					break;
					
					case 3: stats.p1_3InARowDec();
					break;
					
					default: errorPrint();
				}//end switch player 1
			}
			
		}//end if old = null, new = player 2
		
		//case 3, moving from player 1 to null
		else if(oldContents == C4Board.FIRST_PLAYER_NUMBER &
		        newContents == C4Board.NULL_PLAYER_NUMBER)
		{
			//if no player 2 tokens, we have a worse score
			if(numPlayer2 == 0)
			{
				switch(numPlayer1)
				{
					
					case 1: stats.p1_1InARowDec();
					break;
					
					case 2: stats.p1_2InARowDec();
					        stats.p1_1InARowInc();
					break;
					
					case 3: stats.p1_3InARowDec();
					        stats.p1_2InARowInc();
					break;
					
					case 4: stats.p1_4InARowDec();
					        stats.p1_3InARowInc();
					break;
					
					default: errorPrint();
				}//end switch player 1
				
			}//end if numPlayer2 == 0
			//if there was only 1 player 1 token, then we now have a score for p2
			if(numPlayer1 == 1)
			{
				switch(numPlayer2)
				{
					case 0: 
					break;
					
					case 1: stats.p2_1InARowInc();
					break;
					
					case 2: stats.p2_2InARowInc();
					break;
					
					case 3: stats.p2_3InARowInc();
					break;
					
					default: errorPrint();
				}//end switch player 2
			}//end if numPlayer1 == 1
		}//end old = p1, new = null
		
		
		//case 4, moving from player 2 to null
		else if(oldContents == C4Board.SECOND_PLAYER_NUMBER &
		        newContents == C4Board.NULL_PLAYER_NUMBER)
		{
			//if no player 1 tokens, we have a worse score
			if(numPlayer1 == 0)
			{
				switch(numPlayer2)
				{
					
					case 1: stats.p2_1InARowDec();
					break;
					
					case 2: stats.p2_2InARowDec();
					        stats.p2_1InARowInc();
					break;
					
					case 3: stats.p2_3InARowDec();
					        stats.p2_2InARowInc();
					break;
					
					case 4: stats.p2_4InARowDec();
					        stats.p2_3InARowInc();
					break;
					
					default: errorPrint();
				}//end switch player 2
				
			}//end if numPlayer1 == 0
			//if there was only 1 player 2 token, then we now have a score for p1
			if(numPlayer2 == 1)
			{
				switch(numPlayer1)
				{
					case 0: 
					break;
					
					case 1: stats.p1_1InARowInc();
					break;
					
					case 2: stats.p1_2InARowInc();
					break;
					
					case 3: stats.p1_3InARowInc();
					break;
					
					default: errorPrint();
				}//end switch player 1
			}//end if numPlayer2 == 1
		}//end old = p2, new = null
		
		
		if(newContents == C4Board.FIRST_PLAYER_NUMBER)
		{
			numPlayer1++;
		}
		else if(newContents == C4Board.SECOND_PLAYER_NUMBER)
		{
			numPlayer2++;
		}
		else if(newContents == C4Board.NULL_PLAYER_NUMBER)
		{
			if(oldContents == C4Board.FIRST_PLAYER_NUMBER)
			{
				numPlayer1--;
			}
			else if(oldContents == C4Board.SECOND_PLAYER_NUMBER)
			{
				numPlayer2--;
			}
		}
	}//end contentsChanged
	
	private void errorPrint()
	{
		System.err.println("Error in Row state, inconsistent values");
		Thread.dumpStack();
		System.err.println(stats);
		System.exit(0);
	}
}//end class C4Row