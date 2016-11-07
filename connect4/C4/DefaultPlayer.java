// DefaultPlayer.java
 
/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 *
 *  The defaultPlayer implements code that stores the player 
 *  and number of the player.  Subclasses can extend the Default
 *  Player and save some time.
 */
public abstract class DefaultPlayer implements Player 
{

//------------------------------------
	//instance variables
	protected String name;
	protected int number;

//------------------------------------
	//constructors

   
	/** Creates new DefaultPlayer */
	public DefaultPlayer(String name, int number)
	{
		this.name = name;
		this.number = number;
	}

//------------------------------------
	//instance variables


	/** Passed a copy of the board, asked what move it would like to make.
 	 */
	public abstract Move getMove(Board b);

	/** Return the name of the player.
	 */
	public String getName() 
	{
		return name;
	}
	
	/**
	 * Get the players number
	 */
	public int getNumber() 
	{
    	return number;
	}

	public String toString()
	{
		return name;
	}

}//end class DefaultPlayer
