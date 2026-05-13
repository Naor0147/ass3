public class CollisionInfo {
    private Point collisionPoint;
    private Collidable collisionObject;

    public CollisionInfo(Point collisionPoint, Collidable collisionObject) {
        this.collisionPoint = collisionPoint;
        this.collisionObject = collisionObject;
    }

    public Point getCollisionPoint() {
        return this.collisionPoint;
    }

    public Collidable getCollisionObject() {
        return this.collisionObject;
    }
}
