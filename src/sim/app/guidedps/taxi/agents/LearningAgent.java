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
public abstract class LearningAgent {
    public abstract void setState(State state);
    public abstract State.Action ActionSelection();
    public abstract void updatePolicy();
	public double[] stateValue;
	public double[] signalValue;
}
