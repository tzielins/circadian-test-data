/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import static ed.biodare.rhythm.testdata.waveforms.Waveforms.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Skew.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author tzielins
 */
public class TestSuitGenerator {
    
    
    public List<double[]> generateAllShapes() {
        
        double[] times = makeTimes(48, 120);
        int N = 3;
        double period = 24;
        double phase = 0;
        double amplitude = 5;
        double noiseLevel = 0.01;
        
        List<double[][]> sets = new ArrayList<>();
        
        for (Shape shape : Shape.values()) {
            
            for (Skew skew: Skew.values()) {
                if (shape.equals(COS) && !skew.equals(NONE)) continue;
                
                double[][] datas = generateDataSet(N, times, shape, skew, period, phase, amplitude, noiseLevel);
                sets.add(datas);
                System.out.println("#"+shape+":"+skew);
                System.out.println("plt.suptitle('"+shape+":"+skew+"')");
                System.out.println("x="+Arrays.toString(times));
                for (double[] data : datas) {
                    System.out.println("y="+Arrays.toString(data));
                    System.out.println("plt.plot(x,y)");

                }
                System.out.println("plt.show()");
                System.out.println("");
                
            }
        }
        
        List<double[]> datas = new ArrayList<>(sets.size()+1);
        datas.add(times);
        datas.addAll(   sets.stream()
                        .flatMap( dd -> Stream.of(dd))
                        .collect(Collectors.toList()));
        
        return datas;
        
    }
    
    public List<double[]> generateNoiseLevels() {
        
        double[] times = makeTimes(48, 60);
        int N = 1;
        double period = 24;
        double phase = 0;
        double amplitude = 5;
        double[] noiseLevels = {0.001,0.1,0.25,0.5};
        
        List<double[][]> sets = new ArrayList<>();
        
        for (Shape shape : Shape.values()) {
            
            for (Skew skew: Skew.values()) {
                if (shape.equals(COS) && !skew.equals(NONE)) continue;
                
                System.out.println("#"+shape+":"+skew);
                System.out.println("plt.suptitle('NOISES "+shape+":"+skew+"')");
                System.out.println("x="+Arrays.toString(times));
                for (double noiseLevel: noiseLevels) {
                    double[][] datas = generateDataSet(N, times, shape, skew, period, phase, amplitude, noiseLevel);
                    sets.add(datas);
                    for (double[] data : datas) {
                        System.out.println("y="+Arrays.toString(data));
                        System.out.println("plt.plot(x,y)");

                    }
                }
                System.out.println("plt.show()");
                System.out.println("");
                
            }
        }
        
        List<double[]> datas = new ArrayList<>(sets.size()+1);
        datas.add(times);
        datas.addAll(   sets.stream()
                        .flatMap( dd -> Stream.of(dd))
                        .collect(Collectors.toList()));
        
        return datas;
        
    }
    
}
