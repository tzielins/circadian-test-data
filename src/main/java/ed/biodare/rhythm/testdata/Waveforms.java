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
                    case NONE: return new GaussianWaveform(0.234375, phase, false);
                    case LOW: return new SkewedGaussWaveform(0.6661345623320886, skew.skew, phase, true);
                    case MID: return new SkewedGaussWaveform(0.574048375875018, skew.skew, phase, true);
                    case HIGH: return new SkewedGaussWaveform(0.6232796422294266, skew.skew, phase, true);
                    default: throw new IllegalArgumentException("Unsupported skew: "+skew+" for shape: "+shape);
                }
            }
            
            case ONE_THIRD_PEAK: {
                switch (skew) {
                    case NONE: return new GaussianWaveform(0.15625, phase, false);
                    case LOW: return new SkewedGaussWaveform(0.644915498934091, skew.skew, phase, true);
                    case MID: return new SkewedGaussWaveform(0.4191873791721749, skew.skew, phase, true);
                    case HIGH: return new SkewedGaussWaveform(0.4956415140024404, skew.skew, phase, true);
                    default: throw new IllegalArgumentException("Unsupported skew: "+skew+" for shape: "+shape);
                }
            }
            
            case HALF_PEAK: {
                switch (skew) {
                    case NONE: return new GaussianWaveform(0.1171875, phase, true);
                    case LOW: return new SkewedGaussWaveform(0.12824722660377758, skew.skew, phase, true);
                    case MID: return new SkewedGaussWaveform(0.1889432870093771, skew.skew, phase, true);
                    case HIGH: return new SkewedGaussWaveform(0.36060210145279536, skew.skew, phase, true);
                    default: throw new IllegalArgumentException("Unsupported skew: "+skew+" for shape: "+shape);
                }
            }
            
            case QUARTER_PEAK: {
                switch (skew) {
                    case NONE: return new GaussianWaveform(0.05859375, phase, true);
                    case LOW: return new SkewedGaussWaveform(0.06318920294194973, skew.skew, phase, true);
                    case MID: return new SkewedGaussWaveform(0.08171888088089879, skew.skew, phase, true);
                    case HIGH: return new SkewedGaussWaveform(0.13647380017384064, skew.skew, phase, true);
                    default: throw new IllegalArgumentException("Unsupported skew: "+skew+" for shape: "+shape);
                }
            }            
            default: throw new IllegalArgumentException("Unsupported shape: "+shape);
        }
    }
}
