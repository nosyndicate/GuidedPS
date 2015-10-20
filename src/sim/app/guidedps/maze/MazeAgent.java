/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.guidedps.maze;

import sim.app.guidedps.agents.LearningAgent;
import sim.app.guidedps.gridworld.GridAgent;
import sim.app.guidedps.gridworld.State;

/**
 *
 * @author drew
 */
public class MazeAgent extends GridAgent{

	private static final long serialVersionUID = 1L;

	public MazeAgent(Maze model, LearningAgent agent) {
        this.model = model;
        this.agent = agent;
    }
    
    public void resetAgent(int x, int y) {
    	agent.setState(new State(x, y, 0, 0));
		this.setLocation(x, y, model.gridField);
    }
    
}
