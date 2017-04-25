package plot;

import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import main.Main;

import java.util.ArrayList;

/**
 * Created by Nicky on 3/15/2017.
 */
public class Plot extends AnchorPane {
    /**
     * Stores a list of all curves in the plot
     */
    private ArrayList<Curve> listOfCurves = new ArrayList<>();
    /**
     * Stores a list of all curves in the plot
     */
    private ArrayList<Point> listOfPoints = new ArrayList<>();

    /**
     * Contains coordinates of the origin
     */
    private double originX;
    private double originY;

    /**
     * Contains spacing of x and y in pixels
     */
    private double spacingX;
    private double spacingY;

    /**
     * Contains units of each interval
     */
    private double unitX;
    private double unitY;

    /**
     * Contains bounds of x and y axes
     */
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public Plot(double width, double height) {
        setWidth(width);
        setHeight(height);
//        System.out.println(width+" "+height);
        originX = width / 2.0;
        originY = height / 2.0;
        spacingX = spacingY = 50;
        unitX = unitY = 1;
        minX = -originX / spacingX * unitX;
        maxX = (getWidth() - originX) / spacingX * unitX;
        minY = -(getHeight() - originY) / spacingY * unitY;
        maxY = originY / spacingY * unitY;
        initialize();
    }

    private void drawAxis() {
        Path x_axis = new Path();
        MoveTo moveToXAxis = new MoveTo(0, originY);
        LineTo lineToXAxis = new LineTo(getWidth(), originY);
        x_axis.setStrokeWidth(1);
        x_axis.getElements().addAll(moveToXAxis, lineToXAxis);

        Path y_axis = new Path();
        MoveTo moveToYAxis = new MoveTo(originX, 0);
        LineTo lineToYAxis = new LineTo(originX, getHeight());
        y_axis.getElements().addAll(moveToYAxis, lineToYAxis);
        y_axis.setStrokeWidth(1);

        getChildren().addAll(x_axis, y_axis);
//        System.out.println(getChildren());
    }

    private void drawGrid() {
        for (int i = 1; i * spacingX + originX < getWidth(); ++i) {
            Path y_line = new Path();
            MoveTo moveToYLine = new MoveTo(originX + i * spacingX, 0);
            LineTo lineToYLine = new LineTo(originX + i * spacingX, getHeight());
            y_line.setStroke(new Color(0.7, 0.7, 0.7, 1));
            y_line.getElements().addAll(moveToYLine, lineToYLine);
            y_line.setStrokeWidth(0.5);
            getChildren().add(y_line);
        }
        for (int i = 1; originX - i * spacingX > 0; ++i) {
            Path y_line = new Path();
            MoveTo moveToYLine = new MoveTo(originX - i * spacingX, 0);
            LineTo lineToYLine = new LineTo(originX - i * spacingX, getHeight());
            y_line.setStroke(new Color(0.7, 0.7, 0.7, 1));
            y_line.getElements().addAll(moveToYLine, lineToYLine);
            y_line.setStrokeWidth(0.5);
            getChildren().add(y_line);
        }
        for (int i = 1; i * spacingY + originY < getHeight(); ++i) {
            Path x_line = new Path();
            MoveTo moveToXLine = new MoveTo(0, originY + i * spacingY);
            LineTo lineToXLine = new LineTo(getWidth(), originY + i * spacingY);
            x_line.setStroke(new Color(0.7, 0.7, 0.7, 1));
            x_line.getElements().addAll(moveToXLine, lineToXLine);
            x_line.setStrokeWidth(0.5);
            getChildren().add(x_line);
        }
        for (int i = 1; originY - i * spacingY > 0; ++i) {
            Path x_line = new Path();
            MoveTo moveToXLine = new MoveTo(0, originY - i * spacingY);
            LineTo lineToXLine = new LineTo(getWidth(), originY - i * spacingY);
            x_line.setStroke(new Color(0.7, 0.7, 0.7, 1));
            x_line.getElements().addAll(moveToXLine, lineToXLine);
            x_line.setStrokeWidth(0.5);
            getChildren().add(x_line);
        }
    }

    private void drawNumbers() {
        final int INCREMENT = 2;
        for (int i = INCREMENT; i * unitX < maxX; i += INCREMENT) {
            Text text = new Text(String.valueOf(i * unitX));
            AnchorPane.setTopAnchor(text, originY + 5);
            AnchorPane.setLeftAnchor(text, originX + i * spacingX - text.getLayoutBounds().getWidth() / 2.0);
            getChildren().add(text);
        }
        for (int i = -INCREMENT; i * unitX > minX; i -= INCREMENT) {
            Text text = new Text(String.valueOf(i * unitX));
            AnchorPane.setTopAnchor(text, originY + 5);
            AnchorPane.setLeftAnchor(text, originX + i * spacingX - text.getLayoutBounds().getWidth() / 2.0);
            getChildren().add(text);
        }
        for (int i = INCREMENT; i * unitY < maxY; i += INCREMENT) {
            Text text = new Text(String.valueOf(i * unitY));
            AnchorPane.setTopAnchor(text, originY - i * spacingY - text.getLayoutBounds().getHeight() / 2.0);
            AnchorPane.setLeftAnchor(text, originX + 5);
            getChildren().add(text);
        }
        for (int i = -INCREMENT; i * unitY > minY; i -= INCREMENT) {
            Text text = new Text(String.valueOf(i * unitY));
            AnchorPane.setTopAnchor(text, originY - i * spacingY - text.getLayoutBounds().getHeight() / 2.0);
            AnchorPane.setLeftAnchor(text, originX + 5);
            getChildren().add(text);
        }
    }

    public void removeGroup(NormalEquation function) {
        ArrayList<NormalEquation> tempList = Main.getGraph().getEquationArrayList();
        for (NormalEquation e : tempList) {
            if (function.equals(e)) {
                for (Curve c2 : e.getCurves()) {
                    getChildren().remove(c2);
                    listOfCurves.remove(c2);
                }
                for (Point p : e.getPoints()) {
                    getChildren().remove(p);
                    listOfCurves.remove(p);
                }
                break;
            }
        }
//        System.out.println();
    }

    public void drawCurve(NormalEquation oldFunction, NormalEquation newFunction) {
        if (oldFunction != null) removeGroup(oldFunction);
        Main.getBaseController().setCursor(Cursor.WAIT);
        newFunction.draw(minX, maxX, originX, originY, spacingX, spacingY, unitX, unitY);
        for (Curve curveOf : newFunction.getCurves()) {
            int maxID = -1;
            for (Curve c : listOfCurves) {
                if (curveOf.getEquation().equalsWithoutID(c.getEquation()))
                    maxID = Math.max(maxID, c.getID());
            }
            curveOf.setID(maxID + 1);
            getChildren().add(curveOf);
            listOfCurves.add(curveOf);
        }
        for (Point pointOf : newFunction.getPoints()) {
            int maxID = -1;
            for (Point p : listOfPoints) {
                if (pointOf.getEquation().equalsWithoutID(p.getEquation()))
                    maxID = Math.max(maxID, p.getID());
            }
            pointOf.setID(maxID + 1);
            getChildren().add(pointOf);
            listOfPoints.add(pointOf);
        }
        Main.getBaseController().setCursor(Cursor.DEFAULT);
    }

    private void initialize() {
        drawGrid();
        drawAxis();
        drawNumbers();
    }
}