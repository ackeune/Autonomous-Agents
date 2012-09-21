/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 * 
 * Predator has a position and policy.
 */

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Predator {

	Point pos;
	Policy policy;

	// constructors
	public Predator(Point pos, Point stateSize)
	{
		this.pos = pos;
		this.policy = new Policy(stateSize);
	}
	public Predator(Predator p)
	{
		this.pos = new Point(p.pos);
		this.policy = new Policy(p.policy);
	}//end constructors

	/**
	 * Move the predator randomly.
	 * @param environment
	 */
	public void doAction(State environment)
	{

		Random generator = new Random();
		//find the positions around the prey
		List<Point> validMoves = new ArrayList<Point>();
		validMoves.add(environment.nextTo(pos, "N"));
		validMoves.add(environment.nextTo(pos, "E"));
		validMoves.add(environment.nextTo(pos, "S"));
		validMoves.add(environment.nextTo(pos, "W"));
		validMoves.add(pos);
		//move to a random position
		this.pos = validMoves.get(generator.nextInt(validMoves.size()));
	}

	/**
	 * Multiplies a double array by a factor.
	 * @param a
	 * @param factor
	 * @return double array
	 */
	public static double[][] times(double[][] a, double factor)
	{
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
				a[i][j]*=factor;
		return a;
	}
	/**
	 * Sums two double arrays.
	 * @param a
	 * @param b
	 * @return double array
	 */
	public static double[][] add(double[][] a, double[][] b)
	{
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
			{
				a[i][j] += b[i][j];
			}
		return a;
	}
	/**
	 * Subtracts two double arrays.
	 * @param a
	 * @param b
	 * @return double array.
	 */
	public static double[][] minus(double[][] a, double[][] b)
	{
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
			{
				a[i][j] -= b[i][j];
			}
		return a;
	}
	/**
	 * Sets all the values of a double array to their absolute values.
	 * @param a
	 * @return double array.
	 */
	public static double[][] abs(double[][] a)
	{
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
			{
				if( a[i][j]<0 )
					a[i][j] = -a[i][j];
			}
		return a;
	}
	/**
	 * Sums the values in a double array.
	 * @param a
	 * @return sum of values.
	 */
	public static double sum(double[][] a)
	{
		double sum = 0;
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
				sum += a[i][j];
		return sum;
	}
	/**
	 * Change to pos according to the action.
	 * @param action
	 * @param environment
	 */
	public void moveAccordingToAction(String action, State environment)
	{
		this.pos = environment.nextTo(pos, action);
	}

	/**
	 * Evaluate the policy of the predator.
	 * @param environment
	 * @param theta		Evaluate until the difference is smaller than theta.
	 * @param gamma		discount factor
	 * @return	the state-values
	 */
	public double[][] policyEvaluation(State environment, double theta, double gamma)
	{
		double delta;
		double[][] grid = new double[11][11];	// initial grid filled with zeros
		String[] actionList = {"N", "E", "S", "W", "WAIT"};

		int counter = 0;
		do{
			counter++;
			
			delta = 0;
			double[][] oldGrid = State.copyArray(grid);
			for(int i =0 ; i<grid.length;i++)	// loop through all states
			{
				for(int j = 0 ;  j <grid[0].length ; j++)
				{		
					double stateValue = 0;		
					
					for(int agentAction=0; agentAction<actionList.length; agentAction++)	//loop through all actions of the agent
					{						
						State hypotheticalEnvironment = new State(new Point(i,j), environment.prey.pos, environment.stateSize);
						Map<Point, Double> validAgentMoves = getValidMoves(hypotheticalEnvironment);
						
						double tempValue = 0;
						for(int preyAction=0; preyAction<actionList.length; preyAction++)	// loop through all s'
						{
							Point nextAgentPos = hypotheticalEnvironment.nextTo(hypotheticalEnvironment.agent.pos, actionList[agentAction]);
							// calculate sum_{s'} [transFunc * (RewardFunc + gamma * V(s'))]
							tempValue += hypotheticalEnvironment.transitionFunction(actionList[agentAction], actionList[preyAction]) * 
								(hypotheticalEnvironment.rewardFunction(actionList[agentAction], actionList[preyAction]) + 
										gamma * grid[nextAgentPos.x][nextAgentPos.y]);
						}
						
						// sum over the probabilities of performing action a in state s
						stateValue += tempValue * validAgentMoves.get(hypotheticalEnvironment.nextTo(hypotheticalEnvironment.agent.pos, actionList[agentAction]));
					}
					grid[i][j] = stateValue;					
				}
			}//end looping through all states
			delta = Math.max(delta, sum(abs(minus(oldGrid, grid))));
			//State.printArray(grid);
			//System.out.println(delta);
		}while( delta > theta );
		System.out.printf("Policy evaluation iterations:%d\tGamma:%f\n", counter, gamma);
		return grid;
	}//end policyEvalutation

	/**
	 * Evaluate the states according to the best actions.
	 * @param environment
	 * @param theta
	 * @param gamma
	 * @return the state-values
	 */
	public double[][] valueIteration(State environment, double theta, double gamma)
	{
		double delta;
		double[][] grid = new double[11][11];	// initial grid filled with zeros
		String[] actionList = {"N", "E", "S", "W", "WAIT"};

		int counter = 0;
		do{
			counter++;
			
			delta = 0;
			double[][] oldGrid = State.copyArray(grid);
			for(int i =0 ; i<grid.length;i++)	// loop through all states
			{
				for(int j = 0 ;  j <grid[0].length ; j++)
				{		
					double bestActionStateValue = 0;	
					
					for(int agentAction=0; agentAction<actionList.length; agentAction++)	//loop through all actions of the agent
					{
						State hypotheticalEnvironment = new State(new Point(i,j), environment.prey.pos, environment.stateSize);
						
						double tempValue = 0;
						for(int preyAction=0; preyAction<actionList.length; preyAction++)	// loop through all s'
						{
							Point nextAgentPos = hypotheticalEnvironment.nextTo(hypotheticalEnvironment.agent.pos, actionList[agentAction]);
							// calculate sum_{s'} [transFunc * (RewardFunc + gamma * V(s'))]
							tempValue += hypotheticalEnvironment.transitionFunction(actionList[agentAction], actionList[preyAction]) * 
								(hypotheticalEnvironment.rewardFunction(actionList[agentAction], actionList[preyAction]) + 
										gamma * grid[nextAgentPos.x][nextAgentPos.y]);
						}
						
						if( tempValue > bestActionStateValue )	// maximize expected reward over the actions
							bestActionStateValue = tempValue;
					}
					grid[i][j] = bestActionStateValue;						
				}
			}//end looping through all states
			delta = Math.max(delta, sum(abs(minus(oldGrid, grid))));
			//State.printArray(grid);
			//System.out.println(delta);
		}while( delta > theta );
		System.out.printf("Value iterations:%d\tGamma:%f\n", counter, gamma);
		return grid;
	}//end valueIteration
	
	/**
	 * Improve the policy of the predator.
	 * @param environment
	 * @param theta		Used in policy evalutation
	 * @param gamma		Used in policy evalutation
	 * @return the state-values
	 */
	public double[][] policyIteration(State environment, double theta, double gamma)
	{
		int counter = 0;
		Policy oldPolicy;
		double grid[][];
		do
		{
			counter++;
			oldPolicy = new Policy(policy);	//clone the policy
			// evaluate policy
			grid = policyEvaluation(environment, theta, gamma);
			//State.printArray(grid);
			// improve policy
			policy = makePolicy(environment, grid);
			//System.out.println(policy);
		}while( !oldPolicy.equals(policy) );
		System.out.printf("Policy iterations:%d\tGamma:%f\n", counter, gamma);
		return grid;
	}
	
	/**
	 * Make a policy given the state-values
	 * @param environment
	 * @param grid	state-values
	 * @return new policy
	 */
	public static Policy makePolicy(State environment, double[][] grid)
	{
		double oldPreyValue = grid[environment.prey.pos.x][environment.prey.pos.y];
		grid[environment.prey.pos.x][environment.prey.pos.y] = 1000;
		Policy policy = new Policy(environment.stateSize);
		for(int i=0; i<grid.length; i++)	// loop through the grid
		{
			for(int j=0; j<grid[i].length; j++)
			{
				//set probability of performing the actions for this state
				policy.setStatePolicy(new Point(i,j), getStatePolicy(environment, new Point(i,j), grid));	
			}
		}		
		grid[environment.prey.pos.x][environment.prey.pos.y] = oldPreyValue;
		return policy;
	}
	
	/**
	 * Get the StatePolicy for a position of the grid. 
	 * The state-values of the neighbour positions are compared and 
	 * the actions that lead to the positions with the highest state-values
	 * get equal probability while the other actions get a probability of 0.
	 * @param environment
	 * @param pos
	 * @param grid	state-values
	 * @return	StatePolicy
	 */
	public static StatePolicy getStatePolicy(State environment, Point pos, double[][] grid)
	{
		List<Point> neighbours = getNeighbours(environment, pos);	//get neighbour positions
		
		boolean[] usedActions = new boolean[neighbours.size()];	// actions that go to pos with highest state-value 
		int usedActionCounter = 0;	// amount of actions that lead to pos with highest state-value
		double bestValue = 0;	// best state-value
		List<Point> bestActions = new ArrayList<Point>();
		for(int i=0; i<neighbours.size(); i++)
		{
			double tempValue = grid[neighbours.get(i).x][neighbours.get(i).y];
			if (tempValue == bestValue )	// add action to best actions
			{
				bestActions.add(neighbours.get(i));
				usedActions[i]=true;
				usedActionCounter++;
			}
			else if (tempValue > bestValue )// reset best actions
			{
				usedActionCounter = 1;
				usedActions = new boolean[neighbours.size()];
				usedActions[i]=true;
				bestValue = tempValue;
				bestActions = new ArrayList<Point>();
				bestActions.add(neighbours.get(i));
			}
		}
		// divide action probability over those going to positions with the highest state-value  
		double[] policyProbs = new double[neighbours.size()];
		double actionProb = 1.0/usedActionCounter;
		for(int i=0; i<policyProbs.length; i++)
		{
			if( usedActions[i] )
				policyProbs[i] = actionProb;
		}
		
		return new StatePolicy(policyProbs); 
	}// end getStatePolicy
	
	/**
	 * Returns list of neighbour positions.
	 * @param environment
	 * @param pos
	 * @return neighbours
	 */
	public static List<Point> getNeighbours(State environment, Point pos)
	{
		List<Point> neighbours = new ArrayList<Point>();
		neighbours.add(environment.nextTo(pos, "N"));
		neighbours.add(environment.nextTo(pos, "E"));
		neighbours.add(environment.nextTo(pos, "S"));
		neighbours.add(environment.nextTo(pos, "W"));
		neighbours.add(environment.nextTo(pos, "WAIT"));
		
		return neighbours;
	}
	
	/**
	 * Gets map of new positions and probability of getting to that position. 
	 * @param environment
	 * @return Map of position-probability pairs.
	 */
	public Map<Point, Double> getValidMoves(State environment)
	{
		Map<Point, Double> hashedMoves = new HashMap<Point, Double>();
		StatePolicy statePolicy = policy.getStatePolicy(environment.agent.pos);
		hashedMoves.put(environment.nextTo(environment.agent.pos, "N"), statePolicy.N);
		hashedMoves.put(environment.nextTo(environment.agent.pos, "E"), statePolicy.E);
		hashedMoves.put(environment.nextTo(environment.agent.pos, "S"), statePolicy.S);
		hashedMoves.put(environment.nextTo(environment.agent.pos, "W"), statePolicy.W);
		hashedMoves.put(environment.nextTo(environment.agent.pos, "WAIT"), statePolicy.WAIT);
		
		return hashedMoves;
	}


	@Override
	public String toString()
	{
		return String.format("Predator(%d,%d)", pos.x, pos.y);
	}

}//end class Predator
