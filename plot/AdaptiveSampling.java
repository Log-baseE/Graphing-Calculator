package plot;

import mechanisms.Calculator;
import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nicky on 4/4/2017.
 */
@SuppressWarnings("FieldCanBeLocal")
public class AdaptiveSampling {
    private Function f;
    private double epsilon;

    private final double MAX_SEGMENT = 17;
    private final double MAX_STACK = 50;

    private ArrayList<Coordinate> samples = new ArrayList<>();

    private boolean flat(double x1, double y1, double x2, double y2, double x3, double y3) {
        double dy1 = y2 - y1;
        double dy2 = y3 - y2;
        double dx1 = x2 - x1;
        double dx2 = x3 - x2;
        double angle = Math.abs(Math.atan(dy1 / dx1) - Math.atan(dy2 / dx2));
        return angle <= .001;
    }

    private ArrayList<Coordinate> sample(double x1, double x2, int stackIndex) {
        ArrayList<Coordinate> temp = new ArrayList<>();
        Random random = new Random();
        double mid = 0, y_mid;
        double y1 = f.calculate(x1);
        double y2 = f.calculate(x2);
        boolean angled = false;
        for (int i = 1; i <= 3; ++i) {
            mid = x1 + random.nextDouble() * (x2 - x1);
            y_mid = f.calculate(mid);
            if (!flat(x1, y1, mid, y_mid, x2, y2)) {
                angled = true;
                break;
            }
        }
        if (!angled) {
            temp.add(new Coordinate(x1, y1));
            temp.add(new Coordinate(x2, y2));
        } else if (Calculator.length(x1, y1, x2, y2) > epsilon && stackIndex <= MAX_STACK) {
            ArrayList<Coordinate> left = sample(x1, mid, ++stackIndex);
            ArrayList<Coordinate> right = sample(mid, x2, ++stackIndex);
            if (!left.isEmpty()) left.remove(left.size() - 1);
            temp.addAll(left);
            temp.addAll(right);
        }
        return temp;
    }

    public AdaptiveSampling(String function, double a, double b) {
        f = new Function("f(x) = " + function);
        epsilon = (b - a) / 10000;
        for (int i = 1; i < MAX_SEGMENT; ++i) {
            ArrayList<Coordinate> temp = sample(
                    a + (i - 1) * (b - a) / MAX_SEGMENT,
                    a + i * (b - a) / MAX_SEGMENT, 0);
            samples.addAll(temp);
            if(!samples.isEmpty()) samples.remove(samples.size() - 1);
        }
        samples.addAll(sample(a + (MAX_SEGMENT - 1) * (b - a) / MAX_SEGMENT, b, 0));
    }

    @SuppressWarnings("WeakerAccess")
    public ArrayList<Coordinate> getSamples() {
        return samples;
    }

}
