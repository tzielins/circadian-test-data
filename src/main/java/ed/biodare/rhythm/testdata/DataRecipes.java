/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import static ed.biodare.rhythm.testdata.waveforms.Waveforms.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Skew.NONE;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 *
 * @author tzielins
 */
public class DataRecipes {
    
    
    public static void main(String [] args) {
    
        Path outRoot = Paths.get("E:\\Temp\\Rhythmicity_tests");
        Path mainOutDir = outRoot.resolve(LocalDate.now().toString());
        
        try {
            //recipePythonVSJava(mainOutDir.resolve("python_comp"));
            //recipePeriodsSpreadWithSampling(mainOutDir.resolve("period_spread"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
    
    
    /**
     * Data Set for testing python vs java implementation.
     * 2 days of data sampled every two and one hour,
     * 24h periods, various phases, noises 0.5 and 0.25
     */
    public static void recipePythonVSJava(Path outDir) throws IOException {
        
        if (!Files.exists(outDir))
            Files.createDirectories(outDir);
        
        TestSuitGenerator generator = new TestSuitGenerator();
        
        int durationHours = 48;
        int[] intervals = {60, 120};
        
        Shape[] shapes = {COS, WIDE_PEAK};
        Skew[] skews = {NONE, Skew.MID};
        
        double[] periods = {24};
        
        double[] circadianPhases = IntStream.range(0, 20)
                                        .mapToDouble( i -> i*1.5)
                                        .filter( p -> p < 24).toArray();
        double[] noiseLevels = {0.25, 0.5};
        int replicates = 20;
        
        for (int interval: intervals) {
            for (Shape shape: shapes) {
                for (Skew skew: skews) {
                    for (double noiseLevel: noiseLevels) {
                    
                        DataSet set = generator.generateDataSet(durationHours, interval, 
                            new Shape[]{shape}, new Skew[]{skew}, 
                            periods, circadianPhases, 
                            new double[]{noiseLevel}, replicates);
                        
                        if (set.entries.isEmpty()) continue;
                        
                        String name = interval+"_NL_"+noiseLevel+"_"+shape+"_"+skew+"_"+Arrays.toString(periods);
                        
                        Path file = outDir.resolve(name+".ser");
                        generator.saveForJava(set, file);
                        
                        file = outDir.resolve(name+".txt");
                        generator.saveForTxt(set, file, false,"\t");
                        
                    }
                }
            }
            
            DataSet noise = generator.generateNoiseSet(durationHours, interval, 1000);
            String name = interval+"_noise";

            Path file = outDir.resolve(name+".ser");
            generator.saveForJava(noise, file);

            file = outDir.resolve(name+".txt");
            generator.saveForTxt(noise, file, false,"\t");
        }
    }
    

    /**
     * Data Set for testing python vs java implementation.
     * 2 days of data sampled every one, two, fours hours,
     * 18-34h periods (every 1.5), various phases (circadian every 1.5), 
     * noises 0.5, 0.25, 0.1
     */
    public static void recipePeriodsSpreadWithSampling(Path suitDir) throws IOException {
        
        if (!Files.exists(suitDir))
            Files.createDirectories(suitDir);
        
        TestSuitGenerator generator = new TestSuitGenerator();
        
        int durationHours = 48;
        int[] intervals = {60, 120, 240};
        
        Shape[] shapes = {COS, WIDE_PEAK, ONE_THIRD_PEAK, HALF_PEAK, QUARTER_PEAK };
        Skew[] skews = {NONE, Skew.MID, Skew.HIGH};
        
        double[] periods = IntStream.range(0, 20)
                                        .mapToDouble( i -> 18+i*1.5)
                                        .filter( p -> p >= 18 && p <= 34)
                                        .toArray();
        
        double[] circadianPhases = IntStream.range(0, 20)
                                        .mapToDouble( i -> i*1.5)
                                        .filter( p -> p < 24).toArray();
        
        double[] noiseLevels = {0.1, 0.25, 0.5};
        int replicates = 20;
        
        for (int interval: intervals) {
            Path outDir = suitDir.resolve(""+interval);
            Files.createDirectories(outDir);
            
            for (double noiseLevel: noiseLevels) {
                
                int perNoiseSeries = 0;
                for (Shape shape: shapes) {
                    for (Skew skew: skews) {

                            DataSet set = generator.generateDataSet(durationHours, interval, 
                                new Shape[]{shape}, new Skew[]{skew}, 
                                periods, circadianPhases, 
                                new double[]{noiseLevel}, replicates);

                            if (set.entries.isEmpty()) continue;
                            perNoiseSeries+= set.entries.size();

                            String name = interval+"_NL_"+noiseLevel+"_"+shape+"_"+skew+"_18-34";

                            Path file = outDir.resolve(name+".ser");
                            generator.saveForJava(set, file);

                            file = outDir.resolve(name+".txt");
                            generator.saveForTxt(set, file, false,"\t");

                    }
                }
                
                DataSet noise = generator.generateNoiseSet(durationHours, interval, perNoiseSeries);
                //set noise level for aggregating results (50:50 noise to data)
                noise.entries.get(0).description.noiseLevel = noiseLevel;
                String name = interval+"_NL_"+noiseLevel+"_noise";

                Path file = outDir.resolve(name+".ser");
                generator.saveForJava(noise, file);

                file = outDir.resolve(name+".txt");
                generator.saveForTxt(noise, file, false,"\t");
                
                // make another noise data for ration (5: 1 noise to data)
                // so 4 * series as there is already 1 noise per data
                noise = generator.generateNoiseSet(durationHours, interval, perNoiseSeries*4);
                //set noise level for aggregating results
                noise.entries.get(0).description.noiseLevel = -noiseLevel;
                name = interval+"_NL_"+noiseLevel+"_larger_noise";

                file = outDir.resolve(name+".ser");
                generator.saveForJava(noise, file);

                file = outDir.resolve(name+".txt");
                generator.saveForTxt(noise, file, false,"\t");
            }
        }
    }
    
}
