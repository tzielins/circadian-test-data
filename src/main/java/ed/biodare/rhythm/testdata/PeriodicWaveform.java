/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import org.apache.commons.math3.analysis.UnivariateFunction;


/**
 *
 * @author tzielins
 */
public class PeriodicWaveform implements UnivariateFunction {
    
    final double period;
    final double phase;
    final double amplitude;
    final double shift;
    final WrappingWaveform wave;
    
    public PeriodicWaveform(double period, double amplitude, WrappingWaveform wave) {
        this(period, amplitude, wave, 10);
    }
    
    PeriodicWaveform(double period, double amplitude, WrappingWaveform wave, double mean) {
        if (period <= 0) throw new IllegalArgumentException("Period must be possitive");
        if (amplitude <= 0) throw new IllegalArgumentException("Amplitude must be possitive");
        this.period = period;
        this.phase = wave.phase*period;
        this.amplitude = amplitude;
        this.wave = wave;
        this.shift = mean - amplitude*wave.mean();        
    }

    @Override
    public double value(double x) {
        
        x = (x % period) / period;
        return amplitude*wave.value(x) + shift;
    }
    
    public double[] values(double[] xs) {
        double[] values = new double[xs.length];
        for (int i =0;i<xs.length;i++) {
            values[i] = value(xs[i]);
        }
        return values;        
    }
    
    
}
