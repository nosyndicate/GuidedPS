/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.guidedps.maze;

import java.awt.Color;
import java.awt.Point;

import javax.security.auth.x500.X500Principal;

import sim.app.crowd3d.Agent;
import sim.app.guidedps.gridworld.Block;
import sim.app.guidedps.gridworld.Environment;
import sim.app.guidedps.gridworld.State;
import sim.app.guidedps.gridworld.State.Action;

/**
 * 
 * @author drew
 */
public class MazeWorld extends Environment {

	public static int[] movementIndex = new int[] { 0, 1, 2, 3 };
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
		 * http://jmlr.org/proceedings/papers/v28/vanseijen13.pdf In the first
		 * task, the agent moves with 80% probability to the neighbouring square
		 * corresponding to the compass direction, and with 20% probability it
		 * moves at random to one of its four neighbouring squares.
		 * 
		 * DREW: what if the random square it picks is outside the maze? do you
		 * only select from the valid choices or do you just not move the agent?
		 * If you don't move the agent does it get a reward?
		 * 
		 * For both tasks, the reward received at each time step is -1
		 */

		Point oldLoc = model.agent.getLocation();
		// set the new location and then check if it matches a wall
		// if it does then move it back to the old location.

		Action realAction = getRealAction(action);

		// System.out.println("real action is "+realAction);

		if (illegalMovement(oldLoc, realAction)) // return immediately
		{
			return -1;
		}

		if (realAction == Action.NORTH) {
			model.agent.setLocation(oldLoc.x, oldLoc.y - 1, model.gridField);
		} else if (realAction == Action.SOUTH) {
			model.agent.setLocation(oldLoc.x, oldLoc.y + 1, model.gridField);
		} else if (realAction == Action.EAST) {
			model.agent.setLocation(oldLoc.x + 1, oldLoc.y, model.gridField);
		} else if (realAction == Action.WEST) {
			model.agent.setLocation(oldLoc.x - 1, oldLoc.y, model.gridField);
		}
		
		Point newLocPoint = model.agent.getLocation();
		if(newLocPoint.x==25&&newLocPoint.y==22)
			endGame = true;
		
		return -1;

	}

	private boolean illegalMovement(Point oldLoc, Action realAction) {
		// check if hitting the boundary or block
		if (realAction == Action.NORTH) {
			if (oldLoc.y - 1 < 0) // illegal movement
				return true;
			if (isBlock(oldLoc.x, oldLoc.y - 1))
				return true;
		} else if (realAction == Action.SOUTH) {
			if (oldLoc.y + 1 >= model.height) // illegal movement
				return true;
			if (isBlock(oldLoc.x, oldLoc.y + 1))
				return true;
		} else if (realAction == Action.EAST) {
			if (oldLoc.x + 1 >= model.width) // illegal movement
				return true;
			if (isBlock(oldLoc.x + 1, oldLoc.y))
				return true;
		} else if (realAction == Action.WEST) {
			if (oldLoc.x - 1 < 0) // illegal movement
				return true;
			if (isBlock(oldLoc.x - 1, oldLoc.y))
				return true;
		}
		return false;
	}

	private boolean isBlock(int x, int y) {
		Object object = model.backgroundField.get(x, y);
		if (object instanceof Block)
		{
			if(((Block)object).color.equals(Color.BLACK))
				return true;
		}
		return false;
	}

	private Action getRealAction(Action action) {
		if (model.isStochastic()) {
			int index = action.ordinal();
			double sample = model.random.nextDouble();
			if (0.8 <= sample && sample < 0.90) // decrease
			{
				index = (index + 4 - 1) % 4;
			} else if (0.90 <= sample && sample < 1.0) // increase
			{
				index = (index + 1) % 4;
			}
			action = Action.values()[index];
		}
		return action;
	}

}
