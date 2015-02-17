package cs242.chess;

import java.awt.Color;
import java.util.ArrayList;

import cs242.chess.pieces.ChessPiece;
import cs242.chess.pieces.King;
import cs242.chess.pieces.Pawn;
import cs242.chess.pieces.Queen;
import cs242.chess.pieces.Rook;

// TODO Account for moves that put the king in check (Noticed when Queen evasion move left king in check)
public class ComputerPlayer extends ChessPlayer {

	private ChessBoard board;
	private ArrayList<CaptureSpace> dangerSpaces = new ArrayList<CaptureSpace>();
	private ChessPiece king;

	public ComputerPlayer(Color color, ChessBoard newBoard) {
		super(color, newBoard);
		board = newBoard;
		for (ChessPiece p : getPieces()) // should only be one king
		{
			if (p instanceof King) {
				king = p;
			}
		}
	}

	public boolean randomMove() {
		boolean moved = false;
		int tryCount = 0;
		while (!moved) {
			int index = (int) (getPieces().size() * Math.random());
			ChessPiece p = getPieces().get(index);
			ArrayList<ChessSpace> possibleSpaces = board.findPossibleMoves(p, false);
			if (possibleSpaces.size() != 0) {
				int moveIndex = (int) (possibleSpaces.size() * Math.random());
				ChessSpace targetSpace = possibleSpaces.get(moveIndex);
				boolean isSafe = false;
				if (targetSpace.getPiece() == null || p.getColor() != targetSpace.getPiece().getColor()) // is empty or has opponent piece
				{
					if (p instanceof Pawn && (targetSpace.getRow() == 0 || targetSpace.getRow() == 7)) {
						p.getSpace().setPiece(null);
						ChessPiece queen = new Queen(p.getColor(), targetSpace);
						targetSpace.setPiece(queen);
						addPiece(queen);
						removePiece(p);
						return true;
					} else {
						if (tryCount > 10) {
							System.out.println("took 10 tries, but still no");
							isSafe = true; // there may be no safe spaces
						} else {
							ChessSpace pieceSpace = p.getSpace();
							ChessPiece targetPiece = targetSpace.getPiece();
							p.getSpace().setPiece(null); // temporary
							targetSpace.setPiece(p); // if piece is in new place
							ArrayList<CaptureSpace> temp = dangerSpaces;
							dangerSpaces = board.findPossibleMovesIgnoreColor(board.getOpponentPieces(king.getColor()));
							isSafe = !inDanger(targetSpace);
							dangerSpaces = temp; // returning to normal
							pieceSpace.setPiece(p);
							targetSpace.setPiece(targetPiece);
						}
						if (isSafe || p instanceof Pawn) // not valuing Pawns as much
						{
							p.moveTo(targetSpace);
							return true;
						}
					}
					tryCount++;
				}
			}
		}
		return false;
	}

	public boolean captureMove(int tryNum) {
		boolean moved = false;
		while (!moved && tryNum > 0) {
			int index = (int) (getPieces().size() * Math.random());
			ChessPiece p = getPieces().get(index);
			if (captureMove(p)) {
				return true;
			}
			tryNum--;
		}
		return false;
	}

	// makes the best possible capture for a piece.
	// returns true if a capture is possible, false if otherwise
	public boolean captureMove(ChessPiece p) {
		ArrayList<ChessSpace> possibleCaptures = board.findPossibleMoves(p, true);
		ChessSpace bestSpace = null;
		int bestVal = -1;
		if (possibleCaptures.size() > 0) {
			for (ChessSpace space : possibleCaptures) {
				int val = space.getPiece().getValue();
				if (inDanger(space)) // if the space is protected by an opponent piece
				{
					val -= p.getValue();
				}
				if (val > bestVal) {
					bestVal = val;
					bestSpace = space;
				}
			}
			if (bestSpace != null) {
				if (p instanceof Pawn && (bestSpace.getRow() == 0 || bestSpace.getRow() == 7)) {
					p.getSpace().setPiece(null);
					ChessPiece queen = new Queen(p.getColor(), bestSpace);
					bestSpace.setPiece(queen);
					addPiece(queen);
					removePiece(p);	
				} else {
					p.moveTo(bestSpace);
				}
				return true;
			}
		}
		return false;
	}

