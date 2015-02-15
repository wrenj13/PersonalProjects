package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import cs242.chess.ChessBoard;
import cs242.chess.ChessSpace;
import cs242.chess.pieces.Knight;

/**
 * A class to test the functionality of the Knight class
 * 
 * @author REN-JAY_2
 * 
 */
public class KnightTest {

	private static ChessBoard board;
	private static Knight knight;
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
	 * Resets the ChessBoard and sets the knight back to null
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		board.clear();
		knight = null;
	}

	/**
	 * Tests to make sure all the fields are properly instantiated, including row, column, ChessSpace and value
	 */
	@Test
	public void testInstantiate() {
		knight = new Knight(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(knight);
		assertEquals(knight.getSpace().getRow(), 3, DELTA);
		assertEquals(knight.getSpace().getCol(), 4, DELTA);
		assertSame("Error: Pieces are not the same", knight, knight.getSpace().getPiece());
		assertEquals(knight.getValue(), 3);
	}

	/**
	 * Tests to make sure that an out of bounds exception is thrown when instantiating with an out of bounds point
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testBadInstantiate() {
		knight = new Knight(Color.WHITE, board.getPointValue(-1, 8));
		board.getPointValue(-1, 8).setPiece(knight);
	}

	/**
	 * Tests to make sure the method returns true on valid moves and false on invalid moves
	 */
	@Test
	public void testValidMove() {
		knight = new Knight(Color.WHITE, board.getPointValue(4, 4));
		board.getPointValue(4, 4).setPiece(knight);
		assertTrue(knight.validMove(board.getPointValue(5, 6)));
		assertTrue(knight.validMove(board.getPointValue(6, 5)));
		assertTrue(knight.validMove(board.getPointValue(6, 3)));
		assertTrue(knight.validMove(board.getPointValue(5, 2)));
		assertTrue(knight.validMove(board.getPointValue(3, 2)));
		assertTrue(knight.validMove(board.getPointValue(2, 3)));
		assertTrue(knight.validMove(board.getPointValue(2, 5)));
		assertTrue(knight.validMove(board.getPointValue(3, 6)));
		assertFalse(knight.validMove(board.getPointValue(6, 6)));
		assertFalse(knight.validMove(board.getPointValue(4, 4))); // own space is not valid
	}

	/**
	 * Tests to make sure the valid moves still return true even if the moves go "off" the board
	 */
	@Test
	public void testValidMoveOffBoard() {
		knight = new Knight(Color.WHITE, new ChessSpace(8, 5));
		assertTrue(knight.validMove(new ChessSpace(9, 3)));
		assertFalse(knight.validMove(new ChessSpace(9, 2)));
		assertTrue(knight.validMove(new ChessSpace(6, 6)));
	}

	/**
	 * Make sure the method updates the ChessSpace and ChessPiece when the piece is moved
	 */
	@Test
	public void testMoveTo() {
		knight = new Knight(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(knight);
		knight.moveTo(board.getPointValue(4, 6));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The Knight's space data was not updated correctly", knight.getSpace(), board.getPointValue(4, 6));
		assertSame("Error: The ChessSpace's piece data was not updated correctly", knight, board.getPointValue(4, 6).getPiece());
	}

	/**
	 * Make sure the method updates the ChessSpace and both ChessPieces when a piece captures another piece
	 */
	@Test
	public void testCapture() {
		knight = new Knight(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(knight);
		Knight opponentKnight = new Knight(Color.BLACK, board.getPointValue(4, 6));
		board.getPointValue(4, 6).setPiece(opponentKnight);
		knight.moveTo(board.getPointValue(4, 6));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The ChessSpace's piece data was not updated correctly", knight, board.getPointValue(4, 6).getPiece());
		assertNull(opponentKnight.getSpace());
	}
}
