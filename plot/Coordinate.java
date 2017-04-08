package plot;

/**
 * Created by Nicky on 4/4/2017.
 */
public class Coordinate{
    private double x, y;

    /**
     * Constructor of a coordinate object
     * @param x x value of the object
     * @param y y value of the object
     */
    public Coordinate(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }
}
