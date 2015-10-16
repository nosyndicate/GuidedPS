package sim.app.guidedps.gridworld;


public abstract class Environment{
	

	protected double reward;
	protected boolean endGame;

	public double getReward()
	{
		return reward;
	}


	public abstract void update();
	
	
	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}


	public boolean isEndGame() {
		return endGame;
	}
}
