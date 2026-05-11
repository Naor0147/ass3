import biuoop.DrawSurface;

/**
 * Represents an rectangle.
 */
public class Rectangle {

    private Point pointLeftTop;
    private Point pointRightBottom;
    private final Line[] lines = new Line[4];

    private java.awt.Color color = java.awt.Color.BLACK;

    /**
     * Creates a rectangle from two corner coordinates.
     *
     * @param x1 first corner x-coordinate
     * @param y1 first corner y-coordinate
     * @param x2 opposite corner x-coordinate
     * @param y2 opposite corner y-coordinate
     */
    public Rectangle(int x1, int y1, int x2, int y2) {
        setCorners(new Point(x1, y1), new Point(x2, y2));
        updateRectangleLines();
    }

    /**
     * Creates a rectangle from an upper-left corner, width, and height.
     *
     * @param upperLeft the upper-left corner
     * @param width     the rectangle width
     * @param height    the rectangle height
     */
    public Rectangle(Point upperLeft, int width, int height) {
        setCorners(upperLeft, new Point(upperLeft.getX() + width, upperLeft.getY() + height));
        updateRectangleLines();
    }

    /**
     * Creates a rectangle from two corner points.
     *
     * @param upperLeft the upper-left corner
     * @param lowerRight the lower-right corner
     */
    public Rectangle(Point upperLeft, Point lowerRight) {
        setCorners(upperLeft, lowerRight);
        updateRectangleLines();
    }

    /**
     * Noramlize the corners , in a case of wrong corners set them to (0,0).
     *
     * @param p1 point 1
     * @param p2 point 2
     */
    private void setCorners(Point p1, Point p2) {
        if (p1 == null) {
            p1 = new Point(0, 0);
        }
        if (p2 == null) {
            p2 = new Point(0, 0);
        }
        double left = Math.min(p1.getX(), p2.getX());
        double right = Math.max(p1.getX(), p2.getX());
        double top = Math.min(p1.getY(), p2.getY());
        double bottom = Math.max(p1.getY(), p2.getY());

        this.pointLeftTop = new Point(left, top);
        this.pointRightBottom = new Point(right, bottom);
        updateRectangleLines();
    }

    /**
     * Updates the lines of the rectangle.
     */
    private void updateRectangleLines() {
        if (this.pointLeftTop == null || this.pointRightBottom == null) {
            return;
        }
        double leftX = this.pointLeftTop.getX();
        double topY = this.pointLeftTop.getY();
        double rightX = this.pointRightBottom.getX();
        double bottomY = this.pointRightBottom.getY();

        lines[0] = new Line(leftX, topY, rightX, topY);
        lines[1] = new Line(rightX, topY, rightX, bottomY);
        lines[2] = new Line(rightX, bottomY, leftX, bottomY);
        lines[3] = new Line(leftX, bottomY, leftX, topY);
    }


    /**
     * Gets the left top corner of the rectangle.
     * @return the left top corner pount
     */
    public Point getLeftTop() {
        return this.pointLeftTop;
    }
    /**
     * Gets the right bottom corner of the rectangle.
     * @return the right botom cornr point
     */
    public Point getRightBottom() {
        return this.pointRightBottom;
    }
    /**
     * Gets the rectangle lines as line array.
     *
     * @return the rectangle lines
     */
    public Line[] getLines() {
        return lines;
    }

    /**
     * Gets the rectangle color.
     *
     * @return the color
     */
    public java.awt.Color getColor() {
        return color;
    }

    /**
     * Sets the rectangle color.
     *
     * @param color the new color
     */
    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    /**
     * Draws the rectangle on the given surface.
     *
     * @param surface the DrawSurface to draw on
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        int width = (int) (pointRightBottom.getX() - pointLeftTop.getX());
        int height = (int) (pointRightBottom.getY() - pointLeftTop.getY());
        surface.fillRectangle((int) pointLeftTop.getX(), (int) pointLeftTop.getY(), width, height);
    }



    /**
     * Check if point is inside the rectangle.
     *
     * @param point the point to check
     * @return true if the point is inside the rectangle, false otherwise
     */
    public boolean isPointInside(Point point) {
        if (point == null) {
            return false;
        }
        double x = point.getX();
        double y = point.getY();
        boolean insideX = (x >= pointLeftTop.getX()) && (x <= pointRightBottom.getX());
        boolean insideY = (y >= pointLeftTop.getY()) && (y <= pointRightBottom.getY());
        return insideX && insideY;
    }
}
