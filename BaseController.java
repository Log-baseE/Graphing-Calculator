import com.sun.org.omg.CORBA.InitializerSeqHelper;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.omg.SendingContext.RunTime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nicky on 3/15/2017.
 */
public class BaseController extends AnchorPane implements Initializable{
    private Main mainProgram;
    public BaseController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/BaseLayout.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**menu bar*/
    @FXML
    public MenuItem newGraphMenu;

    /**lower menu*/
    @FXML
    public Button newGraphButton;

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
        EquationPane ep = new EquationPane(leftPane);
        equationPaneContainer.getChildren().add(ep);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.3));
        rightPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.7));
        leftPane.prefWidthProperty().bind(splitPane.widthProperty().multiply(0.3));
        rightPane.prefWidthProperty().bind(splitPane.widthProperty().multiply(0.7));
    }

    public AnchorPane getLeftPane(){
        return leftPane;
    }
    public AnchorPane getRightPane(){
        return rightPane;
    }

}
