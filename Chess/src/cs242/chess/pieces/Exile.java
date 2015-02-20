package cs242.chess.pieces;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

import cs242.chess.ChessBoard;
import cs242.chess.ChessComponent;
import cs242.chess.ChessSpace;

/**
 * A class the represents Exile, a new ChessPiece Exile moves like a King. However, once Exile has killed 3 pieces, it can have one
 * opportunity to fire a shockwave that kills all enemy pieces in a cone in front of it. Every time it fires a shockwave the counter resets.
 * The Exile needs to store the board in memory in order to eliminate pieces.
 * 
 * @author REN-JAY_2
 * 
 */
public class Exile extends ChessPiece {

	private int captureCount;
	ChessBoard board;

	/**
	 * Constructs an Exile based off a color and a space. Sets the moveCount to 0.
	 * 
	 * @param color The color of the Exile
	 * @param space The space the Exile begins on
	 * @param newBoard the board that the piece is on
	 */
	public Exile(Color color, ChessSpace space, ChessBoard newBoard) {
		super(color, space);
		Image booImage = null;
		if (color == Color.BLACK) {
			booImage = ChessComponent.createImage("Chess Pictures/Black_Exile.gif");
		} else {
			booImage = ChessComponent.createImage("Chess Pictures/White_Exile.gif");
		}
		setImageIcon(new ImageIcon(booImage));
		setValue(7);
		board = newBoard;
		captureCount = 0;
	}

	/**
	 * Returns the number of moves the Exile has made since its last shockwave.
	 * 
	 * @return the ArrayList of pieces captured by Boo
	 */
	public int getCaptureCount() {
		return captureCount;
	}

	/**
	 * Sets the capture pieces count of the Exile.
	 * 
	 * @param newCaptureCount the new count
	 */
	public void setCaptureCount(int newCaptureCount) {
		captureCount = newCaptureCount;
	}

	/**
	 * Sets the ChessBoard to a new board.
	 * 
	 * @param newBoard the new board of the Exile
	 */
	public void setBoard(ChessBoard newBoard) {
		board = newBoard;
	}

	/**
	 * Checks if the Exile can move to the desired space Firing a shockwave counts as a valid move. Note that "moving" to the piece's
	 * current space is not a valid move.
	 * 
	 * @return true if the target space can be reached by the Exile
	 */
	public boolean validMove(ChessSpace targetSpace) {
		int targetRow = targetSpace.getRow();
		int currentRow = getSpace().getRow();
		int targetCol = targetSpace.getCol();
		int currentCol = getSpace().getCol();
		if (targetRow == currentRow && targetCol == currentCol) {
			return false;
		}
		int rowDifference = Math.abs(targetRow - currentRow);
		int colDifference = Math.abs(targetCol - currentCol);
		if (rowDifference <= 1 && colDifference <= 1) { // within king move
			return true;
		}
		if ((rowDifference > 2 || colDifference > 2) || (rowDifference == 2 && colDifference == 2)) { // out of bounds
			return false;
		}
		// then, the move is valid for shockwave
		if (captureCount >= 3) {
			return true;
		} // otherwise its an invalid move
		return false;
	}

	/**
	 * A helper function that determines if a target space is within the bounds of the board and doesnt have a same-color piece on it. If it
	 * meets the criteria, then the piece is removed. This function assumes that the captureCount is above 3 and that the Exile is firing a
	 * shockwave.
	 * 
	 * @param targetRow the row of the target space
	 * @param targetCol the column of the target space
	 * @return true if the target space is okay to move to, false otherwise
	 */
	public void fireShockwave(int targetRow, int targetCol) {
		System.out.println("wind slash fired!" + targetRow + targetCol);
		if (targetRow < 0 || targetRow >= board.getLength() || targetCol < 0 || targetCol >= board.getWidth()) {
			return;
		}
		// if enemy piece, remove it
		if (board.getPointValue(targetRow, targetCol).getPiece() != null
				&& !board.getPointValue(targetRow, targetCol).getPiece().getColor().equals(getColor())) {
			board.getPointValue(targetRow, targetCol).setPiece(null);
		}
	}

	/**
	 * Overrides the moveTo method from the ChessPiece class If the Exile's move capture count is 3 or above, it can fire a shockwave. This counts as
	 * a move, but does not move the Exile. Instead, it kills all pieces in a cone. This method checks if the move is valid (that is, for
	 * a shockwave, only one of the target row or column is greater than 1 square away)
	 * 
	 * @param newSpace the space Exile wants to move to
	 */
	public void moveTo(ChessSpace newSpace) {
		int targetRow = newSpace.getRow();
		int currentRow = getSpace().getRow();
		int targetCol = newSpace.getCol();
		int currentCol = getSpace().getCol();
		int rowDifference = Math.abs(targetRow - currentRow);
		int colDifference = Math.abs(targetCol - currentCol);
		if (captureCount >= 3 && (rowDifference == 2 && (colDifference <= 1) || (rowDifference <= 1 && colDifference == 2))) {
			if (rowDifference == 2) {
				if (targetRow < currentRow) { // above the exile
					fireShockwave(currentRow - 2, currentCol - 1);
					fireShockwave(currentRow - 2, currentCol);
					fireShockwave(currentRow - 2, currentCol + 1);
					fireShockwave(currentRow - 1, currentCol);
				} else {
					fireShockwave(currentRow + 2, currentCol - 1);
					fireShockwave(currentRow + 2, currentCol);
					fireShockwave(currentRow + 2, currentCol + 1);
					fireShockwave(currentRow + 1, currentCol);
				}
			} else if (colDifference == 2) {
				if (targetCol < currentCol) { // left of the exile
					fireShockwave(currentRow - 1, currentCol - 2);
					fireShockwave(currentRow, currentCol - 2);
					fireShockwave(currentRow + 1, currentCol - 2);
					fireShockwave(currentRow, currentCol - 1);
				} else { // right of the exile
					fireShockwave(currentRow - 1, currentCol + 2);
					fireShockwave(currentRow, currentCol + 2);
					fireShockwave(currentRow + 1, currentCol + 2);
					fireShockwave(currentRow, currentCol + 1);
				}
			}
			captureCount = 0;
		} else {
			if (newSpace.getPiece() != null && !newSpace.getPiece().getColor().equals(getColor())) {
				captureCount++;
			}
			super.moveTo(newSpace);
		}
	}

	/**
	 * Returns a deep copy of the Exile Note that it does not return a deep copy of the board. This is because the board is only relevant in
	 * relation to all the other pieces. This method expects the user to set the board of the Exile appropriately. It does not set the space
	 * for similar reasons.
	 * 
	 * @return A deep copy of the Exile
	 */
	public ChessPiece copy() {
		Exile copyExile = new Exile(getColor(), null, null);
		copyExile.setCaptureCount(getCaptureCount());
		return copyExile;
	}
}
