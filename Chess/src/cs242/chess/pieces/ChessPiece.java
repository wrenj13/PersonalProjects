package cs242.chess.pieces;

import java.awt.Color;

import javax.swing.ImageIcon;

import cs242.chess.ChessSpace;

/**
 * A class to implement a Chess piece. It holds the ChessSpace that it is currently on. The user needs to take care to update the
 * ChessPiece's space data whenever the piece moves. It also includes the image used to draw it with the Java API
 * 
 * @author REN-JAY_2
 * 
 */
public abstract class ChessPiece {

	private Color color;
	private ChessSpace space;
	private ImageIcon image;
	private int value;

	/**
	 * Constructs the ChessPiece based on a color and a space.
	 * 
	 * @param newColor the color of the piece
	 * @param newSpace the space the piece begins on
	 */
	public ChessPiece(Color newColor, ChessSpace newSpace) {
		color = newColor;
		space = newSpace;
	}

	/**
	 * Returns the space the ChessPiece is on
	 * 
	 * @return the space the ChessPiece is on
	 */
	public ChessSpace getSpace() {
		return space;
	}

	/**
	 * Returns the color of the ChessPiece
	 * 
	 * @return the color of the ChessPiece
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns the Graphics image used to represent the ChessPiece in the GUI
	 * 
	 * @return the Graphic used to represent the ChessPiece
	 */
	public ImageIcon getImageIcon() {
		return image;
	}

	/**
	 * The relative value of the ChessPiece
	 * 
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the space the ChessPiece is on
	 * 
	 * @param newSpace the new space the ChessPiece is on
	 */
	public void setSpace(ChessSpace newSpace) {
		space = newSpace;
	}

	/**
	 * Sets the image used to represent the ChessPiece in the GUI
	 * 
	 * @param newImage the new image used to represent the ChessPiece
	 */
	public void setImageIcon(ImageIcon newImage) {
		image = newImage;
	}

	/**
	 * Sets the relative value of the ChessPiece
	 * 
	 * @param newValue the new value
	 */
	public void setValue(int newValue) {
		value = newValue;
	}

	/**
	 * Determines whether or not the piece can move to another ChessSpace This method assumes that there are no pieces in the way. The
	 * method leaves the determination of board limits to the game loop
	 * 
	 * @param newSpace the space the piece wants to go to
	 * @return true if the piece can move there, false otherwise
	 */
	public abstract boolean validMove(ChessSpace newSpace);

	/**
	 * Moves the ChessPiece to a new ChessSpace, taking care to account for the data in both the ChessPiece and the ChessSpaces. If there is
	 * a piece in the new ChessSpace of another color, it is captured and its space data is set to null. The method does not check if the
	 * move is allowed or not. Use in conjunction with canMove() in the ChessBoard class to check
	 * 
	 * @param newSpace the space to move the piece to
	 */
	public void moveTo(ChessSpace newSpace) {
		ChessPiece targetPiece = newSpace.getPiece();
		if (targetPiece != null) {
			newSpace.getPiece().setSpace(null);
		}
		newSpace.setPiece(this);
		if (getSpace() != null) {
			getSpace().setPiece(null);
		}
		setSpace(newSpace);
	}
}
