import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 * 
 * StatePolicy contains the probability of performing action
 * N, E, S, W and WAIT.
 */

public class StatePolicy 
{
	double N;
	double E;
	double S;
	double W;
	double WAIT;
	
	// constructors
	public StatePolicy(StatePolicy statePolicy)
	{
		this.N = statePolicy.N;
		this.E = statePolicy.E;
		this.S = statePolicy.S;
		this.W = statePolicy.W;
		this.WAIT = statePolicy.WAIT;
	}
	public StatePolicy()
	{
		this.N = 0.2;
		this.E = 0.2;
		this.S = 0.2;
		this.W = 0.2;
		this.WAIT = 0.2;
		
	}
	public StatePolicy(double N, double E, double S, double W, double WAIT)
	{
		this.N = N;
		this.E = E;
		this.S = S;
		this.W = W;
		this.WAIT = WAIT;
	}
	public StatePolicy(double[] probs)
	{
		this.N = probs[0];
		this.E = probs[1];
		this.S = probs[2];
		this.W = probs[3];
		this.WAIT = probs[4];
	}//end constructors
	
	public String getMax()
	{
		double bestValue = 0;
		double[] actionValues = {N, E, S, W, WAIT};
		String[] actions = {"N", "E", "S", "W", "WAIT"};
		List<String> bestActions = new ArrayList<String>();
		for(int i=0;i<actions.length;i++){
			if(actionValues[i] > bestValue)
			{
				bestValue = actionValues[i];
				bestActions = new ArrayList<String>();
				bestActions.add(actions[i]);
			}
			else if(actionValues[i] == bestValue)
			{
				bestActions.add(actions[i]);
			}
		}
		Random generator = new Random();
		return bestActions.get(generator.nextInt(bestActions.size()));
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if( this==obj )
			return true;
		
		if( obj == null || getClass() != obj.getClass() ) 
			return false;
		
		StatePolicy statePolicy = (StatePolicy) obj;
		
		if( statePolicy.toString().equals(this.toString()))
			return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		//return getMax();
		String s = "";
		if( N>0 )
			s+="N"+this.N+"\t";
		if( E>0 )
			s+="E"+this.E+"\t";
		if( S>0 )
			s+="S"+this.S+"\t";
		if( W>0 )
			s+="W"+this.W+"\t";
		if( WAIT>0 )	// the action 'WAIT' is printed as 'X'.
			s+="X"+this.WAIT+"\t";
		return s;
	}
	

}//end class StatePolicy