	// attempts to capture a certain space in the best way possible
	// if no decent way is possible, return false
	public boolean captureSpace(ArrayList<ChessPiece> pieces, ChessSpace space) {
		ArrayList<ChessPiece> capturePieces = new ArrayList<ChessPiece>();
		for (ChessPiece p : pieces) {
			if (p.validMove(space) && board.hasClearPath(p, space)) {
				capturePieces.add(p); // piece can capture the space
			}
		}
		ArrayList<ChessPiece> bestPieces = new ArrayList<ChessPiece>();
		int bestVal = -1;
		int startingVal = 0;
		if (space.getPiece() != null) // there is a piece at the space
		{
			startingVal = space.getPiece().getValue();
		}
		boolean inDanger = inDanger(space);
		for (ChessPiece p : capturePieces) {
			int val = startingVal;
			if (inDanger) {
				val -= p.getValue();
			}
			if (val > bestVal) {
				bestPieces.clear();
				bestPieces.add(p);
			}
			if (val == bestVal && val >= 0) {
				bestPieces.add(p);
			}
		}
		if (bestPieces.size() > 0) {
			int randomIndex = (int) (Math.random() * bestPieces.size());
			ChessPiece bestPiece = bestPieces.get(randomIndex);
			bestPiece.moveTo(space);
			return true;
		}
		return false;
	}

	public boolean evasionMove(ChessPiece p) {
		// first, see if it is worth capturing the enemy piece
		ArrayList<ChessPiece> enemyPieces = board.findCaptureSpace(dangerSpaces, p.getSpace()).getPieces();
		if (enemyPieces.size() == 1) {
			if (captureSpace(getPieces(), enemyPieces.get(0).getSpace())) // can capture enemy space well
			{
				return true;
			}
		}
		ArrayList<ChessSpace> possibleMoves = board.findPossibleMoves(p, false);
		ArrayList<CaptureSpace> temp = dangerSpaces;
		board.getPointValue(p.getSpace().getRow(), p.getSpace().getCol()).setPiece(null); // temporarily removing the piece.
		dangerSpaces = board.findPossibleMovesIgnoreColor(board.getOpponentPieces(king.getColor())); // finding the dangerSpaces without the
																										// piece
		dangerSpaces = adjustForPawns(dangerSpaces); // to adjust for possible pawn captures, and possible safe spaces
		ArrayList<ChessSpace> safeMoves = findSafeSpaces(possibleMoves);
		board.getPointValue(p.getSpace().getRow(), p.getSpace().getCol()).setPiece(p); // returning piece
		dangerSpaces = temp; // returning the dangeSpaces back to before
		if (safeMoves.size() > 0) {
			boolean successfulCapture = captureMove(p); // try to capture (partly covered by above part of method)
			if (!successfulCapture) // cannot capture. random evasion move
			{
				int moveIndex = (int) (safeMoves.size() * Math.random());
				ChessSpace targetSpace = safeMoves.get(moveIndex);
				p.moveTo(targetSpace);
			}
			return true;
		}
		// need to add for moving other pieces (worth less) in the line of danger
		return false;
	}

