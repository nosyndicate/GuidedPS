package sim.app.guidedps.taxi;


import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import sim.app.guidedps.taxi.State.Action;


public class World extends Environment {
	
	public enum LocState
	{
		RED,
		GREEN,
		YELLOW,
		BLUE,
		TAXI
	};
	
	public static HashMap<Integer, Point> locationMap;
	public static HashMap<Point, Action> illegalMovement;
	public static ArrayList<Color> colorList;
	static{
		World.locationMap = new HashMap<Integer, Point>();
		illegalMovement = new HashMap<Point, Action>();
		colorList = new ArrayList<Color>();
		locationMap.put(0, new Point(0,0));
		locationMap.put(1, new Point(4,0));
		locationMap.put(2, new Point(0,4));
		locationMap.put(3, new Point(3,4));
		
		
		colorList.add(Color.red);
		colorList.add(Color.green);
		colorList.add(Color.yellow);
		colorList.add(Color.blue);
		
		// add constraint on walls
		
		illegalMovement.put(new Point(1,0), Action.EAST);
		illegalMovement.put(new Point(1,1), Action.EAST);
		illegalMovement.put(new Point(2,0), Action.WEST);
		illegalMovement.put(new Point(2,1), Action.WEST);
		illegalMovement.put(new Point(0,3), Action.EAST);
		illegalMovement.put(new Point(0,4), Action.EAST);
		illegalMovement.put(new Point(1,3), Action.WEST);
		illegalMovement.put(new Point(1,4), Action.WEST);
		illegalMovement.put(new Point(2,3), Action.EAST);
		illegalMovement.put(new Point(2,4), Action.EAST);
		illegalMovement.put(new Point(3,3), Action.WEST);
		illegalMovement.put(new Point(3,4), Action.WEST);
		
		
	}
	private static final long serialVersionUID = 1L;
	
	private Taxi model;
	private boolean endGame;
	
	
	

	

	
	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}


	public boolean isEndGame() {
		return endGame;
	}


	public World(Taxi state)
	{
		model = state;
		endGame = false;
	}
	
	
	@Override
	public void update() {
		// get the action from agent
		Action action = model.agent.getAction();
		// determine the transition of the state, and reward of the action
		reward = processAction(action);		
	}

	private double processAction(Action action) {
		final double regular = -1;
		final double illegal = -10;
		final double success = 20;
		
		if(action==Action.PICKUP)
		{
			Point agentLoc = model.agent.getLocation();
			Point passengerLoc = model.passenger.getLocation();
			// pick up at where the passenger is, only legal action
			if(!model.agent.getPickUp()&&agentLoc.x==passengerLoc.x&&agentLoc.y==passengerLoc.y)
			{
				model.agent.setPickUp(true);
				model.passenger.state = LocState.TAXI;
				return regular;
			}
			// illegal action
			return illegal;
		}
		else if (action==Action.PUTDOWN)
		{
			if(model.agent.getPickUp())
			{
				Point agentLoc = model.agent.getLocation();
				Point desLoc = model.destination.getLocation();
				// success deliver, end of the game
				// this is also the only legal option for put down
				// other situation should be illegal
				if(desLoc.x==agentLoc.x&&desLoc.y==agentLoc.y)
				{
					endGame = true;
					return success + regular;
				}	
			}
			// illegal action, putdown without put
			return illegal;
		}
		else {
			Point agentLoc = model.agent.getLocation();
			if(illegalMovement.containsKey(agentLoc))
			{
				// illegal movement
				if(action==illegalMovement.get(agentLoc))
				{
					return regular;
				}
			}
			
			
			if(action==Action.NORTH)
			{
				if(agentLoc.y-1>=0) // legal movement
				{
					model.agent.setLocation(agentLoc.x, agentLoc.y-1, model.taxiField);;
				}
			}else if (action==Action.SOUTH) {
				if(agentLoc.y+1<Taxi.height) // legal movement
				{
					model.agent.setLocation(agentLoc.x, agentLoc.y+1, model.taxiField);;
				}
			}else if (action==Action.EAST) {
				if(agentLoc.x+1<Taxi.width) // legal movement
				{
					model.agent.setLocation(agentLoc.x+1, agentLoc.y, model.taxiField);;
				}
			}else if (action==Action.WEST) {
				if(agentLoc.x-1>=0) // legal movement
				{
					model.agent.setLocation(agentLoc.x-1, agentLoc.y, model.taxiField);;
				}
			}
			return regular;
		}
	}

	
	
	
}