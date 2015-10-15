package sim.app.guidedps.taxi.agents;




import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import sim.app.guidedps.taxi.State;
import sim.app.guidedps.taxi.Taxi;
import sim.app.guidedps.taxi.TaxiAgent;
import sim.app.guidedps.taxi.TaxiObject;
import sim.app.guidedps.taxi.World;
import sim.app.guidedps.taxi.World.LocState;
import sim.app.guidedps.taxi.State.Action;
import sim.app.guidedps.util.Utils;
import sim.engine.SimState;

public class QAgent extends TaxiAgent{
	
	private static final long serialVersionUID = 1L;

	
	
	private double[][] qValue;
	private double alpha = 0.1;
	private double temperature = 50;
	private double delta = 0.9879;
	//private double gamma = 0.9;
	private double gamma = 1;
	private double initQ = 0;
	private double epsilon = 0.1;
	private State s;

	
	
	public QAgent(Taxi model, TaxiObject passenger) {
		super(model, passenger);
		initializeQTable();		
	}
	
	
	
	
	public void updatePolicy()
	{
		State sprime = new State(x, y, model.destination.state, model.passenger.state);
		reward = model.world.getReward();
		
		// only update the Q value in training mode
		if(model.isTraining())
		{
			int actionIndex = this.action.ordinal();
			int stateIndex = model.stateMap.get(s);
			double val = qValue[stateIndex][actionIndex];
			
			//System.out.println("old value is " + val);
			
			// update the q value
			double newVal = Double.NEGATIVE_INFINITY;
			int sprimeIndex = model.stateMap.get(sprime);
			if(model.world.isEndGame())
				newVal = (1-alpha)*val+alpha*reward;
			else {
				newVal = (1-alpha)*val+alpha*(reward+gamma*maxQ(sprimeIndex));
			}
			
			qValue[stateIndex][actionIndex] = newVal;
		}
		
		// move to the new state
		s = sprime;
	}



	private String qTableForState(ArrayList<Double> actionList) {
		StringBuffer buffer = new StringBuffer();
		for(int i = 0;i<actionList.size();++i)
		{
			buffer.append(Action.values()[i]+":");
			buffer.append(actionList.get(i).floatValue());
			buffer.append(",");
		}
		
		return buffer.toString();
		
	}


	@Override
	public void step(SimState state) {
		if(model.isTraining())
			counter++;
		
		learning();
		
		// if one episode is ended, we start a new one
		if(model.world.isEndGame()) {
			model.gameStartIteration = model.gameEndIteratioin;
			model.gameEndIteratioin = counter;
			model.initGame();
			model.world.setEndGame(false);
		}
				
				
		// run out of iteration, stop the agent and world, reset the counter
		if (counter >= model.stepBounds) {
			model.agentStopper.stop();

			counter = 0;
			System.out.println("==========================");
			System.out.println("end of the training period");
			System.out.println("==========================");
			return;
		}
		
	}
	
	@Override
	public void learning() {
		// action selection
		this.action = ActionSelection();

		// print out the state
		// System.out.println("state:");
		// System.out.println("the position of taxi:("+x+","+y+")");
		// System.out.println("the state of passenger:"+passenger.state);
		// System.out.println("the state of destination:"+model.destination.state);
		// System.out.println(qTableForState(qTable.get(s)));
		// System.out.println("action is "+ this.action);

		// update the world, determine s' and reward
		model.world.update();

		// update the policy
		if (model.isTraining())
			updatePolicy();

	}

	


	private Action ActionSelection() {
		int stateIndex = model.stateMap.get(s);
		
		if(!model.isTraining())
		{
			return Action.values()[Utils.bestAction(model.random, qValue[stateIndex])];
		}
		else {
			//return Action.values()[Utils.boltzmannSelection(model.random, temperature, qTable.get(s))];
			return Action.values()[Utils.epsilonGreedy(model.random, epsilon, qValue[stateIndex])];
		}

	}
	
	private double maxQ(int s) {
		double max = Double.NEGATIVE_INFINITY;
		for(int i = 0;i<model.getNumAction();++i) {
			max = Math.max(qValue[s][i],max);
		}
		return max;
	}
	
	private void initializeQTable() {
		this.qValue = new double[model.getNumState()][model.getNumAction()];
		for(int i = 0;i<qValue.length;++i) {
			for(int j = 0;j<model.getNumAction();++j) {
				qValue[i][j] = initQ;
			}
		}
	}

	
	@Override
	public void resetAgent(int x, int y, LocState passengerState, LocState desState) {
		s = new State(x, y, passengerState, desState);
		this.setPickUp(false);
		this.setLocation(x, y, model.taxiField);
	}

	
	public double getTemperature()
	{
		return this.temperature;
	}
	
}
