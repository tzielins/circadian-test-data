/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class GaussianWaveformTest {
    
    final double EPS = 1E-6;
    
    public GaussianWaveformTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testCreates() {
        
        GaussianWaveform wave = GaussianWaveform.create(0.1);
        assertNotNull(wave);
        
    }
    
    @Test
    public void waveformHasMaxValue1AtPhaseAnd0AtTrough() {
        
        GaussianWaveform wave = GaussianWaveform.create(0.1, 0);
        
        assertEquals(1, wave.value(0), EPS);
        assertEquals(1, wave.value(1), EPS);
        assertEquals(0, wave.value(0.5), EPS);
        
        wave = GaussianWaveform.create(0.05, 0.5);
        assertEquals(1, wave.value(0.5), EPS);
        assertEquals(0, wave.value(1), EPS);
        assertEquals(0, wave.value(0), EPS);
        
        wave = GaussianWaveform.create(0.5, 0.2);
        assertEquals(1, wave.value(0.2), EPS);
        assertEquals(0, wave.value(0.7), EPS);
        
        try {
            double v = wave.value(1.1);
            fail("Exception expected got: "+v);
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void areaWorks() {
        GaussianWaveform wave = GaussianWaveform.create(0.1, 0);
        
        double ar1 = wave.area();
        assertTrue(ar1 > 0 && ar1 < 1);
        
        assertEquals(ar1, GaussianWaveform.create(0.1, 0.1).area(), EPS);
        assertEquals(ar1, GaussianWaveform.create(0.1, 0.33).area(), EPS);
        assertTrue(ar1 > GaussianWaveform.create(0.1-EPS, 0).area());
        assertTrue(ar1 < GaussianWaveform.create(0.1+EPS, 0).area());
    }
    
    @Test
    public void values() {
        GaussianWaveform wave = GaussianWaveform.create(0.2, 0);
        
        List<Double> values = Arrays.asList(0.0,0.5,1.0);
        
        assertEquals(Arrays.asList(1.0,0.0,1.0), wave.valuesList(values));
        
        double[] arr = values.stream().mapToDouble( v -> v).toArray();
        assertArrayEquals(new double[]{1.0,0.0,1.0}, wave.values(arr), EPS);
        
    }
    
}
