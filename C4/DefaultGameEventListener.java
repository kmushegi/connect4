//DefaultGameEventListener.java
 
/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 *
 *  The DefaultGameEventListener implements do nothing 
 *  methods for all the evetns.  Subclasses can override methods that 
 *  they are interested in.
 *
 *  A GameEvent represents a significant event in the life cycle of a game.
 *  Events include, GameStarted, GameStopped, GameRestarted, and moves
 */
public class DefaultGameEventListener implements GameEventListener {
  
	/**
	 * The game has started.
	 */
	public void gameStarted() {}
  
	/**
 	 * The game has stopped
	 */
	public void gameStoped() {}
  
	/**  
 	 * The game has been restarted.
	 */
	public void gameRestarted() {}
  
	/** 
	 * A player has moved.
	 */
	public void moveMade(Move aMove) {}
  
}
