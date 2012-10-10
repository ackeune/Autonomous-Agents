import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Agent 
{
	int index;	// index of agent in list of relative-distances
	Map<StateActionPair, Double> qValues;	// state-action values
	
	//constructors
	public Agent(Agent agent)
	{
		this(agent.index, agent.qValues);
	}
	public Agent(int index)
	{
		this(index, new HashMap<StateActionPair, Double>());
	}
	public Agent(int index, Map<StateActionPair, Double> qValues)
	{
		this.index = index;
		this.qValues = new HashMap<StateActionPair, Double>(qValues);
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
	 * Change the relative distance according to the action.
	 * @param action
	 * @param environment
	 */
	public void moveAccordingToAction(String action, State state)
	{
		if( index<0 )
			state.relativeDistances = state.nextRelativeDistancePrey(action);
		else
			state.relativeDistances[index] = state.nextRelativeDistancePredator(state.relativeDistances[index], action);
	}
	
	/**
	 * Perform an iteration of q-learning. Take an action, observe the reward
	 * and update the q-value. Actions are chosen using e-greedy.
	 * @param environment	s
	 * @param alpha			learning rate
	 * @param gamma			discount factor
	 * @param epsilon		e-greedy probability
	 * @param initialValue  initial value for all state-action pairs
	 */
	public void qLearnIterationEGreedy(State state, double alpha, double gamma, double epsilon,
			double initialValue)
	{
		String action = eGreedyAction(state, epsilon, initialValue);
		State oldEnvironment = new State(state);
		moveAccordingToAction(action, state);	// take action
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
				bestQValue = newQ;
		}
		double updatedValue = oldQ + alpha*(reward + gamma*bestQValue - oldQ);
		qValues.put(oldSap, updatedValue);	// update qValue of State-action pair
	}
	
	/**
	 * Get the value of a state action pair. If state action pair is not known
	 * then 'initialValue' is returned.
	 * @param sap	State action pair
	 * @param initialValue	initial value of all state-action pairs
	 * @return value of state action pair
	 */
	public double getStateActionValue(StateActionPair sap, double initialValue){
		if( sap.state.relativeDistance.equals(new Point(0,0)) )
			return 0;	// if prey is caught
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
	public String eGreedyAction(State state, double epsilon, double initialValue)
	{
		Random generator = new Random();
		String action;
		// find best action
		List<String> actions = getValidActions(state);
		String bestAction = "";
		double bestValue = 0;
		for(int i=0; i<actions.size(); i++)
		{
			StateActionPair sap = new StateActionPair(state, actions.get(i));
			double qValue = getStateActionValue(sap, initialValue);
			if( qValue > bestValue )
			{
				bestAction = sap.action;
				bestValue = qValue;
			}
		}
		
		if( generator.nextDouble() < epsilon )	//  choose random action
		{
			actions = getValidActions(state);
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

		Agent agent = (Agent) o;

		if( agent.qValues.equals(this.qValues) )
			return true;	
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return qValues.hashCode();
	}

}
