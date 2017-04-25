package main;

import controllers.BaseController;
import controllers.EquationPaneController;
import controllers.RedoStack;
import controllers.UndoStack;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import plot.NormalEquation;
import plot.Plot;

import java.io.*;
import java.util.Optional;
import java.util.Random;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Main extends Application {
    private static Stage mainWindow;
    private static Graph graph;
    private static BaseController baseController;
    private static File file;
    private static boolean saved = true;

    public static void main(String[] args) {
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
        mainWindow.setOnCloseRequest(e -> {
            e.consume();
            if (!UndoStack.empty() && !saved) {
                String fileName;
                if (file != null) fileName = file.getName();
                else fileName = "Untitled";
                Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Do you want to save " + fileName + " before closing?",
                        ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.setTitle("Save file?");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.YES) {
                    baseController.saveGraph();
                } else if (result.get() == ButtonType.NO) {
                    mainWindow.close();
                } else {
                    alert.close();
                }
            } else {
                mainWindow.close();
            }
        });
        mainWindow.show();
        graph = new Graph();
        baseController.addPlot();
        Random random = new Random();
        baseController.addManualPane(
                new NormalEquation(
                        "",
                        new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1)
                ));
    }

    public static BaseController getBaseController() {
        return baseController;
    }

    public static Graph getGraph() {
        return graph;
    }

    public static void newGraph() {
        if (!UndoStack.empty() && !saved) {
            String fileName;
            if (file != null) fileName = file.getName();
            else fileName = "Untitled";
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to save " + fileName + " before closing?",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Save file?");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                baseController.saveGraph();
                if(file==null) return;
            } else if(result.get() == ButtonType.NO){
                alert.close();
            } else {
                alert.close();
                return;
            }
        }
        baseController = new BaseController();
        mainWindow.setMinHeight(600);
        mainWindow.setMinWidth(1200);
        mainWindow.getIcons().add(new Image("resources/icon.png"));
        mainWindow.setTitle("Untitled Graph");
        mainWindow.setMaximized(false);
        mainWindow.setScene(new Scene(baseController));
        mainWindow.setMaximized(true);
        graph = new Graph();
        baseController.addPlot();
        baseController.addEquation();
        UndoStack.clear();
        RedoStack.clear();
        file = null;

    }

    public static void refresh() {
        for (Node node : baseController.equationPaneContainer.getChildren()) {
            EquationPaneController epc = (EquationPaneController) node;
            epc.refreshIndex();
        }
    }

    public static void load(File file) {
        try {
            if (file.equals(Main.file)) return;
            baseController = new BaseController();
            mainWindow.setMinHeight(600);
            mainWindow.setMinWidth(1200);
            mainWindow.getIcons().add(new Image("resources/icon.png"));
            mainWindow.setTitle("Untitled Graph");
            mainWindow.setMaximized(false);
            mainWindow.setScene(new Scene(baseController));
            mainWindow.setMaximized(true);
            graph = new Graph();
            baseController.addPlot();
            String fileName = file.getName().substring(
                    0, file.getName().indexOf(Graph.EXTENSION.substring(1)));
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            boolean first = false;
            while ((line = bufferedReader.readLine()) != null) {
                if (!first) {
                    Main.getBaseController().setBaseTitle(line);
                    first = true;
                } else baseController.addManualPane(graph.load(line));
            }
            Main.file = file;
            mainWindow.setTitle(fileName + " - [" + file.getAbsolutePath() + "] - Graphing Calculator");
            baseController.setBaseTitle(fileName);
            fileReader.close();
            UndoStack.clear();
            RedoStack.clear();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File not found!");
            alert.setContentText("Error loading " + file.getName());
            alert.showAndWait();
        } catch (IOException | NullPointerException ignored) {
        } catch (GraphLoadException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading file!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public static File saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Graph file", Graph.EXTENSION)
        );
        if (file != null) fileChooser.setInitialDirectory(file.getParentFile());
        fileChooser.setTitle("Save As");
        File file = fileChooser.showSaveDialog(mainWindow);
        if(file!=null) saved = true;
        return file;
    }

    public static void save(File file) {
        try {
            String fileName = file.getName().substring(
                    0, file.getName().indexOf(Graph.EXTENSION.substring(1)));
            System.out.println("Filename: " + fileName);
            PrintWriter printWriter = new PrintWriter(file.getAbsolutePath());
            printWriter.println(fileName);
            for (NormalEquation e : graph.getEquationArrayList()) {
                printWriter.println(e.toString());
            }
            Main.file = file;
            mainWindow.setTitle(fileName + " - [" + file.getAbsolutePath() + "] - Graphing Calculator");
            baseController.setBaseTitle(fileName);
            printWriter.close();
            saved = true;
        } catch (IOException | NullPointerException ignored) {
        }
    }

    public static void save() {
        save(file);
    }

    public static File getFile() {
        return file;
    }

    public static Stage getMainWindow() {
        return mainWindow;
    }

    public static void setSave(boolean saved) {
        Main.saved = saved;
    }
}