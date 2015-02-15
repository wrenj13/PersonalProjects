package cs242.chess.pieces;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

import cs242.chess.ChessComponent;
import cs242.chess.ChessSpace;

/**
 * A class that represents a Rook. It extends ChessPiece.
 * 
 * @author REN-JAY_2
 * 
 */
public class Rook extends ChessPiece {

	/**
	 * Creates a Rook object. It assigns the ImageIcon based on the color of the Rook It also assigns the space of the Rook Note that Rook
	 * gets the value of "5"
	 * 
	 * @param color the color of the Rook
	 * @param space the space the piece is on
	 */
	public Rook(Color color, ChessSpace space) {
		super(color, space);
		Image rookImage = null;
		if (color == Color.BLACK) {
			rookImage = ChessComponent.createImage("Chess Pictures/Black_Rook.gif");
		} else {
			rookImage = ChessComponent.createImage("Chess Pictures/White_Rook.gif");
		}
		setImageIcon(new ImageIcon(rookImage));
		setValue(5);
	}

	/**
	 * Checks if the Rook can move to the desired space Note that "moving" to the piece's current space is not a valid move
	 * 
	 * @return true if the target space is in a horizontal or vertical line away from the current space. False otherwise
	 */
	public boolean validMove(ChessSpace newSpace) {
		if (newSpace.getRow() != getSpace().getRow() && newSpace.getCol() != getSpace().getCol()) {
			return false;
		}
		if (newSpace.getRow() == getSpace().getRow() && newSpace.getCol() == getSpace().getCol()) {
			return false;
		}
		return true;
	}

}
