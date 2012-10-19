import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;


/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 */
public class Test 
{

	public static void main(String[] args) 
	{
		boolean randomSimulation = false;
		if( randomSimulation )
		{
			System.out.println("Ex1 - simulations with random policies");
			int predatorAmount = 1;
			Environment env1 = new Environment(predatorAmount);
			int episodes = 100;
			double tripProb = 0.2;
			int[] episodeLengths1 = env1.randomSimulations(episodes, tripProb);
			double mean = mean(episodeLengths1);
			double dev = standardDeviation(episodeLengths1, mean);
			System.out.printf("Mean:%f\nStandard deviation:%f\n", mean, dev);
		}
		
		System.out.println("\nEx2 - Independent Q-learning");
		int predatorAmount = 1;
		double initialValue = 15;
		int episodes = 500;
		double alpha = 0.5;
		double gamma = 0.9;
		double epsilon = 0.1; 
		double tripProb = 0.2;	
		int[] episodeLengths2 = new int[episodes];
		int[] episodeEndings2 = new int[episodes];
		int runs = 100;

		for(int r=0; r<runs; r++)
		{
			System.out.printf("Run: %d\n", r);
			Environment env2 = new Environment(predatorAmount);
			int[][] info = env2.independentQLearningEGreedy(initialValue, 
					episodes, alpha, gamma, epsilon, tripProb);
			int[] tempEpisodeLengths = info[0];
			int[] tempEpisodeEndings = info[1];
			episodeLengths2 = add(episodeLengths2, tempEpisodeLengths);
			episodeEndings2 = add(episodeEndings2, tempEpisodeEndings);
		}
		if( runs > 0 )
		{
			double[] averageEpisodeLengths2 = divide(episodeLengths2, runs);
			printArray(averageEpisodeLengths2);
			System.out.println();
			double[] averageEpisodeEndings2 = divide(episodeEndings2, runs);
			printArray(averageEpisodeEndings2);
			System.out.printf("\nMean length:%f\n", mean(averageEpisodeLengths2));
			System.out.printf("Mean endings:%f\n", mean(averageEpisodeEndings2));
			
			// print to file
			double[][] toPrint = 
				new double[2][averageEpisodeLengths2.length];
			toPrint[0] = averageEpisodeLengths2;
			toPrint[1] = averageEpisodeEndings2;
			String fileName = String.format("IQL_Preds%dAlpha%.1fGamma%.1fEpsilon%.1f.txt", 
					predatorAmount, alpha, gamma, epsilon);
			printToFile(fileName, toPrint);
		}
	}//end main
	
	/**
	 * Calculate the mean of a set of times.
	 * @param timeList 	list of episode lengths
	 * @return mean of episode lengths
	 */
	public static double mean(int[] timeList)
	{
		double sum = 0;
		for(int i=0; i<timeList.length; i++)
			sum += timeList[i];
		return sum / timeList.length;
	}
	public static double mean(double[] timeList)
	{
		double sum = 0;
		for(int i=0; i<timeList.length; i++)
			sum += timeList[i];
		return sum / timeList.length;
	}
	
	/**
	 * Calculate the standard deviation of a set of times.
	 * @param timeList	a list of episode lengths
	 * @param mean		the mean of timeList
	 * @return standard deviation of episode lengths
	 */
	public static double standardDeviation(int[] timeList, double mean)
	{
		double dev = 0;
		for(int i=0; i<timeList.length; i++)
		{
			dev += Math.pow(((double)timeList[i]) - mean,2);
		}
		return Math.sqrt(dev / timeList.length);
	}
	
	/**
	 * Return the sum of the values in array 'a' and 'b'.
	 * @param a array
	 * @param b array
	 * @return a+b
	 */
	public static int[] add(int[] a, int[] b)
	{
		int[] c = new int[a.length];
		for(int i=0; i<a.length; i++)
			c[i] = a[i]+b[i];
		return c;
	}
	
	/**
	 * Return the values in array 'a' divided by 'd'.
	 * @param a	array
	 * @param d division factor
	 * @return a/d
	 */
	public static double[] divide(int[] a, double d)
	{
		double[] b = new double[a.length];
		for(int i=0; i<a.length; i++)
			b[i] = ((double)a[i])/d;
		return b;
	}
	
	/**
	 * Return values in array 'a' multiplied by values in 'b'
	 * @param a
	 * @param b
	 * @return a*b
	 */
	public static double[] times(double[] a, double[] b)
	{
		double[] c = new double[a.length];
		for(int i=0; i<a.length; i++)
			c[i] = a[i] * b[i];
		return c;
	}
	
	/**
	 * Print an array
	 * @param <E>
	 * @param a array
	 */
	public static <E> void printArray(E[] a)
	{
		List<E> list = Arrays.asList(a);
		System.out.println(list);
	}
	
	public static void printArray(int[] a)
	{
		for(int i=0; i<a.length; i++)
			System.out.printf("%d ", a[i]);
	}
	
	public static void printArray(double[] a)
	{
		for(int i=0; i<a.length; i++)
			System.out.printf("%f ", a[i]);
	}
	
	public static String arrayToString(double[] a)
	{
		String s = "";
		for(int i=0; i<a.length; i++)
			s += String.format("%f ", a[i]);
		s = s.replaceAll(",", ".");
		return s;
	}
	
	/**
	 * Print double array.
	 * @param <E>	
	 * @param a double array.
	 */
	public static <E> void printDoubleArray(E[][] a)
	{
		for(int i=0; i<a.length; i++)
			printArray(a[i]);
	}
	
	public static void printToFile(String fileName, double[][] a)
	{
		try {
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			for(int i=0; i<a.length; i++)
				out.println(arrayToString(a[i]));
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}//end class Test
