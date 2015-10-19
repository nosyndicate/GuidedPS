/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.guidedps.maze;

import java.awt.Color;
import java.awt.Point;

import sim.app.guidedps.gridworld.Block;
import sim.app.guidedps.gridworld.GridModel;
import sim.app.guidedps.gridworld.GridObject;
import sim.app.guidedps.gridworld.State;
import sim.app.guidedps.taxi.World;
import sim.app.guidedps.taxi.agents.GuidedPS;
import sim.app.guidedps.taxi.agents.LearningAgent;
import sim.app.guidedps.taxi.agents.PrioritizedSweeping;
import sim.app.guidedps.taxi.agents.QAgent;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;

/**
 * 
 * @author drew
 */
public class Maze extends GridModel {

	private static final long serialVersionUID = 1L;
	public final int height = 23;
	public final int width = 26;

	public MazeAgent agent;
	public GridObject source;
	public GridObject destination;
	public DoubleGrid2D stateValueGrid;
	public DoubleGrid2D signalGrid;

	public Maze(long seed) {
		super(seed);

		gridField = new SparseGrid2D(width, height);
		backgroundField = new ObjectGrid2D(width, height);
		stateValueGrid = new DoubleGrid2D(width, height);
		signalGrid = new DoubleGrid2D(width, height);

		buildBackground();
		
		// source
		backgroundField.set(1, 21, new Block(Color.blue,
				new int[] { 0, 0, 0, 0 }));
		// destination
		backgroundField.set(25, 22, new Block(Color.red,
				new int[] { 0, 0, 0, 0 }));

		source = new GridObject();
		destination = new GridObject();
		world = new MazeWorld(this);

		this.constructStateList();
		this.numAction = 4;
		initAgents();
	}
	
	private void buildVerticalBlocks(int x, int start, int end) {
		for(int i = start;i<=end;++i) {
			backgroundField.set(x, i, new Block(Color.BLACK, new int[] { 1, 1, 1,
					1 }));
		}
	}
	
	private void buildHorizontalBlocks(int y, int start, int end) {
		for(int i = start;i<=end;++i) {
			backgroundField.set(i, y, new Block(Color.BLACK, new int[] { 1, 1, 1,
					1 }));
		}
	}

	private void buildBackground() {
		
		// left bottom
		buildVerticalBlocks(3, 14, 22);
		
		// right bottom
		buildHorizontalBlocks(18, 22, 25);
		
		// right
		buildVerticalBlocks(22, 7, 14);
		
		// right top
		buildVerticalBlocks(18, 3, 12);
		buildHorizontalBlocks(3, 19, 25);
		
		// left top
		buildVerticalBlocks(3, 3, 10);
		buildVerticalBlocks(14, 0, 6);
		buildHorizontalBlocks(6, 4, 13);
		
		// mid bottom
		buildVerticalBlocks(7, 10, 21);
		buildVerticalBlocks(18, 17, 22);
		buildHorizontalBlocks(17, 8, 17);
		
		// mid
		buildVerticalBlocks(14, 10, 12);
		buildHorizontalBlocks(10, 11, 13);

	}

	@Override
	protected final void initAgents() {
		//agent = new MazeAgent(this, new QAgent(this));
		//agent = new MazeAgent(this, new PrioritizedSweeping(this));
		agent = new MazeAgent(this, new GuidedPS(this));
		initGame();
		
		updateValueGrid();

		agentStopper = schedule.scheduleRepeating(0, 0, agent);

	}

	public void updateValueGrid() {
		LearningAgent a = this.agent.getLearningAgent();

		for (int i = 0; i < this.width; ++i) {
			for (int j = 0; j < this.height; ++j) {
				State s = new State(i, j, 0, 0);
				int index = stateMap.get(s);
				this.stateValueGrid.field[i][j] = a.stateValue[index];
			}
		}
	}
	
	public void updateSignalGrid() {
		LearningAgent a = this.agent.getLearningAgent();
		
		for (int i = 0; i < this.width; ++i) {
			for (int j = 0; j < this.height; ++j) {
				State s = new State(i, j, 0, 0);
				int index = stateMap.get(s);
				this.signalGrid.field[i][j] = a.signalValue[index];
			}
		}
	}

	@Override
	public final void initGame() {
		gameTime++;

		gridField.clear();

		// agent is alway start at source (blue square)
		int x = 1;
		int y = 21;
		agent.resetAgent(x, y);

	}

	@Override
	public State getCurrentState() {
		return new State(agent.getLocation().x, agent.getLocation().y, 0, 0);
	}

	private void constructStateList() {
		// just x by y states since source and destination are fixed
		int counter = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				State state = new State(x, y, 0, 0);// using the same state but
													// only caring about x and y
				stateList.add(state);
				stateMap.put(state, counter++);
			}
		}
		this.numState = stateList.size();
	}

}
