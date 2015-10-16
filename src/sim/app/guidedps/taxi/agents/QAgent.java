package sim.app.guidedps.taxi.agents;




import java.util.ArrayList;
import sim.app.guidedps.gridworld.GridModel;
import sim.app.guidedps.gridworld.State;
import sim.app.guidedps.gridworld.State.Action;
import sim.app.guidedps.util.Utils;

public class QAgent implements LearningAgent{
	
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
        private GridModel model;
        private double reward;
        private Action action;
	
	
	public QAgent(GridModel model) {
                this.model = model;
		initializeQTable();		
	}
	
	
	
	
	public void updatePolicy()
	{
		State sprime = model.getCurrentState();
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



	


	public Action ActionSelection() {
		int stateIndex = model.stateMap.get(s);
		
		if(!model.isTraining())
		{
			action =  Action.values()[Utils.bestAction(model.random, qValue[stateIndex])];
		}
		else {
			//return Action.values()[Utils.boltzmannSelection(model.random, temperature, qTable.get(s))];
			action = Action.values()[Utils.epsilonGreedy(model.random, epsilon, qValue[stateIndex])];
		}
                return action;
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

	
	public void setState(State state) {
            s = state;
        }

	
	public double getTemperature()
	{
		return this.temperature;
	}
	
}
