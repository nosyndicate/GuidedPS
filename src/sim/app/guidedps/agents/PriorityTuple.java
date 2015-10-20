package sim.app.guidedps.agents;



public class PriorityTuple implements Comparable{
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

	@Override
	public int compareTo(Object o) {
		PriorityTuple that = (PriorityTuple)o;
		if(that.priority < this.priority)
			return -1;
		else if(that.priority > this.priority)
			return 1;
		else 
			return 0;
	}
	
	
	
}
