package cs242.chess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import cs242.chess.pieces.ChessPiece;

public class ChessComponent extends JComponent {

	private ChessBoard board;
	private ArrayList<ChessPlayer> playerList;
	private int pointSize = 100;

	// provided as a default constructor
	public ChessComponent() {
	}

	public ChessComponent(ChessBoard newBoard) {
		board = newBoard;
		playerList = new ArrayList<ChessPlayer>();
	}

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

	public void update() {
		ArrayList<ChessPiece> removeList = new ArrayList<ChessPiece>();
		for (ChessPlayer p : playerList) {
			for (ChessPiece piece : p.getPieces()) {
				if (piece.getSpace() == null) {
					removeList.add(piece);
				}
			}
			for (int i = 0; i < removeList.size(); i++) {
				p.removePiece(removeList.remove(i));
			}
		}
	}

	public int getPointSize() {
		return pointSize;
	}

	public void addPlayer(ChessPlayer newPlayer) {
		playerList.add(newPlayer);
	}

	public ChessPlayer getPlayer(int index) {
		return playerList.get(index);
	}

	public ArrayList<ChessPlayer> getPlayerList() {
		return playerList;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		for (int row = 0; row < board.getLength(); row++) {
			for (int col = 0; col < board.getWidth(); col++) {
				if ((row + col) % 2 == 1) {
					g2.setColor(Color.GRAY);
				}
				else {
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
