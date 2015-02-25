package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs242.chess.CaptureSpace;
import cs242.chess.ChessBoard;
import cs242.chess.ChessPlayer;
import cs242.chess.pieces.Bishop;
import cs242.chess.pieces.ChessPiece;
import cs242.chess.pieces.King;
import cs242.chess.pieces.Knight;
import cs242.chess.pieces.Pawn;
import cs242.chess.pieces.Queen;
import cs242.chess.pieces.Rook;

/**
 * A class to test the ChessPlayer class.
 * 
 * @author REN-JAY_2
 * 
 */
public class ChessPlayerTest {

	private static ChessPlayer player;
	private static ChessBoard board;

	/**
	 * Creates a default ChessBoard.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		board = new ChessBoard();
	}

	/**
	 * Initializes the ChessBoard and the Player array before every test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		board.setChessBoard();
		player = new ChessPlayer(Color.WHITE, board, "New Player");
	}

	/**
	 * Clears the ChessBoard.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		board.clear();
	}

	/**
	 * Tests if the possible moves array contains all the possible moves.
	 */
	@Test
	public void testGetPossibleMoves() {
		ArrayList<CaptureSpace> possibleMoves = player.getPossibleMoves();
		assertEquals(possibleMoves.size(), 16); // should have 16 possible spaces to move to
		Knight knight = (Knight) board.getPointValue(7, 1).getPiece();
		knight.moveTo(board.getPointValue(5, 2));
		possibleMoves = player.getPossibleMoves();
		assertEquals(possibleMoves.size(), 17);
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(3, 3)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(7, 1)));
		assertTrue(board.findCaptureSpace(possibleMoves, board.getPointValue(4, 4)).getPieces()
				.contains(board.getPointValue(6, 4).getPiece()));
		assertTrue(board.findCaptureSpace(possibleMoves, board.getPointValue(4, 4)).getPieces()
				.contains(board.getPointValue(5, 2).getPiece()));
	}

	/**
	 * Tests if the possible moves array only contains move that bring the king out of check when it is in check. This method implicitly
	 * tests moveLeavesKingInCheck.
	 */
	@Test
	public void testGetMovesInCheck() {
		Pawn pawnFrontOfQueen = (Pawn) board.getPointValue(6, 3).getPiece();
		pawnFrontOfQueen.moveTo(board.getPointValue(4, 3));
		Pawn pawnFrontOfKing = (Pawn) board.getPointValue(6, 4).getPiece();
		pawnFrontOfKing.moveTo(board.getPointValue(4, 4));
		Queen blackQueen = (Queen) board.getPointValue(0, 3).getPiece();
		blackQueen.moveTo(board.getPointValue(5, 2)); // checking king
		ArrayList<CaptureSpace> possibleMoves = player.getPossibleMoves();
		assertEquals(possibleMoves.size(), 3); // should have 3 possible spaces to move to
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 2)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(6, 3)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(6, 4)));
		assertTrue(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 2)).getPieces()
				.contains(board.getPointValue(7, 1).getPiece()));
		assertTrue(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 2)).getPieces()
				.contains(board.getPointValue(6, 1).getPiece()));
		assertTrue(board.findCaptureSpace(possibleMoves, board.getPointValue(6, 3)).getPieces()
				.contains(board.getPointValue(7, 2).getPiece()));
		assertTrue(board.findCaptureSpace(possibleMoves, board.getPointValue(6, 4)).getPieces()
				.contains(board.getPointValue(7, 4).getPiece()));
	}

	/**
	 * Tests if the possible moves array is empty on checkmate.
	 */
	@Test
	public void testCheckMate() {
		Bishop bishop = (Bishop) board.getPointValue(0, 5).getPiece();
		bishop.moveTo(board.getPointValue(3, 2));
		Queen queen = (Queen) board.getPointValue(0, 3).getPiece();
		player.removePiece(board.getPointValue(6, 5).getPiece());
		queen.moveTo(board.getPointValue(6, 5)); // checkmate
		ArrayList<CaptureSpace> possibleMoves = player.getPossibleMoves();
		assertEquals(possibleMoves.size(), 0); // should have 0 possible spaces to move to
	}

	/**
	 * Tests if the possible moves array is empty on checkmate.
	 */
	@Test
	public void testBoardUnchanged() {
		Pawn pawnFrontOfQueen = (Pawn) board.getPointValue(6, 3).getPiece();
		pawnFrontOfQueen.moveTo(board.getPointValue(4, 3));
		Pawn pawnFrontOfKing = (Pawn) board.getPointValue(6, 4).getPiece();
		pawnFrontOfKing.moveTo(board.getPointValue(4, 4));
		Pawn pawnThatCanCaptureQueen = (Pawn) board.getPointValue(6, 1).getPiece();
		Queen blackQueen = (Queen) board.getPointValue(0, 3).getPiece();
		blackQueen.moveTo(board.getPointValue(5, 2)); // checking king
		player.getPossibleMoves();
		assertSame(board.getPointValue(5, 2).getPiece(), blackQueen);
		assertSame(board.getPointValue(6, 1).getPiece(), pawnThatCanCaptureQueen);
		assertSame(board.getPointValue(7, 4).getPiece(), player.getKing());
	}
}
