package sim.app.guidedps.taxi.agents;

import java.util.HashMap;

import sim.app.guidedps.gridworld.GridModel;
import sim.app.guidedps.gridworld.State.Action;
import sim.app.guidedps.gridworld.State;
import sim.app.guidedps.util.RelaxablePriorityQueue;
import sim.app.guidedps.util.Utils;

public class GuidedPS extends LearningAgent {

	private static final long serialVersionUID = 1L;
	private RelaxablePriorityQueue<PriorityTuple> queue = new RelaxablePriorityQueue<>();
	private RelaxablePriorityQueue<PriorityTuple> signalQueue = new RelaxablePriorityQueue<>();
	private HashMap<Demonstration, Double> demonstrationMap = new HashMap<Demonstration, Double>();
	private double[] signalValue;
	private int[][] stateActionCounter;
	private double[][] rewardSum;
	private double[][] qValue;
	private int[][][] sASCounter;
	private int numAction;
	private int numState;
	private double gamma = 0.9;
	private double tau = 0.9;
	private double epsilon = 0.1;
	private double theta = Double.MIN_VALUE; // threshold for putting tuple into
												// the queue
	private State s;
	private int p = 3;
	private GridModel model;
	private double reward;
	private Action action;

	public GuidedPS(GridModel model) {
		this.model = model;
		initializaDemonstration();
		initialization();
	}

	private void initializaDemonstration() {
		// add demonstration here
		// demonstrationMap.put(new Demonstration(new State(x, y, d, p), a), value);
		demonstrationMap.put(new Demonstration(new State(0, 0, 3, 0), 4), 1.0); //pick at the red spot
	}

	private void initialization() {
		this.numState = model.getNumState();
		this.numAction = model.getNumAction();
		// N(s,a), we have a terminal state 
		stateActionCounter = new int[numState + 1][numAction]; 
		// N(s,a,s')
		sASCounter = new int[numState + 1][numAction][numState + 1];
		// R(s,a)
		rewardSum = new double[numState + 1][numAction];
		// Q(s,a)
		qValue = new double[numState + 1][numAction];
		// V(s)
		stateValue = new double[numState + 1];
		// H(s)
		signalValue = new double[numState + 1];
	}

	public void updatePolicy() {
		State sprime = model.getCurrentState();
		reward = model.world.getReward();

		int stateIndex = model.stateMap.get(s);
		int sprimeIndex = model.stateMap.get(sprime);
		if (model.world.isEndGame())
			sprimeIndex = model.getNumState(); // into terminal state

		int actionIndex = this.action.ordinal();

		// only update the Q value in training mode
		if (model.isTraining()) {
			try {
				updateModel(stateIndex, actionIndex, sprimeIndex, reward);
				valueBackup(stateIndex, actionIndex, sprimeIndex, reward);

				int counter = 0;
				while (counter < p && !queue.isEmpty()) {
					this.prioritizedSweeping();
					counter++;
				}

				System.out.println("start signal sweeping");
				double signalStrength = getSignalStrength(stateIndex,actionIndex);
				signalForwardup(stateIndex, actionIndex, sprimeIndex,
						signalStrength);
				counter = 0;
				/*while (counter < p && !signalQueue.isEmpty()) {
					this.forwardSweeping();
					counter++;
				}*/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// move to the new state
		setState(sprime);
	}

	public void updateModel(int s, int a, int sprime, double reward) {
		stateActionCounter[s][a]++;
		sASCounter[s][a][sprime]++;
		rewardSum[s][a] += reward;
	}

	private double getSignalStrength(int s, int a) {
		State state = model.stateList.get(s); // get the real state
		Demonstration d = new Demonstration(state, a);
		if(demonstrationMap.containsKey(d))
			return demonstrationMap.get(d);
		return 0;
	}

	private void signalForwardup(int s, int a, int sprime, double signalStrength) {
		try {
			double max = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < numState + 1; ++i) {
				for (int j = 0; j < numAction; ++j) {
					if (sASCounter[i][j][sprime] > 0) {
						double value = tau * sASCounter[i][j][sprime] / stateActionCounter[i][j] * signalValue[s];
						if (i == s && j == a) {
							value += signalStrength;
						}
						max = Math.max(max, value);
					}
				}
			}

			double oldSignalValue = signalValue[sprime];
			if(signalValue[sprime] < max)
				signalValue[sprime] = max;
			
			double delta = Math.abs(signalValue[sprime] - oldSignalValue);
			System.out.println("h value for sprime is "+signalValue[sprime]+", amplify value is "+getAmplification(sprime));
			
			//if (delta > theta)
			//	signalQueue.relax(new PriorityTuple(sprime, delta), delta);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void forwardSweeping() {
		int s = firstOfSignalQueue();

		for (int i = 0; i < numState + 1; ++i) {
			for (int j = 0; j < numAction; ++j) {
				if (sASCounter[s][j][i] > 0) {
					double signalStrength = getSignalStrength(s, j);
					signalForwardup(s, j, i, signalStrength);
				}
			}
		}

	}

	private int firstOfSignalQueue() {
		PriorityTuple tuple = signalQueue.poll();
		return tuple.stateIndex;
	}

	private void valueBackup(int s, int a, int sprime, double r) {
		try {
			double sum = 0;
			for (int i = 0; i < numState + 1; ++i) {
				if (sASCounter[s][a][i] > 0) {
					sum += (sASCounter[s][a][i] * stateValue[i]);
				}
			}
			sum = sum / stateActionCounter[s][a];

			qValue[s][a] = r + gamma * sum;
			double oldStateValue = stateValue[s];
			stateValue[s] = maxQ(s);
			double delta = Math.abs(oldStateValue - stateValue[s])
					* getAmplification(s);
			if (delta > theta)
				queue.relax(new PriorityTuple(s, delta), delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private double getAmplification(int s) {
		double v = Math.exp(-signalValue[s]*2) + 1;
		return 2 / v;
	}

	private void prioritizedSweeping() {
		int s = firstOfQueue();

		for (int i = 0; i < numState + 1; ++i) {
			for (int j = 0; j < numAction; ++j) {
				if (sASCounter[i][j][s] > 0) {
					double rewardEstimation = rewardSum[i][j]
							/ stateActionCounter[i][j];
					this.valueBackup(i, j, s, rewardEstimation);
				}
			}
		}
	}

	private int firstOfQueue() {
		PriorityTuple tuple = queue.poll();
		return tuple.stateIndex;
	}

	public Action ActionSelection() {
		int stateIndex = model.stateMap.get(this.s);

		if (!model.isTraining()) {
			action = Action.values()[Utils.bestAction(model.random,
					qValue[stateIndex])];
		} else {
			action = Action.values()[Utils.epsilonGreedy(model.random, epsilon,
					qValue[stateIndex])];
		}
		return action;
	}

	public void setState(State state) {
		s = state;
	}

	private double maxQ(int s) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < numAction; ++i) {
			max = Math.max(qValue[s][i], max);
		}
		return max;
	}

}
