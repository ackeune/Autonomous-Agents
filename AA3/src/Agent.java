/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 * 
 * Agent has an index, state-action values (Q-values) and a policy.
 * If index<0, then agent is a prey, else agent is a predator.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Agent 
{
	final int index;	// index of agent in list of relative-distances. index<0 indicates prey
	Map<StateActionPair, Double> qValues;	// state-action values
	Policy policy;
	
	//constructors
	public Agent(Agent agent)
	{
		this(agent.index, agent.qValues, agent.policy);
	}
	public Agent(int index)
	{
		this(index, new HashMap<StateActionPair, Double>(), new Policy());
	}
	public Agent(int index, Map<StateActionPair, Double> qValues, Policy policy)
	{
		this.index = index;
		this.qValues = new HashMap<StateActionPair, Double>(qValues);
		this.policy = new Policy(policy);
	}//end constructors
	
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
	public void updateQValue(State oldState, State state, String action, double alpha,
			double gamma, double initialValue)
	{
		//StateActionPair testSap = new StateActionPair(new State(state.relativeDistances.length), "N");
		
		StateActionPair oldSap = new StateActionPair(oldState, action);
		/*if( oldSap.equals(testSap) && this.index == -1 && this.qValues.size() > 605 )
			System.out.println();*/
		double oldQ = getStateActionValue(oldSap, initialValue);
		
		List<String> actions = getValidActions();
		double bestQValue = 0;
		for(int i=0; i<actions.size(); i++)
		{
			StateActionPair newSap = new StateActionPair(state, actions.get(i));
			double newQ = getStateActionValue(newSap, initialValue);
			if( newQ > bestQValue )
				bestQValue = newQ;
		}
		double reward = state.getReward(index);
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
		if( sap.state.confusion() || sap.state.preyCaught() )
			return 0;	// if predators are confused or prey is caught
		return (qValues.containsKey(sap))?
				qValues.get(sap):initialValue;
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
		List<String> actions = getValidActions();
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
			actions = getValidActions();
			actions.remove(bestAction);	// don't choose the best action
			action = actions.get(generator.nextInt(actions.size()));
			return action;
		}
		return bestAction;	// choose best action
	}
	
	/* TODO should policy return first best action or random best action?
	 * 
	 * public String policyAction(State state)
	{
		return policy.getFirstBestAction(state);
		return policy.getRandomBestAction(state);
	}*/
	
	/**
	 * Return a random action.
	 * @return random action.
	 */
	public String randomAction()
	{
		List<String> actions = getValidActions();
		return actions.get(new Random().nextInt(actions.size()));
	}
	
	/**
	 * Give list of possible actions.
	 * @return valid actions
	 */
	public List<String> getValidActions()
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
	
	@Override
	public String toString()
	{
		return String.format("Index:%d", index);
	}

}//end class Agent
