package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import main.Graph;
import main.Main;
import plot.NormalEquation;
import plot.Plot;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Nicky on 3/15/2017. <br><br>
 * This class is a controller for the BaseLayout of the whole program <br><br>
 * BaseController extends a JavaFX AnchorPane - the whole scene
 * is an AnchorPane <br>
 */
public class BaseController extends AnchorPane implements Initializable {

    /**
     * Menu items
     */
    @FXML
    MenuItem undoMenu;
    @FXML
    MenuItem redoMenu;

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
            Main.getMainWindow().close();
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

    public void addManualPane(NormalEquation equation) {
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(equation, true);
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
    }

    public void addManualPane(int index, NormalEquation equation) {
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(equation, false);
        equationPaneContainer.getChildren().add(index, ep);
        ep.getEquationTextField().requestFocus();
    }

    @FXML
    public void integrate() {
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(NormalEquation.Type.INTEGRAL, false);
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
    }

    @FXML
    public void derivative() {
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPaneController ep = new EquationPaneController(NormalEquation.Type.DERIVATIVE, false);
        equationPaneContainer.getChildren().add(ep);
        ep.getEquationTextField().requestFocus();
    }

    @FXML
    public void saveAsGraph() {
//        System.out.println("method invoked");
        Main.save(Main.saveAs());
    }

    @FXML
    public void saveGraph() {
        if (Main.getFile() == null) saveAsGraph();
        else Main.save();
    }

    @FXML
    public void openGraph() {
        if (!UndoStack.empty()) {
            String fileName;
            if (Main.getFile() != null) fileName = Main.getFile().getName();
            else fileName = "Untitled";
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to save " + fileName + " before opening another file?",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Save file?");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                saveGraph();
            } else if (result.get() == ButtonType.CANCEL) {
                alert.close();
                return;
            }
        }
        FileChooser fileChooser = new FileChooser();
        String extension = Graph.EXTENSION;
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Graph file", extension)
        );
        if (Main.getFile() != null)
            fileChooser.setInitialDirectory(Main.getFile().getParentFile());
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(Main.getMainWindow());
        Main.load(file);
    }

    @FXML
    public void export() {
        WritableImage image = splitPane.snapshot(new SnapshotParameters(), null);

        PixelReader reader = image.getPixelReader();

        WritableImage newImage = new WritableImage(
                reader,
                (int) (leftPane.getWidth()), 0,
                (int) (rightPane.getWidth()), (int) (leftPane.getHeight())
        );

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Save Snapshot");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Portable Network Graphic file (*.png)", "*.png"),
                new FileChooser.ExtensionFilter("JPEG file interchange format (*.jpg)", "*.jpg")
        );
        File savedFile = fileChooser.showSaveDialog(null);


        try {
            ImageIO.write(SwingFXUtils.fromFXImage(newImage, null), "png", savedFile);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error exporting image!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void undo() {
        Action action = UndoStack.pop();
        if (action == null) return;
        RedoStack.push(action);
        System.out.println();
        switch (action.getType()) {
            case ADD:
                ((EquationPaneController) equationPaneContainer.getChildren().get(action.getIndex())).deleteManual();
                break;
            case CHANGE_TYPE:
            case CHANGE_TEXT:
            case CHANGE_POINT:
            case CHANGE_LOWER:
            case CHANGE_UPPER:
                ((EquationPaneController) equationPaneContainer.getChildren().get(action.getIndex()))
                        .editEquationManual((NormalEquation) action.getOldValue());
                break;
            case CHANGE_COLOR:
                ((EquationPaneController) equationPaneContainer.getChildren().get(action.getIndex()))
                        .setColorManual((Color) action.getOldValue());
                break;
            case DELETE:
                addManualPane(action.getIndex(), (NormalEquation) action.getOldValue());
                break;
        }
    }

    @FXML
    public void redo() {
        Action action = RedoStack.pop();
        if (action == null) return;
        UndoStack.push(action);
        System.out.println();
        switch (action.getType()) {
            case ADD:
                addManualPane(action.getIndex(), (NormalEquation) action.getNewValue());
                break;
            case CHANGE_TYPE:
            case CHANGE_TEXT:
            case CHANGE_POINT:
            case CHANGE_LOWER:
            case CHANGE_UPPER:
                ((EquationPaneController) equationPaneContainer.getChildren().get(action.getIndex()))
                        .editEquationManual((NormalEquation) action.getNewValue());
                break;
            case CHANGE_COLOR:
                ((EquationPaneController) equationPaneContainer.getChildren().get(action.getIndex()))
                        .setColorManual((Color) action.getNewValue());
                break;
            case DELETE:
                ((EquationPaneController) equationPaneContainer.getChildren().get(action.getIndex())).deleteManual();
                break;
        }
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

    public void setBaseTitle(String s) {
        baseTitle.setText(s);
    }

}
