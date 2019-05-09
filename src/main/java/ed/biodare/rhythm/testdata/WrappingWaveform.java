/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.UnivariateFunction;
import static org.apache.commons.math3.util.FastMath.min;

/**
 *
 * @author tzielins
 */
public class WrappingWaveform implements UnivariateFunction {
    
    
    final double phase;
    final double max;
    final double min;
    final UnivariateFunction function;
    
    final double wrapBound;
    final double peakShift;
    
    private double area;
    private double mean;
    final static double STEP = 0.001;
    
    WrappingWaveform(double phase, boolean fixMin,
            UnivariateFunction function, double peakPosition,
            double wrapBound                    
    ) {
        
        if (phase >= 1) {
            throw new IllegalArgumentException("Gaussian waveform operates only "
                    + "in [0,1) so the phase also has to be within this range");
        }
        
        this.function = function;
        this.wrapBound = wrapBound; 
        peakShift = peakPosition - phase; 
        
        double tmax = function.value(peakPosition);
        min = fixMin ? min(function.value(wrapBound-STEP), function.value(wrapBound-1)) : 0;
        max = tmax - min;
        this.phase = phase;
    }    

    @Override
    public double value(double x) {
        if (x < 0 || x > 1)
            throw new IllegalArgumentException("Gaussian waveform operrates only in [0,1), x="+x);
        x = x + peakShift;
        // not going all the way to bound is it may be super steep in skewed
        if (x > (wrapBound-STEP)) {
            x = x -1;
        }
        return (function.value(x)-min)/max;
    }
    
    public double[] values(double[] x) {
        double[] values = new double[x.length];
        for (int i =0;i<x.length;i++) {
            values[i] = value(x[i]);
        }
        return values;
    }
    
    public List<Double> valuesList(List<Double> xs) {
        List<Double> values = new ArrayList<>(xs.size());
        for (Double x : xs) {
            values.add(value(x));
        }
        return values;
    }
    
    public double area() {
        
        if (area > 0) {
            return area;
        }
        double step = STEP;
        double sum = 0;
        for (double i = 0; i< 1; i+= step) {
            sum += (function.value(i) - min);
        }
        
        area = (step*sum) / max; 
        return area;
    }
    
    public double mean() {
        if (mean > 0) {
            return mean;
        }
        int N = 0;
        double step = STEP;
        double sum = 0;
        for (double i = 0; i< 1; i+= step) {
            sum += function.value(i) - min;
            N++;
        }
        mean = (sum / max)/ N;
        return mean;
        
    }
    
    
}
