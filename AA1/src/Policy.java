
public class Policy 
{
	double N;
	double E;
	double S;
	double W;
	double WAIT;
	
	public Policy()
	{
		this.N = 0.2;
		this.E = 0.2;
		this.S = 0.2;
		this.W = 0.2;
		this.WAIT = 0.2;
		
	}
	public Policy(double N, double E, double S, double W, double WAIT)
	{
		this.N = N;
		this.E = E;
		this.S = S;
		this.W = W;
		this.WAIT = WAIT;
	}
	
	public Policy(double[] probs)
	{
		this.N = probs[0];
		this.E = probs[1];
		this.S = probs[2];
		this.W = probs[3];
		this.WAIT = probs[4];
	}
	
}
