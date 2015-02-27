package cs242.chess;

/**
 * A class that is responsible for managing Commands.
 * It executes, undoes and tracks commands.
 * 
 * @author REN-JAY_2
 *
 */
public class CommandManager {

	private Command lastCommand;
	
	/**
	 * Executes a command
	 * 
	 * @param c the command to be executed
	 */
	public void executeCommand(Command c) {
		c.execute();
		lastCommand = c;
	}
	
	/**
	 * Returns whether or not an undo is possible
	 * 
	 * @return true if an undo is possible, false otherwise
	 */
	public boolean isUndoAvailable() {
	    return lastCommand != null;
	}
	
	/**
	 * Undoes the last command that was executed.
	 * If there was no last command, this function does nothing.
	 */
	public void undo() {
	    if (isUndoAvailable()) {
	    	lastCommand.undo();
	    	lastCommand = null;
	    }
	}
}
