package cs242.chess.tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cs242.chess.CaptureSpace;
import cs242.chess.ChessBoard;
import cs242.chess.ChessSpace;
import cs242.chess.pieces.Bishop;
import cs242.chess.pieces.ChessPiece;
import cs242.chess.pieces.King;
import cs242.chess.pieces.Knight;
import cs242.chess.pieces.Pawn;
import cs242.chess.pieces.Queen;
import cs242.chess.pieces.Rook;

/**
 * A class to test the functionality of the ChessBoard class.
 * 
 * @author REN-JAY_2
 * 
 */
public class ChessBoardTest {

	private static ChessBoard board;

	/**
	 * Initializes the ChessBoard before every test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		board = new ChessBoard();
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
	 * Tests to make sure all fields are properly initialized, including row, column and default ChessSpace values.
	 */
	@Test
	public void testInitialize() {
		board = new ChessBoard(7, 13);
		assertEquals(board.getLength(), 7);
		assertEquals(board.getWidth(), 13);
		assertEquals(board.getPointValue(5, 6).getRow(), 5);
		assertEquals(board.getPointValue(5, 6).getCol(), 6);
		assertNull(board.getPointValue(5, 6).getPiece());
	}

	/**
	 * Checks if the ChessBoard produced by copy() has the same values as the original board and that it performs a deep copy.
	 */
	@Test
	public void testCopy() {
		Rook piece = new Rook(Color.BLACK, board.getPointValue(5, 6));
		board.getPointValue(5, 6).setPiece(piece);
		ChessBoard copyBoard = (ChessBoard) board.copy();
		assertNotSame(board, copyBoard);
		assertNotSame(board.getPointValue(5, 6), copyBoard.getPointValue(5, 6));
		assertNotSame(board.getPointValue(5, 6).getPiece(), copyBoard.getPointValue(5, 6).getPiece());
		assertEquals(board.getLength(), copyBoard.getLength());
		assertEquals(board.getWidth(), copyBoard.getWidth());
		assertEquals(board.getPointValue(5, 6).getRow(), copyBoard.getPointValue(5, 6).getRow());
		assertEquals(board.getPointValue(5, 6).getCol(), copyBoard.getPointValue(5, 6).getCol());
		assertEquals(board.getPointValue(5, 6).getPiece().getValue(), copyBoard.getPointValue(5, 6).getPiece().getValue());
	}

	/**
	 * Checks if the ChessBoard produced by clear() has the appropriate row and column indices, and a null ChessPiece object.
	 */
	@Test
	public void testClear() {
		Rook piece = new Rook(Color.BLACK, board.getPointValue(5, 6));
		board.getPointValue(5, 6).setPiece(piece);
		board.clear();
		assertNull(board.getPointValue(5, 6).getPiece());
		assertEquals(board.getPointValue(5, 6).getRow(), 5);
		assertEquals(board.getPointValue(5, 6).getCol(), 6);
	}

	/**
	 * Tests if the function returns true if there is a clear path and false otherwise. Note that the function assumes that there is a valid
	 * path if there were no other pieces.
	 */
	@Test
	public void testClearPath() {
		board.setChessBoard();
		Queen whiteQueen = (Queen) board.getPointValue(7, 3).getPiece();
		whiteQueen.moveTo(board.getPointValue(4, 3)); // teleport the Queen to the middle of the board to aid in testing

		assertTrue(board.hasClearPath(whiteQueen, board.getPointValue(2, 5))); // empty space (diagonal line to the above right of Queen)
		assertTrue(board.hasClearPath(whiteQueen, board.getPointValue(1, 6))); // capture space (diagonal line to the above right of Queen)
		assertFalse(board.hasClearPath(whiteQueen, board.getPointValue(0, 7))); // cannot capture space behind pawn (diagonal line to the
																				// above right of Queen)

		assertTrue(board.hasClearPath(whiteQueen, board.getPointValue(5, 2))); // empty space (diagonal line to the below left of Queen)
		assertTrue(board.hasClearPath(whiteQueen, board.getPointValue(6, 1))); // capture space (diagonal line to the below left of Queen)
		assertFalse(board.hasClearPath(whiteQueen, board.getPointValue(7, 0))); // cannot capture space behind pawn (diagonal line to the
																				// below left of Queen)

		assertTrue(board.hasClearPath(whiteQueen, board.getPointValue(6, 3))); // empty space (vertical line in front of Queen)
		assertTrue(board.hasClearPath(whiteQueen, board.getPointValue(6, 3))); // capture space (vertical line in front of Queen)
		assertFalse(board.hasClearPath(whiteQueen, board.getPointValue(7, 3))); // cannot capture space behind pawn (vertical line in front
																				// of Queen)
		Pawn whitePawn = (Pawn) board.getPointValue(6, 1).getPiece();
		whitePawn.moveTo(board.getPointValue(4, 1));
		assertTrue(board.hasClearPath(whiteQueen, board.getPointValue(4, 2))); // empty space (horizontal line to the left of Queen)
		assertTrue(board.hasClearPath(whiteQueen, board.getPointValue(4, 1))); // capture space (horizontal line to the left of Queen)
		assertFalse(board.hasClearPath(whiteQueen, board.getPointValue(4, 0))); // cannot capture space behind pawn (horizontal line to the
																				// left of Queen)

		Knight whiteKnight = (Knight) board.getPointValue(7, 1).getPiece(); // test for knight
		assertTrue(board.hasClearPath(whiteKnight, board.getPointValue(5, 0)));
	}

