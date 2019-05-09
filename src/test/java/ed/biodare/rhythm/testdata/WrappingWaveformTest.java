/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import org.apache.commons.math3.analysis.UnivariateFunction;
import static org.apache.commons.math3.util.FastMath.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class WrappingWaveformTest {
    
    double EPS = 1E-6;
    
    public WrappingWaveformTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testMean() {
        
        WrappingWaveform wave = new CosineWaveform(0.5);
        
        assertEquals(0.5, wave.mean(), EPS);
        assertEquals(0.5, wave.mean(), EPS);
        
        wave = new CosineWaveform(0);
        assertEquals(0.5, wave.mean(), EPS);
        
        wave = new WrappingWaveform(0.5, true, new TwoFunction(), 0.5, 1);
        assertEquals(1, wave.mean(), 0.01);
    }
    
    @Test
    public void testArea() {
        
        WrappingWaveform wave = new CosineWaveform(0.5);
        
        assertEquals(0.5, wave.area(), EPS);
        
        wave = new CosineWaveform(0);
        assertEquals(0.5, wave.area(), EPS);
        
        wave = new WrappingWaveform(0.5, true, new TwoFunction(), 0.5, 1);
        assertEquals(1, wave.area(), 0.01);
        
    } 
    
    static class TwoFunction implements UnivariateFunction {

        @Override
        public double value(double d) {
            if (d == 0 || d == 1) return 0;
            return 2;
        }
        
    }
    
}
