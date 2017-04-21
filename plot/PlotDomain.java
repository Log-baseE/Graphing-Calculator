package plot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.mariuszgromada.math.mxparser.Function;

import mechanisms.Calculator;

public class PlotDomain {
	/**
	 * Function to be drawn
	 */
	private Function f;
	/**
	 * Number of periods to be drawn in domain
	 */
	private int noOfPeriods = 1;
	/**
	 * Factor for calculating period
	 * (equal to product of few small prime numbers)
	 */
	private double periodFactor = 210;
	/**
	 * Smallest period that can be detected
	 */
	private double minPeriod = Math.PI / periodFactor;
	/**
	 * Largest period that can be detected
	 */
	private double maxPeriod = Math.PI * periodFactor;
	/**
	 * Number of cycles to test before assuming function is periodic
	 */
	private int periodCycles = 5;
	/**
	 * Sample size of x to test for periodicity
	 */
	private int periodSampleSize = 100;
	/**
	 * Value to tune delta
	 */
	private double fuzz = 0.1;
	/**
	 * Sample size of x to test for monotonicity
	 */
	private int monotoneSampleSize = 10000;
	/**
	 * 
	 */
	private double maxMonotoneValue = 1000;
	
	private ArrayList<Double> positiveDiffX = new ArrayList<Double>();
	
	private ArrayList<Double> negativeDiffX = new ArrayList<Double>();
	
	private final double defaultPositiveX = 5;
	
	private final double defaultNegativeX = -5;
	
	/**
	 * Constructor with function, minimum and maximum value for period
	 * @param f
	 * @param minValue
	 * @param maxValue
	 */
	public PlotDomain(Function f, double minValue, double maxValue) {
		this.f = f;
		this.minPeriod = minValue;
		this.maxPeriod = maxValue;
	}
	
	/**
	 * Constructor with function
	 * @param f - in form of Function
	 */
	public PlotDomain(Function f) {
		this.f = f;
	}
	
	/**
	 * Constructor with function in form of string
	 * @param f - function in form of string
	 */
	public PlotDomain(String f) {
		this(new Function("f(x) = " + f));
	}
	
	/**
	 * Sets number of periods to be plotted
	 * @param noOfPeriods
	 */
	public void setNoOfPeriods(int noOfPeriods) {
		this.noOfPeriods = noOfPeriods;
	}
	
	/**
	 * Returns domain of function to be plotted
	 * @return array of minimum and maximum x-coordinates
	 */
	public double[] getDomain() {
		double[] xValues = new double[2];
		
		// Get positive domain
		double positivePeriod = getPeriod(minPeriod, maxPeriod);
		System.out.println(positivePeriod);
		if(positivePeriod == -1) {
			double positiveMonotone = this.getMonotonicity();
			if(Math.abs(positiveDiffX.get(positiveDiffX.size() - 1)) < 0.1) {
				positiveMonotone = this.trimPositiveDomain();
				if(positiveMonotone == 0)	xValues[1] = defaultPositiveX;
				else						xValues[1] = positiveMonotone;
			}
			else {
				if(positiveMonotone == 0)	xValues[1] = defaultPositiveX;
				else						xValues[1] = positiveMonotone;
			}
		}
		else {
			xValues[1] = positivePeriod * noOfPeriods;
		}
		
		// Get negative domain
		double negativePeriod = getNegativePeriod(-minPeriod, -maxPeriod);
		if(negativePeriod == -1) {
			double negativeMonotone = this.getNegativeMonotonicity();
			if(Math.abs(negativeDiffX.get(negativeDiffX.size() - 1)) < 0.1) {
				negativeMonotone = this.trimPositiveDomain();
				if(negativeMonotone == 0)	xValues[0] = defaultNegativeX;
				else						xValues[0] = negativeMonotone;
			}
			else {
				if(negativeMonotone == 0)	xValues[0] = defaultNegativeX;
				else						xValues[0] = negativeMonotone;
			}
		}
		else {
			xValues[0] = negativePeriod * noOfPeriods;
		}
		
		return xValues;
	}
	
	/**
	 * Returns period of function for positive domain
	 * @return period of function, or -1 if period is inexistent
	 */
	public double getPeriod(double min, double max) {
		double delta = midrange() * fuzz;
		double period;
		boolean isPeriodic;
		double fa, fb, fc;
		
		for(period = min; period < max; period += delta) {
			for(double x = min; x < max; x += period) {
				fa = f.calculate(x);
				isPeriodic = true;
				for(int i = 1; i <= periodCycles; ++i) {
					fb = f.calculate(x + i*period);
					fc = f.calculate(x + i*period + period/2);
					if(Math.abs(fb-fa) > delta || Math.abs(fc-fa) < delta) {
						isPeriodic = false;
					}
				}
				if(isPeriodic) {
					if(Math.abs(period) > 0.1)	return period;
					else						return Double.NaN;
				}
			}
		}
		return Double.NaN;
	}
	
