//GameMaster.java

/** 
 *
 * @author  Sean Bridges
 * @version 1.0.1
 * 
 * The game master controls the state of the game, and is responsible for 
 * making the players move in sequence.
 *
 * If a subclass overrides start, restart or stop, it should call super.start,
 * restart or stop respectively.  
 *
 * Whenever start or restart are called a new thread is created which 
 * asks the players for their moves in sequence.  
 * If there was an old thread, the old thread will be interrupted, and discarded.
 */

import java.util.*;

public class GameMaster 
{

//--------------------------------------------------------
	//instance variables
	private Vector listeners = new Vector(); //listeners to the state,
                                             //use a vector for jdk1.0 compatability
	private Player[] players; //the players of the game
	private Board board;
	private int currentPlayerIndex = 0;
	private GameThread gameThread;
    
  
//--------------------------------------------------------
	//constructor
  
	/** 
	 * Creates new GameMaster.
	 * The PlayerArray should have at least 1 player in it.
	 * The order the players occur in the array is the order in which the 
	 * players will move.
	 * If the array is empty, an ArrayOutOfBoundsException will be thrown.
	 */
	public GameMaster(Board board, Player[] players ) 
	{
    
		if(players.length == 0)
		{
			System.err.println("Error, player array is empty, GameMaster() ");
			throw new ArrayIndexOutOfBoundsException("Game Master created with empty array");
		}
    
		this.board = board;
		this.players = players;
    
	}

//--------------------------------------------------------
	//instance methods

//------------------------------------------------------
	//listeners
  
	/**
	 * Add a game event listener.
	 */
	public synchronized void addListener(GameEventListener l)
	{
		listeners.addElement(l);
	}	
  
	public synchronized void removeListener(GameEventListener l)
	{
		listeners.removeElement(l);
	}
  
	/**
	 * Clone the listener vector in case the list of listeners 
	 * is modified while informing the listeners.
	 * This means the enumeration enumerates a copy of the list,
	 * and additions can be made to the list while informing listeners.
	 */
	private synchronized Enumeration enumerateListeners()
	{
		return ((Vector) listeners.clone() ).elements();
	}

  
	private void notifyListenersMoveMade(Move aMove)
	{
		Enumeration e = enumerateListeners();
		while(e.hasMoreElements())
		{
			((GameEventListener) e.nextElement()).moveMade(aMove);
		}
	}  
  
	private void notifyListenersGameReStarted()
	{
		Enumeration e = enumerateListeners();
		while(e.hasMoreElements())
		{
			((GameEventListener) e.nextElement()).gameRestarted();
		}
	}  
  
  
	private void notifyListenersGameStarted()
	{
		Enumeration e = enumerateListeners();
		while(e.hasMoreElements())
		{
			((GameEventListener) e.nextElement()).gameStarted();
		}
	}

	private void notifyListenersGameStoped()
	{
		Enumeration e = enumerateListeners();
		while(e.hasMoreElements())
		{
			((GameEventListener) e.nextElement()).gameStoped();
		}
	}
  
//--------------------------------------------------------
	//player management
  
	/**
	 * The players whose move it currently is.
	 */
	private synchronized Player getCurrentPlayer()
	{
		return players[currentPlayerIndex];
	}
  
	/**
	 * Advance the player
	 */
	private synchronized void advancePlayer()
	{
		currentPlayerIndex ++;
		if(currentPlayerIndex >= players.length)
		{
			currentPlayerIndex = 0;
		}
	}



//------------------------------------------------------
	//state

	/** 
	 *  Start the game.
	 *  Tells the board to start, starts the game loop thread,
	 *  then notifies listeners.
	 */
	public synchronized void startGame()
	{
		if(gameThread != null)
		{
			gameThread.stopThread();
			gameThread = null;
		}
    
		board.gameStarted();
		currentPlayerIndex = 0;
		gameThread = new GameThread();
		gameThread.start();
		notifyListenersGameStarted();
    
	}


	/**
	 * Stop the game.
	 * Stops the game thread, tells the board the game is stopped, and
	 * then notifies listeners.
	 */
	public synchronized void stopGame()
	{
		if(gameThread != null)
		{
			gameThread.stopThread();
			gameThread = null;
		}
    
		board.gameStoped();
		notifyListenersGameStoped();
	}

	/**
	 * Restart the game.
	 * Stops the current game loop, tells the board to restart,
	 * starts a new game loop, and then notifies listeneres.
	 */
	public synchronized void restartGame()
	{
		if(gameThread != null)
		{
			gameThread.stopThread();
			gameThread = null;
		}
    
		board.gameRestarted();
		currentPlayerIndex = 0;
		gameThread = new GameThread();
		gameThread.start();
		notifyListenersGameReStarted();
    
	}   
  
  
//--------------------------------------------------------
	//inner classes

	/** 
	 * The game thread class does the work of 
	 * making the players move.
	 * Exits after stop is called.
     */
	class GameThread extends Thread
	{    
    
		public boolean active = true;
    
		public void stopThread()
		{
			if(active)
			{
				active = false;
				this.interrupt();
			}
		}
    
		public  void run()
		{
			while( active && ! board.isGameOver() )
			{
        
				//get the players move.
				Player player = getCurrentPlayer();
				Move move = player.getMove((Board) board.clone());
				if(move == null)
				{
					//ignore null moves
					continue;
				}
        
				//if we are still active, make the move
				boolean moveMade = false;
  
				if(active)  
				{
					moveMade = board.move(move); 
				}
        
				//if we are still active, and the move was made, notify listeners
				if(active && moveMade)
				{
					advancePlayer();
					notifyListenersMoveMade(move);
				}
        
        
			}//end while
			
			if(active && board.isGameOver())
			{
				notifyListenersGameStoped();
			}

			active = false;
		}//end run
    
  
	}//end class GameThread
  
}//end class GameMaster

