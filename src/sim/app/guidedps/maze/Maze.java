/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.guidedps.maze;

import sim.app.guidedps.gridworld.GridModel;
import sim.app.guidedps.gridworld.GridObject;
import sim.app.guidedps.gridworld.State;

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
    }

    @Override
    protected void initAgents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public State getCurrentState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
        
        
    
    
}
