
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Graphics;
class WaterclickListener extends MouseAdapter{
   private FlowPanel panel;
   public WaterclickListener(FlowPanel panel){
      this.panel = panel;
   } 
   
   @Override
   public void mouseClicked(MouseEvent e){

      panel.addSquare(new gridpoint(e.getX(),e.getY()));//add to paint to the terrain image 
      panel.fill();//color point and assign water value
   }  
}