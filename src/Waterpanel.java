import java.util.List;
import java.util.LinkedList;
import javax.swing.JPanel;
import java.awt.Graphics;

class Waterpanel extends JPanel{
   private List<Square> squares = new LinkedList<Square>();
   public void addSquare(Square square){
      squares.add(square);
   }
   @Override
   public void paint(Graphics g){
      for(Square c: squares){
         c.draw(g);
         this.repaint();
      }
   }
}