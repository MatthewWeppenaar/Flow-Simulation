import java.awt.Color;
public class Simulation extends java.lang.Thread {
   Terrain land;
	int high;
	int low;

	volatile boolean check = true;
	volatile boolean stop = false;
	volatile boolean wait = true;
   Simulation(Terrain t, int l, int h) {
		land = t;
		low = l;
		high = h;
		}
   
   public void flow(int lo, int hi) {//resposible for  the flow of water on the terrain
		int xp;
		int yp;
		int yWidth = land.dimx - 1;
		int xWidth = land.dimy - 1;

		int[] newPos = new int[2];
		int[] prevCurr = new int[2];
		int tempPos[] = new int[2];

		
		int size = land.dim();

		float waterDepth;

		
		float currentWater;
		float lowestWater;

		for (int i = lo; i < hi; i++) {
			unset();
			// Obtaining random position to permute
			land.getPermute(i, tempPos);
         
			// Getting x and y position of the temporay chosen position
			xp = tempPos[0];
			yp = tempPos[1];

			
			currentWater = land.water[xp][yp];
         

			if (!(xp == 0 || yp == 0 || xp == xWidth || yp == yWidth)) {

				// Checking if the current pixel has water
				if (currentWater != 0) {

					// Finding the lowest closest position
               float[][] waterdep = land.water;
					newPos = land.compare(xp, yp, waterdep);

					// This means the water has reached a basin
					if (newPos[0] == 0 && newPos[1] == 0) {
						;
					} else {

						lowestWater = land.water[newPos[0]][newPos[1]];
						
						land.setBlue(newPos[0], newPos[1]);

						// Increasing and decreasing the waterDepth to indicate transfering of water
						land.inreamentDepth(land.water, newPos);
						land.decrementDepth(land.water, tempPos);

						

						// Checking  for water
						if (land.water[xp][yp] == 0) {
							land.reColor(xp, yp);
						} else {
							// reshade the current pixel
							land.setBlue(xp, yp);
						}

						prevCurr[0] = tempPos[0];
						prevCurr[1] = tempPos[1];// }// End of lowestWater synch
					}

				} 
			} 

			else if ((xp == 0 || yp == 0 || xp == xWidth || yp == yWidth)) {
				land.water[xp][yp]=0.0f;
			}
			
			Flow.fp.repaint();
			
			Flow.setTrue();
			set();
			//Flow.timeLabel.setText(String.valueOf(i/1000));
			
			
			
			
		} 
		
			}
         
    

	
	public void pause(){
		stop = true;
	}

	public void unpause(){
		stop = false;
	}

	public void set(){
		wait = true;
		
	}

	public void unset(){
		wait = false;
	}

	public boolean getflag(){
		return wait;
	}
   public void restart(){
		int tempPos [] = new int[2];
		int size = land.dim();
		
		
		for(int i = 0; i<size;i++){
			land.locate(i, tempPos);
			land.reColor(tempPos[0], tempPos[1]);
			land.water[tempPos[0]][tempPos[1]] = (float)(0.00);
		}
      Flow.timeLabel.setText(String.valueOf(0));
      }
   
   @Override
	public void run() {
		while(check){
			if(!(stop)){
				flow(low,high);
				Flow.fp.repaint();}
		}
  }
}