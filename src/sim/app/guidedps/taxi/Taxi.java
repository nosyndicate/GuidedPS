package sim.app.guidedps.taxi;


import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.app.guidedps.taxi.World;
import sim.app.guidedps.taxi.State.Action;
import sim.app.guidedps.taxi.World.LocState;
import sim.app.guidedps.taxi.agents.QAgent;

public class Taxi extends SimState{

	private static final long serialVersionUID = 1L;

	public static final int height = 5;
	public static final int width = 5;
	public SparseGrid2D taxiField = new SparseGrid2D(width, height);
	public ObjectGrid2D backgroundField = new ObjectGrid2D(width, height);
	public ArrayList<State> stateList = new ArrayList<State>();
	public HashMap<State, Integer> stateMap = new HashMap<State,Integer>();
	
	public int stepBounds = 10000000;
	public Stoppable agentStopper;
	public World world;
	public TaxiAgent agent;
	public TaxiObject passenger;
	public TaxiObject destination;
	
	private boolean training = true;
	private int gameTime = 0;
	public int gameStartIteration = 0;
	public int gameEndIteratioin = 0;

	private boolean verbose = true;
	private int numState;
	private int numAction;
	
	public int getGameTime()
	{
		return gameTime;
	}
	
	public int getGameIterations()
	{
		return gameEndIteratioin - gameStartIteration;
	}
	
	
	public boolean isTraining() {
		return training;
	}

	public void setTraining(boolean training) {
		this.training = training;
	}

	public int getStepBounds()
	{
		return stepBounds;
	}
	
	public void setStepBounds(int val)
	{
		this.stepBounds = val;
	}
	
	public Taxi(long seed) {
		super(seed);
	
		// setup the background
		
		backgroundField.set(0, 0, new Block(Color.red, new int[]{0,0,0,0}));
		backgroundField.set(1, 0, new Block(null, new int[]{0,1,0,0}));
		backgroundField.set(2, 0, new Block(null, new int[]{0,0,0,1}));
		backgroundField.set(1, 1, new Block(null, new int[]{0,1,0,0}));
		backgroundField.set(2, 1, new Block(null, new int[]{0,0,0,1}));
		backgroundField.set(4, 0, new Block(Color.green, new int[]{0,0,0,0}));
		backgroundField.set(0, 4, new Block(Color.yellow, new int[]{0,1,0,0}));
		backgroundField.set(0, 3, new Block(null, new int[]{0,1,0,0}));
		backgroundField.set(1, 3, new Block(null, new int[]{0,0,0,1}));
		backgroundField.set(1, 3, new Block(null, new int[]{0,0,0,1}));
		backgroundField.set(3, 4, new Block(Color.blue, new int[]{0,0,0,1}));
		backgroundField.set(2, 4, new Block(null, new int[]{0,0,0,1}));
		backgroundField.set(2, 3, new Block(null, new int[]{0,1,0,0}));
		backgroundField.set(2, 4, new Block(null, new int[]{0,1,0,0}));
		
		
		
		passenger = new TaxiObject();
		destination = new TaxiObject();
		world = new World(this);
		
		this.constructStateList();
		this.numAction = State.Action.values().length;
		
	}

	public void start()
	{
		super.start();
		initAgents();
	}
	

	private void initAgents() {
		
		agent = new QAgent(this, passenger);

		initGame();
		
		agentStopper = schedule.scheduleRepeating(0, 0, agent);
		
	}
	
	
	public void initGame()
	{
		gameTime++;
		
		taxiField.clear();
		
		// reset the passenger's location and state
		int passengerIndex = random.nextInt(4);
		//int passengerIndex = 0;
		Point loc = World.locationMap.get(passengerIndex);
		passenger.setLocation(loc.x, loc.y, taxiField);
		passenger.state = World.LocState.values()[passengerIndex];
		
		// initial destination's location and state
		int destinationIndex = random.nextInt(4);
		//int destinationIndex = 3;
		loc = World.locationMap.get(destinationIndex);
		destination.setLocation(loc.x, loc.y, taxiField);
		destination.state = World.LocState.values()[destinationIndex];
		
		int x = random.nextInt(4);
		int y = random.nextInt(4);
		
		// debug setting
		//int x = 0;
		//int y = 1;
		agent.resetAgent(x, y, passenger.state, destination.state);

	}
	
	public void constructStateList() {
		int counter = 0;
		for(int x = 0;x<5;++x)
		{
			for(int y = 0;y<5;++y)
			{
				for(int d = 0;d<4;++d) // des is available in the range 0 to 3
				{
					for(int p = 0;p<5;++p) // p is available in the range 0 to 4
					{
						State state = new State(x, y, d, p);
						stateList.add(state);
						stateMap.put(state, counter++);
					}
				}
			}
		}
		this.numState = stateList.size(); // we have a terminal state
		
	}
	
	public static void main(String[] args)
	{
		doLoop(Taxi.class, args);
		System.exit(0);
	}

	public boolean getVerbose() {
		return this.verbose;
	}
	
	public void setVerbose(boolean val)
	{
		this.verbose = val;
	}
	
	public int getNumState() {
		return this.numState;
	}
	
	public int getNumAction() {
		return this.numAction;
	}
	
	
}
