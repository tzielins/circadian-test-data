/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata.waveforms;

import static ed.biodare.rhythm.testdata.waveforms.WrappingWaveform.STEP;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import static org.apache.commons.math3.util.FastMath.*;
/**
 * Skewed gaussian wave.
 * Based on http://elki.dbs.ifi.lmu.de/browser/elki/elki-core-math/src/main/java/de/lmu/ifi/dbs/elki/math/statistics/distribution/SkewGeneralizedNormalDistribution.java
 * @author tzielins
 */
class MarginSkewedGaussWaveform extends SkewedGaussWaveform {
    
    final static double SMOOTH_MARGIN = 0.1;
    
    MarginSkewedGaussWaveform(double sigma, double skew, double phase, boolean fixMin, double rightMargin) {
        
        this(sigma, skew, phase, fixMin, new SkewedGaussianFunction(sigma, skew), rightMargin);
        
        
    }

    MarginSkewedGaussWaveform(double sigma, double skew, double phase, boolean fixMin,
            SkewedGaussianFunction function, double rightMargin                    
    ) {
        this(sigma, skew, phase, fixMin, 
                function, function.getPeakPosition(), findWrappingShift(function, rightMargin));
    }
    
    MarginSkewedGaussWaveform(double sigma, double skew, double phase, boolean fixMin,
            UnivariateFunction function, double peakPosition,
            double wrapBound                    
    ) {
        this(sigma, skew, phase, fixMin, new SmoothedJoinFunction(function, wrapBound, SMOOTH_MARGIN),
                peakPosition);
    }
    
    MarginSkewedGaussWaveform(double sigma, double skew, double phase, boolean fixMin,
            SmoothedJoinFunction function, double peakPosition) {
        super(sigma, skew, phase, fixMin, function,
                peakPosition, function.wrapBound);
    }
    

    
    static class SmoothedJoinFunction implements UnivariateFunction {

        final UnivariateFunction function;
        final PolynomialSplineFunction spline;
        final double wrapBound;
        final double join;
        
        SmoothedJoinFunction(UnivariateFunction function, double wrapBound, double margin) {
            this.function = function;
            this.spline = calculateJoin(function, wrapBound, margin);
            this.join = wrapBound;
            this.wrapBound = wrapBound+margin;
        }
        
        static PolynomialSplineFunction calculateJoin(UnivariateFunction function, double wrapBound, double margin) {
            /*double[] xs = new double[15];
            double[] ys = new double[15];

            for (int i = 5; i> 0;i--) {
                double x = wrapBound-i*STEP;
                xs[5-i] = x;
                ys[5-i] = function.value(x);
            }
            
            for (int i = 0; i< 10; i++) {
                double x = wrapBound+margin+i*STEP;
                xs[5+i] = x;
                ys[5+i] = function.value(x - 1);
            }*/
            
            double[] xs = new double[3];
            double[] ys = new double[3];

            xs[0] = wrapBound;
            ys[0] = function.value(xs[0]);
            xs[2] = wrapBound+margin;
            ys[2] = function.value(xs[2]-1);
            xs[1] = wrapBound+(margin/2);
            ys[1] = (ys[0]+ys[2])/2;
            
            PolynomialSplineFunction spline = (new SplineInterpolator()).interpolate(xs,ys);
            return spline;
        }
        
        @Override
        public double value(double x) {
            if (x >= join && x < wrapBound)
                return spline.value(x);
            return function.value(x);
        }
        
    }

    static double findWrappingShift(SkewedGaussianFunction function, double rightMargin) {
        
        double ZEROLIKE = 0.05;
        double left = function.getPeakPosition() + 0.002;
        double step = STEP;
        
        while (function.value(left) >= ZEROLIKE) {
            left+=step;
        }
        
        double wrap = left+rightMargin+STEP;
        if (function.value(wrap) >= ZEROLIKE)
            throw new IllegalStateException("Wrap point is larger thatn expected: "+wrap+" min left: "+left);
        
        return wrap;
        
    }
    
    

}
