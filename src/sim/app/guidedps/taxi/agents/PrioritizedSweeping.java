package sim.app.guidedps.taxi.agents;

import sim.app.guidedps.taxi.State.Action;
import sim.app.guidedps.taxi.State;
import sim.app.guidedps.taxi.Taxi;
import sim.app.guidedps.taxi.TaxiAgent;
import sim.app.guidedps.taxi.TaxiObject;
import sim.app.guidedps.taxi.World.LocState;
import sim.app.guidedps.util.RelaxablePriorityQueue;
import sim.app.guidedps.util.Utils;
import sim.engine.SimState;

public class PrioritizedSweeping extends TaxiAgent {

	
	private static final long serialVersionUID = 1L;
	private RelaxablePriorityQueue<PriorityTuple> queue = new RelaxablePriorityQueue<>();
	private double[] stateValue;
	private int[][] stateActionCounter;
	private double[][] rewardSum;
	private double[][] qValue;
	private int[][][] sASCounter;
	private int numAction;
	private int numState;
	private double gamma = 0.9;
	private double epsilon = 0.1;
	private double theta = 0.05; // threshold for putting tuple into the queue
	private State s;
	private int p = 3;

	public PrioritizedSweeping(Taxi model, TaxiObject passenger) {
		super(model, passenger);
		initialization();
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

	private void updatePolicy() {
		State sprime = new State(x, y, model.destination.state, model.passenger.state);
		reward = model.world.getReward();
		
		int stateIndex = model.stateMap.get(s);
		int sprimeIndex = model.stateMap.get(sprime);
		if(model.world.isEndGame())
			sprimeIndex = model.getNumState(); // into terminal state
		
		int actionIndex = this.action.ordinal();
		
		
		// only update the Q value in training mode
		if(model.isTraining())
		{
			try {
				this.updateModel(stateIndex, actionIndex, sprimeIndex, reward);
				this.valueBackup(stateIndex, actionIndex, sprimeIndex, reward);
				
				int counter = 0;
				while(counter<p&&!queue.isEmpty())
				{
					this.prioritizedSweeping();
					counter++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// move to the new state
		s = sprime;
		
	}

	private Action ActionSelection() {
		int stateIndex = model.stateMap.get(this.s);
		
		if(!model.isTraining())
		{
			return Action.values()[Utils.bestAction(model.random, qValue[stateIndex])];
		}
		else {
			return Action.values()[Utils.epsilonGreedy(model.random, epsilon, qValue[stateIndex])];
		}

	}

	@Override
	public void resetAgent(int x, int y, LocState passengerState,
			LocState desState) {
		s = new State(x, y, passengerState, desState);
		this.setPickUp(false);
		this.setLocation(x, y, model.taxiField);
		
	}
	
	public void initialization() {
		this.numState = model.getNumState();
		this.numAction = model.getNumAction();
		// N(s,a)
		stateActionCounter = new int[numState+1][numAction]; // we have a terminal state
		// N(s,a,s')
		sASCounter = new int[numState+1][numAction][numState+1]; 
		// R(s,a)
		rewardSum = new double[numState+1][numAction];
		// Q(s,a)
		qValue = new double[numState+1][numAction];
		// V(s)
		stateValue = new double[numState+1];
	}
	
	
	public void updateModel(int s, int a, int sprime, double reward) {
		stateActionCounter[s][a]++;
		sASCounter[s][a][sprime]++;
		rewardSum[s][a]+=reward;
	}
	
	public void valueBackup(int s, int a, int sprime, double r) {
		try {
			double sum = 0;
			for (int i = 0; i < numState+1; ++i) {
				if (sASCounter[s][a][i] > 0) {
					sum += (sASCounter[s][a][i] * stateValue[i]);
				}
			}
			sum = sum / stateActionCounter[s][a];

			qValue[s][a] = r + gamma * sum;
			double oldStateValue = stateValue[s];
			stateValue[s] = maxQ(s);
			double delta = Math.abs(oldStateValue - stateValue[s]);
			if (delta > theta)
				queue.relax(new PriorityTuple(s, delta), delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void prioritizedSweeping() {
		int s = firstOfQueue();
		
		for(int i = 0;i<numState+1;++i) {
			for(int j = 0;j<numAction;++j) {
				if(sASCounter[i][j][s] > 0) {
					double rewardEstimation = rewardSum[i][j]/stateActionCounter[i][j];
					this.valueBackup(i, j, s, rewardEstimation);
				}
			}
		}
	}
	
	
	private int firstOfQueue() {
		PriorityTuple tuple = queue.poll();
		return tuple.stateIndex;
	}

	private double maxQ(int s) {
		double max = Double.NEGATIVE_INFINITY;
		for(int i = 0;i<numAction;++i) {
			max = Math.max(qValue[s][i],max);
		}
		return max;
	}
	
}
		


	
