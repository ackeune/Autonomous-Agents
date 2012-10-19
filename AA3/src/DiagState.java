import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class DiagState implements State 
{
	private Point stateSize;
	private Point[] relativeDistances;
	private String[] areas;
	
	public DiagState(int predatorAmount)
	{
		this.stateSize = new Point(11,11);
		if( predatorAmount > 4 )
		{
			System.out.println("PredatorAmount cannot be larger than 4. PredatorAmount is has been set to 4.");
			predatorAmount = 4;
		}
		this.relativeDistances = new Point[predatorAmount];
		this.areas = new String[predatorAmount];
		Point[] possiblePoints = {new Point(-5,-5), new Point(5,5), 
				new Point(5,-5), new Point(-5,5)};	// array of possible initial predator relative distances
		String[] possibleAreas = {"NW", "SE", "SW", "NE"}; // the corresponding areas
		for(int i=0; i<predatorAmount; i++)
		{
			this.relativeDistances[i] = possiblePoints[i];
			this.areas[i] = possibleAreas[i];
		}
	}//end constructor
	
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
		{
			relativeDistances = nextRelativeDistancePrey(action);
			for(int i=0; i<areas.length; i++)
				areas[i] = relDistToArea(relativeDistances[i]);
		}
		else
		{
			relativeDistances[index] = nextRelativeDistancePredator(relativeDistances[index], action);
			areas[index] = relDistToArea(relativeDistances[index]);
		}
	}
	
	public String relDistToArea(Point pos)
	{
		if( pos.x==pos.y )	// on diagonal
		{
			if( pos.x<0 && pos.y<0 )
				return "NW";
			else if( pos.x>0 && pos.y>0 )
				return "SE";
			else if( pos.x>0 && pos.y<0 )
				return "SW";
			else if( pos.x<0 && pos.y>0 )
				return "NE";
			else
				return "X";	// on prey
		}else
		{
			if( Math.abs(pos.x) < Math.abs(pos.y) )	// above or below
			{
				if( pos.x<0 )
					return "N";
				else
					return "S";
			}else	// left or right
			{
				if( pos.y<0 )
					return "W";
				else
					return "E";
			}
		}
	}//end relDistToArea

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
		return new DiagState(getPredatorAmount());
	}

	@Override
	public String toString()
	{
		return String.format("StateSize%s\nRelativeDistances%s\nAreas%s", 
				this.stateSize, Arrays.asList(this.relativeDistances), 
				Arrays.asList(this.areas));
	}

	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		DiagState state = (DiagState) o;
		// equal when state size is equal and when predators are in the same areas
		if( state.stateSize.equals(this.stateSize) && 
				Arrays.deepEquals(state.areas, this.areas) ) 
			return true;
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return stateSize.hashCode() + Arrays.deepHashCode(areas);
	}

}
