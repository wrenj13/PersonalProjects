import java.util.ArrayList;


public class SudokuSpace {

	private int number;
	private int playerGuess;
	private ArrayList<Integer> possibleNums;
	private boolean revealed;
	
	public SudokuSpace(int num)
	{
		number = num;
		revealed = true;
		playerGuess = 0;
		possibleNums = new ArrayList<Integer>();
	}
	
	public int getNum()
	{
		return number;
	}
	
	public int getGuess()
	{
		return playerGuess;
	}
	
	public ArrayList<Integer> getPossibleNums()
	{
		return possibleNums;
	}
	
	public boolean getRevealed()
	{
		return revealed;
	}
	
	public void addPossible(int num)
	{
		if (!possibleNums.contains(num))
		{
			possibleNums.add(num);
		}
	}
	
	public void removePossible(int num)
	{
		possibleNums.remove((Object) num);
	}
	public void setNum(int newNum)
	{
		number = newNum;
	}
	
	public void setGuess(int newGuess)
	{
		playerGuess = newGuess;
	}
	
	public void setRevealed(boolean isRevealed)
	{
		revealed = isRevealed;
	}
	
	public SudokuSpace copy()
	{
		SudokuSpace newSpace = new SudokuSpace(getNum());
		newSpace.setGuess(getGuess());
		newSpace.setRevealed(getRevealed());
		ArrayList<Integer> origNums = getPossibleNums();
		for (int i = 0; i < origNums.size(); i++)
		{
			newSpace.addPossible(origNums.get(i));
		}
		return newSpace;
	}
}
