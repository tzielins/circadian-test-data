/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tzielins
 */
public class TestSuitGeneratorTest {
    
    public TestSuitGeneratorTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of generateAllShapes method, of class TestSuitGenerator.
     */
    @Test
    public void testGenerateAllShapes() {
        TestSuitGenerator instance = new TestSuitGenerator();
        //instance.generateAllShapes();
        instance.generateNoiseLevels();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
