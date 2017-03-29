package main;

import javafx.scene.canvas.Canvas;

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
