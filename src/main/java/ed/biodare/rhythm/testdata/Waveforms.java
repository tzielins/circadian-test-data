/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

/**
 *
 * @author tzielins
 */
public class Waveforms {
    
    public enum Shape {
        COS,
        WIDE_PEAK,
        ONE_THIRD_PEAK,
        HALF_PEAK,
        QUARTER_PEAK
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
    
    public WrappingWaveform waveform(Shape shape,Skew skew,double phase) {
        
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
                    case HIGH: return new SkewedGaussWaveform(0.1147667366789808, skew.skew, phase, true);
                    default: throw new IllegalArgumentException("Unsupported skew: "+skew+" for shape: "+shape);
                }
            }            
            default: throw new IllegalArgumentException("Unsupported shape: "+shape);
        }
    }
}
