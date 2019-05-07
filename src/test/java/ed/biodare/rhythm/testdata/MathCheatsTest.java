/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.function.DoubleUnaryOperator;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tomasz Zielinski
 */
public class MathCheatsTest {
    
    double EPS = 1E-6;
    
    public MathCheatsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of findXForMax method, of class MathCheats.
     */
    @Test
    public void testFindXForMaxInArc() {
        UnivariateFunction function = new Gaussian(0, 0.4);
        
        double start = -0.1;
        double end = 0.5;
        double xEps = 0.001;
        double yEps = 0.001;
        MathCheats instance = new MathCheats();
        double expResult = 0.0;
        double result = instance.findXForMaxInArc(function, start, end, xEps, yEps);
        assertEquals(expResult, result, EPS);
    }
    
}
