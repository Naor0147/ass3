package gameobjects;

/**
 * Interface for objects that can notify listeners of hit events.
 */
public interface HitNotifier {
   /**
    * Add hl as a listener to hit events.
    *
    * @param hl the listener to add
    */
   void addHitListener(HitListener hl);

   /**
    * Remove hl from the list of listeners to hit events.
    *
    * @param hl the listener to remove
    */
   void removeHitListener(HitListener hl);
}
