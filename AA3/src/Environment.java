
/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 */

public class Environment 
{
	State state;
	Agent[] predators;
	Agent prey;
	
	
	public Environment(int predatorAmount)
	{
		this.state = new State(predatorAmount);
		this.predators = new Agent[predatorAmount];
		for(int i=0; i<predatorAmount; i++)
			predators[i] = new Agent(i);
		this.prey = new Agent(-1);
	}
	
	/**
	 * Perform q-learning using e-greedy
	 * @param initialValue	initial value for all state-action pairs
	 * @param episodes		amount of episodes
	 * @param alpha			learning rate
	 * @param gamma			discount factor
	 * @param epsilon		e-greedy factor
	 * @return	qValues
	 */
	public int[] qLearningEGreedy(double initialValue, int episodes, double alpha, double gamma, double epsilon)
	{	
		int[] episodeLengths = new int[episodes];
		for(int e=0; e<episodes; e++)
		{
			state = new State(state.relativeDistances.length);
			int counter = 0;
			while( !state.preyCaught() )
			{
				counter++;
				State oldState = new State(state);
				String[] actions = new String[predators.length];
				for(int p=0; p<predators.length; p++) // choose and take action derived from Q
				{
					
					String action = predators[p].eGreedyAction(state, epsilon, initialValue);
					actions[p] = action;
					predators[p].moveAccordingToAction(action, state);
				}
				for(int p=0; p<predators.length; p++) // update Q-values
				{					
					predators[p].updateQValue(oldState, state, actions[p], alpha, gamma, initialValue);
				}	
			}//end hunting prey
			episodeLengths[e] = counter;			
		}//end episodes
		return episodeLengths;
	}

}//end class Environment
