package cs242.chess.pieces;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

import cs242.chess.ChessComponent;
import cs242.chess.ChessSpace;

/**
 * A class that represents a Bishop. It extends ChessPiece.
 * 
 * @author REN-JAY_2
 * 
 */
public class Bishop extends ChessPiece {

	/**
	 * Creates a Bishop object. It assigns the ImageIcon based on the color of the Bishop. It also assigns the space of the Bishop. Note
	 * that Bishop gets the value of "3".
	 * 
	 * @param color the color of the Bishop
	 * @param space the space the piece is on
	 */
	public Bishop(Color color, ChessSpace space) {
		super(color, space);
		Image bishopImage = null;
		if (color == Color.BLACK) {
			bishopImage = ChessComponent.createImage("Chess Pictures/Black_Bishop.gif");
		} else {
			bishopImage = ChessComponent.createImage("Chess Pictures/White_Bishop.gif");
		}
		setImageIcon(new ImageIcon(bishopImage));
		setValue(3);
	}

	/**
	 * Checks if the Bishop can move to the desired space. Note that "moving" to the piece's current space is not a valid move.
	 * 
	 * @return true if the target space is in a diagonal line with the current space. False otherwise
	 */
	public boolean validMove(ChessSpace targetSpace) {
		int targetRow = targetSpace.getRow();
		int currentRow = getSpace().getRow();
		int targetCol = targetSpace.getCol();
		int currentCol = getSpace().getCol();
		if (Math.abs(targetRow - currentRow) != Math.abs(targetCol - currentCol)) {
			return false;
		}
		if (targetRow == currentRow) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a deep copy of the Bishop. Note that we don't set the Bishop's space because there is no point in doing so without relation
	 * to a Board.
	 * 
	 * @return A deep copy of the Bishop
	 */
	public ChessPiece copy() {
		return new Bishop(getColor(), null);
	}
}