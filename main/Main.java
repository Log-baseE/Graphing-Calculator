package main;

import controllers.BaseController;
import controllers.EquationPaneController;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import plot.Plot;

public class Main extends Application{
    public Stage mainWindow;
    private static Graph graph;
    private static BaseController baseController;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        baseController = new BaseController();
        Scene mainScene = new Scene(baseController);
        mainWindow = primaryStage;
        mainWindow.setMinHeight(600);
        mainWindow.setMinWidth(1200);
        mainWindow.setScene(mainScene);
        mainWindow.getIcons().add(new Image("resources/icon.png"));
        mainWindow.setTitle("Untitled Graph");
        mainWindow.setMaximized(true);
        mainWindow.show();
        graph = new Graph();
        baseController.addEquation();
        baseController.addPlot();
    }

    public static BaseController getBaseController(){return baseController;}
    public static Graph getGraph(){return graph;}
    public static void refresh(){
        for(Node node : baseController.equationPaneContainer.getChildren()){
            EquationPaneController epc = (EquationPaneController) node;
            epc.refreshIndex();
        }
    }
}