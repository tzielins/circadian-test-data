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
    
    
    final double max;
    final double min;
    final UnivariateFunction function;
    
    final double wrapBound;
    final double peakShift;
    
    private double area;
    
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
        min = fixMin ? min(function.value(wrapBound), function.value(wrapBound-1)) : 0;
        max = tmax - min;
    }    

    @Override
    public double value(double x) {
        if (x < 0 || x > 1)
            throw new IllegalArgumentException("Gaussian waveform operrates only in [0,1)");
        x = x + peakShift;
        if (x >= wrapBound) {
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
        int N = 500;
        double step = 0.5 / N;
        double sum = -N * min;
        for (int i = 0; i< N; i++) {
            sum += function.value(i*step);
        }
        
        area = (sum / max) / N;
        return area;
    }
    
    
}
