/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class CosineWaveformTest {
    
    double EPS = 1E-6;
    
    public CosineWaveformTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testGetsWaveform() {
        
        CosineWaveform wave = new CosineWaveform(0.5);
        
        assertEquals(0, wave.value(0),EPS);
        assertEquals(0, wave.value(1),EPS);
        assertEquals(1, wave.value(0.5),EPS);
        
        wave = new CosineWaveform(0);
        
        assertEquals(1, wave.value(0),EPS);
        assertEquals(1, wave.value(1),EPS);
        assertEquals(0, wave.value(0.5),EPS);        
    }
    
}
