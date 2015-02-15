package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import cs242.chess.ChessBoard;
import cs242.chess.ChessSpace;
import cs242.chess.pieces.Pawn;

/**
 * A class to test the functionality of the Pawn class
 * 
 * @author REN-JAY_2
 * 
 */
public class PawnTest {

	private static ChessBoard board;
	private static Pawn pawn;
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
	 * Resets the ChessBoard and sets the pawn back to null
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		board.clear();
		pawn = null;
	}

	/**
	 * Tests to make sure all the fields are properly instantiated, including row, column, ChessSpace and value
	 */
	@Test
	public void testInstantiate() {
		pawn = new Pawn(Color.WHITE, board.getPointValue(3, 4), 0);
		board.getPointValue(3, 4).setPiece(pawn);
		assertEquals(pawn.getSpace().getRow(), 3, DELTA);
		assertEquals(pawn.getSpace().getCol(), 4, DELTA);
		assertSame("Error: Pieces are not the same", pawn, pawn.getSpace().getPiece());
		assertEquals(pawn.getValue(), 1);
	}

	/**
	 * Tests to make sure that an out of bounds exception is thrown when instantiating with an out of bounds point
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testBadInstantiate() {
		pawn = new Pawn(Color.WHITE, board.getPointValue(-1, 8), 0);
		board.getPointValue(-1, 8).setPiece(pawn);
	}

	/**
	 * Tests to make sure the method returns true on valid moves and false on invalid moves
	 */
	@Test
	public void testValidMove() {
		pawn = new Pawn(Color.WHITE, board.getPointValue(1, 4), 0);
		board.getPointValue(1, 4).setPiece(pawn);
		assertTrue(pawn.validMove(board.getPointValue(2, 4))); // one space down
		assertTrue(pawn.validMove(board.getPointValue(3, 4))); // two spaces down
		assertFalse(pawn.validMove(board.getPointValue(2, 3))); // diagonal down left
		assertFalse(pawn.validMove(board.getPointValue(2, 5))); // diagonal down right
		assertFalse(pawn.validMove(board.getPointValue(0, 4))); // backwards
		board.setPointValue(1, 4, null);
		pawn = new Pawn(Color.BLACK, board.getPointValue(6, 3), 1); // going up
		board.getPointValue(6, 3).setPiece(pawn);
		assertTrue(pawn.validMove(board.getPointValue(5, 3))); // one space
		assertTrue(pawn.validMove(board.getPointValue(4, 3))); // two spaces
		assertFalse(pawn.validMove(board.getPointValue(5, 2))); // diagonal up
		assertFalse(pawn.validMove(board.getPointValue(5, 4)));
		assertFalse(pawn.validMove(board.getPointValue(7, 3))); // backwards
		Pawn opponentPawn = new Pawn(Color.BLACK, board.getPointValue(5, 2), 0); // test capturing opponent pawn
		board.getPointValue(5, 2).setPiece(opponentPawn);
		assertTrue(pawn.validMove(board.getPointValue(5, 2)));
		assertFalse(pawn.validMove(board.getPointValue(6, 3))); // own space is not valid
	}

	/**
	 * Tests to make sure the valid moves still return true even if the moves go "off" the board
	 */
	@Test
	public void testValidMoveOffBoard() {
		pawn = new Pawn(Color.WHITE, new ChessSpace(8, 5), 0);
		assertTrue(pawn.validMove(new ChessSpace(9, 5)));
		assertFalse(pawn.validMove(new ChessSpace(10, 5)));
		assertFalse(pawn.validMove(new ChessSpace(9, 4)));
	}

	/**
	 * Make sure the method updates the ChessSpace and ChessPiece when the piece is moved. Test to see if popup dialogue appears and Pawn
	 * promotes when Pawn reaches the end of the board.
	 */
	@Test
	public void testMoveTo() {
		pawn = new Pawn(Color.WHITE, board.getPointValue(6, 4), 0);
		board.getPointValue(6, 4).setPiece(pawn);
		pawn.moveTo(board.getPointValue(7, 4));
		assertNull(board.getPointValue(6, 4).getPiece());
		assertSame("Error: The Pawn's space data was not updated correctly", pawn.getSpace(), board.getPointValue(7, 4));
		assertNotSame("Error: The ChessSpace's piece data was not updated correctly", pawn, board.getPointValue(7, 4).getPiece());
	}

	/**
	 * Make sure the method updates the ChessSpace and both ChessPieces when a piece captures another piece
	 */
	@Test
	public void testCapture() {
		pawn = new Pawn(Color.WHITE, board.getPointValue(1, 4), 0);
		board.getPointValue(1, 4).setPiece(pawn);
		Pawn opponentPawn = new Pawn(Color.BLACK, board.getPointValue(2, 3), 1);
		board.getPointValue(2, 3).setPiece(opponentPawn);
		pawn.moveTo(board.getPointValue(2, 3));
		assertNull(board.getPointValue(1, 4).getPiece());
		assertSame("Error: The ChessSpace's piece data was not updated correctly", pawn, board.getPointValue(2, 3).getPiece());
		assertNull(opponentPawn.getSpace());
	}
}
