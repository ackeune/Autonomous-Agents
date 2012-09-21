/*
 * By:
 * Michael Cabot (6047262), Anna Keune (6056547), 
 * Sander Nugteren (6042023) and Richard Rozeboom (6173292)
 * 
 * Policy contains a grid of StatePolicy where each StatePolicy
 * denotes the actions that should be performed at each position 
 * on the grid.
 */

import java.awt.Point;


public class Policy 
{
	StatePolicy[][] policyGrid;
	
	// constructors
	public Policy(Policy policy)
	{
		this.policyGrid = new StatePolicy[policy.policyGrid.length]
		                                  [policy.policyGrid[0].length];
		for(int i=0; i<policy.policyGrid.length; i++)
		{
			for(int j=0; j<policy.policyGrid[i].length; j++)
			{
				this.policyGrid[i][j] = new StatePolicy(policy.getStatePolicy(new Point(i,j)));
			}
		}
	}
	public Policy(Point stateSize)
	{
		this.policyGrid = new StatePolicy[stateSize.x][stateSize.y];
		for(int i=0; i<stateSize.x; i++)
		{
			for(int j=0; j<stateSize.y; j++)
			{
				policyGrid[i][j] = new StatePolicy();
			}
		}
	}//end constructors
	
	/**
	 * Set a StatePolicy
	 * @param point
	 * @param statePolicy
	 */
	public void setStatePolicy(Point point, StatePolicy statePolicy)
	{
		policyGrid[point.x][point.y] = statePolicy;
	}
	
	/**
	 * Get a StatePolicy
	 * @param point
	 * @return StatePolicy
	 */
	public StatePolicy getStatePolicy(Point point)
	{
		return policyGrid[point.x][point.y];
	}
	
	@Override 
	public boolean equals(Object obj)
	{
		if( this==obj )
			return true;
		
		if( obj == null || getClass() != obj.getClass() ) 
			return false;
		
		Policy policy = (Policy) obj;
		if( policy.toString().equals(this.toString()) )
			return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		for(int i=0; i<policyGrid.length; i++)
		{
			for(int j=0; j<policyGrid[i].length; j++)
			{
				s += String.format("%s\t ", policyGrid[i][j].toString());
			}
			s += "\n";
		}
		return s;
	}
}//end class Policy