	// take out spaces in front of enemy pawns
	// put in spaces where enemy pawns could capture if computer piece was there
	public ArrayList<CaptureSpace> adjustForPawns(ArrayList<CaptureSpace> captureSpacesList) {
		ArrayList<CaptureSpace> captureSpaces = new ArrayList<CaptureSpace>(); // making a copy
		for (CaptureSpace cs : captureSpacesList) {
			captureSpaces.add(cs);
		}
		int removeIndex = -1;
		ArrayList<Integer> removeSpaceIndices = new ArrayList<Integer>();
		Color compColor = getPieces().get(0).getColor();
		for (CaptureSpace cs : captureSpaces) // there should be a maximum of one improperly added Pawn per space
		{
			for (ChessPiece capturePiece : cs.getPieces()) {
				if (capturePiece instanceof Pawn) {
					if (capturePiece.getSpace().getCol() == cs.getCol()) {
						removeIndex = cs.getPieces().indexOf(capturePiece);
					}
				}
			}
			if (removeIndex != -1) {
				cs.getPieces().remove(removeIndex);
				removeIndex = -1;
				if (cs.getPieces().size() == 0) // space to be removed has no more pieces that can capture it
				{
					removeSpaceIndices.add(captureSpaces.indexOf(cs));
				}
			}
		}
		for (int i = removeSpaceIndices.size() - 1; i >= 0; i--) // starting from the end, so that taking out the capture spaces does not
																	// change the removeSpacIndices
		{
			captureSpaces.remove(removeSpaceIndices.get(i).intValue());
		}
		ArrayList<CaptureSpace> posNewCaptureSpaces = new ArrayList<CaptureSpace>(); // add in spaces where an opponent pawn could capture,
																						// if there was a computer piece there
		boolean addSpaceOne = false;
		CaptureSpace newCapSpace = null;
		for (int i = 0; i < board.getLength(); i++) {
			for (int j = 0; j < board.getWidth(); j++) {
				ChessSpace space = board.getPointValue(i, j);
				if (space.getPiece() == null && space.getRow() < 7) // pawns wont be able to capture a space on the 7th row
				{
					ChessSpace posCaptureSpace = null;
					if (space.getCol() > 0) {
						posCaptureSpace = board.getPointValue(space.getRow() + 1, space.getCol() - 1);
						if (posCaptureSpace.getPiece() instanceof Pawn && posCaptureSpace.getPiece().getColor() != compColor) // is an
																																// opponent
																																// pawn
						{
							newCapSpace = new CaptureSpace(space);
							newCapSpace.addPiece(posCaptureSpace.getPiece());
							addSpaceOne = true;
						}
					}
					if (space.getCol() < 7) {
						posCaptureSpace = board.getPointValue(space.getRow() + 1, space.getCol() + 1);
						if (posCaptureSpace.getPiece() instanceof Pawn && posCaptureSpace.getPiece().getColor() != compColor) // is an
																																// opponent
																																// pawn
						{
							if (addSpaceOne) {
								newCapSpace.addPiece(posCaptureSpace.getPiece());
							} else {
								newCapSpace = new CaptureSpace(space);
								newCapSpace.addPiece(posCaptureSpace.getPiece());
							}
						}
					}
					if (newCapSpace != null) {
						posNewCaptureSpaces.add(newCapSpace);
						newCapSpace = null;
						addSpaceOne = false;
					}
				}
			}
		}
		for (CaptureSpace newCS : posNewCaptureSpaces) {
			CaptureSpace originalSpace = board.findCaptureSpace(captureSpaces, newCS);
			if (originalSpace == null) // space is not there
			{
				captureSpaces.add(newCS);
			} else // space is already there
			{
				for (ChessPiece newPiece : newCS.getPieces()) {
					originalSpace.addPiece(newPiece); // the new pieces shouldn't have any overlap
				}
			}
		}
		return captureSpaces;
	}

	public ArrayList<ChessSpace> findSafeSpaces(ArrayList<ChessSpace> possibleSpaces) {
		ArrayList<ChessSpace> safeMoves = new ArrayList<ChessSpace>();
		for (ChessSpace space : possibleSpaces) {
			if (!inDanger(space)) {
				safeMoves.add(space);
			}
		}
		return safeMoves;
	}

	public boolean inDanger(ChessSpace space) {
		for (CaptureSpace c : dangerSpaces) {
			if (c.equalsChessSpace(space)) {
				return true;
			}
		}
		return false;
	}

	// this method is subjective to what is considered a valuable piece.
	// currently, it considers queens and rooks valuable.
	// it adds the most valuable pieces to the front
	public ArrayList<ChessPiece> getValuablePieces() {
		ArrayList<ChessPiece> pieces = getPieces();
		ArrayList<ChessPiece> valuable = new ArrayList<ChessPiece>();
		for (ChessPiece p : pieces) {
			if (p instanceof Queen) {
				valuable.add(p);
			}
		}
		for (ChessPiece p : pieces) {
			if (p instanceof Rook) {
				valuable.add(p);
			}
		}
		return valuable;
	}

	public boolean hasMoveablePiece() {
		ArrayList<ChessPiece> pieces = getPieces();
		for (ChessPiece p : pieces) {
			if (board.findPossibleMoves(p, false).size() != 0) {
				return true;
			}
		}
		return false;
	}

	public boolean move() {
		dangerSpaces = board.findPossibleMovesIgnoreColor(board.getOpponentPieces(king.getColor()));
		boolean kingDanger = inDanger(king.getSpace());
		if (kingDanger) {
			System.out.println("Opponent king in check");
			boolean escaped = evasionMove(king);
			return escaped;
		}
		ArrayList<ChessPiece> valuablePieces = getValuablePieces();
		for (ChessPiece p : valuablePieces) {
			if (inDanger(p.getSpace())) {
				boolean success = evasionMove(p);
				if (success) {
					System.out.println("evaded!");
					return success;
				}
				System.out.println("failed evasion..." + p);
			}
		}
		if (captureMove(10)) {
			System.out.println("capture move");
			return true;
		}
		System.out.println("random...");
		return randomMove();
	}
}
