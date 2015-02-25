package cs242.chess;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs242.chess.pieces.ChessPiece;
import cs242.chess.pieces.Pawn;
import cs242.chess.pieces.Exile;

/**
 * A class to display the ChessBoard and ChessPieces of a chess game. It also includes the mouse interface.
 * 
 * @author REN-JAY_2
 * 
 */
public class ChessDisplay {

	private static ChessPiece currentPiece;
	private static ChessSpace currentSpace;
	private static ArrayList<ChessSpace> possibleMoves; // the spaces the currentPiece can move to. These will be highlighted in green
	private static int currentPlayerIndex = 1; // index will be 0 or 1
	private static int previousPlayerIndex = 0;
	
	/**
	 * Takes care of the logistics for a computer move.
	 * Specifically, it makes the move, checks for end game conditions and updates the piece arrays.
	 * 
	 * @param player the computer player to move
	 * @param frame the frame the computer is playing on
	 * @param boardComponent the graphical representation of the chess game
	 * @return true if the game continues, false otherwise
	 */
	private static boolean computerMove(ComputerPlayer player, JFrame frame, ChessComponent boardComponent) {
		boolean canMove = player.move();
		if (!canMove) {
			boardComponent.repaint();
			if (!player.hasMoveablePiece()) {
				JOptionPane.showMessageDialog(frame, boardComponent.getPlayer(currentPlayerIndex).getName() + " cannot move. Stalemate.", "Stalemate.",
						JOptionPane.INFORMATION_MESSAGE);
			} else // computer has been checkmated
			{
				JOptionPane.showMessageDialog(frame, boardComponent.getPlayer(previousPlayerIndex).getName() + " wins!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
			}
			return false;
		}
		// the move was a success. therefore, the king is not in check
		player.getKing().setCheck(false);
		// move on to the next player
		int temp = currentPlayerIndex;
		currentPlayerIndex = previousPlayerIndex;
		previousPlayerIndex = temp;
		ChessPlayer nextPlayer = boardComponent.getPlayer(currentPlayerIndex);
		nextPlayer.updatePieceArray();
		ArrayList<CaptureSpace> dangerSpaces = boardComponent.getBoard().findCaptureMoves(boardComponent.getBoard().getOpponentPieces(nextPlayer.getKing().getColor()));
		nextPlayer.getKing().setCheck(boardComponent.getBoard().findCaptureSpace(dangerSpaces, nextPlayer.getKing().getSpace()) != null);
		boardComponent.repaint();
		return true;

	}
	
	/**
	 * Prompts the user if he or she wants a computer player, and then runs the game.
	 * 
	 * @param args string arguments
	 */
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Chess");

		final ChessBoard board = new ChessBoard();

		// First, we put all the pieces on the board.
		board.setChessBoard();
		board.addNewPieces();

		final ChessComponent boardComponent = new ChessComponent(board);
		
		class ChessMouseListener extends MouseAdapter {
			
		
			public void mousePressed(MouseEvent e) {
				if (currentSpace == null) {
					return;
				}
				currentPiece = currentSpace.getPiece();
				if (currentPiece == null) {
					return;
				}
				ChessPlayer currentPlayer = boardComponent.getPlayer(currentPlayerIndex);
				if (currentPiece.getColor() != currentPlayer.getKing().getColor()) {
					currentPiece = null;
					return;
				}
				possibleMoves = currentPlayer.getPossibleMoves(currentPiece); // the list of moves the piece can make
				for (int i = 0; i < possibleMoves.size(); i++) {
					if (possibleMoves.get(i).getPiece() != null) {
						boardComponent.addCaptureSpace(possibleMoves.get(i));
					}
					else {
						boardComponent.addMoveSpace(possibleMoves.get(i));
					}
				}
				boardComponent.repaint();
			}

			public void mouseReleased(MouseEvent e) {
				boardComponent.clearLists(); // reset the mouse over colors on the board
				boardComponent.repaint();
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
				ChessSpace targetSpace = board.getPointValue(row, col); // This is the space the current player wants to move to
				if (!possibleMoves.contains(targetSpace)) { // move not allowed
					return;
				}
				// move is legitimate
				ChessPiece targetPiece = targetSpace.getPiece();
				currentPiece.moveTo(targetSpace);
				ChessPlayer currentPlayer = boardComponent.getPlayer(currentPlayerIndex);
				// if the player was able to move, then the king is out of check
				currentPlayer.getKing().setCheck(false);
				boardComponent.repaint();
				// remove the eliminated piece if piece was taken
				if (currentPiece instanceof Exile) {
					// recalculate pieces array
					int opponentIndex = (currentPlayerIndex + 1) % boardComponent.getPlayerList().size();
					ChessPlayer otherPlayer = boardComponent.getPlayer(opponentIndex);
					otherPlayer.updatePieceArray();
				} else { // if not Exile, do this for efficiency
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
				}
				int temp = currentPlayerIndex;
				currentPlayerIndex = previousPlayerIndex;
				previousPlayerIndex = temp;
				currentPlayer = boardComponent.getPlayer(currentPlayerIndex);
				ArrayList<CaptureSpace> dangerSpaces = board.findCaptureMoves(board.getOpponentPieces(currentPlayer.getKing().getColor()));
				// set the King's check variable
				currentPlayer.getKing().setCheck(board.findCaptureSpace(dangerSpaces, currentPlayer.getKing().getSpace()) != null);
				int endGame = boardComponent.getPlayer(currentPlayerIndex).checkEndConditions();
				if (endGame == 1) {
					JOptionPane.showMessageDialog(frame, boardComponent.getPlayer(previousPlayerIndex).getName() + " wins!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (endGame == 2) {
					JOptionPane.showMessageDialog(frame, boardComponent.getPlayer(currentPlayerIndex).getName() + " cannot move. Stalemate.", "Stalemate.",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (boardComponent.getPlayer(currentPlayerIndex) instanceof ComputerPlayer) {
					computerMove((ComputerPlayer) boardComponent.getPlayer(currentPlayerIndex), frame, boardComponent);
				}
				boardComponent.repaint();
			}
			
			public void mouseMoved(MouseEvent e) {
				int y = e.getY() - 30;
				int x = e.getX() - 10;
				int row = y / boardComponent.getPointSize();
				int col = x / boardComponent.getPointSize();
				if (row < board.getLength() && col < board.getWidth()) {
					currentSpace = board.getPointValue(row, col);
				}
				else {
					currentSpace = null;
				}
				boardComponent.setMouseOverSpace(currentSpace);
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
		int gameValue = JOptionPane.showOptionDialog(null, "Choose an option", "Input", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);

		ChessPlayer whitePlayer;
		ChessPlayer blackPlayer;

		if (gameValue == 0) {
			whitePlayer = new ChessPlayer(Color.WHITE, board, "Player White");
			blackPlayer = new ChessPlayer(Color.BLACK, board, "Player Black");
		} else if (gameValue == 1) {
			whitePlayer = new ChessPlayer(Color.WHITE, board, "Player White");
			blackPlayer = new ComputerPlayer(Color.BLACK, board, "Computer Black");
		} else {
			whitePlayer = new ComputerPlayer(Color.WHITE, board, "Computer White");
			blackPlayer = new ComputerPlayer(Color.BLACK, board, "Computer Black");
		}
		boardComponent.addPlayer(blackPlayer);
		boardComponent.addPlayer(whitePlayer);
		boardComponent.repaint();
		
		if (gameValue == 2) { // computer vs computer loop
			// dont want the user to be able to interfere
			frame.removeMouseListener(chessMouseListener);
			boolean gameOver = false;
			while (!gameOver) {
				gameOver = !computerMove((ComputerPlayer) boardComponent.getPlayer(currentPlayerIndex), frame, boardComponent);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
