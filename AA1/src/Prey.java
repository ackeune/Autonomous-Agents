import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	
	
	public void doAction(State environment)
	{
		Random generator = new Random();
		/*if( randomNumber < 0.8 )
		{
			//stay in the same position
			return;
		}else
		{
			Map<Point, > validMoves = getValidMoves(environment);
			if(validMoves.size() == 0){
				return;
			}
			this.pos = validMoves.get(generator.nextInt(validMoves.size()));
		}*/
		
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
	
	public Map<Point, Double> getValidMoves(State environment)
	{
		//find the positions around the prey
		List<Point> validMoves = new ArrayList<Point>();
		//List<String> validActions = new ArrayList<String>();
		validMoves.add(State.nextTo(pos, "N"));
		validMoves.add(State.nextTo(pos, "E"));
		validMoves.add(State.nextTo(pos, "S"));
		validMoves.add(State.nextTo(pos, "W"));
		
		for(int i = 0; i < environment.agents.size(); i++){
			if(validMoves.contains(environment.agents.get(i).pos))
				validMoves.remove(environment.agents.get(i).pos);
		}
		Map<Point, Double> hashedMoves = new HashMap<Point, Double>();
		hashedMoves.put(environment.prey.pos, 0.8);
		for(int i=0; i<validMoves.size(); i++)
		{
			hashedMoves.put(validMoves.get(i), 0.2/validMoves.size());
		}
			
		return hashedMoves;
	}
	
	
	public String toString()
	{
		return String.format("Prey(%d,%d)", pos.x, pos.y);
	}

}
