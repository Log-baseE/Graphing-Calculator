package controllers;

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
import plot.DerivativeEquation;
import plot.NormalEquation;
import plot.IntegralEquation;
import main.Main;
import mechanisms.Calculator;
import mechanisms.PrettyDouble;
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
    private ChoiceBox<NormalEquation.Type> choiceBox;
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

    /**Integral/derivative components*/
    private IntegralBoundsController integralBounds;
    private DerivativeController derivativePoint;

    /**Parent components*/
    @FXML
    private VBox parent = Main.getBaseController().equationPaneContainer;

    /**Components*/
    private GridPane gridPane;
    private ArrayList<ColumnConstraints> columns;
    private ArrayList<RowConstraints> rows;
    private ObservableList<NormalEquation.Type> typeChoices =
            FXCollections.observableArrayList(NormalEquation.Type.NORMAL, NormalEquation.Type.INTEGRAL, NormalEquation.Type.DERIVATIVE);

    /** Other attributes */
    private int index;
    private NormalEquation.Type choice;
    private NormalEquation thisEquation;

    public EquationPaneController(NormalEquation.Type type) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/EquationPane.fxml"));
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

        errorLabel.setVisible(false);
        latexLabel.setVisible(false);
        latexRepresent.setVisible(false);
        resultLabel.setVisible(false);
        resultView.setVisible(false);

        integralBounds = new IntegralBoundsController();
        integralBounds.lower.setOnAction(evt -> {
//                    System.out.println("lower");
            String lower = integralBounds.lower.getText();
            String upper = integralBounds.upper.getText();
            String equation = equationTextField.getText();
            IntegralEquation oldEquation = (IntegralEquation) thisEquation;
            thisEquation = new IntegralEquation(equation, lower, upper, lineColorPicker.getValue());
            if (!lower.isEmpty() && !upper.isEmpty() && !equation.isEmpty()) {
                resultEngine.loadContent(
                        PrettyDouble.beautifyGenerateHTML(Calculator.integrate(equation, lower, upper))
                );
                resultLabel.setVisible(true);
                resultView.setVisible(true);
            } else {
                resultLabel.setVisible(false);
                resultView.setVisible(false);
            }
            Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
            Main.getGraph().setEquationAt(index, thisEquation);
        });
        integralBounds.upper.setOnAction(evt -> {
            String lower = integralBounds.getLower();
            String upper = integralBounds.getUpper();
            String equation = equationTextField.getText();
            IntegralEquation oldEquation = (IntegralEquation) thisEquation;
            thisEquation = new IntegralEquation(equation, lower, upper, lineColorPicker.getValue());
            if (!lower.isEmpty() && !upper.isEmpty() && !equation.isEmpty()) {
//                        System.out.println(Calculator.integrate(equation, lower, upper));
                resultEngine.loadContent(
                        PrettyDouble.beautifyGenerateHTML(Calculator.integrate(equation, lower, upper)));
                resultLabel.setVisible(true);
                resultView.setVisible(true);
            } else {
                resultLabel.setVisible(false);
                resultView.setVisible(false);
            }
            Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
            Main.getGraph().setEquationAt(index, thisEquation);
        });

        derivativePoint = new DerivativeController();
        derivativePoint.point.setOnAction(evt -> {
            System.out.println("ACTION!");
            String point = derivativePoint.getPoint();
            String equation = equationTextField.getText();
            DerivativeEquation oldEquation = (DerivativeEquation) thisEquation;
            thisEquation = new DerivativeEquation(equation, point, lineColorPicker.getValue());
            ((DerivativeEquation) thisEquation).setPoint(point);
            if (!point.isEmpty() && !equation.isEmpty()) {
                resultEngine.loadContent(
                        PrettyDouble.beautifyGenerateHTML(Calculator.derivative(equation, point))
                );
                resultLabel.setVisible(true);
                resultView.setVisible(true);
            } else {
                resultLabel.setVisible(false);
                resultView.setVisible(false);
            }
            Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
            Main.getGraph().setEquationAt(index, thisEquation);
        });

        if (type == NormalEquation.Type.INTEGRAL) {
            if (!equationTextField.getText().isEmpty()) {
                resultEngine.loadContent(
                        PrettyDouble.beautifyGenerateHTML(Calculator.integrate(
                                equationTextField.getText(),
                                integralBounds.lower.getText(),
                                integralBounds.upper.getText())));
                resultLabel.setVisible(true);
                resultView.setVisible(true);
            }
            gridPane.add(integralBounds, 0, 1, 4, 1);
            gridPane.getChildren().remove(derivativePoint);
            thisEquation = new IntegralEquation(
                    equationTextField.getText(),
                    integralBounds.lower.getText(),
                    integralBounds.upper.getText(),
                    lineColorPicker.getValue()
            );
        } else if (type == NormalEquation.Type.DERIVATIVE) {
            if (!equationTextField.getText().isEmpty()) {
                resultEngine.loadContent(
                        PrettyDouble.beautifyGenerateHTML(Calculator.derivative(
                                equationTextField.getText(),
                                derivativePoint.point.getText()
                        )));
                resultLabel.setVisible(true);
                resultView.setVisible(true);
            }
            gridPane.add(derivativePoint, 0, 1, 4, 1);
            gridPane.getChildren().remove(integralBounds);
            thisEquation = new DerivativeEquation(
                    equationTextField.getText(),
                    derivativePoint.point.getText(),
                    lineColorPicker.getValue()
            );
        } else {
            thisEquation = new NormalEquation("", lineColorPicker.getValue());
        }
        Main.getGraph().addEquation(thisEquation);

        choiceBox.setItems(typeChoices);
        choiceBox.setValue(type);
        choiceBox.setOnAction(e -> {
            choice = choiceBox.getValue();
            if (choice == NormalEquation.Type.INTEGRAL) {
                if (!equationTextField.getText().isEmpty()) {
                    resultEngine.loadContent(
                            PrettyDouble.beautifyGenerateHTML(Calculator.integrate(
                            equationTextField.getText(),
                            integralBounds.lower.getText(),
                            integralBounds.upper.getText())));
                    resultLabel.setVisible(true);
                    resultView.setVisible(true);
                }
                integralBounds.lower.setOnAction(evt -> {
//                    System.out.println("lower");
                    String lower = integralBounds.lower.getText();
                    String upper = integralBounds.upper.getText();
                    String equation = equationTextField.getText();
                    IntegralEquation oldEquation = (IntegralEquation) thisEquation;
                    thisEquation = new IntegralEquation(equation, lower, upper, lineColorPicker.getValue());
                    if (!lower.isEmpty() && !upper.isEmpty() && !equation.isEmpty()) {
                        resultEngine.loadContent(
                                PrettyDouble.beautifyGenerateHTML(Calculator.integrate(equation, lower, upper))
                        );
                        resultLabel.setVisible(true);
                        resultView.setVisible(true);
                    } else {
                        resultLabel.setVisible(false);
                        resultView.setVisible(false);
                    }
                    Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                    Main.getGraph().setEquationAt(index, thisEquation);
                });
                integralBounds.upper.setOnAction(evt -> {
                    String lower = integralBounds.getLower();
                    String upper = integralBounds.getUpper();
                    String equation = equationTextField.getText();
                    IntegralEquation oldEquation = (IntegralEquation) thisEquation;
                    thisEquation = new IntegralEquation(equation, lower, upper, lineColorPicker.getValue());
                    if (!lower.isEmpty() && !upper.isEmpty() && !equation.isEmpty()) {
//                        System.out.println(Calculator.integrate(equation, lower, upper));
                        resultEngine.loadContent(
                                PrettyDouble.beautifyGenerateHTML(Calculator.integrate(equation, lower, upper)));
                        resultLabel.setVisible(true);
                        resultView.setVisible(true);
                    } else {
                        resultLabel.setVisible(false);
                        resultView.setVisible(false);
                    }
                    Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                    Main.getGraph().setEquationAt(index, thisEquation);
                });
                gridPane.add(integralBounds, 0, 1, 4, 1);
                gridPane.getChildren().remove(derivativePoint);
                NormalEquation oldEquation = thisEquation;
                thisEquation = new IntegralEquation(
                        equationTextField.getText(),
                        integralBounds.lower.getText(),
                        integralBounds.upper.getText(),
                        lineColorPicker.getValue()
                );
                Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                Main.getGraph().setEquationAt(index, thisEquation);
            } else if (choice == NormalEquation.Type.DERIVATIVE) {
                if (!equationTextField.getText().isEmpty()) {
                    resultEngine.loadContent(
                            PrettyDouble.beautifyGenerateHTML(Calculator.derivative(
                            equationTextField.getText(),
                            derivativePoint.point.getText()
                            )));
                    resultLabel.setVisible(true);
                    resultView.setVisible(true);
                }
                gridPane.add(derivativePoint, 0, 1, 4, 1);
                gridPane.getChildren().remove(integralBounds);
                NormalEquation oldEquation = thisEquation;
                thisEquation = new DerivativeEquation(
                        equationTextField.getText(),
                        derivativePoint.point.getText(),
                        lineColorPicker.getValue()
                );
                Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                Main.getGraph().setEquationAt(index, thisEquation);
            } else {
                gridPane.getChildren().removeAll(derivativePoint, integralBounds);
                NormalEquation oldEquation = thisEquation;
                thisEquation = new NormalEquation(equationTextField.getText(), lineColorPicker.getValue());
                Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                Main.getGraph().setEquationAt(index, thisEquation);
            }
        });
    }

    public EquationPaneController() {
        this(NormalEquation.Type.NORMAL);
    }

    @FXML
    public void deleteEquation() {
        Main.refresh();
        parent.getChildren().remove(index);
        Main.getBaseController().getPlot().removeGroup(thisEquation);
        Main.getGraph().getEquationArrayList().remove(thisEquation);
//        System.out.println(Main.getGraph().getEquationArrayList());
    }

    @FXML
    public void editEquation(KeyEvent event) {
        String textInput = equationTextField.getText();
        textInput = textInput.replace(" ", "");
        switch (choiceBox.getValue()) {
            case NORMAL:
                if (event.getCode() == KeyCode.ENTER) {
                    boolean equation = false, calculate = false;
                    for (int i = 0; i < textInput.length(); ++i) {
                        if (textInput.charAt(i) == 'x') equation = true;
                        if (!Character.isLetterOrDigit(textInput.charAt(i)) && textInput.charAt(i) != '.')
                            calculate = true;
                    }
                    if (!equation && calculate) {
                        Expression expr = new Expression(textInput);
                        resultEngine.loadContent(PrettyDouble.beautifyGenerateHTML(expr.calculate()));
                        resultLabel.setVisible(true);
                        resultView.setVisible(true);
                    } else {
                        NormalEquation oldEquation = thisEquation;
                        thisEquation = new NormalEquation(textInput, lineColorPicker.getValue());
                        Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                        resultLabel.setVisible(false);
                        resultView.setVisible(false);
                    }
                }
                break;
            case INTEGRAL:
                if (event.getCode() == KeyCode.ENTER) {
                    String lower = integralBounds.getLower();
                    String upper = integralBounds.getUpper();
                    String equation = equationTextField.getText();
                    if (!lower.isEmpty() && !upper.isEmpty() && !equation.isEmpty()) {
                        resultEngine.loadContent(
                                PrettyDouble.beautifyGenerateHTML(Calculator.integrate(equation, lower, upper))
                        );
                        resultLabel.setVisible(true);
                        resultView.setVisible(true);
                    } else {
                        resultLabel.setVisible(false);
                        resultView.setVisible(false);
                    }
                    NormalEquation oldEquation = thisEquation;
                    thisEquation = new IntegralEquation(equation, lower, upper, lineColorPicker.getValue());
                    Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                }
                break;
            case DERIVATIVE:
                if (event.getCode() == KeyCode.ENTER) {
                    String point = derivativePoint.point.getText();
                    String equation = equationTextField.getText();
                    if (!point.isEmpty() && !equation.isEmpty()) {
                        resultEngine.loadContent(
                                PrettyDouble.beautifyGenerateHTML(Calculator.derivative(equation, point))
                        );
                        resultLabel.setVisible(true);
                        resultView.setVisible(true);
                    } else {
                        resultLabel.setVisible(false);
                        resultView.setVisible(false);
                    }
                    NormalEquation oldEquation = thisEquation;
                    thisEquation = new DerivativeEquation(equation, point, lineColorPicker.getValue());
                    Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                }
                break;
        }
        Main.getGraph().setEquationAt(index, thisEquation);
//        System.out.println(Main.getGraph().getEquationArrayList());
    }

    @FXML
    public void setColor() {
        NormalEquation oldEquation = thisEquation;
        thisEquation.setLineColor(lineColorPicker.getValue());
        Main.getGraph().setColorAt(index, lineColorPicker.getValue());
        Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
//        System.out.println(Main.getGraph().getEquationArrayList());
    }

    public Color getColor() {
        return lineColorPicker.getValue();
    }

    public void refreshIndex(){
        index = Main.getBaseController().equationPaneContainer.getChildren().indexOf(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public TextField getEquationTextField(){
        return equationTextField;
    }
}
