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


public class HiveMind 
{
	final int index;	// index of agent in list of relative-distances. index<0 indicates prey
	Map<StateActionListPair, Double> qValues;	// state-action values
	Policy policy;
	final List<String[]> actionCombosSet;
	
	//constructors
	public HiveMind(HiveMind agent, int predatorAmount)
	{
		this(agent.index, agent.qValues, agent.policy, predatorAmount);
	}
	public HiveMind(int index, int predatorAmount)
	{
		this(index, new HashMap<StateActionListPair, Double>(), new Policy(), predatorAmount);
	}
	public HiveMind(int index, Map<StateActionListPair, Double> qValues, Policy policy, int predatorAmount)
	{
		this.index = index;
		this.qValues = new HashMap<StateActionListPair, Double>(qValues);
		this.policy = new Policy(policy);
		this.actionCombosSet = getActionCombos(predatorAmount);
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
	public void updateQValue(State oldState, State state, String[] actionList, double alpha,
			double gamma, double initialValue)
	{
		//StateActionPair testSap = new StateActionPair(new State(state.relativeDistances.length), "N");
		
		StateActionListPair oldSap = new StateActionListPair(oldState, actionList);
		/*if( oldSap.equals(testSap) && this.index == -1 && this.qValues.size() > 605 )
			System.out.println();*/
		double oldQ = getStateActionListValue(oldSap, initialValue);
		
		List<String[]> actionCombos = new ArrayList<String[]>(actionCombosSet);
		double bestQValue = 0;
		for(int i=0; i<actionCombos.size(); i++)
		{
			StateActionListPair newSap = new StateActionListPair(state, actionCombos.get(i));
			double newQ = getStateActionListValue(newSap, initialValue);
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
	public double getStateActionListValue(StateActionListPair sap, double initialValue){
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
	public String[] eGreedyActionList(State state, double epsilon, double initialValue)
	{
		Random generator = new Random();
		// find best action
		String[] bestActionList = new String[state.getPredatorAmount()];
		double bestValue = 0;
		List<String[]> actionCombos = new ArrayList<String[]>(actionCombosSet);
		for(int i=0; i<actionCombos.size(); i++)
		{
			StateActionListPair sap = new StateActionListPair(state, actionCombos.get(i));
			double qValue = getStateActionListValue(sap, initialValue);
			if( qValue > bestValue )
			{
				bestActionList = sap.actionList;
				bestValue = qValue;
			}
		}
		
		if( generator.nextDouble() < epsilon )	//  choose random action
		{
			actionCombos.remove(bestActionList);	// don't choose the best action
			String[] actionList = actionCombos.get(generator.nextInt(actionCombos.size()));
			return actionList;
		}
		return bestActionList;	// choose best action
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
	
	public List<String[]> getActionCombos(int predatorAmount)
	{
		List<String> actions = getValidActions();
		List<String[]> actionCombos = new ArrayList<String[]>();
		for(int i=0; i<actions.size(); i++)
		{
			String[] newCombo = {actions.get(i)};
			actionCombos.add(newCombo);
		}
		predatorAmount--;
		return getActionCombos(predatorAmount, actionCombos);
	}
	
	public List<String[]> getActionCombos(int predatorAmount, List<String[]> actionCombos)
	{
		if( predatorAmount <=0 )
			return actionCombos;
		
		int currentComboSize = actionCombos.get(0).length;
		List<String> actions = getValidActions();
		List<String[]> newActionCombos = new ArrayList<String[]>();
		for(int i=0; i<actionCombos.size(); i++)
			for(int a=0; a<actions.size(); a++)
			{
				String[] newCombo = new String[currentComboSize+1];
				System.arraycopy(actionCombos.get(i), 0, newCombo, 0, 
						actionCombos.get(i).length);
				newActionCombos.add(newCombo);
				newCombo[newCombo.length-1] = actions.get(a);
			}
		predatorAmount--;
		return getActionCombos(predatorAmount, newActionCombos);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		HiveMind agent = (HiveMind) o;

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

}//end class HiveMind
