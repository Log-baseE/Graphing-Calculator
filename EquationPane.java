import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.mariuszgromada.math.mxparser.Expression;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


public class EquationPane extends AnchorPane implements Initializable{
    public EquationPane(AnchorPane parent){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/EquationPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setWidth(parent.getWidth());
        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }

    /** Components*/
    private GridPane gridPane;
    private ArrayList<ColumnConstraints> columns;
    private ArrayList<RowConstraints> rows;

    @FXML
    private TextField equationTextField;
    @FXML
    private ColorPicker lineColorPicker;
    @FXML
    private Button deleteEquationButton;
    @FXML
    private Label latexLabel;
    @FXML
    private ImageView latexRepresent;
    @FXML
    private Label errorLabel;
    @FXML
    private WebView resultView;
    @FXML
    private WebEngine resultEngine;

    /** Parent components*/
    @FXML private VBox parent = Main.getBaseController().equationPaneContainer;
    @FXML private Main mainProgram = new Main();

    private int index;
    @FXML private void initialize(){
        index = parent.getChildren().indexOf(this);
    }
    @FXML public void deleteEquation(){
        parent.getChildren().remove(index);
        Main.getPlot().getGraph().deleteAt(index);
    }
    @FXML public void editEquation(KeyEvent event){
        if(event.getCode()==KeyCode.ENTER){
            resultEngine = resultView.getEngine();
            String textInput = equationTextField.getText();
            textInput = textInput.replace(" ","");
            boolean equation = false, calculate = false;
            for(int i = 0; i<textInput.length(); ++i) {
                if (textInput.charAt(i) == 'x') equation = true;
                if (!Character.isLetterOrDigit(textInput.charAt(i))&&textInput.charAt(i)!='.') calculate=true;
            }
            if(!equation&&calculate) {
                Expression expr = new Expression(textInput);
                resultEngine.loadContent(PrettyDouble.beautifyGenerateHTML(String.valueOf(expr.calculate())));
                resultView.setVisible(true);
            } else {
                resultView.setVisible(false);
            }
        }

        Main.getPlot().getGraph().setEquationAt(index, equationTextField.getText());
        System.out.println(Main.getPlot().getGraph().getEquationArrayList());
    }
    @FXML public void setColor(){
        Main.getPlot().getGraph().setColorAt(index, lineColorPicker.getValue());
        System.out.println(Main.getPlot().getGraph().getEquationArrayList());
    }
    public Color getColor(){
        return lineColorPicker.getValue();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridPane = (GridPane) this.getChildren().get(0);
        ColumnConstraints col0 = gridPane.getColumnConstraints().get(0);
        col0.prefWidthProperty().bind(
                gridPane.prefWidthProperty().
                        subtract(deleteEquationButton.widthProperty().add(lineColorPicker.widthProperty()))
        );
        Random rand = new Random();
        double red = rand.nextDouble();
        double green = rand.nextDouble();
        double blue = rand.nextDouble();
        lineColorPicker.setValue(new Color(red, green, blue, 1));
        errorLabel.setVisible(false);
        resultView.setVisible(false);
        latexLabel.setVisible(false);
        latexRepresent.setVisible(false);
        Main.getPlot().getGraph().addEquation("", lineColorPicker.getValue());
        System.out.println(Main.getPlot().getGraph().getEquationArrayList());
    }
}
