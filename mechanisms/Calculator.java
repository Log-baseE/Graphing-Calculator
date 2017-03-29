package mechanisms;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

/**
 * Created by Nicky on 3/28/2017.
 */
public class Calculator {
    public static String integrate(String expression, String lower, String upper){
        String s = "int(" + expression + ", x, " + lower + ", " + upper + ")";
        System.out.println(s);
        Expression expr = new Expression(s);
        double d = expr.calculate();
        return PrettyDouble.beautifyGenerateHTML(d);
    }
    public static String derivative(String expression, String point){
        String s = "der(" + expression + ", x)";
        Expression pointExpr = new Expression(point);
        System.out.println(s);
        Expression expr = new Expression(s, new Argument("x", pointExpr.calculate()));
        double d = expr.calculate();
        return PrettyDouble.beautifyGenerateHTML(d);
    }
    public static String calculate(String expression){
        Expression expr = new Expression(expression);
        return PrettyDouble.beautifyGenerateHTML(expr.calculate());
    }
}
