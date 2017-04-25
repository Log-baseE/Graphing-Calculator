package mechanisms;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.LinearSolverFactory;
import org.ejml.interfaces.linsol.LinearSolver;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

/**
 * Created by Nicky on 3/28/2017.<br><br>
 * This class is used for calculating expressions <br><br>
 * Methods included:
 *     <ul>
 *         <li>Integrals</li>
 *         <li>Derivatives</li>
 *         <li>Length of a line</li>
 *         <li>Normal calculation (e.g. 1+5*3+sin(pi/2) = 17)</li>
 *     </ul>
 */
@SuppressWarnings("unused")
public class Calculator {
    /**
     * Calculates the definite integral of a function f(x), between the lower and upper bounds
     * @param expression the expression that will be integrated
     * @param lower the lower bound of the integral
     * @param upper the upper bound of the integral
     * @return the result of the integral
     */
    public static double integrate(String expression, String lower, String upper) {
        String s = "int(" + expression + ", x, " + lower + ", " + upper + ")";
//        System.out.println(s);
        Expression expr = new Expression(s);
        return expr.calculate();
    }

    /**
     * Calculates the definite integral of a function f(x), between the lower and upper bounds
     * @param expression the expression that will be integrated
     * @param lower the lower bound of the integral
     * @param upper the upper bound of the integral
     * @return the result of the integral
     */
    public static double integrate(String expression, double lower, double upper) {
        String s = "int(" + expression + ", x, " + lower + ", " + upper + ")";
//        System.out.println(s);
        Expression expr = new Expression(s);
        return expr.calculate();
    }

    /**
     * Calculates the derivative of a function f(x) at a point x
     * @param expression the expression that will be derived
     * @return the derivative of the function f(x) at the point
     */
    public static double derivative(String expression, String point) {
        String s = "der(" + expression + ", x)";
        Expression pointExpr = new Expression(point);
//        System.out.println(s);
        Expression expr = new Expression(s, new Argument("x", pointExpr.calculate()));
        return expr.calculate();
    }

    /**
     * Calculates the derivative of a function f(x) at a point x
     * @param expression the expression that will be derived
     * @return the derivative of the function f(x) at the point
     */
    public static double derivative(String expression, double point) {
        String s = "der(" + expression + ", x)";
//        System.out.println(s);
        Expression expr = new Expression(s, new Argument("x", point));
        return expr.calculate();
    }

    /**
     * Calculates a mathematical expression
     * @param expression the expression that will be calculated
     * @return the result
     */
    public static double calculate(String expression) {
        Expression expr = new Expression(expression);
        return expr.calculate();
    }
    public static double secondDerivative(Function f, double point){
    	/*Argument x = new Argument("x");
    	Expression expr = new Expression(expression, x);
        return Calculus.derivativeNth(expr, 2, x, point, Calculus.GENERAL_DERIVATIVE, 1E-8, 20);*/
    	double h = 0.001;
    	return (f.calculate(point + h) + f.calculate(point - h) - 2*f.calculate(point)) / (h*h);
    }
    public static double[] linearSolve(double[][] aValues, double[][] bValues, int dimension) {
    	DenseMatrix64F A = new DenseMatrix64F(aValues);
    	DenseMatrix64F b = new DenseMatrix64F(bValues);
    	DenseMatrix64F X = new DenseMatrix64F(dimension,1);
    	LinearSolver<DenseMatrix64F> solver = LinearSolverFactory.general(dimension, dimension);
		solver.setA(A);
		solver.solve(b, X);
    	double[] matrixSolution = new double[dimension];
		for(int i=0; i<dimension; ++i) {
			matrixSolution[i] = X.get(i, 0);
		}
		return matrixSolution;
    }

    /**
     * Calculates the euclidean distance between two points P<sub>1</sub>(x1, y1)
     * and P<sub>2</sub>(x2,y2)
     * @param x1 the x-coordinate of P<sub>1</sub>
     * @param y1 the y-coordinate of P<sub>1</sub>
     * @param x2 the x-coordinate of P<sub>2</sub>
     * @param y2 the y-coordinate of P<sub>2</sub>
     * @return the euclidean distance between P<sub>1</sub> and P<sub>2</sub>
     */
    public static double length(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}
