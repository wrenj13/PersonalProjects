package cs242.chess;

import cs242.chess.pieces.ChessPiece;

/**
 * A class that represents a space on a Chess board. It contains information about the row index, column index and the piece that is on it.
 * The user needs to take care to update the ChessSpace's piece data whenever a piece moves on or off it.
 * 
 * @author REN-JAY_2
 * 
 */
public class ChessSpace {

	private ChessPiece currentPiece;
	private int row;
	private int col;

	/**
	 * Constructs the space based on row and column indices.
	 * 
	 * @param r the row index of the space
	 * @param c the column index of the space
	 */
	public ChessSpace(int r, int c) {
		row = r;
		col = c;
	}

	/**
	 * Returns the row index.
	 * 
	 * @return the row index
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column index.
	 * 
	 * @return the column index
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Returns the piece currently on the space.
	 * 
	 * @return the piece
	 */
	public ChessPiece getPiece() {
		return currentPiece;
	}

	/**
	 * Sets the piece currently on the space.
	 * 
	 * @param newPiece the piece to be put on the space
	 */
	public void setPiece(ChessPiece newPiece) {
		currentPiece = newPiece;
	}

	/**
	 * Returns a string representation of the ChessSpace.
	 * 
	 * @return a string representation
	 */
	public String toString() {
		return "Row: " + row + ", Col: " + col;
	}
}
