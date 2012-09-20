
public class StatePolicy 
{
	double N;
	double E;
	double S;
	double W;
	double WAIT;
	
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
		if( WAIT>0 )
			s+="X";
		return s;
	}
}
