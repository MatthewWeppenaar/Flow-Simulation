import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Scanner;
import java.util.*;
class Water{

   float [][] height; // regular grid of height values
	int dimx, dimy; // data dimensions
	BufferedImage img2; // greyscale image for displaying the terrain top-down

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
		  return img2;
	}
	
	// convert linear position into 2D location in grid
	void locate(int pos, int [] ind)
	{
		ind[0] = (int) pos / dimy; // x
		ind[1] = pos % dimy; // y	
	} 
   
   	void deriveImage()
	{
		img2 = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);
      for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				 // find normalized height value in range
				 
				 Color col = new Color(0, 0, 255);
				 img2.setRGB(x, y, col.getRGB());
			}
	}
   
   BufferedImage changePixel(int x,int y){
		Color col = new Color(0, 0, 255);
      img2.setRGB(x,y,col.getRGB());
      img2.setRGB(x+1,y,col.getRGB());
      img2.setRGB(x-1,y,col.getRGB());
      img2.setRGB(x,y+1,col.getRGB());
      img2.setRGB(x,y-1,col.getRGB());
      return img2;
   }
     
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