/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author tzielins
 */
public class DataSetTest {
    
    double EPS = 1E6;
    
    @Rule
    public TemporaryFolder testFolder= new TemporaryFolder();
  
    public DataSetTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of add method, of class DataSet.
     */
    @Test
    public void testAdd_DataEntry() {
        DataEntry entry = new DataEntry();
        DataSet instance = new DataSet();
        instance.add(entry);
        
        assertSame(entry, instance.entries.get(0));
        assertEquals(1, entry.id);
    }


    @Test
    public void canSerializeAndDeserialize() throws Exception {
        
        double[] times = {1, 2, 3};
        double[] v1 = {0, 1, 2};
        double[] v2 = {2, 1, 1};
        
        DataDescription desc = new DataDescription();
        desc.rhythmic = true;
        desc.period = 24;
        
        DataEntry entry = new DataEntry();
        entry.description = desc;
        entry.values = v1;
        
        DataSet set = new DataSet();
        set.name = "Test";
        set.times = times;
        set.add(entry);
        
        desc = new DataDescription();
        desc.rhythmic = false;        
        entry = new DataEntry();
        entry.description = desc;
        entry.values = v2;
        set.add(entry);
        
        Path file = testFolder.newFile().toPath();
        
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(set);
        }
        
        try (ObjectInputStream out = new ObjectInputStream(Files.newInputStream(file))) {
            DataSet set2 = (DataSet)out.readObject();
            
            assertNotNull(set2);
            assertEquals(set.name, set2.name);
            assertEquals(set.description, set2.description);
            assertArrayEquals(times, set2.times, EPS);
            assertEquals(2, set2.entries.size());
            
            assertEquals(24, set2.entries.get(0).description.period, EPS);
            assertArrayEquals(v1, set2.entries.get(0).values, EPS);
            assertEquals(0, set2.entries.get(1).description.period, EPS);
            assertArrayEquals(v2, set2.entries.get(1).values, EPS);
            
        }
        
    }
 
    
    @Test
    public void toTable() throws Exception {
        
        double[] times = {1, 2, 3};
        double[] v1 = {0, 1, 2};
        double[] v2 = {2, 1, 1};
        
        DataDescription desc = new DataDescription();
        desc.rhythmic = true;
        desc.period = 24;
        
        DataEntry entry = new DataEntry();
        entry.description = desc;
        entry.values = v1;
        
        DataSet set = new DataSet();
        set.name = "Test";
        set.times = times;
        set.add(entry);
        
        desc = new DataDescription();
        desc.rhythmic = false;        
        entry = new DataEntry();
        entry.description = desc;
        entry.values = v2;
        set.add(entry);
        
        List<List<String>> table = set.toTable(true);
        assertEquals(3, table.size());
        
        List<String> row = table.get(1);
        assertEquals("1",row.get(0));
        assertEquals("24.0",row.get(2));
        assertEquals("2.0",row.get(12));
    }
    
}
