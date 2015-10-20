package sim.app.guidedps.agents;

import sim.app.guidedps.gridworld.GridModel;
import sim.app.guidedps.gridworld.State.Action;
import sim.app.guidedps.gridworld.State;
import sim.app.guidedps.util.RelaxablePriorityQueue;
import sim.app.guidedps.util.Utils;

public class PrioritizedSweeping extends LearningAgent {

	
	private static final long serialVersionUID = 1L;
	private RelaxablePriorityQueue<PriorityTuple> queue = new RelaxablePriorityQueue<>();
	private int[][] stateActionCounter;
	private double[][] rewardSum;
	private double[][] qValue;
	private int[][][] sASCounter;
	private int numAction;
	private int numState;
	private double gamma = 1;
	private double epsilon = 0.1;
	private double theta = 0.05; // threshold for putting tuple into the queue
	private State s;
	private int p = 3;
        private GridModel model;
        private double reward;
        private Action action;

	public PrioritizedSweeping(GridModel model) {
		this.model = model;
		initialization();
	}

	

	public void updatePolicy() {
		State sprime = model.getCurrentState();
		reward = model.world.getReward();
		
		int stateIndex = model.stateMap.get(s);
		int sprimeIndex = model.stateMap.get(sprime);
		if(model.world.isEndGame())
			sprimeIndex = model.getNumState(); // into terminal state
		
		int actionIndex = action.ordinal();
		
		
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
		setState(sprime);
		
	}

	public Action ActionSelection() {
		int stateIndex = model.stateMap.get(this.s);
		
		if(!model.isTraining())
		{
			action = Action.values()[Utils.bestAction(model.random, qValue[stateIndex])];
		}
		else {
			action = Action.values()[Utils.epsilonGreedy(model.random, epsilon, qValue[stateIndex])];
		}
                return action;
	}

        public void setState(State state) {
            s = state;
        }
        
	
	
	private void initialization() {
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
		// signal value, not use in prioritized sweeping
		signalValue = new double[numState+1];
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
		


	
