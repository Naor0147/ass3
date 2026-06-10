package gameobjects;

import biuoop.DrawSurface;
import geometry.Point;
import geometry.Velocity;
import geometry.Line;
import collision.Collidable;
import collision.CollisionInfo;
import collision.GameEnvironment;
import game.GameConstants;

/**
 * Ball class with a center point, radius, and color for the game.
 */
public class Ball implements Sprite {

    // fields
    private Point point;
    private int radius;
    private java.awt.Color color;
    private Velocity velocity;

    private GameEnvironment environment;

    // const
    private static final int MAX_RADIUS = 100;

    /**
     * Builds a new Ball given its center point, radius, and color.
     * Sets velocity to zero.
     *
     * @param center the center point of the ball
     * @param r      the radius of the ball
     * @param color  the color of the ball
     */
    public Ball(Point center, int r, java.awt.Color color) {
        this.radius = normalizeRadius(r);

        // make sure the center is valid
        this.point = (center != null) ? center : new Point(this.radius, this.radius);
        this.color = (color != null) ? color : java.awt.Color.BLACK;
        this.velocity = new Velocity(0, 0);

    }

    /**
     * Initializes the ball with a game environment.
     *
     * @param environment the game environment
     */
    public void init(GameEnvironment environment) {
        this.environment = environment;

    }

    /**
     * Gets the x-coordinate of the ball's center.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return (int) this.point.getX();
    }

    /**
     * Gets the y-coordinate of the ball's center.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return (int) this.point.getY();
    }

    /**
     * Gets the radius of the ball.
     *
     * @return the radius
     */
    public int getSize() {
        return this.radius;
    }

    /**
     * Gets the exact point of the ball.
     *
     * @return the center Point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Gets the color of the ball.
     *
     * @return the color
     */
    public java.awt.Color getColor() {
        return this.color;
    }

    /**
     * Gets the velocity of the ball.
     *
     * @return the velocty
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity of the ball.
     *
     * @param vel the new velocity
     */
    public void setVelocity(Velocity vel) {
        this.velocity = (vel != null) ? vel : new Velocity(0, 0);
    }

    /**
     * Sets the velocity of the ball using dx and dy.
     *
     * @param dx the change in x-coordinate
     * @param dy the change in y-coordinate
     */
    public void setVelocity(double dx, double dy) {
        this.velocity = new Velocity(dx, dy);
    }

    /**
     * Draw the ball on the given DrawSurface.
     *
     * @param surface the DrawSurface to draw the ball on
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(getX(), getY(), this.radius);
    }

    /**
     * Normalizes the radius to the accepted range.
     *
     * @param r raw radius.
     * @return normalized radius.
     */
    private int normalizeRadius(int r) {
        if (r < 0) {
            return 0;
        }
        if (r > MAX_RADIUS) {
            return MAX_RADIUS;
        }
        return r;
    }

    /**
     * Sets the center of the ball to a new point.
     *
     * @param newCenter the new center point of the ball
     */
    public void setPoint(Point newCenter) {
        this.point = (newCenter != null) ? newCenter : new Point(this.radius, this.radius);
    }

    /**
     * Sets the center of the ball to a new point.
     *
     * @param x the x-coordinate of the new center
     * @param y the y-coordinate of the new center
     */
    public void setCenter(double x, double y) {
        this.point = new Point(x, y);
    }

    /**
     * Move the ball one step based on its velocity.
     */
    public void moveOneStep() {
        if (this.environment == null) {
            this.point = this.velocity.applyToPoint(this.point);
            return;
        }

        // fix overlaps before moving
        resolveOverlaps();

        // where does the ball want to go this frame
        Point endPoint = this.velocity.applyToPoint(this.point);
        Line trajectory = new Line(this.point, endPoint);
        CollisionInfo collisionInfo = this.environment.getClosestCollision(trajectory);
        if (collisionInfo == null) {
            this.point = endPoint;
            return;
        }

        Point collisionPoint = collisionInfo.getCollisionPoint();
        Collidable collidable = collisionInfo.getCollisionObject();

        double dx = this.velocity.getDx();
        double dy = this.velocity.getDy();
        double speed = Math.sqrt((dx * dx) + (dy * dy));

        // push the ball just behind the hit point so it doesnt get stuck
        if (speed > GameConstants.EPSILON) {
            double backOff = (this.radius + GameConstants.EPSILON) * 1.1;
            double newX = collisionPoint.getX() - (dx / speed) * backOff;
            double newY = collisionPoint.getY() - (dy / speed) * backOff;
            this.point = new Point(newX, newY);
        } else {
            this.point = collisionPoint;
        }

        this.velocity = collidable.hit(this, collisionPoint, this.velocity);
    }

    private void resolveOverlaps() {
        // go through every collidable and push the ball out if needed
        for (Collidable collidable : new java.util.ArrayList<>(this.environment.getCollidables())) {
            fixOverlapWith(collidable);
        }
    }

    private void fixOverlapWith(Collidable collidable) {
        if (collidable == null) {
            return;
        }
        geometry.Rectangle rect = collidable.getCollisionRectangle();
        if (rect == null) {
            return;
        }

        double left = rect.getUpperLeft().getX();
        double right = rect.getBottomRight().getX();
        double top = rect.getUpperLeft().getY();
        double bottom = rect.getBottomRight().getY();

        double cx = this.point.getX();
        double cy = this.point.getY();
        double target = this.radius + GameConstants.EPSILON;

        double closestX = Math.max(left, Math.min(cx, right));
        double closestY = Math.max(top, Math.min(cy, bottom));
        double dx = cx - closestX;
        double dy = cy - closestY;
        double distSq = (dx * dx) + (dy * dy);

        boolean inside = (cx >= left && cx <= right && cy >= top && cy <= bottom);
        if (!inside && distSq >= target * target) {
            return;
        }

        Point collisionPoint;
        double normalX;
        double normalY;

        if (inside) {
            double distLeft = Math.abs(cx - left);
            double distRight = Math.abs(right - cx);
            double distTop = Math.abs(cy - top);
            double distBottom = Math.abs(bottom - cy);

            if (distLeft <= distRight && distLeft <= distTop && distLeft <= distBottom) {
                collisionPoint = new Point(left, cy);
                normalX = -1;
                normalY = 0;
            } else if (distRight <= distTop && distRight <= distBottom) {
                collisionPoint = new Point(right, cy);
                normalX = 1;
                normalY = 0;
            } else if (distTop <= distBottom) {
                collisionPoint = new Point(cx, top);
                normalX = 0;
                normalY = -1;
            } else {
                collisionPoint = new Point(cx, bottom);
                normalX = 0;
                normalY = 1;
            }
        } else {
            double dist = Math.sqrt(distSq);
            if (dist < GameConstants.EPSILON) {
                return;
            }
            collisionPoint = new Point(closestX, closestY);
            normalX = dx / dist;
            normalY = dy / dist;
        }

        if (inside) {
            this.velocity = collidable.hit(this, collisionPoint, this.velocity);
        } else {
            double dot = (this.velocity.getDx() * normalX) + (this.velocity.getDy() * normalY);
            if (dot < 0) {
                this.velocity = collidable.hit(this, collisionPoint, this.velocity);
            }
        }

        this.point = new Point(
                collisionPoint.getX() + normalX * target,
                collisionPoint.getY() + normalY * target);
    }

    /**
     * Update the ball for this frame.
     */
    public void timePassed() {
        moveOneStep();
    }

    /**
     * Add this ball to the game.
     *
     * @param g the game to add to
     */
    public void addToGame(game.Game g) {
        g.addSprite(this);
    }
}
