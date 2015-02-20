package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import cs242.chess.ChessBoard;
import cs242.chess.ChessSpace;
import cs242.chess.pieces.Boo;
import cs242.chess.pieces.Queen;

/**
 * A class to test the functionality of the Boo class.
 * 
 * @author REN-JAY_2
 * 
 */
public class BooTest {

	private static ChessBoard board;
	private static Boo boo;
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
	 * Resets the ChessBoard and sets the boo back to null.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		board.clear();
		boo = null;
	}

	/**
	 * Tests to make sure all the fields are properly instantiated, including row, column, ChessSpace and value.
	 */
	@Test
	public void testInstantiate() {
		boo = new Boo(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(boo);
		assertEquals(boo.getSpace().getRow(), 3, DELTA);
		assertEquals(boo.getSpace().getCol(), 4, DELTA);
		assertSame("Error: Pieces are not the same", boo, boo.getSpace().getPiece());
		assertEquals(boo.getValue(), 5);
	}

	/**
	 * Tests to make sure that an out of bounds exception is thrown when instantiating with an out of bounds point.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testBadInstantiate() {
		boo = new Boo(Color.WHITE, board.getPointValue(-1, 8));
		board.getPointValue(-1, 8).setPiece(boo);
	}

	/**
	 * Tests to make sure the method returns true on valid moves and false on invalid moves.
	 */
	@Test
	public void testValidMove() {
		boo = new Boo(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(boo);
		assertTrue(boo.validMove(board.getPointValue(4, 5))); // diagonals
		assertTrue(boo.validMove(board.getPointValue(2, 5)));
		assertTrue(boo.validMove(board.getPointValue(3, 5))); // horizontal
		assertTrue(boo.validMove(board.getPointValue(4, 4))); // vertical
		assertFalse(boo.validMove(board.getPointValue(5, 4)));
		assertFalse(boo.validMove(board.getPointValue(3, 4))); // own space is not valid
	}

	/**
	 * Tests to make sure the valid moves still return true even if the moves go "off" the board.
	 */
	@Test
	public void testValidMoveOffBoard() {
		boo = new Boo(Color.WHITE, new ChessSpace(-1, 8));
		assertTrue(boo.validMove(new ChessSpace(-2, 8)));
		assertTrue(boo.validMove(new ChessSpace(0, 8)));
		assertTrue(boo.validMove(new ChessSpace(-1, 9)));
	}

	/**
	 * Make sure the method updates the ChessSpace and ChessPiece when the piece is moved.
	 */
	@Test
	public void testMoveTo() {
		boo = new Boo(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(boo);
		boo.moveTo(board.getPointValue(3, 5));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The Boo's space data was not updated correctly", boo.getSpace(), board.getPointValue(3, 5));
		assertSame("Error: The ChessSpace's piece data was not updated correctly", boo, board.getPointValue(3, 5).getPiece());
	}

	/**
	 * Make sure that when Boo captures a piece, it updates its value and piece array.
	 * Also tests if Boo can go to the its captured pieces' move locations.
	 */
	@Test
	public void testCapture() {
		boo = new Boo(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(boo);
		Queen opponentQueen = new Queen(Color.BLACK, board.getPointValue(3, 5));
		board.getPointValue(3, 5).setPiece(opponentQueen);
		boo.moveTo(board.getPointValue(3, 5));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The ChessSpace's piece data was not updated correctly", boo, board.getPointValue(3, 5).getPiece());
		assertNull(opponentQueen.getSpace());
		assertEquals(boo.getCaptured().size(), 1);
		assertTrue(boo.getCaptured().contains(opponentQueen));
		assertEquals(boo.getValue(), 14);
		assertTrue(boo.validMove(board.getPointValue(0, 5)));
	}
}
