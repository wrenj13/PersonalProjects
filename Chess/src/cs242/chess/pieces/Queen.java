package cs242.chess.pieces;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

import cs242.chess.ChessComponent;
import cs242.chess.ChessSpace;

/**
 * A class that represents a Queen. It extends ChessPiece.
 * 
 * @author REN-JAY_2
 * 
 */
public class Queen extends ChessPiece {

	/**
	 * Creates a Queen object. It assigns the ImageIcon based on the color of the Queen It also assigns the space of the Queen Note that
	 * Queen gets the value of "9"
	 * 
	 * @param color the color of the Queen
	 * @param space the space the piece is on
	 */
	public Queen(Color color, ChessSpace space) {
		super(color, space);
		Image queenImage = null;
		if (color == Color.BLACK) {
			queenImage = ChessComponent.createImage("Chess Pictures/Black_Queen.gif");
		} else {
			queenImage = ChessComponent.createImage("Chess Pictures/White_Queen.gif");
		}
		setImageIcon(new ImageIcon(queenImage));
		setValue(9);
	}

	/**
	 * Checks if the Queen can move to the desired space Note that "moving" to the piece's current space is not a valid move
	 * 
	 * @return true if the target space is in a horizontal, vertical or diagonal line away from the current space. False otherwise
	 */
	public boolean validMove(ChessSpace targetSpace) {
		int targetRow = targetSpace.getRow();
		int currentRow = getSpace().getRow();
		int targetCol = targetSpace.getCol();
		int currentCol = getSpace().getCol();
		if (currentRow == targetRow && currentCol == targetCol) {
			return false;
		}
		if (currentRow == targetRow || currentCol == targetCol) {
			return true;
		}
		if (Math.abs(targetRow - currentRow) != Math.abs(targetCol - currentCol)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns a deep copy of the Queen
	 * Note that we don't set the Queen's space because there is no point in doing so without relation to a Board
	 * 
	 * @return A deep copy of the Queen
	 */
	public ChessPiece copy() {
		return new Queen(getColor(), null);
	}
}