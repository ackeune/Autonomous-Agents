/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 * 
 */

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Prey {
	
	/**
	 * Performs a random valid action. 
	 * A action is valid if the prey doesn't move to the predator.
	 * @param environment
	 */
	public void doAction(State environment)
	{
		if( environment.preyCaught() )
			return;
		Random generator = new Random();
		Map<Point, Double> hashedMoves = getValidMoves(environment);
		double randomDouble = generator.nextDouble();
		double sum = 0;
		for( Point key: hashedMoves.keySet() )
		{
			sum += hashedMoves.get(key);
			if( sum > randomDouble )
			{
				environment.relativeDistance = key;
				return;
			}
		}			
	}//end doAction
	
	/**
	 * Returns a hashmap of positions for each valid move and 
	 * the probability of performing each move.
	 * @param environment
	 * @return Map of position probability pairs. 
	 */
	public Map<Point, Double> getValidMoves(State environment)
	{
		//find the positions around the prey
		List<Point> validMoves = new ArrayList<Point>();
		validMoves.add(environment.nextRelativeDistancePrey(environment.relativeDistance, "N"));
		validMoves.add(environment.nextRelativeDistancePrey(environment.relativeDistance, "E"));
		validMoves.add(environment.nextRelativeDistancePrey(environment.relativeDistance, "S"));
		validMoves.add(environment.nextRelativeDistancePrey(environment.relativeDistance, "W"));
		// not allowed to move to the position of the predator
		if( validMoves.contains(new Point(0,0)) )	
			validMoves.remove(new Point(0,0));
		
		double probWait = 1;	// probability of prey remaining at same position
		Map<Point, Double> hashedMoves = new HashMap<Point, Double>();
		hashedMoves.put(new Point(environment.relativeDistance), probWait);	// 0.8 prob of staying at same position
		for(int i=0; i<validMoves.size(); i++)
		{
			// equal probability of moving to valid position
			hashedMoves.put(validMoves.get(i), (1-probWait)/validMoves.size());	
		}			
		return hashedMoves;
	}
	
	/**
	 * Change the position of the prey according to the given action.
	 * @param action	
	 * @param environment	
	 */
	public void moveAccordingToAction(String action, State environment)
	{
		environment.relativeDistance = environment.nextRelativeDistancePrey(environment.relativeDistance, action);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		//Prey prey = (Prey) o;

		return true;	// all prey are created equal
	}
	@Override
	public int hashCode()
	{
		return 0;
	}

}//end class Prey
