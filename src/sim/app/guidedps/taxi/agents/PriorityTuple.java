package sim.app.guidedps.taxi.agents;



public class PriorityTuple {
	int stateIndex;
	double priority;
	
	public PriorityTuple(int state, double priority)
	{
		this.stateIndex = state;
		this.priority = priority;
	}
	
	@Override
	public boolean equals(Object obj) {
		PriorityTuple tuple = (PriorityTuple)obj;
		if(tuple.stateIndex==this.stateIndex)
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.stateIndex*1000;
	}
	
}
