// C4Slot.java
 
/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 * class C4 Slot extends Object
 * 
 * a slot represents one slot in the game board
 * a slot stores the player number of the player
 * who owns it.
 * If the slot is empty then it stores the
 * nullPlayer value, C4Board.NULL_PLAYER_NUMBER
 */

import java.util.Vector;

public final class C4Slot 
{

//--------------------------------------------
	//instance variables
	
	private int contents = C4Board.NULL_PLAYER_NUMBER;//what we store

	//listeners are stored in an array to minimize access time.
	//the array is grown as listeners are added.
	//Vectors arent ideal since all methods are synchronized and slow
	//cant use new collection framework since we want to maintain 
	//compatability with older VM's.
	private int numberOfListeners = 0;
	private C4SlotListener[] listeners = new C4SlotListener[11];


//--------------------------------------------
	//constructors

	/** Creates new C4Slot */
 	public C4Slot() 
	{
 	}

//--------------------------------------------
	//instance methods

//--------------------------------------------
	//listeners
	
	/**
	 * Add a new listener to the slot.
	 */
	public void addSlotListener(C4SlotListener listener)
	{
		//if we have to grow the array
		if(numberOfListeners == listeners.length)
		{
			int newArrayLength = numberOfListeners + 5;
			C4SlotListener[] newListeners = new C4SlotListener[newArrayLength];
			for(int i = 0; i < numberOfListeners; i++)
			{
				newListeners[i] = listeners[i];
			}
			listeners = newListeners;
		}
		
		listeners[numberOfListeners] = listener;
		numberOfListeners++;
	}
	
	/**
	 * Remove a listener to the slot.
	 */
	public void removeListener(C4SlotListener listener)
	{
		//find the listener in the array.
		for(int i = 0; i < numberOfListeners; i++)
		{
			//when we find it, move the last listener into that 
			//listeners spot, and remove the reference to the last
			//listener, decrement the number of listeners, and stop.
			if(listeners[i] == listener)
			{
				listeners[i] = listeners[numberOfListeners - 1];
				listeners[numberOfListeners - 1] = null;
				numberOfListeners --;
				break;
			}
		}
	}
	
	private void notifyListenersContentsChanged(int oldContents, int newContents)
	{
		for(int i = 0; i < numberOfListeners; i++)
		{
			listeners[i].contentsChanged(oldContents, newContents);
		}
	}

	
//--------------------------------------------
	//contents
	
	public void setContents(int newContents)
	{
		int oldContents = contents;
		contents = newContents;
		notifyListenersContentsChanged(oldContents, newContents);
	}
	
	public int getContents()
	{
		return contents;
	}
	
	
		
//--------------------------------------------
	//printing
	public String toString()
	{
		return String.valueOf(contents);
	}
}