	/**
	 * Tests if the ArrayList the function returns contains the opponent pieces and no other pieces.
	 */
	@Test
	public void testGetPieces() {
		board.setChessBoard();
		ArrayList<ChessPiece> whitePieces = board.getPieces(Color.WHITE);
		assertFalse(whitePieces.contains(board.getPointValue(0, 0).getPiece()));
		assertFalse(whitePieces.contains(board.getPointValue(1, 6).getPiece()));
		assertFalse(whitePieces.contains(board.getPointValue(4, 4).getPiece()));
		assertTrue(whitePieces.contains(board.getPointValue(6, 2).getPiece()));
	}

	/**
	 * Tests if the ArrayList the function returns contains the opponent pieces and no other pieces.
	 */
	@Test
	public void testGetOpponentPieces() {
		board.setChessBoard();
		ArrayList<ChessPiece> blackPieces = board.getOpponentPieces(Color.WHITE);
		assertTrue(blackPieces.contains(board.getPointValue(0, 0).getPiece()));
		assertTrue(blackPieces.contains(board.getPointValue(1, 6).getPiece()));
		assertFalse(blackPieces.contains(board.getPointValue(4, 4).getPiece()));
		assertFalse(blackPieces.contains(board.getPointValue(6, 2).getPiece()));
	}

	/**
	 * Tests if the ArrayList the function returns contains the piece's allowable moves and no others. The method tests for both all moves
	 * and capture moves only.
	 */
	@Test
	public void testFindPossibleMovesSingle() {
		board.setChessBoard();
		Queen whiteQueen = (Queen) board.getPointValue(7, 3).getPiece();
		whiteQueen.moveTo(board.getPointValue(4, 3)); // teleport the Queen to the middle of the board to aid in testing
		ArrayList<ChessSpace> possibleMoves = board.findPossibleMoves(whiteQueen, false);
		ArrayList<ChessSpace> possibleMovesCaptureOnly = board.findPossibleMoves(whiteQueen, true);

		assertTrue(possibleMoves.contains(board.getPointValue(2, 3))); // empty space in front
		assertFalse(possibleMovesCaptureOnly.contains(board.getPointValue(2, 3)));

		assertTrue(possibleMoves.contains(board.getPointValue(1, 0))); // capture space diagonal left
		assertTrue(possibleMovesCaptureOnly.contains(board.getPointValue(1, 0)));

		assertFalse(possibleMoves.contains(board.getPointValue(2, 0))); // invalid move
		assertFalse(possibleMovesCaptureOnly.contains(board.getPointValue(2, 0)));

		assertFalse(possibleMoves.contains(board.getPointValue(6, 5))); // cannot capture own piece
		assertFalse(possibleMovesCaptureOnly.contains(board.getPointValue(6, 5)));

		assertFalse(possibleMoves.contains(board.getPointValue(0, 7))); // capture space behind piece
		assertFalse(possibleMovesCaptureOnly.contains(board.getPointValue(0, 7)));

		// Move Pawn. The above two should be true now
		Pawn blackPawn = (Pawn) board.getPointValue(1, 6).getPiece();
		blackPawn.moveTo(board.getPointValue(3, 6));
		possibleMoves = board.findPossibleMoves(whiteQueen, false);
		possibleMovesCaptureOnly = board.findPossibleMoves(whiteQueen, true);
		assertTrue(possibleMoves.contains(board.getPointValue(0, 7))); // capture space behind piece
		assertTrue(possibleMovesCaptureOnly.contains(board.getPointValue(0, 7)));
	}

