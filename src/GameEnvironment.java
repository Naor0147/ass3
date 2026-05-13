public class GameEnvironment {

    private java.util.List<Collidable> collidables;

    public GameEnvironment() {
        this.collidables = new java.util.ArrayList<>();
    }

    public java.util.List<Collidable> getCollidables() {
        return this.collidables;
    }

    // add the given collidable to the environment.
    public void addCollidable(Collidable c) {
        this.collidables.add(c);
    }

    /**
     * gets the closest CollisionInfo to the start of the trajectory. If there is no collision, return null.
     *
     * @param trajectory the path the object is moving along.
     * @return CollisionInfo about the closest collision, or null if no collision
     *         occurs.
     */
    public CollisionInfo getClosestCollision(Line trajectory) {
        // Return null if the trajectory is null or if the environment is empty
        if (trajectory == null || this.collidables == null || this.collidables.isEmpty()) {
            return null;
        }

        Point closestPoint = null;
        Collidable closestCollidable = null;

        // Start with the maximum possible distance
        double minDistance = Double.POSITIVE_INFINITY;

        // Loop through all collidable objects in the environment
        for (Collidable c : this.collidables) {
            Rectangle rect = c.getCollisionRectangle();

            // Get the closest intersection point with the current collidable's rectangle
            Point currentIntersection = trajectory.closestIntersectionToStartOfLine(rect);

            // If there is an intersection, check if it's the closest one found so far
            if (currentIntersection != null) {
                double distanceToStart = trajectory.start().distance(currentIntersection);

                // Update the minimum distance and the corresponding collision details
                if (distanceToStart < minDistance) {
                    minDistance = distanceToStart;
                    closestPoint = currentIntersection;
                    closestCollidable = c;
                }
            }
        }

        // If no intersection was found after checking all collidables, return null
        if (closestPoint == null) {
            return null;
        }

        // Return the collision information containing the point and the object hit
        return new CollisionInfo(closestPoint, closestCollidable);
    }
}