package cs242.chess;

import java.awt.Color;
import java.util.ArrayList;

import cs242.chess.pieces.ChessPiece;
import cs242.chess.pieces.Knight;

/**
 * An implementation of a chess board using the Board interface. The board contains a 2-dimensional array of ChessSpaces. It contains
 * methods for assigning ChessSpaces, as well as useful methods for checking conditions.
 * 
 * @author REN-JAY
 * 
 */
public class ChessBoard implements Board<ChessSpace> {

	private ChessSpace[][] dimensions;

	/**
	 * A default constructor that assumes the board has 8x8 dimensions.
	 */
	public ChessBoard() {
		this(8, 8);
	}

	/**
	 * Takes a row and a column number. It sets the dimensions of the board accordingly.
	 * 
	 * @param row The number of rows in the board.
	 * @param col The number of columns in the board.
	 */
	public ChessBoard(int row, int col) {
		dimensions = new ChessSpace[row][col];
		clear();
	}

	/**
	 * Returns the ChessSpace value at a certain index in the board.
	 * 
	 * @param row The row index
	 * @param col The column index
	 * @return The ChessSpace at the given index.
	 */
	public ChessSpace getPointValue(int row, int col) {
		return dimensions[row][col];
	}

	/**
	 * Sets the ChessSpace value at a certain index in the board.
	 * 
	 * @param row The row index
	 * @param col The column index
	 * @param value The new ChessSpace value to be set at the index
	 */
	public void setPointValue(int row, int col, ChessSpace value) {
		dimensions[row][col] = value;
	}

	/**
	 * Returns the length, or number of rows, of the board.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return dimensions.length;
	}

	/**
	 * Returns the width, or number of columns, of the board.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return dimensions[0].length;
	}

	/**
	 * Sets the internal representation of the board to a given 2-dimensional array of ChessSpaces. The method makes a shallow copy of the
	 * dimensions.
	 * 
	 * @param newDimensions The new 2-dimensional array of the board
	 */
	public void setDimensions(ChessSpace[][] newDimensions) {
		dimensions = newDimensions;
	}

	/**
	 * Makes a deep copy of the board with a shallow copy of the ChessPieces.
	 * 
	 * @return A deep copy of the ChessBoard. Note that the copy returned is of type ChessBoard, a subclass of Board<ChessSpace>
	 */
	public Board<ChessSpace> copy() {
		ChessBoard newBoard = new ChessBoard(getLength(), getWidth());
		for (int i = 0; i < getLength(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				newBoard.setPointValue(i, j, getPointValue(i, j));
			}
		}
		return newBoard;
	}

	/**
	 * Sets every ChessSpace in the board to a default ChessSpace object.
	 */
	public void clear() {
		for (int i = 0; i < getLength(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				setPointValue(i, j, new ChessSpace(i, j));
			}
		}
	}

	/**
	 * Checks whether or not a ChessPiece can proceed to a given space without being blocked by another ChessPiece. The method assumes that
	 * the targetSpace is a valid target by the piece; i.e. the piece could move to that space if there were no obstacles.
	 * 
	 * @param piece The piece that is being moved
	 * @param targetSpace The space the piece is being moved to
	 * @return True if the piece can be moved to the space, false otherwise.
	 */
	public boolean hasClearPath(ChessPiece piece, ChessSpace targetSpace) {
		if (piece instanceof Knight) { // Knights do not get blocked by other pieces
			return true;
		} // All other pieces move in a straight line to their target.
		int currentRow = piece.getSpace().getRow();
		int currentCol = piece.getSpace().getCol();
		int targetRow = targetSpace.getRow();
		int targetCol = targetSpace.getCol();
		// First test if the path is in the same vertical or horizontal line as the piece.
		if (targetSpace.getRow() == piece.getSpace().getRow() || targetSpace.getCol() == piece.getSpace().getCol()) {
			return hasClearPathVertHorizontal(currentRow, currentCol, targetRow, targetCol);
		}
		// Otherwise, it must proceed to the space on a diagonal path
		return hasClearPathDiagonal(currentRow, currentCol, targetRow, targetCol);
	}

