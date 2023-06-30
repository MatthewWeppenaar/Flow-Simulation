class gridpoint{
   private int x;
   private int y;
   
   public gridpoint(int x,int y){
      this.x = x;
      this.y = y;
   }
   
   synchronized public int getXpoint(){
      return x;
   }
    
  synchronized public int getYpoint(){
      return y;
   }
   
}