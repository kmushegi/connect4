//Move.java

/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 * 
 * The move represents a single move in a game.
 * A move can represent itself as an integer, and 
 * knows the player number of the player made it.
 */
public interface Move {

	/**
	 * The default method of accessing the moves value.
	 * This method is to avoid casting.  Rather than having 
	 * a board cast the Move to its real class, it can access 
	 * the move as an int.  At times though it may be neccassary
	 * to cast the Move to its true class.
	 *
	 * Casting can be an expensive process.  When time is an issue, eg for
	 * games that try thousands of moves in order to determine the best one, 
	 * it is better to avoid casting.
	 *
	 * The clever programmer may also use the toString() method to 
	 * represent the move as a String.
	 */

	public int toInt();
  
	/**
	 * The player who made the move.
	 */
	public Player maker();
  
}
