import java.util.Arrays;


public class StateActionListPair 
{
	State state;
	String[] actionList;
	
	// constructors
	public StateActionListPair(StateActionListPair sap)
	{
		this(sap.state, sap.actionList);
	}
	public StateActionListPair(State state, String[] actions)
	{
		this.state = state.clone();
		this.actionList = new String[actions.length];
		System.arraycopy(actions, 0, this.actionList, 0, actions.length);
	}//end constructors
	
	@Override
	public boolean equals(Object o)
	{
		if( this == o ) 
			return true;
		if( o == null || getClass() != o.getClass() ) 
			return false;

		StateActionListPair sap = (StateActionListPair) o;

		if( sap.state.equals(this.state) && 
				Arrays.deepEquals(sap.actionList, this.actionList) )
			return true;
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return state.hashCode()*33+Arrays.deepHashCode(actionList)*33;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s %s", state, Arrays.asList(actionList));
	}
}
