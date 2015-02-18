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
 * they will be removed from this list. It also keeps track of the player's king object, assuming it is included in the ArrayList,
 * 
 * @author REN-JAY_2
 * 
 */
public class ChessPlayer {

	private ArrayList<ChessPiece> pieces;
	private ChessBoard board;
	private King king;

	/**
	 * Constructor that receives a color and a board. It finds all the pieces of that color on the board and stores them in an ArrayList It
	 * finds the king and stores it in data as well
	 * 
	 * @param color the color of the pieces the player is using
	 * @param startingBoard the board the player is playing on
	 */
	public ChessPlayer(Color color, ChessBoard startingBoard) {
		pieces = startingBoard.getPieces(color);
		board = startingBoard;
		for (ChessPiece p : pieces) {
			if (p instanceof King) {
				king = (King) p;
				break;
			}
		}
	}

	/**
	 * Returns the ArrayList of ChessPieces
	 * 
	 * @return the ArrayList of ChessPieces
	 */
	public ArrayList<ChessPiece> getPieces() {
		return pieces;
	}

	/**
	 * Sets the pieces ArrayList to a new ArrayList
	 * 
	 * @param newPieces the new ArrayList of pieces
	 */
	public void setPieces(ArrayList<ChessPiece> newPieces) {
		pieces = newPieces;
	}

	/**
	 * Removes a piece from the player's ArrayList of ChessPieces
	 * 
	 * @param piece the piece to be removed
	 */
	public void removePiece(ChessPiece piece) {
		pieces.remove(piece);
	}

	/**
	 * Adds a piece from the player's ArrayList of ChessPieces
	 * 
	 * @param piece the piece to be added
	 */
	public void addPiece(ChessPiece piece) {
		pieces.add(piece);
	}

	/**
	 * Returns the player's king
	 * 
	 * @return the player's king
	 */
	public King getKing() {
		return king;
	}

	/**
	 * Tests if moving a piece to a target space puts the king in check The method returns the ChessBoard to its original state at the end
	 * of the method The method assumes that the move is valid, has a clear path and is of a different color
	 * 
	 * @param piece the piece to be moved
	 * @param targetSpace the space the piece wants to be moved to
	 * @return true if after the move the king is in check, false otherwise
	 */
	public boolean moveLeavesKingInCheck(ChessPiece piece, ChessSpace targetSpace) {
		if (piece instanceof Exile) {
			ChessBoard testBoard = (ChessBoard) board.copy();
			ChessSpace testSpace = testBoard.getPointValue(piece.getSpace().getRow(), piece.getSpace().getCol());
			return moveLeavesKingInCheck(testBoard, testSpace.getPiece(), testBoard.getPointValue(targetSpace.getRow(), targetSpace.getCol()));
		}
		ChessPiece targetPiece = targetSpace.getPiece(); // This is the piece at the targetSpace (can be null)}
		ChessSpace originalSpace = piece.getSpace();
		ArrayList<ChessPiece> opponentPieces = board.getOpponentPieces(piece.getColor());
		ArrayList<CaptureSpace> dangerSpaces = board.findPossibleMoves(opponentPieces);
		// remove from opponent list
		if (targetPiece != null) {
			opponentPieces.remove(targetPiece);
		}
		// if it is a Pawn, we don't want to let it promote
		Pawn currentPawn = null;
		if (piece instanceof Pawn) {
			currentPawn = (Pawn) piece;
		}
		if (currentPawn != null
				&& ((currentPawn.getDirection() == 0 && targetSpace.getRow() == 7) || (currentPawn.getDirection() == 1 && targetSpace
						.getRow() == 0))) {
			if (targetPiece != null) {
				targetSpace.getPiece().setSpace(null);
			}
			targetSpace.setPiece(piece);
			piece.getSpace().setPiece(null);
			piece.setSpace(targetSpace);
		} else { // otherwise use the abstract method to move the piece (and remove the piece there)
			piece.moveTo(targetSpace);
		}
		// check if in check
		dangerSpaces = board.findPossibleMoves(opponentPieces);
		CaptureSpace canCaptureKing = board.findCaptureSpace(dangerSpaces, king.getSpace());
		// move piece back
		piece.moveTo(originalSpace);
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
	 * A more inefficient but conceptually simpler and more bug-free way of checking if moving to a space leaves the king in check
	 * The method makes a copy of the ChessBoard. We only use this method when it is easier to code and debug
	 * Otherwise, the checking takes too long
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
	 * Returns a list of all valid moves a player can make according to the rules of chess This accounts for putting the King in check
	 * 
	 * @return an ArrayList of all the moves the player can make
	 */
	public ArrayList<CaptureSpace> getPossibleMoves() {
		if (pieces.size() == 0) {
			return null;
		}
		ArrayList<CaptureSpace> possibleMoves = board.findPossibleMoves(pieces);
		for (int spaceIndex = possibleMoves.size() - 1; spaceIndex >= 0; spaceIndex--) {
			CaptureSpace currentSpace = possibleMoves.get(spaceIndex);
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
}
