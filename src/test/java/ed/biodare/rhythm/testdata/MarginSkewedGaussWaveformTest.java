/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class MarginSkewedGaussWaveformTest {
    
    double EPS = 1E-6;
    public MarginSkewedGaussWaveformTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testWave() {
        
        double sigma = 0.4191873791721749;
        double skew = 0.8;
        double phase = 0;
        double margin = 0.25;
        
        
        MarginSkewedGaussWaveform wave = new MarginSkewedGaussWaveform(sigma, skew, phase, false, margin);
        
        List<Double> xs = IntStream.range(0, 100).mapToDouble(s -> (s/100.0)).boxed().collect(Collectors.toList());
        List<Double> ys = xs.stream().map( x -> wave.value(x)).collect(Collectors.toList());
        System.out.println(xs);
        System.out.println(ys);
        
        assertEquals(1, wave.value(0), EPS);
        
        List<Double> zeros = IntStream.range(0, 1000).mapToDouble(i -> i / 1000.0)
                .filter( x -> wave.value(x) < 0.05).boxed().collect(Collectors.toList());
        
        double left = zeros.stream().min(Comparator.naturalOrder()).get();
        double right = zeros.stream().max(Comparator.naturalOrder()).get();
        
        
        System.out.println(left+":"+(left+margin)+":"+wave.value(left)+":"+wave.value(left+margin));
        assertTrue((right - left) >= margin);
        assertTrue(wave.value(left+margin) < 0.05);
        
    }
    
}
