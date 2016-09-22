// BoardStats.java

/** 
 *  
 * @author  Sean Bridges
 * @version 1.0
 *
 * A BoardStats is meant to be interrogated to discover the score of the 
 * game and a heuristic for determining how each player is doing.
 * 
 * The getScore(playerNumber) method is intended to return the concrete score
 * which is how well the player is doing.
 * The getHeuristic(playerNumber) method detemines how well a player is doing.
 * 
 * getScore is meant for methods that display the score of the game, while
 * getStrength is meant for algorithims to determine the next best move.
 *
 */
public interface BoardStats {

	/**
	 * Get the score for the given player.
	 * A score is an indisputable thing, such as the number of hits in battleship.
	 */
	public int getScore(Player aPlayer);
  
	/** 
	 * Get an evaluation of how well a player is doing.
	 * The number returned could be debabtable, such as a measure
	 * of how strong a chess players position is.
	 */
	public int getStrength(Player aPlayer);
  
	/**
	 * Returns the maximum possible hueristic.
	 */
	public int getMaxStrength();
  
	/**
	 * Returns the minimum possible hueristic.
	 */
	public int getMinStrength();
  
}//end interface BoardStats
