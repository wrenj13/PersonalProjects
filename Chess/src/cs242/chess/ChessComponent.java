package cs242.chess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import cs242.chess.pieces.ChessPiece;

/**
 * A class that represents a ChessBoard object with the pieces. It creates the board outline and displays the pieces on it. It also holds an
 * ArrayList of the players.
 * 
 * @author REN-JAY_2
 * 
 */
@SuppressWarnings("serial")
public class ChessComponent extends JComponent {

	private ChessBoard board;
	private ArrayList<ChessPlayer> playerList;
	private int pointSize = 100;

	/**
	 * Creates a ChessComponent object with the chess board is is to represent.
	 * 
	 * @param newBoard the ChessBoard the component is to represent
	 */
	public ChessComponent(ChessBoard newBoard) {
		board = newBoard;
		playerList = new ArrayList<ChessPlayer>();
	}

	/**
	 * Creates an Image object from a file name.
	 * 
	 * @param fileName the name of the file to be created
	 * @return the Image that the file refers to
	 */
	public static Image createImage(String fileName) {
		Image image = null;
		File f = new File(fileName);
		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * Returns the size of a square on the board.
	 * 
	 * @return the size of a square on the board
	 */
	public int getPointSize() {
		return pointSize;
	}

	/**
	 * Adds a player to the game.
	 * 
	 * @param newPlayer the player to add to the game
	 */
	public void addPlayer(ChessPlayer newPlayer) {
		playerList.add(newPlayer);
	}

	/**
	 * Returns the player at the given index in the ArrayList.
	 * 
	 * @param index the index of the desired player
	 * @return the player at that index
	 */
	public ChessPlayer getPlayer(int index) {
		return playerList.get(index);
	}

	/**
	 * Returns the ArrayList of players.
	 * 
	 * @return the ArrayList of players
	 */
	public ArrayList<ChessPlayer> getPlayerList() {
		return playerList;
	}

	/**
	 * Paints the board and pieces.
	 * 
	 * @param g the graphics to be used
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		for (int row = 0; row < board.getLength(); row++) {
			for (int col = 0; col < board.getWidth(); col++) {
				if ((row + col) % 2 == 1) {
					g2.setColor(Color.GRAY);
				} else {
					g2.setColor(Color.WHITE);
				}
				Rectangle r = new Rectangle(row * pointSize, col * pointSize, pointSize, pointSize);
				g2.fill(r);
			}
		}

		for (ChessPlayer p : playerList) {
			for (ChessPiece c : p.getPieces()) {
				c.getImageIcon().paintIcon(this, g2, c.getSpace().getCol() * pointSize, c.getSpace().getRow() * pointSize); // note that col
																															// is x and row
																															// is y
			}
		}
	}
}
