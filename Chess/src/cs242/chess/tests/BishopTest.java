package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import cs242.chess.ChessBoard;
import cs242.chess.ChessSpace;
import cs242.chess.pieces.Bishop;

/**
 * A class to test the functionality of the Bishop class
 * 
 * @author REN-JAY_2
 * 
 */
public class BishopTest {

	private static ChessBoard board;
	private static Bishop bishop;
	private static final double DELTA = .01;

	/**
	 * Creates a default ChessBoard
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		board = new ChessBoard();
	}

	/**
	 * Resets the ChessBoard and sets the bishop back to null
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		board.clear();
		bishop = null;
	}

	/**
	 * Tests to make sure all the fields are properly instantiated, including row, column, ChessSpace and value
	 */
	@Test
	public void testInstantiate() {
		bishop = new Bishop(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(bishop);
		assertEquals(bishop.getSpace().getRow(), 3, DELTA);
		assertEquals(bishop.getSpace().getCol(), 4, DELTA);
		assertSame("Error: Pieces are not the same", bishop, bishop.getSpace().getPiece());
		assertEquals(bishop.getValue(), 3);
	}

	/**
	 * Tests to make sure that an out of bounds exception is thrown when instantiating with an out of bounds point
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testBadInstantiate() {
		bishop = new Bishop(Color.WHITE, board.getPointValue(-1, 8));
		board.getPointValue(-1, 8).setPiece(bishop);
	}

	/**
	 * Tests to make sure the method returns true on valid moves and false on invalid moves
	 */
	@Test
	public void testValidMove() {
		bishop = new Bishop(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(bishop);
		assertTrue(bishop.validMove(board.getPointValue(5, 6)));
		assertFalse(bishop.validMove(board.getPointValue(7, 7)));
		assertFalse(bishop.validMove(board.getPointValue(3, 4))); // own space is not valid
		assertTrue(bishop.validMove(new ChessSpace(7, 8)));
		assertTrue(bishop.validMove(new ChessSpace(-1, 0)));
	}

	/**
	 * Tests to make sure the valid moves still return true even if the moves go "off" the board
	 */
	@Test
	public void testValidMoveOffBoard() {
		bishop = new Bishop(Color.WHITE, new ChessSpace(-1, 8));
		assertTrue(bishop.validMove(new ChessSpace(0, 7)));
		assertFalse(bishop.validMove(new ChessSpace(1, 5)));
		assertTrue(bishop.validMove(new ChessSpace(-2, 9)));
	}

	/**
	 * Make sure the method updates the ChessSpace and ChessPiece when the piece is moved
	 */
	@Test
	public void testMoveTo() {
		bishop = new Bishop(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(bishop);
		bishop.moveTo(board.getPointValue(5, 6));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The Bishop's space data was not updated correctly", bishop.getSpace(), board.getPointValue(5, 6));
		assertSame("Error: The ChessSpace's piece data was not updated correctly", bishop, board.getPointValue(5, 6).getPiece());
	}

	/**
	 * Make sure the method updates the ChessSpace and both ChessPieces when a piece captures another piece
	 */
	@Test
	public void testCapture() {
		bishop = new Bishop(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(bishop);
		Bishop opponentBishop = new Bishop(Color.BLACK, board.getPointValue(3, 6));
		board.getPointValue(3, 6).setPiece(opponentBishop);
		bishop.moveTo(board.getPointValue(3, 6));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The ChessSpace's piece data was not updated correctly", bishop, board.getPointValue(3, 6).getPiece());
		assertNull(opponentBishop.getSpace());
	}
}

