package cs242.chess;

/**
 * An interface for a board of type E. It is assumed to hold a two-dimensional array of type E.
 * 
 * @author Ren-Jay Wang
 * 
 * @param <E>
 */
public interface Board<E> {

	/**
	 * Returns the value of the index in the array at the specified row and column.
	 * 
	 * @param row the row of the desired index
	 * @param col the column of the desired index
	 * @return the value of the specified index in the array
	 */
	public E getPointValue(int row, int col);

	/**
	 * Sets the value of the index in the array to the specified row and column.
	 * 
	 * @param row the row of the desired index
	 * @param col the column of the desired index
	 * @param value the value to which to set the index
	 */
	public void setPointValue(int row, int col, E value);

	/**
	 * Returns the number of rows, or length, in the array
	 * 
	 * @return the number of rows of the array
	 */
	public int getLength();

	/**
	 * Returns the number of columns, or width, of the array
	 * 
	 * @return the number of columns of the array
	 */
	public int getWidth();

	/**
	 * Sets the array reference in the data to a new array.
	 * 
	 * @param newDimensions the new array to which the data reference is set
	 */
	public void setDimensions(E[][] newDimensions);

	/**
	 * Returns a copy of the Board.
	 * 
	 * @return a copy of the Board
	 */
	public Board<E> copy();

	/**
	 * Clears the Board.
	 */
	public void clear();
}
