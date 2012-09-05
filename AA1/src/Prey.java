import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Prey {
	
	
	Point pos = new Point();
	
	public Prey(Point pos)
	{
		this.pos = pos;
	}
	
	
	public void doAction(Map environment)
	{
		Random generator = new Random();
		double randomNumber = generator.nextDouble();
		if( randomNumber < 0.8 )
		{
			//stay in the same position
			return;
		}else
		{
			//find the positions around the prey
			List<Point> validMoves = new ArrayList<Point>();
			validMoves.add(Map.nextTo(pos, "N"));
			validMoves.add(Map.nextTo(pos, "E"));
			validMoves.add(Map.nextTo(pos, "S"));
			validMoves.add(Map.nextTo(pos, "W"));
			
			for(int i = 0; i < environment.agents.size(); i++){
				if(validMoves.contains(environment.agents.get(i).pos))
					validMoves.remove(environment.agents.get(i).pos);
			}
			if(validMoves.size() == 0){
				return;
			}
			this.pos = validMoves.get(generator.nextInt(validMoves.size()));
		}
	}
	
	public String toString()
	{
		return String.format("Prey(%d,%d)", pos.x, pos.y);
	}

}
