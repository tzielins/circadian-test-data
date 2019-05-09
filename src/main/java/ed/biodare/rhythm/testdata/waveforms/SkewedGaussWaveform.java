/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata.waveforms;

import org.apache.commons.math3.analysis.UnivariateFunction;
import static org.apache.commons.math3.util.FastMath.*;
/**
 * Skewed gaussian wave.
 * Based on http://elki.dbs.ifi.lmu.de/browser/elki/elki-core-math/src/main/java/de/lmu/ifi/dbs/elki/math/statistics/distribution/SkewGeneralizedNormalDistribution.java
 * @author tzielins
 */
class SkewedGaussWaveform extends GaussianWaveform {
    
    final double ONE_BY_SQRTTWOPI = 1. / sqrt(2. * PI);
    
    final double skew;
    
    
    SkewedGaussWaveform(double sigma, double skew, double phase, boolean fixMin) {
        
        this(sigma, skew, phase, fixMin, new SkewedGaussianFunction(sigma, skew));
        
        
    }

    SkewedGaussWaveform(double sigma, double skew, double phase, boolean fixMin,
            SkewedGaussianFunction function                    
    ) {
        this(sigma, skew, phase, fixMin, 
                function, function.getPeakPosition(), findWrappingShift(function));
    }
    
    SkewedGaussWaveform(double sigma, double skew, double phase, boolean fixMin,
            UnivariateFunction function, double peakPosition,
            double wrapBound                    
    ) {
        super(sigma, phase, fixMin, function, peakPosition, wrapBound);
        this.skew = skew;
    }
    
    
    

    static double findWrappingShift(SkewedGaussianFunction function) {
        
        double lowest = function.getPeakPosition() + 0.002;
        double highest = min(function.getPeakPosition()+0.5,function.rightZeroStart);
        //System.out.println("L"+lowest+"\tH"+highest);
        double best = highest;
        double minDif = function.value(highest-1)-function.value(highest);
        
        if (minDif < 0)
            throw new IllegalStateException("The diff betwenn right and left is negative should not happen in positive"
                    + "skewed");
        
        for (double shift = highest; shift >= lowest; shift-=0.002) {
            double diff = function.value(shift-1)-function.value(shift);
            if (diff < 0) break; //we start rising
            if (diff < minDif) {
                minDif = diff;
                best = shift;
            }
        }
        return best;
    }
    
    
    static class SkewedGaussianFunction implements UnivariateFunction {

        final double ONE_BY_SQRTTWOPI = 1. / sqrt(2. * PI);
        final double sigma; 
        final double skew;
        final double rightZeroStart;
        Double peakPossition;
        Double peakValue;
        
        SkewedGaussianFunction(double sigma, double skew) {
            
            if (skew <= 0) 
                throw new IllegalArgumentException("Only positive skew values are supported");
            
            this.sigma = sigma;
            this.skew = skew;
            
            /*
              cause in pdf
                x = x / sigma;
                if ((skew * x) >= 1) return 0;                
            */            
            this.rightZeroStart = sigma / skew;
        }
        
        @Override
        public double value(double x) {
            
            return pdf(x);
        }
        
        final double pdf(double x) {
            x = x / sigma;

            if ((skew * x) >= 1) return 0; // cuase ther is log(1+skew*x) bellow

            double y = -log1p(-skew * x) / skew;

            return ONE_BY_SQRTTWOPI / sigma * exp(-.5 * y * y) / (1 - skew * x);
        }   
        
        double getPeakPosition() {
            
            if (peakPossition != null) {
                return peakPossition;
            }

            MathCheats math = new MathCheats();

            double start = -0.02; //for not skewed gaussian;
            double end =  rightZeroStart - 0.01; // it was giving wrong derivative for some combination
            if (skew < 0.1) end = sigma;

            peakPossition = math.findXForMaxInArc(this, start, end);
            peakValue = this.value(peakPossition);
            
            return peakPossition;

        }   
        
        double getPeakValue() {
            if (peakValue != null) {
                return peakValue;
            }
            
            getPeakPosition();
            return peakValue;
        }
        
    }
    
    

}
