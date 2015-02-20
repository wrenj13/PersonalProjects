package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import cs242.chess.ChessBoard;
import cs242.chess.ChessSpace;
import cs242.chess.pieces.Rook;

/**
 * A class to test the functionality of the Rook class.
 * 
 * @author REN-JAY_2
 * 
 */
public class RookTest {

	private static ChessBoard board;
	private static Rook rook;
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
	 * Resets the ChessBoard and sets the rook back to null.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		board.clear();
		rook = null;
	}

	/**
	 * Tests to make sure all the fields are properly instantiated, including row, column, ChessSpace and value.
	 */
	@Test
	public void testInstantiate() {
		rook = new Rook(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(rook);
		assertEquals(rook.getSpace().getRow(), 3, DELTA);
		assertEquals(rook.getSpace().getCol(), 4, DELTA);
		assertSame("Error: Pieces are not the same", rook, rook.getSpace().getPiece());
		assertEquals(rook.getValue(), 5);
	}

	/**
	 * Tests to make sure that an out of bounds exception is thrown when instantiating with an out of bounds point.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testBadInstantiate() {
		rook = new Rook(Color.WHITE, board.getPointValue(-1, 8));
		board.getPointValue(-1, 8).setPiece(rook);
	}

	/**
	 * Tests to make sure the method returns true on valid moves and false on invalid moves.
	 */
	@Test
	public void testValidMove() {
		rook = new Rook(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(rook);
		assertTrue(rook.validMove(board.getPointValue(3, 6))); // horizontal
		assertTrue(rook.validMove(board.getPointValue(7, 4))); // vertical
		assertFalse(rook.validMove(board.getPointValue(3, 4))); // own space is not valid
		assertFalse(rook.validMove(board.getPointValue(7, 7)));
	}

	/**
	 * Tests to make sure the valid moves still return true even if the moves go "off" the board.
	 */
	@Test
	public void testValidMoveOffBoard() {
		rook = new Rook(Color.WHITE, new ChessSpace(-1, 8));
		assertTrue(rook.validMove(new ChessSpace(5, 8)));
		assertTrue(rook.validMove(new ChessSpace(-1, 9)));
	}

	/**
	 * Make sure the method updates the ChessSpace and ChessPiece when the piece is moved.
	 */
	@Test
	public void testMoveTo() {
		rook = new Rook(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(rook);
		rook.moveTo(board.getPointValue(7, 4));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The Rook's space data was not updated correctly", rook.getSpace(), board.getPointValue(7, 4));
		assertSame("Error: The ChessSpace's piece data was not updated correctly", rook, board.getPointValue(7, 4).getPiece());
	}

	/**
	 * Make sure the method updates the ChessSpace and both ChessPieces when a piece captures another piece.
	 */
	@Test
	public void testCapture() {
		rook = new Rook(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(rook);
		Rook opponentRook = new Rook(Color.BLACK, board.getPointValue(3, 6));
		board.getPointValue(3, 6).setPiece(opponentRook);
		rook.moveTo(board.getPointValue(3, 6));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The ChessSpace's piece data was not updated correctly", rook, board.getPointValue(3, 6).getPiece());
		assertNull(opponentRook.getSpace());
	}
}
