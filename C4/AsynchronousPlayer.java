// AsynchronousPlayer.java
 

/** 
 * @author  Sean Bridges
 * @version 1.0.1
 *
 * The asynchronous player blocks the getMove call until
 * its makeMove() method is called.  Can support multiple
 * threads waiting.  They are all woken up when makeMove is called.
 *
 * Currently you cannot tell when the asynchronous player
 * has a thread waiting for moves.  The class is designed mostly
 * to interact with UI's that produce a sequence of moves
 * in response to user inputs, such as a mouse click, and dont care
 * about the current state of the game.
 */

public class AsynchronousPlayer extends DefaultPlayer
{
  
//------------------------------------------
	//instance variables
    
	private Move lastMove = null;

  
	/** Creates new AsynchronousPlayer */
	public AsynchronousPlayer(String name, int number) 
	{
		super(name, number);
	}
    
	/*
	 * Passed a copy of the board, asked what move it would like to make.
	 * This call will block until makeMove(aMove) is called, at which time
	 * aMove will be returned.
	 *
	*/
	public synchronized  Move getMove(Board b)
	{
		try
		{
			wait();
		}
		//if we are interrupted, then it means the game
		//is over, and we no one cares about what we return, 
		//so return null.
		catch(InterruptedException e)
		{
			return null;
		}
  	 
		return lastMove;
	}
  
  
	/** 
	 * Make a move.
	 * If there are any threads waiting in the getMove()
	 * method, wakes them up, and passes them this move.
	 * If no threads waiting, then does nothing
	 */
	public synchronized void makeMove(Move aMove)
	{
		lastMove = aMove;
		notifyAll();
	}
 
}//end class AsynchronousPlayer
