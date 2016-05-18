/*
 * Author:Aral Tasher, ataser2010@my.fit.edu
 * Course: CSE1002, Section 01 Spring, 2012
 * Project: pong
 */
import java.awt.event.KeyEvent;
import java.util.Random;

public class Pong {

    private static final double FIELD_START_POINT = .1;
    private static final double FIELD_HEIGHT = .95;
    private static final double SPACE_OF_LINES = 0.02;
    private static final double TOP_DASH_LINES = 1.05;
    private static final double LOW_DASH_LINES = -.8;
    private static final double LOWER_RANGE = -.7;
    private static final double UPPER_RANGE = .9;
    private static final double MOVE_PADDLE = 0.05;
    private static final double SCORE_Y = -.95;
    private static final double SCORE_X = .75;
    private static final double MESSAGE_COORDINATE = .5;
    private static final int FPS = 20;
    private static final double PAD_LENG = .12;
    private static final int END_GAME_VALUE = 7;
    private static final double MAX_FIELDWIDTH = 1.52;
    private static final double MIN_FIELDPOINT = -.82;
    private static final double ADD_SPEED = .005;
    private static final int PI = 180;
    private static final double SPEED = .01;
    private static final int THIRTY = 30;
    private static final int ANGLE = 60;
    private static final double POS_SIDE = 1.5;
    private static final double NEGATIVE_SIDE = -POS_SIDE;
    private static final int CANVAS_Y = 480;
    private static final int CANVAS_X = 640;
    private static final double RADIUS = .025;
    private static final Random RNG = new Random (Long.getLong ("seed",
            System.nanoTime ()));

    public static void main (final String[] args) {

        StdDraw.setCanvasSize (CANVAS_X, CANVAS_Y);
        StdDraw.setXscale (NEGATIVE_SIDE, POS_SIDE);
        StdDraw.setYscale (-1, 1);
        playGame ();
    }

    // method for playing the game
    public static void playGame () {
        // original positions for the ball is assigned
        double posX = 0, posY = 0;
        // angle for the ball is assigned,
        double balla = Math.toRadians ( ( RNG.nextDouble () * ANGLE) - THIRTY);
        double ballv = SPEED;
        if (RNG.nextBoolean ()) {
            balla += Math.toRadians (PI);
        }
        // positions of the paddles are assigned, together with scores
        double leftPaddleJ = 0, rightPaddleJ = 0;
        final double leftPaddleI = -1.43, rightPaddleI = 1.43;
        int score1 = 0, score2 = 0;
        drawBackground (score1, score2);
        boolean gamePlaying = true;
        // while loop to play the game while all the conditions are true(e.g.
        // none of the players reached 7)
        while (gamePlaying) {
            // if function to test if the ball hits the left paddle.If it does
            // the ball bounces back with an angle
            if (testPaddle (posX, posY, balla, ballv, leftPaddleJ, leftPaddleI
                    + SPEED)) {
                final double x = Math.cos (balla), y = Math.sin (balla);
                balla = Math.atan2 (y, Math.abs (x));
                ballv += ADD_SPEED;
            }
            // same if function for the right paddle.
            if (testPaddle (posX, posY, balla, ballv, rightPaddleJ,
                    rightPaddleI - SPEED)) {
                final double x = Math.cos (balla), y = Math.sin (balla);
                balla = Math.atan2 (y, -Math.abs (x));
                ballv += ADD_SPEED;
            }
            // if function to keep the balls within the borders.
            if ( ( ( posY + ( ballv * Math.sin (balla))) > 1)
                    || ( ( posY + ( ballv * Math.sin (balla))) < MIN_FIELDPOINT)) {
                final double x = Math.cos (balla), y = Math.sin (balla);
                balla = Math.atan2 (-y, x);
            }
            // final x & y positions are assigned to the ball
            posX = posX + ( ballv * Math.cos (balla));
            posY = posY + ( ballv * Math.sin (balla));
            // if function to test if the ball passed the right paddle. In this
            // case the left player's (Player1) score increases by 1 and the the
            // game starts again with the paddles and the ball back in the
            // original position
            if (posX > MAX_FIELDWIDTH) {
                score1++;
                posX = 0;
                posY = 0;
                balla = Math.toRadians ( ( RNG.nextDouble () * ANGLE) - THIRTY);
                ballv = SPEED;
                if (RNG.nextBoolean ()) {
                    balla += Math.toRadians (PI);
                }
                leftPaddleJ = 0;
                rightPaddleJ = 0;

            }
            // same if function for the left paddle. But in this case the right
            // player's (Player2) score increases by 1.
            if (posX < -MAX_FIELDWIDTH) {
                score2++;
                posX = 0;
                posY = 0;
                balla = Math.toRadians ( ( RNG.nextDouble () * ANGLE) - THIRTY);
                ballv = SPEED;
                if (RNG.nextBoolean ()) {
                    balla += Math.toRadians (PI);
                }
                leftPaddleJ = 0;
                rightPaddleJ = 0;
            }
            StdDraw.setPenColor (StdDraw.BLACK);
            // draws the background
            drawBackground (score1, score2);
            // draws position of the ball
            StdDraw.setPenRadius (RADIUS);
            StdDraw.point (posX, posY);
            // assigns the new positions of the paddles as the keys are pressed
            // and draws them
            leftPaddleJ = drawLeftPaddle (leftPaddleJ);
            rightPaddleJ = drawRightPaddle (rightPaddleJ);
            StdDraw.filledRectangle (leftPaddleI, leftPaddleJ, SPEED, PAD_LENG);
            StdDraw.filledRectangle (rightPaddleI, rightPaddleJ, SPEED,
                    PAD_LENG);
            // if any of the players reaches the end game value it calls for the
            // restart game method in which, the method ends the game and asks
            // the player to restart the
            // game if they wish to.
            if ( ( score1 == END_GAME_VALUE) || ( score2 == END_GAME_VALUE)) {

                gamePlaying = false;
                restartGame (score1, score2);
            }
            StdDraw.show (FPS);
        }

    }

