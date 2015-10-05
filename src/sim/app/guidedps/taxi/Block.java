package sim.app.guidedps.taxi;


import java.awt.Color;

public class Block
{
	public Color color;
	public int[] walls; // direction: top, right, bottom, left
						 // 0 no wall, 1 for wall
	public Block(Color c, int[] walls)
	{
		color = c;
		this.walls = walls;
	}
}