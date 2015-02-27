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
import cs242.chess.pieces.King;

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
	private int pointSize = 75;
	private ArrayList<ChessSpace> moveSpaces; // highlight these spaces in green
	private ArrayList<ChessSpace> captureSpaces; // highlight these spaces in red
	private ChessSpace mouseOverSpace; // highlight this space in blue

	/**
	 * Creates a ChessComponent object with the chess board is is to represent.
	 * 
	 * @param newBoard the ChessBoard the component is to represent
	 */
	public ChessComponent(ChessBoard newBoard) {
		board = newBoard;
		playerList = new ArrayList<ChessPlayer>();
		moveSpaces = new ArrayList<ChessSpace>();
		captureSpaces = new ArrayList<ChessSpace>();
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
	 * Returns the ChessBoard.
	 * 
	 * @return the ChessBoard
	 */
	public ChessBoard getBoard() {
		return board;
	}
	
	/**
	 * Clears the ArrayList for highlighting squares.
	 */
	public void clearLists() {
		moveSpaces = new ArrayList<ChessSpace>();
		captureSpaces = new ArrayList<ChessSpace>();
	}
	
	/**
	 * Adds a new move space.
	 * 
	 * @param newSpace The space to be added
	 */
	public void addMoveSpace(ChessSpace newSpace) {
		moveSpaces.add(newSpace);
	}
	
	/**
	 * Adds a new space that can be captured
	 * 
	 * @param newSpace the space that can be captured
	 */
	public void addCaptureSpace(ChessSpace newSpace) {
		captureSpaces.add(newSpace);
	}
	
	/**
	 * Sets the current space that the mouse is over.
	 * 
	 * @param newSpace the space that the mouse is over
	 */
	public void setMouseOverSpace(ChessSpace newSpace) {
		mouseOverSpace = newSpace;
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
		// rescale images
		for (int pieceIndex = 0; pieceIndex < newPlayer.getPieces().size(); pieceIndex++) {
			Image scaledImage = newPlayer.getPieces().get(pieceIndex).getImageIcon().getImage().getScaledInstance(pointSize, pointSize, Image.SCALE_DEFAULT);
			newPlayer.getPieces().get(pieceIndex).getImageIcon().setImage(scaledImage);
		}
		playerList.add(newPlayer);
	}
	
	/**
	 * Removes all players from the player list.
	 */
	public void clearPlayerList() {
		playerList.clear();
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
				Rectangle r = new Rectangle(col * pointSize, row * pointSize, pointSize, pointSize);
				g2.fill(r);
			}
		}

		
		// paint the possible move spaces
		g2.setColor(new Color(51, 255, 51, 150)); // lime green
		for (ChessSpace space : moveSpaces) {
			int row = space.getRow();
			int col = space.getCol();
			Rectangle r = new Rectangle(col * pointSize, row * pointSize, pointSize, pointSize);
			g2.fill(r);
		}
		
		// paint the capture spaces
		g2.setColor(new Color(255, 51, 51, 150)); // light red
		for (ChessSpace space : captureSpaces) {
			int row = space.getRow();
			int col = space.getCol();
			Rectangle r = new Rectangle(col * pointSize, row * pointSize, pointSize, pointSize);
			g2.fill(r);
		}
		
		// paint the space the mouse is over
		if (mouseOverSpace != null) {
			g2.setColor(new Color(132, 112, 255, 150)); // light blue
			Rectangle r = new Rectangle(mouseOverSpace.getCol() * pointSize, mouseOverSpace.getRow() * pointSize, pointSize, pointSize);
			g2.fill(r);
		}
		
		for (ChessPlayer p : playerList) {
			for (ChessPiece c : p.getPieces()) {
				// if the piece is a king and is in check, paint the backgroud red
				if (c instanceof King) {
					King king = (King) c;
					if (king.getCheck()) {
						g2.setColor(Color.RED);
						Rectangle r = new Rectangle(king.getSpace().getCol() * pointSize, king.getSpace().getRow() * pointSize, pointSize, pointSize);
						g2.fill(r);
					}
				}
				// note that col is x and row is y
				// we include this if statement for the case in which the board is repainted before the piece array can update
				if (c.getSpace() != null) {
					c.getImageIcon().paintIcon(this, g2, c.getSpace().getCol() * pointSize, c.getSpace().getRow() * pointSize);
				}
			}
		}
	}	
}
