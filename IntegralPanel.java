import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.mariuszgromada.math.mxparser.Expression;

import javax.swing.text.NumberFormatter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.Double.NaN;
import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

/**
 * Created by Nicky on 3/24/2017.
 */
public class IntegralPanel extends AnchorPane{
    public IntegralPanel() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/IntegralPanel.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        fxmlLoader.load();
        expressionField.requestFocus();
    }

    @FXML private TextField lowerBoundField;
    @FXML private TextField upperBoundField;
    @FXML private TextField expressionField;
    @FXML private Label resultLabel;
    @FXML private WebView resultView;
    @FXML private WebEngine resultEngine;
    @FXML private Button calculateButton;

    @FXML public double calculate(){
        String expression = expressionField.getText();
        String lowerBound = lowerBoundField.getText();
        String upperBound = upperBoundField.getText();
        String s = "int(" + expression + ", x, " + lowerBound + ", " + upperBound + ")";
        System.out.println(s);
        Expression expr = new Expression(s);
        double d = expr.calculate();
        String html = PrettyDouble.beautifyGenerateHTML(d);
        resultEngine = resultView.getEngine();
        resultEngine.loadContent(html);
        resultLabel.setVisible(true);
        resultView.setVisible(true);
        return d;
    }
}
