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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.omg.SendingContext.RunTime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nicky on 3/15/2017.
 */
public class BaseController extends AnchorPane implements Initializable{

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
        EquationPane ep = new EquationPane();
        equationPaneContainer.getChildren().add(ep);
    }
    @FXML
    public void integrate(){
        equationPaneContainer.setPrefWidth(leftPane.getWidth());
        EquationPane ep = new EquationPane(Equation.Type.INTEGRAL);
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
