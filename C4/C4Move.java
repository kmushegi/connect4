// C4Move.java

 


/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 */
public final class C4Move implements Move
{
	
//-----------------------------------------------
	//instance variables
	
	Player maker;
	int column;

//-----------------------------------------------
	//constructors

  /** Creates new C4Move */
	public C4Move(Player maker, int column) 
	{
		this.column = column;
		this.maker = maker;
	}

//-----------------------------------------------
	//instance methods

	
	/** 
 	 * Return the column the move is made in.
	 */	
	public int toInt()
	{	
		return column;
	}
	
	/** The player who made the move.
 	*/
	public Player maker()
	{
		return maker;
	}
	
	public String toString()
	{
		return "C4Move by:" + maker.getNumber() + " in column:" + column;
	}
}