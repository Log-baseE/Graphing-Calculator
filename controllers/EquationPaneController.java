package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import mechanisms.*;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import plot.DerivativeEquation;
import plot.NormalEquation;
import plot.IntegralEquation;
import main.Main;
import org.mariuszgromada.math.mxparser.Expression;

import java.awt.*;
import java.awt.image.BufferedImage;
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

    /**
     * Components
     */
    private GridPane gridPane;
    private ArrayList<ColumnConstraints> columns;
    private ArrayList<RowConstraints> rows;
    private ObservableList<NormalEquation.Type> typeChoices =
            FXCollections.observableArrayList(
                    NormalEquation.Type.NORMAL, NormalEquation.Type.INTEGRAL, NormalEquation.Type.DERIVATIVE);

    /**
     * Other attributes
     */
    private int index;
    private NormalEquation.Type choice;
    private NormalEquation thisEquation;
    private static final KeyEvent ENTER = new KeyEvent(new EventType<>(), "", "", KeyCode.ENTER,
            false, false, false, false);

    public EquationPaneController(NormalEquation.Type type, boolean ignoreURStack) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/EquationPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading file!");
            alert.setContentText("Error loading EquationPane.fxml");
            alert.showAndWait();
        }
        if(type == NormalEquation.Type.INTEGRAL){
            thisEquation = new IntegralEquation("", lineColorPicker.getValue());
        } else if(type == NormalEquation.Type.DERIVATIVE){
            thisEquation = new DerivativeEquation("", lineColorPicker.getValue());
        } else {
            thisEquation = new NormalEquation("", lineColorPicker.getValue());
        }
        if(!ignoreURStack) {
            UndoStack.push(new Action(Action.Type.ADD, index, null, thisEquation));
            RedoStack.clear();
        }
        choiceBox.setOnAction(e -> typeChange(true));
        choiceBox.setValue(type);
        choiceBox.setOnAction(e -> typeChange(false));
    }

    public EquationPaneController(NormalEquation equation) {
        this(equation.getType(), true);
        lineColorPicker.setValue(equation.getLineColor());
        equationTextField.setText(equation.getFunction());
        if (equation.getType() == NormalEquation.Type.DERIVATIVE)
            derivativePoint.point.setText(((DerivativeEquation) equation).getPoint());
        else if (equation.getType() == NormalEquation.Type.INTEGRAL) {
            integralBounds.upper.setText(((IntegralEquation) equation).getUpper());
            integralBounds.lower.setText(((IntegralEquation) equation).getLower());
        }
        editEquation(ENTER);
    }

    public EquationPaneController() {
        this(NormalEquation.Type.NORMAL, false);
    }

    @FXML
    public void deleteEquation() {
        Main.refresh();
        parent.getChildren().remove(index);
        UndoStack.push(new Action(Action.Type.DELETE, index, thisEquation, null));
        RedoStack.clear();
        Main.getBaseController().getPlot().removeGroup(thisEquation);
        Main.getGraph().getEquationArrayList().remove(thisEquation);
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
                        if(!textInput.isEmpty()) {
                            UndoStack.push(new Action(Action.Type.CHANGE_TEXT, index, oldEquation, thisEquation));
                            RedoStack.clear();
                        }
                    }
                }
                imageNormal();
                break;
            case INTEGRAL:
                String lower = integralBounds.getLower();
                String upper = integralBounds.getUpper();
                if (event.getCode() == KeyCode.ENTER) {
                    if (!lower.isEmpty() && !upper.isEmpty()) {
                        if (!textInput.isEmpty())
                            resultEngine.loadContent(
                                    PrettyDouble.beautifyGenerateHTML(
                                            Calculator.integrate(textInput, lower, upper))
                            );
                        else
                            resultEngine.loadContent(
                                    PrettyDouble.beautifyGenerateHTML(
                                            Calculator.integrate(String.valueOf(1), lower, upper))
                            );
                        resultLabel.setVisible(true);
                        resultView.setVisible(true);
                    } else {
                        resultLabel.setVisible(false);
                        resultView.setVisible(false);
                    }
                    NormalEquation oldEquation = thisEquation;
                    if (!textInput.isEmpty())
                        thisEquation = new IntegralEquation(textInput, lower, upper, lineColorPicker.getValue());
                    else
                        thisEquation = new IntegralEquation(String.valueOf(1), lower, upper, lineColorPicker.getValue());
                    Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                    if(!textInput.isEmpty()) {
                        UndoStack.push(new Action(Action.Type.CHANGE_TEXT, index, oldEquation, thisEquation));
                        RedoStack.clear();
                    }
                }
                imageIntegral();
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
                    if(!textInput.isEmpty()){
                        UndoStack.push(new Action(Action.Type.CHANGE_TEXT, index, oldEquation, thisEquation));
                        RedoStack.clear();
                    }
                }
                imageDerivative();
                break;
        }
        Main.getGraph().setEquationAt(index, thisEquation);
    }

    @FXML
    public void setColor() {
        NormalEquation oldEquation = thisEquation;
        Color oldColor = oldEquation.getLineColor();
        thisEquation.setLineColor(lineColorPicker.getValue());
        Main.getGraph().setColorAt(index, lineColorPicker.getValue());
        Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
        UndoStack.push(new Action(Action.Type.CHANGE_COLOR, index, oldColor, lineColorPicker.getValue()));
        RedoStack.clear();
//        System.out.println(Main.getGraph().getEquationArrayList());
    }

    public Color getColor() {
        return lineColorPicker.getValue();
    }

    public void refreshIndex() {
        index = Main.getBaseController().equationPaneContainer.getChildren().indexOf(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columns = new ArrayList<>();
        rows = new ArrayList<>();
        index = Main.getBaseController().equationPaneContainer.getChildren().size();
//        System.out.println("index: " + index);
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
            if (!equation.isEmpty())
                thisEquation = new IntegralEquation(equation, lower, upper, lineColorPicker.getValue());
            else
                thisEquation = new IntegralEquation(String.valueOf(1), lower, upper, lineColorPicker.getValue());
            if (!lower.isEmpty() && !upper.isEmpty()) {
                if (!equation.isEmpty())
                    resultEngine.loadContent(
                            PrettyDouble.beautifyGenerateHTML(
                                    Calculator.integrate(equation, lower, upper))
                    );
                else
                    resultEngine.loadContent(
                            PrettyDouble.beautifyGenerateHTML(
                                    Calculator.integrate(String.valueOf(1), lower, upper))
                    );
                resultLabel.setVisible(true);
                resultView.setVisible(true);
            } else {
                resultLabel.setVisible(false);
                resultView.setVisible(false);
            }
            UndoStack.push(new Action(Action.Type.CHANGE_LOWER, index, oldEquation, thisEquation));
            RedoStack.clear();
            Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
            Main.getGraph().setEquationAt(index, thisEquation);
        });
        integralBounds.lower.addEventFilter(KeyEvent.KEY_RELEASED, event -> imageIntegral());
        integralBounds.upper.setOnAction(evt -> {
            String lower = integralBounds.getLower();
            String upper = integralBounds.getUpper();
            String equation = equationTextField.getText();
            IntegralEquation oldEquation = (IntegralEquation) thisEquation;
            if (!equation.isEmpty())
                thisEquation = new IntegralEquation(equation, lower, upper, lineColorPicker.getValue());
            else
                thisEquation = new IntegralEquation(String.valueOf(1), lower, upper, lineColorPicker.getValue());
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
            UndoStack.push(new Action(Action.Type.CHANGE_UPPER, index, oldEquation, thisEquation));
            RedoStack.clear();
        });
        integralBounds.upper.addEventFilter(KeyEvent.KEY_RELEASED, event -> imageIntegral());

        derivativePoint = new DerivativeController();
        derivativePoint.point.setOnAction(evt -> {
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
            UndoStack.push(new Action(Action.Type.CHANGE_POINT, index, oldEquation, thisEquation));
            RedoStack.clear();
        });
        derivativePoint.point.addEventFilter(KeyEvent.KEY_RELEASED, event -> imageDerivative());

        choiceBox.setItems(typeChoices);
        choiceBox.setOnAction(e -> typeChange(false));
    }

    public TextField getEquationTextField() {
        return equationTextField;
    }

    public ColorPicker getLineColorPicker() {
        return lineColorPicker;
    }

    private static final int LATEX_SIZE = 40;

    private void imageIntegral() {
        String latex = "", lower = "", upper = "";
        try {
            latex = LaTeXParser.parseToLaTeX(equationTextField.getText());
            lower = LaTeXParser.parseToLaTeX(integralBounds.getLower());
            upper = LaTeXParser.parseToLaTeX(integralBounds.getUpper());
            integralBounds.errorLabel.setVisible(false);
        } catch (LaTeXParserException ex) {
            integralBounds.errorLabel.setText(ex.getMessage());
            integralBounds.errorLabel.setVisible(true);
        }
//        System.out.println(lower);
//        System.out.println(upper);
        latex = "\\int_{" + lower + "}^{" + upper + "} " + latex + "\\ dx";
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(LATEX_SIZE)
                .build();
        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D graphics2D = bufferedImage.createGraphics();
        latexRepresent.setFitHeight(icon.getIconHeight());
        icon.paintIcon(null, graphics2D, 0, 0);
        gridPane.getRowConstraints().get(3).setPrefHeight(icon.getIconHeight());
//        latexRepresent.setStyle("-fx-background-color:black");
        latexRepresent.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        latexRepresent.setVisible(true);
    }

    private void imageNormal() {
        String latex = "";
        try {
            latex = LaTeXParser.parseToLaTeX(equationTextField.getText());
            errorLabel.setVisible(false);
        } catch (LaTeXParserException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(LATEX_SIZE)
                .build();
        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D graphics2D = bufferedImage.createGraphics();
        latexRepresent.setFitHeight(icon.getIconHeight());
        icon.paintIcon(null, graphics2D, 0, 0);
        gridPane.getRowConstraints().get(3).setPrefHeight(icon.getIconHeight());
//        latexRepresent.setStyle("-fx-background-color:black");
        latexRepresent.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        latexRepresent.setVisible(true);
    }

    private void imageDerivative() {
        String latex = "", point = "";
        try {
            latex = LaTeXParser.parseToLaTeX(equationTextField.getText());
            point = LaTeXParser.parseToLaTeX(derivativePoint.getPoint());
            derivativePoint.errorLabel.setVisible(false);
        } catch (LaTeXParserException ex) {
            derivativePoint.errorLabel.setText(ex.getMessage());
            derivativePoint.errorLabel.setVisible(true);
            latexRepresent.setVisible(false);
        }
        latex = "\\frac{d}{dx}\\ " + latex + "\\Bigr|_{x=" + point + "}";
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(LATEX_SIZE)
                .build();
        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D graphics2D = bufferedImage.createGraphics();
        latexRepresent.setFitHeight(icon.getIconHeight());
        icon.paintIcon(null, graphics2D, 0, 0);
        gridPane.getRowConstraints().get(3).setPrefHeight(icon.getIconHeight());
//        latexRepresent.setStyle("-fx-background-color:black");
        latexRepresent.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        latexRepresent.setVisible(true);
    }

    public void editEquationManual(NormalEquation equation) {
        String textInput = equation.getFunction();
        NormalEquation.Type type = equation.getType();
        Color color = equation.getLineColor();
        equationTextField.setText(textInput);
        choiceBox.setOnAction(e -> typeChange(true));
        choiceBox.setValue(type);
        choiceBox.setOnAction(e -> typeChange(false));
        lineColorPicker.setValue(color);
//        System.out.println(color);
        switch (type) {
            case NORMAL: {
                boolean eq = false, calculate = false;
                for (int i = 0; i < textInput.length(); ++i) {
                    if (textInput.charAt(i) == 'x') eq = true;
                    if (!Character.isLetterOrDigit(textInput.charAt(i))
                            && textInput.charAt(i) != '.')
                        calculate = true;
                }
                if (!eq && calculate) {
                    Expression expr = new Expression(textInput);
                    resultEngine.loadContent(PrettyDouble.beautifyGenerateHTML(expr.calculate()));
                    resultLabel.setVisible(true);
                    resultView.setVisible(true);
                } else {
                    NormalEquation oldEquation = thisEquation;
                    thisEquation = equation;
                    Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
                    resultLabel.setVisible(false);
                    resultView.setVisible(false);
                }
            }
            imageNormal();
            break;
            case INTEGRAL: {
                String lower = String.valueOf(((IntegralEquation) equation).getLower());
                String upper = String.valueOf(((IntegralEquation) equation).getUpper());
                integralBounds.lower.setText(lower);
                integralBounds.upper.setText(upper);
                if (!lower.isEmpty() && !upper.isEmpty()) {
                    if (!textInput.isEmpty())
                        resultEngine.loadContent(
                                PrettyDouble.beautifyGenerateHTML(
                                        Calculator.integrate(textInput, lower, upper))
                        );
                    else
                        resultEngine.loadContent(
                                PrettyDouble.beautifyGenerateHTML(
                                        Calculator.integrate(String.valueOf(1), lower, upper))
                        );
                    resultLabel.setVisible(true);
                    resultView.setVisible(true);
                } else {
                    resultLabel.setVisible(false);
                    resultView.setVisible(false);
                }
                NormalEquation oldEquation = thisEquation;
                if (!textInput.isEmpty())
                    thisEquation = equation;
                else
                    thisEquation = equation;
                Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
            }
            imageIntegral();
            break;
            case DERIVATIVE: {
                String point = ((DerivativeEquation) equation).getPoint();
                derivativePoint.point.setText(point);
                if (!point.isEmpty() && !textInput.isEmpty()) {
                    resultEngine.loadContent(
                            PrettyDouble.beautifyGenerateHTML(Calculator.derivative(textInput, point))
                    );
                    resultLabel.setVisible(true);
                    resultView.setVisible(true);
                } else {
                    resultLabel.setVisible(false);
                    resultView.setVisible(false);
                }
                NormalEquation oldEquation = thisEquation;
                thisEquation = equation;
                Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
            }
            imageDerivative();
            break;
        }
        Main.getGraph().setEquationAt(index, thisEquation);
    }

    public void deleteManual(){
        Main.refresh();
        parent.getChildren().remove(index);
        Main.getBaseController().getPlot().removeGroup(thisEquation);
        Main.getGraph().getEquationArrayList().remove(thisEquation);
    }

    public void setColorManual(Color color) {
        NormalEquation oldEquation = thisEquation;
        thisEquation.setLineColor(color);
        lineColorPicker.setValue(color);
        Main.getGraph().setColorAt(index, color);
        Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
//        System.out.println(Main.getGraph().getEquationArrayList());
    }

    private void typeChange(boolean ignoreURStack){
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
            } else {
                resultEngine.loadContent(
                        PrettyDouble.beautifyGenerateHTML(Calculator.integrate(
                                String.valueOf(1),
                                integralBounds.lower.getText(),
                                integralBounds.upper.getText())));
                resultLabel.setVisible(true);
                resultView.setVisible(true);
            }
            errorLabel.setVisible(false);
            gridPane.add(integralBounds, 0, 1, 4, 1);
            gridPane.getChildren().remove(derivativePoint);
            NormalEquation oldEquation = thisEquation;
            if (!equationTextField.getText().isEmpty())
                thisEquation = new IntegralEquation(
                        equationTextField.getText(),
                        integralBounds.lower.getText(),
                        integralBounds.upper.getText(),
                        lineColorPicker.getValue()
                );
            else
                thisEquation = new IntegralEquation(
                        String.valueOf(1),
                        integralBounds.lower.getText(),
                        integralBounds.upper.getText(),
                        lineColorPicker.getValue()
                );
            Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
            if(!ignoreURStack) {
                UndoStack.push(new Action(Action.Type.CHANGE_TYPE, index, oldEquation, thisEquation));
                RedoStack.clear();
            }
            imageIntegral();
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
            errorLabel.setVisible(false);
            gridPane.add(derivativePoint, 0, 1, 4, 1);
            gridPane.getChildren().remove(integralBounds);
            NormalEquation oldEquation = thisEquation;
            thisEquation = new DerivativeEquation(
                    equationTextField.getText(),
                    derivativePoint.point.getText(),
                    lineColorPicker.getValue()
            );
            Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
            imageDerivative();
            if(!ignoreURStack) {
                UndoStack.push(new Action(Action.Type.CHANGE_TYPE, index, oldEquation, thisEquation));
                RedoStack.clear();
            }
//                Main.getGraph().setEquationAt(index, thisEquation);
        } else {
            gridPane.getChildren().removeAll(derivativePoint, integralBounds);
            NormalEquation oldEquation = thisEquation;
            thisEquation = new NormalEquation(equationTextField.getText(), lineColorPicker.getValue());
            Main.getBaseController().getPlot().drawCurve(oldEquation, thisEquation);
            imageNormal();
            if(!ignoreURStack) {
                UndoStack.push(new Action(Action.Type.CHANGE_TYPE, index, oldEquation, thisEquation));
                RedoStack.clear();
            }
//                Main.getGraph().setEquationAt(index, thisEquation);
        }
        if (index >= Main.getGraph().getEquationArrayList().size()) {
            Main.getGraph().addEquation(thisEquation);
        } else {
            Main.getGraph().setEquationAt(index, thisEquation);
        }
    }

}
