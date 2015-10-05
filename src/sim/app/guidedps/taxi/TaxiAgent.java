package sim.app.guidedps.taxi;

import java.awt.Point;

import sim.app.guidedps.taxi.State.Action;
import sim.app.guidedps.taxi.World.LocState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;

public abstract class TaxiAgent implements Steppable{

	private static final long serialVersionUID = 1L;
	protected int x;
	protected int y;
	protected boolean pickup;
	protected Taxi model;
	protected TaxiObject passenger;
	protected Action action;
	protected double reward;
	protected int counter;

	
	public String desAction()
	{
		return "This is the action taken last step";
	}
	
	
	public Action getAction()
	{
		return action;
	}
	
	public abstract void resetAgent(int x, int y, LocState passengerState, LocState desState);
	
	
	public String desReward()
	{
		return "This is the reward received last step";
	}
	
	public double getReward()
	{
		return this.reward;
	}
	
	

	public TaxiAgent(Taxi model, TaxiObject passenger)
	{
		this.model = model;
		this.passenger = passenger;
		this.counter = 0;
	}

	
	public void setLocation(int x, int y, SparseGrid2D field)
	{
		this.x = x;
		this.y = y;
		field.setObjectLocation(this,x,y);
		
		// if taxi pick the player, we take it with the taxi
		if(pickup)
		{
			passenger.setLocation(x, y, field);
		}
	}
	
	public Point getLocation()
	{
		return new Point(x, y);
	}
	
	
	public void setPickUp(boolean val)
	{
		this.pickup = val;
	}
	
	public boolean getPickUp()
	{
		return this.pickup;
	}


	public void learning() {
		
	}

	
	

}
