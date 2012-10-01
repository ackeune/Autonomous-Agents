/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 * 
 * Main/1 executes the sub-assignments 1,2,4 and 5 of assignment 1.
 * Sub-assignment 3 has not (yet) been implemented.
 * 
 * State contains the static functions: 
 * 		copyArray/1, printArray/1, standardDeviation/2
 * 
 * The object State contains the agent, prey and stateSize.
 * 	  
 */

import java.awt.Point;
import java.util.Map;


public class State {
	boolean print = false;		// for debugging sub-assignment 1

	/**
	 * Execute the sub-assignments 1,2,4 and 5.
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Ex1 - Simulator
		System.out.println("Ex1 - Q-learning");
		double[] alphas = {0.1};//, 0.2, 0.3, 0.4, 0.5};
		double[] gammas = {0.1};//, 0.5, 0.7, 0.9};
		double initialValue = 15;
		int episodes = 100;
		double epsilon = 0.1;
		for(int a=0; a<alphas.length; a++)	//loop through alphas
		{
			for(int g=0; g<gammas.length; g++)	//loop through gammas
			{

				System.out.printf("Episode:%d\nAlpha:%f\nGamma:%f\n", 
						episodes, alphas[a], gammas[g]);
				qLearning(initialValue, episodes,
						alphas[a], gammas[g], epsilon);
				System.out.println();				
			}
		}

	}//end main

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
	 * Copy a double array
	 * @param a	A double array containing doubles.
	 * @return	A copy of the given array 
	 */
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


	// constructors
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
	}//end constructors

	/**
	 * Runs a simulation where both the predator and prey move randomly.
	 * @return The time it takes the predator to catch the prey.
	 */
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

	public static Map<StateActionPair, Double> qLearning(double initialValue, int episodes,
			double alpha, double gamma, double epsilon)
			{	
		State state = new State();	//initialize state
		State stateClone = new State(state);
		for(int i=0; i<episodes; i++)
		{
			state.agent.pos = new Point(stateClone.agent.pos);	// reset the agent
			state.prey = new Prey(stateClone.prey);	// reset the prey
			int counter = 0;
			while( !state.preyCaught() )
			{
				counter++;
				state.agent.qLearnIteration(state, alpha, gamma, epsilon, initialValue);
				state.prey.doAction(state);
			}
			if(counter<5)
				System.out.println();
			System.out.printf("Counter:%d\n",counter);
		}
		return state.agent.stateActionValues;
			}

	/**
	 * Returns the probability of THIS state changing to a state where
	 * the agent does an action and the prey does an action. 
	 * @param agentAction	The action the agent performs
	 * @param preyAction	The action the prey performs
	 * @return	The probability of reaching the state after the actions have been performed.
	 */
	public double transitionFunction(String agentAction, String preyAction)
	{
		if( this.agent.pos.equals(this.prey.pos) )	// no return if the prey is already caught
			return 0;
		State newState = new State(this);
		newState.agent.moveAccordingToAction(agentAction, newState);
		if( newState.agent.pos.equals(newState.prey.pos) )	// the prey can't move if the predator catches it.
		{
			return 1;
		}
		Point nextPreyPoint = nextTo(newState.prey.pos, preyAction);
		Map<Point, Double> validMovesHash= prey.getValidMoves(this);
		// the probability of the prey performing an action given the
		// position of the prey
		if( validMovesHash.containsKey(nextPreyPoint) )	
			return validMovesHash.get(nextPreyPoint);	
		return 0;
	}

	/**
	 * Returns the reward of moving to a position.
	 * @param agentAction
	 * @param preyAction
	 * @return 10 if predator catches the prey, else 0.
	 */
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

	/**
	 * Checks whether the predator has caught the prey.
	 * @return true if predator and prey have the same position
	 */
	public boolean preyCaught(){
		return agent.pos.equals(prey.pos);
	}

	/**
	 * Calculates the position that is reached given the current position, action
	 * and size of the toroidal world. 
	 * E.g: nextTo((0,0), North) gives next point (10,0).
	 * @param pos
	 * @param action
	 * @return the next point
	 */
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
	
	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		State state = (State) o;

		if( state.toString().equals(this.toString()) )
			return true;
		return false;
	}
	@Override
	public int hashCode()
	{
		int hash = this.toString().hashCode();
		return hash;
	}

	/**
	 * Prints a double array.
	 * @param grid	A double array containing doubles.
	 */
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

}//end class State
