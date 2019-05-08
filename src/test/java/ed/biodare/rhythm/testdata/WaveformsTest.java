/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import ed.biodare.rhythm.testdata.Waveforms.Shape;
import static ed.biodare.rhythm.testdata.Waveforms.Shape.*;
import static ed.biodare.rhythm.testdata.Waveforms.Skew.*;
import ed.biodare.rhythm.testdata.Waveforms.Skew;
import java.util.Arrays;
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
public class WaveformsTest {
    
    double EPS = 1E-6;
    public WaveformsTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testWaveforms() {
        
        Waveforms waves = new Waveforms();
        
        double phase = 0;
        
        List<Double> xs = IntStream.range(0, 100).mapToDouble(s -> (s/100.0)).boxed().collect(Collectors.toList());
        
        System.out.println(xs);
        
        for (Shape shape : Shape.values()) {
            
            
            System.out.println("\n#"+shape);
            System.out.println("xt="+xs);
            double area = waves.waveform(shape, NONE, phase).area();
            for (Skew skew: Skew.values()) {
                if (shape.equals(COS) && !skew.equals(NONE)) continue;
                
                WrappingWaveform wave = waves.waveform(shape, skew, phase);
                List<Double> ys = xs.stream().map( x -> wave.value(x)).collect(Collectors.toList());
                System.out.println("yt="+ys);
                System.out.println("plt.plot(xt, yt)");
                assertEquals("Wrong peak"+shape+":"+skew,1,ys.get((int)(phase*100)),EPS);
                assertEquals("Wrong aread"+shape+":"+skew,area,wave.area(),0.01*area);
            }
        }
    }
        
        
    
    
}
