package cs242.chess;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs242.chess.pieces.Bishop;
import cs242.chess.pieces.ChessPiece;
import cs242.chess.pieces.King;
import cs242.chess.pieces.Knight;
import cs242.chess.pieces.Pawn;
import cs242.chess.pieces.Queen;
import cs242.chess.pieces.Rook;

public class ChessDisplay {

	private static ChessPiece currentPiece;
	private static int currentPlayerIndex = 1;

	public static void main(String[] args) {
		final JFrame frame = new JFrame("Chess");

		final ChessBoard board = new ChessBoard();

		// First, we put all the pieces on the board.
		ArrayList<ChessPiece> blackPieces = new ArrayList<ChessPiece>();
		ChessPiece blackRookLeft = new Rook(Color.BLACK, board.getPointValue(0, 0));
		board.getPointValue(0, 0).setPiece(blackRookLeft);
		blackPieces.add(blackRookLeft);
		ChessPiece blackKnightLeft = new Knight(Color.BLACK, board.getPointValue(0, 1));
		board.getPointValue(0, 1).setPiece(blackKnightLeft);
		blackPieces.add(blackKnightLeft);
		ChessPiece blackBishopLeft = new Bishop(Color.BLACK, board.getPointValue(0, 2));
		board.getPointValue(0, 2).setPiece(blackBishopLeft);
		blackPieces.add(blackBishopLeft);
		ChessPiece blackQueen = new Queen(Color.BLACK, board.getPointValue(0, 3));
		board.getPointValue(0, 3).setPiece(blackQueen);
		blackPieces.add(blackQueen);
		ChessPiece blackKing = new King(Color.BLACK, board.getPointValue(0, 4));
		board.getPointValue(0, 4).setPiece(blackKing);
		blackPieces.add(blackKing);
		ChessPiece blackBishopRight = new Bishop(Color.BLACK, board.getPointValue(0, 5));
		board.getPointValue(0, 5).setPiece(blackBishopRight);
		blackPieces.add(blackBishopRight);
		ChessPiece blackKnightRight = new Knight(Color.BLACK, board.getPointValue(0, 6));
		board.getPointValue(0, 6).setPiece(blackKnightRight);
		blackPieces.add(blackKnightRight);
		ChessPiece blackRookRight = new Rook(Color.BLACK, board.getPointValue(0, 7));
		board.getPointValue(0, 7).setPiece(blackRookRight);
		blackPieces.add(blackRookRight);

		ChessPiece blackPawn = null;
		for (int i = 0; i < board.getWidth(); i++) {
			blackPawn = new Pawn(Color.BLACK, board.getPointValue(1, i), 0);
			board.getPointValue(1, i).setPiece(blackPawn);
			blackPieces.add(blackPawn);
		}

		ChessPlayer blackPlayer = new ComputerPlayer(blackPieces, board);

		ArrayList<ChessPiece> whitePieces = new ArrayList<ChessPiece>();
		ChessPiece whiteRookLeft = new Rook(Color.WHITE, board.getPointValue(7, 0));
		board.getPointValue(7, 0).setPiece(whiteRookLeft);
		whitePieces.add(whiteRookLeft);
		ChessPiece whiteKnightLeft = new Knight(Color.WHITE, board.getPointValue(7, 1));
		board.getPointValue(7, 1).setPiece(whiteKnightLeft);
		whitePieces.add(whiteKnightLeft);
		ChessPiece whiteBishopLeft = new Bishop(Color.WHITE, board.getPointValue(7, 2));
		board.getPointValue(7, 2).setPiece(whiteBishopLeft);
		whitePieces.add(whiteBishopLeft);
		ChessPiece whiteQueen = new Queen(Color.WHITE, board.getPointValue(7, 3));
		board.getPointValue(7, 3).setPiece(whiteQueen);
		whitePieces.add(whiteQueen);
		ChessPiece whiteKing = new King(Color.WHITE, board.getPointValue(7, 4));
		board.getPointValue(7, 4).setPiece(whiteKing);
		whitePieces.add(whiteKing);
		ChessPiece whiteBishopRight = new Bishop(Color.WHITE, board.getPointValue(7, 5));
		board.getPointValue(7, 5).setPiece(whiteBishopRight);
		whitePieces.add(whiteBishopRight);
		ChessPiece whiteKnightRight = new Knight(Color.WHITE, board.getPointValue(7, 6));
		board.getPointValue(7, 6).setPiece(whiteKnightRight);
		whitePieces.add(whiteKnightRight);
		ChessPiece whiteRookRight = new Rook(Color.WHITE, board.getPointValue(7, 7));
		board.getPointValue(7, 7).setPiece(whiteRookRight);
		whitePieces.add(whiteRookRight);

		ChessPiece whitePawn = null;
		for (int i = 0; i < board.getWidth(); i++) {
			whitePawn = new Pawn(Color.WHITE, board.getPointValue(6, i), 1);
			board.getPointValue(6, i).setPiece(whitePawn);
			whitePieces.add(whitePawn);
		}

		ChessPlayer whitePlayer = new ChessPlayer(whitePieces, board);

		final ChessComponent boardComponent = new ChessComponent(board);
		boardComponent.addPlayer(blackPlayer);
		boardComponent.addPlayer(whitePlayer);

		class ChessMouseListener extends MouseAdapter {
			public void mousePressed(MouseEvent e) {
				int y = e.getY() - 30;
				int x = e.getX() - 10;
				int row = y / boardComponent.getPointSize();
				int col = x / boardComponent.getPointSize();
				if (row < board.getLength() && col < board.getWidth()) {
					currentPiece = board.getPointValue(row, col).getPiece();
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (currentPiece == null) {
					return;
				}
				int y = e.getY() - 30;
				int x = e.getX() - 10;
				int row = y / boardComponent.getPointSize();
				int col = x / boardComponent.getPointSize();
				ChessPlayer currentPlayer = boardComponent.getPlayer(currentPlayerIndex);
				ArrayList<CaptureSpace> possibleMoves = currentPlayer.getPossibleMoves();
				ChessSpace targetSpace = board.getPointValue(row, col); // This is the space the current player wants to move to
				CaptureSpace targetSpaceWithPossiblePieces = board.findCaptureSpace(possibleMoves, targetSpace);
				if (targetSpaceWithPossiblePieces == null) { // move not allowed
					boardComponent.repaint();
					return;
				}
				// does the piece being move exist in there
				if (!targetSpaceWithPossiblePieces.getPieces().contains(currentPiece)) {
					boardComponent.repaint();
					return;
				}
				// move is legitimate
				ChessPiece targetPiece = targetSpace.getPiece();
				currentPiece.moveTo(targetSpace);
				// remove the eliminated piece if piece was taken
				if (targetPiece != null) {
					for (ChessPlayer p : boardComponent.getPlayerList()) {
						if (p.getPieces().contains(targetPiece)) {
							p.removePiece(targetPiece);
						}
					}
				}
				if (currentPiece instanceof Pawn) {
					if (targetSpace.getRow() == 0 || targetSpace.getRow() == 7) {
						currentPlayer.getPieces().remove(currentPiece);
						currentPlayer.addPiece(targetSpace.getPiece());
					}
				}
				currentPlayerIndex += 1;
				if (currentPlayerIndex >= boardComponent.getPlayerList().size()) {
					currentPlayerIndex %= boardComponent.getPlayerList().size();
				}
				while (boardComponent.getPlayer(currentPlayerIndex) instanceof ComputerPlayer) {
					ComputerPlayer computerPlayer = (ComputerPlayer) boardComponent.getPlayer(currentPlayerIndex);
					boolean canMove = computerPlayer.move();
					if (!canMove) {
						boardComponent.repaint();
						if (!computerPlayer.hasMoveablePiece()) {
							JOptionPane.showMessageDialog(frame, "The opponent cannot move. Stalemate.", "Stalemate.",
									JOptionPane.INFORMATION_MESSAGE);
						} else // computer has been checkmated
						{
							JOptionPane.showMessageDialog(frame, "You win!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
						}
						return;
					}
					currentPlayerIndex += 1;
					if (currentPlayerIndex >= boardComponent.getPlayerList().size()) // The game supports more than 2 players. We
																						// want to loop back to the first player
																						// once we reach the last player.
					{
						currentPlayerIndex %= boardComponent.getPlayerList().size();
					}
					boardComponent.update();
				}
				boardComponent.repaint();
			}

			public void mouseDragged(MouseEvent e) {
				if (currentPiece == null) {
					return;
				}
				boardComponent.repaint();
			}
		}

		MouseListener chessMouseListener = new ChessMouseListener();

		frame.add(boardComponent);
		frame.addMouseListener(chessMouseListener);
		frame.addMouseMotionListener((MouseMotionListener) chessMouseListener);
		frame.setTitle("Chess");
		frame.setSize((board.getWidth() + 1) * boardComponent.getPointSize(), (board.getLength() + 1) * boardComponent.getPointSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
