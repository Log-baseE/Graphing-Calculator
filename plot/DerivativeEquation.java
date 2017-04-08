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
public class DerivativeEquation extends NormalEquation {
    /**point*/
    private double point;

    /**tangent line*/
    private Curve tangent;
    private String tangentLine;

    public DerivativeEquation(String equation, double point, Color color) {
        super(Type.DERIVATIVE, equation, color);
        this.point = point;
        Function f = new Function("f(x) = " + function);
        double gradient = Calculator.derivative(function, point);
        double coorX = point;
        double coorY = f.calculate(coorX);
        tangentLine = gradient + "*(x - " + coorX + ") + " + coorY;
//        System.out.println("tangent: "+tangentLine);
    }
    public DerivativeEquation(String equation, double point){
        this(equation, point, new Color(0,0,0,1));
    }
    public DerivativeEquation(String equation, String point, Color color){
        this(equation, new Expression(point).calculate(), color);
    }
    public DerivativeEquation(String equation, String point){
        this(equation, point, new Color(0,0,0,1));
    }
    public DerivativeEquation(String equation, Color color) {
        this(equation, 0, color);
    }
    public DerivativeEquation(String equation) {
        this(equation, new Color(0,0,0,1));
    }

    public double getPoint(){
        return point;
    }
    public void setPoint(double point) {
        this.point = point;
    }
    public void setPoint(String point){
        setPoint(new Expression(point).calculate());
    }

    private void drawTangent(double from, double to,
                             double originX, double originY, double spacingX, double spacingY, double unitX, double unitY){
        AdaptiveSampling samples = new AdaptiveSampling(tangentLine, from, to);
        boolean first = false;
        Color tangentColor = new Color(
                lineColor.getRed(),
                lineColor.getGreen(),
                lineColor.getBlue(),
                0.5
        );
        tangent = new Curve(new NormalEquation(tangentLine, tangentColor));
        for (Coordinate coor : samples.getSamples()) {
            if (!first) {
                MoveTo moveTo = new MoveTo(
                        coor.getX() * spacingX  / unitX + originX,
                        originY - coor.getY() * spacingY / unitY);
                tangent.getElements().add(moveTo);
                first = true;
            } else {
                LineTo lineTo = new LineTo(
                        coor.getX() * spacingX / unitX + originX ,
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
        if(!(obj instanceof DerivativeEquation)) return false;
        else{
            DerivativeEquation otherDerivativeEquation = (DerivativeEquation) obj;
            return (otherDerivativeEquation.getType() == getType()
                    && otherDerivativeEquation.getFunction().equals(getFunction())
                    && otherDerivativeEquation.getLineColor().equals(getLineColor())
                    && otherDerivativeEquation.getPoint() == getPoint()
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
    public ArrayList<Curve> getCurves(){
        ArrayList<Curve> temp = new ArrayList<>();
        temp.add(curve);
        temp.add(tangent);
        return temp;
    }
}
