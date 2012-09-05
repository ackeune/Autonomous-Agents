import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Predator {
	
	Point pos;
	
	public Predator(Point pos)
	{
		this.pos = pos;
	}
	
	public void doAction(Map environment)
	{

		Random generator = new Random();

		//find the positions around the prey
		List<Point> validMoves = new ArrayList<Point>();
		validMoves.add(Map.nextTo(pos, "N"));
		validMoves.add(Map.nextTo(pos, "E"));
		validMoves.add(Map.nextTo(pos, "S"));
		validMoves.add(Map.nextTo(pos, "W"));
		validMoves.add(pos);
		//move to a random position
		this.pos = validMoves.get(generator.nextInt(validMoves.size()));
	}
	
	public String toString()
	{
		return String.format("Predator(%d,%d)", pos.x, pos.y);
	}

}
