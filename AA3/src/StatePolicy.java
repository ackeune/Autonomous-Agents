import java.util.ArrayList;
import java.util.List;

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
	public StatePolicy()	// random state policy
	{
		this(0.2, 0.2, 0.2, 0.2, 0.2);
	}
	public StatePolicy(StatePolicy statePolicy)
	{
		this(statePolicy.N, statePolicy.E, statePolicy.S, statePolicy.W, statePolicy.WAIT);
	}
	public StatePolicy(double[] probs)
	{
		this(probs[0], probs[1], probs[2], probs[3], probs[4]);
	}
	public StatePolicy(double N, double E, double S, double W, double WAIT)
	{
		this.N = N;
		this.E = E;
		this.S = S;
		this.W = W;
		this.WAIT = WAIT;
	}//end constructors
	
	/**
	 * Return set of actions that have the highest value.
	 * @return best actions
	 */
	public List<String> getBestActions()
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
		return bestActions;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if( this==obj )
			return true;
		
		if( obj == null || getClass() != obj.getClass() ) 
			return false;
		
		StatePolicy statePolicy = (StatePolicy) obj;
		
		if( statePolicy.N==this.N && statePolicy.E==this.E &&
				statePolicy.S==this.S && statePolicy.W==this.W &&
				statePolicy.WAIT==this.WAIT )
			return true;
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return String.format("%f%f%f%f%f", N, E, S, W, WAIT).hashCode();
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
