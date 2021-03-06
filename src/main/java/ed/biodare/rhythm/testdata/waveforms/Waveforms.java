/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata.waveforms;

import java.util.Arrays;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.Well19937c;
import static org.apache.commons.math3.util.FastMath.*;
/**
 *
 * @author tzielins
 */
public class Waveforms {

    public static double DEFAULT_MEAN = 10;

    public enum Shape {
        COS,
        WIDE_PEAK,
        ONE_THIRD_PEAK,
        HALF_PEAK,
        QUARTER_PEAK,
        NOISE;
        
        public String shortName() {
            switch(this) {
                case COS: return "COS";
                case WIDE_PEAK: return "WIDE";
                case ONE_THIRD_PEAK: return "3rd";
                case HALF_PEAK: return "HALF";
                case QUARTER_PEAK: return "SPIKE";
                case NOISE: return "NOISE";
                default: return this.toString();
            }
        };
    }
    
    public enum Skew {
        NONE(0),
        LOW(0.4),
        MID(0.8),
        HIGH(1.2);
        
        Skew(double skew) {
            this.skew = skew;   
        }
        
        final public double skew;
    }
    
    public static WrappingWaveform waveform(Shape shape,Skew skew,double phase) {
        
        switch (shape) {
            case COS: {
                if (skew.equals(Skew.NONE))
                    return new CosineWaveform(phase);
                throw new IllegalArgumentException("Cossine shape cannot have skewness");
            }
            
            case WIDE_PEAK: {
                switch (skew) {
                    case NONE: return new GaussianWaveform(0.203125, phase, false);
                    /*
                    case LOW: return new SkewedGaussWaveform(0.21897347065651573, skew.skew, phase, false);
                    case MID: return new SkewedGaussWaveform(0.2784780878770199, skew.skew, phase, false);
                    case HIGH: return new SkewedGaussWaveform(0.415636603046377, skew.skew, phase, false);
                    */
                    case LOW: return new SkewedGaussWaveform(0.711034440272608, skew.skew, phase, true);
                    case MID: return new SkewedGaussWaveform(0.6700816291266387, skew.skew, phase, true);
                    case HIGH: return new SkewedGaussWaveform(.7442591767475941, skew.skew, phase, true);
                    default: throw new IllegalArgumentException("Unsupported skew: "+skew+" for shape: "+shape);
                }
            }
            
            case ONE_THIRD_PEAK: {
                switch (skew) {
                    case NONE: return new GaussianWaveform(0.140625, phase, false);
                    /*case LOW: return new SkewedGaussWaveform(0.1516067989805035, skew.skew, phase, false);
                    case MID: return new SkewedGaussWaveform(.19283827997555814, skew.skew, phase, false);
                    case HIGH: return new SkewedGaussWaveform(.2878768391594375, skew.skew, phase, false);*/
                    case LOW: return new SkewedGaussWaveform(0.16159421514938846, skew.skew, phase, true);
                    case MID: return new SkewedGaussWaveform(0.2890010832949674, skew.skew, phase, true);
                    case HIGH: return new SkewedGaussWaveform(0.4830298083311916, skew.skew, phase, true);
                    default: throw new IllegalArgumentException("Unsupported skew: "+skew+" for shape: "+shape);
                }
            }
            
            case HALF_PEAK: {
                switch (skew) {
                    case NONE: return new GaussianWaveform(0.1015625, phase, false);
                    /*case LOW: return new SkewedGaussWaveform(0.10950254478545297, skew.skew, phase, false);
                    case MID: return new SkewedGaussWaveform(0.13931330292050073, skew.skew, phase, false);
                    case HIGH: return new SkewedGaussWaveform(0.20802617814718816, skew.skew, phase, false);*/
                    case LOW: return new SkewedGaussWaveform(0.11016841333997066, skew.skew, phase, true);
                    case MID: return new SkewedGaussWaveform(0.1500836205989934, skew.skew, phase, true);
                    case HIGH: return new SkewedGaussWaveform(0.28198739813204843, skew.skew, phase, true);
                    default: throw new IllegalArgumentException("Unsupported skew: "+skew+" for shape: "+shape);
                }
            }
            
            case QUARTER_PEAK: {
                switch (skew) {
                    case NONE: return new GaussianWaveform(0.05078125, phase, false);
                    /*case LOW: return new SkewedGaussWaveform(0.05476715911828679, skew.skew, phase, false);
                    case MID: return new SkewedGaussWaveform(0.06973107359499559, skew.skew, phase, false);
                    case HIGH: return new SkewedGaussWaveform(0.10420928568117749, skew.skew, phase, false);*/
                    case LOW: return new SkewedGaussWaveform(0.054767394902696165, skew.skew, phase, true);
                    case MID: return new SkewedGaussWaveform(0.07037477639896425, skew.skew, phase, true);
                    case HIGH: return new SkewedGaussWaveform(0.11476673667898121, skew.skew, phase, true);
                    default: throw new IllegalArgumentException("Unsupported skew: "+skew+" for shape: "+shape);
                }
            }            
            default: throw new IllegalArgumentException("Unsupported shape: "+shape);
        }
    }
    
