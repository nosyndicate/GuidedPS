package sim.app.guidedps.taxi;


public abstract class Environment{
	

	protected double reward;
	

	public double getReward()
	{
		return reward;
	}


	public abstract void update();
	
	
	
	
}
