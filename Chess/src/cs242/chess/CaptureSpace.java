package cs242.chess;

import java.util.ArrayList;

import cs242.chess.pieces.ChessPiece;

/**
 * This class extends the ChessSpace class by adding an ArrayList of ChessPieces that can capture the given space. Note that the
 * CaptureSpace may or may not contain a piece. If it does contain a piece, that piece should also be listed in the ArrayList.
 * 
 * @author REN-JAY_2
 * 
 */
public class CaptureSpace extends ChessSpace {

	private ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>(); // pieces that can capture the space

	/**
	 * A constructor that constructs the piece based off a ChessSpace's row and column.
	 * 
	 * @param newSpace the piece to base the CaptureSpace off of
	 */
	public CaptureSpace(ChessSpace newSpace) {
		super(newSpace.getRow(), newSpace.getCol());
	}

	/**
	 * Adds a piece to the ArrayList of pieces that can capture the given piece.
	 * 
	 * @param piece the piece to be added
	 */
	public void addPiece(ChessPiece piece) {
		pieces.add(piece);
	}

	/**
	 * Returns the ArrayList of pieces that can capture the given piece.
	 * 
	 * @return the ArrayList of pieces
	 */
	public ArrayList<ChessPiece> getPieces() {
		return pieces;
	}

	/**
	 * Returns true if the spaces have the same row and column.
	 * 
	 * @param anotherSpace The space being compared to
	 * @return true if the spaces are equal
	 */
	public boolean equalsChessSpace(ChessSpace anotherSpace) {
		return getRow() == anotherSpace.getRow() && getCol() == anotherSpace.getCol();
	}
}
