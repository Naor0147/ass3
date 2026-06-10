package collision;

import geometry.Line;
import geometry.Point;
import geometry.Rectangle;

/**
 * Holds the collidable objects in the game.
 */
public class GameEnvironment {

    private java.util.List<Collidable> collidables;

    /**
     * Make a new enviroment.
     */
    public GameEnvironment() {
        this.collidables = new java.util.ArrayList<>();
    }

    /**
     * Get all collidables.
     *
     * @return list of collidables
     */
    public java.util.List<Collidable> getCollidables() {
        return this.collidables;
    }

    // add the given collidable to the environment.
    /**
     * Add a collidable.
     *
     * @param c collidable to add
     */
    public void addCollidable(Collidable c) {
        this.collidables.add(c);
    }

    /**
     * gets the closest CollisionInfo to the start of the trajectory. If there is no
     * collision, return null.
     *
     * @param trajectory the path the object is moving along.
     * @return CollisionInfo about the closest collision, or null if no collision
     *         occurs.
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        if (trajectory == null || this.collidables == null || this.collidables.isEmpty()) {
            return null;
        }

        Point closestPoint = null;
        Collidable closestCollidable = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Collidable c : this.collidables) {
            Rectangle rect = c.getCollisionRectangle();
            Point currentIntersection = trajectory.closestIntersectionToStartOfLine(rect);

            if (currentIntersection != null) {
                double distanceToStart = trajectory.start().distance(currentIntersection);
                if (distanceToStart < minDistance) {
                    minDistance = distanceToStart;
                    closestPoint = currentIntersection;
                    closestCollidable = c;
                }
            }
        }

        if (closestPoint == null) {
            return null;
        }
        return new CollisionInfo(closestPoint, closestCollidable);
    }
}
