/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.guidedps.maze;

import sim.app.guidedps.gridworld.Environment;
import sim.app.guidedps.gridworld.State;

/**
 *
 * @author drew
 */
public class MazeWorld extends Environment{

    private Maze model;
    
    @Override
    public void update() {
            // get the action from agent
            State.Action action = model.agent.getAction();
            // determine the transition of the state, and reward of the action
            reward = processAction(action);		
    }
    
    
    public double processAction(State.Action action) {
        return 0.0;
    }
    
}
