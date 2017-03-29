package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import main.Equation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nicky on 3/15/2017.
 */
public class BaseController extends AnchorPane implements Initializable{

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
    }
    @FXML
    public void integrate(){
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(Equation.Type.INTEGRAL);
        equationPaneContainer.getChildren().add(ep);
    }
    @FXML
    public void derivative(){
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(Equation.Type.DERIVATIVE);
        equationPaneContainer.getChildren().add(ep);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.35));
        rightPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.65));
        leftPane.prefWidthProperty().bind(splitPane.widthProperty().multiply(0.35));
        rightPane.prefWidthProperty().bind(splitPane.widthProperty().multiply(0.65));
    }

}
