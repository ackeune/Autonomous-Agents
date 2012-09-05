import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public class Map {
	boolean print = true;
	
	public static void main(String[] args)
	{
		
		
		int simulations = 1;
		double mean =0;
		double[] timeList = new double[simulations];
		for(int i=0; i<simulations; i++)
		{
			Map environment = new Map();
			int time = environment.run();
			timeList[i] = time;
			mean += time;
			
		}
		mean /=simulations;
		double dev = standardDeviation(timeList, mean);
		System.out.printf("mean: %f\nstandard deviation: %f\n", mean, dev);
	}  
	
	public static double standardDeviation(double[] timeList, double mean)
	{
		double dev = 0;
		for(int i=0; i<timeList.length; i++)
		{
			dev += Math.pow(timeList[i]- mean,2);
		}
		return Math.sqrt(dev / timeList.length);
	}

	List<Predator> agents;
	Prey prey;


	public Map()
	{
		agents = new ArrayList<Predator>();
		agents.add(new Predator(new Point(0,0)));
		prey = new Prey(new Point(5,5));
	}

	public int run()
	{
		//the amount of iterations
		int time = 0;
		while(!preyCaught()){
			//move prey
			prey.doAction(this);
			//move predator
			for( int i = 0;i < agents.size() ;i++ )
			{
				agents.get(i).doAction(this);
			}
			time++;
			if(print = true)
			System.out.print(toString());
		}
		return time;
	}

	public boolean preyCaught(){
		for(int i = 0; i < agents.size(); i++){
			//if an agent has found the predator, the game is over
			if(agents.get(i).pos.equals(prey.pos))
				return true;
		}
		return false;
	}

	public static Point nextTo(Point pos, String action)
	{
		Point newP = new Point(pos);
		if( action.equals("N") )
		{
			newP.y--;
			if(newP.y<0)
				newP.y=11;
		}else if( action.equals("E") )
		{
			newP.x++;
			if(newP.x>10)
				newP.x=0;
		}else if( action.equals("S") )
		{
			newP.y++;
			if(newP.y>10)
				newP.y=0;
		}else if( action.equals("W") )
		{
			newP.x--;
			if(newP.x<0)
				newP.x=11;
		}
		return newP;
	}

	public String toString()
	{
		String s = "";
		s += prey.toString() + "\n";
		for(int i = 0; i<agents.size(); i++){
			s += agents.get(i).toString() + "\n";
		}
		return s;
	}
	
}
