/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import ed.biodare.rhythm.testdata.SkewedGaussWaveform.SkewedGaussianFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class SkewedGaussWaveformTest {

    final double EPS = 1E-6;
    
    public SkewedGaussWaveformTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void testGivesWave() {
        double sigma = 0.3;
        double skew = 0.8;
        SkewedGaussWaveform wave = new SkewedGaussWaveform(sigma, skew, 0.5, false);
        
        //System.out.println(wave.peakShift);
        //printF(wave,0,1,0.01);
        assertEquals(1, wave.value(0.5),EPS);
        
        sigma = 0.3;
        skew = 0.4;
        SkewedGaussWaveform wave2 = new SkewedGaussWaveform(sigma, skew, 0.5, true);
        assertEquals(1, wave2.value(0.5),EPS);
        
        double min = IntStream.range(0, 10000).mapToDouble( i -> i / 10000.0)
                                .map( x -> wave2.value(x))
                                .min().getAsDouble();
        
        assertEquals(0, min,0.001);
        
    }
    
    @Test
    public void testFindWrappingShift() {
       
        double sigma = 0.3;
        double skew = 0.8;
        
        SkewedGaussianFunction function = new SkewedGaussianFunction(sigma, skew);
        
        SkewedGaussWaveform wave = new SkewedGaussWaveform(sigma, skew, 0, false);
        
        double shift = wave.findWrappingShift(function);
        System.out.println(shift);
        assertTrue(shift > 0.17);
        assertTrue(shift < 0.5);
        
        sigma = 0.3;
        skew = 0.01;
        
        function = new SkewedGaussianFunction(sigma, skew);        
        wave = new SkewedGaussWaveform(sigma, skew, 0, false);
        
        //printF(function, 0, 1, 0.02);
        shift = wave.findWrappingShift(function);
        System.out.println(shift);
        assertTrue(shift > 0.45);
        //assertTrue(shift < 0.5);
        
    }
    
    protected void printF(UnivariateFunction function, double l, double h, double step) {
        List<Double> X = new ArrayList<>();
        List<Double> Y = new ArrayList<>();
        
        for (double x = l; x < h; x+=step) {
            double y = function.value(x);
            X.add(x);
            Y.add(y); 
        }
        
        System.out.println(X);
        System.out.println(Y);
        
    }

    @Test
    public void testPeakPosition() {
        
        double sigma = 0.3;
        double skew = 0.8;
        
        
        SkewedGaussianFunction wave = new SkewedGaussianFunction(sigma, skew);
        
        double peak = wave.getPeakPosition();
        
        assertEquals(0.1773, peak, 0.0001);
        
        skew = 0.01;
        wave = new SkewedGaussianFunction(sigma, skew);
        
        peak = wave.getPeakPosition();
        //System.out.println(wave.value(0)+"\t"+wave.value(peak));
        assertEquals(0.0, peak, 0.01);
        
    }
    
    @Test
    public void testSkewedPdf() {
        
        double sigma = 0.3;
        double skew = 0.8;
        
        double[] xs = {0.,  0.1, 0.2, 0.3, 0.4};
        double[] exps = {1.3298076,  1.68208822, 1.81009519, 0.87880505, 0.};
        
        double[] res = new double[xs.length];
        
        SkewedGaussianFunction wave = new SkewedGaussianFunction(sigma, skew);
        
        for (int i = 0; i<xs.length; i++) {
            res[i] = wave.pdf(xs[i]);
        }
        
        assertArrayEquals(exps, res, EPS);
    
    }
    
    
}