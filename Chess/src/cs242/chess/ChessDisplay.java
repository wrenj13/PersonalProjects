package cs242.chess;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
	private static int playerOneScore = 0;
	private static int playerTwoScore = 0;
	private static JLabel playerOneText; // the score for the player
	private static JLabel playerTwoText;
	private static int restartCount = 0;
	private static CommandManager commandManager;
	
	/**
	 * A method that swaps the current player index with the previous one.
	 * It assumes there are only two relevant indices.
	 */
	private static void swapPlayerIndices() {
		int temp = currentPlayerIndex;
		currentPlayerIndex = previousPlayerIndex;
		previousPlayerIndex = temp;
	}
	
	/**
	 * A method that resets the board, conserving the player scores and computer status.
	 */
	private static void resetBoard(ChessComponent boardComponent) {
		ChessBoard board = boardComponent.getBoard();
		board.clear();
		board.setChessBoard();
		board.addNewPieces();
		ChessPlayer player = boardComponent.getPlayer(currentPlayerIndex);
		ChessPlayer otherPlayer = boardComponent.getPlayer(previousPlayerIndex);
		boardComponent.clearPlayerList();
		if (player instanceof ComputerPlayer) {
			player = new ComputerPlayer(player.getKing().getColor(), board, "Computer " + player.getKing().getColor());
		} else {
			player = new ChessPlayer(player.getKing().getColor(), board, "Player " + player.getKing().getColor());
		}
		if (otherPlayer instanceof ComputerPlayer) {
			otherPlayer = new ComputerPlayer(otherPlayer.getKing().getColor(), board, "Computer " + otherPlayer.getKing().getColor());
		} else {
			otherPlayer = new ChessPlayer(otherPlayer.getKing().getColor(), board, "Player " + otherPlayer.getKing().getColor());
		}
		if (player.getKing().getColor().equals(Color.BLACK)) {
			boardComponent.addPlayer(player);
			boardComponent.addPlayer(otherPlayer);
		} else {
			boardComponent.addPlayer(otherPlayer);
			boardComponent.addPlayer(player);
		}
		currentPlayerIndex = 1;
		previousPlayerIndex = 0;
		boardComponent.repaint();
	}

	/**
	 * Takes care of the logistics for a computer move. Specifically, it makes the move, checks for end game conditions and updates the
	 * piece arrays.
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
				JOptionPane.showMessageDialog(frame, boardComponent.getPlayer(currentPlayerIndex).getName() + " cannot move. Stalemate.",
						"Stalemate.", JOptionPane.INFORMATION_MESSAGE);
			} else // computer has been checkmated
			{
				JOptionPane.showMessageDialog(frame, boardComponent.getPlayer(previousPlayerIndex).getName() + " wins!",
						"Congratulations!", JOptionPane.INFORMATION_MESSAGE);
			}
			return false;
		}
		// the move was a success. therefore, the king is not in check
		player.getKing().setCheck(false);
		// move on to the next player
		swapPlayerIndices();
		ChessPlayer nextPlayer = boardComponent.getPlayer(currentPlayerIndex);
		nextPlayer.updatePieceArray();
		ArrayList<CaptureSpace> dangerSpaces = boardComponent.getBoard().findCaptureMoves(
				boardComponent.getBoard().getOpponentPieces(nextPlayer.getKing().getColor()));
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
		

		/**
		 * A class that represents a Chess move.
		 * It stores information about which piece was moved and which piece was captured.
		 * 
		 * @author REN-JAY_2
		 *
		 */
		class ChessMoveCommand implements Command {

			ArrayList<ChessPiece> capturedPieces = new ArrayList<ChessPiece>();
			ChessBoard board;
			ChessPiece piece;
			ChessPiece pieceCopy; // copy the piece in case it has special internal variables
			ChessSpace targetSpace;
			ChessPlayer player;
			ChessPlayer otherPlayer;
			int originalRestartCount;
			boolean currentCheck;
			boolean opponentCheck;

			/**
			 * A constructor that sets the command variables.
			 * 
			 * @param newBoard the board the command is on
			 * @param newPiece the piece that is being moved
			 * @param newTarget the target space the piece is being moved to
			 * @param playerIssuingCommand the player that is moving the piece
			 * @param opponent the other player
			 */
			private ChessMoveCommand(ChessBoard newBoard, ChessPiece newPiece, ChessSpace newTarget, ChessPlayer playerIssuingCommand, ChessPlayer opponent) {
				board = newBoard;
				piece = newPiece;
				pieceCopy = piece.copy();
				pieceCopy.setSpace(piece.getSpace());
				pieceCopy.setImageIcon(piece.getImageIcon());
				targetSpace = newTarget;
				player = playerIssuingCommand;
				originalRestartCount = restartCount;
				currentCheck = player.getKing().getCheck();
				otherPlayer = opponent;
				opponentCheck = otherPlayer.getKing().getCheck();
			}

			/**
			 * Moves a piece to a target location
			 */
			public void execute() {
				ChessPiece targetPiece = targetSpace.getPiece();
				piece.moveTo(targetSpace);
				if (targetPiece != null) {
					targetPiece.setSpace(targetSpace); // make the removed piece remember its old space so that undo is possible
				}
				// if the player was able to move, then the king is out of check
				player.getKing().setCheck(false);
				// if the player moved, the restart counter is reset
				restartCount = 0;
				// remove the eliminated piece if piece was taken
				if (currentPiece instanceof Exile) {
					// iterate through old piece array and check which pieces are still on the board
					// the pieces that are not are added to the captured list and removed from the other player's pieces
					ArrayList<ChessPiece> oldPieceArray = otherPlayer.getPieces();
					ArrayList<ChessPiece> remainingPieces = board.getPieces(otherPlayer.getKing().getColor());
					for (int pieceIndex = oldPieceArray.size() - 1; pieceIndex >=0; pieceIndex--) {
						if (!remainingPieces.contains(oldPieceArray.get(pieceIndex))) {
							capturedPieces.add(oldPieceArray.get(pieceIndex));
							otherPlayer.getPieces().remove(pieceIndex);
						}
					}
				} else { // if not Exile, do this for efficiency
					if (targetPiece != null) {
						otherPlayer.removePiece(targetPiece);
						capturedPieces.add(targetPiece);
					}
					if (currentPiece instanceof Pawn) {
						if (targetSpace.getRow() == 0 || targetSpace.getRow() == 7) {
							player.removePiece(currentPiece);
							player.addPiece(targetSpace.getPiece());
							piece = targetSpace.getPiece();
						}
					}
				}
				ArrayList<CaptureSpace> dangerSpaces = board.findCaptureMoves(board.getOpponentPieces(otherPlayer.getKing().getColor()));
				// set the King's check variable
				otherPlayer.getKing().setCheck(board.findCaptureSpace(dangerSpaces, otherPlayer.getKing().getSpace()) != null);
				swapPlayerIndices();
			}

			/**
			 * Undoes the move and adds pieces back to the player array.
			 * Moves the original piece back to its original space.
			 */
			public void undo() {
				// add a copy of the piece back to the board
				pieceCopy.getSpace().setPiece(pieceCopy);
				player.removePiece(piece);
				piece.getSpace().setPiece(null);
				player.addPiece(pieceCopy);
				// make the board remember the removed pieces
				// add pieces back to their owner's array
				for (int pieceIndex = 0; pieceIndex < capturedPieces.size(); pieceIndex++) {
					capturedPieces.get(pieceIndex).getSpace().setPiece(capturedPieces.get(pieceIndex));
					otherPlayer.addPiece(capturedPieces.get(pieceIndex));
				}
				player.getKing().setCheck(currentCheck);
				otherPlayer.getKing().setCheck(opponentCheck);
				restartCount = originalRestartCount;
				swapPlayerIndices();
			}
		}

		/**
		 * A class to provide mouse input. When the mouse is clicked and released, the pieces are moved according to the final and ending
		 * positions. It also draws colors on the board according to possible moves and check.
		 * 
		 * @author REN-JAY_2
		 * 
		 */
		class ChessMouseListener extends MouseAdapter {

			/**
			 * Sets the current piece and checks for the possible moves it can make.
			 * This allows the possible move spaces to be highlighted.
			 */
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
					} else {
						boardComponent.addMoveSpace(possibleMoves.get(i));
					}
				}
				boardComponent.repaint();
			}

			/**
			 * Allows the player to make a move.
			 */
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
				commandManager.executeCommand(new ChessMoveCommand(board, currentPiece, targetSpace, boardComponent.getPlayer(currentPlayerIndex), boardComponent.getPlayer(previousPlayerIndex)));
				int endGame = boardComponent.getPlayer(currentPlayerIndex).checkEndConditions();
				if (endGame == 1) {
					JOptionPane.showMessageDialog(frame, boardComponent.getPlayer(previousPlayerIndex).getName() + " wins!",
							"Congratulations!", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (endGame == 2) {
					JOptionPane.showMessageDialog(frame, boardComponent.getPlayer(currentPlayerIndex).getName()
							+ " cannot move. Stalemate.", "Stalemate.", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (boardComponent.getPlayer(currentPlayerIndex) instanceof ComputerPlayer) {
					computerMove((ComputerPlayer) boardComponent.getPlayer(currentPlayerIndex), frame, boardComponent);
				}
				boardComponent.repaint();
			}

			/**
			 * Updates the current space based on where the mouse is.
			 * This allows the board component to color the space.
			 */
			public void mouseMoved(MouseEvent e) {
				int y = e.getY() - 30;
				int x = e.getX() - 10;
				int row = y / boardComponent.getPointSize();
				int col = x / boardComponent.getPointSize();
				if (row < board.getLength() && col < board.getWidth()) {
					currentSpace = board.getPointValue(row, col);
				} else {
					currentSpace = null;
				}
				boardComponent.setMouseOverSpace(currentSpace);
				boardComponent.repaint();
			}
		}

		/**
		 * A class to provide input from a button. When clicked, the button allows the player to forfeit the game. This will give a point to
		 * the other player. This method resets the board and the pieces on it in preparation for another game.
		 */
		class ForfeitListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				if (currentPlayerIndex == 0) {
					playerTwoScore++;
					playerTwoText.setText("  Score: " + playerTwoScore);
				} else {
					playerOneScore++;
					playerOneText.setText("  Score: " + playerOneScore);
				}
				resetBoard(boardComponent);
			}
		}

		/**
		 * A class to allow the game to be restarted if both players agree. If the players press the button for two consecutive turns, the
		 * game is restarted.
		 */
		class RestartListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				if (restartCount == 0) {
					restartCount++;
					int temp = currentPlayerIndex;
					currentPlayerIndex = previousPlayerIndex;
					previousPlayerIndex = temp;
				} else if (restartCount == 1) {
					restartCount = 0;
					resetBoard(boardComponent);
				}
			}
		}

		/**
		 * A class that allows a ChessPlayer to set their name.
		 * 
		 * @author REN-JAY_2
		 *
		 */
		class TextListener implements ActionListener {

			private JTextField textfield;
			private ChessPlayer player;

			public TextListener(JTextField newTextfield, ChessPlayer newPlayer) {
				textfield = newTextfield;
				player = newPlayer;
			}

			public void actionPerformed(ActionEvent event) {
				String eventText = textfield.getText();
				player.setName(eventText);
				textfield.selectAll();
			}
		}
		
		/**
		 * A class that allows the player to undo their last move.
		 * 
		 * @author REN-JAY_2
		 *
		 */
		class UndoListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				commandManager.undo();
			}
		}

		// Creates the Forfeit button and sets its dimensions.
		JButton forfeitButton = new JButton("Forfeit");
		forfeitButton.setBounds(0, boardComponent.getPointSize() * board.getWidth(), boardComponent.getPointSize() * board.getWidth() / 3,
				50);
		ActionListener forfeitListener = new ForfeitListener();
		forfeitButton.addActionListener(forfeitListener);

		JButton restartButton = new JButton("Restart");
		restartButton.setBounds(forfeitButton.getWidth(), boardComponent.getPointSize() * board.getWidth(), boardComponent.getPointSize()
				* board.getWidth() / 3, 50);
		ActionListener restartListener = new RestartListener();
		restartButton.addActionListener(restartListener);

		MouseListener chessMouseListener = new ChessMouseListener();

		// Making a menu.
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Options");
		menuBar.add(menu);
		JMenuItem undoOption = new JMenuItem("Undo");
		menu.add(undoOption);
		
		ActionListener undoListener = new UndoListener();
		undoOption.addActionListener(undoListener);
		
		commandManager = new CommandManager();
		
		// set up the frame
		
		int textWidth = 200;
		int scoreWidth = 100;
		
		frame.add(forfeitButton);
		frame.add(restartButton);
		frame.add(boardComponent);
		frame.addMouseListener(chessMouseListener);
		frame.addMouseMotionListener((MouseMotionListener) chessMouseListener);
		frame.setJMenuBar(menuBar);
		frame.setTitle("Chess");
		frame.setSize((board.getWidth() + 1) * boardComponent.getPointSize() + textWidth + scoreWidth, (board.getLength() + 1) * boardComponent.getPointSize() + + forfeitButton.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Object[] possibleValues = { "Human vs Human", "Human vs Computer", "Computer vs Computer" };
		int gameValue = JOptionPane.showOptionDialog(null, "Choose an option", "Input", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);

		// setting up the players and their scores
		
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

		JTextField blackText = new JTextField(blackPlayer.getName(), 15); // maximum of 15 characters
		JTextField whiteText = new JTextField(whitePlayer.getName(), 15);
		blackText.setEditable(true);
		whiteText.setEditable(true);
		blackText.setBounds(boardComponent.getPointSize() * board.getWidth(), 0, textWidth, 50);
		whiteText.setBounds(boardComponent.getPointSize() * board.getWidth(), boardComponent.getPointSize() * board.getLength() / 2, textWidth,
				50);
		ActionListener blackTextListener = new TextListener(blackText, blackPlayer);
		ActionListener whiteTextListener = new TextListener(whiteText, whitePlayer);
		blackText.addActionListener(blackTextListener);
		whiteText.addActionListener(whiteTextListener);
		boardComponent.add(blackText);
		boardComponent.add(whiteText);

		playerOneText = new JLabel("  Score: " + playerOneScore);
		playerTwoText = new JLabel("  Score: " + playerTwoScore);
		playerOneText.setBounds(boardComponent.getPointSize() * board.getWidth() + blackText.getWidth(), 0, scoreWidth, 50);
		playerTwoText.setBounds(boardComponent.getPointSize() * board.getWidth() + whiteText.getWidth(), boardComponent.getPointSize()
				* board.getLength() / 2, scoreWidth, 50);
		boardComponent.add(playerOneText);
		boardComponent.add(playerTwoText);

		boardComponent.repaint();

		// set up the computer vs computer loop
		
		if (gameValue == 2) {
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
