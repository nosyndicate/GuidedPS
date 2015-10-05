package sim.app.guidedps.taxi;




import java.awt.Point;

import sim.app.guidedps.taxi.World.LocState;
import sim.field.grid.SparseGrid2D;

public class TaxiObject
{
	public int x;
	public int y;
	public LocState state;
	
	public TaxiObject()
	{
		
	}
	
	public TaxiObject(int x, int y, SparseGrid2D field)
	{
		setLocation(x, y, field);
	}
	
	public void setLocation(int x, int y, SparseGrid2D field)
	{
		this.x = x;
		this.y = y;
		field.setObjectLocation(this, x, y);
	}
	
	public Point getLocation()
	{
		return new Point(x, y);
	}
}
