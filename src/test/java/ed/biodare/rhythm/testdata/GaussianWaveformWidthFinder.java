/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class GaussianWaveformWidthFinder {
    
    final double EPS = 1E-6;
    
    public GaussianWaveformWidthFinder() {
    }
    
    @Before
    public void setUp() {
    }

    
    @Test
    public void findSigmaForStartsPoints() {
        
        double start = 1.0/6;
        double zerolike = 0.01;
        double nonzero = 0.1;
        double E = 0.02;
        
        double sigma = 1;
        int NMax = 100;
        
        for (;;) {
            GaussianWaveform wave = new GaussianWaveform(sigma, 0.5, false);
            if (wave.value(start) > nonzero) {
                break;
            }
            sigma = sigma * 2;
        }
        double prevH = sigma * 2;
        double prevS = 0;
        
        for (int i =0; i<NMax; i++) {
            GaussianWaveform wave = new GaussianWaveform(sigma, 0.5, false);
            
            double sW = wave.value(start);
            System.out.println("S: "+sigma+"\t"+wave.value(start)+"\t"+wave.value(0));
            if (wave.value(0) <= zerolike && sW >= (nonzero-E) && sW <= (nonzero+E)) {
                System.out.println("Found at: "+i);
                break;
            }
            
            if (sW > (nonzero+E)) {
                prevH = sigma;
                sigma = (sigma+prevS) / 2;                
            } else if (sW < (nonzero-E)) {
                prevS = sigma;
                sigma = (sigma+prevH) / 2; 
            } else {
                //double step = FastMath.min(prevH - sigma, sigma - prevS) / 2;
                prevH = sigma;
                sigma = (sigma+prevS) / 2;
                // nonzero ok but too high ends can we go lower   
                if (sigma == prevS) {
                    break;
                }                
            }

            
            
        }
       

        
        GaussianWaveform wave = new GaussianWaveform(sigma, 0.5, false);
        System.out.println("Found: "+sigma+"\t"+wave.value(start)+"\t"+wave.value(0));
        
        assertTrue(wave.value(start) >= (nonzero-E));
        assertTrue(wave.value(0) <= zerolike);
    }
    
    
}
