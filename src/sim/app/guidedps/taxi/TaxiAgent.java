package sim.app.guidedps.taxi;

import java.awt.Point;
import sim.app.guidedps.gridworld.GridAgent;
import sim.app.guidedps.gridworld.GridModel;
import sim.app.guidedps.gridworld.GridObject;
import sim.app.guidedps.gridworld.State;

import sim.app.guidedps.gridworld.State.Action;
import sim.app.guidedps.taxi.World.LocState;
import sim.app.guidedps.taxi.agents.LearningAgent;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;

public class TaxiAgent extends GridAgent{

	private static final long serialVersionUID = 1L;
	protected GridObject passenger;
	

	public TaxiAgent(GridModel model, GridObject passenger, LearningAgent agent)
	{
		this.model = model;
		this.passenger = passenger;
		this.counter = 0;
                this.agent = agent;
	}
        
        public void setLocation(int x, int y, SparseGrid2D field)
	{
		super.setLocation(x,y,field);
		// if taxi pick the player, we take it with the taxi
		if(pickup)
		{
			passenger.setLocation(x, y, field);
		}
	}
        
        public void setPickUp(boolean val)
	{
		this.pickup = val;
	}
	
	public boolean getPickUp()
	{
		return this.pickup;
	}

	public void resetAgent(int x, int y, LocState passengerState,
			LocState desState) {
		agent.setState(new State(x, y, passengerState, desState));
		this.setPickUp(false);
		this.setLocation(x, y, model.gridField);
	}

}
