
public class SudokuBoard implements Board<SudokuSpace>{

	private SudokuSpace[][] dimensions;

	public SudokuBoard()
	{
		dimensions = new SudokuSpace[9][9];
		clear();
	}
	
	public SudokuSpace getPointValue(int row, int col)
	{
		return dimensions[row][col];
	}

	public void setPointValue(int row, int col, SudokuSpace value)
	{
		dimensions[row][col] = value;
	}

	public int getLength()
	{
		return dimensions.length;
	}

	public int getWidth() 
	{
		return dimensions[0].length;
	}

	public void setDimensions(SudokuSpace[][] newDimensions) 
	{
		dimensions = newDimensions;
	}

	public Board<SudokuSpace> copy() 
	{
		SudokuBoard newBoard = new SudokuBoard();
		for (int i = 0; i < dimensions.length; i++)
		{
			for (int j = 0; j < dimensions[0].length; j++)
			{
				newBoard.setPointValue(i, j, getPointValue(i, j));
			}
		}
		return newBoard;
	}

	public void clear()
	{
		for (int i = 0; i < dimensions.length; i++)
		{
			for (int j = 0; j < dimensions.length; j++)
			{
				setPointValue(i, j, new SudokuSpace(0));
			}
		}
	}
	
	public boolean checkNums(int[] nums)
	{
		for (int i = 1; i <= 9; i++)
		{
			if (!ArrayMethods.hasNum(nums, i))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean checkSudoku()
	{
		int[][] nums = new int[9][9];
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				nums[i][j] = getPointValue(i, j).getNum();
			}
		}
		int[][] squareList = new int[9][9];
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				squareList[i][j] = getPointValue((j / 3 + (i / 3) * 3), (j % 3 + (i % 3) * 3)).getNum();
			}
		}
		for (int[] square : squareList)
		{
			if(!checkNums(square))
			{
				return false;
			}
		}
		for (int[] row : nums)
		{
			if (!checkNums(row))
			{
				return false;
			}
		}
		int[][] colList = new int[9][9];
		for (int i = 0; i < nums.length; i++)
		{
			for (int j = 0; j < nums.length; j++)
			{
				colList[i][j] = nums[j][i];
			}
		}
		for (int[] col : nums)
		{
			if (!checkNums(col))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean checkPlayerResult()
	{
		SudokuBoard playerBoard = new SudokuBoard();
		for (int i = 0; i < dimensions.length; i++)
		{
			for (int j = 0; j < dimensions[0].length; j++)
			{
				playerBoard.setPointValue(i, j, getPointValue(i, j));
				if (!getPointValue(i, j).getRevealed())
				{
					playerBoard.getPointValue(i, j).setNum(playerBoard.getPointValue(i, j).getGuess());
				}
			}
		}
		return playerBoard.checkSudoku();
	}
	
	public int addNum(int[] nums, int num, boolean[] possibleIndices)
	{
		if (ArrayMethods.hasNum(nums, num))
		{
			return -1;
		}
		int possibleCount = 0;
		for (int i = 0; i < possibleIndices.length; i++)
		{
			if (possibleIndices[i] && nums[i] == 0)
			{
				possibleCount++;
			}
		}
		if (possibleCount == 0)
		{
			return -1;
		}
		int[] possibleList = new int[possibleCount];
		int count = 0;
		for (int i = 0; i < possibleIndices.length; i++)
		{
			if (possibleIndices[i] && nums[i] == 0)
			{
				possibleList[count] = i;
				count++;
			}
		}
		int index = possibleList[(int) (Math.random() * possibleCount)];
		nums[index] = num;
		return index;
	}
	
	public void setSudoku()
	{
		int[][] beginningList = new int[9][9];
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				beginningList[i][j] = getPointValue((j / 3 + (i / 3) * 3), (j % 3 + (i % 3) * 3)).getNum();
			}
		}
		int[][] squareList = null;
		while (squareList == null)
		{
			squareList = setNumsOnBoard(1, ArrayMethods.copyArray(beginningList));
		}
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				getPointValue(i, j).setNum(squareList[(i / 3) * 3 + (j / 3)][(i % 3) * 3 + j % 3]);
			}
		}
	}
	
	public int[][] setNumsOnBoard(int num, int[][] squareList)
	{
		if (num > 9 || num < 1)
		{
			return squareList;
		}
		int tryCount = 0;
		boolean keepTrying = true;
		int[][] copy = ArrayMethods.copyArray(squareList);
		while (tryCount < 3)
		{
			keepTrying = true;
			squareList = copy;
			boolean[] usedRows = new boolean[9];
			boolean[] usedCols = new boolean[9];
			for (int i = 0; i < squareList.length && keepTrying; i++)
			{
				boolean[] possibleRows = {!usedRows[(i / 3) * 3], !usedRows[(i / 3) * 3 + 1], !usedRows[(i / 3) * 3 + 2]}; // go square by square
				boolean[] possibleCols = {!usedCols[(i % 3) * 3], !usedCols[(i % 3) * 3 + 1], !usedCols[(i % 3) * 3 + 2]};
				boolean[] possibleIndices = new boolean[9]; // 9 possible spaces per square
				for (int j = 0; j < possibleRows.length; j++)
				{
					for (int k = 0; k < possibleCols.length; k++)
					{
						possibleIndices[j * 3 + k] = possibleRows[j] && possibleCols[k];
					}
				}
				int index = addNum(squareList[i], num, possibleIndices);
				if (index == -1)
				{
					tryCount++;
					keepTrying = false;
				}
				else
				{
					int row = index / 3 + (i / 3) * 3;
					int col = index % 3 + (i % 3) * 3;
					usedRows[row] = true;
					usedCols[col] = true;
				}
			}
			if (keepTrying)
			{
				int[][] result = setNumsOnBoard(++num, squareList);
				if (result != null)
				{
					return result;
				}
			}
		}
		return null;
	}

	public void revealSpaces()
	{
		for (int i = 0; i < getLength(); i++)
		{
			for (int j = 0; j < getWidth(); j++)
			{
				getPointValue(i, j).setRevealed(true);
			}
		}
	}
	
	/**
	 * An outdated function that randomly removes numbers from the board
	 * @param num The number of numbers to remove
	 */
	public void hideSpaces(int num)
	{
		if (num > 81)
		{
			num = 81;
		}
		if (num < 0)
		{
			return;
		}
		int[] numPerSquare = new int[9];
		int remainingNum = num;
		int number = 0;
		for (int i = 0; i < 9; i++) // first, choose how many indices per square are going to be hidden
		{
			if (i == 8) // on the last square, whatever number of remaining spaces to be hidden are chosen
			{
				number = remainingNum;
			}
			else
			{
				number = (remainingNum / (9 - i)) + (int) (3 * Math.random() - 1);
			}
			numPerSquare[i] = number;
			remainingNum -= number;
		}
		// note that all spaces are preset as revealed because of the SudokuSpace constructor
		int[] hideIndices = new int[num];
		int indicesCount = 0;
		int count = 0;
		for (int i = 0; i < numPerSquare.length; i++)
		{
			while (count < numPerSquare[i])
			{
				int newNum = (int) (Math.random()  * 9 + 9 * i);
				if (!ArrayMethods.hasNum(hideIndices, newNum))
				{
					hideIndices[indicesCount] = newNum;
					indicesCount++;
					count++;
				}
			}
			count = 0;
		}
		for (int i = 0; i < num; i++)
		{
			int row = ((hideIndices[i] / 9) / 3) * 3 + (hideIndices[i] / 3) % 3;
			int col = ((hideIndices[i] / 9) % 3) * 3 + hideIndices[i] % 3;
			getPointValue(row, col).setRevealed(false);
		}
	}
	
	/**
	 * This method hide spaces so that there is a unique solution to the puzzle.
	 * It also makes it so that you cannot remove any more spaces without destroying the uniqueness of the solution.
	 * Credits to http://stackoverflow.com/questions/6924216/how-to-generate-sudoku-boards-with-unique-solutions for this algorithm
	 */
	public void hideSpaces()
	{
		int[] indices = new int[81];
		for (int i = 0; i < indices.length; i++)
		{
			indices[i] = i;
		}
		ArrayMethods.shuffle(indices);
		for (int i = 0; i < indices.length; i++)
		{
			int row = indices[i] / 9;
			int col = indices[i] % 9;
			dimensions[row][col].setRevealed(false);
			if (!hasUniqueSolution())
			{
				dimensions[row][col].setRevealed(true);
			}
		}
	}
	
	/**
	 * This algorithm traverses the rows and columns for the first empty space.
	 * Once it finds an empty space, it attempts to fill it with a legal number and recursively tries to solve the puzzle.
	 * 
	 * @return True if the puzzle only has one solution
	 */
	public boolean hasUniqueSolution()
	{
		SudokuSpace[][] testArray = ArrayMethods.copyArray(dimensions);
		return hasUniqueSolution(testArray) == 0? true : false;

	}
	
	// A helper function for hasUniqueSolution()
	// 0 indicates that there is a unique solution
	// 1 indicates that the puzzle as is can not be solved
	// 2 indicates that there are multiple solutions
	public int hasUniqueSolution(SudokuSpace[][] array)
	{
		int numSol = 0;
		boolean[] impossibleNums;
		boolean emptyFound = false;
		int emptyRow = -1;
		int emptyCol = -1;
		for (int i = 0; i < array.length && !emptyFound; i++) // first, I need to find the first empty space
		{
			for (int j = 0; j < array[0].length && !emptyFound; j++)
			{ 
				if (!array[i][j].getRevealed()) // found an empty space!
				{
					emptyFound = true;
					emptyRow = i;
					emptyCol = j;
				}
			}
		}
		if (!emptyFound) // The puzzle is full! (i.e. no empty spaces)
		{
			return 0;
		}
		impossibleNums = new boolean[9]; // now we need to mark down which numbers are not allowed
		for (int i = 0; i < array.length; i++)
		{
				if (array[emptyRow][i].getRevealed())
				{
					impossibleNums[array[emptyRow][i].getNum() - 1] = true;
				}
		}
		for (int i = 0; i < array.length; i++) // factor in column numbers
		{
			if (array[i][emptyCol].getRevealed())
			{
				impossibleNums[array[i][emptyCol].getNum() - 1] = true;
			}
		}
		int squareRow = emptyRow / 3;
		int squareCol = emptyCol / 3;
		for (int i = 0; i < 3; i++) // factor in square numbers
		{
			for (int j = 0; j < 3; j++)
			{
					if (array[squareRow * 3 + i][squareCol * 3 + j].getRevealed())
					{
						impossibleNums[array[squareRow * 3 + i][squareCol * 3 + j].getNum() - 1] = true;
					}
			}
		}
		boolean hasLegalNum = false;
		array[emptyRow][emptyCol].setRevealed(true);
		for (int i = 0; i < impossibleNums.length; i++) // try all legal numbers
		{
			if (!impossibleNums[i]) // if the number is allowed then try putting it in and recursively solve the puzzle
			{
				hasLegalNum = true;
				array[emptyRow][emptyCol].setNum(i + 1); // add one because the array is indexed starting at 0
				int result = hasUniqueSolution(array);
				if (result == 2) // if there are multiple solutions, return immediately. Don't waste time computing further.
				{
					return 2;
				}
				if (result == 0) // there is a solution (most of the time this should fail)
				{
					if (numSol > 0)
					{
						return 2; // there are multiple solutions
					}
					numSol++;
				}
			}
		}
		if (!hasLegalNum) // if no number is possible, then there is no solution
		{
			return 1;
		}
		return 0; // should get here only when there is one solution
	}
	
	public String squareToString(int[][] squareList)
	{
		String s = "";
		for (int i = 0; i < squareList.length; i++)
		{
			for (int j = 0; j < squareList[0].length; j++)
			{
				s += squareList[(i / 3) * 3 + (j / 3)][(i % 3) * 3 + j % 3] + " ";
			}
			s += "\n";
		}
		return s;
	}
	
	public String toString()
	{
		String s = "";
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				if (!getPointValue(i, j).getRevealed())
				{
					s += "  ";
				}
				else
				{
					s += getPointValue(i, j).getNum() + " ";
				}
			}
			s += "\n";
		}
		return s;
	}
	
	public static void main(String[] args)
	{
		SudokuBoard board = new SudokuBoard();
		board.setSudoku();
		board.hideSpaces();
		System.out.println(board);
	}
}
