// C4SlotListener.java
 


/** 
 *
 * @author  Sean Bridges
 * @version 1.0
 *
 * A slot listener listens to a C4Slot, and is notified when its 
 * contents are changed.
 */
public interface C4SlotListener 
{
	public void contentsChanged(int oldContents, int newContents);

}
