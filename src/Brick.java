import acm.graphics.GRect;
import java.awt.Color;

public class Brick extends GRect {

    public static final int WIDTH = 44;
    public static final int HEIGHT = 20;

    public int hP;

    public Brick(double x, double y, Color color, int row){
        super(x,y,WIDTH, HEIGHT);
        this.setFillColor(color);
        this.setFilled(true);
    }

    public void changeHP (int x){
        hP = x;
    }

}
//oaur naur my code, its brocken