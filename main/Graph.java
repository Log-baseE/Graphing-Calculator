package main;

import javafx.scene.paint.Color;
import plot.NormalEquation;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nicky on 3/19/2017.
 */
public class Graph implements Serializable{
    private ArrayList<NormalEquation> normalEquationArrayList;
    public Graph(){
        normalEquationArrayList = new ArrayList<NormalEquation>();
    }
    public void addEquation(NormalEquation normalEquation){
        int count = 0;
        for(NormalEquation equation: normalEquationArrayList){
            if(normalEquation.equalsWithoutID(equation)) ++count;
        }
        normalEquation.setID(count);
        normalEquationArrayList.add(normalEquation);
    }

    public void deleteAt(int index){
        if(normalEquationArrayList.size()>0) normalEquationArrayList.remove(index);
    }
    public void setEquationAt(int index, NormalEquation equation){
        int count = 0;
        for(int i = 0; i<normalEquationArrayList.size(); ++i){
            if(normalEquationArrayList.get(i).equalsWithoutID(equation) && i!=index) ++count;
        }
        equation.setID(count);
        normalEquationArrayList.set(index, equation);
    }
    public void setColorAt(int index, Color color){
        NormalEquation equation = normalEquationArrayList.get(index);
        equation.setLineColor(color);
        int count = 0;
        for(int i = 0; i<normalEquationArrayList.size(); ++i){
//            System.out.println(normalEquationArrayList.get(i));
//            System.out.println(equation);
            if(normalEquationArrayList.get(i).equalsWithoutID(equation)
                    && i!=index) ++count;
//            System.out.println("i: " + i + " index: " + index + " count: "+count);
        }
        equation.setID(count);
        normalEquationArrayList.set(index, equation);
    }

    public ArrayList<NormalEquation> getEquationArrayList(){
        return normalEquationArrayList;
    }
}
