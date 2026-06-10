package geometry;

import game.GameConstants;

/**
 * Simple line segment between two points.
 */
public class Line {

    private final Point start;
    private final Point end;

    /**
     * Builds a line segment from two points.
     * Tries to keep it valid even when one of the points is null.
     *
     * @param start start point.
     * @param end   end point.
     */
    public Line(Point start, Point end) {
        Point safeStrt;
        if (start == null) {
            safeStrt = new Point(0, 0);
        } else {
            safeStrt = start;
        }

        Point safeEdn;
        if (end == null) {
            safeEdn = safeStrt;
        } else {
            safeEdn = end;
        }
        this.start = safeStrt;
        this.end = safeEdn;
    }

    /**
     * Builds a line segment from raw coordinates.
     *
     * @param x1 x-coordinate of the start point.
     * @param y1 y-coordinate of the start point.
     * @param x2 x-coordinate of the end point.
     * @param y2 y-coordinate of the end point.
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    /**
     * Gets the length of this segment.
     *
     * @return the segment length.
     */
    public double length() {
        return this.start.distance(this.end);
    }

    /**
     * Gets the midpoint of this segment.
     *
     * @return the midpoint of the segment.
     */
    public Point middle() {
        return new Point((this.start.getX() + this.end.getX()) / 2, (this.start.getY() + this.end.getY()) / 2);
    }

    /**
     * Gets the start point of this segment.
     *
     * @return the start point.
     */
    public Point start() {
        return this.start;
    }

    /**
     * Gets the end point of this segment.
     *
     * @return the end point.
     */
    public Point end() {
        return this.end;
    }

    /**
     * Checks if this segment intersects another one.
     *
     * @param other another line segment.
     * @return true when they intersect (including overlap),
     *         false otherwise.
     */
    public boolean isIntersecting(Line other) {
        if (other == null) {
            return false;
        }
        if (this.intersectionWith(other) != null) {
            return true;
        }
        double determnt = getDeterminant(other);
        if (Math.abs(determnt) < GameConstants.EPSILON) {
            return isPointOnSegment(this.end, other)
                    || isPointOnSegment(this.start, other)
                    || isPointOnSegment(other.start, this)
                    || isPointOnSegment(other.end, this);
        }
        return false;
    }

    /**
     * Checks whether this line intersects both of two given lines.
     *
     * @param other1 first line segment.
     * @param other2 second line segment.
     * @return true if this line intersects both given lines,
     *         false otherwise.
     */
    public boolean isIntersecting(Line other1, Line other2) {
        return this.isIntersecting(other1) && this.isIntersecting(other2);
    }

    /**
     * Finds the intersection point with another line segment.
     *
     * @param other another line segment.
     * @return the intersection point when there is exactly one; otherwise
     *         null. If they overlap with infinite points this returns null.
     */
    public Point intersectionWith(Line other) {
        if (other == null) {
            return null;
        }

        // handle parallel or collinear lines
        double determnt = getDeterminant(other);
        if (Math.abs(determnt) < GameConstants.EPSILON) {
            if (this.start.equals(other.start)
                    && !isPointOnSegment(this.end, other)
                    && !isPointOnSegment(other.end, this)) {
                return this.start;
            }
            if (this.start.equals(other.end)
                    && !isPointOnSegment(this.end, other)
                    && !isPointOnSegment(other.start, this)) {
                return this.start;
            }
            if (this.end.equals(other.start)
                    && !isPointOnSegment(this.start, other)
                    && !isPointOnSegment(other.end, this)) {
                return this.end;
            }
            if (this.end.equals(other.end)
                    && !isPointOnSegment(this.start, other)
                    && !isPointOnSegment(other.start, this)) {
                return this.end;
            }
            return null;
        }

        // math forumal that check if intersction lies in the fintie line
        double tNumm = (other.start().getX() - this.start.getX()) * (other.end().getY() - other.start().getY())
                - (other.start().getY() - this.start.getY()) * (other.end().getX() - other.start().getX());
        double uNumm = (other.start().getX() - this.start.getX()) * (this.end.getY() - this.start.getY())
                - (other.start().getY() - this.start.getY()) * (this.end.getX() - this.start.getX());
        double t = tNumm / determnt;
        double u = uNumm / determnt;

        if (t >= -GameConstants.EPSILON && t <= 1.0 + GameConstants.EPSILON
                && u >= -GameConstants.EPSILON && u <= 1.0 + GameConstants.EPSILON) {
            double interX = this.start.getX() + t * (this.end.getX() - this.start.getX());
            double interY = this.start.getY() + t * (this.end.getY() - this.start.getY());
            return new Point(interX, interY);
        }
        return null;
    }

