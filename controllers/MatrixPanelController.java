package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JLabel;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;

import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import mechanisms.Calculator;

public class MatrixPanelController extends AnchorPane implements Initializable{
	/**
	 * JavaFX components
	 */
	@FXML
	private ToolBar toolbar;
	@FXML
	private HBox toolbarSpace;
	@FXML
	private TextField dimensionField;
	@FXML
	private Button calculateButton;
	@FXML
	private Button clearButton;
	@FXML
	private TabPane tabPane;
	@FXML
	private SplitPane matrixPane;
	@FXML
	private AnchorPane matrixLeftContainer;
	@FXML
	private GridPane matrixLeftGrid;
	@FXML
	private AnchorPane matrixRightContainer;
	@FXML
	private VBox matrixRightGrid;
	@FXML
	private VBox solutionPane;
	
	/**
	 * Stage which contains scene for controller
	 */
	private Stage parent = Main.getBaseController().getMatrixWindow();
	/**
	 * Dimension of the matrix in linear system
	 */
	private int dimension;
	/**
	 * Default width of window
	 */
	private final static double DEFAULT_WIDTH = 145;
	/**
	 * Default height of window
	 */
	private final static double DEFAULT_HEIGHT = 140;
	/**
	 * Width scale of window in proportion to dimension
	 */
	private final static double WIDTH_SCALE = 80;
	/**
	 * Height scale of window in proportion to dimension
	 */
	private final static double HEIGHT_SCALE = 50;
	/**
	 * Default width of toolbar
	 */
	private final static double DEFAULT_TOOLBAR_WIDTH = 265;
	
	
	/**
	 * Constructor with dimension
	 * @param dimension of matrix in linear system
	 */
	public MatrixPanelController(int dimension) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/MatrixPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    
        this.dimension = dimension;
        dimensionField.setText(new Integer(dimension).toString());
        
        double newHeight = DEFAULT_HEIGHT + HEIGHT_SCALE * dimension;
        double newWidth = DEFAULT_WIDTH + WIDTH_SCALE * dimension;
        this.parent.setHeight(newHeight);
        this.parent.setWidth(newWidth);
        this.toolbar.setMaxWidth(newWidth);
        this.toolbar.setMinWidth(newWidth);
        this.toolbarSpace.setMaxWidth(newWidth - DEFAULT_TOOLBAR_WIDTH);
        this.toolbarSpace.setMinWidth(newWidth - DEFAULT_TOOLBAR_WIDTH);
        
        for(int i = 0; i < dimension; ++i) {
        	for(int j = 0; j < dimension; ++j) {
        		TextField textField = new TextField();
            	String rowId = String.format("%02d", i);
            	String columnId = String.format("%02d", j);
            	textField.setId("A:" + rowId + columnId);
        		textField.setPrefWidth(40);
        		matrixLeftGrid.add(textField, j, i);
        		matrixLeftGrid.setHgap(40);
        		matrixLeftGrid.setVgap(25);
        	}
        	
        	TextField textField = new TextField();
        	String rowId = String.format("%02d", i);
        	textField.setId("b:" + rowId);
    		textField.setPrefWidth(40);
        	matrixRightGrid.getChildren().add(i, textField);
    		matrixRightGrid.setSpacing(25);
        }
    }
    
	/**
	 * Calculate solutions to linear system
	 * @param event - button click event
	 */
	@FXML
	public void calculate(ActionEvent event) {
		double[][] aValues = new double[dimension][dimension];
		double[][] bValues = new double[dimension][1];
		
		for (Node node : matrixLeftGrid.getChildren()) {
		    if (node instanceof TextField) {
		    	try {
					int fieldRow = Integer.parseInt(node.getId().substring(2, 4));
					int	fieldColumn = Integer.parseInt(node.getId().substring(4, 6));
					
					String fieldText = ((TextField)node).getText().trim().replace(" ", "");
					if(fieldText.equals("")) {
						aValues[fieldRow][fieldColumn] = 0;
					}
					else {
						aValues[fieldRow][fieldColumn] = Double.parseDouble(fieldText);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		}
		
		for (Node node : matrixRightGrid.getChildren()) {
		    if (node instanceof TextField) {
		    	try {
					int fieldRow = Integer.parseInt(node.getId().substring(2, 4));
					String fieldText = ((TextField)node).getText().trim().replace(" ", "");
					if(fieldText.equals("")) {
						bValues[fieldRow][0] = 0;
					}
					else {
						bValues[fieldRow][0] = Double.parseDouble(fieldText);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		}
		
		double[] solution = Calculator.linearSolve(aValues, bValues, dimension);
		printAnswer(solution);
	}
	
	/**
	 * Prints answer to solution pane as image
	 * @param solution - array of double with x-values
	 */
	private void printAnswer(double[] solution) {
		solutionPane.getChildren().clear();
		
		for(int i=0; i<dimension; ++i) {
			ImageView imageView = new ImageView();
			BufferedImage latexBufferedImage = generateLatexImage(i+1, solution[i]);
			Image latexImage = SwingFXUtils.toFXImage(latexBufferedImage, null);
			imageView.setImage(latexImage);
			imageView.setVisible(true);
			solutionPane.getChildren().add(imageView);
		}
		
		tabPane.getSelectionModel().selectNext();
	}
	
	/**
	 * Generates buffered image of latex expression of solution
	 * @param i - index of x
	 * @param result - solution of i-th x
	 * @return buffered image
	 */
	private BufferedImage generateLatexImage(int i, double result) {
		String latex = "";
		String solution = Double.toString(result);
		if(!solution.equals("NaN"))	latex = "x_{" + i + "}=" + solution;
		else						latex = "x_{" + i + "} \\text{ has no solution.}";
        
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();
        icon.setInsets(new Insets(5, 5, 5, 5));
        
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setColor(new Color(244, 244, 244));
        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);
        
        return bufferedImage;
	}
	
	/**
	 * Clears all text fields in the input pane
	 * @param event - button click event
	 */
	@FXML
	public void clear(ActionEvent event) {
		for(Node node : matrixLeftGrid.getChildren()) {
		    if(node instanceof TextField) {
		        ((TextField)node).setText("");
		    }
		}
		for(Node node : matrixRightGrid.getChildren()) {
		    if(node instanceof TextField) {
		        ((TextField)node).setText("");
		    }
		}
		solutionPane.getChildren().clear();
	}
	
	/**
	 * Performs check when dimension field is editted
	 * @param event - keyboard event
	 */
	@FXML
	public void editDimension(KeyEvent event) {
		String dimensionText = dimensionField.getText();
		try {
			if(event.getCode() == KeyCode.ENTER) {
				int newDimension = Integer.parseInt(dimensionText);
				if(newDimension > 10 || newDimension < 2) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Dimension Limits");
					alert.setHeaderText(null);
					alert.setContentText("Matrix dimensions should be between 2 to 10.");
					alert.showAndWait();
				}
				else {
					setDimension(newDimension);
				}
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * Sets dimension of matrix in the linear system
	 * @param dimension
	 */
	public void setDimension(int dimension) {
		Main.getBaseController().createMatrixWindow(dimension);
	}

	/**
	 * Initialises the object
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
