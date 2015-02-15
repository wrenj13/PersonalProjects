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

public class SudokuComponent extends JComponent {

	private SudokuBoard board;
	private int pointSize = 75;
	private int rectangleSize = 10;
	
	public SudokuComponent(SudokuBoard newBoard)
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
		File preOneFile = new File("Sudoku Pictures/Preset_One.png");
		try {
			preOne = ImageIO.read(preOneFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preOneIcon = new ImageIcon(preOne);
		
		Image preTwo = null;
		File preTwoFile = new File("Sudoku Pictures/Preset_Two.png");
		try {
			preTwo = ImageIO.read(preTwoFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preTwoIcon = new ImageIcon(preTwo);
		
		Image preThree = null;
		File preThreeFile = new File("Sudoku Pictures/Preset_Three.png");
		try {
			preThree = ImageIO.read(preThreeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preThreeIcon = new ImageIcon(preThree);
	
		Image preFour = null;
		File preFourFile = new File("Sudoku Pictures/Preset_Four.png");
		try {
			preFour = ImageIO.read(preFourFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preFourIcon = new ImageIcon(preFour);
		
		Image preFive = null;
		File preFiveFile = new File("Sudoku Pictures/Preset_Five.png");
		try {
			preFive = ImageIO.read(preFiveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preFiveIcon = new ImageIcon(preFive);
		
		Image preSix = null;
		File preSixFile = new File("Sudoku Pictures/Preset_Six.png");
		try {
			preSix = ImageIO.read(preSixFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preSixIcon = new ImageIcon(preSix);
		
		Image preSeven = null;
		File preSevenFile = new File("Sudoku Pictures/Preset_Seven.png");
		try {
			preSeven = ImageIO.read(preSevenFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preSevenIcon = new ImageIcon(preSeven);
		
		Image preEight = null;
		File preEightFile = new File("Sudoku Pictures/Preset_Eight.png");
		try {
			preEight = ImageIO.read(preEightFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preEightIcon = new ImageIcon(preEight);
		
		Image preNine = null;
		File preNineFile = new File("Sudoku Pictures/Preset_Nine.png");
		try {
			preNine = ImageIO.read(preNineFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon preNineIcon = new ImageIcon(preNine);
		
		Image setOne = null;
		File setOneFile = new File("Sudoku Pictures/Set_One.png");
		try {
			setOne = ImageIO.read(setOneFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setOneIcon = new ImageIcon(setOne);
		
		Image setTwo = null;
		File setTwoFile = new File("Sudoku Pictures/Set_Two.png");
		try {
			setTwo = ImageIO.read(setTwoFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setTwoIcon = new ImageIcon(setTwo);
		
		Image setThree = null;
		File setThreeFile = new File("Sudoku Pictures/Set_Three.png");
		try {
			setThree = ImageIO.read(setThreeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setThreeIcon = new ImageIcon(setThree);
	
		Image setFour = null;
		File setFourFile = new File("Sudoku Pictures/Set_Four.png");
		try {
			setFour = ImageIO.read(setFourFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setFourIcon = new ImageIcon(setFour);
		
		Image setFive = null;
		File setFiveFile = new File("Sudoku Pictures/Set_Five.png");
		try {
			setFive = ImageIO.read(setFiveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setFiveIcon = new ImageIcon(setFive);
		
		Image setSix = null;
		File setSixFile = new File("Sudoku Pictures/Set_Six.png");
		try {
			setSix = ImageIO.read(setSixFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setSixIcon = new ImageIcon(setSix);
		
		Image setSeven = null;
		File setSevenFile = new File("Sudoku Pictures/Set_Seven.png");
		try {
			setSeven = ImageIO.read(setSevenFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setSevenIcon = new ImageIcon(setSeven);
		
		Image setEight = null;
		File setEightFile = new File("Sudoku Pictures/Set_Eight.png");
		try {
			setEight = ImageIO.read(setEightFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon setEightIcon = new ImageIcon(setEight);
		
		Image setNine = null;
		File setNineFile = new File("Sudoku Pictures/Set_Nine.png");
		try {
			setNine = ImageIO.read(setNineFile);
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
		
		for (int i = 1; i <= 2; i++)
		{
			Rectangle r = new Rectangle(i * 3 * pointSize - rectangleSize / 2, 0, rectangleSize, board.getLength() * pointSize);
			g2.fill(r);
		}
		for (int j = 1; j <= 2; j++)
		{
			Rectangle r = new Rectangle(0, j * 3 * pointSize - rectangleSize / 2, board.getLength() * pointSize, rectangleSize);
			g2.fill(r);
		}	
	}
}
