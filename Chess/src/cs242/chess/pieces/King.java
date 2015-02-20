package cs242.chess.pieces;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

import cs242.chess.ChessComponent;
import cs242.chess.ChessSpace;

/**
 * A class that represents a King. It extends ChessPiece.
 * 
 * @author REN-JAY_2
 * 
 */
public class King extends ChessPiece {

	/**
	 * Creates a King object. It assigns the ImageIcon based on the color of the King. It also assigns the space of the King. Note that King
	 * gets the value of "99", which is far higher than any piece.
	 * 
	 * @param color the color of the King
	 * @param space the space the piece is on
	 */
	public King(Color color, ChessSpace space) {
		super(color, space);
		Image kingImage = null;
		if (color == Color.BLACK) {
			kingImage = ChessComponent.createImage("Chess Pictures/Black_King.gif");
		} else {
			kingImage = ChessComponent.createImage("Chess Pictures/White_King.gif");
		}
		setImageIcon(new ImageIcon(kingImage));
		setValue(99);
	}

	/**
	 * Checks if the King can move to the desired space Note that "moving" to the piece's current space is not a valid move.
	 * 
	 * @return true if the target space is 1 space away from the current space. False otherwise
	 */
	public boolean validMove(ChessSpace targetSpace) {
		int currentRow = getSpace().getRow();
		int currentCol = getSpace().getCol();
		int targetRow = targetSpace.getRow();
		int targetCol = targetSpace.getCol();
		if (targetRow == currentRow && targetCol == currentCol) {
			return false;
		}
		int rowDifference = Math.abs(currentRow - targetRow);
		int colDifference = Math.abs(currentCol - targetCol);
		if (rowDifference > 1 || colDifference > 1) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a deep copy of the King. Note that we don't set the King's space because there is no point in doing so without relation to a
	 * Board.
	 * 
	 * @return A deep copy of the King
	 */
	public ChessPiece copy() {
		return new King(getColor(), null);
	}
}
