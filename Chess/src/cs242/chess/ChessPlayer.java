package cs242.chess;

import java.util.ArrayList;

import cs242.chess.pieces.ChessPiece;
import cs242.chess.pieces.King;

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
	 * Constructor that receives an ArrayList of ChessPieces and stores them in data
	 * It finds the king and stores it in data as well
	 * 
	 * @param startingPieces the ArrayList of pieces the player has
	 */
	public ChessPlayer(ArrayList<ChessPiece> startingPieces, ChessBoard startingBoard) {
		pieces = startingPieces;
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
	 * Tests if moving a piece to a target space puts the king in check
	 * The method returns the ChessBoard to its original state at the end of the method
	 * The method assumes that the move is valid, has a clear path and is of a different color
	 * 
	 * @param piece the piece to be moved
	 * @param targetSpace the space the piece wants to be moved to
	 * @return true if after the move the king is in check, false otherwise
	 */
	public boolean moveLeavesKingInCheck(ChessPiece piece, ChessSpace targetSpace) {
		ChessPiece targetPiece = targetSpace.getPiece(); // This is the piece at the targetSpace (can be null)
		ChessSpace originalSpace = piece.getSpace();
		ArrayList<ChessPiece> opponentPieces = board.getOpponentPieces(piece.getColor());
		ArrayList<CaptureSpace> dangerSpaces = board.findPossibleMoves(opponentPieces);
		// remove from opponent list
		if (targetPiece != null) {
			opponentPieces.remove(targetPiece);
		}
		// move piece (and remove piece there)
		piece.moveTo(targetSpace);
		// check if in check
		dangerSpaces = board.findPossibleMoves(opponentPieces);
		CaptureSpace canCaptureKing = board.findCaptureSpace(dangerSpaces, king.getSpace());
		// move piece back
		piece.moveTo(originalSpace);
		// undo capture and re-add piece. If there is no piece there, targetSpace will have its piece set to null
		targetSpace.setPiece(targetPiece);
		return canCaptureKing == null? false : true;
	}
	
	/**
	 * Returns a list of all valid moves a player can make according to the rules of chess
	 * This accounts for putting the King in check
	 * @return an ArrayList of all the moves the player can make
	 */
	public ArrayList<CaptureSpace> getPossibleMoves() {
		if (pieces.size() == 0) {
			return null;
		}
		ArrayList<CaptureSpace> possibleMoves = board.findPossibleMoves(pieces);
		for (int spaceIndex = pieces.size() - 1; spaceIndex >= 0; spaceIndex--) {
			CaptureSpace currentSpace = possibleMoves.get(spaceIndex);
			for (int pieceIndex = currentSpace.getPieces().size() - 1; pieceIndex >= 0; pieceIndex--) {
					if (moveLeavesKingInCheck(currentSpace.getPieces().get(pieceIndex), currentSpace)) {
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
