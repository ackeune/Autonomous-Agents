
public class StateActionPair 
{
	State state;
	String action;
	
	// constructors
	public StateActionPair(StateActionPair sap)
	{
		this(sap.state, sap.action);
	}
	public StateActionPair(State state, String action)
	{
		this.state = state.clone();
		this.action = new String(action);
	}//end constructors
	
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
		return state.hashCode()*33+action.hashCode()*33;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s %s", state ,action);
	}
	
}
