/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata.waveforms;

import ed.biodare.rhythm.testdata.waveforms.WrappingWaveform;
import ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Skew.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class WaveformsTest {
    
    double EPS = 1E-6;
    public WaveformsTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testWaveforms() {
        
         
        double phase = 0;
        
        List<Double> xs = IntStream.range(0, 100).mapToDouble(s -> (s/100.0)).boxed().collect(Collectors.toList());
        
        System.out.println(xs);
        
        for (Shape shape : Shape.values()) {
            
            
            System.out.println("\n#"+shape);
            System.out.println("xt="+xs);
            double area = waveform(shape, NONE, phase).area();
            for (Skew skew: Skew.values()) {
                if (shape.equals(COS) && !skew.equals(NONE)) continue;
                
                WrappingWaveform wave = waveform(shape, skew, phase);
                List<Double> ys = xs.stream().map( x -> wave.value(x)).collect(Collectors.toList());
                System.out.println("yt="+ys);
                System.out.println("plt.plot(xt, yt)");
                assertEquals("Wrong peak"+shape+":"+skew,1,ys.get((int)(phase*100)),EPS);
                assertEquals("Wrong aread"+shape+":"+skew,area,wave.area(),0.01*area);
            }
        }
    }


    /**
     * Test of makeTimes method, of class Waveforms.
     */
    @Test
    public void testMakeTimes() {
        int durationHours = 1;
        int stepInMinutes = 60;
        double[] expResult = {0.0, 1.0};
        double[] result = makeTimes(durationHours, stepInMinutes);
        assertArrayEquals(expResult, result, EPS);
        
        
        durationHours = 1;
        stepInMinutes = 15;
        expResult = new double[]{0.0, 0.25, 0.5, 0.75, 1.0};
        result = makeTimes(durationHours, stepInMinutes);
        assertArrayEquals(expResult, result, EPS);
        
        durationHours = 24;
        stepInMinutes = 240;
        expResult = new double[]{0.0, 4., 8., 12., 16.0, 20, 24};
        result = makeTimes(durationHours, stepInMinutes);
        assertArrayEquals(expResult, result, EPS);
    }
        
    @Test
    public void calculateSeedDependsOnPattern() {
        double[] pattern = {1, 2};
        double factor = 0.5;
        
        int seed = calculateSeed(pattern, factor);
        assertEquals(seed, calculateSeed(pattern, factor));
        
        assertNotEquals(seed, calculateSeed(pattern, factor+3));
        pattern[0]+=1;
        assertNotEquals(seed, calculateSeed(pattern, factor));
        
    }
    
    @Test
    public void addNoiseCreatesNewNoisyData() {
        double[] org = {1,2,3};
        double[] pattern = Arrays.copyOf(org, org.length);
        
        NormalDistribution dis1 = new NormalDistribution(new Well19937c(1),0,1);
        NormalDistribution dis2 = new NormalDistribution(new Well19937c(1),0,1);
        
        double[] res = addNoise(pattern, dis2);
        assertArrayEquals(org, pattern, EPS);
        assertEquals(org.length, res.length);
        assertNotEquals(pattern[0], res[0], EPS);
        assertEquals(1+dis1.sample(), res[0], EPS);
    }
    
    @Test
    public void multiplyWithNoiseMakesNewSetWithNoise() {
        
        int size = 2;
        double[] pattern = {1, 2, 3};
        double level = 0.5;
        
        double[][] set = multiplyWithNoise(size, pattern, level);
        assertEquals(2,set.length);
        assertEquals(1, pattern[0],EPS);
        assertNotEquals(1.0, set[0][0],EPS);
        assertNotEquals(1.0, set[1][0],EPS);
        assertNotEquals(set[0][0], set[1][0],EPS);
        
                
    }
    
    @Test
    public void generateSeriesMakesASignal() {
    
        double[] times = {0, 13, 26, 39};
        Shape shape = COS;
        Skew skew = NONE;
        double period = 26;
        double ciracdianPhase = 12;
        double amplitude = 1;

        double[] exp = {9.5, 10.5, 9.5, 10.5};
        double[] res = generateSerie(times, shape, skew, period, ciracdianPhase, amplitude);
        assertArrayEquals(exp, res, EPS);
    }
    
    @Test
    public void generatesDataSet() {
        
        int size = 3;
        double[] times = {14, 40};
        Shape shape = WIDE_PEAK;
        Skew skew = HIGH;
        
        double period = 28;
        double ciracdianPhase = 0;
        double amplitude = 2;
        double noiseLevel = 0.1;
        
        double[][] set =generateDataSet(size, times, shape, skew, period, 
                ciracdianPhase, amplitude, noiseLevel);
        
        assertEquals(3, set.length);
        assertEquals(2, set[1].length);
        assertNotEquals(set[0][0],set[1][0], EPS);
        assertTrue(set[2][0] > 9.5);
    }
    
    
}
