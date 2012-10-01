
public class StateActionPair 
{
	State state;
	String action;
	
	public StateActionPair(State state, String action)
	{
		this.state = state;
		this.action = action;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		StateActionPair sap = (StateActionPair) o;

		if( sap.state.equals(this.state) && sap.action.equals(this.action) )
			return true;
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return state.hashCode()+action.hashCode();
	}
}
