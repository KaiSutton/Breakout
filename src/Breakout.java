import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.program.GraphicsProgram;
import svu.csc213.Dialog;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Breakout extends GraphicsProgram {

    private int lives = 3;
    private GLabel livesLabel = new GLabel("Your lives " + lives);
    private int points = 0;
    private GLabel pointsLabel = new GLabel("Your points " + points);

    private Ball ball;
    private Paddle paddle;

    private int numBricksInRow;
    private int bricksLeft;

    private Color[] rowColors = {Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, Color.YELLOW, Color.YELLOW,
            Color.GREEN, Color.GREEN, Color.CYAN, Color.CYAN};

    @Override
    public void init(){

        numBricksInRow = (int) (getWidth() / (Brick.WIDTH + 5.0));
        bricksLeft =10*numBricksInRow;


        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < numBricksInRow; col++) {

                double brickX = 10 + col * (Brick.WIDTH + 5);
                double brickY = Brick.HEIGHT + row * (Brick.HEIGHT + 5);

                Brick brick = new Brick(brickX, brickY, rowColors[row], row);
                add(brick);
                brick.changeHP(2);
            }
        }


        ball = new Ball(getWidth()/2, 350, 10, this.getGCanvas());
        add(ball);

        paddle = new Paddle(230, 430, 50 ,10);
        add(paddle);

        //adds the glabels
        add(livesLabel, (getWidth() - livesLabel.getWidth() -30), (getHeight() - livesLabel.getHeight() - 20));
        add(pointsLabel, (getWidth() - pointsLabel.getWidth() -30), (getHeight() - pointsLabel.getHeight() - 40));
    }

    @Override
    public void run(){
        addMouseListeners();
        waitForClick();
        gameLoop();
    }

    @Override
    public void mouseMoved(MouseEvent me){
        // make sure that the paddle doesn't go offscreen
        if((me.getX() < getWidth() - paddle.getWidth()/2)&&(me.getX() > paddle.getWidth() / 2)){
            paddle.setLocation(me.getX() - paddle.getWidth()/2, paddle.getY());
        }
    }

    private void gameLoop(){
        while(true){
            // move the ball
            ball.handleMove();

            // handle collisions
            handleCollisions();

            // handle losing the ball
            if(ball.lost){
                handleLoss();
            }

            pause(5);
        }
    }

    private void handleCollisions(){
        // obj can store what we hit
        GObject obj = null;

        // check to see if the ball is about to hit something

        if(obj == null){
            // check the top right corner
            obj = this.getElementAt(ball.getX()+ball.getWidth(), ball.getY());
        }

        if(obj == null){
            // check the top left corner
            obj = this.getElementAt(ball.getX(), ball.getY());
        }

        //check the bottom right corner for collision
        if (obj == null) {
            obj = this.getElementAt(ball.getX() + ball.getWidth(), ball.getY() + ball.getHeight());
        }
        //check the bottom left corner for collision
        if (obj == null) {
            obj = this.getElementAt(ball.getX(), ball.getY() + ball.getHeight());
        }

        // see if we hit something
        if(obj != null){

            // lets see what we hit!
            if(obj instanceof Paddle){

                if(ball.getX() < (paddle.getX() + (paddle.getWidth() * .2))){
                    // did I hit the left side of the paddle?
                    ball.bounceLeft();
                } else if(ball.getX() > (paddle.getX() + (paddle.getWidth() * .8))) {
                    // did I hit the right side of the paddle?
                    ball.bounceRight();
                } else {
                    // did I hit the middle of the paddle?
                    ball.bounce();
                }

            }


            if(obj instanceof Brick){
                Color drkCyan = new Color(0, 135, 147);
                Color drkGreen = new Color(43, 128, 4, 255);
                Color drkYellow = new Color(203, 182, 0);
                Color drkOrange = new Color(187, 143, 0);
                Color drkRed = new Color(110, 2, 2);
                // bounce the ball
                ball.bounce();
                //take away brick hp
                ((Brick) obj).hP -= 1;
                if(((Brick) obj).getFillColor() == Color.CYAN){
                    ((Brick) obj).setFillColor(drkCyan);
                }else if(((Brick) obj).getFillColor() == Color.GREEN){
                    ((Brick) obj).setFillColor(drkGreen);
                }else if (((Brick) obj).getFillColor() == Color.YELLOW){
                    ((Brick) obj).setFillColor(drkYellow);
                }else if(((Brick) obj).getFillColor() == Color.ORANGE){
                    ((Brick) obj).setFillColor(drkOrange);
                }else if(((Brick) obj).getFillColor() == Color.RED){
                    ((Brick) obj).setFillColor(drkRed);
                }

                // destroy the brick
                if (((Brick) obj).hP <= 0){
                    this.remove(obj);
                    bricksLeft -= 1;
                }
                //add points for breaking brick
                points += 100;
                //update points label
                pointsLabel.setLabel("Your points " + points);

                if(bricksLeft <= 0){
                    Dialog.showMessage("Good job you beat the game!");
                    System.exit(0);
                }

            }

        }

        // if by the end of the method obj is still null, we hit nothing
    }

    private void handleLoss(){
        lives -= 1;
        ball.lost = false;
        if(lives <= 0){
            gameOver();
        }
        reset();
    }

    private void gameOver() {
        Dialog.showMessage("You lost");
        System.exit(0);
    }

    private void reset(){
        ball.setLocation(getWidth()/2, 350);
        paddle.setLocation(230, 430);

        livesLabel.setLabel("Your lives " + lives);
        pointsLabel.setLabel("Your points " + points);

        waitForClick();
    }

    public static void main(String[] args) {
        new Breakout().start();
    }

}
//oaur naur my code, its brocken