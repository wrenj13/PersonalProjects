package cs242.chess;

import java.awt.Color;
import java.util.ArrayList;

import cs242.chess.pieces.Boo;
import cs242.chess.pieces.ChessPiece;
import cs242.chess.pieces.Exile;
import cs242.chess.pieces.King;
import cs242.chess.pieces.Pawn;

/**
 * Contains the implementation for a human player. The class contains an ArrayList of all pieces that the player has; as pieces are taken,
 * they will be removed from this list. It also keeps track of the player's king object, assuming it is included in the ArrayList.
 * 
 * @author REN-JAY_2
 * 
 */
public class ChessPlayer {

	private ArrayList<ChessPiece> pieces;
	private ChessBoard board;
	private King king;
	private String name;

	/**
	 * Constructor that receives a color and a board. It finds all the pieces of that color on the board and stores them in an ArrayList. It
	 * finds the king and stores it in data as well.
	 * 
	 * @param color the color of the pieces the player is using
	 * @param startingBoard the board the player is playing on
	 */
	public ChessPlayer(Color color, ChessBoard startingBoard, String playerName) {
		pieces = startingBoard.getPieces(color);
		board = startingBoard;
		for (ChessPiece p : pieces) {
			if (p instanceof King) {
				king = (King) p;
				break;
			}
		}
		name = playerName;
	}

	/**
	 * Returns the ArrayList of ChessPieces.
	 * 
	 * @return the ArrayList of ChessPieces
	 */
	public ArrayList<ChessPiece> getPieces() {
		return pieces;
	}

	/**
	 * Sets the pieces ArrayList to a new ArrayList.
	 * 
	 * @param newPieces the new ArrayList of pieces
	 */
	public void setPieces(ArrayList<ChessPiece> newPieces) {
		pieces = newPieces;
	}

	/**
	 * Removes a piece from the player's ArrayList of ChessPieces.
	 * 
	 * @param piece the piece to be removed
	 */
	public void removePiece(ChessPiece piece) {
		pieces.remove(piece);
	}

	/**
	 * Adds a piece from the player's ArrayList of ChessPieces.
	 * 
	 * @param piece the piece to be added
	 */
	public void addPiece(ChessPiece piece) {
		pieces.add(piece);
	}

	/**
	 * Returns the player's king.
	 * 
	 * @return the player's king
	 */
	public King getKing() {
		return king;
	}

	/**
	 * Returns the player's name.
	 * 
	 * @return the player's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the player
	 * 
	 * @param newName the new name of the player
	 */
	public void setName(String newName) {
		name = newName;
	}
	
	/**
	 * Tests if moving a piece to a target space puts the king in check. The method returns the ChessBoard to its original state at the end
	 * of the method. The method assumes that the move is valid, has a clear path and is of a different color.
	 * 
	 * @param piece the piece to be moved
	 * @param targetSpace the space the piece wants to be moved to
	 * @return true if after the move the king is in check, false otherwise
	 */
	public boolean moveLeavesKingInCheck(ChessPiece piece, ChessSpace targetSpace) {
		if (piece instanceof Exile) {
			ChessBoard testBoard = (ChessBoard) board.copy();
			ChessSpace testSpace = testBoard.getPointValue(piece.getSpace().getRow(), piece.getSpace().getCol());
			return moveLeavesKingInCheck(testBoard, testSpace.getPiece(),
					testBoard.getPointValue(targetSpace.getRow(), targetSpace.getCol()));
		}
		ChessPiece targetPiece = targetSpace.getPiece(); // This is the piece at the targetSpace (can be null)}
		ChessSpace originalSpace = piece.getSpace();
		ArrayList<ChessPiece> opponentPieces = board.getOpponentPieces(piece.getColor());
		ArrayList<CaptureSpace> dangerSpaces = board.findPossibleMoves(opponentPieces);
		// remove captured enemy piece from opponent list
		if (targetPiece != null) {
			opponentPieces.remove(targetPiece);
		}
		// we use the abstract method to prevent Pawn promotions and similar move actions
		piece.generalMoveTo(targetSpace);
		// check if in check
		dangerSpaces = board.findPossibleMoves(opponentPieces);
		CaptureSpace canCaptureKing = board.findCaptureSpace(dangerSpaces, king.getSpace());
		// move piece back
		piece.generalMoveTo(originalSpace);
		// undo capture and re-add piece. If there is no piece there, targetSpace will have its piece set to null
		if (targetPiece != null) {
			// if the piece is a Boo, undo the absorption
			if (piece instanceof Boo) {
				Boo boo = (Boo) piece;
				boo.getCaptured().remove(targetPiece);
				boo.setValue(boo.getValue() - targetPiece.getValue());
			}
			targetPiece.generalMoveTo(targetSpace);
		}
		return canCaptureKing == null ? false : true;
	}

