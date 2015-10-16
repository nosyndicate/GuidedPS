package sim.app.guidedps.gridworld;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import sim.field.grid.ObjectGrid2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.SimplePortrayal2D;


public class BlockPortrayal extends SimplePortrayal2D{
	private static final long serialVersionUID = 1;


    private Color color = Color.white;
    private BasicStroke stroke = new BasicStroke(2f);
    private ObjectGrid2D field;
    
    public BlockPortrayal(ObjectGrid2D field)
    {
    	this.field = field;
    }

    public void draw(Object object, Graphics2D g, DrawInfo2D info)
    {
        
        Block block = (Block)object;
        
        if(block==null)
        	return;
        
                
        int ox = (int)info.draw.x;
        int oy = (int)info.draw.y;
        int sc = (int)info.draw.width / 2;
         
        
        
        if(block.color!=null)
        {
        	this.color = block.color;
        }
        else {
			this.color = Color.white;
		}
    
      
         g.setColor(color);
         g.setStroke(stroke);
         // draw the rectangle
         g.fillRect(ox-sc,oy-sc, sc*2, sc*2);
         
         int x1 = ox-sc;
         int y1 = oy-sc;
         int x2 = ox+sc;
         int y2 = oy+sc;
         
         // start to draw the line
         for(int i = 0;i<block.walls.length;++i)
         {
        	 if(block.walls[i]==1)
        	 {
        		 g.setColor(Color.black);
        		 if(i==0)
        		 {
        			 g.drawLine(x1, y1, x2, y1);
        		 }
        		 if(i==1)
        		 {
        			 g.drawLine(x2, y1, x2, y2);
        		 }
        		 if(i==2)
        		 {
        			 g.drawLine(x1, y2, x2, y2);
        		 }
        		 if(i==3)
        		 {
        			 g.drawLine(x1, y1, x1, y2);
        		 }
        	 }
         }
    }

}
