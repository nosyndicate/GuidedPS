package sim.app.guidedps.agents;

import sim.app.guidedps.gridworld.State;

public class Demonstration {
	State s;
	int a;
	
	public Demonstration(State s, int a) {
		this.s = s;
		this.a = a;
	}
	
	@Override
	public boolean equals(Object obj) {
		Demonstration that = (Demonstration)obj;
		if(that.s.equals(this.s)&&that.a==this.a)
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return 100*s.hashCode() + a;
	}
}
