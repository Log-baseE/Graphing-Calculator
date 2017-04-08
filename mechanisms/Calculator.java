package mechanisms;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

/**
 * Created by Nicky on 3/28/2017.
 */
public class Calculator {
    public static double integrate(String expression, String lower, String upper){
        String s = "int(" + expression + ", x, " + lower + ", " + upper + ")";
//        System.out.println(s);
        Expression expr = new Expression(s);
        double d = expr.calculate();
        return d;
    }
    public static double integrate(String expression, double lower, double upper){
        String s = "int(" + expression + ", x, " + lower + ", " + upper + ")";
//        System.out.println(s);
        Expression expr = new Expression(s);
        double d = expr.calculate();
        return d;
    }
    public static double derivative(String expression, String point){
        String s = "der(" + expression + ", x)";
        Expression pointExpr = new Expression(point);
//        System.out.println(s);
        Expression expr = new Expression(s, new Argument("x", pointExpr.calculate()));
        double d = expr.calculate();
        return d;
    }
    public static double derivative(String expression, double point){
        String s = "der(" + expression + ", x)";
//        System.out.println(s);
        Expression expr = new Expression(s, new Argument("x", point));
        double d = expr.calculate();
        return d;
    }
    public static double calculate(String expression){
        Expression expr = new Expression(expression);
        return expr.calculate();
    }
}
