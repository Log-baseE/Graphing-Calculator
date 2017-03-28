import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Created by Nicky on 3/28/2017.
 */
public class IntegralBoundsController extends GridPane{
    @FXML TextField lower;
    @FXML TextField upper;

    public IntegralBoundsController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/IntegralBounds.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getLower(){
        return lower.getText();
    }
    public String getUpper(){
        return upper.getText();
    }
}
