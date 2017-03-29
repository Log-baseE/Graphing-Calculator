package main;

import controllers.BaseController;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;

/**
 * Created by Nicky on 3/21/2017.
 */

/**
 * Creates both axis in a javaFX pane
 * Contructor takes two pairs of {@code double}
 * representing the lower and upper bound of the axis.
 */
public class Axes extends Pane {
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private BaseController parentController;
    private double tick = 1;


    public Axes(double xLow, double xHi, double yLow, double yHi){
        parentController = new BaseController();
        xAxis = new NumberAxis(xLow, xHi, tick);
        yAxis = new NumberAxis(yLow, yHi, tick);
    }
    public void setTick(double tick){
        this.tick = tick;
    }
}
