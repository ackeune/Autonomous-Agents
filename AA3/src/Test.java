import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
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
		//ex1();
		ex2(false);
		//ex3(false);
		
	}//end main
	
	public static void ex1()
	{
		System.out.println("Ex1 - simulations with random policies");
		int predatorAmount = 1;
		String stateType = "RelativeState";
		Environment env1 = new Environment(predatorAmount, stateType);
		int episodes = 100;
		double tripProb = 0.2;
		int[] episodeLengths1 = env1.randomSimulations(episodes, tripProb);
		double mean = mean(episodeLengths1);
		double dev = standardDeviation(episodeLengths1, mean);
		System.out.printf("Mean:%f\nStandard deviation:%f\n", mean, dev);
		System.out.println();
	}//end ex1
	
	public static void ex2(boolean print)
	{
		System.out.println("Ex2 - Independent Q-learning");
		int predatorAmount = 3;
		double initialValue = 15;
		int episodes = 50000;
		double alpha = 0.5;
		double gamma = 0.9;
		double epsilon = 0.1; 
		double tripProb = 0.2;
		int runs = 10;
		String stateType = "DiagState";
		int[][] episodeLengthsSet = new int[runs][episodes];
		int[][] episodeEndingsSet = new int[runs][episodes];
		
		for(int r=0; r<runs; r++)
		{
			System.out.printf("Run: %d\t%s\n", r, new Date());
			Environment env = new Environment(predatorAmount, stateType);
			int[][] info = env.independentQLearningEGreedy(initialValue, 
					episodes, alpha, gamma, epsilon, tripProb);
			int[] tempEpisodeLengths = info[0];
			int[] tempEpisodeEndings = info[1];
			episodeLengthsSet[r] = tempEpisodeLengths;
			episodeEndingsSet[r] = tempEpisodeEndings;
		}
		if( runs > 0 )
		{
			double[] averageEpisodeLengths = mean(episodeLengthsSet);
			double[] stnDevEpisodeLengths = standardDeviation(episodeLengthsSet, averageEpisodeLengths);
			double[] averageEpisodeEndings = mean(episodeEndingsSet);
			double[] stnDevEpisodeEndings = standardDeviation(episodeEndingsSet, averageEpisodeEndings);
			if( print )
			{
				System.out.println(arrayToString(averageEpisodeLengths));
				System.out.println(arrayToString(stnDevEpisodeLengths));
				System.out.println(arrayToString(averageEpisodeEndings));
				System.out.println(arrayToString(stnDevEpisodeEndings));
			}
									
			// print to file
			double[][] toPrint = new double[4][averageEpisodeLengths.length];
			toPrint[0] = averageEpisodeLengths;
			toPrint[1] = stnDevEpisodeLengths;
			toPrint[2] = averageEpisodeEndings;
			toPrint[3] = stnDevEpisodeEndings;
			String stateSpace = new Environment(1, stateType).getStateName();
			String fileName = String.format("IQL_Preds%dAlpha%.1fGamma%.1fEpsilon%.1fEpisodes%dRuns%d%s.txt", 
					predatorAmount, alpha, gamma, epsilon, episodes, runs, stateSpace);
			fileName = fileName.replaceAll(",", ".");
			printToFile(fileName, toPrint);
		}
		System.out.println();
	}//end ex2
	
	public static void ex3(boolean print)
	{
		System.out.println("Ex3 - Hive Mind Q-learning");
		int predatorAmount = 2;
		double initialValue = 15;
		int episodes = 10000;
		double alpha = 0.5;
		double gamma = 0.9;
		double epsilon = 0.1; 
		double tripProb = 0.2;
		int runs = 10;
		String stateType = "DiagState";
		int[][] episodeLengthsSet = new int[runs][episodes];
		int[][] episodeEndingsSet = new int[runs][episodes];
		
		for(int r=0; r<runs; r++)
		{
			System.out.printf("Run: %d\t%s\n", r, new Date());
			Environment env = new Environment(predatorAmount, stateType);
			int[][] info = env.hiveMindQLearningEGreedy(initialValue, 
					episodes, alpha, gamma, epsilon, tripProb);
			int[] tempEpisodeLengths = info[0];
			int[] tempEpisodeEndings = info[1];
			episodeLengthsSet[r] = tempEpisodeLengths;
			episodeEndingsSet[r] = tempEpisodeEndings;
		}
		if( runs > 0 )
		{
			double[] averageEpisodeLengths = mean(episodeLengthsSet);
			double[] stnDevEpisodeLengths = standardDeviation(episodeLengthsSet, averageEpisodeLengths);
			double[] averageEpisodeEndings = mean(episodeEndingsSet);
			double[] stnDevEpisodeEndings = standardDeviation(episodeEndingsSet, averageEpisodeEndings);
			if( print )
			{
				System.out.println(arrayToString(averageEpisodeLengths));
				System.out.println(arrayToString(stnDevEpisodeLengths));
				System.out.println(arrayToString(averageEpisodeEndings));
				System.out.println(arrayToString(stnDevEpisodeEndings));
			}
						
			// print to file
			double[][] toPrint = new double[4][averageEpisodeLengths.length];
			toPrint[0] = averageEpisodeLengths;
			toPrint[1] = stnDevEpisodeLengths;
			toPrint[2] = averageEpisodeEndings;
			toPrint[3] = stnDevEpisodeEndings;
			String stateSpace = new Environment(1, stateType).getStateName();
			String fileName = String.format("HMQL_Preds%dAlpha%.1fGamma%.1fEpsilon%.1fEpisodes%dRuns%d%s.txt", 
					predatorAmount, alpha, gamma, epsilon, episodes, runs, stateSpace);
			fileName = fileName.replaceAll(",", ".");
			printToFile(fileName, toPrint);
		}
		System.out.println();
	}//end ex3
	
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
	
	public static double[] mean(int[][] episodes)
	{
		int sum[] = new int[episodes[0].length];
		for(int i=0; i<episodes.length; i++)
			sum = add(sum, episodes[i]);
		return divide(sum, episodes.length); // divide by runs
	}
	
	public static double[] standardDeviation(int[][] episodes, double[] mean)
	{
		double[] sqrdDif = new double[episodes[0].length];
		for(int i=0; i<episodes.length; i++)
		{
			double[] dif = subtract(episodes[i], mean);
			double[] tempSqrdDif = pow(dif, 2);
			sqrdDif = add(sqrdDif, tempSqrdDif);
		}
		double[] div = divide(sqrdDif, episodes.length);
		return sqrt(div);
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
	
	public static double[] sqrt(double[] a)
	{
		double[] b = new double[a.length];
		for(int i=0; i<a.length; i++)
			b[i] = Math.sqrt(a[i]);
		return b;
	}
	
	public static double[] pow(double[] a, double pow)
	{
		double[] b = new double[a.length];
		for(int i=0; i<a.length; i++)
			b[i] = Math.pow(a[i], pow);
		return b;
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
	public static double[] add(double[] a, double[] b)
	{
		double[] c = new double[a.length];
		for(int i=0; i<a.length; i++)
			c[i] = a[i]+b[i];
		return c;
	}
	
	public static double[] subtract(int[] a, double[] b)
	{
		double[] c = new double[a.length];
		for(int i=0; i<a.length; i++)
			c[i] = a[i]-b[i];
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
	public static double[] divide(double[] a, double d)
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
