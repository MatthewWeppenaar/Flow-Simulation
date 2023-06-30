
import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Scanner;
import java.util.*;
public class Terrain {

	float [][] height; // regular grid of height values
   float [][] water; // grid of water level at that (x,y) position
   Color [][] colorArray;//terrain color data for recoloring terrain
   int [][] positions;//array for positions of terrain
	int dimx, dimy; // data dimensions
	BufferedImage img; // greyscale image for displaying the terrain top-down

	ArrayList<Integer> permute;	// permuted list of integers in range [0, dimx*dimy)
	
	// overall number of elements in the height grid
	int dim(){
		return dimx*dimy;
	}
	
	// get x-dimensions (number of columns)
	int getDimX(){
		return dimx;
	}
	
	// get y-dimensions (number of rows)
	int getDimY(){
		return dimy;
	}
	
	// get greyscale image
	public BufferedImage getImage() {
		  return img;
	}
	
	// convert linear position into 2D location in grid
	void locate(int pos, int [] ind)
	{
		ind[0] = (int) pos / dimy; // x
		ind[1] = pos % dimy; // y	
	}
	
	// convert height values to greyscale colour and populate an image
	void deriveImage()
	{
		img = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);
		float maxh = -10000.0f, minh = 10000.0f;
		
		// determine range of heights
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				float h = height[x][y];
				if(h > maxh)
					maxh = h;
				if(h < minh)
					minh = h;
			}
		
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				 // find normalized height value in range
				 float val = (height[x][y] - minh) / (maxh - minh);
				 Color col = new Color(val, val, val, 1.0f);
             colorArray[x][y] = col;
				 img.setRGB(x, y, col.getRGB());
			}
	}
   public void setWater(int x,int y){//adds water to a specfic gridpoints
      water[x][y] = 0.03f;
   }
   
  
   public int[] compare (int x,int y,float[][] waterdepth){ 
		//Array that returns lowest position
		int lowest_pos [] = new int[2];
		float [][] waterDep = waterdepth;
		//Array to hold x and y cooridinates of the grid position read in from the method
		int [] pos = new int[2];

		int matrix[][] = positions;

		// Bounds of the grid
		int xWidth = getDimX();		

	
		//int index = x*xWidth + y;
		float height2 = 0;
		float temp = 0;
		int count = 0;
		
		pos[0] = x;
		pos[1] = y;
		height2 = height[x][y]+(waterDep[x][y]);
		
      
		//Creating an area arround (x,y) for comparison
		int int_pos [] = new int[8];
      int_pos[0] = positions[pos[0]][pos[1]-1];
	   int_pos[1] = positions[pos[0]][pos[1]+1];
		int_pos[2] = positions[pos[0]-1][pos[1]];
		int_pos[3] = positions[pos[0]+1][pos[1]];
		int_pos[4] = positions[pos[0]-1][pos[1]-1];
		int_pos[5] = positions[pos[0]+1][pos[1]-1];
		int_pos[6] = positions[pos[0]-1][pos[1]+1];
		int_pos[7] = positions[pos[0]+1][pos[1]+1];
		
	
		//Loop for comparing result
		synchronized(this){
		for(int i = 0;i<8;i++){
			
			if(count != 0){
				
				locate(int_pos[i], pos);
				temp = height[pos[0]][pos[1]]+waterDep[pos[0]][pos[1]];

				if(temp<height2){
					height2 = temp;
					lowest_pos[0] = pos[0];
					lowest_pos[1] = pos[1];
				}

			}
			else{
				
				count = 1;
				locate(int_pos[i], pos);
				temp = height[pos[0]][pos[1]]+waterDep[pos[0]][pos[1]];

				if(temp<height2){
					height2 = temp;
					lowest_pos[0] = pos[0];
					lowest_pos[1] = pos[1];
				}

			}

		}
    }
		
		
		return lowest_pos;
		
	}
   

   void setBlue(int x, int y){ //set pixel to blue
		
		img.setRGB(x, y, Color.blue.getRGB());
	}
   
   void reColor(int x, int y){ // recolor pixel in the terrain
		
		img.setRGB(x, y, colorArray[x][y].getRGB());
	}

   
   
	synchronized public void inreamentDepth( float [][] waterArr, int [] pos){//increment water at (x,y)
		int x = pos[0];
		int y = pos[1];
		float temp = waterArr[x][y];
		waterArr[x][y] = temp+ 0.01f;
	}

	synchronized  public void decrementDepth( float [][] waterArr, int [] pos){//decrement water at (x,y)
		int x = pos[0];
		int y = pos[1];
		float temp = waterArr[x][y];
		if(temp!=0){//checking that water level at (x,y) is not 0
		waterArr[x][y] = temp- 0.01f;
      }
	}
   
   
   public void changePixel(int x,int y){//change pixels surrounding mouse click to blue and returning a new image
     
      int xstart = x-3;
      int xend = x+3;
      int ystart = y-3;
      int yend = y+3;
      
		Color col = new Color(0, 0, 255);
      for(int i =xstart;i<xend;i++)
         for(int j=ystart;j<yend;j++){
            setWater(i,j);
            img.setRGB(i,j,col.getRGB());
         }
   
      //return img;
   }
   
	
	// generate a permuted list of linear index positions to allow a random
	// traversal over the terrain
	void genPermute() {
		permute = new ArrayList<Integer>();
		for(int idx = 0; idx < dim(); idx++)
			permute.add(idx);
		java.util.Collections.shuffle(permute);
	}
	
	// find permuted 2D location from a linear index in the
	// range [0, dimx*dimy)
	void getPermute(int i, int [] loc) {
		locate(permute.get(i), loc);
	}
	
	// read in terrain from file
	void readData(String fileName){ 
		try{ 
			Scanner sc = new Scanner(new File(fileName));
			sc.useLocale(Locale.US);
			// read grid dimensions
			// x and y correpond to columns and rows, respectively.
			// Using image coordinate system where top left is (0, 0).
			dimy = sc.nextInt(); 
			dimx = sc.nextInt();
			
			// populate height grid
			height = new float[dimx][dimy];
			water = new float[dimx][dimy];
         colorArray = new Color[dimx][dimy];
         positions = new int[dimx][dimy];
         int i =0;
			for(int y = 0; y < dimy; y++){
				for(int x = 0; x < dimx; x++)	{
					height[x][y] = sc.nextFloat();
               positions[y][x] = i;
               i++;
               //water[x][y] =0;//set initial waterdepth to 0
				}
            }
				
			sc.close(); 
			
			// create randomly permuted list of indices for traversal 
			genPermute(); 
			
			// generate greyscale heightfield image
			deriveImage();
		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
}