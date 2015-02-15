import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

public class SudokuDisplay {

	private static SudokuSpace currentSpace;
	private static double timerCount = 0.0;
	
	public static int askSudokuNum()
	{
		int num = 0;
		String s = "";
		boolean firstTime = true;
		while (num < 1 || num > 9)
		{
			if (firstTime)
			{
				s = JOptionPane.showInputDialog(null, "Please enter a number", "Enter a number", JOptionPane.QUESTION_MESSAGE);
				try {
					num = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					num = 0;
				}
				firstTime = false;
			}
			else
			{
				s = JOptionPane.showInputDialog(null, "Please enter a valid number, greater than 0 and less than 10", "Enter a number", JOptionPane.QUESTION_MESSAGE);
				try {
					num = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					num = 0;
				}
			}
		}
		return num;
	}
	
	public static double roundToDecimal(double d, int numPlaces)
	{
		int num = (int) (d * Math.pow(10, numPlaces));
		return (double) (num / Math.pow(10, numPlaces));
	}
	
	public static void main(String[] args)
	{
		final SudokuBoard board = new SudokuBoard();
		board.setSudoku();
		board.hideSpaces(35); 	// default setting 
		final SudokuComponent boardComponent = new SudokuComponent(board);
		
		JRadioButton easyButton = new JRadioButton("Easy", true);
		JRadioButton mediumButton = new JRadioButton("Medium");
		JRadioButton hardButton = new JRadioButton("Hard");
		ButtonGroup group = new ButtonGroup();
		group.add(easyButton);
		group.add(mediumButton);
		group.add(hardButton);
		
		final JFrame frame = new JFrame("Sudoku");
		
		final JLabel timerLabel = new JLabel("Timer: 0.0");
		timerLabel.setBounds(0, (int) (board.getLength() * boardComponent.getPointSize()), 200, 50);
		
		class TimerListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) 
			{
				timerCount+= .1;
				timerLabel.setText("Timer: " + SudokuDisplay.roundToDecimal(timerCount, 1));
			}
			
		}
		
		ActionListener timerListener = new TimerListener();
		final Timer timer = new Timer(100, timerListener);
		
		class EasyListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) 
			{
				board.hideSpaces(35);
			}
		}
		
		ActionListener easyButtonListener = new EasyListener();
		easyButton.addActionListener(easyButtonListener);
		
		class MediumListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				board.hideSpaces(45);
			}
		}
		
		ActionListener mediumButtonListener = new MediumListener();
		mediumButton.addActionListener(mediumButtonListener);
		
		class HardListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				board.hideSpaces(55);
			}
		}
		
		ActionListener hardButtonListener = new HardListener();
		hardButton.addActionListener(hardButtonListener);
		
		JPanel difficultyPanel = new JPanel();
		
		difficultyPanel.add(easyButton);
		difficultyPanel.add(mediumButton);
		difficultyPanel.add(hardButton);
		JOptionPane.showMessageDialog(null, difficultyPanel, "Please choose a difficulty", JOptionPane.QUESTION_MESSAGE);

		class SudokuMouseListener extends MouseAdapter
		{
			public void mousePressed(MouseEvent e)
			{
				int y = e.getY() - 30;
				int x = e.getX() - 10;
				int row = y / boardComponent.getPointSize();
				int col = x / boardComponent.getPointSize();
				if (row >= 0 && row < 9 && col >= 0 && col < 9)
				{
					currentSpace = board.getPointValue(row, col);
				}
				else
				{
					currentSpace = null;
				}
				if (currentSpace != null)
				{
					timer.start();
				}
			}
			
			public void mouseReleased(MouseEvent e)
			{
				if (currentSpace == null)
				{
					return;
				}
				if (currentSpace.getRevealed())
				{
					return;
				}
				Object[] options = {"Enter a number", "Enter/Remove a possibility", "Remove a number"};
				int result = JOptionPane.showOptionDialog(null, "Do you want to add/remove a number or a add/remove a possibility?",
						"Please select an option", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, "Enter a number");
				if (result == 0)
				{
					int num = SudokuDisplay.askSudokuNum();
					currentSpace.setGuess(num);
					if (board.checkPlayerResult())
					{
						boardComponent.repaint();
						timer.stop();
						int numMinutes = (int) (timerCount / 60);
						double numSeconds = SudokuDisplay.roundToDecimal(timerCount % 60, 1);
						JOptionPane.showMessageDialog(null, "You win!" + "\n It took you " + numMinutes + " minutes and " + numSeconds + " seconds.",
								"Congratulations", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				if (result == 1)
				{
					Object[] possibleActions = {"Add", "Remove"};
					int action = JOptionPane.showOptionDialog(null, "Do you add or remove a possibility?",
							"Add", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, possibleActions, "Add");
					int num = SudokuDisplay.askSudokuNum();
					if (action == 0)
					{
						currentSpace.addPossible(num);
					}
					if (action == 1)
					{
						currentSpace.removePossible(num);
					}
				}
				if (result == 2)
				{
					currentSpace.setGuess(0);
				}
				boardComponent.repaint();
			}
		}
		
		MouseListener sudokuMouseListener = new SudokuMouseListener();
		
		frame.addMouseListener(sudokuMouseListener);
		frame.add(timerLabel);
		frame.add(boardComponent); 
		frame.setSize((board.getWidth() + 1 ) * boardComponent.getPointSize(), (board.getLength() + 1 ) * boardComponent.getPointSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