    // method to restart the game. Prints the winner and asks to start the game
    // over again. while loop is added so that the question will be asked until
    // the player presses the correct key
    public static void restartGame (final int score1, final int score2) {
        final boolean test = true;
        while (test) {
            StdDraw.clear (StdDraw.BLACK);
            if (score1 == END_GAME_VALUE) {
                StdDraw.setPenColor (StdDraw.PINK);
                StdDraw.text (0, MESSAGE_COORDINATE, "Player1 Wins!");
            }
            if (score2 == END_GAME_VALUE) {
                StdDraw.setPenColor (StdDraw.PINK);
                StdDraw.text (0, MESSAGE_COORDINATE, "Player2 Wins!");
            }
            StdDraw.text (0, 0, "Press [R] To Restart The Game");

            if (StdDraw.isKeyPressed (KeyEvent.VK_R)) {
                playGame ();
            }
            StdDraw.show (FPS);
        }

    }

    // tests if the ball is going to hit the paddle. compares the position of
    // the ball, taking account the speed and angle of it, and check if the x&y
    // coordinate of the ball is in range with the paddle.Basically if they will
    // collide.
    public static boolean testPaddle (final double posX, final double posY,
            final double balla, final double ballv, final double paddleY,
            final double paddleX) {
        final double posX2 = ( ballv * Math.cos (balla)) + posX, posY2 = ( ballv * Math
                .sin (balla)) + posY;
        if ( ( ( posX2 > paddleX) && ( posX < paddleX))
                || ( ( posX2 < paddleX) && ( posX > paddleX))) {
            final double slope = ( ( posY2 - posY) / ( posX2 - posX));
            final double b = posY - ( slope * posX);
            final double yTest = ( slope * paddleX) + b;
            if ( ( yTest >= ( paddleY - PAD_LENG))
                    && ( yTest <= ( paddleY + PAD_LENG))) {
                return true;
            }
        }
        return false;
    }

    // method to draw the background. Clears the background and paints it to
    // black, then it draws the cyan dashed lines in the middle, and the border
    // of the field.
    public static void drawBackground (final int score1, final int score2) {
        StdDraw.clear (StdDraw.BLACK);
        StdDraw.setPenColor (StdDraw.CYAN);

        StdDraw.text (-SCORE_X, SCORE_Y, "" + score1);
        StdDraw.text (SCORE_X, SCORE_Y, "" + score2);
        StdDraw.setPenRadius ();
        StdDraw.rectangle (0, FIELD_START_POINT, POS_SIDE, FIELD_HEIGHT);
        for (double k = LOW_DASH_LINES; k <= TOP_DASH_LINES; k += MOVE_PADDLE) {
            StdDraw.line (0, k, 0, k + SPACE_OF_LINES);
        }
    }

    // draws the left paddle. moves up if [A] is pressed, and moves down if [S]
    // is pressed
    public static double drawLeftPaddle (final double leftPaddlej) {
        double changedPosition = leftPaddlej;
        if (StdDraw.isKeyPressed (KeyEvent.VK_A)) {
            if (leftPaddlej < UPPER_RANGE) {
                changedPosition += MOVE_PADDLE;
            }
        }
        if (StdDraw.isKeyPressed (KeyEvent.VK_S)) {
            if (leftPaddlej > LOWER_RANGE) {
                changedPosition -= MOVE_PADDLE;
            }
        }
        return changedPosition;

    }

    // draws the right paddle. moves up if [J] is pressed, and moves down if [K]
    // is pressed.
    public static double drawRightPaddle (final double rightPaddlej) {
        double changedPosition = rightPaddlej;
        if (StdDraw.isKeyPressed (KeyEvent.VK_J)) {
            if (rightPaddlej < UPPER_RANGE) {
                changedPosition += MOVE_PADDLE;
            }
        }
        if (StdDraw.isKeyPressed (KeyEvent.VK_K)) {
            if (rightPaddlej > LOWER_RANGE) {
                changedPosition -= MOVE_PADDLE;
            }
        }
        return changedPosition;

    }

}
