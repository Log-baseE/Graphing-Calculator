package main;

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import plot.DerivativeEquation;
import plot.IntegralEquation;
import plot.NormalEquation;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Nicky on 3/19/2017.
 */
public class Graph implements Serializable{
    public static final String EXTENSION = "*.graph";
    private ArrayList<NormalEquation> normalEquationArrayList;
    public Graph(){
        normalEquationArrayList = new ArrayList<NormalEquation>();
    }
    public void addEquation(NormalEquation normalEquation){
        int maxID = -1;
        for(NormalEquation equation: normalEquationArrayList){
            if(normalEquation.equalsWithoutID(equation)) maxID = Math.max(equation.getID(), maxID);
        }
        normalEquation.setID(maxID+1);
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

    NormalEquation load(String s) throws GraphLoadException{
        String[] sa = s.split(" ");
        for(String s1: sa) System.out.println(s1);
        String type = sa[0];
        int id = Integer.valueOf(sa[1]);
        NormalEquation normalEquation;
        try{
            if(type.equals("NORMAL")){
                normalEquation = new NormalEquation(
                        sa[2],
                        (Color) Paint.valueOf(sa[3]));
                normalEquation.setID(id);
            } else if (type.equals("DERIVATIVE")){
                normalEquation = new DerivativeEquation(
                        sa[2], sa[3],
                        (Color) Paint.valueOf(sa[4]));
                normalEquation.setID(id);
            } else if (type.equals("INTEGRAL")){
                normalEquation = new IntegralEquation(
                        sa[2], sa[3], sa[4],
                        (Color) Paint.valueOf(sa[5]));
                normalEquation.setID(id);
            } else throw new GraphLoadException("Unexpected type: " + type);
        } catch (ArrayIndexOutOfBoundsException e){
            throw new GraphLoadException("Error loading line: " + s);
        }
        return normalEquation;
    }
}
