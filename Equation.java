import javafx.scene.paint.Color;

/**
 * Created by Nicky on 3/19/2017.
 */
public class Equation {
    enum Type{
        NORMAL,
        INTEGRAL,
        DERIVATIVE
    }
    Type type;
    String equation;
    Color lineColor;
    public Equation(Type type, String equation, Color color){
        this.type = type;
        this.equation = equation;
        lineColor = color;
    }
    public Equation(String equation, Color color){
        this(Type.NORMAL, equation, color);
    }
    public Equation(String equation){
        this(equation, Color.BLACK);
    }

    public void setLineColor(Color color){
        this.lineColor = color;
    }
    public void setEquation(String equation) {
        this.equation = equation;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public String getEquation(){
        return equation;
    }
    public Color getLineColor(){
        return lineColor;
    }
    public Type getType(){
        return type;
    }
    @Override
    public String toString() {
        return type.toString() + " " + equation + " " + lineColor.toString();
    }

}
