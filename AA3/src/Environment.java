import java.util.Arrays;
import java.util.Random;


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
	public Environment(int predatorAmount, String stateType)
	{
		if( stateType.equals("RelativeState") ) // choose state representation
			this.state = new RelativeState(predatorAmount);
		else
			this.state = new DiagState(predatorAmount);	
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
		this.state = state.clone();
		this.predators = new Agent[predators.length];
		System.arraycopy(predators, 0, this.predators, 0, predators.length);
		this.prey = new Agent(prey);
	}//end constructors
	
	public int[] randomSimulations(int episodes, double tripProb)
	{
		Random generator = new Random();
		
		int[] episodeLengths = new int[episodes];
		for(int e=0; e<episodes; e++)
		{
			state.reset();
			int counter = 0;
			while( !state.confusion() && !state.preyCaught() )
			{
				counter++;
				for(int p=0; p<predators.length; p++)
				{
					String actionPredator = predators[p].randomAction();
					state.moveAccordingToAction(actionPredator, p);
				}
				String actionPrey = prey.randomAction();
				if( generator.nextDouble() < 1-tripProb )
					state.moveAccordingToAction(actionPrey, -1);
			}//end hunting prey
			episodeLengths[e] = counter;	
		}
		return episodeLengths;
	}
	
	/**
	 * Perform independent q-learning using e-greedy action selection.
	 * @param initialValue	initial value for all state-action pairs
	 * @param episodes		amount of episodes
	 * @param alpha			learning rate
	 * @param gamma			discount factor
	 * @param epsilon		e-greedy factor
	 * @param tripProb		probability that prey trips, causing it to remain at the same position. 
	 * @return	episode lengths
	 */
	public int[][] independentQLearningEGreedy(double initialValue, int episodes, double alpha, double gamma, double epsilon, double tripProb)
	{	
		Random generator = new Random();
		
		int[] episodeLengths = new int[episodes];
		int[] episodeEndings = new int[episodes];
		for(int e=0; e<episodes; e++)
		{
			state.reset();
			int counter = 0;
			while( !state.confusion() && !state.preyCaught() )
			{
				counter++;
				State oldState = state.clone();
				String[] actions = new String[predators.length];
				// agents choose and take actions derived from Q
				for(int p=0; p<predators.length; p++) // predators choose and take actions derived from Q
				{
					
					String action = predators[p].eGreedyAction(state, epsilon, initialValue);
					actions[p] = action;
					state.moveAccordingToAction(action, p);
				}
				String actionPrey = prey.eGreedyAction(state, epsilon, initialValue);	// prey chooses action 
				if( generator.nextDouble() < 1-tripProb )
					state.moveAccordingToAction(actionPrey, -1);
				
				// agents update Q-values
				for(int p=0; p<predators.length; p++) // update Q-values predators
				{					
					predators[p].updateQValue(oldState, state, actions[p], alpha, gamma, initialValue);
				}	
				prey.updateQValue(oldState, state, actionPrey, alpha, gamma, initialValue);	// update Q-values prey
			
			}//end hunting prey
			//System.out.printf("Episode %d \tPrey q-values amount: %d\n", e, prey.qValues.size());
			episodeLengths[e] = counter;
			episodeEndings[e] = state.confusion()?0:1; // 0 if confusion, 1 if prey caught
		}//end episodes		
		//System.out.println(predators[0].qValues.size());
		int[][] info = new int[2][episodeLengths.length];
		info[0] = episodeLengths;
		info[1] = episodeEndings;
		return info;
	}//end independentQLearningEGreedy
	
	public int[][] hiveMindQLearningEGreedy(double initialValue, int episodes, double alpha, double gamma, double epsilon, double tripProb)
	{	
		Random generator = new Random();
		HiveMind hiveMind = new HiveMind(1, state.getPredatorAmount());
		
		int[] episodeLengths = new int[episodes];
		int[] episodeEndings = new int[episodes];
		for(int e=0; e<episodes; e++)
		{
			state.reset();
			int counter = 0;
			while( !state.confusion() && !state.preyCaught() )
			{
				counter++;
				State oldState = state.clone();
				// agents choose and take actions derived from Q
				String[] actionList = hiveMind.eGreedyActionList(state, epsilon, initialValue);
				for(int p=0; p<actionList.length; p++) // predators choose and take actions derived from Q
				{
					state.moveAccordingToAction(actionList[p], p);
				}
				String actionPrey = prey.eGreedyAction(state, epsilon, initialValue);	// prey chooses action 
				if( generator.nextDouble() < 1-tripProb )
					state.moveAccordingToAction(actionPrey, -1);
				
				// agents update Q-values
				hiveMind.updateQValue(oldState, state, actionList, alpha, gamma, initialValue); // update Q-values hive mind
				prey.updateQValue(oldState, state, actionPrey, alpha, gamma, initialValue);	// update Q-values prey
			
			}//end hunting prey
			episodeLengths[e] = counter;
			episodeEndings[e] = state.confusion()?0:1; // 0 if confusion, 1 if prey caught
		}//end episodes		
		int[][] info = new int[2][episodeLengths.length];
		info[0] = episodeLengths;
		info[1] = episodeEndings;
		return info;
	}//end hiveMindQLearningEGreedy
	
	/**
	 * Return the class name of the state space being used.
	 * @return state class name
	 */
	public String getStateName()
	{
		return state.getClass().toString().replaceAll("class ", "");
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
