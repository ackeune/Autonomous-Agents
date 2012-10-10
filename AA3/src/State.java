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


public class State 
{
	Point stateSize;
	Point[] relativeDistances;

	// constructors
	public State(int predatorAmount)
	{
		this.stateSize = new Point(11,11);
		if( predatorAmount > 4 )
		{
			System.out.println("PredatorAmount cannot be larger than 4. PredatorAmount is now 4.");
			predatorAmount = 4;
		}
		relativeDistances = new Point[predatorAmount];
		Point[] maxPoints = {new Point(0,0), new Point(10,10), 
				new Point(10,0), new Point(0,10)};
		for(int i=0; i<predatorAmount; i++)
		{
			relativeDistances[i] = maxPoints[i];
		}
	}
	public State(State state)
	{
		this(state.relativeDistances, state.stateSize);
	}
	public State(Point[] relativeDistances, Point stateSize)
	{
		this.stateSize = new Point(stateSize);
		this.relativeDistances = new Point[relativeDistances.length];
		for(int i=0; i<relativeDistances.length; i++)
			relativeDistances[i] = new Point(relativeDistances[i]);
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
	 * @return true if at least 2 predators have the same relative distance.
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
	 * Return the next relative distance according to a move performed by the prey.
	 * This is equal to moving the predator in the opposite direction.
	 * @param relativeDistance between predator and agent
	 * @param action	action performed by prey
	 * @return	new relative distance between predator and prey
	 */
	/*
	 * TODO fix
	 * change relative distances of all predators
	 * output relativeDistance-array
	 * input ""
	 * intput action-array?
	 */
	public Point nextRelativeDistancePrey(String action)
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
		
	}

	/**
	 * Return the next relative distance according to a move performed by the predator.
	 * @param relativeDistance	between predator and agent
	 * @param action	performed by predator
	 * @return	new relative distance between predator and prey
	 */
	/*
	 * TODO fix
	 * output relativeDistance-array
	 * input ""
	 * intput action-array?
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
		return String.format("StateSize%s\nRelativeDistance%s\n", 
				this.stateSize, Arrays.asList(this.relativeDistances));
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
				state.relativeDistances.equals(this.relativeDistances) )
			return true;
		return false;
	}
	@Override
	public int hashCode()
	{
		int hash = stateSize.hashCode() + relativeDistances.hashCode();
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

}//end class State