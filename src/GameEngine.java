import java.awt.Color;

/**
 * Shared game utilities.
 */
public final class GameEngine {

    // constants
    private static final int MAX_RADIUS_SIZE = 100;
    private static final int MIN_RADIUS_SIZE = 1;
    private static final int MIN_COLOR_VALUE = 40;
    private static final int MAX_COLOR_VALUE = 255;
    private static final double SAFE_MULTIPLIER_FOR_RANDOM_POINT = 1.2;

    /**
     * maximum ball speed.
     */
    private static final int MAX_BALL_SPEED = 10;

    /**
     * minimum size of a ball that has the minimum speed.
     */
    private static final int MIN_SIZE_BALL_OF_MIN_SPEED = 50;

    /**
     * utility class - should not be instantiated.
     */
    private GameEngine() {
    }

    /**
     * Calcute a velocity based on the radius.
     *
     * @param radius the effective radius used for speed.
     * @return velocity based on radius.
     */
    public static Velocity getVelocityFromRadius(int radius) {
        radius = (radius < MIN_SIZE_BALL_OF_MIN_SPEED) ? radius
                : MIN_SIZE_BALL_OF_MIN_SPEED;
        double divisor = (double) MIN_SIZE_BALL_OF_MIN_SPEED / MAX_BALL_SPEED;
        double speed = Math.max(1.0, MAX_BALL_SPEED - (radius / divisor));
        return new Velocity(speed, speed);
    }

    /**
     * Creates an array of balls with valid radius values.
     *
     * @param intArray array of raw radius values
     * @return array of Balls with valid radius
     */
    public static Ball[] createValidBallsArr(int[] intArray) {
        int[] validBallsRadiusArray = createValidBallsRadiusArr(intArray);
        int validBallsLength = validBallsRadiusArray.length;
        Ball[] balls = new Ball[validBallsLength];

        for (int i = 0; i < validBallsLength; i++) {
            Point ballCenterPoint = getRandomCenterPointForBall(new Point(0, 0),
                    new Point(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT), validBallsRadiusArray[i]);

            balls[i] = new Ball(ballCenterPoint, validBallsRadiusArray[i], getrandomColor());

        }
        return balls;

    }

    /**
     * Returns a random color.
     *
     * @return random color
     */
    public static Color getrandomColor() {
        int redCol = (int) getRandomInt(MIN_COLOR_VALUE, MAX_COLOR_VALUE);
        int greenCol = (int) getRandomInt(MIN_COLOR_VALUE, MAX_COLOR_VALUE);
        int blueCol = (int) getRandomInt(MIN_COLOR_VALUE, MAX_COLOR_VALUE);
        return new Color(redCol, greenCol, blueCol);
    }

