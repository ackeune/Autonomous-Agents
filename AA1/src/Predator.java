import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Predator {

	Point pos;

	public Predator(Point pos)
	{
		this.pos = pos;
	}
	
	public Predator(Predator p)
	{
		this.pos = new Point(p.pos);
	}

	public void doAction(State environment)
	{

		Random generator = new Random();

		//find the positions around the prey
		List<Point> validMoves = new ArrayList<Point>();
		validMoves.add(State.nextTo(pos, "N"));
		validMoves.add(State.nextTo(pos, "E"));
		validMoves.add(State.nextTo(pos, "S"));
		validMoves.add(State.nextTo(pos, "W"));
		validMoves.add(pos);
		//move to a random position
		this.pos = validMoves.get(generator.nextInt(validMoves.size()));
	}

	public double[][] times(double[][] a, double factor)
	{
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
				a[i][j]*=factor;
		return a;
	}
	public double[][] add(double[][] a, double[][] b)
	{
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
			{
				a[i][j] += b[i][j];
			}
		return a;
	}
	public double[][] minus(double[][] a, double[][] b)
	{
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
			{
				a[i][j] -= b[i][j];
			}
		return a;
	}
	public double[][] abs(double[][] a)
	{
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
			{
				if( a[i][j]<0 )
					a[i][j] = -a[i][j];
			}
		return a;
	}
	public double sum(double[][] a)
	{
		double sum = 0;
		for(int i=0; i<a.length; i++)
			for(int j=0; j<a[i].length; j++)
				sum += a[i][j];
		return sum;
	}

	public void moveAccordingToAction(String action)
	{
		this.pos = State.nextTo(pos, action);
	}

	public double[][] policyEvaluation(State environment)
	{
		double delta;
		double theta = 0.0;
		double gamma = 0.8;
		double[][] grid = new double[11][11];	// initial grid filled with zeros
		String[] actionList = {"N", "E", "S", "W", "WAIT"};

		int counter = 0;
		do{
			counter++;
			
			delta = 0;
			double[][] oldGrid = State.copyArray(grid);
			for(int i =0 ; i<grid.length;i++)	// loop through all states
			{
				for(int j = 0 ;  j <grid[0].length ; j++)
				{		
					double stateValue = 0;	
					
					
					for(int agentAction=0; agentAction<actionList.length; agentAction++)	//loop through all actions of the agent
					{						
						State hypotheticalEnvironment = new State(new Point(i,j), environment.prey.pos);
						Map<Point, Double> validAgentMoves = getValidMoves(hypotheticalEnvironment);
						
						double tempValue = 0;
						for(int preyAction=0; preyAction<actionList.length; preyAction++)	// loop through all s'
						{
							if( i==2 && j==5 && agentAction==1 && preyAction==4)
								System.out.print("");
							Point nextAgentPos = State.nextTo(hypotheticalEnvironment.agent.pos, actionList[agentAction]);
							// calculate sum_{s'} [transFunc * (RewardFunc + gamma * V(s'))]
							tempValue += hypotheticalEnvironment.transitionFunction(actionList[agentAction], actionList[preyAction]) * 
								(hypotheticalEnvironment.rewardFunction(actionList[agentAction], actionList[preyAction]) + 
										gamma * grid[nextAgentPos.x][nextAgentPos.y]);
						}
						
						// sum over the probabilities of performing action a in state s
						stateValue += tempValue * validAgentMoves.get(State.nextTo(hypotheticalEnvironment.agent.pos, actionList[agentAction]));
					}
					grid[i][j] = stateValue;
										
				}
			}//end looping through all states
			delta = Math.max(delta, sum(abs(minus(oldGrid, grid))));
			//State.printArray(grid);
			//System.out.println(delta);
		}while( delta > theta );
		System.out.printf("counter:%d\n", counter);
		return grid;
	}

	public double[][] valueIteration(State environment)
	{
		double delta;
		double theta = 0.1;
		double gamma = 0.8;
		double[][] grid = new double[11][11];	// initial grid filled with zeros
		String[] actionList = {"N", "E", "S", "W", "WAIT"};

		int counter = 0;
		do{
			counter++;
			
			delta = 0;
			double[][] oldGrid = State.copyArray(grid);
			for(int i =0 ; i<grid.length;i++)	// loop through all states
			{
				for(int j = 0 ;  j <grid[0].length ; j++)
				{		
					double bestActionStateValue = 0;	
					
					for(int agentAction=0; agentAction<actionList.length; agentAction++)	//loop through all actions of the agent
					{
						State hypotheticalEnvironment = new State(new Point(i,j), environment.prey.pos);
						
						double tempValue = 0;
						for(int preyAction=0; preyAction<actionList.length; preyAction++)	// loop through all s'
						{
							if( i==2 && j==5 && agentAction==1 && preyAction==4)
								System.out.print("");
							Point nextAgentPos = State.nextTo(hypotheticalEnvironment.agent.pos, actionList[agentAction]);
							// calculate sum_{s'} [transFunc * (RewardFunc + gamma * V(s'))]
							tempValue += hypotheticalEnvironment.transitionFunction(actionList[agentAction], actionList[preyAction]) * 
								(hypotheticalEnvironment.rewardFunction(actionList[agentAction], actionList[preyAction]) + 
										gamma * grid[nextAgentPos.x][nextAgentPos.y]);
						}
						
						if( tempValue > bestActionStateValue )	// maximize expected reward over the actions
							bestActionStateValue = tempValue;
					}
					grid[i][j] = bestActionStateValue;
										
				}
			}//end looping through all states
			delta = Math.max(delta, sum(abs(minus(oldGrid, grid))));
			//State.printArray(grid);
			//System.out.println(delta);
		}while( delta > theta );
		System.out.printf("counter:%d\n", counter);
		return grid;
	}
	
	public Map<Point, Double> getValidMoves(State environment)
	{
		List<Point> validMoves = new ArrayList<Point>();
		validMoves.add(State.nextTo(environment.agent.pos, "N"));
		validMoves.add(State.nextTo(environment.agent.pos, "E"));
		validMoves.add(State.nextTo(environment.agent.pos, "S"));
		validMoves.add(State.nextTo(environment.agent.pos, "W"));
		validMoves.add(State.nextTo(environment.agent.pos, "WAIT"));
		
		Map<Point, Double> hashedMoves = new HashMap<Point, Double>();
		for(int i=0; i<validMoves.size(); i++)
		{
			hashedMoves.put(validMoves.get(i), 1.0/validMoves.size());
		}	
		return hashedMoves;
	}



	public String toString()
	{
		return String.format("Predator(%d,%d)", pos.x, pos.y);
	}

}
