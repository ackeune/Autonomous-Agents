/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 * 
 * Prey has a position.
 */

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Prey {
	
	
	Point pos = new Point();
	
	public Prey(Point pos)
	{
		this.pos = pos;
	}
	
	public Prey(Prey prey)
	{
		this.pos = new Point(prey.pos);
	}
	
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
				this.pos = key;
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
		validMoves.add(environment.nextTo(pos, "N"));
		validMoves.add(environment.nextTo(pos, "E"));
		validMoves.add(environment.nextTo(pos, "S"));
		validMoves.add(environment.nextTo(pos, "W"));
		// not allowed to move to the position of the predator
		if( validMoves.contains(environment.agent.pos) )	
			validMoves.remove(environment.agent.pos);
		
		Map<Point, Double> hashedMoves = new HashMap<Point, Double>();
		hashedMoves.put(environment.prey.pos, 0.8);	// 0.8 prob of staying at same position
		for(int i=0; i<validMoves.size(); i++)
		{
			// equal probability of moving to valid position
			hashedMoves.put(validMoves.get(i), 0.2/validMoves.size());	
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
		this.pos = environment.nextTo(pos, action);
	}
	
	@Override
	public String toString()
	{
		return String.format("Prey(%d,%d)", pos.x, pos.y);
	}

}//end class Prey
