package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import cs242.chess.ChessBoard;
import cs242.chess.ChessSpace;
import cs242.chess.pieces.Exile;
import cs242.chess.pieces.Queen;

/**
 * A class to test the functionality of the Exile class.
 * 
 * @author REN-JAY_2
 * 
 */
public class ExileTest {

	private static ChessBoard board;
	private static Exile exile;
	private static final double DELTA = .01;

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
	 * Resets the ChessBoard and sets the exile back to null.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		board.clear();
		exile = null;
	}

	/**
	 * Tests to make sure all the fields are properly instantiated, including row, column, ChessSpace and value.
	 */
	@Test
	public void testInstantiate() {
		exile = new Exile(Color.WHITE, board.getPointValue(3, 4), board);
		board.getPointValue(3, 4).setPiece(exile);
		assertEquals(exile.getSpace().getRow(), 3, DELTA);
		assertEquals(exile.getSpace().getCol(), 4, DELTA);
		assertSame("Error: Pieces are not the same", exile, exile.getSpace().getPiece());
		assertEquals(exile.getCaptureCount(), 0);
		assertEquals(exile.getValue(), 7);
	}

	/**
	 * Tests to make sure that an out of bounds exception is thrown when instantiating with an out of bounds point.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testBadInstantiate() {
		exile = new Exile(Color.WHITE, board.getPointValue(-1, 8), board);
		board.getPointValue(-1, 8).setPiece(exile);
	}

	/**
	 * Tests to make sure the method returns true on valid moves and false on invalid moves.
	 */
	@Test
	public void testValidMove() {
		board.setChessBoard();
		exile = new Exile(Color.WHITE, board.getPointValue(3, 4), board);
		board.getPointValue(3, 4).setPiece(exile);
		assertTrue(exile.validMove(board.getPointValue(4, 5))); // diagonals
		assertTrue(exile.validMove(board.getPointValue(2, 5)));
		assertTrue(exile.validMove(board.getPointValue(3, 5))); // horizontal
		assertTrue(exile.validMove(board.getPointValue(4, 4))); // vertical
		assertFalse(exile.validMove(board.getPointValue(5, 4)));
		exile.moveTo(board.getPointValue(0, 0));
		exile.moveTo(board.getPointValue(0, 1));
		exile.moveTo(board.getPointValue(0, 2)); // capture count is 3
		assertTrue(exile.validMove(board.getPointValue(0, 4))); // shockwave
		assertTrue(exile.validMove(board.getPointValue(1, 4))); // shockwave
		assertFalse(exile.validMove(board.getPointValue(0, 2))); // own space is not valid
	}

	/**
	 * Tests to make sure the valid moves still return true even if the moves go "off" the board.
	 */
	@Test
	public void testValidMoveOffBoard() {
		exile = new Exile(Color.WHITE, new ChessSpace(-1, 8), board);
		assertTrue(exile.validMove(new ChessSpace(-2, 8)));
		assertTrue(exile.validMove(new ChessSpace(0, 8)));
		assertTrue(exile.validMove(new ChessSpace(-1, 9)));
	}

	/**
	 * Make sure the method updates the ChessSpace and ChessPiece when the piece is moved.
	 */
	@Test
	public void testMoveTo() {
		exile = new Exile(Color.WHITE, board.getPointValue(3, 4), board);
		board.getPointValue(3, 4).setPiece(exile);
		exile.moveTo(board.getPointValue(3, 5));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The Exile's space data was not updated correctly", exile.getSpace(), board.getPointValue(3, 5));
		assertSame("Error: The ChessSpace's piece data was not updated correctly", exile, board.getPointValue(3, 5).getPiece());
	}

	/**
	 * Make sure that when Exile captures a piece, it updates its value and capture count.
	 */
	@Test
	public void testCapture() {
		exile = new Exile(Color.WHITE, board.getPointValue(3, 4), board);
		board.getPointValue(3, 4).setPiece(exile);
		Queen opponentQueen = new Queen(Color.BLACK, board.getPointValue(3, 5));
		board.getPointValue(3, 5).setPiece(opponentQueen);
		exile.moveTo(board.getPointValue(3, 5));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The ChessSpace's piece data was not updated correctly", exile, board.getPointValue(3, 5).getPiece());
		assertNull(opponentQueen.getSpace());
		assertEquals(exile.getCaptureCount(), 1);
	}

	/**
	 * Tests the Exile's shockwave functionality.
	 */
	@Test
	public void testShockwaveWhite() {
		board.setChessBoard();
		board.addNewPieces();
		exile = (Exile) board.getPointValue(6, 1).getPiece();
		exile.moveTo(board.getPointValue(0, 7));
		exile.moveTo(board.getPointValue(0, 6));
		exile.moveTo(board.getPointValue(0, 5)); // shockwave ready
		exile.moveTo(board.getPointValue(0, 0)); // shouldnt fire
		assertNotNull(board.getPointValue(0, 1).getPiece());
		exile.moveTo(board.getPointValue(3, 1));
		exile.moveTo(board.getPointValue(2, 1));
		assertEquals(exile.getCaptureCount(), 4);
		exile.moveTo(board.getPointValue(0, 0)); // fire shockwave
		assertNull(board.getPointValue(0, 0).getPiece());
		assertNull(board.getPointValue(0, 1).getPiece());
		assertNull(board.getPointValue(0, 2).getPiece());
		assertNull(board.getPointValue(1, 1).getPiece());
		assertSame(board.getPointValue(2, 1), exile.getSpace());
		exile.moveTo(board.getPointValue(1, 7));
		exile.moveTo(board.getPointValue(1, 6));
		exile.moveTo(board.getPointValue(1, 5));
		assertEquals(exile.getCaptureCount(), 3);
		exile.moveTo(board.getPointValue(2, 1));
		board.getPointValue(7, 0).getPiece().moveTo(board.getPointValue(0, 0)); // move same color piece over
		exile.moveTo(board.getPointValue(0, 0));
		assertNotNull(board.getPointValue(0, 0).getPiece());
		assertEquals(exile.getCaptureCount(), 0);
	}
	
	/**
	 * Tests the Exile's shockwave functionality from a different direction.
	 */
	@Test
	public void testShockwaveBlack() {
		board.setChessBoard();
		board.addNewPieces();
		exile = (Exile) board.getPointValue(1, 6).getPiece();
		exile.moveTo(board.getPointValue(7, 0));
		exile.moveTo(board.getPointValue(7, 1));
		exile.moveTo(board.getPointValue(7, 2));
		exile.moveTo(board.getPointValue(7, 3));
		assertEquals(exile.getCaptureCount(), 4);
		exile.moveTo(board.getPointValue(5, 5));
		exile.moveTo(board.getPointValue(7, 5));
		assertNull(board.getPointValue(7, 5).getPiece());
		assertNull(board.getPointValue(7, 4).getPiece());
		assertNull(board.getPointValue(7, 6).getPiece());
		assertNull(board.getPointValue(6, 5).getPiece());
		assertNotNull(board.getPointValue(6, 4).getPiece());
		assertSame(board.getPointValue(5, 5), exile.getSpace());
		assertEquals(exile.getCaptureCount(), 0);
	}
}
