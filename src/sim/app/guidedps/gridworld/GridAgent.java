package sim.app.guidedps.gridworld;

import java.awt.Point;

import sim.app.guidedps.gridworld.State.Action;
import sim.app.guidedps.maze.Maze;
import sim.app.guidedps.taxi.World.LocState;
import sim.app.guidedps.taxi.agents.LearningAgent;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;

public abstract class GridAgent implements Steppable {

	private static final long serialVersionUID = 1L;
	protected int x;
	protected int y;
	protected boolean pickup;
	protected GridModel model;
	protected Action action;

	protected int counter;

	protected LearningAgent agent;

	@Override
	public void step(SimState state) {
		if (model.isTraining())
			counter++;

		learning();

		// if one episode is ended, we start a new one
		if (model.world.isEndGame()) {
			model.gameStartIteration = model.gameEndIteratioin;
			model.gameEndIteratioin = counter;
			model.initGame();
			model.world.setEndGame(false);
		}

		// run out of iteration, stop the agent and world, reset the counter
		if (counter >= model.stepBounds) {
			model.agentStopper.stop();

			counter = 0;
			System.out.println("==========================");
			System.out.println("end of the training period");
			System.out.println("==========================");
			return;
		}

	}

	public void learning() {
		// action selection
		this.action = agent.ActionSelection();

		// print out the state
		// System.out.println("state:");
		// System.out.println("the position of taxi:("+x+","+y+")");
		// System.out.println("the state of passenger:"+passenger.state);
		// System.out.println("the state of destination:"+model.destination.state);
		// System.out.println(qTableForState(qTable.get(s)));
		// System.out.println("action is "+ this.action);

		// update the world, determine s' and reward
		model.world.update();

		// update the policy
		if (model.isTraining())
			agent.updatePolicy();
		
		if(model instanceof Maze)
		{
			((Maze)model).updateValueGrid();
			((Maze)model).updateSignalGrid();
		}

	}

	public String desAction() {
		return "This is the action taken last step";
	}

	public Action getAction() {
		return action;
	}

	public void setLocation(int x, int y, SparseGrid2D field) {
		this.x = x;
		this.y = y;
		field.setObjectLocation(this, x, y);
	}

	public Point getLocation() {
		return new Point(x, y);
	}

	public LearningAgent getLearningAgent() {
		return this.agent;
	}
	
}
