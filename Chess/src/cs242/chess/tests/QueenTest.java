package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import cs242.chess.ChessBoard;
import cs242.chess.ChessSpace;
import cs242.chess.pieces.Queen;

/**
 * A class to test the functionality of the Queen class
 * 
 * @author REN-JAY_2
 * 
 */
public class QueenTest {

	private static ChessBoard board;
	private static Queen queen;
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
	 * Resets the ChessBoard and sets the queen back to null
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		board.clear();
		queen = null;
	}

	/**
	 * Tests to make sure all the fields are properly instantiated, including row, column, ChessSpace and value
	 */
	@Test
	public void testInstantiate() {
		queen = new Queen(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(queen);
		assertEquals(queen.getSpace().getRow(), 3, DELTA);
		assertEquals(queen.getSpace().getCol(), 4, DELTA);
		assertSame("Error: Pieces are not the same", queen, queen.getSpace().getPiece());
		assertEquals(queen.getValue(), 9);
	}

	/**
	 * Tests to make sure that an out of bounds exception is thrown when instantiating with an out of bounds point
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testBadInstantiate() {
		queen = new Queen(Color.WHITE, board.getPointValue(-1, 8));
		board.getPointValue(-1, 8).setPiece(queen);
	}

	/**
	 * Tests to make sure the method returns true on valid moves and false on invalid moves
	 */
	@Test
	public void testValidMove() {
		queen = new Queen(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(queen);
		assertTrue(queen.validMove(board.getPointValue(5, 6))); // diagonals
		assertTrue(queen.validMove(board.getPointValue(2, 5)));
		assertTrue(queen.validMove(board.getPointValue(3, 6))); // horizontal
		assertTrue(queen.validMove(board.getPointValue(6, 4))); // vertical
		assertFalse(queen.validMove(board.getPointValue(5, 3)));
		assertFalse(queen.validMove(board.getPointValue(3, 4))); // own space is not valid
	}

	/**
	 * Tests to make sure the valid moves still return true even if the moves go "off" the board
	 */
	@Test
	public void testValidMoveOffBoard() {
		queen = new Queen(Color.WHITE, new ChessSpace(-1, 8));
		assertTrue(queen.validMove(new ChessSpace(1, 6)));
		assertTrue(queen.validMove(new ChessSpace(5, 8)));
		assertTrue(queen.validMove(new ChessSpace(-1, 9)));
	}

	/**
	 * Make sure the method updates the ChessSpace and ChessPiece when the piece is moved
	 */
	@Test
	public void testMoveTo() {
		queen = new Queen(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(queen);
		queen.moveTo(board.getPointValue(5, 6));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The Queen's space data was not updated correctly", queen.getSpace(), board.getPointValue(5, 6));
		assertSame("Error: The ChessSpace's piece data was not updated correctly", queen, board.getPointValue(5, 6).getPiece());
	}

	/**
	 * Make sure the method updates the ChessSpace and both ChessPieces when a piece captures another piece
	 */
	@Test
	public void testCapture() {
		queen = new Queen(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(queen);
		Queen opponentQueen = new Queen(Color.BLACK, board.getPointValue(3, 6));
		board.getPointValue(3, 6).setPiece(opponentQueen);
		queen.moveTo(board.getPointValue(3, 6));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The ChessSpace's piece data was not updated correctly", queen, board.getPointValue(3, 6).getPiece());
		assertNull(opponentQueen.getSpace());
	}
}