	/**
	 * Tests if the ArrayList the function returns contains all possible moves that the ArrayList of pieces can do. Also tests the data
	 * inside the CaptureSpaces that are contained in the ArrayList.
	 */
	@Test
	public void testFindPossibleMovesArray() {
		board.setChessBoard();
		ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
		Queen whiteQueen = (Queen) board.getPointValue(7, 3).getPiece();
		whiteQueen.moveTo(board.getPointValue(4, 3)); // teleport the Queen to the middle of the board to aid in testing
		pieces.add(whiteQueen);
		Knight whiteKnight = (Knight) board.getPointValue(7, 6).getPiece();
		whiteKnight.moveTo(board.getPointValue(3, 5));
		pieces.add(whiteKnight);
		ArrayList<CaptureSpace> possibleMoves = board.findPossibleMoves(pieces);
		// Check spaces only Queen can take
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(2, 3))); // empty space in front
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 3))); // capture pawn in front
		// Check spaces only knight can take
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 6))); // empty space to the right
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 4))); // capture pawn in front
		// Check spaces both can go to
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 4)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 6)));
		// Make sure own space is not included
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(4, 3)));
		// Make sure CaptureSpace contains array of pieces that can capture it
		CaptureSpace cs = board.findCaptureSpace(possibleMoves, board.getPointValue(1, 6));
		assertTrue(cs.getPieces().contains(whiteQueen));
		assertTrue(cs.getPieces().contains(whiteKnight));
		assertEquals(cs.getPieces().size(), 2);
		// Check spaces that neither can capture
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(3, 1)));
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 3)));

		// Add black piece and make sure its spaces are included
		pieces.add(board.getPointValue(1, 3).getPiece()); // add black Pawn
		possibleMoves = board.findPossibleMoves(pieces);
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(2, 2)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(2, 3)));
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(2, 4)));
	}

	/**
	 * Tests if the ArrayList the function returns contains all possible moves that the ArrayList of pieces can do. This should ignore the
	 * color of any other piece.
	 */
	@Test
	public void testFindPossibleMovesIgnoreColor() {
		board.setChessBoard();
		ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
		Queen whiteQueen = (Queen) board.getPointValue(7, 3).getPiece();
		whiteQueen.moveTo(board.getPointValue(4, 3)); // teleport the Queen to the middle of the board to aid in testing
		pieces.add(whiteQueen);
		Knight whiteKnight = (Knight) board.getPointValue(7, 6).getPiece();
		whiteKnight.moveTo(board.getPointValue(3, 5));
		pieces.add(whiteKnight);
		ArrayList<CaptureSpace> possibleMoves = board.findPossibleMovesIgnoreColor(pieces);
		// Check spaces only Queen can take
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(2, 3))); // empty space in front
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 3))); // capture pawn in front
		// Check spaces only knight can take
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 6))); // empty space to the right
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 4))); // capture pawn in front
		// Check spaces both can go to
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 4)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 6)));
		// Knight can capture Queen but Queen can't capture Knight
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(4, 3)));
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(3, 5)));
		// Make sure CaptureSpace contains array of pieces that can capture it, but only those that are provided in the parameter ArrayList
		CaptureSpace cs = board.findCaptureSpace(possibleMoves, board.getPointValue(1, 6));
		assertTrue(cs.getPieces().contains(whiteQueen));
		assertTrue(cs.getPieces().contains(whiteKnight));
		assertEquals(cs.getPieces().size(), 2);
		// Check spaces that neither can capture
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(3, 1)));
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 3)));

		// Add black piece and make sure its spaces are included
		pieces.add(board.getPointValue(0, 3).getPiece()); // add black Queen
		possibleMoves = board.findPossibleMovesIgnoreColor(pieces);
		// Make sure own color piece captures are included
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 2)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 4)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 2)));
	}

	/**
	 * Tests if the ArrayList the function returns contains all possible moves where the ArrayList of pieces can capture. Make sure non
	 * capture moves are not included.
	 */
	@Test
	public void testFindCaptureMoves() {
		board.setChessBoard();
		ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
		Queen whiteQueen = (Queen) board.getPointValue(7, 3).getPiece();
		whiteQueen.moveTo(board.getPointValue(4, 3)); // teleport the Queen to the middle of the board to aid in testing
		pieces.add(whiteQueen);
		Knight whiteKnight = (Knight) board.getPointValue(7, 6).getPiece();
		whiteKnight.moveTo(board.getPointValue(3, 5));
		pieces.add(whiteKnight);
		ArrayList<CaptureSpace> possibleMoves = board.findCaptureMoves(pieces);
		// Check spaces only Queen can take
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(2, 3))); // empty space in front
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 3))); // capture pawn in front
		// Check spaces only knight can take
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 6))); // empty space to the right
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 4))); // capture pawn in front
		// Check spaces both can go to
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 4)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 6))); // capture move
		// Make sure own space is not included
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(4, 3)));
		// Make sure CaptureSpace contains array of pieces that can capture it, but only those that are provided in the parameter ArrayList
		CaptureSpace cs = board.findCaptureSpace(possibleMoves, board.getPointValue(1, 6));
		assertTrue(cs.getPieces().contains(whiteQueen));
		assertTrue(cs.getPieces().contains(whiteKnight));
		assertEquals(cs.getPieces().size(), 2);
		// Check spaces that neither can capture
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(3, 1)));
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 3)));

		// Add black piece and make sure its spaces are not included
		pieces.add(board.getPointValue(0, 3).getPiece()); // add black Queen
		possibleMoves = board.findCaptureMoves(pieces);
		// Make sure own color piece captures are not included
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 2)));
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 4)));
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 2)));
	}

	/**
	 * Tests if the ArrayList the function returns contains all possible moves where the ArrayList of pieces can capture, regardless of
	 * color. Make sure non capture moves are not included.
	 */
	@Test
	public void testFindCaptureMovesIgnoreColor() {
		board.setChessBoard();
		ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
		Queen whiteQueen = (Queen) board.getPointValue(7, 3).getPiece();
		whiteQueen.moveTo(board.getPointValue(4, 3)); // teleport the Queen to the middle of the board to aid in testing
		pieces.add(whiteQueen);
		Knight whiteKnight = (Knight) board.getPointValue(7, 6).getPiece();
		whiteKnight.moveTo(board.getPointValue(3, 5));
		pieces.add(whiteKnight);
		ArrayList<CaptureSpace> possibleMoves = board.findCaptureMovesIgnoreColor(pieces);
		// Check spaces only Queen can take
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(2, 3))); // empty space in front
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 3))); // capture pawn in front
		// Check spaces only knight can take
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 6))); // empty space to the right
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 4))); // capture pawn in front
		// Check spaces both can go to
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(5, 4)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 6))); // capture move
		// Knight can capture Queen but Queen can't capture Knight
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(4, 3)));
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(3, 5)));
		// Check spaces that neither can capture
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(3, 1)));
		assertNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 3)));
		// Add black bishop and make sure black pawn capture space includes it
		pieces.add(board.getPointValue(0, 5).getPiece());
		possibleMoves = board.findCaptureMovesIgnoreColor(pieces);
		CaptureSpace cs = board.findCaptureSpace(possibleMoves, board.getPointValue(1, 6));
		assertTrue(cs.getPieces().contains(whiteQueen));
		assertTrue(cs.getPieces().contains(whiteKnight));
		assertTrue(cs.getPieces().contains(board.getPointValue(0, 5).getPiece()));
		assertEquals(cs.getPieces().size(), 3);

		// Add black piece and make sure its spaces are included
		pieces.add(board.getPointValue(0, 3).getPiece()); // add black Queen
		possibleMoves = board.findCaptureMovesIgnoreColor(pieces);
		// Make sure own color piece captures are included
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 2)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(0, 4)));
		assertNotNull(board.findCaptureSpace(possibleMoves, board.getPointValue(1, 2)));
	}
}
