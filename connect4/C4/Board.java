//Board.java

/** 
 *
 * @author  Sean Bridges
 * @version  1.0
 * 
 *  The board interface represents the theater in which moves are made 
 *  and evaluated.  You can make and unmake moves on a board, you can
 *  ask for the list of moves that are currently allowed, and you can 
 *  ask if the game has been won.
 *  You can ask a board for its statistics object to evaluate the score
 *  and how each player is doing.
 * 
 *  A board must be cloneable because a clone of a board is given to players
 *  when we ask them to move, rather than the board itself.  
 *  
 *  Threading issue.  Note that the board's .move(..) method will be called 
 *  in a seperate thread.  It is possible that the GameMaster() may call
 *  .startGame() .stopGame() or .restartGame() from another thread while the
 *  the game loop is in .move() or vice versa.  
 *  If you synchronize all these methods you should be fine.  
 *
 */

import java.io.*;

public interface Board extends Cloneable {

	/** 
 	 * Return an object which can be interrogated to discover the current
	 * state of the game
	 */
	public BoardStats getBoardStats();
  
 	/** 
     * Try and make a move.
	 * Returns wether or not the move attempt was successful.
	 */
	public boolean move(Move m);
  
  
	/**
	 * Undo the last move made.
	 */
	public void undoLastMove();
  
  
	/** 
	 * Get the list of moves that are possible for the given 
	 * player
	 */
	public Move[] getPossibleMoves(Player aPlayer);

	/**
	 * Whether or not the game is over.
	 */
	public boolean isGameOver();


	/**
	 * Called by the game master when the game is started.
	 */
	public void gameStarted();

	/**
	 * Called by the game master when the game is restarted.
	 */
	public void gameRestarted();

	/**
	 * Called by the game master when the game is stopped.
	 */
	public void gameStoped();

	/**
	 * A board must be cloneable since a copy of it is sent
	 * to players when we ask them for their move.
	 */
	public Object clone();
  
}
