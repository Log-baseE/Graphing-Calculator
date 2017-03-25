import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;

import java.io.*;

/**
 * Created by Nicky on 3/15/2017.
 */
public class Plot extends Canvas{
    private Graph graph;
    private Axes axes;
    public Plot(){
        graph = new Graph();
    }
    public Plot(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            graph = (Graph)ois.readObject();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public Graph getGraph(){return graph;}
}
