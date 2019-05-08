/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import org.apache.commons.math3.analysis.UnivariateFunction;
import static org.apache.commons.math3.util.FastMath.*;
/**
 *
 * @author tzielins
 */
public class CosineWaveform extends WrappingWaveform {
    
    
    public CosineWaveform(double phase) {
        super(phase, false, new CosineOneWidthFunction(), 0, 0.5);
    }
    
    static class CosineOneWidthFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return cos(x*2*PI)+1;
        }
        
    }
}
