import java.util.Random;


public class ArrayMethods {

	public static int[][] copyArray(int[][] array)
	{
		int[][] newArray = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++)
		{
			for (int j = 0; j < array[0].length; j++)
			{
				newArray[i][j] = array[i][j];
			}
		}
		return newArray;
	}

	public static SudokuSpace[][] copyArray(SudokuSpace[][] array)
	{
		SudokuSpace[][] newArray = new SudokuSpace[array.length][array[0].length];
		for (int i = 0; i < array.length; i++)
		{
			for (int j = 0; j < array[0].length; j++)
			{
				newArray[i][j] = array[i][j].copy();
			}
		}
		return newArray;
	}
	
	public static boolean hasNum(int[] nums, int num)
	{
		for (int e : nums)
		{
			if (e == num)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * The Fisher-Yates shuffle array algorithm
	 * @param array
	 */
	public static void shuffle(int[] array)
	{
		int index, temp;
		Random random = new Random();
		for (int i = array.length - 1; i > 0; i--)
		{
			index = random.nextInt(i + 1);
			temp = array[i];
			array[i] = array[index];
			array[index] = temp;
		}
	}
}
