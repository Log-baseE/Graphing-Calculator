package mechanisms;

import org.ejml.data.DenseMatrix64F;
import org.ejml.factory.LinearSolverFactory;
import org.ejml.interfaces.linsol.LinearSolver;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

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
}
