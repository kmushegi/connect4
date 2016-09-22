//GamePlayer.java


/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 *
 * A game player can make moves on a board. It is given a copy of the board,
 * and asked for the move that it wishes to make.
 * 
 */
public interface Player 
{

	/**
	 * Passed a copy of the board, asked what move it would like to make.
	 * 
	 * This method is called from a seperate thread that is created each time a 
	 * game is started or restarted. Each time start or restart is called on 
	 * the gameMaster, a new thread is created.
	 *
	 * Because of this, there is the potential for a player to be asked for
	 * its move, and for the game to be stopped or restarted before the player
	 * can decide on a move.  In that case the GameMaster will interrupt the
	 * thread that called getMove(), and ignore the results returned.
	 */
	public Move getMove(Board b);
  
	/**
	 * Return the name of the player.
	 */
	public String getName();
  
	/** 
	 * Get the players number
	 */
	public int getNumber();
  
  
}
