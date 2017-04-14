package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by Nicky on 3/28/2017.
 */
public class DerivativeController extends VBox{
    @FXML
    TextField point;
    @FXML
    Label errorLabel;

    public DerivativeController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/DerivativePoint.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
//            System.out.println("der load successful!");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading file!");
            alert.setContentText("Error loading DerivativePoint.fxml");
            alert.showAndWait();
        }
        point.setText("0");
    }

    public String getPoint(){
        return point.getText();
    }
}
