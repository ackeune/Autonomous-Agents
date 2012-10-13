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
		StatePolicy a = new StatePolicy();
		StatePolicy b = new StatePolicy();
		System.out.printf("%b\n", a.equals(b));
		System.out.printf("%d\n%d\n", a.hashCode(), b.hashCode());
	}
	
	/**
	 * Calculate the standard deviation of a set of times.
	 * @param timeList	a list of time values
	 * @param mean		the mean of timeList
	 * @return standard deviation
	 */
	public static double standardDeviation(double[] timeList, double mean)
	{
		double dev = 0;
		for(int i=0; i<timeList.length; i++)
		{
			dev += Math.pow(timeList[i]- mean,2);
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
	 * Print an array
	 * @param <E>
	 * @param a array
	 */
	public static <E> void printArray(E[] a)
	{
		List<E> list = Arrays.asList(a);
		System.out.println(list);
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

}//end class Test
