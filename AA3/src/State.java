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
 * The object State contains the agent, prey, stateSize and relative distance between agent and prey.
 * 	  
 */



public interface State 
{	
	/**
	 * Checks whether a predator has caught the prey.
	 * @return true if the relative distance between a predator and prey is 0,0.
	 */
	public boolean preyCaught();
	
	/**
	 * Checks whether there is confusion, i.e. 2 or more predators occupy the same position.
	 * @return true if at least 2 predators have the same relative distance to the prey.
	 */
	public boolean confusion();
	
	/**
	 * Get the reward of prey or predator based on the state.
	 * @param index of agent
	 * @return reward of agent with given index
	 */
	public double getReward(int index);

	/**
	 * Change the relative distance according to the action.
	 * @param action
	 * @param index of the agent
	 */
	public void moveAccordingToAction(String action, int index);
	
	/**
	 * Return the amount of predators.
	 * @return predator amount
	 */
	public int getPredatorAmount();
	
	/**
	 * Return a clone of this state
	 * @return clone
	 */
	public State clone();
	
	@Override
	public String toString();

	@Override
	public boolean equals(Object o);
		
	@Override
	public int hashCode();

}//end interface State