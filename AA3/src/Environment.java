import java.util.Arrays;


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
	
	// constructors
	public Environment(int predatorAmount)
	{
		this.state = new State(predatorAmount);
		if( predatorAmount > 4 )
		{
			System.out.println("PredatorAmount cannot be larger than 4. PredatorAmount is has been set to 4.");
			predatorAmount = 4;
		}
		this.predators = new Agent[predatorAmount];
		for(int i=0; i<predatorAmount; i++)
			predators[i] = new Agent(i);
		this.prey = new Agent(-1);
	}
	public Environment(Environment environment)
	{
		this(environment.state, environment.predators, environment.prey);
	}
	public Environment(State state, Agent[] predators, Agent prey)
	{
		this.state = new State(state);
		this.predators = new Agent[predators.length];
		System.arraycopy(predators, 0, this.predators, 0, predators.length);
		this.prey = new Agent(prey);
	}//end constructors
	
	/**
	 * Perform q-learning using e-greedy action selection.
	 * @param initialValue	initial value for all state-action pairs
	 * @param episodes		amount of episodes
	 * @param alpha			learning rate
	 * @param gamma			discount factor
	 * @param epsilon		e-greedy factor
	 * @return	episode lengths
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
	
	@Override
	public String toString()
	{
		return String.format("%s", this.state);
	}

	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		Environment environment = (Environment) o;

		if( environment.state.equals(this.state) && 
				Arrays.deepEquals(environment.predators, this.predators) &&
				environment.prey.equals(this.prey) )
			return true;
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return state.hashCode() + Arrays.deepHashCode(predators) + prey.hashCode();
	}

}//end class Environment
