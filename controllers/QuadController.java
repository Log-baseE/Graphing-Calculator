package quad;

import static java.lang.Math.sqrt;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.*;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


/**
 *
 * @author Ricky Gani
 */
public class FXMLDocumentController extends AnchorPane implements Initializable {
    
    @FXML
    private Label resLabel;
    @FXML
    private Label warnLabel;
    @FXML
    private TextField valA;
    @FXML
    private TextField valB;
    @FXML
    private TextField valC;


    @FXML
    private void handlecalcXAction(ActionEvent event) {
        if(isNumeric("a")) System.out.println("YES");
        
        if(valA.getText().length()==0 || valB.getText().length()==0 || valC.getText().length()==0) {
            warnLabel.setText("Please enter number in all the boxes!");
        } else if(isNumeric(valA.getText()) == false || isNumeric(valB.getText()) == false || isNumeric(valC.getText()) == false) {
            warnLabel.setText("Please Input Numbers Only");
        
        } else if(Integer.parseInt(valA.getText()) == 0) {
            warnLabel.setText("Please Input a Quadratic Equation!");
            
        } else {
            try{
            double a = Double.parseDouble(valA.getText());
            double b = Double.parseDouble(valB.getText());
            double c = Double.parseDouble(valC.getText());
            
            double d = (b*b)-4*a*c;

            d = sqrt(d);
            warnLabel.setText("");
            if(d!=d) {
                resLabel.setText("Roots are Imaginary.");
            } else if(d==0) {
                double root;
                root = ((-1*b)+d)/(2*a);
                
                resLabel.setText("Root is " + root);
            }
            else {
                double one,two;
                one = ((-1*b)+d)/(2*a);
                two = ((-1*b)-d)/(2*a);

                resLabel.setText("Roots are " + one + " and " + two);
            }
            } catch (NumberFormatException nfe) {
                warnLabel.setText("Please Input Numbers Only!");
            }
        }
    }
    
    public static boolean isNumeric(String str)  
    {  
        try  
        {  
            double d = Double.parseDouble(str);  
        }  
        catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
        return true;  
}
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
