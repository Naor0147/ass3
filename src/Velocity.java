/**
 * Represents the velocity of an object.
 */
public class Velocity {

    private double dx;
    private double dy;

    /**
     * Builds a velocity from dx and dy componants.
     *
     * @param dx change along x-axis.
     * @param dy change along y-axis.
     */
    public Velocity(double dx, double dy) {

        this.dx = dx;
        this.dy = dy;

    }

    /**
     * @return the change value on x-axis.
     */
    public double getDx() {
        return this.dx;
    }

    /**
     * @return the change value on y-axis.
     */
    public double getDy() {
        return this.dy;
    }

    /**
     * Changes the speed value on the x-axis.
     *
    * @param dx the new x-axis speed value
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /**
     * Changes the speed value on the y-axis.
     *
     * @param dy new y-axis speed value
     */
    public void setDy(double dy) {
        this.dy = dy;
    }

    /**
     * Changes the speed value on both axes.
     *
     * @param dx new x-axis speed value
     * @param dy new y-axis speed value
     */
    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Creates velocity from an angle and speed.
     * Angle 0 means up, and positive angles go clock-wise.
     * this beacuse the (0,0) corandion is in the top left
     *
     * @param angle the direction angle in degrees.
     * @param speed the movement speed.
     * @return a new velocity object based on the angle and speed.
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double rad = Math.toRadians(angle);
        double dx = speed * Math.sin(rad);
        double dy = -speed * Math.cos(rad);
        return new Velocity(dx, dy);
    }


    /**
     * Gets a point with position (x,y) and returns a new point
     * with position (x+dx, y+dy).
     *
     * @param p the point to apply the velocity to.
     * @return a new point after applying the velocity.
     */
    public Point applyToPoint(Point p) {
        if (p == null) {
            return new Point(this.dx, this.dy);
        }
        double newX = p.getX() + this.dx;
        double newY = p.getY() + this.dy;
        return new Point(newX, newY);
    }

}
