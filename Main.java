
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{
    public Stage mainWindow;
    private static Plot thisPlot;
    private static BaseController baseController;

    public static void main(String[] args){
        thisPlot = new Plot();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        baseController = new BaseController();
        Scene mainScene = new Scene(baseController);
        mainWindow = primaryStage;
        mainWindow.setMinHeight(600);
        mainWindow.setMinWidth(1200);
        mainWindow.setScene(mainScene);
        mainWindow.getIcons().add(new Image("resources/icon.png"));
        mainWindow.setTitle("Untitled Graph");
        mainWindow.setMaximized(true);
        mainWindow.show();
    }

    public static BaseController getBaseController(){return baseController;}
    public static Plot getPlot(){return thisPlot;}
}