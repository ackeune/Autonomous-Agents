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

	Policy policy;
	Map<StateActionPair, Double> stateActionValues;	//TODO change to double array of StateActionPairs

	// constructors
	public Predator(Point stateSize)
	{
		this.policy = new Policy(stateSize);
		this.stateActionValues = new HashMap<StateActionPair, Double>();
	}
	public Predator(Point stateSize, 
			Map<StateActionPair, Double> stateActionValues)
	{
		this.policy = new Policy(stateSize);
		this.stateActionValues = stateActionValues;
	}
	public Predator(Predator p)
	{
		this.policy = new Policy(p.policy);
		this.stateActionValues = new HashMap<StateActionPair, Double>(p.stateActionValues);
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
		validMoves.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "N"));
		validMoves.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "E"));
		validMoves.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "S"));
		validMoves.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "W"));
		validMoves.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "WAIT"));
		//move to a random position
		environment.relativeDistance = validMoves.get(generator.nextInt(validMoves.size()));
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
	 * Change the relative distance according to the action.
	 * @param action
	 * @param environment
	 */
	public void moveAccordingToAction(String action, State environment)
	{
		environment.relativeDistance = environment.nextRelativeDistancePredator(environment.relativeDistance, action); 
	}
	
	
	
	/**
	 * Perform an iteration of q-learning. Take an action, observe the reward
	 * and update the q-value.
	 * @param environment	s
	 * @param alpha			learning rate
	 * @param gamma			discount factor
	 * @param epsilon		e-greedy probability
	 * @param initialValue  initial value for all state-action pairs
	 */
	public void qLearnIteration(State environment, double alpha, double gamma, double epsilon,
			double initialValue)
	{
		String action = eGreedyAction(environment, epsilon, initialValue);
		State oldEnvironment = new State(environment);
		moveAccordingToAction(action, environment);	// take action
		double reward = 0;
		if( environment.preyCaught() )	// observe reward
			reward = 10;
		updateQValue(oldEnvironment, environment, action, reward, alpha, gamma, initialValue);
	}
	
	/**
	 * Update the q-value
	 * @param oldEnvironment s
	 * @param environment	 s'
	 * @param action		 
	 * @param reward
	 * @param alpha			learning rate
	 * @param gamma			discount factor
	 * @param initialValue	initial value for all state-action pairs
	 */
	public void updateQValue(State oldEnvironment, State environment, String action, double reward, double alpha,
			double gamma, double initialValue)
	{
		StateActionPair oldSap = new StateActionPair(oldEnvironment, action);
		double oldQ = getStateActionValue(oldSap, initialValue);
		
		List<String> actions = getValidActions(environment);
		double bestQValue = 0;
		for(int i=0; i<actions.size(); i++)
		{
			StateActionPair newSap = new StateActionPair(environment, actions.get(i));
			double newQ = getStateActionValue(newSap, initialValue);
			if( newQ > bestQValue )
			{
				bestQValue = newQ;
			}
		}
		double updatedValue = oldQ + alpha*(reward + gamma*bestQValue - oldQ);
		stateActionValues.put(oldSap, updatedValue);	// update qValue of State-action pair
	}

	/**
	 * Get the value of a state action pair. If state action pair is not known
	 * then 'initialValue' is returned.
	 * @param sap	State action pair
	 * @param initialValue	initial value of all state-action pairs
	 * @return value of state action pair
	 */
	public double getStateActionValue(StateActionPair sap, double initialValue){
		return (stateActionValues.containsKey(sap))?
				stateActionValues.get(sap):initialValue;
	}
	
	/**
	 * Get an action according to e-greedy. The greedy action is chosen with probability
	 * 1-e. The other actions get equal probability.
	 * @param environment
	 * @param epsilon		e-greedy probability
	 * @param initialValue	initial value of all state-action pairs
	 * @return e-greedy action
	 */
	public String eGreedyAction(State environment, double epsilon, double initialValue)
	{
		Random generator = new Random();
		String action;
		// find best action
		List<String> actions = getValidActions(environment);
		String bestAction = "";
		double bestValue = 0;
		for(int i=0; i<actions.size(); i++)
		{
			StateActionPair sap = new StateActionPair(environment, actions.get(i));
			double qValue = getStateActionValue(sap, initialValue);
			if( qValue > bestValue )
			{
				bestAction = sap.action;
				bestValue = qValue;
			}
		}
		
		if( generator.nextDouble() < epsilon )	//  choose random action
		{
			actions = getValidActions(environment);
			actions.remove(bestAction);	// don't choose the best action
			action = actions.get(generator.nextInt(actions.size()));
			return action;
		}
		return bestAction;	// choose best action
	}
	
	/**
	 * Returns list of neighbour positions.
	 * @param environment
	 * @param pos
	 * @return neighbours
	 */
	public static List<Point> getNeighbours(State environment, Point pos)
	{
		List<Point> neighbours = new ArrayList<Point>();
		neighbours.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "N"));
		neighbours.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "E"));
		neighbours.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "S"));
		neighbours.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "W"));
		neighbours.add(environment.nextRelativeDistancePredator(environment.relativeDistance, "WAIT"));
		
		return neighbours;
	}
	
	/**
	 * Give list of valid actions.
	 * @param environment
	 * @return valid actions
	 */
	public List<String> getValidActions(State environment)
	{
		List<String> actions = new ArrayList<String>();
		actions.add("N"); 
		actions.add("E");
		actions.add("S");
		actions.add("W");
		actions.add("WAIT");
		return actions;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		Predator predator = (Predator) o;

		if( predator.policy.equals(this.policy) &&
				predator.stateActionValues.equals(this.stateActionValues) )
			return true;	
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return policy.hashCode() + stateActionValues.hashCode();
	}


}//end class Predator
