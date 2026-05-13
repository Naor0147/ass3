/**
 * Info about a collision event.
 */
public class CollisionInfo {
    private Point collisionPoint;
    private Collidable collisionObject;

    /**
     * Make a new collision info.
     *
     * @param collisionPoint  the hit point
     * @param collisionObject the object hit
     */
    public CollisionInfo(Point collisionPoint, Collidable collisionObject) {
        this.collisionPoint = collisionPoint;
        this.collisionObject = collisionObject;
    }

    /**
     * Get the collision point.
     *
     * @return the hit point
     */
    public Point getCollisionPoint() {
        return this.collisionPoint;
    }

    /**
     * Get the collision point.
     *
     * @return the hit point
     */
    public Point collisionPoint() {
        return this.collisionPoint;
    }

    /**
     * Get the hit object.
     *
     * @return the collidable object
     */
    public Collidable getCollisionObject() {
        return this.collisionObject;
    }

    /**
     * Get the hit object.
     *
     * @return the collidable object
     */
    public Collidable collisionObject() {
        return this.collisionObject;
    }
}
