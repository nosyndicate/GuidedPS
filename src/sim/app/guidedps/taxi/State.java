package sim.app.guidedps.taxi;

import sim.app.guidedps.taxi.World.LocState;



public class State {

	public int x;
	public int y;
	public LocState destination;
	public LocState passenger;
	

	public enum Action
	{
		NORTH,
		EAST,
		SOUTH,
		WEST,
		PICKUP,
		PUTDOWN
	}
	
	
	public int getActionNum() {
		return Action.values().length;
	}
	
	
	
	public State(int x, int y, LocState d, LocState p)
	{
		this.x = x;
		this.y = y;
		this.destination = d;
		this.passenger = p;
	}
	
	public State(int x, int y, int d, int p)
	{
		this(x, y, LocState.values()[d], LocState.values()[p]);
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		State state = (State)obj;
		if(this.x!=state.x)
			return false;
		if(this.y!=state.y)
			return false;
		if(this.destination!=state.destination)
			return false;
		if(this.passenger!=state.passenger)
			return false;
		
		return true;
	}
	
	
	@Override
	public int hashCode() {
		int i = x;
		i = i*10+y;
		i = i*10+destination.ordinal();
		i = i*10+passenger.ordinal();
		
		return i;
	}
}