    /**
     * Checks if this line and another line represent the same segment, regardless
     * of direction.
     *
     * @param other another line segment.
     * @return true if both segments are equal, false otherwise.
     */
    public boolean equals(Line other) {
        if (other == null) {
            return false;
        }
        return (this.start.equals(other.start) && this.end.equals(other.end))
                || (this.start.equals(other.end) && this.end.equals(other.start));
    }

    /**
     * Helper that computes determinant for intersection math.
     * if the result is in the GameConstants.EPSILON threshold the line are Parallel
     * or colliner
     *
     * @param other another line segment.
     * @return determinant of the two segment direction vectors.
     */
    private double getDeterminant(Line other) {
        return (this.end.getX() - this.start.getX()) * (other.end().getY() - other.start().getY())
                - (this.end.getY() - this.start.getY()) * (other.end().getX() - other.start().getX());
    }

    /**
     * Checks whether a point lies on a given segment.
     *
     * @param point point to check.
     * @param line  segment on which to check.
     * @return true if the point lies on the segment, false
     *         otherwise.
     */
    private boolean isPointOnSegment(Point point, Line line) {
        if (point == null || line == null) {
            return false;
        }
        double dst1 = point.distance(line.start());
        double dst2 = point.distance(line.end());
        return Math.abs((dst1 + dst2) - line.length()) < GameConstants.EPSILON;
    }

    /**
     * If this line does not intersect with the rectangle, return null.
     * Otherwise, return the closest intersection point to the
     * start of the line.
     *
     * @param rect rectangle to check
     * @return closest intersection point to the start of the line
     */
    public Point closestIntersectionToStartOfLine(geometry.Rectangle rect) {
        if (rect == null) {
            return null;
        }
        Point minIntersection = null;
        double minDst = Double.POSITIVE_INFINITY;
        for (Point intersection : rect.intersectionPoints(this)) {
            double dst = this.start.distance(intersection);
            if (dst < minDst) {
                minDst = dst;
                minIntersection = intersection;
            }
        }
        return minIntersection;
    }

    /**
     * Calculate the distance from a point to this line segment.
     *
     * @param point the point to measure from
     * @return distance from the point to this line segment, or
     *         GameConstants.THE_NULL_DISTANCE if the point is null.
     */
    public double distance(Point point) {
        if (point == null) {
            return GameConstants.THE_NULL_DISTANCE;
        }
        double x1 = this.start.getX();
        double y1 = this.start.getY();
        double x2 = this.end.getX();
        double y2 = this.end.getY();
        double pointX = point.getX();
        double pointY = point.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;
        double lineLengthSquared = dx * dx + dy * dy;
        if (lineLengthSquared < GameConstants.EPSILON) {
            return point.distance(this.start);
        }

        double t = ((pointX - x1) * dx + (pointY - y1) * dy) / lineLengthSquared;
        t = Math.max(0, Math.min(1, t));

        double closestX = x1 + t * (x2 - x1);
        double closestY = y1 + t * (y2 - y1);

        return point.distance(new Point(closestX, closestY));
    }

}
