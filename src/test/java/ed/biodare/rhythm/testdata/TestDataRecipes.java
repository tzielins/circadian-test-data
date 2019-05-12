/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import static ed.biodare.rhythm.testdata.waveforms.Waveforms.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Skew.NONE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author tzielins
 */
public class TestDataRecipes {
    
    double EPS = 1E-6;
    
    @Rule
    public TemporaryFolder testFolder= new TemporaryFolder();    
    
    Path outRoot = Paths.get("E:\\Temp\\Rhythmicity_tests");
    TestSuitGenerator generator;
    Path mainOutDir;
    
    public TestDataRecipes() {
    }
    
    @Before
    public void setUp() {
        
        generator = new TestSuitGenerator();        
        mainOutDir = outRoot.resolve(LocalDate.now().toString());
        
    }

    /**
     * Data Set for testing python vs java implementation.
     * 2 days of data sampled every two and one hour,
     * 24h periods, various phases, noises 0.5 and 0.25
     */
    @Test
    public void recipePythonVSJava() throws Exception {
        
        Path outDir = mainOutDir.resolve("python_comp");
        Files.createDirectories(outDir);
        
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
    
    
}
