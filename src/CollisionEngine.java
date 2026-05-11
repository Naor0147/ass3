/**
 * Handles the collision aspect of the game.
 */
public class CollisionEngine {

    private static final int MAX_BOUNCES_PER_FRAME = 3;
    private static final double ANTI_STICK_PUSH_DISTANCE = 0.001;
    private static final double IMPOSSIBLE_COLLISION_FRACTION_TEMP = 2.0; // any value > 1.0 works, since we only
    // consider fractions between 0 and 1
    private static final double MIN_TIME_REMAINING_THRESHOLD = 0.001; // to prevent infinite loops

    /**
     * Helper class to store collison results.
     */
    private static final class CollisionResult {
        private double fraction;
        private Line wallHit;
        private double normalX;
        private double normalY;

        private CollisionResult() {
            // i set fractionOfFrameToClosestCollision
            // to IMPOSSIBLE_COLLISION_FRACTION_TEMP
            // because it is impossible to have a collision after the end of the frame (1.0)
            this.fraction = IMPOSSIBLE_COLLISION_FRACTION_TEMP;
        }

        private void updateIfCloser(double hitFraction, Line wall, double hitNormalX, double hitNormalY) {
            if (hitFraction < this.fraction) {
                this.fraction = hitFraction;
                this.wallHit = wall;
                this.normalX = hitNormalX;
                this.normalY = hitNormalY;
            }
        }
    }

    /**
     * Resolves the collision between a ball and line segments.
     * and update ball Velocity and position accordingly.
     *
     * @param ball      the ball involved in the collision
     * @param wallArray the walls involved in the collision
     */
    public static void resolveBallLineCollision(Ball ball, Line[] wallArray) {
        double frameTimeRemaining = 1.0;
        int bounceCountThisFrame = 0;
        double ballRadius = ball.getSize();

        while (frameTimeRemaining > MIN_TIME_REMAINING_THRESHOLD
                && bounceCountThisFrame < MAX_BOUNCES_PER_FRAME) {
            double ballStartPosX = ball.getPoint().getX();
            double ballStartPosY = ball.getPoint().getY();

            // speed
            double ballVx = ball.getVelocity().getDx() * frameTimeRemaining;
            double ballVy = ball.getVelocity().getDy() * frameTimeRemaining;

            // end position of the ball if no collisions occur in this step
            double ballIntendedEndX = ballStartPosX + ballVx;
            double ballIntendedEndY = ballStartPosY + ballVy;
            // variables for the closest wall to the ball.
            // nd this way we can easily check if we found a collision or not.
            Point ballIntendedEnd = new Point(ballIntendedEndX, ballIntendedEndY);
            CollisionResult collision = findClosestCollision(wallArray, ball.getPoint(), ballVx, ballVy,
                    ballIntendedEnd, ballRadius);

            if (collision.wallHit == null) {
                ball.setCenter(ballIntendedEndX, ballIntendedEndY);
                break;
            }

            // if collsion happend
            // we calculate impact point
            double impactPositionX = ballStartPosX + (ballVx * collision.fraction);
            double impactPositionY = ballStartPosY + (ballVy * collision.fraction);

            // I added ANTI_STICK_PUSH_DISTANCE so the.
            // ball dont get stuck inside the wall
            double finalPosX = impactPositionX + (collision.normalX * ANTI_STICK_PUSH_DISTANCE);
            double finalPosY = impactPositionY + (collision.normalY * ANTI_STICK_PUSH_DISTANCE);
            ball.setCenter(finalPosX, finalPosY);

            // implemnt a better verion than reqiurd for better accrecy
            // foor futre profing if line at anglend
            double currentDx = ball.getVelocity().getDx();
            double currentDy = ball.getVelocity().getDy();
            double dotProduct = (currentDx * collision.normalX) + (currentDy * collision.normalY);

            // Calculate the new reflected velocity: V_new = V - 2 * (V . N) * N
            double newDx = currentDx - 2 * dotProduct * collision.normalX;
            double newDy = currentDy - 2 * dotProduct * collision.normalY;

            ball.setVelocity(newDx, newDy);
            // calac the time spent reaching this wall and do decut from 1;
            frameTimeRemaining = frameTimeRemaining * (1.0 - collision.fraction);
            bounceCountThisFrame++;
        }

    }

