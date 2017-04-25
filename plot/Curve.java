package plot;

import javafx.scene.shape.Path;

import java.io.Serializable;

/**
 * Created by Nicky on 4/5/2017.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Curve extends Path implements Serializable{
    private NormalEquation equation;
    private int id;

    public Curve(NormalEquation equation){
        this.equation = equation;
        setStroke(equation.getLineColor());
        setStrokeWidth(1.5);
    }
    public void setID(int id){
        this.id = id;
    }
    public int getID(){
        return id;
    }
    public NormalEquation getEquation(){
        return equation;
    }
    public void setEquation(NormalEquation equation){
        this.equation = equation;
    }
    @Override
    public boolean equals(Object obj) {
//        System.out.println("class: "+obj.getClass());
        if(!(obj instanceof Curve)) return false;
        else{
            Curve otherCurve = (Curve)obj;
            return (
                    otherCurve.getID() == id
                    && otherCurve.getEquation().equalsWithoutID(equation)
            );
        }
    }

    @Override
    public String toString() {
        return "id: " + id + " [" + equation.toString() + "]";
    }
}
