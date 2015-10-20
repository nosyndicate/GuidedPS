/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.guidedps.gridworld;

import java.util.ArrayList;
import java.util.HashMap;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;

/**
 * 
 * @author drew
 */
public abstract class GridModel extends SimState {

	private static final long serialVersionUID = 1L;

	public int height = 5;
	public int width = 5;
	public SparseGrid2D gridField;
	public ObjectGrid2D backgroundField;
	public ArrayList<State> stateList = new ArrayList<State>();
	public HashMap<State, Integer> stateMap = new HashMap<State, Integer>();

	public int stepBounds = 10000000;
	public Stoppable agentStopper;
	public Environment world;

	protected boolean training = true;
	protected int gameTime = 0;
	public int gameStartIteration = 0;
	public int gameEndIteratioin = 0;
	protected double accumulativeReward = 0;
	protected double averageReward = 0;

	

	protected boolean verbose = true;
	private boolean stochastic = false;
	protected int numState;
	protected int numAction;

	protected abstract void initAgents();

	public abstract void initGame();

	public GridModel(long seed) {
		super(seed);
	}

	public void start() {
		super.start();
		gameTime = 0;
		accumulativeReward = 0;
		averageReward = 0;
		initAgents();
	}

	public double getAverageReward() {
		return averageReward;
	}

	public int getGameTime() {
		return gameTime;
	}

	public int getGameIterations() {
		return gameEndIteratioin - gameStartIteration;
	}

	public boolean isTraining() {
		return training;
	}

	public void setTraining(boolean training) {
		this.training = training;
	}

	public int getStepBounds() {
		return stepBounds;
	}

	public void setStepBounds(int val) {
		this.stepBounds = val;
	}

	public boolean getVerbose() {
		return this.verbose;
	}

	public void setVerbose(boolean val) {
		this.verbose = val;
	}

	public int getNumState() {
		return this.numState;
	}

	public int getNumAction() {
		return this.numAction;
	}

	public boolean isStochastic() {
		return stochastic;
	}

	public void setStochastic(boolean value) {
		this.stochastic = value;
	}
	
	public double getAccumulativeReward() {
		return accumulativeReward;
	}

	public abstract State getCurrentState();

}
