package collision;

import geometry.Rectangle;
import geometry.Point;
import geometry.Velocity;
import gameobjects.Ball;

/**
 * Things the ball can collide with.
 */
public interface Collidable {
   /**
    * Return the collision shape.
    *
    * @return the rect shape
    */
   Rectangle getCollisionRectangle();

   /**
    * Notify about a hit and return the new velocity.
    *
    * @param collisionPoint  point of the hit
    * @param currentVelocity current ball velocity
    * @return new velocity after the hit
    */
   Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}
