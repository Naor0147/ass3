package game;

public class Counter {
   private int count;
   // add number to current count.
   public void increase(int number){
         this.count += number;
   }
   // subtract number from current count.
   public void decrease(int number){
         this.count -= number;
   }
   // get current count.
   public int getValue(){
         return this.count;
   }
}
