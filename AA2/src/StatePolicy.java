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
		String bestAction = "";
		if( this.N > bestValue )
		{
			bestValue = this.N;
			bestAction = "N";
		}	
		else if(this.E > bestValue)
		{
			bestValue = this.E;
			bestAction = "E";
		}
		else if(this.S > bestValue)
		{
			bestValue = this.S;
			bestAction = "S";
		}
		else if(this.W > bestValue)
		{
			bestValue = this.W;
			bestAction = "W";
		}	
		else if(this.WAIT > bestValue)
		{
			bestValue = this.WAIT;
			bestAction = "WAIT";
		}
		return bestAction;
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
		String s = "";
		if( N>0 )
			s+="N";
		if( E>0 )
			s+="E";
		if( S>0 )
			s+="S";
		if( W>0 )
			s+="W";
		if( WAIT>0 )	// the action 'WAIT' is printed as 'X'.
			s+="X";
		return s;
	}
	

}//end class StatePolicy
