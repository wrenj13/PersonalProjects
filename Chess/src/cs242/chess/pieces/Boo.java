package cs242.chess.pieces;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import cs242.chess.ChessComponent;
import cs242.chess.ChessSpace;

/**
 * A class the represents Boo, a new ChessPiece
 * Boo moves like a King. However, whenever Boo captures a piece, it gains the ability to move like that piece
 * 
 * @author REN-JAY_2
 *
 */
public class Boo extends ChessPiece {

	private ArrayList<ChessPiece> captured;
	
	public Boo(Color color, ChessSpace space) {
		super(color, space);
		Image booImage = null;
		if (color == Color.BLACK) {
			booImage = ChessComponent.createImage("Chess Pictures/Black_Boo.gif");
		} else {
			booImage = ChessComponent.createImage("Chess Pictures/White_Boo.gif");
		}
		setImageIcon(new ImageIcon(booImage));
		setValue(5);
		captured = new ArrayList<ChessPiece>();
	}
	
	/**
	 * Returns the ArrayList of pieces captured by Boo
	 * 
	 * @return the ArrayList of pieces captured by Boo
	 */
	public ArrayList<ChessPiece> getCaptured() {
		return captured;
	}
	
	
	/**
	 * Checks if the Boo can move to the desired space
	 * Note that "moving" to the piece's current space is not a valid move
	 * 
	 * @return true if the target space can be reached by Boo or any of its captured pieces
	 */
	public boolean validMove(ChessSpace targetSpace) {
		int targetRow = targetSpace.getRow();
		int currentRow = getSpace().getRow();
		int targetCol = targetSpace.getCol();
		int currentCol = getSpace().getCol();
		if (targetRow == currentRow && targetCol == currentCol) {
			return false;
		}
		for (ChessPiece p : captured) {
			p.setSpace(getSpace());
			if (p.validMove(targetSpace)) {
				p.setSpace(null);
				return true;
			}
			p.setSpace(null);
		}
		int rowDifference = Math.abs(targetRow - currentRow);
		int colDifference = Math.abs(targetCol - currentCol);
		if (rowDifference > 1 || colDifference > 1) {
			return false;
		}
		return true;
	}

	/**
	 * Overrides the moveTo method from the ChessPiece class
	 * Boo adds any captured pieces to its collection, and resets all their spaces to its current space
	 * It also increments its value by the value of the captured piece
	 * 
	 * @param the space Boo wants to move to
	 */
	public void moveTo(ChessSpace newSpace) {
		ChessPiece targetPiece = newSpace.getPiece();
		if (targetPiece != null) {
			captured.add(targetPiece);
			setValue(getValue() + targetPiece.getValue());
		}
		super.moveTo(newSpace);
	}
}
