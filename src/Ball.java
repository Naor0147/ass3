import biuoop.DrawSurface;

/**
 * Ball class with a center point, radius, and color for the game.
 */
public class Ball {

    // fields
    private Point point;
    private int radius;
    private java.awt.Color color;
    private Velocity velocity;

    // obstacles
    private Line[] obstacles;

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
        if (obstacles == null) {
            this.point = velocity.applyToPoint(this.point);
            return;
        }
        CollisionEngine.resolveBallLineCollision(this, this.obstacles);
    }

    /**
     * Sets the obstacles for the ball.
     *
     * @param obstacles array of obstacle lines
     */
    public void setObstacles(Line[] obstacles) {
        this.obstacles = obstacles;
    }

    /**
     * Adds obstacles to the ball.
     *
     * @param newObstacles array of the new obstacle lines
     */
    public void addObstacles(Line[] newObstacles) {
        if (newObstacles == null) {
            return;
        }
        if (this.obstacles == null) {
            this.obstacles = newObstacles;
            return;

        }
        // combine the old with new
        Line[] combined = new Line[this.obstacles.length + newObstacles.length];
        for (int i = 0; i < this.obstacles.length; i++) {
            combined[i] = this.obstacles[i];
        }
        for (int i = 0; i < newObstacles.length; i++) {
            combined[this.obstacles.length + i] = newObstacles[i];
        }
        this.obstacles = combined;
    }
}