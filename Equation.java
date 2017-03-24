import javafx.scene.paint.Color;

/**
 * Created by Nicky on 3/19/2017.
 */
public class Equation {
    String equation;
    String latexRepresentation;
    Color lineColor;
    public Equation(String equation, Color color){
        this.equation = equation;
        lineColor = color;
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
    public String getEquation(){
        return equation;
    }
    public Color getLineColor(){
        return lineColor;
    }
    @Override
    public String toString(){
        return equation+" "+lineColor.toString();
    }

}
