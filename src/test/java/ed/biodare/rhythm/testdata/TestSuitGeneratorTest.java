/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import static ed.biodare.rhythm.testdata.waveforms.Waveforms.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Shape.*;
import static ed.biodare.rhythm.testdata.waveforms.Waveforms.Skew.NONE;
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
public class TestSuitGeneratorTest {
    
    double EPS = 1E-6;
    
    @Rule
    public TemporaryFolder testFolder= new TemporaryFolder();    
    
    public TestSuitGeneratorTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of generateAllShapes method, of class TestSuitGenerator.
     */
    //@Test
    public void testGenerateAllShapes() {
        TestSuitGenerator instance = new TestSuitGenerator();
        //instance.generateAllShapes();
        //instance.generateNoiseLevels();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateDataSet method, of class TestSuitGenerator.
     */
    @Test
    public void testGenerateDataSet() {
        int durationHours = 48;
        int intervalMinutes = 120;
        
        Shape[] shapes = {COS};
        Skew[] skews = {NONE};
        double[] periods = {24, 26};
        double[] circadianPhases = {12, 0};
        double[] noiseLevels = {0.5};
        int replicates = 1;
        
        TestSuitGenerator instance = new TestSuitGenerator();
        
        DataSet set = instance.generateDataSet(durationHours, intervalMinutes, shapes, skews, periods, circadianPhases, noiseLevels, replicates);
        
        assertEquals(48, set.durationHours, EPS);
        assertEquals(120, set.intervalInMinutes, EPS);
        assertEquals(25, set.times.length);
        assertEquals(48, set.times[24], EPS);
        
        assertEquals(4, set.entries.size());
        
        assertEquals(24, set.entries.get(0).description.period,EPS);
        assertEquals(25, set.entries.get(0).values.length);
        
        assertEquals(24, set.entries.get(1).description.period,EPS);
        assertEquals(0, set.entries.get(1).description.phase,EPS);
        assertEquals(5, set.entries.get(1).description.amplitude,EPS);
        assertTrue(set.entries.get(1).description.rhythmic);
        assertEquals(48, set.entries.get(1).description.durationHours,EPS);
        assertEquals(120, set.entries.get(1).description.intervalInMinutes,EPS);
        assertEquals(25, set.entries.get(1).values.length);
        
        assertEquals(26, set.entries.get(2).description.period,EPS);
        assertEquals(12, set.entries.get(2).description.phase,EPS);
        assertEquals(25, set.entries.get(2).values.length);
        
        assertEquals(26, set.entries.get(3).description.period,EPS);
        assertEquals(0, set.entries.get(3).description.phase,EPS);
        assertEquals(5, set.entries.get(3).description.amplitude,EPS);
        assertTrue(set.entries.get(3).description.rhythmic);
        assertEquals(48, set.entries.get(3).description.durationHours,EPS);
        assertEquals(120, set.entries.get(3).description.intervalInMinutes,EPS);
        assertEquals(25, set.entries.get(3).values.length);
    }
    
    @Test
    public void canSaveForJave() throws Exception {
        
        int durationHours = 48;
        int intervalMinutes = 120;
        
        Shape[] shapes = {COS, WIDE_PEAK};
        Skew[] skews = {NONE};
        double[] periods = {24};
        double[] circadianPhases = {12};
        double[] noiseLevels = {0.5};
        int replicates = 1;
        
        TestSuitGenerator instance = new TestSuitGenerator();
        
        DataSet set = instance.generateDataSet(durationHours, intervalMinutes, shapes, skews, periods, circadianPhases, noiseLevels, replicates);
        
        Path file = testFolder.newFile().toPath();
        
        instance.saveForJava(set, file);
        assertTrue(Files.exists(file));
        
        set = instance.readFromJava(file);
        assertNotNull(set);
        assertEquals(2, set.entries.size());
        assertEquals(COS, set.entries.get(0).description.shape);
        assertEquals(WIDE_PEAK, set.entries.get(1).description.shape);
        
        
    }
    
    @Test
    public void canSaveAsCSV() throws Exception {
        
        int durationHours = 48;
        int intervalMinutes = 120;
        
        Shape[] shapes = {COS, WIDE_PEAK};
        Skew[] skews = {NONE};
        double[] periods = {24};
        double[] circadianPhases = {12};
        double[] noiseLevels = {0.5};
        int replicates = 1;
        
        TestSuitGenerator instance = new TestSuitGenerator();
        
        DataSet set = instance.generateDataSet(durationHours, intervalMinutes, shapes, skews, periods, circadianPhases, noiseLevels, replicates);
        
        Path file = testFolder.newFile().toPath();
        
        instance.saveForTxt(set, file, true,",");
        assertTrue(Files.exists(file));
        
        List<String> lines = Files.readAllLines(file);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).startsWith("id,"));
        assertTrue(lines.get(2).startsWith("2,"));
        
        
    }
    

    /**
     * Test of generateEntries method, of class TestSuitGenerator.
     */
    /*@Test
    public void testGenerateEntries() {
        System.out.println("generateEntries");
        double[] times = null;
        Waveforms.Shape[] shapes = null;
        Waveforms.Skew[] skews = null;
        double[] periods = null;
        double[] circadianPhases = null;
        double[] noiseLevels = null;
        int replicates = 0;
        TestSuitGenerator instance = new TestSuitGenerator();
        List<DataEntry> expResult = null;
        List<DataEntry> result = instance.generateEntries(times, shapes, skews, periods, circadianPhases, noiseLevels, replicates);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
}
