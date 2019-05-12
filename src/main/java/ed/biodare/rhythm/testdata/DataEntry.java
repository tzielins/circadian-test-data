/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author tzielins
 */
public class DataEntry implements Serializable {
    
    static final long serialVersionUID = 42L;

    public int id;
    public DataDescription description;
    public double[] values;
    
    public DataEntry() {
        this(new DataDescription(),new double[0]);
    }
    
    public DataEntry(DataDescription description, double[] values) {
        Objects.requireNonNull(description);
        Objects.requireNonNull(values);
        
        this.description = description;
        this.values = values;
    }

    static List<String> makeHeader(boolean withDescription) {
        List<String> row = new ArrayList<>();
        row.add("id");
        if (withDescription) row.addAll(DataDescription.makeHeader());
        return row;
    }
    
    
    List<String> toRow(boolean withDescription) {
        List<String> row = new ArrayList<>();
        row.add(Integer.toString(id));
        if (withDescription) row.addAll(description.toRow());
        for (double value: values) {
            row.add(Double.toString(value));
        }
        return row;
    }
    
}
