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
public class GaussianWaveformTest {
    
    public GaussianWaveformTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testCreates() {
        
        GaussianWaveform wave = GaussianWaveform.create();
        assertNotNull(wave);
        
    }
    
}
