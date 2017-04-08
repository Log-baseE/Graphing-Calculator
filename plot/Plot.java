package plot;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import main.Main;

import java.util.ArrayList;

/**
 * Created by Nicky on 3/15/2017.
 */
public class Plot extends AnchorPane {
    /**Stores a list of all curves in the plot*/
    private ArrayList<Curve> listOfCurves = new ArrayList<>();

    /**Contains coordinates of the origin*/
    private double originX;
    private double originY;

    /**Contains spacing of x and y in pixels*/
    private double spacingX;
    private double spacingY;

    /**Contains units of each interval*/
    private double unitX;
    private double unitY;

    /**Contains bounds of x and y axes*/
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public Plot(double width, double height){
        setWidth(width);
        setHeight(height);
//        System.out.println(width+" "+height);
        originX = width/2.0;
        originY = height/2.0;
        spacingX = spacingY = 50;
        unitX = unitY = 2;
        minX = -originX/spacingX*unitX;
        maxX = (getWidth()-originX)/spacingX*unitX;
        minY = -(getHeight()-originY)/spacingY*unitY;
        maxY = originY/spacingY*unitY;
        initialize();
    }

    private void drawAxis(){
        Path x_axis = new Path();
        MoveTo moveToXAxis = new MoveTo(0, originY);
        LineTo lineToXAxis = new LineTo(getWidth(), originY);
        x_axis.setStrokeWidth(1);
        x_axis.getElements().addAll(moveToXAxis, lineToXAxis);

        Path y_axis = new Path();
        MoveTo moveToYAxis = new MoveTo(originX, 0);
        LineTo lineToYAxis = new LineTo(originX, getHeight());
        y_axis.getElements().addAll(moveToYAxis, lineToYAxis);
        y_axis.setStrokeWidth(1);

        getChildren().addAll(x_axis, y_axis);
//        System.out.println(getChildren());
    }
    private void drawGrid(){
        for(int i = 1; i*spacingX+originX<getWidth(); ++i){
            Path y_line = new Path();
            MoveTo moveToYLine = new MoveTo(originX+i*spacingX, 0);
            LineTo lineToYLine = new LineTo(originX+i*spacingX, getHeight());
            y_line.setStroke(new Color(0.7,0.7,0.7,1));
            y_line.getElements().addAll(moveToYLine, lineToYLine);
            y_line.setStrokeWidth(0.5);
            getChildren().add(y_line);
        }
        for(int i = 1; originX-i*spacingX>0; ++i){
            Path y_line = new Path();
            MoveTo moveToYLine = new MoveTo(originX-i*spacingX, 0);
            LineTo lineToYLine = new LineTo(originX-i*spacingX, getHeight());
            y_line.setStroke(new Color(0.7,0.7,0.7,1));
            y_line.getElements().addAll(moveToYLine, lineToYLine);
            y_line.setStrokeWidth(0.5);
            getChildren().add(y_line);
        }
        for(int i = 1; i*spacingY+originY<getHeight(); ++i){
            Path x_line = new Path();
            MoveTo moveToXLine = new MoveTo(0,originY+i*spacingY);
            LineTo lineToXLine = new LineTo(getWidth(), originY+i*spacingY);
            x_line.setStroke(new Color(0.7,0.7,0.7,1));
            x_line.getElements().addAll(moveToXLine, lineToXLine);
            x_line.setStrokeWidth(0.5);
            getChildren().add(x_line);
        }
        for(int i = 1; originY-i*spacingY>0; ++i){
            Path x_line = new Path();
            MoveTo moveToXLine = new MoveTo(0, originY-i*spacingY);
            LineTo lineToXLine = new LineTo(getWidth(), originY-i*spacingY);
            x_line.setStroke(new Color(0.7,0.7,0.7,1));
            x_line.getElements().addAll(moveToXLine, lineToXLine);
            x_line.setStrokeWidth(0.5);
            getChildren().add(x_line);
        }
    }

    public void removeGroup(NormalEquation function){
        ArrayList<NormalEquation> tempList = Main.getGraph().getEquationArrayList();
        for(NormalEquation e: tempList){
            System.out.println("searhing: " + function);
            System.out.println("found: " + e);
            if(function.equals(e)) {
                System.out.println("curves: " + e.getCurves());
                for(Curve c2: e.getCurves()){
                    System.out.println("removed: " + c2);
                    getChildren().remove(c2);
                    listOfCurves.remove(c2);
                }
                break;
            }
        }
        System.out.println();
    }
    public void drawCurve(NormalEquation oldFunction, NormalEquation newFunction){
        removeGroup(oldFunction);
        newFunction.draw(minX, maxX, originX, originY, spacingX, spacingY, unitX, unitY);
        for(Curve curveOf: newFunction.getCurves()){
            int count = 0;
            for(Curve c: listOfCurves){
                if(curveOf.getEquation().equalsWithoutID(c.getEquation())) ++count;
            }
            curveOf.setID(count);
            getChildren().add(curveOf);
            listOfCurves.add(curveOf);
        }
    }

    private void initialize(){
        drawGrid();
        drawAxis();
    }
}
