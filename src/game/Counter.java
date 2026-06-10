package game;

/**
 * A simple counter that tracks an integer value.
 */
public class Counter {
   private int count;

   /**
    * Add number to current count.
    *
    * @param number the amount to add
    */
   public void increase(int number) {
         this.count += number;
   }

   /**
    * Subtract number from current count.
    *
    * @param number the amount to subtract
    */
   public void decrease(int number) {
         this.count -= number;
   }

   /**
    * Get current count.
    *
    * @return the current value
    */
   public int getValue() {
         return this.count;
   }
}
