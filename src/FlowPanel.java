import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;

public class FlowPanel extends JPanel implements Runnable {
   volatile boolean check = true;
	volatile boolean stop = false;
	Terrain land;
   int xpoint;
   int ypoint;
   int low;
   int high;
   //Water water;
   private List<gridpoint> squares = new ArrayList<gridpoint>();
   //private boolean done;
	FlowPanel(Terrain terrain/*,Water waterdepth*/) {
		land=terrain;
      low = 0;
      high = terrain.dim();
      //water=waterdepth;
	}
   public void addSquare(gridpoint square){
      squares.add(square);//add points to a list for painting
   }
   
   

   
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the landscape in greyscale as an image
		if (land.getImage() != null){
			g.drawImage(land.getImage(), 0, 0, null);
		}
      
      repaint();
	}
   public void fill() {
   for(gridpoint c: squares){
         land.changePixel(c.getXpoint(),c.getYpoint());
         Flow.fp.repaint();//painting squares on the gui
         }
         squares.clear();//removing painted squares from list,so that it is not repainted when new water is added.
				
      }
	//Boolean that is used to stop the simulation
	public void stop(){
		check = false;
	}

	// Boolean that is used to pause the simulation
	public void pause(){
		stop = true;
	}

	public void unpause(){
		stop = false;
	}
   public void restart(){
		int tempPos [] = new int[2];
		//int size = land.dim();
		
		// Reset everything
		for(int i = 0; i<land.dim();i++){
			land.locate(i, tempPos);
			land.reColor(tempPos[0], tempPos[1]);
			land.water[tempPos[0]][tempPos[1]] = (float)(0.00);
		}

		
	}
	public void run() {
	    repaint();
	}
}