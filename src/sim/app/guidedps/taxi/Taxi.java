package sim.app.guidedps.taxi;

import sim.app.guidedps.gridworld.State;
import sim.app.guidedps.gridworld.Block;

import java.awt.Color;
import java.awt.Point;

import sim.app.guidedps.gridworld.GridModel;
import sim.app.guidedps.gridworld.GridObject;
import sim.app.guidedps.taxi.agents.GuidedPS;
import sim.app.guidedps.taxi.agents.PrioritizedSweeping;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;

public class Taxi extends GridModel {

	private static final long serialVersionUID = 1L;

	public final int height = 5;
	public final int width = 5;

	public TaxiAgent agent;
	public GridObject passenger;
	public GridObject destination;

	public Taxi(long seed) {
		super(seed);

		gridField = new SparseGrid2D(width, height);
		backgroundField = new ObjectGrid2D(width, height);

		// setup the background

		backgroundField.set(0, 0,
				new Block(Color.red, new int[] { 0, 0, 0, 0 }));
		backgroundField.set(1, 0, new Block(null, new int[] { 0, 1, 0, 0 }));
		backgroundField.set(2, 0, new Block(null, new int[] { 0, 0, 0, 1 }));
		backgroundField.set(1, 1, new Block(null, new int[] { 0, 1, 0, 0 }));
		backgroundField.set(2, 1, new Block(null, new int[] { 0, 0, 0, 1 }));
		backgroundField.set(4, 0, new Block(Color.green,
				new int[] { 0, 0, 0, 0 }));
		backgroundField.set(0, 4, new Block(Color.yellow, new int[] { 0, 1, 0,
				0 }));
		backgroundField.set(0, 3, new Block(null, new int[] { 0, 1, 0, 0 }));
		backgroundField.set(1, 3, new Block(null, new int[] { 0, 0, 0, 1 }));
		backgroundField.set(1, 3, new Block(null, new int[] { 0, 0, 0, 1 }));
		backgroundField.set(3, 4, new Block(Color.blue,
				new int[] { 0, 0, 0, 1 }));
		backgroundField.set(2, 4, new Block(null, new int[] { 0, 0, 0, 1 }));
		backgroundField.set(2, 3, new Block(null, new int[] { 0, 1, 0, 0 }));
		backgroundField.set(2, 4, new Block(null, new int[] { 0, 1, 0, 0 }));

		passenger = new GridObject();
		destination = new GridObject();
		world = new World(this);

		this.constructStateList();
		this.numAction = State.Action.values().length;
		initAgents();
	}

	@Override
	protected final void initAgents() {

		// agent = new QAgent(this, passenger);
		// agent = new TaxiAgent(this, passenger, new
		// PrioritizedSweeping(this));
		agent = new TaxiAgent(this, passenger, new GuidedPS(this));
		initGame();

		agentStopper = schedule.scheduleRepeating(0, 0, agent);

	}

	@Override
	public final void initGame() {
		gameTime++;

		gridField.clear();

		// reset the passenger's location and state
		int passengerIndex = random.nextInt(4);
		// int passengerIndex = 0;
		Point loc = World.locationMap.get(passengerIndex);
		passenger.setLocation(loc.x, loc.y, gridField);
		passenger.state = World.LocState.values()[passengerIndex];

		// initial destination's location and state
		int destinationIndex = random.nextInt(4);
		// int destinationIndex = 3;
		loc = World.locationMap.get(destinationIndex);
		destination.setLocation(loc.x, loc.y, gridField);
		destination.state = World.LocState.values()[destinationIndex];

		int x = random.nextInt(4);
		int y = random.nextInt(4);
		// debug setting
		// int x = 0;
		// int y = 1;
		agent.resetAgent(x, y, passenger.state, destination.state);

	}

	private void constructStateList() {
		int counter = 0;
		for (int x = 0; x < 5; ++x) {
			for (int y = 0; y < 5; ++y) {
				for (int d = 0; d < 4; ++d) // des is available in the range 0
											// to 3
				{
					for (int p = 0; p < 5; ++p) // p is available in the range 0
												// to 4
					{
						State state = new State(x, y, d, p);
						stateList.add(state);
						stateMap.put(state, counter++);
					}
				}
			}
		}
		this.numState = stateList.size(); 

	}

	public State getCurrentState() {
		return new State(agent.getLocation().x, agent.getLocation().y,
				destination.state, passenger.state);
	}

	public static void main(String[] args) {
		doLoop(Taxi.class, args);
		System.exit(0);
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

}
