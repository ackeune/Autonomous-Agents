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

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class RelativeState implements State
{
	public Point stateSize;
	private Point[] relativeDistances;

	// constructors
	public RelativeState(int predatorAmount)
	{
		this.stateSize = new Point(11,11);
		if( predatorAmount > 4 )
		{
			System.out.println("PredatorAmount cannot be larger than 4. PredatorAmount is has been set to 4.");
			predatorAmount = 4;
		}
		relativeDistances = new Point[predatorAmount];
		Point[] possiblePoints = {new Point(-5,-5), new Point(5,5), 
				new Point(5,-5), new Point(-5,5)};	// array of possible initial predator relative distances
		for(int i=0; i<predatorAmount; i++)
		{
			relativeDistances[i] = possiblePoints[i];
		}
	}
	public RelativeState(RelativeState state)
	{
		this(state.relativeDistances, state.stateSize);
	}
	public RelativeState(Point[] relativeDistances, Point stateSize)
	{
		this.stateSize = new Point(stateSize);
		this.relativeDistances = new Point[relativeDistances.length];
		System.arraycopy(relativeDistances, 0, this.relativeDistances, 0, relativeDistances.length);
	}//end constructors

	/**
	 * Checks whether a predator has caught the prey.
	 * @return true if the relative distance between a predator and prey is 0,0.
	 */
	public boolean preyCaught()
	{
		Point caught = new Point(0,0);
		for(int i=0; i<relativeDistances.length; i++)
			if( relativeDistances[i].equals(caught) )
				return true;
		return false;
	}
	
	/**
	 * Checks whether there is confusion, i.e. 2 or more predators occupy the same position.
	 * @return true if at least 2 predators have the same relative distance to the prey.
	 */
	public boolean confusion()
	{
		Set<Point> relDistSet = new HashSet<Point>();
		for(int i=0; i<relativeDistances.length; i++)
			if( relDistSet.contains(relativeDistances[i]) )
				return true;
			else
				relDistSet.add(relativeDistances[i]);
		return false;
	}
	
	/**
	 * Get the reward of prey or predator based on the state.
	 * @param index of agent
	 * @return reward of agent with given index
	 */
	public double getReward(int index)
	{	
		int reward;
		if( confusion() )
			reward = -10;
		else if( preyCaught() )
			reward = 10;
		else
			return 0;	// no confusion and prey not caught
		if( index < 0 )
			return -1*reward;	// prey reward
		else
			return reward;	// predator reward
	}
	
	/**
	 * Change the relative distance according to the action.
	 * @param action
	 * @param index of the agent
	 */
	public void moveAccordingToAction(String action, int index)
	{
		if( index<0 )
			relativeDistances = nextRelativeDistancePrey(action);
		else
			relativeDistances[index] = nextRelativeDistancePredator(relativeDistances[index], action);
	}

	/**
	 * Return the next relative distances according to a move performed by the prey.
	 * This is equal to moving all predators in the opposite direction.
	 * @param action	action performed by prey
	 * @return	new relative distances between predator and prey
	 */
	private Point[] nextRelativeDistancePrey(String action)
	{
		Point[] newRelativeDistances = new Point[relativeDistances.length];
		for(int i=0; i<relativeDistances.length; i++)
		{
			Point nextRelDistPred;
			if( action.equals("N") )
				nextRelDistPred = nextRelativeDistancePredator(relativeDistances[i], "S");
			else if( action.equals("E") )
				nextRelDistPred = nextRelativeDistancePredator(relativeDistances[i], "W");
			else if( action.equals("S") )
				nextRelDistPred = nextRelativeDistancePredator(relativeDistances[i], "N");
			else if( action.equals("W") )
				nextRelDistPred = nextRelativeDistancePredator(relativeDistances[i], "E");
			else 
				nextRelDistPred = nextRelativeDistancePredator(relativeDistances[i], action);
			
			newRelativeDistances[i] = nextRelDistPred;
		}
		return newRelativeDistances;
	}

	/**
	 * Return the next relative distance according to a move performed by the predator.
	 * @param relativeDistance	between predator and agent
	 * @param action	performed by predator
	 * @return	new relative distance between predator and prey
	 */
	private Point nextRelativeDistancePredator(Point relativeDistance, String action)
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
	
	/**
	 * Return the amount of predators.
	 * @return predator amount
	 */
	public int getPredatorAmount()
	{
		return relativeDistances.length;
	}
	
	/**
	 * Return a clone of this state.
	 */
	public State clone()
	{
		return new RelativeState(getPredatorAmount());
	}

	@Override
	public String toString()
	{
		return String.format("StateSize%s\nRelativeDistances%s", 
				this.stateSize, Arrays.asList(this.relativeDistances));
	}

	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		RelativeState state = (RelativeState) o;

		if( state.stateSize.equals(this.stateSize) && 
				Arrays.deepEquals(state.relativeDistances, this.relativeDistances) )
			return true;
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return stateSize.hashCode() + Arrays.deepHashCode(relativeDistances);
	}

}//end class RelativeState