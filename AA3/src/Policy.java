/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 * 
 * Policy contains a map of StatePolicy where each StatePolicy
 * denotes the actions that should be performed in each state.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Policy 
{
	Map<State, StatePolicy> policies;
	
	// constructors
	public Policy()
	{
		this.policies = new HashMap<State, StatePolicy>();
	}
	public Policy(Policy policy)
	{
		this(policy.policies);
	}
	public Policy(Map<State, StatePolicy> policies)
	{
		this.policies = new HashMap<State, StatePolicy>(policies);
	}//end constructors
	
	/**
	 * Set a StatePolicy. A state policy belonging to the given 
	 * state will be replaces with the new state policy.
	 * @param state
	 * @param statePolicy
	 */
	public void setStatePolicy(State state, StatePolicy statePolicy)
	{
		policies.put(state, statePolicy);
	}
	
	/**
	 * Get a StatePolicy for given state. If no state policy is found
	 * then return a random state policy.
	 * @param state
	 * @return StatePolicy
	 */
	public StatePolicy getStatePolicy(State state)
	{
		return policies.containsKey(state)?policies.get(state):new StatePolicy();
	}
	
	/**
	 * Return the first of the set of best actions.
	 * @param state
	 * @return first best action
	 */
	public String getFirstBestAction(State state)
	{
		List<String> bestActions = getStatePolicy(state).getBestActions();
		return bestActions.get(0);
	}
	
	/**
	 * Return at random an action from the set of best actions.
	 * @param state
	 * @return random best action
	 */
	public String getRandomBestAction(State state)
	{
		List<String> bestActions = getStatePolicy(state).getBestActions();
		Random generator = new Random();
		return bestActions.get(generator.nextInt(bestActions.size()));
	}
	
	@Override 
	public boolean equals(Object obj)
	{
		if( this==obj )
			return true;
		
		if( obj == null || getClass() != obj.getClass() ) 
			return false;
		
		Policy policy = (Policy) obj;
		if( policy.policies.equals(this.policies) )
			return true;
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.policies.hashCode();
	}	
	
}//end class Policy
