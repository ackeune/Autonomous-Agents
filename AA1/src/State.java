import java.awt.Point;
import java.util.Map;


public class State {
	boolean print = false;

	public static void main(String[] args)
	{


		int simulations = 100;
		double mean =0;
		double[] timeList = new double[simulations];
		for(int i=0; i<simulations; i++)
		{
			State environment = new State();
			int time = environment.run();
			timeList[i] = time;
			mean += time;

		}
		mean /=simulations;
		double dev = standardDeviation(timeList, mean);
		System.out.printf("mean: %f\nstandard deviation: %f\n", mean, dev);

		State environment2 = new State();
		printArray(environment2.agent.valueIteration(environment2));
		
		State environment3 = new State(new Point(10,10), new Point(0,0));
		double[][] grid = environment3.agent.policyEvaluation(environment3); 
		printArray(grid);
		System.out.println(grid[10][10]);

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
	
	public static double[][] copyArray(double[][] a)
	{
		double[][] clone = new double[a.length][a[0].length];
		for(int i=0; i<a.length; i++)
		{
			for(int j=0; j<a[i].length; j++)
			{
				clone[i][j] = a[i][j];
			}
		}
		return clone;
	}
	
	public double transitionFunction(String agentAction, String preyAction)
	{
		if( this.agent.pos.equals(this.prey.pos) )	// no return if the prey is already caught
			return 0;
		
		State newState = new State(this);
		newState.agent.moveAccordingToAction(agentAction);
		Point nextPreyPoint = State.nextTo(newState.prey.pos, preyAction);
		Map<Point, Double> validMovesHash= prey.getValidMoves(this);
		if( validMovesHash.containsKey(nextPreyPoint) )
			return validMovesHash.get(nextPreyPoint);
		return 0;
	}
	
	public double rewardFunction(String agentAction, String preyAction)
	{
		if( this.agent.pos.equals(this.prey.pos) )	// no return if the prey is already caught
			return 0;
		Point nextPredatorPoint = State.nextTo(this.agent.pos, agentAction);
		Point nextPreyPoint = State.nextTo(this.prey.pos, preyAction);
		if( nextPredatorPoint.equals(nextPreyPoint) )
			return 10;	// reward for catching the prey
		return 0;	
	}

	Predator agent;
	Prey prey;


	public State()
	{
		this.agent = new Predator(new Point(0,0));
		prey = new Prey(new Point(5,5));
	}

	public State(State environment)
	{
		this.agent = new Predator(environment.agent);
		this.prey = new Prey(environment.prey); 
	}
	
	public State(Point agentPos, Point preyPos)
	{
		this.agent = new Predator(agentPos);
		this.prey = new Prey(preyPos);
	}

	public int run()
	{
		//the amount of iterations
		int time = 0;
		while(!preyCaught()){
			//move prey
			prey.doAction(this);
			//move predator
			agent.doAction(this);
			time++;
			if(print == true)
				System.out.print(toString());
		}
		return time;
	}

	public boolean preyCaught(){
		return agent.pos.equals(prey.pos);
	}

	public static Point nextTo(Point pos, String action)
	{
		Point newP = new Point(pos);
		if( action.equals("N") )
		{
			newP.y--;
			if(newP.y<0)
				newP.y=10;
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
				newP.x=10;
		}
		return newP;
	}

	public String toString()
	{
		String s = "";
		s += prey.toString() + "\n";
		s += agent.toString() + "\n";
		return s;
	}

	public static void printArray(double[][] grid)
	{
		for (int i =0; i < grid.length; i++) 
		{
			for (int j = 0; j < grid[i].length; j++) 
			{
				System.out.printf("%.2f ", grid[i][j]);
			}
			System.out.println("");
		}
		
	}

}