	/**
	 * A helper method that determines if a piece moving in a vertical or horizontal line from the current space can proceed to the target
	 * space without running into another piece
	 * 
	 * @param curRow the row index of the current space
	 * @param curCol the column index of the current space
	 * @param targetRow the row index of the target space
	 * @param targetCol the column index of the target space
	 * @return True if the piece can proceed without obstacle, false otherwise.
	 */
	public boolean hasClearPathVertHorizontal(int currentRow, int currentCol, int targetRow, int targetCol) {
		if (targetRow == currentRow) {
			if (targetCol > currentCol) {
				for (int i = currentCol + 1; i < targetCol; i++) {
					if (getPointValue(currentRow, i).getPiece() != null) {
						return false;
					}
				}
			} else {
				for (int i = currentCol - 1; i > targetCol; i--) {
					if (getPointValue(currentRow, i).getPiece() != null) {
						return false;
					}
				}
			}
		} else { // Same column
			if (targetRow > currentRow) {
				for (int i = currentRow + 1; i < targetRow; i++) {
					if (getPointValue(i, currentCol).getPiece() != null) {
						return false;
					}
				}
			} else {
				for (int i = currentRow - 1; i > targetRow; i--) {
					if (getPointValue(i, currentCol).getPiece() != null) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * A helper method that determines if a piece moving in a diagonal line from the current space can proceed to the target space without
	 * running into another piece. It does not check whether or not the space has a piece on it currently
	 * 
	 * @param curRow the row index of the current space
	 * @param curCol the column index of the current space
	 * @param targetRow the row index of the target space
	 * @param targetCol the column index of the target space
	 * @return True if the piece can proceed without obstacle, false otherwise.
	 */
	public boolean hasClearPathDiagonal(int currentRow, int currentCol, int targetRow, int targetCol) {
		if (targetRow > currentRow) {
			if (targetCol > currentCol) {
				for (int i = currentRow + 1; i < targetRow; i++) { // iterate towards the bottom right of the array
					if (getPointValue(i, currentCol + (i - currentRow)).getPiece() != null) {
						return false;
					}
				}
			} else {
				for (int i = currentRow + 1; i < targetRow; i++) { // iterate towards the bottom left of the array
					if (getPointValue(i, currentCol - (i - currentRow)).getPiece() != null) {
						return false;
					}
				}
			}
		} else {
			if (targetCol > currentCol) {
				for (int i = currentRow - 1; i > targetRow; i--) { // iterate towards the top right of the array
					if (getPointValue(i, currentCol + (currentRow - i)).getPiece() != null) {
						return false;
					}
				}
			} else {
				for (int i = currentRow - 1; i > targetRow; i--) { // iterate towards the top left of the array
					if (getPointValue(i, currentCol - (currentRow - i)).getPiece() != null) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks if a piece can successfully be moved to another space. The method checks if the move is valid, if there is an open path and if
	 * the new space is within the limits of the board. If there is a piece in the new space of the same color, the move is not allowed
	 * 
	 * @param piece the piece to be moved
	 * @param newSpace the ChessSpace the ChessPiece is moving to
	 * @return true if the piece can be successfully moved, false otherwise
	 */
	public boolean canMove(ChessPiece piece, ChessSpace newSpace) {
		int targetRow = newSpace.getRow();
		int targetCol = newSpace.getCol();
		if (targetRow >= getLength() || targetRow < 0 || targetCol >= getWidth() || targetCol < 0) {
			return false; // out of bounds
		}
		if (!piece.validMove(newSpace) || !hasClearPath(piece, newSpace)) {
			return false; // not a valid move or no clear path
		}
		ChessPiece targetPiece = newSpace.getPiece();
		if (targetPiece != null && targetPiece.getColor().equals(piece.getColor())) {
			return false; // same color piece
		}
		return true;
	}

	/**
	 * Returns an ArrayList of all pieces on the board that are not the given color.
	 * 
	 * @param color The color of pieces not to include
	 * @return An ArrayList of pieces not of the given color
	 */
	public ArrayList<ChessPiece> getOpponentPieces(Color color) {
		// We iterate through the board array and check each piece.
		ArrayList<ChessPiece> opponentPieces = new ArrayList<ChessPiece>();
		for (int i = 0; i < getLength(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (getPointValue(i, j).getPiece() != null && getPointValue(i, j).getPiece().getColor() != color) // if opponent
																													// piece
				{
					opponentPieces.add(getPointValue(i, j).getPiece());
				}
			}
		}
		return opponentPieces;
	}

	/**
	 * Finds all possible spaces a ChessPiece can move to on a given ChessBoard
	 * This method accounts move validity, clear path, and enemy piece color (if applicable)
	 * 
	 * @param piece the piece that is moving
	 * @param captureOnly true to only include the spaces where the piece can capture an enemy piece, false otherwise
	 * @return an ArrayList of all spaces the piece can move to
	 */
	public ArrayList<ChessSpace> findPossibleMoves(ChessPiece piece, boolean captureOnly) {
		ArrayList<ChessSpace> spaces = new ArrayList<ChessSpace>();
		boolean captureCondition;
		for (int i = 0; i < getLength(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				ChessSpace targetSpace = getPointValue(i, j);
				captureCondition = captureOnly? targetSpace.getPiece() != null : true;
				// The move is valid if it is a valid space, there is a clear path, and if the space is empty or has an enemy
				// piece.
				if (canMove(piece, targetSpace) && captureCondition) {
					spaces.add(getPointValue(i, j));
				}
			}
		}
		return spaces;
	}

	/**
	 * Find the set of all possible spaces an ArrayList of ChessPieces can move to. It returns the information in an array of CaptureSpaces,
	 * which list which pieces can capture each space. The boolean options allow the user to customize whether or not to consider the color
	 * of the pieces and whether or not to only include moves that capture pieces. This function should not be called. Call the below four
	 * functions when appropriate.
	 * 
	 * @param pieces The pieces that are moving
	 * @param ignoreColor true if the result includes spaces where pieces "capture" pieces of the same color
	 * @param
	 * @return an ArrayList containing all possible spaces.
	 */
	public ArrayList<CaptureSpace> findPossibleMoves(ArrayList<ChessPiece> pieces, boolean ignoreColor, boolean captureMove) {
		ArrayList<CaptureSpace> spaces = new ArrayList<CaptureSpace>();
		boolean addCondition;
		for (int i = 0; i < getLength(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				CaptureSpace captureSpace = new CaptureSpace(getPointValue(i, j));
				for (int k = 0; k < pieces.size(); k++) {
					ChessPiece currentPiece = pieces.get(k);
					ChessPiece targetPiece = getPointValue(i, j).getPiece();
					if (ignoreColor && !captureMove) {
						addCondition = true; // the space is valid as long as a piece can get there
					} else if (!ignoreColor && captureMove) {
						addCondition = targetPiece != null && targetPiece.getColor() != currentPiece.getColor();
					} else if (ignoreColor && captureMove) {
						addCondition = targetPiece != null;
					} else {
						addCondition = targetPiece == null || targetPiece.getColor() != currentPiece.getColor(); // default
																													// condition
					}
					// target space is empty or has opponent piece
					if (currentPiece.validMove(getPointValue(i, j)) && hasClearPath(currentPiece, getPointValue(i, j)) && addCondition) {
						captureSpace.addPiece(currentPiece);
					}
				}
				if (!captureSpace.getPieces().isEmpty()) {
					spaces.add(captureSpace);
				}
			}
		}
		return spaces;
	}

	/**
	 * Find the set of all possible spaces an array of pieces can move to
	 * 
	 * @param pieces the pieces that are moving
	 * @return the ArrayList of possible spaces
	 */
	public ArrayList<CaptureSpace> findPossibleMoves(ArrayList<ChessPiece> pieces) {
		return findPossibleMoves(pieces, false, false);
	}

	/**
	 * Find the set of all possible spaces an array of pieces can move to, allowing pieces to capture pieces of the same color
	 * 
	 * @param pieces the pieces that are moving
	 * @return the ArrayList of possible spaces
	 */
	public ArrayList<CaptureSpace> findPossibleMovesIgnoreColor(ArrayList<ChessPiece> pieces) {
		return findPossibleMoves(pieces, true, false);
	}

	/**
	 * Finds all the possible spaces that can be captured by an ArrayList of pieces
	 * 
	 * @param pieces the pieces that are capturing
	 * @return the ArrayList of possible spaces
	 */
	public ArrayList<CaptureSpace> findCaptureMoves(ArrayList<ChessPiece> pieces) {
		return findPossibleMoves(pieces, false, true);
	}

	/**
	 * Finds all the possible spaces that can be captured by an ArrayList of pieces, allowing the pieces to capture pieces of the same color
	 * 
	 * @param pieces the pieces that are capturing
	 * @return the ArrayList of possible spaces
	 */
	public ArrayList<CaptureSpace> findCaptureMovesIgnoreColor(ArrayList<ChessPiece> pieces) {
		return findPossibleMoves(pieces, true, true);
	}

	/**
	 * Iterates through an ArrayList<CaptureSpace> to find the corresponding CaptureSpace to a given ChessSpace That is, the returned
	 * CaptureSpace has the same row and column as the ChessSpace If no space exists, return null
	 * 
	 * @param spaces the ArrayList to iterate through
	 * @param certainSpace the ChessSpace to find
	 * @return the CaptureSpace that corresponds to the ChessSpace
	 */
	public CaptureSpace findCaptureSpace(ArrayList<CaptureSpace> spaces, ChessSpace certainSpace) {
		for (CaptureSpace cs : spaces) {
			if (cs.equalsChessSpace(certainSpace)) {
				return cs;
			}
		}
		return null;
	}
}