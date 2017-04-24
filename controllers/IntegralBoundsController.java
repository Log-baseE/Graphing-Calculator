package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import main.Main;

import java.io.IOException;

/**
 * Created by Nicky on 3/28/2017.
 */
public class IntegralBoundsController extends GridPane{
    @FXML TextField lower;
    @FXML TextField upper;
    @FXML
    Label errorLabel;

    public IntegralBoundsController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/IntegralBounds.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
//            System.out.println("int load successful!");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading file!");
            alert.setContentText("Error loading IntegralBounds.fxml");
            alert.showAndWait();
            Main.getMainWindow().close();
        }
        lower.setText("0");
        upper.setText("1");
    }
    public String getLower(){
        return lower.getText();
    }
    public String getUpper(){
        return upper.getText();
    }
}
