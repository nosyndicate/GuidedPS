package sim.app.guidedps.gridworld;


public abstract class Environment{
	

	protected double reward;
	

	public double getReward()
	{
		return reward;
	}


	public abstract void update();
	
	
	
	
}
