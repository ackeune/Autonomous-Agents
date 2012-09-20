import java.awt.Point;
import java.util.Map;


public class State {
	boolean print = false;

	public static void main(String[] args)
	{
		System.out.println("Ex1");
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

		System.out.println("\nEx4");
		Point stateSize = new Point(11,11);
		State environment4 = new State();
		double theta = 0;
		double gamma = 0.8;
		double[][] grid = environment4.agent.valueIteration(environment4, theta, gamma); 
		printArray(grid);
		System.out.println(Predator.makePolicy(environment4, grid));
		
		System.out.println("\nEx2");
		State environment2 = new State(new Point(10,10), new Point(0,0), stateSize);
		grid = environment2.agent.policyEvaluation(environment2, theta, gamma); 
		printArray(grid);
		System.out.println(grid[10][10]);
		
		System.out.println("\nEx5");
		State environment5 = new State(new Point(0,0), new Point(5,5), stateSize);
		Policy policy = environment5.agent.policyIteration(environment5, theta, gamma);
		//System.out.println(policy);

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

	Predator agent;
	Prey prey;
	Point stateSize;


	public State()
	{
		this.agent = new Predator(new Point(0,0), new Point(11,11));
		this.prey = new Prey(new Point(5,5));
		this.stateSize = new Point(11,11);
	}

	public State(State environment)
	{
		this.agent = new Predator(environment.agent);
		this.prey = new Prey(environment.prey); 
		this.stateSize = new Point(environment.stateSize);
	}
	
	public State(Point agentPos, Point preyPos, Point stateSize)
	{
		this.agent = new Predator(agentPos, stateSize);
		this.prey = new Prey(preyPos);
		this.stateSize = stateSize;
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
	
	public double transitionFunction(String agentAction, String preyAction)
	{
		if( this.agent.pos.equals(this.prey.pos) )	// no return if the prey is already caught
			return 0;
		State newState = new State(this);
		newState.agent.moveAccordingToAction(agentAction, newState);
		Point nextPreyPoint = nextTo(newState.prey.pos, preyAction);
		Map<Point, Double> validMovesHash= prey.getValidMoves(this);
		if( validMovesHash.containsKey(nextPreyPoint) )
			return validMovesHash.get(nextPreyPoint);
		return 0;
	}
	
	public double rewardFunction(String agentAction, String preyAction)
	{
		if( this.agent.pos.equals(this.prey.pos) )	// no return if the prey is already caught
			return 0;
		Point nextPredatorPoint = nextTo(this.agent.pos, agentAction);
		Point nextPreyPoint = nextTo(this.prey.pos, preyAction);
		if( nextPredatorPoint.equals(nextPreyPoint) )
			return 10;	// reward for catching the prey
		return 0;	
	}

	public boolean preyCaught(){
		return agent.pos.equals(prey.pos);
	}

	public Point nextTo(Point pos, String action)
	{
		Point newP = new Point(pos);
		if( action.equals("N") )
		{
			newP.x--;
			if(newP.x<0)
				newP.x = stateSize.x-1;
		}else if( action.equals("E") )
		{
			newP.y++;
			if(newP.y>stateSize.y-1)
				newP.y=0;
		}else if( action.equals("S") )
		{
			newP.x++;
			if(newP.x>stateSize.x-1)
				newP.x=0;
		}else if( action.equals("W") )
		{
			newP.y--;
			if(newP.y<0)
				newP.y=stateSize.y-1;
		}
		return newP;
	}
	
	@Override
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
				System.out.printf("%.2f\t ", grid[i][j]);
			}
			System.out.println("");
		}
	}

}
