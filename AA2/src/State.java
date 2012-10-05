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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class State {
	boolean print = false;		// for debugging run/0.

	/**
	 * Execute the sub-assignments.
	 * @param args
	 */
	public static void main(String[] args)
	{
		try {
			// Ex1 - Q-learning 
			System.out.println("Ex1 - Q-learning");
			int runs1 = 10;  //skips ex1 if set to 0
			for(int r=0; r<runs1; r++)
			{
				System.out.printf("run:%d\n", r);
				String fileNameEGreedy = String.format("episodeLengths_egreedy%d", r);
				//String fileNameSoftmax = String.format("episodeLengths_softmax%d", r);
				PrintWriter outEGreedy = new PrintWriter(new FileWriter(fileNameEGreedy+".txt"));
				//PrintWriter outSoftmax = new PrintWriter(new FileWriter(fileNameSoftmax+".txt"));

				double[] alphas = {0.1, 0.2, 0.3, 0.4, 0.5};	
				double[] gammas = {0.1, 0.5, 0.7, 0.9};	
				double initialValue = 15;
				int episodes = 100;
				double epsilon = 0.1; 
				//double temperature = 0.1;
				for(int a=0; a<alphas.length; a++)	//loop through alphas
				{
					for(int g=0; g<gammas.length; g++)	//loop through gammas
					{
						System.out.printf("Alpha:%f\tGamma:%f\n", alphas[a], gammas[g]);
						String episodeLengthsEGreedy = qLearningEGreedy(initialValue, episodes, alphas[a], gammas[g], epsilon);
						//String episodeLengthsSoftmax = qLearningSoftmax(initialValue, episodes, alphas[a], gammas[g], temperature);
						// write to files
						outEGreedy.println(episodeLengthsEGreedy);
						//outSoftmax.println(episodeLengthsSoftmax);
						//System.out.println();				
					}
				}
				// close to files
				outEGreedy.close();	
				//outSoftmax.close();
			}

			// Ex2 - Q-learning	different e-values and optimistic initialization
			System.out.println("Ex2 - Q-learning	different e-values and optimistic initialization");
			int runs2 = 10;
			for(int r=0; r<runs2; r++)
			{
				double[] alphas = {0.5};
				double[] gammas = {0.9};
				double[] epsilons = {0.1, 0.3, 0.5, 0.7};
				double[] initialValues = {5, 10, 15, 30};
				int episodes = 100;
				for(int a=0; a<alphas.length; a++)
				{
					for(int g=0; g<gammas.length; g++)
					{
						String fileNameEGreedy = String.format("episodeLengths_egreedy_fixedAlpha%fGamma%f_%d", alphas[a], gammas[g], r);
						PrintWriter outEGreedy = new PrintWriter(new FileWriter(fileNameEGreedy+".txt"));
						for(int e=0; e<epsilons.length; e++)
						{
							for(int i=0; i<initialValues.length; i++)
							{
								System.out.printf("Epsilon:%f\tInitialValue:%f\n", epsilons[e], initialValues[i]);
								String episodeLenghtsEGreedy = qLearningEGreedy(initialValues[i], episodes, alphas[a], gammas[g], epsilons[e]);
								outEGreedy.println(episodeLenghtsEGreedy);
							}//end for initial-values							
						}//end for epsilons
						outEGreedy.close();
					}//end for gammas
				}//end for alphas
			}//end for runs
			
			// Ex3 - compare egreedy with softmax
			System.out.println("Ex3 - compare egreedy with softmax");
			int runsCompareSoftmax = 10;
			for(int r=0; r<runsCompareSoftmax; r++)
			{
				System.out.printf("run:%d\n", r);
				String fileNameCompareSoftmax = String.format("compareEGreedySoftmaxFixedA5G9_%d", r);
				PrintWriter outSoftmax = new PrintWriter(new FileWriter(fileNameCompareSoftmax+".txt"));
				double alpha = 0.5;
				double gamma = 0.9;
				int episodes = 100;
				double[] temperatures = {0.1, 0.3, 0.5, 0.7};
				double initialValue = 15;
				for(int t = 0; t<temperatures.length; t++)
				{
					System.out.printf("Temperature:%f\n", temperatures[t]);
					String episodeLengthsEGreedy = qLearningEGreedy(initialValue, episodes, alpha, gamma, temperatures[t]);
					String episodeLengthsSoftmax = qLearningSoftmax(initialValue, episodes, alpha, gamma, temperatures[t]);
					outSoftmax.println(episodeLengthsEGreedy);
					outSoftmax.println(episodeLengthsSoftmax);
				}
				outSoftmax.close();
			}

			

			// Ex4 qLearningvsSarsa
			System.out.println("Ex4 qLearningvsSarsa");
			int runsSarsa = 10;
			for(int r=0; r<runsSarsa; r++)
			{
				System.out.printf("run:%d\n", r);
				System.out.println(r);
				String fileNameSarsa = String.format("qLearningVsSarsa%d", r);
				PrintWriter outSarsa = new PrintWriter(new FileWriter(fileNameSarsa+".txt"));

				//double[] alphas = {0.1, 0.2, 0.3, 0.4, 0.5};	
				//double[] gammas = {0.1, 0.5, 0.7, 0.9};
				double alpha = 0.5;
				double gamma = 0.9;
				double initialValue = 15;
				int episodes = 100;
				double epsilon = 0.1; 
				String episodeLengthsQLearning = qLearningEGreedy(initialValue, episodes, alpha, gamma, epsilon);
				String episodeLengthsSarsa = sarsa(initialValue, episodes, alpha, gamma, epsilon);
				// write to files
				outSarsa.println(episodeLengthsQLearning);
				outSarsa.println(episodeLengthsSarsa);
				// close to files
				outSarsa.close();	
			}

		} catch (IOException e) {
			e.printStackTrace();
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
	Point relativeDistance;


	// constructors
	public State()
	{
		this.stateSize = new Point(11,11);
		this.agent = new Predator(stateSize);
		this.prey = new Prey();
		this.relativeDistance = new Point(5,5);
	}
	public State(State environment)
	{
		this.agent = new Predator(environment.agent);
		this.prey = new Prey(); 
		this.stateSize = new Point(environment.stateSize);
		this.relativeDistance = new Point(environment.relativeDistance);
	}
	public State(Point relativeDistance, Point stateSize)
	{
		this.agent = new Predator(stateSize);
		this.prey = new Prey();
		this.stateSize = stateSize;
		this.relativeDistance = new Point(relativeDistance);
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

	/**
	 * Perform q-learning
	 * @param initialValue	initial value for all state-action pairs
	 * @param episodes		amount of episodes
	 * @param alpha			learning rate
	 * @param gamma			discount factor
	 * @param epsilon		e-greedy factor
	 * @return	qValues
	 */
	public static String qLearningEGreedy(double initialValue, int episodes,	double alpha, double gamma, double epsilon)
	{	
		String episodeLengths = "";
		State state = new State();	//initialize state
		State stateClone = new State(state);
		for(int i=0; i<episodes; i++)
		{
			state.relativeDistance = new Point(stateClone.relativeDistance);	// reset the relative distance between predator and prey
			int counter = 0;
			while( !state.preyCaught() )
			{
				counter++;
				state.agent.qLearnIterationEGreedy(state, alpha, gamma, epsilon, initialValue);
				state.prey.doAction(state);
			}
			episodeLengths += String.format("%d ", counter);
			//System.out.printf("%d ",counter);	// print episode length			
		}
		return episodeLengths;
	}
	
	public static String qLearningSoftmax(double initialValue, int episodes,	double alpha, double gamma, double temperature)
	{	
		String episodeLengths = "";
		State state = new State();	//initialize state
		State stateClone = new State(state);
		for(int i=0; i<episodes; i++)
		{
			state.relativeDistance = new Point(stateClone.relativeDistance);	// reset the relative distance between predator and prey
			int counter = 0;
			while( !state.preyCaught() )
			{
				counter++;
				state.agent.qLearnIterationSoftMax(state, alpha, gamma, temperature, initialValue);
				state.prey.doAction(state);
			}
			episodeLengths += String.format("%d ", counter);
			//System.out.printf("%d ",counter);	// print episode length			
		}
		return episodeLengths;
	}
	
	/**
	 * Perform sarsa.
	 * @param initialValue	initial value for all state-action pairs
	 * @param episodes		amount of episodes
	 * @param alpha			learning rate
	 * @param gamma			discount factor
	 * @param epsilon		e-greedy factor
	 * @return	string of episode lengths
	 */
	public static String sarsa(double initialValue, int episodes, double alpha, double gamma, double epsilon)
	{	
		String episodeLengths = "";
		State state = new State();	//initialize state
		State stateClone = new State(state);
		for(int i=0; i<episodes; i++)
		{
			state.relativeDistance = new Point(stateClone.relativeDistance);	// reset the relative distance between predator and prey
			int counter = 0;
			String action = state.agent.eGreedyAction(state, epsilon, initialValue); // a
			while( !state.preyCaught() )
			{
				counter++;
				action = state.agent.sarsaIteration(state, alpha, gamma, epsilon, initialValue, action);
				state.prey.doAction(state);
			}
			episodeLengths += String.format("%d ", counter);
			//System.out.printf("%d ",counter);	// print episode length
		}
		return episodeLengths;
	}

	public static void onPolicyMC(double initialValue, int episodes,double epsilon, double gamma)
	{
		//initialization
		State state = new State();
		ArrayList<StateActionPair> sapsEpisode = new ArrayList<StateActionPair>();
		Map<StateActionPair, Double> totalReturns = new HashMap<StateActionPair,Double>();
		Map<StateActionPair, Double> occurences = new HashMap<StateActionPair, Double>();
		
		for(int n = 0; n<episodes; n++)
		{
			//generate episode
			state.relativeDistance.x = -5;
			state.relativeDistance.y = -5;
			sapsEpisode = state.agent.generateEpisode(state);
			//calculate returns
			Map<StateActionPair, Double> currentReturns = new HashMap<StateActionPair,Double>();
			StateActionPair lastSap = sapsEpisode.get(sapsEpisode.size()-1);
			//currentReturns.put(lastSap, 10.0);		
			//double nextReturns = 0;
			//loop through the StateActionPairs backwards to calculate the rewards
			for(int i = sapsEpisode.size()-1; i>=0 ;i--)
			{
				StateActionPair sap = sapsEpisode.get(i);
				//StateActionPair nextSap = sapsEpisode.get(i+1);
				//double nextReturn = currentReturns.get(nextSap);
				double returnValue = Math.pow(gamma,sapsEpisode.size()-(i+1))*10;
				currentReturns.put(sap, returnValue);
			}
			//calculate q values by averaging returns
			for(int i = 0; i<sapsEpisode.size();i++)
			{
				// add currentReturns to totalReturns
				for (StateActionPair key : currentReturns.keySet()) 
				{
				    if(totalReturns.containsKey(key))
				    {	
				    	double value = totalReturns.get(key);
				    	totalReturns.put(key, value+currentReturns.get(key));
				    	occurences.put(key, occurences.get(key)+1.0);
				    }
				    else
				    {
				    	occurences.put(key, 1.0);
				    	totalReturns.put(key, currentReturns.get(key));
				    }
				}

				// average returns -> q
				for (StateActionPair key : totalReturns.keySet()) 
				{
					double value = totalReturns.get(key);
					double average = value/occurences.get(key);
					state.agent.stateActionValues.put(key, average);
				}
				
			}
			
			// for each s in the episode: a* = max_a Q(s,a)
			for(int i=0;i<sapsEpisode.size();i++)
			{
				StateActionPair sap = sapsEpisode.get(i);
				List<String> actions = state.agent.getValidActions(sap.state);
				double bestQValue = 0;
				String bestAction = "";
				
				//find the best Action
				for(int a=0; a<actions.size(); a++)
				{
					StateActionPair newSap = new StateActionPair(sap.state, actions.get(a));
					double newQ = sap.state.agent.getStateActionValue(newSap, initialValue);
					if( newQ > bestQValue )
					{
						bestQValue = newQ;
						bestAction = actions.get(a);
					}
				}
				
				
				//update policy
				int posX = sap.state.relativeDistance.x + 5;
				int posY = sap.state.relativeDistance.y + 5;
				double[] probs = new double[5];
				for(int a=0; a<actions.size(); a++)
				{
					if(actions.get(a) == bestAction)
					{
						probs[a] = 1 - epsilon + (epsilon/actions.size());
						
					}
					else
					{
						probs[a] = (epsilon/actions.size());
					}
					
				}
				StatePolicy statePolicy = new StatePolicy(probs);
				state.agent.policy.setStatePolicy(new Point(posX,posY), statePolicy);
				//System.out.println(statePolicy.N);
			}// end part c
			//System.out.println(state.agent.policy.toString());
		
		}// end episodes
		printQValues(state.agent,15,state.stateSize);
	}//end onPolicyMC
    
	/**
	 * Returns the probability of THIS state changing to a state where
	 * the agent does an action and the prey does an action. 
	 * @param agentAction	The action the agent performs
	 * @param preyAction	The action the prey performs
	 * @return	The probability of reaching the state after the actions have been performed.
	 */
	//TODO check if correct: do agent and prey move at same time?
	// should have input oldState and newState?
	public double transitionFunction(String agentAction, String preyAction)
	{
		if( preyCaught() )	// no return if the prey is already caught
			return 0;
		State newState = new State(this);
		newState.agent.moveAccordingToAction(agentAction, newState);
		if( newState.preyCaught() )	// the prey can't move if the predator catches it.
		{
			return 1;
		}
		Point nextRelativeDist = nextRelativeDistancePrey(relativeDistance, preyAction);
		Map<Point, Double> validMovesHash= prey.getValidMoves(this);
		// the probability of the prey performing an action given the
		// position of the prey
		if( validMovesHash.containsKey(nextRelativeDist) )	
			return validMovesHash.get(nextRelativeDist);	
		return 0;
	}

	/**
	 * Returns the reward of moving to a position.
	 * @param agentAction
	 * @param preyAction
	 * @return 10 if predator catches the prey, else 0.
	 */
	//TODO check if correct: do agent and prey move at same time?
	// should have input oldState and newState?
	public double rewardFunction(String agentAction, String preyAction)
	{
		if( preyCaught() )	// no return if the prey is already caught
			return 0;
		Point nextRelativeDist = this.nextRelativeDistancePredator(relativeDistance, agentAction);
		nextRelativeDist = this.nextRelativeDistancePrey(relativeDistance, agentAction);
		if( nextRelativeDist.equals(new Point(0,0)) )
			return 10;	// reward for catching the prey
		return 0;	
	}

	/**
	 * Checks whether the predator has caught the prey.
	 * @return true if the relative distance between predator and prey is 0,0.
	 */
	public boolean preyCaught(){
		return relativeDistance.equals(new Point(0,0));
	}

	/**
	 * Return the next relative distance according to a move performed by the prey.
	 * This is equal to moving the predator in the opposite direction.
	 * @param relativeDistance between predator and agent
	 * @param action	action performed by prey
	 * @return	new relative distance between predator and prey
	 */
	public Point nextRelativeDistancePrey(Point relativeDistance, String action)
	{
		if( action.equals("N") )
			return nextRelativeDistancePredator(relativeDistance, "S");
		if( action.equals("E") )
			return nextRelativeDistancePredator(relativeDistance, "W");
		if( action.equals("S") )
			return nextRelativeDistancePredator(relativeDistance, "N");
		if( action.equals("W") )
			return nextRelativeDistancePredator(relativeDistance, "E");
		return nextRelativeDistancePredator(relativeDistance, action);
	}

	/**
	 * Return the next relative distance according to a move performed by the predator.
	 * @param relativeDistance	between predator and agent
	 * @param action	performed by predator
	 * @return	new relative distance between predator and prey
	 */
	public Point nextRelativeDistancePredator(Point relativeDistance, String action)
	{
		Point newRelativeDistance = new Point(relativeDistance);
		if( action.equals("N") )
		{
			newRelativeDistance.x--;
			if(newRelativeDistance.x<-stateSize.x/2)
				newRelativeDistance.x = stateSize.x/2;
		}else if( action.equals("E") )
		{
			newRelativeDistance.y++;
			if( newRelativeDistance.y>stateSize.y/2 )
				newRelativeDistance.y = -stateSize.y/2;
		}else if( action.equals("S") )
		{
			newRelativeDistance.x++;
			if(newRelativeDistance.x>stateSize.x/2)
				newRelativeDistance.x = -stateSize.y/2;
		}else if( action.equals("W") )
		{
			newRelativeDistance.y--;
			if(newRelativeDistance.y<-stateSize.y/2)
				newRelativeDistance.y=stateSize.y/2;
		}
		return newRelativeDistance;
	}

	@Override
	public String toString()
	{
		return String.format("StateSize%s\nRelativeDistance%s\n", this.stateSize, this.relativeDistance);
	}

	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		State state = (State) o;

		if( state.stateSize.equals(this.stateSize) && 
				state.relativeDistance.equals(this.relativeDistance) )
			return true;
		return false;
	}
	@Override
	public int hashCode()
	{
		int hash = stateSize.hashCode() + relativeDistance.hashCode();
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
	
	/**
	 * Print q-values.
	 * @param agent			contains the q-values
	 * @param initialValue	initial q-value for all state-action pairs
	 * @param stateSize		size of the state
	 */
	public static void printQValues(Predator agent, double initialValue, 
			Point stateSize)
	{
		String[] actions = {"N", "E", "S", "W", "WAIT"};
		for(int x=-stateSize.x/2; x<stateSize.x/2+1; x++)
		{
			for(int y=-stateSize.y/2; y<stateSize.y/2+1; y++)
			{
				String string = "";
				for(int a=0; a<actions.length; a++)
				{
					StateActionPair sap = 
						new StateActionPair(new State(new Point(x,y), stateSize), actions[a]);
					double value = agent.getStateActionValue(sap, initialValue);
					string += String.format("%.2f|", value);
				}
				System.out.printf("%s\t", string);
			}
			System.out.println();
		}
	}

}//end class State

