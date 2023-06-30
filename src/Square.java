import java.awt.Color;
import java.awt.Graphics;
class Square{
   private int x;
   private int y;
   private int side;
   private Color color;

   public Square(int x,int y,int side, Color color){
      this.x = x;
      this.y = y;
      this.side = side;
      this.color = color;
   }
   public int getX(){return x;}
   public int getY(){return y;}
   public int getSide(){return side;}
   public Color getColor(){return color;}
   
   public void draw(Graphics g){
      g.setColor(color);
      g.fillOval(x,y,side,side);
   }
}