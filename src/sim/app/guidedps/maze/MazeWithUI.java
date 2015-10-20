/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.app.guidedps.maze;

import java.awt.Color;
import javax.swing.JFrame;
import sim.app.guidedps.gridworld.BlockPortrayal;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.SimpleInspector;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.inspector.TabbedInspector;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.gui.SimpleColorMap;

/**
 * 
 * @author drew
 */
public class MazeWithUI extends GUIState {
	private final int displayWidth = 800;
	private final int displayHeight = 700;
	private Maze model;
	private SparseGridPortrayal2D mazeFieldPortrayal;
	private ObjectGridPortrayal2D backgroundPortrayal;
	private FastValueGridPortrayal2D stateValuePortrayal;
	private FastValueGridPortrayal2D signalValuePortrayal;
	private Display2D display;

	public MazeWithUI() {
		super(new Maze(System.currentTimeMillis()));
		model = (Maze) state;
	}

	public static String getName() {
		return "Maze Domain in RL";
	}

	public void start() {
		super.start();
		setupObjectPortrayals();

		display.reset();
		display.setBackdrop(Color.white);
		display.repaint();
	}

	public void setupObjectPortrayals() {
		mazeFieldPortrayal.setPortrayalForObject(model.agent,
				getAgentPortrayal());
	}

	public void load(SimState state) {
		super.load(state);
	}

	private SparseGridPortrayal2D getAgentFieldPortrayal() {
		SparseGridPortrayal2D portrayal = new SparseGridPortrayal2D();
		portrayal.setField(model.gridField);
		return portrayal;
	}

	private OvalPortrayal2D getAgentPortrayal() {
		OvalPortrayal2D portrayal = new OvalPortrayal2D(Color.black, 0.6, true);
		return portrayal;
	}

	private void setupPortrayals() {
		attachFields(display);
	}

	public void init(Controller c) {
		super.init(c);
		display = new Display2D(displayWidth, displayHeight, this);
		display.setClipping(true);

		JFrame displayFrame = display.createFrame();
		displayFrame.setTitle("Taxi");
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);

		setupPortrayals();

	}

	private void attachFields(Display2D display) {

		mazeFieldPortrayal = getAgentFieldPortrayal();
		backgroundPortrayal = getBackgroundPortrayal();
		stateValuePortrayal = getStateValuePortrayal();
		signalValuePortrayal = getSignalValuePortrayal();
		//signalValuePortrayal = getSignalValuePortrayal();
		display.attach(backgroundPortrayal, "Background");
		display.attach(mazeFieldPortrayal, "Agent");
		display.attach(stateValuePortrayal, "State Value");
		display.attach(signalValuePortrayal, "Demonstration Signal");

	}

	private FastValueGridPortrayal2D getSignalValuePortrayal() {
		FastValueGridPortrayal2D portrayal = new FastValueGridPortrayal2D();
		portrayal.setField(model.signalGrid);
		portrayal.setMap(new SimpleColorMap(0, 1.0, new Color(0,0,0,0),Color.GREEN));
		return portrayal;
	}

	private FastValueGridPortrayal2D getStateValuePortrayal() {
		FastValueGridPortrayal2D portrayal = new FastValueGridPortrayal2D();
		portrayal.setField(model.stateValueGrid);
		portrayal.setMap(new SimpleColorMap(-70, 0, Color.red, new Color(0,0,0,0)));
		return portrayal;
	}

	private ObjectGridPortrayal2D getBackgroundPortrayal() {
		ObjectGridPortrayal2D portrayal = new ObjectGridPortrayal2D();
		portrayal.setPortrayalForAll(new BlockPortrayal(model.backgroundField));
		portrayal.setField(model.backgroundField);

		// Color[] colorTable = new Color[]{Color.red, Color.green,
		// Color.yellow, Color.blue};
		// SimpleColorMap map = new SimpleColorMap(colorTable);
		// portrayal.setMap(map);

		return portrayal;
	}

	@Override
	public Inspector getInspector() {
		TabbedInspector i = new TabbedInspector();

		i.addInspector(new SimpleInspector(model, this), "Main");

		return i;
	}

	public static void main(String[] args) {
		MazeWithUI modelUI = new MazeWithUI();
		Console c = new Console(modelUI);
		c.setVisible(true);
	}
}
