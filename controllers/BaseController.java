package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import plot.NormalEquation;
import plot.Plot;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nicky on 3/15/2017.
 */
public class BaseController extends AnchorPane implements Initializable{

    /**lower menu*/
    @FXML
    public Button newGraphButton;
    @FXML
    public Button integralButton;

    /**containers*/
    @FXML
    public BorderPane borderContainer;
    @FXML
    public VBox equationPaneContainer;
    @FXML
    public AnchorPane leftPane;
    @FXML
    public AnchorPane rightPane;
    @FXML
    public SplitPane splitPane;

    private Plot plot;

    public BaseController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/BaseLayout.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void createNewGraph(){
//        System.out.println("create");
//        System.out.println(equationPaneContainer);
    }

    @FXML
    public void addEquation(){
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController();
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
    }
    @FXML
    public void integrate(){
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(NormalEquation.Type.INTEGRAL);
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
    }
    @FXML
    public void derivative(){
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(NormalEquation.Type.DERIVATIVE);
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.35));
        rightPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.65));
        leftPane.prefWidthProperty().bind(splitPane.widthProperty().multiply(0.35));
        rightPane.prefWidthProperty().bind(splitPane.widthProperty().multiply(0.65));
    }

    public void addPlot(){
        plot = new Plot(rightPane.getWidth(), rightPane.getHeight());
        rightPane.getChildren().add(plot);
    }

    public Plot getPlot(){
        return plot;
    }

}
