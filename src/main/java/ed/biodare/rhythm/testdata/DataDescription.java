/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape.COS;
import ed.biodare.rhythm.testdata.waveforms.Waveforms.Skew;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Skew.NONE;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author tzielins
 */
public class DataDescription implements Serializable, Cloneable {
    
    static final long serialVersionUID = 42L;

    
    public boolean rhythmic;
    public double period;
    public double phase;
    public double amplitude;
    public double noiseLevel;
    public double mean;
    public Shape shape = COS;
    public Skew skew = NONE;
    public double intervalInMinutes;
    public double durationHours;

    @Override
    public DataDescription clone() {
        try {
            return (DataDescription) super.clone(); 
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static List<String> makeHeader() {
        return Arrays.asList("rhythmic","period","phase","amplitude","mean",
                "noiseLevel","shape","skew");
    }

    public List<String> toRow() {
        return Arrays.asList(
                Boolean.toString(rhythmic),
                Double.toString(period),
                Double.toString(phase),
                Double.toString(amplitude),
                Double.toString(mean),
                Double.toString(noiseLevel),
                shape.toString(),
                skew.toString()
        );
    }
    
    
}