    /**
     * Returns a random number in range.
     *
     * @param min min value
     * @param max max value
     * @return random number within the range
     */
    public static double getRandomInt(double min, double max) {
        if (max < min) {
            double temp = max;
            max = min;
            min = temp;
        }
        return (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Parses args into non-negative integers.
     *
     * @param args command-line arguments
     * @return parsed int array, or null when invalid input is detected
     */
    public static int[] parseArgsForPositiveIntegers(String[] args) {
        if (args == null) {
            System.out.println("you need to write integers");
            return null;
        }
        int[] intArray = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            intArray[i] = (int) Double.parseDouble(args[i]);
            if (intArray[i] < 0) {
                System.out.println("all integers must be bigger or eqaul to 0");
                return null;
            }
        }
        return intArray;
    }

    /**
     * Returns a random number in range with a threshold.
     *
     * @param min       min value
     * @param max       max value
     * @param threshold threshold value
     * @return random number within the range using the threshold
     */
    private static double getRandomIntWithThershold(double min, double max, double threshold) {
        if (max < min) {
            double temp = max;
            max = min;
            min = temp;
        }
        return getRandomInt(min + threshold, max - threshold);

    }

    /**
     * Returns a random point in a certain area.
     *
     * @param p1     the first corner of the bounding rectangle
     * @param p2     the second corner of the bounding rectangle
     * @param radius the radius of the ball
     * @return a random Point within the valid area
     */
    public static Point getRandomCenterPointForBall(Point p1, Point p2, int radius) {

        // increase the radius to make sure the ball is fully visible within the bounds
        radius = (int) (radius * SAFE_MULTIPLIER_FOR_RANDOM_POINT);
        double x1 = p1.getX();
        double x2 = p2.getX();

        double y1 = p1.getY();
        double y2 = p2.getY();

        double x = getRandomIntWithThershold(x1, x2, radius);
        double y = getRandomIntWithThershold(y1, y2, radius);

        return new Point(x, y);
    }

    /**
     * Returns a random point in a certain area and outside of obstacles.
     *
     * @param windowRectangle the boundry rectangle to sarch within
     * @param radius          the radius of the ball
     * @param obstacles       array of obstacles to avoid
     * @return a random Point within the valid area and outside obstacles
     */
    public static Point randomPointInRecWithObstacls(Rectangle windowRectangle, Rectangle[] obstacles,
            int radius) {
        Point p = new Point(0, 0);
        boolean isInObstacle = true;
        int maxAttempts = 100;
        int attempts = 0;
        int safty = radius;
        while (isInObstacle && attempts < maxAttempts) {
            attempts++;
            p = getRandomCenterPointForBall(windowRectangle.getLeftTop(), windowRectangle.getRightBottom(), radius);
            isInObstacle = false;

            for (Rectangle obstacle : obstacles) {
                // Expand the boundaries of the obstacle by the ball's radius (threshold)
                double expandedMinX = obstacle.getLeftTop().getX() - safty;
                double expandedMaxX = obstacle.getRightBottom().getX() + safty;
                double expandedMinY = obstacle.getLeftTop().getY() - safty;
                double expandedMaxY = obstacle.getRightBottom().getY() + safty;

                // If the ball's center is inside this expanded bounding box, part of the ball
                // overlaps the obstacle.
                if (p.getX() > expandedMinX && p.getX() < expandedMaxX
                        && p.getY() > expandedMinY && p.getY() < expandedMaxY) {

                    isInObstacle = true;
                    break;
                }
            }

        }

        return p;
    }

    /**
     * creat a randm point insde a rect.
     *
     * @param rect      to be insde
     * @param threshold to make sure the point is not to close to the edge of the
     *                  rect
     * @return a random point inside the rectangle
     */
    public static Point createRandomPointInRect(Rectangle rect, int threshold) {
        return GameEngine.getRandomCenterPointForBall(rect.getLeftTop(), rect.getRightBottom(), threshold);
    }

    /**
     * Returns the number of valid balls.
     *
     * @param intArray array of ball radii
     * @return number of valid balls
     */
    private static int getNumberOfValidBalls(int[] intArray) {
        int n = 0;
        for (int i = 0; i < intArray.length; i++) {
            if (intArray[i] > 0) {
                n++;
            }
        }
        return n;
    }

    /**
     * Normalizes a value inside the accepted range.
     *
     * @param value the value to normalize
     * @param min   the minimum accepted value
     * @param max   the maximum accepted value
     * @return the normalized value
     */
    private static int normalizeValueInsideRange(int value, int min, int max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;

    }

    /**
     * Creates a valid ball radius array.
     * Normalizes the radius values to be inside the accepted range.
     *
     * @param intArray array of raw radius values
     * @return valid ball radius array
     */
    private static int[] createValidBallsRadiusArr(int[] intArray) {
        int validBalls = getNumberOfValidBalls(intArray);
        int[] validBallsRadiusArr = new int[validBalls];
        int j = 0;
        for (int i = 0; i < intArray.length; i++) {
            if (intArray[i] > 0) {
                validBallsRadiusArr[j] = normalizeValueInsideRange(intArray[i], MIN_RADIUS_SIZE, MAX_RADIUS_SIZE);
                j++;
            }
        }
        return validBallsRadiusArr;
    }

}
