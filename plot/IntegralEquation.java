package plot;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;

/**
 * Created by Nicky on 4/5/2017.
 */
public class IntegralEquation extends NormalEquation {
    private String lowerBound, upperBound;
    private Curve area;

    public IntegralEquation(String equation, double lowerBound, double upperBound, Color color) {
        this(equation, String.valueOf(lowerBound), String.valueOf(upperBound), color);
    }

    public IntegralEquation(String equation, double lowerBound, double upperBound) {
        this(equation, lowerBound, upperBound, new Color(0, 0, 0, 1));
    }

    public IntegralEquation(String equation, String lowerBound, String upperBound, Color color) {
        super(Type.INTEGRAL, equation, color);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public IntegralEquation(String equation, String lowerBound, String upperBound) {
        this(equation, lowerBound, upperBound, new Color(0, 0, 0, 1));
    }

    public IntegralEquation(String equation, Color color) {
        this(equation, 0, 1, color);
    }

    public IntegralEquation(String equation) {
        this(equation, new Color(0, 0, 0, 1));
    }

    public void setLowerBound(double lowerBound) {
        setLowerBound(String.valueOf(lowerBound));
    }

    public void setLowerBound(String lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(double upperBound) {
        setUpperBound(String.valueOf(upperBound));
    }

    public void setUpperBound(String upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public String toString() {
        return type.toString() + " " +
                id + " " +
                function + " " +
                lowerBound + " " +
                upperBound + " " +
                lineColor.toString();
    }

    public String getLower() {
        return lowerBound;
    }

    public String getUpper() {
        return upperBound;
    }


    private void drawArea(double from, double to,
                          double originX, double originY,
                          double spacingX, double spacingY,
                          double unitX, double unitY) {
        AdaptiveSampling samples = new AdaptiveSampling(function, from, to);
        area = new Curve(this);
        MoveTo moveTo = new MoveTo(from * spacingX / unitX + originX, originY);
        area.getElements().add(moveTo);
        for (Coordinate coor : samples.getSamples()) {
            LineTo lineTo = new LineTo(
                    coor.getX() * spacingX / unitX + originX,
                    originY - coor.getY() / unitY * spacingY);
            area.getElements().add(lineTo);
        }
        LineTo lineTo = new LineTo(to * spacingX / unitX + originX, originY);
        area.getElements().add(lineTo);
        area.setFill(new Color(
                lineColor.getRed(),
                lineColor.getGreen(),
                lineColor.getBlue(),
                0.5
        ));
    }

    @Override
    public void draw(double from, double to,
                     double originX, double originY,
                     double spacingX, double spacingY,
                     double unitX, double unitY) {
        super.draw(from, to, originX, originY, spacingX, spacingY, unitX, unitY);
        drawArea(
                new Expression(lowerBound).calculate(), new Expression(upperBound).calculate(),
                originX, originY, spacingX, spacingY,
                unitX, unitY);
    }

    @Override
    public boolean equalsWithoutID(Object obj) {
        if (!(obj instanceof IntegralEquation)) return false;
        else {
            IntegralEquation otherIntegralEquation = (IntegralEquation) obj;
            return (otherIntegralEquation.getType() == getType()
                    && otherIntegralEquation.getFunction().equals(getFunction())
                    && otherIntegralEquation.getLineColor().equals(getLineColor())
                    && otherIntegralEquation.getLower().equals(getLower())
                    && otherIntegralEquation.getUpper().equals(getUpper())
            );
        }
    }

    @Override
    public ArrayList<Curve> getCurves() {
        ArrayList<Curve> temp = new ArrayList<>();
        temp.add(curve);
        temp.add(area);
        return temp;
    }

    @Override
    public IntegralEquation copy() {
        return new IntegralEquation(function, lowerBound, upperBound, lineColor);
    }
}