	/**
	 * Returns period of function for negative domain
	 * @return period of function, or NaN if period is inexistent
	 */
	public double getNegativePeriod(double max, double min) {
		double delta = midrange() * fuzz;
		double period;
		boolean isPeriodic;
		double fa, fb, fc;
		
		for(period = max; period > min; period -= delta) {
			for(double x = max; x > min; x += period) {
				fa = f.calculate(x);
				isPeriodic = true;
				for(int i = 1; i <= periodCycles; ++i) {
					fb = f.calculate(x + i*period);
					fc = f.calculate(x + i*period + period/2);
					if(Math.abs(fb-fa) > delta || Math.abs(fc-fa) < delta) {
						isPeriodic = false;
					}
				}
				if(isPeriodic) {
					if(Math.abs(period) > 0.1)	return period;
					else						return Double.NaN;
				}
			}
		}
		return Double.NaN;
	}
	
	/**
	 * Calculates midrange of the function
	 * @return midrange (interquartile range) of function
	 */
	public double midrange() {
		double midrange, upperQuartile, lowerQuartile;
		double h = (maxPeriod - minPeriod) / periodSampleSize;
		ArrayList<Double> sampleX = new ArrayList<Double>();
		
		for(double x = minPeriod; x < maxPeriod; x += h) {
			sampleX.add(f.calculate(x));
		}
		Collections.sort(sampleX);
		
		upperQuartile = sampleX.get((int)Math.round(0.75 * periodSampleSize));
		lowerQuartile = sampleX.get((int)Math.round(0.25 * periodSampleSize));
		midrange = upperQuartile - lowerQuartile;
		
		return midrange;
	}
	
	/**
	 * Gets value of x in positive domain where function is singular
	 * @return value of x or NaN if inexistent
	 */
	public double getMonotonicity() {
		double x = 1/maxMonotoneValue;
		double diffX = Calculator.derivative(f.getFunctionExpressionString(), x);
		for(int i = 1; i <= maxMonotoneValue; ++i) {
			double newDiffX = Calculator.derivative(f.getFunctionExpressionString(), x);
			double secondDiffX = Math.abs(Calculator.secondDerivative(f, x));
			System.out.println(i + ": " + x + " " + f.calculate(x) + " " + diffX +  " " + newDiffX + " " + secondDiffX);
			positiveDiffX.add(diffX);
			if(diffX * newDiffX >= 0 && ( Math.abs(secondDiffX) < ((x*x)/maxMonotoneValue) || Math.abs(secondDiffX) > (maxMonotoneValue/(x*x))) ) {
				if(Math.abs(x) > 0.1)	return x;
				else					return 0;
			}
			diffX = newDiffX;
			x *= 1.1;
		}
		return Double.NaN;
	}
	
	/**
	 * Gets value of x in negative domain where function is singular
	 * @return value of x or NaN if inexistent
	 */
	public double getNegativeMonotonicity() {
		double x = -1/maxMonotoneValue;
		double diffX = Calculator.derivative(f.getFunctionExpressionString(), x);
		for(int i = 1; i <= monotoneSampleSize; ++i) {
			double newDiffX = Calculator.derivative(f.getFunctionExpressionString(), x);
			double secondDiffX = Math.abs(Calculator.secondDerivative(f, x));
			System.out.println(i + ": " + x + " " + f.calculate(x) + " " + diffX +  " " + newDiffX + " " + secondDiffX);
			negativeDiffX.add(diffX);
			if(diffX * newDiffX >= 0 && ( Math.abs(secondDiffX) < ((x*x)/maxMonotoneValue) || Math.abs(secondDiffX) > (maxMonotoneValue/(x*x)) ) ) {
				if(Math.abs(x) > 0.1)	return x;
				else					return 0;
			}
			diffX = newDiffX;
			x *= 1.1;
		}
		return Double.NaN;
	}
	
	public double trimPositiveDomain() {
		double delta = 0.01;
		int length = positiveDiffX.size();
		int i;
		Double lastDiff = positiveDiffX.get(length - 1);
		for(i = 0; i < length; ++i) {
			if(Math.abs(lastDiff - positiveDiffX.get(i)) >= delta)	break;
		}
		double res = 1/maxMonotoneValue * Math.pow(1.1, i+1);
		if(Math.abs(res) > 0.1)	return res;
		else					return 0;
	}
	
	public double trimNegativeDomain() {
		double delta = 0.01;
		int length = positiveDiffX.size();
		int i;
		Double lastDiff = positiveDiffX.get(length - 1);
		for(i = 0; i < length; ++i) {
			if(Math.abs(lastDiff - positiveDiffX.get(i)) >= delta)	break;
		}
		double res = -1/maxMonotoneValue * Math.pow(1.1, i+1);
		if(Math.abs(res) > 0.1)	return res;
		else					return 0;
	}
}
