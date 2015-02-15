import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
//import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

//import java.applet.*;

public class SudokuDisplayApplet extends JApplet {
	
	private static SudokuSpace currentSpace;
	private static double timerCount = 0.0;

	public int askSudokuNum()
	{
		int num = 0;
		String s = "";
		boolean firstTime = true;
		while (num < 1 || num > 9)
		{
			if (firstTime)
			{
				s = JOptionPane.showInputDialog(null, "Please enter a number", "Enter a number", JOptionPane.QUESTION_MESSAGE);
				if (s == null)
				{
					return -1; // pressed the cancel button
				}
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
				if (s == null)
				{
					return -1; // pressed the cancel button
				}
				try {
					num = Integer.parseInt(s);
				} catch (NumberFormatException e) {
					num = 0;
				}
			}
		}
		return num;
	}
	
	public double roundToDecimal(double d, int numPlaces)
	{
		int num = (int) (d * Math.pow(10, numPlaces));
		return (double) (num / Math.pow(10, numPlaces));
	}

	public void init() 
	{
		final SudokuBoard board = new SudokuBoard();
		board.setSudoku();
		board.hideSpaces(); 	// default setting 
		final SudokuComponentApplet boardComponent;;boardComponent = new SudokuComponentApplet(board);
		
		final JLabel timerLabel = new JLabel("Timer: 0.0");
		timerLabel.setBounds(0, (int) (board.getLength() * boardComponent.getPointSize()), 200, 50);
		
		class TimerListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) 
			{
				timerCount+= .1;
				timerLabel.setText("Timer: " + roundToDecimal(timerCount, 1));
			}
			
		}
		
		ActionListener timerListener = new TimerListener();
		final Timer timer = new Timer(100, timerListener);
		
		class SudokuMouseListener extends MouseAdapter
		{
			public void mousePressed(MouseEvent e)
			{
				int y = e.getY();
				int x = e.getX();
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
				int result = -1;
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					result = 0;
				}
				else if (e.getButton() == MouseEvent.BUTTON3)
				{
					result = 2;
				}
				else
				{
					result = 1;
				}
				if (result == 0)
				{
					int num = askSudokuNum();
					if (num == -1)
					{
						return;
					}
					currentSpace.setGuess(num);
					if (board.checkPlayerResult())
					{
						boardComponent.repaint();
						timer.stop();
						int numMinutes = (int) (timerCount / 60);
						double numSeconds = roundToDecimal(timerCount % 60, 1);
						JOptionPane.showMessageDialog(null, "You win!" + "\n It took you " + numMinutes + " minutes and " + numSeconds + " seconds.",
								"Congratulations", JOptionPane.INFORMATION_MESSAGE);
						getContentPane().removeMouseListener(this);
					}
				}
				if (result == 1)
				{
					Object[] possibleActions = {"Add", "Remove"};
					int action = JOptionPane.showOptionDialog(null, "Do you add or remove a possibility?",
							"Add", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, possibleActions, "Add");
					int num = askSudokuNum();
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
		getContentPane().addMouseListener(sudokuMouseListener);
		getContentPane().add(timerLabel);
		getContentPane().add(boardComponent);
	}
	
	public void start()
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void destroy()
	{
		
	}
	
	public static void main(String[] args)
	{
//		final JFrame frame = new JFrame("Sudoku");
		SudokuDisplayApplet a = new SudokuDisplayApplet();
		a.init();
	//	frame.setSize(width, height);
	//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	frame.setVisible(true);
	}
}