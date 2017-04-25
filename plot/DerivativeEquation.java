package plot;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import mechanisms.Calculator;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;

/**
 * Created by Nicky on 4/5/2017.
 */
@SuppressWarnings("WeakerAccess")
public class DerivativeEquation extends NormalEquation {
    /**
     * point
     */
    private String point;
    private double pointX;
    private double pointY;

    /**
     * tangent line
     */
    private Curve tangent;
    private String tangentLine;

    public DerivativeEquation(String equation, double pointX, Color color) {
        this(equation, String.valueOf(pointX), color);
    }

    public DerivativeEquation(String equation, double pointX) {
        this(equation, pointX, new Color(0, 0, 0, 1));
    }

    public DerivativeEquation(String equation, String pointX, Color color) {
        super(Type.DERIVATIVE, equation, color);
        point = pointX;
        this.pointX = new Expression(pointX).calculate();
        Function f = new Function("f(x) = " + function);
        double gradient = Calculator.derivative(function, pointX);
        this.pointY = f.calculate(this.pointX);
        tangentLine = gradient + "*(x - " + pointX + ") + " + pointY;
    }

    public DerivativeEquation(String equation, String pointX) {
        this(equation, pointX, new Color(0, 0, 0, 1));
    }

    public DerivativeEquation(String equation, Color color) {
        this(equation, 0, color);
    }

    public DerivativeEquation(String equation) {
        this(equation, new Color(0, 0, 0, 1));
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(double pointX) {
        setPoint(String.valueOf(pointX));
    }

    public void setPoint(String point) {
        this.point = point;
        this.pointX = new Expression(point).calculate();
    }

    private void drawTangent(double from, double to,
                             double originX, double originY, double spacingX, double spacingY, double unitX, double unitY) {
        AdaptiveSampling samples = new AdaptiveSampling(tangentLine, from, to);
        boolean first = false;
        Color tangentColor = new Color(
                lineColor.getRed(),
                lineColor.getGreen(),
                lineColor.getBlue(),
                0.5
        );
        tangent = new Curve(new NormalEquation(tangentLine, tangentColor));
        Point newPoint = new Point(
                this,
                pointX * spacingX / unitX + originX,
                originY - pointY * spacingY / unitY,
                pointX, pointY,
                lineColor);
        if(listOfPoints.isEmpty()) {
            listOfPoints.add(newPoint);
        } else {
            listOfPoints.set(0, newPoint);
        }

        for (Coordinate coor : samples.getSamples()) {
            if (!first) {
                MoveTo moveTo = new MoveTo(
                        coor.getX() * spacingX / unitX + originX,
                        originY - coor.getY() * spacingY / unitY);
                tangent.getElements().add(moveTo);
                first = true;
            } else {
                LineTo lineTo = new LineTo(
                        coor.getX() * spacingX / unitX + originX,
                        originY - coor.getY() * spacingY / unitY);
                tangent.getElements().add(lineTo);
            }
        }
    }

    @Override
    public String toString() {
        return type.toString() + " " +
                id + " " +
                function + " " +
                point + " " +
                lineColor.toString();
    }

    @Override
    public boolean equalsWithoutID(Object obj) {
        if (!(obj instanceof DerivativeEquation)) return false;
        else {
            DerivativeEquation otherDerivativeEquation = (DerivativeEquation) obj;
            return (otherDerivativeEquation.getType() == getType()
                    && otherDerivativeEquation.getFunction().equals(getFunction())
                    && otherDerivativeEquation.getLineColor().equals(getLineColor())
                    && otherDerivativeEquation.getPoint().equals(getPoint())
            );
        }
    }

    @Override
    public void draw(double from, double to,
                     double originX, double originY,
                     double spacingX, double spacingY,
                     double unitX, double unitY) {
        super.draw(from, to, originX, originY, spacingX, spacingY, unitX, unitY);
        drawTangent(from, to, originX, originY, spacingX, spacingY, unitX, unitY);
    }

    @Override
    public ArrayList<Curve> getCurves() {
        ArrayList<Curve> temp = new ArrayList<>();
        temp.add(curve);
        temp.add(tangent);
        return temp;
    }

    @Override
    public DerivativeEquation copy() {
        return new DerivativeEquation(function, point, lineColor);
    }
}
