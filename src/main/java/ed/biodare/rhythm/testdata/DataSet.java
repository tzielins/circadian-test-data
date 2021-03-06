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

/**
 *
 * @author tzielins
 */
public class DataSet implements Serializable, Cloneable {
    
    static final long serialVersionUID = 42L;
    
    public String name;
    public String description;
    public double durationHours;
    public double intervalInMinutes;
    
    public double[] times;
    public List<DataEntry> entries = new ArrayList<>();

    @Override
    public DataSet clone()  {
        try {
            return (DataSet)super.clone(); 
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    public void add(DataEntry entry) {
        add(entry, true, true);
    }

    public void addEntries(Collection<DataEntry> entries) {
        entries.forEach( entry -> {
            add(entry, true, true);
        });
    }
    
    public void add(DataEntry entry,boolean setId, boolean setTimeCharacter) {
        if (setId) {
            entry.id = entries.size()+1;
        }
        if (setTimeCharacter) {
            entry.description.intervalInMinutes = intervalInMinutes;
            entry.description.durationHours = durationHours;
        }
        entries.add(entry);
    }
    
    public List<List<String>> toTable(boolean withDescription) {
        List<List<String>> rows = new ArrayList<>();
        
        rows.add(makeHeader(withDescription));
        entries.forEach( entry -> rows.add(entry.toRow(withDescription)));
        
        return rows;
    }

    List<String> makeHeader(boolean withDescription) {
        List<String> row = new ArrayList<>();
        row.addAll(DataEntry.makeHeader(withDescription));
        for (double time: times) {
            row.add(Double.toString(time));
        }
        
        return row;
    }
}
