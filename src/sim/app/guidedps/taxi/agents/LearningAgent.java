/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.guidedps.taxi.agents;

import sim.app.guidedps.gridworld.State;

/**
 *
 * @author drew
 */
public interface LearningAgent {
    
    public void setState(State state);
    public State.Action ActionSelection();
    public void updatePolicy();
}
