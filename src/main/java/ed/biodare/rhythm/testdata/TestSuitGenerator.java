/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import static ed.biodare.rhythm.testdata.waveforms.Waveforms.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Skew.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    
    
    public DataSet generateDataSet(int durationHours, int intervalMinutes, 
            Shape[] shapes, Skew[] skews,
            double[] periods, double[] circadianPhases, double[] noiseLevels,
            int replicates) {
        
        
        double[] times = roundToMil(makeTimes(durationHours, intervalMinutes));
        
        DataSet set = new DataSet();
        set.durationHours = durationHours;
        set.intervalInMinutes = intervalMinutes;
        set.times = times;
        set.addEntries(
                generateEntries(times, shapes, skews, 
                    periods, circadianPhases, noiseLevels, replicates));
        
        return set;
    }
    
    public List<DataEntry> generateEntries(double[] times, 
            Shape[] shapes, Skew[] skews,
            double[] periods, double[] circadianPhases, double[] noiseLevels,
            int replicates) {
        
        
        double amplitude = 5;
        
        List<DataEntry> entries = new ArrayList<>();
        
        for (double period: periods) {
            for (double circadianPhase: circadianPhases) {
                for (double noiseLevel: noiseLevels) {
                    for (Shape shape: shapes) {
                        for (Skew skew: skews) {
                            if (shape.equals(COS) && !skew.equals(NONE)) continue;
                            
                            double[][] datas = generateSeriesSet(replicates, times, 
                                    shape, skew, period, circadianPhase, amplitude, noiseLevel);
                            
                            DataDescription description = new DataDescription();
                            description.amplitude = amplitude;
                            description.mean = 10;
                            description.noiseLevel = noiseLevel;
                            description.period = period;
                            description.phase = circadianPhase;
                            description.rhythmic = true;
                            description.shape = shape;
                            description.skew = skew;
                            
                            for (double[] values : datas) {
                                DataEntry entry = new DataEntry(description, roundToMil(values));
                                entries.add(entry);
                            }
                        }
                    }
                }
            }
        }
        return entries;
    } 
    
    public DataSet generateNoiseSet(int durationHours, int intervalMinutes, 
            int replicates) {
        
        
        double[] times = roundToMil(makeTimes(durationHours, intervalMinutes));
        
        DataSet set = new DataSet();
        set.durationHours = durationHours;
        set.intervalInMinutes = intervalMinutes;
        set.times = times;
        set.addEntries(
                generateNoiseEntries(times, replicates));
        
        return set;
    } 
    
    public List<DataEntry> generateNoiseEntries(double[] times, 
            int replicates) {
        
        
        List<DataEntry> entries = new ArrayList<>();
                            
        double[][] datas = generateNoise(replicates, times);

        DataDescription description = new DataDescription();
        description.rhythmic = false;

        for (double[] values : datas) {
            DataEntry entry = new DataEntry(description, roundToMil(values));
            entries.add(entry);
        }
        return entries;
    }     
    
    public void saveForJava(DataSet set, Path file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(set);
        }
    }
    
    public DataSet readFromJava(Path file) throws IOException {
        try (ObjectInputStream out = new ObjectInputStream(Files.newInputStream(file))) {
            return (DataSet)out.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void saveForTxt(DataSet set, Path file, boolean withDescription, String SEP) throws IOException {
        
        List<List<String>> table = set.toTable(withDescription);
        
        List<String> lines = table.stream()
                                    .map ( l -> l.stream().collect(Collectors.joining(SEP)))
                                    .collect(Collectors.toList());
        
        Files.write(file, lines);
        
    }
    
  
    /*
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
                
                double[][] datas = generateSeriesSet(N, times, shape, skew, period, phase, amplitude, noiseLevel);
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
                    double[][] datas = generateSeriesSet(N, times, shape, skew, period, phase, amplitude, noiseLevel);
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
        
    }*/
    
}
