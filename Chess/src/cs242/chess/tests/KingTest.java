package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import cs242.chess.ChessBoard;
import cs242.chess.ChessSpace;
import cs242.chess.pieces.King;

/**
 * A class to test the functionality of the King class
 * 
 * @author REN-JAY_2
 * 
 */
public class KingTest {

	private static ChessBoard board;
	private static King king;
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
	 * Resets the ChessBoard and sets the king back to null
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		board.clear();
		king = null;
	}

	/**
	 * Tests to make sure all the fields are properly instantiated, including row, column, ChessSpace, value and check
	 */
	@Test
	public void testInstantiate() {
		king = new King(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(king);
		assertEquals(king.getSpace().getRow(), 3, DELTA);
		assertEquals(king.getSpace().getCol(), 4, DELTA);
		assertSame("Error: Pieces are not the same", king, king.getSpace().getPiece());
		assertEquals(king.getValue(), 99);
	}

	/**
	 * Tests to make sure that an out of bounds exception is thrown when instantiating with an out of bounds point
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testBadInstantiate() {
		king = new King(Color.WHITE, board.getPointValue(-1, 8));
		board.getPointValue(-1, 8).setPiece(king);
	}

	/**
	 * Tests to make sure the method returns true on valid moves and false on invalid moves
	 */
	@Test
	public void testValidMove() {
		king = new King(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(king);
		assertTrue(king.validMove(board.getPointValue(2, 3)));
		assertFalse(king.validMove(board.getPointValue(3, 4))); // own space is not valid
		assertFalse(king.validMove(board.getPointValue(7, 7)));
	}

	/**
	 * Tests to make sure the valid moves still return true even if the moves go "off" the board
	 */
	@Test
	public void testValidMoveOffBoard() {
		king = new King(Color.WHITE, new ChessSpace(0, 6));
		assertTrue(king.validMove(new ChessSpace(0, 7)));
		assertFalse(king.validMove(new ChessSpace(-1, 4)));
		assertTrue(king.validMove(new ChessSpace(-1, 5)));
	}

	/**
	 * Make sure the method updates the ChessSpace and ChessPiece when the piece is moved
	 */
	@Test
	public void testMoveTo() {
		king = new King(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(king);
		king.moveTo(board.getPointValue(4, 4));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The King's space data was not updated correctly", king.getSpace(), board.getPointValue(4, 4));
		assertSame("Error: The ChessSpace's piece data was not updated correctly", king, board.getPointValue(4, 4).getPiece());
	}

	/**
	 * Make sure the method updates the ChessSpace and both ChessPieces when a piece captures another piece
	 */
	@Test
	public void testCapture() {
		king = new King(Color.WHITE, board.getPointValue(3, 4));
		board.getPointValue(3, 4).setPiece(king);
		King opponentKing = new King(Color.BLACK, board.getPointValue(2, 3));
		board.getPointValue(2, 3).setPiece(opponentKing);
		king.moveTo(board.getPointValue(2, 3));
		assertNull(board.getPointValue(3, 4).getPiece());
		assertSame("Error: The ChessSpace's piece data was not updated correctly", king, board.getPointValue(2, 3).getPiece());
		assertNull(opponentKing.getSpace());
	}
}
