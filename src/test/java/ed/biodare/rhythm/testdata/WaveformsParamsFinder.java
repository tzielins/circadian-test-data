/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.Arrays;
import java.util.List;
import static org.apache.commons.math3.util.FastMath.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class WaveformsParamsFinder {
    
    final double EPS = 1E-6;
    
    public WaveformsParamsFinder() {
    }
    
    @Before
    public void setUp() {
    }

    
    @Test
    public void findSkewedSigmas() {
        
        // starts = [0.0, 0.16666666666666666, 0.25, 0.375]
        double[] sigmas = {0.234375, 0.15625, 0.1171875, 0.05859375};
        double[] skews = { 0.4, 0.8, 1.2};
        
        for (double sigma : sigmas) {
            for (double skew : skews) {
                
                double skewedSigma = findSkewedSigmaMatchingAread(skew, sigma);
                System.out.println("SOrg: "+sigma+"\tSkew: "+skew+"\tSkewd: "+skewedSigma);
                
                GaussianWaveform norm = new GaussianWaveform(sigma, 0.5, true);
                SkewedGaussWaveform skewed = new SkewedGaussWaveform(skewedSigma,skew,0.5,true);
                
                assertEquals(norm.area(),skewed.area(),norm.area()*0.05);
            }
        }
    }
    
    double findSkewedSigmaMatchingAread(double skew, double symSigma) {
    
        GaussianWaveform norm = new GaussianWaveform(symSigma, 0.5, true);
        double area = norm.area();
        double E = 0.01 * area;
        
        double lSigma = symSigma;
        
        {
            SkewedGaussWaveform skewed = new SkewedGaussWaveform(lSigma,skew,0.5,true);
            assertTrue(skewed.area() < area);
        }
        
        double hSigma = symSigma;
        
        {
            SkewedGaussWaveform skewed = new SkewedGaussWaveform(hSigma,skew,0.5,true);
            
            for (int i = 0; i < 100; i++) {
                skewed = new SkewedGaussWaveform(hSigma,skew,0.5,true);
                if (skewed.area() > area) break;
                hSigma = 2 * hSigma;
            }
            assertTrue(skewed.area() > area);
        }
        
        double sigma = (lSigma+hSigma)/2;
        
        for (int i = 0; i < 100; i++) {
            SkewedGaussWaveform skewed = new SkewedGaussWaveform(sigma,skew,0.5,true);
            
            if (skewed.area() < area - E) {
                lSigma = sigma;
            } else if (skewed.area() > area - E) {
                hSigma = sigma;
            } else {
                break;
            };
            sigma = (lSigma+hSigma)/2;
            
        }
        
        return lSigma;
    }
    
    @Test
    public void findSigmaForStartsPoints() {
        
        double[] starts = {0, 1.0/6, 0.25, 3.0/8};

        double zerolike = 0.01;
        double nonzero = 0.1;
        
        double[] sigmas = new double[starts.length];
        for (int i =0; i< starts.length; i++) {
            sigmas[i] = findSigmaForStartPoint(starts[i], nonzero, zerolike);
        }
        
        System.out.println(Arrays.toString(starts));
        System.out.println(Arrays.toString(sigmas));
        
        for (int i =1; i< starts.length; i++) {
            assertTrue(sigmas[i] < sigmas[i-1]);
        }
        
        /*
        GaussianWaveform wave = new GaussianWaveform(sigma, 0.5, false);
        System.out.println("Found: "+sigma+"\t"+wave.value(start)+"\t"+wave.value(0));
        
        assertTrue(wave.value(start) >= (nonzero-E));
        assertTrue(wave.value(0) <= zerolike);
        */
    }
    
    public double findSigmaForStartPoint(double start, double nonzero, double zerolike) {
        
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
            //System.out.println("S: "+sigma+"\t"+wave.value(start)+"\t"+wave.value(0));
            if (sW >= (nonzero-E) && sW <= (nonzero+E)) {
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
                throw new IllegalStateException("Should not get here");
                /*
                //double step = FastMath.min(prevH - sigma, sigma - prevS) / 2;
                prevH = sigma;
                sigma = (sigma+prevS) / 2;
                // nonzero ok but too high ends can we go lower   
                if (sigma == prevS) {
                    break;
                }*/               
            }

            
            
        }
       

        
        GaussianWaveform wave = new GaussianWaveform(sigma, 0.5, false);
        System.out.println("Found: "+sigma+"\t"+wave.value(start)+"\t"+wave.value(0));
        
        // assertTrue(wave.value(start) >= (nonzero-E));
        // assertTrue(wave.value(0) <= zerolike);
        return sigma;
    }
    
}
