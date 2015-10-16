/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.guidedps.maze;

import java.awt.Point;
import sim.app.guidedps.gridworld.Environment;
import sim.app.guidedps.gridworld.State;

/**
 *
 * @author drew
 */
public class MazeWorld extends Environment{

    private Maze model;
    
    
    public MazeWorld(Maze model) {
        this.model = model;
    }
    
    
    @Override
    public void update() {
            // get the action from agent
            State.Action action = model.agent.getAction();
            // determine the transition of the state, and reward of the action
            reward = processAction(action);		
    }
    
    
    public double processAction(State.Action action) {
        
        
        // so get the agent's current x,y and determine if it can
        // move in that direction
        
        
        /*
        http://jmlr.org/proceedings/papers/v28/vanseijen13.pdf
        In the first task, the
        agent moves with 80% probability to the neighbouring
        square corresponding to the compass direction, and
        with 20% probability it moves at random to one of
        its four neighbouring squares.
        
        DREW:  what if the random square it picks is outside the maze?
                do you only select from the valid choices or do you 
                just not move the agent?  If you don't move the agent
                does it get a reward?
        
        For both tasks, the reward received
        at each time step is -1
        
        
        */
        
        Point oldLoc = model.agent.getLocation();
        action.NORTH
        if ()
        
        
        
        
        
        return -1;
    }
    
}
