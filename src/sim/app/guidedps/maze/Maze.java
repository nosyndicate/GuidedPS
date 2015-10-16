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
import sim.app.guidedps.taxi.agents.PrioritizedSweeping;

/**
 *
 * @author drew
 */
public class Maze extends GridModel{

    private static final long serialVersionUID = 1L;
    public final int height = 26;
    public final int width = 26;
    
    
    public MazeAgent agent;
    public GridObject source;
    public GridObject destination;

    public Maze(long seed) {
        super(seed);
        
        
        //setup the background
        backgroundField.set(0, 14, new Block(Color.BLACK, new int[]{1,1,1,1}));
        backgroundField.set(1, 14, new Block(Color.BLACK, new int[]{1,1,1,1}));
        backgroundField.set(2, 14, new Block(Color.BLACK, new int[]{1,1,1,1}));
        backgroundField.set(3, 14, new Block(Color.BLACK, new int[]{1,1,1,1}));
        backgroundField.set(4, 14, new Block(Color.BLACK, new int[]{1,1,1,1}));
        backgroundField.set(5, 14, new Block(Color.BLACK, new int[]{1,1,1,1}));
        backgroundField.set(6, 14, new Block(Color.BLACK, new int[]{1,1,1,1}));
        
        
        source = new GridObject();
        destination = new GridObject();
        world = new MazeWorld(this);
        
        this.constructStateList();
        this.numAction = 4;
        initAgents();
    }

    @Override
    protected final void initAgents() {
        agent = new MazeAgent(this, new PrioritizedSweeping(this));
        initGame();

        agentStopper = schedule.scheduleRepeating(0, 0, agent);
		
    }

    @Override
    public final void initGame() {
        gameTime++;
		
        gridField.clear();

        // reset the passenger's location and state
        int passengerIndex = random.nextInt(4);
        //int passengerIndex = 0;
        Point loc = World.locationMap.get(passengerIndex);
        source.setLocation(loc.x, loc.y, gridField);
       

        // initial destination's location and state
        int destinationIndex = random.nextInt(4);
        //int destinationIndex = 3;
        loc = World.locationMap.get(destinationIndex);
        destination.setLocation(loc.x, loc.y, gridField);
        destination.state = World.LocState.values()[destinationIndex];

       
        agent.resetAgent();

    }

    @Override
    public State getCurrentState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void constructStateList() {
        // just x by y states since source and destination are fixed
        int counter = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                State state = new State(x, y, 0, 0);// using the same state but only caring about x and y
                stateList.add(state);
		stateMap.put(state, counter++);
            }
        }
        this.numState = stateList.size(); // we have a terminal state
    }    
        
    
    
}
