import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * Created by Nicky on 3/28/2017.
 */
public class DerivativeController extends HBox{
    @FXML
    TextField point;

    public DerivativeController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/DerivativePoint.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPoint(){
        return point.getText();
    }
}