    private static CollisionResult findClosestCollision(Line[] wallArray, Point ballStart,
            double ballVx, double ballVy, Point ballIntendedEnd, double ballRadius) {

        CollisionResult result = new CollisionResult();
        double ballStartPosX = ballStart.getX();
        double ballStartPosY = ballStart.getY();
        double ballIntendedEndX = ballIntendedEnd.getX();
        double ballIntendedEndY = ballIntendedEnd.getY();

        for (Line wall : wallArray) {
            double wallStartX = wall.start().getX();
            double wallStartY = wall.start().getY();
            double wallEndX = wall.end().getX();
            double wallEndY = wall.end().getY();

            double wallDirectionX = wallEndX - wallStartX;
            double wallDirectionY = wallEndY - wallStartY;

            double wallLength = Math.sqrt(wallDirectionX * wallDirectionX + wallDirectionY * wallDirectionY);

            if (wallLength < GameConstants.EPSILON) {
                continue; // skip zero-length walls
            }

            double wallNormalX = -wallDirectionY / wallLength;
            double wallNormalY = wallDirectionX / wallLength;

            double vectorFromWallToBallX = ballStartPosX - wallStartX;
            double vectorFromWallToBallY = ballStartPosY - wallStartY;
            if ((wallNormalX * vectorFromWallToBallX + wallNormalY * vectorFromWallToBallY) < 0) {
                wallNormalX = -wallNormalX;
                wallNormalY = -wallNormalY;
            }

            double offsetWallStartX = wallStartX + (wallNormalX * ballRadius);
            double offsetWallStartY = wallStartY + (wallNormalY * ballRadius);
            double offsetWallEndX = wallEndX + (wallNormalX * ballRadius);
            double offsetWallEndY = wallEndY + (wallNormalY * ballRadius);

            double intersectionDenominator = (ballStartPosX - ballIntendedEndX)
                    * (offsetWallStartY - offsetWallEndY)
                    - (ballStartPosY - ballIntendedEndY) * (offsetWallStartX - offsetWallEndX);

            if (Math.abs(intersectionDenominator) < GameConstants.EPSILON) {
                continue;
            }

            double hitFractionAlongBallPath = ((ballStartPosX - offsetWallStartX)
                    * (offsetWallStartY - offsetWallEndY)
                    - (ballStartPosY - offsetWallStartY) * (offsetWallStartX - offsetWallEndX))
                    / intersectionDenominator;

            double hitFractionAlongWall = ((ballStartPosX - offsetWallStartX)
                    * (ballStartPosY - ballIntendedEndY)
                    - (ballStartPosY - offsetWallStartY) * (ballStartPosX - ballIntendedEndX))
                    / intersectionDenominator;

            if (hitFractionAlongBallPath >= 0
                    && hitFractionAlongBallPath <= 1
                    && hitFractionAlongWall >= 0
                    && hitFractionAlongWall <= 1) {
                result.updateIfCloser(hitFractionAlongBallPath, wall, wallNormalX, wallNormalY);
            }

            Point[] corners = new Point[] {wall.start(), wall.end()};

            for (Point corner : corners) {
                double cornerX = corner.getX();
                double cornerY = corner.getY();

                double theAPart = (ballVx * ballVx) + (ballVy * ballVy);
                double theBPart = 2
                        * ((ballVx * (ballStartPosX - cornerX)) + (ballVy * (ballStartPosY - cornerY)));
                double theCPart = ((ballStartPosX - cornerX) * (ballStartPosX - cornerX))
                        + ((ballStartPosY - cornerY) * (ballStartPosY - cornerY))
                        - (ballRadius * ballRadius);

                if (theAPart < GameConstants.EPSILON) {
                    continue;
                }

                double discriminant = (theBPart * theBPart) - (4 * theAPart * theCPart);

                if (discriminant >= 0) {
                    double hitFractionTime1 = (-theBPart - Math.sqrt(discriminant)) / (2 * theAPart);
                    double hitFractionTime2 = (-theBPart + Math.sqrt(discriminant)) / (2 * theAPart);

                    double closestCornerHitFraction = -1;
                    if (hitFractionTime1 >= 0 && hitFractionTime1 <= 1) {
                        closestCornerHitFraction = hitFractionTime1;
                    }
                    if (hitFractionTime2 >= 0
                            && hitFractionTime2 <= 1
                            && (hitFractionTime2 < closestCornerHitFraction || closestCornerHitFraction < 0)) {
                        closestCornerHitFraction = hitFractionTime2;
                    }

                    if (closestCornerHitFraction >= 0) {
                        double impactPositionX = ballStartPosX + (ballVx * closestCornerHitFraction);
                        double impactPositionY = ballStartPosY + (ballVy * closestCornerHitFraction);
                        double cornerNormalDirX = impactPositionX - cornerX;
                        double cornerNormalDirY = impactPositionY - cornerY;

                        double cornerNormalLen = Math.sqrt((cornerNormalDirX * cornerNormalDirX)
                                + (cornerNormalDirY * cornerNormalDirY));

                        if (cornerNormalLen < GameConstants.EPSILON) {
                            continue;
                        }
                        double cornerNormalX = cornerNormalDirX / cornerNormalLen;
                        double cornerNormalY = cornerNormalDirY / cornerNormalLen;

                        result.updateIfCloser(closestCornerHitFraction, wall, cornerNormalX, cornerNormalY);
                    }
                }
            }

        }

        return result;
    }

}
