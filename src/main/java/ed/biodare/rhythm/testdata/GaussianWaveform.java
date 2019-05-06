/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Gaussian;
import static org.apache.commons.math3.util.FastMath.*;

/**
 *
 * @author tzielins
 */
public class GaussianWaveform implements UnivariateFunction {
    
    public static GaussianWaveform create(double sigma, double phase) {
        return new GaussianWaveform(sigma, phase, true);
    }
    
    public static GaussianWaveform create(double sigma) {
        return create(sigma, 0);
    }
    
    
    final double sigma;
    final double phase;
    
    final double max;
    final double min;
    final UnivariateFunction function;
    
    private double area;
    
    GaussianWaveform(double sigma, double phase, boolean fixMin) {
        
        if (phase >= 1) {
            throw new IllegalArgumentException("Gaussian waveform operates only "
                    + "in [0,1) so the phase also has to be within this range");
        }
        
        this.sigma = sigma;
        this.phase = phase;
        function = new Gaussian(0, sigma);
        
        double tmax = function.value(0);
        min = fixMin ? min(function.value(0.5), function.value(-0.5)) : 0;
        max = tmax - min;
               
    }

    @Override
    public double value(double x) {
        if (x < 0 || x > 1)
            throw new IllegalArgumentException("Gaussian waveform operrates only in [0,1)");
        x = x - phase;
        if (x >= 0.5) {
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
