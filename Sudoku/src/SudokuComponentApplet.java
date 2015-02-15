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
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class SudokuComponentApplet extends JComponent {

	private SudokuBoard board;
	private int pointSize = 75;
	private int rectangleSize = 10;
	
	public SudokuComponentApplet(SudokuBoard newBoard)
	{
		board = newBoard;
	}
	
	public int getPointSize()
	{
		return pointSize;
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		// Getting images
		
		Image preOne = null;
		try {
			preOne = ImageIO.read(getClass().getResource("Sudoku Pictures/Preset_One.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preOneIcon = new ImageIcon(preOne);
		
		Image preTwo = null;
		try {
			preTwo = ImageIO.read(getClass().getResource("Sudoku Pictures/Preset_Two.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preTwoIcon = new ImageIcon(preTwo);
		
		Image preThree = null;
		try {
			preThree = ImageIO.read(getClass().getResource("Sudoku Pictures/Preset_Three.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preThreeIcon = new ImageIcon(preThree);
	
		Image preFour = null;
		try {
			preFour = ImageIO.read(getClass().getResource("Sudoku Pictures/Preset_Four.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preFourIcon = new ImageIcon(preFour);
		
		Image preFive = null;
		try {
			preFive = ImageIO.read(getClass().getResource("Sudoku Pictures/Preset_Five.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preFiveIcon = new ImageIcon(preFive);
		
		Image preSix = null;
		try {
			preSix = ImageIO.read(getClass().getResource("Sudoku Pictures/Preset_Six.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preSixIcon = new ImageIcon(preSix);
		
		Image preSeven = null;
		try {
			preSeven = ImageIO.read(getClass().getResource("Sudoku Pictures/Preset_Seven.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preSevenIcon = new ImageIcon(preSeven);
		
		Image preEight = null;
		try {
			preEight = ImageIO.read(getClass().getResource("Sudoku Pictures/Preset_Eight.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preEightIcon = new ImageIcon(preEight);
		
		Image preNine = null;
		try {
			preNine = ImageIO.read(getClass().getResource("Sudoku Pictures/Preset_Nine.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preNineIcon = new ImageIcon(preNine);
		
		Image setOne = null;
		try {
			setOne = ImageIO.read(getClass().getResource("Sudoku Pictures/Set_One.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setOneIcon = new ImageIcon(setOne);
		
		Image setTwo = null;
		try {
			setTwo = ImageIO.read(getClass().getResource("Sudoku Pictures/Set_Two.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setTwoIcon = new ImageIcon(setTwo);
		
		Image setThree = null;
		try {
			setThree = ImageIO.read(getClass().getResource("Sudoku Pictures/Set_Three.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setThreeIcon = new ImageIcon(setThree);
	
		Image setFour = null;
		try {
			setFour = ImageIO.read(getClass().getResource("Sudoku Pictures/Set_Four.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setFourIcon = new ImageIcon(setFour);
		
		Image setFive = null;
		try {
			setFive = ImageIO.read(getClass().getResource("Sudoku Pictures/Set_Five.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setFiveIcon = new ImageIcon(setFive);
		
		Image setSix = null;
		try {
			setSix = ImageIO.read(getClass().getResource("Sudoku Pictures/Set_Six.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setSixIcon = new ImageIcon(setSix);
		
		Image setSeven = null;
		try {
			setSeven = ImageIO.read(getClass().getResource("Sudoku Pictures/Set_Seven.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setSevenIcon = new ImageIcon(setSeven);
		
		Image setEight = null;
		try {
			setEight = ImageIO.read(getClass().getResource("Sudoku Pictures/Set_Eight.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setEightIcon = new ImageIcon(setEight);
		
		Image setNine = null;
		try {
			setNine = ImageIO.read(getClass().getResource("Sudoku Pictures/Set_Nine.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setNineIcon = new ImageIcon(setNine);
		
		// painting the preset numbers
		
		for (int i = 0; i < board.getLength(); i++)
		{
			for (int j = 0; j < board.getWidth(); j++)
			{
				SudokuSpace space = board.getPointValue(i, j);
				int value = space.getNum();
				boolean revealed = space.getRevealed();
				if (revealed)
				{
					if (value == 1)
					{
						preOneIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
					}
					if (value == 2)
					{
						preTwoIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
					}
					if (value == 3)
					{
						preThreeIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
					}
					if (value == 4)
					{
						preFourIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
					}
					if (value == 5)
					{
						preFiveIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
					}
					if (value == 6)
					{
						preSixIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
					}
					if (value == 7)
					{
						preSevenIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
					}
					if (value == 8)
					{
						preEightIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
					}
					if (value == 9)
					{
						preNineIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
					}
				}
				else
				{
					int guess = space.getGuess();
					if (guess != 0)
					{
						if (guess == 1)
						{
							setOneIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
						}
						if (guess == 2)
						{
							setTwoIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
						}
						if (guess == 3)
						{
							setThreeIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
						}
						if (guess == 4)
						{
							setFourIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
						}
						if (guess == 5)
						{
							setFiveIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
						}
						if (guess == 6)
						{
							setSixIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
						}
						if (guess == 7)
						{
							setSevenIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
						}
						if (guess == 8)
						{
							setEightIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
						}
						if (guess == 9)
						{
							setNineIcon.paintIcon(this, g2, j * pointSize, i * pointSize);
						}
					}
					else
					{
						ArrayList<Integer> possibleNums = space.getPossibleNums();
						if (!possibleNums.isEmpty())
						{
							String s = "";
							for (Integer integer : possibleNums)
							{
								s += integer + " ";
							}
							g2.drawString(s, (int) (j * pointSize +  pointSize * .1), (int) (i * pointSize + pointSize * .25));
						}
					}
				}
			}
		}
		
		// painting the board
		for (int i = 1; i <= board.getLength(); i++)
		{
			Line2D.Double vLine = new Line2D.Double(i * pointSize, 0, i * pointSize, board.getLength() * pointSize);
			g2.draw(vLine);
		}
		for (int j = 1; j <= board.getWidth(); j++)
		{
			Line2D.Double hLine = new Line2D.Double(0, j * pointSize, board.getWidth() * pointSize, j * pointSize);
			g2.draw(hLine);
		}
		
		// filling in the square outlines with rectangles
		
		for (int i = 0; i <= 3; i++)
		{
			Rectangle r = new Rectangle(i * 3 * pointSize - rectangleSize / 2, 0, rectangleSize, board.getLength() * pointSize);
			g2.fill(r);
		}
		for (int j = 0; j <= 3; j++)
		{
			Rectangle r = new Rectangle(0, j * 3 * pointSize - rectangleSize / 2, board.getLength() * pointSize, rectangleSize);
			g2.fill(r);
		}	
	}
}
