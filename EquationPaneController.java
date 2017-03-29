import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.mariuszgromada.math.mxparser.Expression;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


public class EquationPaneController extends AnchorPane implements Initializable {
    @FXML
    private TextField equationTextField;
    @FXML
    private ChoiceBox<Equation.Type> choiceBox;
    @FXML
    private ColorPicker lineColorPicker;
    @FXML
    private Button deleteEquationButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Label latexLabel;
    @FXML
    private ImageView latexRepresent;
    @FXML
    private Label resultLabel;
    @FXML
    private WebView resultView;
    @FXML
    private WebEngine resultEngine;

    /**
     * Integral/derivative components
     */
    private IntegralBoundsController integralBounds;
    private DerivativeController derivativePoint;

    /**
     * Parent components
     */
    @FXML
    private VBox parent = Main.getBaseController().equationPaneContainer;
    @FXML
    private Main mainProgram = new Main();

    /**
     * Components
     */
    private GridPane gridPane;
    private ArrayList<ColumnConstraints> columns;
    private ArrayList<RowConstraints> rows;
    private ObservableList<Equation.Type> typeChoices =
            FXCollections.observableArrayList(Equation.Type.NORMAL, Equation.Type.INTEGRAL, Equation.Type.DERIVATIVE);

    /**
     * Other attributes
     */
    private int index;
    private Equation.Type choice;

    public EquationPaneController(Equation.Type type) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/EquationPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        columns = new ArrayList<>();
        rows = new ArrayList<>();
        index = Main.getBaseController().equationPaneContainer.getChildren().size();
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

        resultEngine = resultView.getEngine();
        System.out.println(resultEngine);

        errorLabel.setVisible(false);
        latexLabel.setVisible(false);
        latexRepresent.setVisible(false);
        resultLabel.setVisible(false);
        resultView.setVisible(false);

        integralBounds = new IntegralBoundsController();
        derivativePoint = new DerivativeController();

        System.out.println(integralBounds.getChildren());
        System.out.println(derivativePoint.getChildren());

        choiceBox.setItems(typeChoices);
        choiceBox.setValue(type);
        choiceBox.setOnAction(e -> {
            choice = choiceBox.getValue();
            Main.getPlot().getGraph().setTypeAt(index, choice);
            if (choice == Equation.Type.INTEGRAL) {
                integralBounds.lower.setOnAction(evt -> {
                    System.out.println("lower");
                    String lower = integralBounds.lower.getText();
                    String upper = integralBounds.upper.getText();
                    String equation = equationTextField.getText();
                    if (!lower.isEmpty() && !upper.isEmpty() && !equation.isEmpty()) {
                        resultEngine.loadContent(Calculator.integrate(equation, lower, upper));
                        resultLabel.setVisible(true);
                        resultView.setVisible(true);
                    } else {
                        resultLabel.setVisible(false);
                        resultView.setVisible(false);
                    }
                });
                integralBounds.upper.setOnAction(evt -> {
                    System.out.println("upper");
                    String lower = integralBounds.lower.getText();
                    String upper = integralBounds.upper.getText();
                    String equation = equationTextField.getText();
                    if (!lower.isEmpty() && !upper.isEmpty() && !equation.isEmpty()) {
                        System.out.println(Calculator.integrate(equation, lower, upper));
                        resultEngine.loadContent(Calculator.integrate(equation, lower, upper));
                        resultLabel.setVisible(true);
                        resultView.setVisible(true);
                    } else {
                        resultLabel.setVisible(false);
                        resultView.setVisible(false);
                    }
                });
                gridPane.add(integralBounds, 0, 1, 4, 1);
                gridPane.getChildren().remove(derivativePoint);
            } else if (choice == Equation.Type.DERIVATIVE) {
                gridPane.add(derivativePoint, 0, 1, 4, 1);
                gridPane.getChildren().remove(integralBounds);
                derivativePoint.point.setOnAction(evt->{
                    String point = derivativePoint.point.getText();
                    String equation = equationTextField.getText();
                    if (!point.isEmpty() && !equation.isEmpty()) {
                        resultEngine.loadContent(Calculator.derivative(equation, point));
                        resultLabel.setVisible(true);
                        resultView.setVisible(true);
                    } else {
                        resultLabel.setVisible(false);
                        resultView.setVisible(false);
                    }
                });
            } else {
                gridPane.getChildren().removeAll(derivativePoint, integralBounds);
            }
        });

        Main.getPlot().getGraph().addEquation(type, "", lineColorPicker.getValue());
        System.out.println(Main.getPlot().getGraph().getEquationArrayList());
    }

    public EquationPaneController() {
        this(Equation.Type.NORMAL);
    }

    @FXML
    public void deleteEquation() {
        parent.getChildren().remove(index);
        Main.getPlot().getGraph().deleteAt(index);
    }

    @FXML
    public void editEquation(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String textInput = equationTextField.getText();
            textInput = textInput.replace(" ", "");
            boolean equation = false, calculate = false;
            for (int i = 0; i < textInput.length(); ++i) {
                if (textInput.charAt(i) == 'x') equation = true;
                if (!Character.isLetterOrDigit(textInput.charAt(i)) && textInput.charAt(i) != '.') calculate = true;
            }
            if (!equation && calculate) {
                Expression expr = new Expression(textInput);
                resultEngine.loadContent(PrettyDouble.beautifyGenerateHTML(expr.calculate()));
                resultLabel.setVisible(true);
                resultView.setVisible(true);
            } else {
                resultLabel.setVisible(false);
                resultView.setVisible(false);
            }
        }
        Main.getPlot().getGraph().setEquationAt(index, equationTextField.getText());
        System.out.println(Main.getPlot().getGraph().getEquationArrayList());
    }

    @FXML
    public void setColor() {
        Main.getPlot().getGraph().setColorAt(index, lineColorPicker.getValue());
        System.out.println(Main.getPlot().getGraph().getEquationArrayList());
    }

    public Color getColor() {
        return lineColorPicker.getValue();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
