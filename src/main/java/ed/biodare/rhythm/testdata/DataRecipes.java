/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;


import ed.biodare.rhythm.testdata.waveforms.Waveforms;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Skew.NONE;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 *
 * @author tzielins
 */
public class DataRecipes {
    
    static int REPLICATES = 10;
    
    public static void main(String [] args) {
    
        Path outRoot = Paths.get("E:\\Temp\\Student");
        Path mainOutDir = outRoot.resolve(LocalDate.now().toString());
        
        try {

            recipeRNNTry(mainOutDir.resolve("rnn"));
            
            // for validating results in methods refactoring and jc2
            //recipeBDMethodsConsitency(mainOutDir.resolve("bd_consistency"));
            
            /* from eJTK tets
            // those one used to assess presets
            recipeCloseTo24PeriodsDownsampled(mainOutDir.resolve("closeTo24"));
            recipeShortPeriodsDownsampled(mainOutDir.resolve("short"));
            recipeLongPeriodsDownsampled(mainOutDir.resolve("long"));
            // manually copy short and long to non24
            
            recipeCloseTo24SpikeDownsampled(mainOutDir.resolve("spike24"));
            recipeShortSpikeDownsampled(mainOutDir.resolve("spikeShort"));
            // excluded long as not big difference in inital runs recipeLongSpikeDownsampled(mainOutDir.resolve("spikeLong"));
            recipeCloseTo24HighNoiseDownsampled(mainOutDir.resolve("closeTo24Noise035"),new double[]{0.35});
            // excluded higher noises as were not giving results recipeCloseTo24HighNoiseDownsampled(mainOutDir.resolve("closeTo24Noise075"),new double[]{0.75});
            // excluded higher noises as were not giving results recipeShortPeriodsHighNoiseDownsampled(mainOutDir.resolve("shortNoise05"),new double[]{0.5});
                        
            //recipePythonVSJava(mainOutDir.resolve("python_comp"));
            //recipePeriodsSpreadWithSampling(mainOutDir.resolve("period_spread"));
            //recipeClosePeriodsPhasesWithSampling(mainOutDir.resolve("period_resolution"));
            //recipeClosePeriodsPhasesDownsampled(mainOutDir.resolve("period_resolution_downsampled"));
            */
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
   
    
    /**
     * Data Set for testing resolution of phases and periods.
     * 2 days of data sampled every one, two, fours hours,
     * Periods 23, 23.5, 24, 24.5, 25, 26
     * Phases 0, 1, 2, 3, 4, 5  non-circadian
     * noises 0.5, 0.25, 0.1
     */
    public static void recipeClosePeriodsPhasesWithSampling(Path suitDir) throws IOException {
        
        if (!Files.exists(suitDir))
            Files.createDirectories(suitDir);
        
        TestSuitGenerator generator = new TestSuitGenerator();
        
        int durationHours = 48;
        int[] intervals = {60, 120, 240};
        
        Shape[] shapes = {COS, WIDE_PEAK, ONE_THIRD_PEAK, HALF_PEAK, QUARTER_PEAK };
        Skew[] skews = {NONE, Skew.MID, Skew.HIGH};
        
        double[] periods = {23, 23.5, 24, 24.5, 25, 26}; 
        
        double[] phases = {0, 1, 2, 3, 4, 5};
        
        double[] noiseLevels = {0.1, 0.25, 0.5};
        int replicates = 20;
        
        for (int interval: intervals) {
            Path outDir = suitDir.resolve(""+interval);
            Files.createDirectories(outDir);
            
            double[] times = roundToMil(makeTimes(durationHours, interval));
        
            
            for (double noiseLevel: noiseLevels) {
                
                int perNoiseSeries = 0;
                for (Shape shape: shapes) {
                    for (Skew skew: skews) {

                        DataSet joined = new DataSet();
                        joined.durationHours = durationHours;
                        joined.intervalInMinutes = interval;
                        joined.times = times;
                        
                        for (double period: periods) {
                            
                            double[] circadianPhases = Arrays.stream(phases).map( p -> (p*24)/period).toArray();
                            
                            joined.addEntries(
                                    generator.generateEntries(times, new Shape[]{shape}, new Skew[]{skew}, 
                                            new double[]{period}, 
                                            circadianPhases, 
                                            new double[]{noiseLevel}, replicates
                                            ));
                        }
                        
                        if (joined.entries.isEmpty()) continue;
                        perNoiseSeries+= joined.entries.size();

                        String name = interval+"_NL_"+noiseLevel+"_"+shape+"_"+skew+"_18-34";

                        Path file = outDir.resolve(name+".ser");
                        generator.saveForJava(joined, file);

                        file = outDir.resolve(name+".txt");
                        generator.saveForTxt(joined, file, false,"\t");

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
 
    
    /**
     * Data Set for testing resolution of phases and periods.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 23, 23.5, 24, 24.5, 25, 26
     * Phases 0, 1, 2, 3, 4, 5  non-circadian
     * noises 0.25, 0.1
     * Then data cut to 3, 2, 1 day.
     * Two days and 1 day are also downsampled to 2 hours.
     * The series have consisted id over datasets.
     */
    public static void recipeClosePeriodsPhasesDownsampled(Path suitDir) throws IOException {
        
        if (!Files.exists(suitDir))
            Files.createDirectories(suitDir);
        
        TestSuitGenerator generator = new TestSuitGenerator();
        
        AtomicInteger ids = new AtomicInteger();
        int durationHours = 24*4;
        int interval = 60;
        
        Shape[] shapes = {COS, WIDE_PEAK, ONE_THIRD_PEAK, HALF_PEAK, QUARTER_PEAK };
        Skew[] skews = {NONE, Skew.MID, Skew.HIGH};
        
        double[] periods = {23, 23.5, 24, 24.5, 25, 26}; 
        
        double[] phases = {0, 1, 2, 3, 4, 5};
        
        double[] noiseLevels = {0.1, 0.25};
        int replicates = 20;
        
        Path suitPattern;
        // generation of main data
        {
            Path outDir = suitDir.resolve("60");
            suitPattern = outDir;
            Files.createDirectories(outDir);
            
            double[] times = roundToMil(makeTimes(durationHours, interval));
        
            
            for (double noiseLevel: noiseLevels) {
                
                int perNoiseSeries = 0;
                for (Shape shape: shapes) {
                    for (Skew skew: skews) {

                        DataSet joined = new DataSet();
                        joined.durationHours = durationHours;
                        joined.intervalInMinutes = interval;
                        joined.times = times;
                        
                        for (double period: periods) {
                            
                            double[] circadianPhases = Arrays.stream(phases).map( p -> (p*24)/period).toArray();
                            
                            joined.addEntries(
                                    generator.generateEntries(times, new Shape[]{shape}, new Skew[]{skew}, 
                                            new double[]{period}, 
                                            circadianPhases, 
                                            new double[]{noiseLevel}, replicates
                                            ));
                        }
                        
                        if (joined.entries.isEmpty()) continue;
                        
                        joined.entries.forEach( e -> e.id = ids.getAndIncrement());
                        perNoiseSeries+= joined.entries.size();

                        String name = interval+"_NL_"+noiseLevel+"_"+shape+"_"+skew+"_23-26";

                        Path file = outDir.resolve(name+".ser");
                        generator.saveForJava(joined, file);

                        file = outDir.resolve(name+".txt");
                        generator.saveForTxt(joined, file, false,"\t");

                    }
                }
                
                DataSet noise = generator.generateNoiseSet(durationHours, interval, perNoiseSeries);
                //set noise level for aggregating results (50:50 noise to data)
                noise.entries.get(0).description.noiseLevel = noiseLevel;
                noise.entries.forEach( e -> e.id = ids.getAndIncrement());
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
                noise.entries.forEach( e -> e.id = ids.getAndIncrement());
                
                name = interval+"_NL_"+noiseLevel+"_larger_noise";

                file = outDir.resolve(name+".ser");
                generator.saveForJava(noise, file);

                file = outDir.resolve(name+".txt");
                generator.saveForTxt(noise, file, false,"\t");
            }
        }
        
        List<Path> patterns = new ArrayList<>();
        patterns.add(suitPattern);
        
        //downsampling
        {
            Path outDir = suitDir.resolve("120_0");
            Files.createDirectories(outDir);
            downSampleDir(suitPattern, 2, outDir, 0);
            patterns.add(outDir);
            
            outDir = suitDir.resolve("120_1");
            Files.createDirectories(outDir);
            downSampleDir(suitPattern, 2, outDir, 1);
            patterns.add(outDir);        
        }
        
        for (Path dir : patterns) {
            trimSeries(dir, List.of(24, 48, 72), "", suitDir);            
        }
        
        
    }

    
    /**
     * Data Set for testing data with close to 24 periods and few phases.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 23, 24, 25
     * Phases 0, 1, 2, 3, 4, 8, 12  non-circadian
     * noises 0.25, 0.1
     * Then data cut to 3, 2, 1 day.
     * Days are also downsampled to 2 and 4 hours.
     * The series have consisted id over datasets.
     * Shapes are limitted to "easy" ones, relatively wide and only low and mid assymetry
     */
    public static void recipeVeryCloseTo24PeriodsDownsampled(Path suitDir) throws IOException {
        
        if (!Files.exists(suitDir))
            Files.createDirectories(suitDir);
        
        Shape[] shapes = {WIDE_PEAK, ONE_THIRD_PEAK, HALF_PEAK };
        Skew[] skews = {Skew.LOW, Skew.MID};
        
        double[] periods = {23, 24, 25}; 
        
        double[] phases = {0, 1, 2, 3, 4, 8, 12};
        
        double[] noiseLevels = {0.1, 0.25};

        
        recipeDownsampled(suitDir, shapes, skews, periods, phases, noiseLevels);
        
    }
    
    /**
     * Data Set for testing data with close to 24 periods and few phases.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 22, 23, 24, 25, 26
     * Phases 0, 1, 2, 3, 4, 8, 12  non-circadian
     * noises 0.25, 0.1
     * Then data cut to 3, 2, 1 day.
     * Days are also downsampled to 2 and 4 hours.
     * The series have consisted id over datasets.
     * Shapes are limitted to "easy" ones, relatively wide and only low and mid assymetry
     */
    public static void recipeCloseTo24PeriodsDownsampled(Path suitDir) throws IOException {
        
        if (!Files.exists(suitDir))
            Files.createDirectories(suitDir);
        
        Shape[] shapes = {WIDE_PEAK, ONE_THIRD_PEAK, HALF_PEAK };
        Skew[] skews = {Skew.LOW, Skew.MID};
        
        double[] periods = {22, 23, 24, 25, 26}; 
        
        double[] phases = {0, 1, 2, 3, 4, 8, 12};
        
        double[] noiseLevels = {0.1, 0.25};

        
        recipeDownsampled(suitDir, shapes, skews, periods, phases, noiseLevels);
        
    }
    
    
    /**
     * Data Set for testing data with short periods and few phases.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 17, 18, 19, 20, 21
     * Phases 0, 1, 2, 3, 4, 8, 12  non-circadian
     * noises 0.25, 0.1
     * Then data cut to 3, 2, 1 day.
     * Days are also downsampled to 2 and 4 hours.
     * The series have consisted id over datasets.
     * Shapes are limitted to "easy" ones, relatively wide and only low and mid assymetry
     */
    public static void recipeShortPeriodsDownsampled(Path suitDir) throws IOException {
        
        if (!Files.exists(suitDir))
            Files.createDirectories(suitDir);
        
        Shape[] shapes = {WIDE_PEAK, ONE_THIRD_PEAK, HALF_PEAK };
        Skew[] skews = {Skew.LOW, Skew.MID};
        
        double[] periods = {17, 18, 19, 20, 21}; 
        
        double[] phases = {0, 1, 2, 3, 4, 8, 12};
        
        double[] noiseLevels = {0.1, 0.25};
        
        recipeDownsampled(suitDir, shapes, skews, periods, phases, noiseLevels);
    }
    
    /**
     * Data Set for testing data with long periods and few phases.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 28, 30, 31, 33, 35
     * Phases 0, 1, 2, 3, 4, 8, 12  non-circadian
     * noises 0.25, 0.1
     * Then data cut to 3, 2, 1 day.
     * Days are also downsampled to 2 and 4 hours.
     * The series have consisted id over datasets.
     * Shapes are limitted to "easy" ones, relatively wide and only low and mid assymetry
     */
    public static void recipeLongPeriodsDownsampled(Path suitDir) throws IOException {
        
        if (!Files.exists(suitDir))
            Files.createDirectories(suitDir);
        
        Shape[] shapes = {WIDE_PEAK, ONE_THIRD_PEAK, HALF_PEAK };
        Skew[] skews = {Skew.LOW, Skew.MID};
        
        double[] periods = {28, 30, 31, 33, 35}; 
        
        double[] phases = {0, 1, 2, 3, 4, 8, 12};
        
        double[] noiseLevels = {0.1, 0.25};
        
        recipeDownsampled(suitDir, shapes, skews, periods, phases, noiseLevels);
    }
    
    
    /**
     * Data Set for testing spike data with close to 24 periods and few phases.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 22, 23, 24, 25, 26
     * Phases 0, 1, 2, 3, 4, 8, 12  non-circadian
     * noises 0.25, 0.1
     * Then data cut to 3, 2, 1 day.
     * Days are also downsampled to 2 and 4 hours.
     * The series have consisted id over datasets.
     * Shapes are limitted to "easy" ones, relatively wide and only low and mid assymetry
     */
    public static void recipeCloseTo24SpikeDownsampled(Path suitDir) throws IOException {
        
        
        Shape[] shapes = {QUARTER_PEAK };
        Skew[] skews = {Skew.NONE, Skew.LOW, Skew.MID};
        
        double[] periods = {22, 23, 24, 25, 26};         
        double[] phases = {0, 1, 2, 3, 4, 8, 12};
        
        double[] noiseLevels = {0.1, 0.25};
        
        recipeDownsampled(suitDir, shapes, skews, periods, phases, noiseLevels);
        
    }  
    
    /**
     * Data Set for testing spike data with short periods and few phases.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 17, 18, 19, 20, 21
     * Phases 0, 1, 2, 3, 4, 8, 12  non-circadian
     * noises 0.25, 0.1
     * Then data cut to 3, 2, 1 day.
     * Days are also downsampled to 2 and 4 hours.
     * The series have consisted id over datasets.
     * Shapes are limitted to "easy" ones, relatively wide and only low and mid assymetry
     */
    public static void recipeShortSpikeDownsampled(Path suitDir) throws IOException {
        
        
        Shape[] shapes = {QUARTER_PEAK };
        Skew[] skews = {Skew.NONE, Skew.LOW, Skew.MID};
        
        double[] periods = {17, 18, 19, 20, 21};         
        double[] phases = {0, 1, 2, 3, 4, 8, 12};
        
        double[] noiseLevels = {0.1, 0.25};
        
        recipeDownsampled(suitDir, shapes, skews, periods, phases, noiseLevels);
        
    }   
    
    /**
     * Data Set for testing spike data with long periods and few phases.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 28, 30, 31, 33, 35
     * Phases 0, 1, 2, 3, 4, 8, 12  non-circadian
     * noises 0.25, 0.1
     * Then data cut to 3, 2, 1 day.
     * Days are also downsampled to 2 and 4 hours.
     * The series have consisted id over datasets.
     * Shapes are limitted to "easy" ones, relatively wide and only low and mid assymetry
     */
    public static void recipeLongSpikeDownsampled(Path suitDir) throws IOException {
        
        
        Shape[] shapes = {QUARTER_PEAK };
        Skew[] skews = {Skew.NONE, Skew.LOW, Skew.MID};
        
        double[] periods = {28, 30, 31, 33, 35};         
        double[] phases = {0, 1, 2, 3, 4, 8, 12};
        
        double[] noiseLevels = {0.1, 0.25};
        
        recipeDownsampled(suitDir, shapes, skews, periods, phases, noiseLevels);
        
    }     
    
    /**
     * Data Set for testing high noise data with close to 24 periods and few phases.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 22, 23, 24, 25, 26
     * Phases 0, 1, 2, 3, 4, 8, 12  non-circadian
     * noises as given
     * Then data cut to 3, 2, 1 day.
     * Days are also downsampled to 2 and 4 hours.
     * The series have consisted id over datasets.
     * Shapes are limitted to "easy" ones, relatively wide and only low and mid assymetry
     */
    public static void recipeCloseTo24HighNoiseDownsampled(Path suitDir, double[] noiseLevels) throws IOException {
        
        
        Shape[] shapes = {WIDE_PEAK, ONE_THIRD_PEAK, HALF_PEAK };
        Skew[] skews = {Skew.LOW, Skew.MID};
        
        double[] periods = {22, 23, 24, 25, 26};         
        double[] phases = {0, 1, 2, 3, 4, 8, 12};

        recipeDownsampled(suitDir, shapes, skews, periods, phases, noiseLevels);
        
    }  
    
    /**
     * Data Set for testing high noise data with short periods and few phases.
     * Using trimming and down sampling.
     * 4 days of data sampled every one hours,
     * Periods 17, 18, 19, 20, 21
     * Phases 0, 1, 2, 3, 4, 8, 12  non-circadian
     * noises 0.25, 0.1
     * Then data cut to 3, 2, 1 day.
     * Days are also downsampled to 2 and 4 hours.
     * The series have consisted id over datasets.
     * Shapes are limitted to "easy" ones, relatively wide and only low and mid assymetry
     */
    public static void recipeShortPeriodsHighNoiseDownsampled(Path suitDir, double[] noiseLevels) throws IOException {
        
        
        Shape[] shapes = {WIDE_PEAK, ONE_THIRD_PEAK, HALF_PEAK };
        Skew[] skews = {Skew.LOW, Skew.MID};
        
        double[] periods = {17, 18, 19, 20, 21};         
        double[] phases = {0, 1, 2, 3, 4, 8, 12};

        recipeDownsampled(suitDir, shapes, skews, periods, phases, noiseLevels);
        
    }
    
    public static void recipeDownsampled(Path suitDir, Shape[] shapes, Skew[] skews, 
            double[] periods, double[] phases, double[] noiseLevels) throws IOException {
        
        if (!Files.exists(suitDir))
            Files.createDirectories(suitDir);
        
        TestSuitGenerator generator = new TestSuitGenerator();
        
        AtomicInteger ids = new AtomicInteger();
        int durationHours = 24*4;
        int interval = 60;
        
        int replicates = REPLICATES;
        int p1 = (int)periods[0];
        int p2 = (int)periods[periods.length-1];
        
        Path suitPattern;
        // generation of main data
        {
            Path outDir = suitDir.resolve("96_1");
            suitPattern = outDir;
            Files.createDirectories(outDir);
            
            double[] times = roundToMil(makeTimes(durationHours, interval));
        
            
            for (double noiseLevel: noiseLevels) {
                
                int perNoiseSeries = 0;
                for (Shape shape: shapes) {
                    for (Skew skew: skews) {

                        DataSet joined = new DataSet();
                        joined.durationHours = durationHours;
                        joined.intervalInMinutes = interval;
                        joined.times = times;
                        
                        for (double period: periods) {
                            
                            double[] circadianPhases = Arrays.stream(phases).map( p -> (p*24)/period).toArray();
                            
                            joined.addEntries(
                                    generator.generateEntries(times, new Shape[]{shape}, new Skew[]{skew}, 
                                            new double[]{period}, 
                                            circadianPhases, 
                                            new double[]{noiseLevel}, replicates
                                            ));
                        }
                        
                        if (joined.entries.isEmpty()) continue;
                        
                        joined.entries.forEach( e -> e.id = ids.getAndIncrement());
                        perNoiseSeries+= joined.entries.size();

                        String name = interval+"_NL_"+noiseLevel+"_"+shape+"_"+skew+"_"+p1+"-"+p2;

                        Path file = outDir.resolve(name+".ser");
                        generator.saveForJava(joined, file);

                        file = outDir.resolve(name+".txt");
                        generator.saveForTxt(joined, file, true,"\t");

                    }
                }
                
                DataSet noise = generator.generateNoiseSet(durationHours, interval, perNoiseSeries);
                //set noise level for aggregating results (50:50 noise to data)
                noise.entries.get(0).description.noiseLevel = noiseLevel;
                noise.entries.forEach( e -> e.id = ids.getAndIncrement());
                String name = interval+"_NL_"+noiseLevel+"_NOISE"+"_"+p1+"-"+p2;;

                Path file = outDir.resolve(name+".ser");
                generator.saveForJava(noise, file);

                file = outDir.resolve(name+".txt");
                generator.saveForTxt(noise, file, false,"\t");
                

            }
        }
        
        List<Path> patterns = new ArrayList<>();
        patterns.add(suitPattern);
        
        //downsampling
        {
            Path outDir = suitDir.resolve("96_2");
            Files.createDirectories(outDir);
            downSampleDir(suitPattern, 2, outDir, 0);
            patterns.add(outDir);
            
            /*
            outDir = suitDir.resolve("120_1");
            Files.createDirectories(outDir);
            downSampleDir(suitPattern, 2, outDir, 1);
            patterns.add(outDir);        
            */
            
            outDir = suitDir.resolve("96_4");
            Files.createDirectories(outDir);
            downSampleDir(suitPattern, 4, outDir, 0);
            patterns.add(outDir);             
            
        }
        
        for (Path dir : patterns) {
            trimSeries(dir, List.of(24, 48, 72), "", suitDir);            
        }
        
        
    }    
    
    static List<Path> trimSeries(Path suitPattern, List<Integer> durations, String suffix, Path suitDir) throws IOException {
        
        List<Path> dirs = new ArrayList<>();
        
        for (int duration: durations) {
            String name = suitPattern.getFileName().toString();
            name = name.contains("_") ? name.substring(name.indexOf("_")) : name;
            name = duration+name;
            Path outDir = suitDir.resolve(name+suffix);
            Files.createDirectories(outDir);
            trimSeries(suitPattern, duration, outDir);
            dirs.add(outDir);
        }
        
        return dirs;        
    }

    static void trimSeries(Path suitPattern, int duration, Path outDir) throws IOException {
        
        
        List<Path> files = Files.list(suitPattern)
                .filter( f -> f.getFileName().toString().endsWith(".ser"))
                .collect(Collectors.toList());
        
        for (Path file: files) {
            
            DataSet set = TestSuitGenerator.readFromJava(file);
            set = trimSet(set, duration);
            
            Path outFile = outDir.resolve(file.getFileName());
            TestSuitGenerator.saveForJava(set, outFile);
            
            outFile = outDir.resolve(file.getFileName().toString().replace(".ser", ".txt"));
            TestSuitGenerator.saveForTxt(set, outFile, true,"\t");
        }
        
    }

    static DataSet trimSet(DataSet set, int duration) {
        
        double last = duration+set.times[0];
        int size = 0;
        while (set.times[size] <= last) size++;
        
        
        set.times = Arrays.copyOf(set.times, size);
        set.durationHours = set.times[set.times.length-1];
        
        int size1 = size;
        set.entries.forEach( entry -> {
            entry.description.durationHours = set.durationHours;
            entry.values = Arrays.copyOf(entry.values, size1);
        });
        return set;
    }

    static void downSampleDir(Path patterns, int downSample, Path outDir, int offset) throws IOException {
        
        List<Path> files = Files.list(patterns)
                .filter( f -> f.getFileName().toString().endsWith(".ser"))
                .collect(Collectors.toList());
        
        for (Path file: files) {
            
            DataSet set = TestSuitGenerator.readFromJava(file);
            set = downsampleSet(set, downSample, offset);
            
            String fileName = file.getFileName().toString();
            fileName = fileName.substring(fileName.indexOf("_"));
            fileName = set.intervalInMinutes+fileName;
            
            Path outFile = outDir.resolve(fileName);
            TestSuitGenerator.saveForJava(set, outFile);
            
            outFile = outDir.resolve(outFile.getFileName().toString().replace(".ser", ".txt"));
            TestSuitGenerator.saveForTxt(set, outFile, true,"\t");
        }
    }

    static DataSet downsampleSet(DataSet set, int downSample, int offset) {
        
        
        set.times = downsampleArray(set.times, downSample, offset);
        set.durationHours = set.times[set.times.length-1]-set.times[0];
        set.intervalInMinutes = set.intervalInMinutes*downSample;
        
        set.entries.forEach( entry -> {
            entry.description.durationHours = set.durationHours;
            entry.description.intervalInMinutes = set.intervalInMinutes;
            entry.values = downsampleArray(entry.values, downSample, offset);
        
        });
        
        return set;
    }
    
    static double[] downsampleArray(double[] values, int downSample, int offset) {
        int size = 1+ (values.length-offset) / downSample;
        while ( (size*downSample+offset) > values.length) {
            size--;
        }
        double[] news = new double[size];
        for (int i =0; i< size; i++) {
            news[i] = values[offset+i*downSample];
        }
        
        return news;
        
    }

    static void recipeBDMethodsConsitency(Path outDir) throws Exception {
        
        if (!Files.exists(outDir))
            Files.createDirectories(outDir);
        
        TestSuitGenerator generator = new TestSuitGenerator();
        
        int[] durationHours = {48, 5*24};
        int[] intervals = {60};
        
        Shape[] shapes = {COS, ONE_THIRD_PEAK};
        Skew[] skews = {NONE};
        
        double[] periods = {24};
        
        double[] circadianPhases = {5};
        double[] noiseLevels = {0.1, 0.3};
        int replicates = 1;
        
        for (int duration: durationHours) {
        for (int interval: intervals) {
                DataSet set = generator.generateDataSet(duration, interval, 
                            shapes, skews, 
                            periods, circadianPhases, 
                            noiseLevels, replicates);
                        
                if (set.entries.isEmpty()) continue;            

                DataSet noise = generator.generateNoiseSet(duration, interval, replicates);
                set.addEntries(noise.entries);
                
                String name = duration+"_"+interval+"_NL_"+Arrays.toString(periods);
                        
                Path file = outDir.resolve(name+".ser");
                //generator.saveForJava(set, file);
                        
                file = outDir.resolve(name+".txt");
                generator.saveForTxt(set, file, true,"\t");
            
        }
        }
        
    }
 
    static void recipeRNNTry(Path outDir) throws Exception {
        
        if (!Files.exists(outDir))
            Files.createDirectories(outDir);
        
        TestSuitGenerator generator = new TestSuitGenerator();
        generator.DEFAULT_AMPLITUDE = 2;
        Waveforms.DEFAULT_MEAN = 0;
        
        int[] durationHours = {3*24};
        int[] intervals = {60};
        
        Shape[] shapes = {COS};
        Skew[] skews = {NONE};
        
        double[] periods = {24};
        
        double[] circadianPhases = {0};
        double[] noiseLevels = {0.1};
        double noiseSTD = 0.5;
        int replicates = 2;
        
        for (int duration: durationHours) {
        for (int interval: intervals) {
                DataSet set = generator.generateDataSet(duration, interval, 
                            shapes, skews, 
                            periods, circadianPhases, 
                            noiseLevels, replicates);
                        
                if (set.entries.isEmpty()) continue;            

                DataSet noise = generator.generateNoiseSet(duration, interval, replicates, noiseSTD);
                set.addEntries(noise.entries);
                
                String name = duration+"_"+interval+"_P_"+Arrays.toString(periods);
                        
                Path file = outDir.resolve(name+".ser");
                //generator.saveForJava(set, file);
                        
                file = outDir.resolve(name+".tsv");
                generator.saveForTxt(set, file, true,"\t");
            
        }
        }
        
    }
    
}
