package cs242.chess;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cs242.chess.pieces.Bishop;
import cs242.chess.pieces.ChessPiece;
import cs242.chess.pieces.King;
import cs242.chess.pieces.Knight;
import cs242.chess.pieces.Pawn;
import cs242.chess.pieces.Queen;
import cs242.chess.pieces.Rook;
import cs242.chess.pieces.Exile;


public class ChessDisplay {

	private static ChessPiece currentPiece;
	private static int currentPlayerIndex = 1;

	public static void main(String[] args) {
		final JFrame frame = new JFrame("Chess");

		final ChessBoard board = new ChessBoard();
		System.out.println("the board " + board);

		// First, we put all the pieces on the board.
		board.setChessBoard();
		board.addNewPieces();

		final ChessComponent boardComponent = new ChessComponent(board);

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
				if (row >= board.getLength() || row < 0 || col >= board.getWidth() || col < 0) {
					return;
				}
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
				if (currentPiece instanceof Exile) {
					// recalculate pieces array
					for (int i = currentPlayerIndex + 1; i != currentPlayerIndex; i++) {
						if (i >= boardComponent.getPlayerList().size()) {
							i %= boardComponent.getPlayerList().size();
						}
						ChessPlayer otherPlayer = boardComponent.getPlayer(i);
						otherPlayer.setPieces(board.getPieces(otherPlayer.getKing().getColor()));
					}
				} else { // if not Exile, do this for efficiency
					if (targetPiece != null) {
						for (ChessPlayer p : boardComponent.getPlayerList()) {
							if (p.getPieces().contains(targetPiece)) {
								p.removePiece(targetPiece);
							}
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
		

		Object[] possibleValues = { "Human vs Human", "Human vs Computer", "Computer vs Computer" };
		int gameValue = JOptionPane.showOptionDialog(null, "Choose an option", "Input", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
				possibleValues, possibleValues[0]);

		ChessPlayer whitePlayer;
		ChessPlayer blackPlayer;


		if (gameValue == 0) {
			whitePlayer = new ChessPlayer(Color.WHITE, board);
			blackPlayer = new ChessPlayer(Color.BLACK, board);
		}
		else if (gameValue == 1) {
			whitePlayer = new ChessPlayer(Color.WHITE, board);
			blackPlayer = new ComputerPlayer(Color.BLACK, board);
		}
		else {
			whitePlayer = new ComputerPlayer(Color.WHITE, board);
			blackPlayer = new ComputerPlayer(Color.BLACK, board);
		}
		boardComponent.addPlayer(blackPlayer);
		boardComponent.addPlayer(whitePlayer);
		boardComponent.repaint();
	}
}
