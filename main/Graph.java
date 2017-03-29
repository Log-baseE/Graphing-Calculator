package main;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nicky on 3/19/2017.
 */
public class Graph implements Serializable{
    private ArrayList<Equation> equationArrayList;
    public Graph(){
        equationArrayList = new ArrayList<Equation>();
    }
    public void addEquation(Equation equation){
        equationArrayList.add(equation);
    }
    public void addEquation(Equation.Type type, String equation, Color color){
        equationArrayList.add(new Equation(type, equation, color));
    }
    public void addEquation(String equation, Color color){
        addEquation(Equation.Type.NORMAL, equation, color);
    }
    public void addEquation(String equation){
        addEquation(equation, Color.BLACK);
    }

    public void deleteAt(int index){
        if(equationArrayList.size()>0) equationArrayList.remove(index);
    }
    public void setEquationAt(int index, String equation){
        equationArrayList.get(index).setEquation(equation);
    }
    public void setColorAt(int index, Color color){
        equationArrayList.get(index).setLineColor(color);
    }
    public void setTypeAt(int index, Equation.Type type){
        equationArrayList.get(index).setType(type);
    }

    public ArrayList<Equation> getEquationArrayList(){
        return equationArrayList;
    }
}
