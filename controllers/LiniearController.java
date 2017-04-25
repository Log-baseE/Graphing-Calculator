package linear;

import static java.lang.Math.sqrt;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

/**
 *
 * @author Ricky Gani
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label resLabel;
    
    @FXML
    private Label warnLabel;
    
    @FXML 
    private TextField valA1;
    
    @FXML 
    private TextField valA2;
    
    @FXML 
    private TextField valB1;
    
    @FXML 
    private TextField valB2;
    
    @FXML 
    private TextField valC1;
    
    @FXML 
    private TextField valC2;
    
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {  
        
        
        if(valA1.getText().length()==0 || valB1.getText().length()==0 || valC1.getText().length()==0 || valA2.getText().length()==0 || valB2.getText().length()==0 || valC2.getText().length()==0) {
            warnLabel.setText("Please enter number in all the boxes!");
        }else {
            try {
                double a = Double.parseDouble(valA1.getText());
                double b = Double.parseDouble(valB1.getText());
                double e = Double.parseDouble(valC1.getText());
                double c = Double.parseDouble(valA2.getText());
                double d = Double.parseDouble(valB2.getText());
                double f = Double.parseDouble(valC2.getText());
                warnLabel.setText("");
                double dtr = a*d - b*c;
                if (dtr != 0) {
                    double x = (e*d - b*f)/dtr;
                    double y = (a*f - e*c)/dtr;

                    if (y == -0)y *=y;
                    if (x == -0)x *=x; 

                    resLabel.setText("x = "+x+"         y = "+y);
                } else {
                    resLabel.setText("There is no solution for this equation!");
                }
            } catch (NumberFormatException nfe) {
                warnLabel.setText("Please Input Numbers Only!");
            }
            
        }
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
