package cs242.chess.pieces;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

import cs242.chess.ChessComponent;
import cs242.chess.ChessSpace;

/**
 * A class that represents a Knight. It extends ChessPiece.
 * 
 * @author REN-JAY_2
 * 
 */
public class Knight extends ChessPiece {

	/**
	 * Creates a Knight object. It assigns the ImageIcon based on the color of the Knight It also assigns the space of the Knight Note that
	 * Knight gets the value of "3"
	 * 
	 * @param color the color of the Knight
	 * @param space the space the piece is on
	 */
	public Knight(Color color, ChessSpace space) {
		super(color, space);
		Image knightImage = null;
		if (color == Color.BLACK) {
			knightImage = ChessComponent.createImage("Chess Pictures/Black_Knight.gif");
		} else {
			knightImage = ChessComponent.createImage("Chess Pictures/White_Knight.gif");
		}
		setImageIcon(new ImageIcon(knightImage));
		setValue(3);
	}

	/**
	 * Checks if the Knight can move to the desired space
	 * 
	 * @return true if the target space is an L away from the current space. False otherwise
	 */
	public boolean validMove(ChessSpace targetSpace) {
		int targetRow = targetSpace.getRow();
		int currentRow = getSpace().getRow();
		int targetCol = targetSpace.getCol();
		int currentCol = getSpace().getCol();
		int difference = Math.abs(targetRow - currentRow) + Math.abs(targetCol - currentCol);
		if (difference != 3) {
			return false;
		}
		if (Math.abs(targetRow - currentRow) != 1 && Math.abs(targetRow - currentRow) != 2) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns a deep copy of the Knight
	 * Note that we don't set the Knight's space because there is no point in doing so without relation to a Board
	 * 
	 * @return A deep copy of the Knight
	 */
	public ChessPiece copy() {
		return new Knight(getColor(), null);
	}
}