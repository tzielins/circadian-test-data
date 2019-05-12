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
public class DataSet implements Serializable {
    
    static final long serialVersionUID = 42L;
    
    public String name;
    public String description;
    public double durationHours;
    public double intervalInMinutes;
    
    public double[] times;
    public List<DataEntry> entries = new ArrayList<>();
    
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
            entry.description.id = entries.size()+1;
        }
        if (setTimeCharacter) {
            entry.description.intervalInMinutes = intervalInMinutes;
            entry.description.durationHours = durationHours;
        }
        entries.add(entry);
    }
    
    public List<List<String>> toTable() {
        List<List<String>> rows = new ArrayList<>();
        
        rows.add(makeHeader());
        entries.forEach( entry -> rows.add(entry.toRow()));
        
        return rows;
    }

    List<String> makeHeader() {
        List<String> row = new ArrayList<>();
        row.addAll(DataEntry.makeHeader());
        for (double time: times) {
            row.add(Double.toString(time));
        }
        
        return row;
    }
}
