package plot;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by Nicky on 4/9/2017.
 */
@SuppressWarnings("WeakerAccess")
public class Point extends Circle {
    private static final double radius = 4;
    private NormalEquation equation;
    private int id;
    private Tooltip tooltip;

    public Point(NormalEquation equation, double x, double y, double oriX, double oriY, Color color) {
        super(x, y, radius, color);
        this.equation = equation;
        tooltip = new Tooltip("(" + oriX + ", " + oriY + ")");
        Tooltip.install(this, tooltip);
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public NormalEquation getEquation() {
        return equation;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) return false;
        else {
            Point otherPoint = (Point) obj;
            return (getCenterX() == otherPoint.getCenterX() &&
                    getCenterY() == otherPoint.getCenterY() &&
                    id == otherPoint.getID() &&
                    getFill().equals(otherPoint.getFill())
            );
        }
    }
}
