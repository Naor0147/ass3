/**
 * Represents a 2D point with x and y coordinates.
 */
public class Point {


   private final double x;
   private final double y;

   /**
    * Builds a point from x/y coordinates.
    *
    * @param x the x-coordinate.
    * @param y the y-coordinate.
    */
   public Point(double x, double y) {
      this.x = x;
      this.y = y;
   }

   /**
    * Calculates distance from this point to another one.
    *
    * @param other another point.
    * @return the Euclidean distance to the other poinxt; returns NULL_DISTANCE
    *         when the other point is null.
    */
   public double distance(Point other) {
      if (other == null) {
         return GameConstants.THE_NULL_DISTANCE;
      }
      double dxx = this.x - other.x;
      double dyy = this.y - other.y;
      return Math.sqrt(dxx * dxx + dyy * dyy);
   }

   /**
    * Checks if two points are basically at same spot.
    *
    * @param other another point.
    * @return true if both coordinates are equal up to a small epsilon,
    *         false otherwise.
    */
   public boolean equals(Point other) {
      if (other == null) { // if the point isnt defined
         return false;
      }
      return Math.abs(this.x - other.x) < GameConstants.EPSILON && Math.abs(this.y - other.y) < GameConstants.EPSILON;
   }

   /**
    * @return the x-coordinate.
    */
   public double getX() {
      return this.x;
   }

   /**
    * @return the y-coordinate.
    */
   public double getY() {
      return this.y;
   }

}