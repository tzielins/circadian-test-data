/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata.waveforms;

import ed.biodare.rhythm.testdata.waveforms.WrappingWaveform;
import ed.biodare.rhythm.testdata.waveforms.Waveforms;
import ed.biodare.rhythm.testdata.waveforms.PeriodicWaveform;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class PeriodicWaveformTest {
    
    double EPS = 1E-6;
    
    public PeriodicWaveformTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of value method, of class PeriodicWaveform.
     */
    @Test
    public void testValue() {
        Waveforms waveforms = new Waveforms();
        PeriodicWaveform instance = new PeriodicWaveform(24, 1, 
                waveforms.waveform(Waveforms.Shape.COS, Waveforms.Skew.NONE, 0), 0);
        
        double x = 0.0;
        double expResult = 0.5;
        double result = instance.value(x);
        assertEquals(expResult, result, EPS);
        
        x = 24.0;
        expResult = 0.5;
        result = instance.value(x);
        assertEquals(expResult, result, EPS);
        
        x = 12.0;
        expResult = -0.5;
        result = instance.value(x);
        assertEquals(expResult, result, EPS);
        
        x = 6.0;
        expResult = 0;
        result = instance.value(x);
        assertEquals(expResult, result, EPS);
        
    }
    
    @Test
    public void testValue2() {
        PeriodicWaveform instance = new PeriodicWaveform(24, 2, 
                Waveforms.waveform(Waveforms.Shape.COS, Waveforms.Skew.NONE, 0.5), 0);
        
        double x = 0.0;
        double expResult = -1;
        double result = instance.value(x);
        assertEquals(expResult, result, EPS);
        
        x = 24.0;
        expResult = -1;
        result = instance.value(x);
        assertEquals(expResult, result, EPS);
        
        x = 12.0;
        expResult = 1;
        result = instance.value(x);
        assertEquals(expResult, result, EPS);
        
        x = 6.0;
        expResult = 0;
        result = instance.value(x);
        assertEquals(expResult, result, EPS);
        
    }

    /**
     * Test of values method, of class PeriodicWaveform.
     */
    @Test
    public void testValues() {
        double[] xs = {0, 12.5, 25};
        
        WrappingWaveform wave = Waveforms.waveform(Waveforms.Shape.WIDE_PEAK, Waveforms.Skew.NONE, 0);
        PeriodicWaveform instance = new PeriodicWaveform(25, 1, wave, wave.mean());
        
        double[] expResult = {1, wave.value(0.5), 1};
        double[] result = instance.values(xs);
        assertArrayEquals(expResult, result, EPS);
    }
    
    
}
