package cs242.chess;

/**
 * A representation of an abstract action.
 * It contains execute and undo methods.
 * 
 * @author REN-JAY_2
 *
 */
public interface Command {

	/**
	 * A method that executes the action.
	 */
	public void execute();
	
	/**
	 * A method that undoes the last action
	 */
	public void undo();
}