	/**
	 * A more inefficient but conceptually simpler and more bug-free way of checking if moving to a space leaves the king in check The
	 * method makes a copy of the ChessBoard. We only use this method when it is easier to code and debug. Otherwise, the checking takes too
	 * long.
	 * 
	 * @param testBoard the copy of the ChessBoard used to test on
	 * @param piece the piece to move
	 * @param targetSpace the space to move the piece to
	 * @return true if after the move the king is in check, false otherwise
	 */
	public boolean moveLeavesKingInCheck(ChessBoard testBoard, ChessPiece piece, ChessSpace targetSpace) {
		ArrayList<ChessPiece> opponentPieces = testBoard.getOpponentPieces(piece.getColor());
		ArrayList<CaptureSpace> dangerSpaces = testBoard.findPossibleMoves(opponentPieces);
		piece.moveTo(targetSpace);
		// remake opponent list
		opponentPieces = testBoard.getOpponentPieces(piece.getColor());
		// check if in check
		dangerSpaces = testBoard.findPossibleMoves(opponentPieces);
		CaptureSpace canCaptureKing = testBoard.findCaptureSpace(dangerSpaces, king.getSpace());
		testBoard.clear();
		return canCaptureKing == null ? false : true;
	}

	/**
	 * Returns a list of all valid moves a player can make according to the rules of chess. This accounts for putting the King in check.
	 * 
	 * @return an ArrayList of all the moves the player can make
	 */
	public ArrayList<CaptureSpace> getPossibleMoves() {
		return getPossibleMoves(pieces);
	}
	
	/**
	 * A helper function that takes in an ArrayList of pieces and returns a list of all valid moves a player can make using those pieces.
	 * 
	 * @param pieceArray the array of pieces to consider
	 * @return an ArrayList of valid spaces
	 */
	public ArrayList<CaptureSpace> getPossibleMoves(ArrayList<ChessPiece> pieceArray) {
		if (pieceArray.size() == 0) {
			return null;
		}
		ArrayList<CaptureSpace> possibleMoves = board.findPossibleMoves(pieceArray);
		// iterate through all spaces
		for (int spaceIndex = possibleMoves.size() - 1; spaceIndex >= 0; spaceIndex--) {
			CaptureSpace currentSpace = possibleMoves.get(spaceIndex);
			// iterate through all pieces that can capture that space
			for (int pieceIndex = currentSpace.getPieces().size() - 1; pieceIndex >= 0; pieceIndex--) {
				if (moveLeavesKingInCheck(currentSpace.getPieces().get(pieceIndex),
						board.getPointValue(currentSpace.getRow(), currentSpace.getCol()))) {
					currentSpace.getPieces().remove(pieceIndex);
				}
			}
			if (currentSpace.getPieces().size() == 0) {
				possibleMoves.remove(spaceIndex);
			}
		}
		return possibleMoves;
	}
	
	/**
	 * Returns a list of possible spaces a single piece can move to according to the rules of Chess.
	 * This method accounts for putting the king in check.
	 * 
	 * @param p the piece in consideration
	 * @return an ArrayList of possible ChessSpaces
	 */
	public ArrayList<ChessSpace> getPossibleMoves(ChessPiece p) {
		ArrayList<ChessSpace> possibleMoves = board.findPossibleMoves(p, false);
		for (int i = possibleMoves.size() - 1; i >= 0; i--) {
			if (moveLeavesKingInCheck(p, possibleMoves.get(i))) {
				possibleMoves.remove(i);
			}
		}
		return possibleMoves;
	}
	
	/**
	 * Determines if the player is in checkmate.
	 * 
	 * @param possibleMoves the valid moves the player can make
	 * @return true if the player is in checkmate, false otherwise
	 */
	public boolean inCheckmate(ArrayList<CaptureSpace> possibleMoves) {
		// we scan the spaces that the opponent can capture.
		// we don't need to account for the opponent putting their king in check because it is not their turn
		// if the player has moves, then no checkmate
		if (possibleMoves.size() != 0) {
			return false;
		}
		// if the king is in check, return true
		return king.getCheck();
	}
	
	/**
	 * Determines if the player is in stalemate.
	 * This method does not account for the 50 move rule.
	 * 
	 * @param possibleMoves the valid moves the player can make
	 * @return true if the player is in stalemate, false otherwise
	 */
	public boolean inStalemate(ArrayList<CaptureSpace> possibleMoves) {
		if (!king.getCheck() && possibleMoves.size() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * A wrapper method for the checkmate and stalemate methods.
	 * We use this method for computational efficiency because the two methods are very similar
	 * 
	 * @return 1 if checkmate, 2 if stalemate, 0 otherwise
	 */
	public int checkEndConditions() {
		ArrayList<CaptureSpace> possibleMoves = getPossibleMoves();
		if (inCheckmate(possibleMoves)) {
			return 1;
		}
		if (inStalemate(possibleMoves)) {
			return 2;
		}
		return 0;
	}
	
	/**
	 * Updates the pieces array according to the remaining pieces on the board.
	 */
	public void updatePieceArray() {
		setPieces(board.getPieces(getKing().getColor()));
	}
}
