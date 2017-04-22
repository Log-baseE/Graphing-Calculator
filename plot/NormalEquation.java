package plot;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.StrokeLineJoin;
import org.mariuszgromada.math.mxparser.Expression;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nicky on 3/19/2017.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class NormalEquation implements Serializable{
    public enum Type {
        NORMAL,
        INTEGRAL,
        DERIVATIVE
    }

    protected Type type;
    String function;
    Color lineColor;

    Curve curve;
    ArrayList<Point> listOfPoints = new ArrayList<>();
    protected int id;

    public NormalEquation(Type type, String function, Color color) {
        this.type = type;
        this.function = function;
        lineColor = color;
    }

    public NormalEquation(String function, Color color) {
        this(Type.NORMAL, function, color);
    }

    public NormalEquation(String function) {
        this(function, Color.BLACK);
    }

    /**
     * setter methods
     */
    public void setLineColor(Color color) {
        this.lineColor = color;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setID(int id) {
        this.id = id;
    }

    /**
     * getter methods
     */
    public String getFunction() {
        return function;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public Type getType() {
        return type;
    }

    public int getID() {
        return id;
    }

    public Curve getCurve() {
        return curve;
    }

    public ArrayList<Curve> getCurves() {
        ArrayList<Curve> temp = new ArrayList<>();
        temp.add(curve);
        return temp;
    }
    public ArrayList<Point> getPoints(){
        return listOfPoints;
    }


    @Override
    public String toString() {
        return type.toString() + " " + id + " " + function + " " + lineColor.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NormalEquation)) return false;
        else {
            NormalEquation otherNormalEquation = (NormalEquation) obj;
            return (otherNormalEquation.toString().equals(toString()));
        }
    }

    public NormalEquation copy(){
        return new NormalEquation(function, lineColor);
    }

    public boolean equalsWithoutID(Object obj) {
        if (!(obj instanceof NormalEquation)) return false;
        else {
            NormalEquation otherNormalEquation = (NormalEquation) obj;
            return (otherNormalEquation.getType() == getType()
                    && otherNormalEquation.getFunction().equals(getFunction())
                    && otherNormalEquation.getLineColor().equals(getLineColor())
            );
        }
    }

    public void draw(double from, double to,
                     double originX, double originY,
                     double spacingX, double spacingY,
                     double unitX, double unitY) {
        AdaptiveSampling samples = new AdaptiveSampling(function, from, to);
        boolean first = false;
        curve = new Curve(this);
        curve.setStroke(lineColor);
        curve.setStrokeLineJoin(StrokeLineJoin.ROUND);
        for (Coordinate coor : samples.getSamples()) {
            if (!first) {
                MoveTo moveTo = new MoveTo(coor.getX() * spacingX / unitX + originX,
                        originY - coor.getY() * spacingY / unitY);
                curve.getElements().add(moveTo);
                first = true;
            } else {
                LineTo lineTo = new LineTo(coor.getX() * spacingX / unitX + originX,
                        originY - coor.getY() * spacingY / unitY);
                curve.getElements().add(lineTo);
            }
        }
    }

}
