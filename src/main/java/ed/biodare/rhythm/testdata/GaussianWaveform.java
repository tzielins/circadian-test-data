/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Gaussian;

/**
 *
 * @author tzielins
 */
public class GaussianWaveform extends WrappingWaveform {
    
    public static GaussianWaveform create(double sigma, double phase) {
        return new GaussianWaveform(sigma, phase, true);
    }
    
    public static GaussianWaveform create(double sigma) {
        return create(sigma, 0);
    }
    
    
    final double sigma;
    final double phase;
    
    
    GaussianWaveform(double sigma, double phase, boolean fixMin) {
        
        this(sigma, phase, fixMin,
            new Gaussian(0, sigma), 0, //gausioan peaks at 0
            0.5 // we are simetrical around 0, goint to 0.5
                );
        
        
               
    }
    
    GaussianWaveform(double sigma, double phase, boolean fixMin,
            UnivariateFunction function, double peakPosition,
            double wrapBound                    
    ) {
        
        super(phase,fixMin,function, peakPosition,wrapBound);
        this.sigma = sigma;
        this.phase = phase;
        
    }    

    
    
}
