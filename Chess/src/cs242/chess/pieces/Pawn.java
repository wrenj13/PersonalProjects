package cs242.chess.pieces;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import cs242.chess.ChessComponent;
import cs242.chess.ChessSpace;

/**
 * A class that represents a Pawn. It extends ChessPiece.
 * 
 * @author REN-JAY_2
 * 
 */
public class Pawn extends ChessPiece {

	private int direction;

	/**
	 * Creates a Pawn object. It assigns the ImageIcon based on the color of the Pawn. It also assigns the space of the Pawn Note that Pawn
	 * gets the value of "1".
	 * 
	 * @param color the color of the Pawn
	 * @param space the space the piece is on
	 * @param dir the direction the pawn is moving in. 0 - down (from column 0 -> 8), 1 - up (from column 8->0)
	 */
	public Pawn(Color color, ChessSpace space, int dir) {
		super(color, space);
		direction = dir;
		Image pawnImage = null;
		if (color == Color.BLACK) {
			pawnImage = ChessComponent.createImage("Chess Pictures/Black_Pawn.gif");
		} else {
			pawnImage = ChessComponent.createImage("Chess Pictures/White_Pawn.gif");
		}
		setImageIcon(new ImageIcon(pawnImage));
		setValue(1);
	}

	/**
	 * Returns the direction of the pawn. 0-down, 1-up
	 * 
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Sets the direction of the pawn.
	 * 
	 * @param newDir the new direction
	 */
	public void setDirection(int newDir) {
		direction = newDir;
	}

	/**
	 * Checks if the Pawn can move to the desired space.
	 * 
	 * @return true if the target space is 1 space in front of the current space, or for two-space advance. False otherwise
	 */
	public boolean validMove(ChessSpace targetSpace) {
		int targetRow = targetSpace.getRow();
		int currentRow = getSpace().getRow();
		int targetCol = targetSpace.getCol();
		int currentCol = getSpace().getCol();
		if (direction == 1) { // up
			if (currentRow == 6) {
				if (targetRow != currentRow - 1 && targetRow != currentRow - 2) {
					return false;
				}
			} else if (targetRow != currentRow - 1) {
				return false;
			}
			// if there is a piece on the desired space, and it is either not one column away or on the next row, reject the space
			if (targetSpace.getPiece() != null
					&& (targetCol != currentCol + 1 && targetCol != currentCol - 1 || (targetRow != (currentRow - 1)))) {
				return false;
			} else if (targetSpace.getPiece() == null && targetCol != currentCol) {
				return false;
			}
		} else if (direction == 0) { // down
			if (currentRow == 1) {
				if (targetRow != (currentRow + 1) && targetRow != (currentRow + 2)) {
					return false;
				}
			} else if (targetRow != currentRow + 1) {
				return false;
			}
			if (targetSpace.getPiece() != null
					&& ((targetCol != currentCol + 1 && targetCol != currentCol - 1) || targetRow != (currentRow + 1))) {
				return false;
			} else if (targetSpace.getPiece() == null && targetCol != currentCol) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method overrides the method inherited from the ChessPiece class. It allows Pawns to be promoted if they reach the opposite end
	 * of the board. Otherwise, it behaves like the inherited moveTo method.
	 * 
	 * @param newSpace the space the Pawn is moving to
	 */
	public void moveTo(ChessSpace newSpace) {
		super.moveTo(newSpace);
		ArrayList<String> possibleStrings = new ArrayList<String>();
		possibleStrings.add("Queen");
		possibleStrings.add("Rook");
		possibleStrings.add("Knight");
		possibleStrings.add("Bishop");

		if (direction == 1) // up
		{
			if (newSpace.getRow() == 0) {
				ChessPiece[] possiblePromotions = { new Queen(Color.WHITE, newSpace), new Rook(Color.WHITE, newSpace),
						new Knight(Color.WHITE, newSpace), new Bishop(Color.WHITE, newSpace) };
				String pieceString = (String) JOptionPane.showInputDialog(null, "Promotion!", "Please choose a promotion.",
						JOptionPane.QUESTION_MESSAGE, null, possibleStrings.toArray(), possiblePromotions[0]);
				int index = possibleStrings.indexOf(pieceString);
				newSpace.setPiece(possiblePromotions[index]);
			}
		} else if (direction == 0) {
			if (newSpace.getRow() == 7) {
				ChessPiece[] possiblePromotions = { new Queen(Color.BLACK, newSpace), new Rook(Color.BLACK, newSpace),
						new Knight(Color.BLACK, newSpace), new Bishop(Color.BLACK, newSpace) };
				String pieceString = (String) JOptionPane.showInputDialog(null, "Promotion!", "Please choose a promotion.",
						JOptionPane.QUESTION_MESSAGE, null, possibleStrings.toArray(), possiblePromotions[0]);
				int index = possibleStrings.indexOf(pieceString);
				newSpace.setPiece(possiblePromotions[index]);
			}
		}
	}

	/**
	 * Returns a deep copy of the Pawn. Note that we don't set the Pawn's space because there is no point in doing so without relation to a
	 * Board.
	 * 
	 * @return A deep copy of the Pawn
	 */
	public ChessPiece copy() {
		return new Pawn(getColor(), null, getDirection());
	}
}