    public static double[] makeTimes(int durationHours, int intervalInMinutes) {
        
        int size = 1+(durationHours*60)/intervalInMinutes;
        double[] times = new double[size];
        
        for (int i =0;i<size;i++) {
            times[i] = rint((i*intervalInMinutes*100)/60.0)/100.0; // multiplied by 100 to havd a nice centy rounding
        }
        return times;
    }
    
    public static double[] generateSerie(double[] times, Shape shape, Skew skew, 
            double period, double ciracdianPhase, double amplitude) {
        
        if (amplitude < 1 || amplitude > 8) 
            throw new IllegalArgumentException("Supported amplitudes are (1,8) as mean is 10, requrested="+amplitude);
        
        double phase = ciracdianPhase/24.0;
        
        PeriodicWaveform wave = new PeriodicWaveform(period, amplitude, waveform(shape, skew, phase), DEFAULT_MEAN);

        return wave.values(times);
    }
    
    public static double[][] generateSeriesSet(int size, double[] times, Shape shape, Skew skew, 
            double period, double ciracdianPhase, double amplitude, double noiseLevel   ) {
        
        double noiseFactor = noiseLevel*amplitude;
        double[] pattern = generateSerie(times, shape, skew, period, ciracdianPhase, amplitude);
                        
        double[][] dataset = multiplyWithNoise(size, pattern, noiseFactor);
        return dataset;
        
    }
    
    public static double[] roundToMil(double[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = Math.rint(values[i]*1000)/1000;
        }
        return values;
    }
    
    public static double[][] generateNoise(int size, double[] times) {
        return generateNoise(size, times, 1);
    }
    
    public static double[][] generateNoise(int size, double[] times, double noiseSTD) {
        
        
        //This random generator is used with unparametrized NormalDistribution
        Well19937c random = new Well19937c(calculateSeed(times, noiseSTD));        
        NormalDistribution dis = new NormalDistribution(random, 0, noiseSTD);        
        
        double[] pattern = Arrays.copyOf(new double[0], times.length);
        
        double[][] dataset = multiplyWithNoise(size, pattern, dis);
        return dataset;
        
    } 
    
    static double[][] multiplyWithNoise(int size, double[] pattern, double noiseFactor) {
        
        //This random generator is used with unparametrized NormalDistribution
        Well19937c random = new Well19937c(calculateSeed(pattern, noiseFactor));        
        NormalDistribution dis = new NormalDistribution(random, 0, noiseFactor);
        
        return multiplyWithNoise(size, pattern, dis);
    }
    
    static double[][] multiplyWithNoise(int size, double[] pattern, NormalDistribution dis) {
        
        double[][] dataset = new double[size][];
        
        for (int i =0; i< size; i++) {
            dataset[i] = addNoise(pattern, dis);
        }
        return dataset;
    }    
    
    
    static int calculateSeed(double[] pattern, double noiseFactor) {
        int seed = Double.hashCode(noiseFactor);
        for (double d : pattern) seed+=Double.hashCode(d);
        return seed;
    }

    static double[] addNoise(double[] pattern, NormalDistribution dis) {
        double[] res = Arrays.copyOf(pattern, pattern.length);
        double[] noise = dis.sample(res.length);
        
        for (int i =0;i< res.length; i++) {
            res[i]+= noise[i];
        }
        return res;
    }

    
    
    
}
