package plot;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nicky on 4/4/2017.
 */
public class AdaptiveSampling {
    private String function;
    private Function f;

    private ArrayList<Coordinate> samples = new ArrayList<>();
    private boolean flat(double x1, double y1, double x2, double y2, double x3, double y3){
        final double epsilon = 0.001;
        double dy1 = y2-y1;
        double dy2 = y3-y2;
        double dx1 = x2-x1;
        double dx2 = x3-x2;
        double angle = Math.abs(Math.atan(dy1/dx1)-Math.atan(dy2/dx2));
        return angle <= epsilon;
    }

    ArrayList<Coordinate> sample(double a, double b, int stackIndex) {
        ArrayList<Coordinate> temp = new ArrayList<>();
        Random random = new Random();
        double mid = a + random.nextDouble() * (b - a);
        double y1 = f.calculate(a);
        double y2 = f.calculate(mid);
        double y3 = f.calculate(b);
        final double epsilon = 0.001;
        if(flat(a,y1,mid,y2,b,y3)){
            temp.add(new Coordinate(a,y1));
            temp.add(new Coordinate(b,y3));
        } else if(Math.sqrt((y3-y1)*(y3-y1)+(b-a)*(b-a))>epsilon && stackIndex<=1000){
            ArrayList<Coordinate> left = sample(a, mid, ++stackIndex);
            ArrayList<Coordinate> right = sample(mid, b, ++stackIndex);
            if(!left.isEmpty()) left.remove(left.size()-1);
            temp.addAll(left);
            temp.addAll(right);
        }
        return temp;
    }

    public AdaptiveSampling(String function, double a, double b){
        f = new Function("f(x) = "+function);
        samples = sample(a,b,0);
    }

    public ArrayList<Coordinate> getSamples(){
        return samples;
    }

}
