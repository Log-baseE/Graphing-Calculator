package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import main.Graph;
import main.Main;
import plot.NormalEquation;
import plot.Plot;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nicky on 3/15/2017. <br><br>
 * This class is a controller for the BaseLayout of the whole program <br><br>
 * BaseController extends a JavaFX AnchorPane - the whole scene
 * is an AnchorPane <br>
 */
public class BaseController extends AnchorPane implements Initializable {

    /**
     * lower menu
     */
    @FXML
    public Button newGraphButton;
    @FXML
    public Button integralButton;
    @FXML
    private Label baseTitle;

    /**
     * containers
     */
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

    /**
     * Other components
     */
    private Plot plot;


    /**
     * Constructor for BaseController class <br><br>
     * only loads BaseLayout.fxml
     */
    public BaseController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/BaseLayout.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading file!");
            alert.setContentText("Error loading BaseLayout.fxml");
            alert.showAndWait();
        }
    }

    @FXML
    public void createNewGraph() {
        Main.newGraph();
    }

    @FXML
    public void addEquation() {
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController();
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
//        System.out.println(Main.getGraph().getEquationArrayList());
    }

    public void addManualPane(NormalEquation equation){
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(equation);
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
    }

    @FXML
    public void integrate() {
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(NormalEquation.Type.INTEGRAL);
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
    }

    @FXML
    public void derivative() {
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(NormalEquation.Type.DERIVATIVE);
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
    }

    @FXML
    public void saveAsGraph(){
        System.out.println("method invoked");
        Main.save(Main.saveAs());
    }

    @FXML
    public void saveGraph(){
        if(Main.getFile()==null) saveAsGraph();
        else Main.save();
    }

    @FXML public void openGraph(){
        FileChooser fileChooser = new FileChooser();
        String extension = Graph.EXTENSION;
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Graph file", extension)
        );
        if(Main.getFile()!=null)
            fileChooser.setInitialDirectory(Main.getFile().getParentFile());
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(Main.getStage());
        Main.load(file);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.35));
        rightPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.65));
        leftPane.prefWidthProperty().bind(splitPane.widthProperty().multiply(0.35));
        rightPane.prefWidthProperty().bind(splitPane.widthProperty().multiply(0.65));
    }

    public void addPlot() {
        plot = new Plot(rightPane.getWidth(), rightPane.getHeight());
        rightPane.getChildren().add(plot);
    }

    public Plot getPlot() {
        return plot;
    }

    public void setBaseTitle(String s){
        baseTitle.setText(s);
    }

}
