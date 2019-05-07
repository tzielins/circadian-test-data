/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
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
        MathCheats instance = new MathCheats();
        double expResult = 0.0;
        double result = instance.findXForMaxInArc(function, start, end);
        assertEquals(expResult, result, EPS);
        
        function = new Gaussian(0.2, 0.4);
        start = -0.4;
        end = 0.4;
        
        expResult = 0.2;
        result = instance.findXForMaxInArc(function, start, end);
        assertEquals(expResult, result, EPS);
        
        function = new Gaussian(0.4, 0.4);
        start = -0.4;
        end = 0.4;
        
        expResult = 0.4;
        result = instance.findXForMaxInArc(function, start, end);
        assertEquals(expResult, result, EPS);        
    }
    
    //@Test
    public void testGausianDer() {
        
        double start = -0.1;
        double end = 0.5;
        
        UnivariateFunction function = new Gaussian(0, 0.4);        
        FiniteDifferencesDifferentiator differentiator = new FiniteDifferencesDifferentiator(5, 0.001);

        UnivariateDifferentiableFunction derivative = differentiator.differentiate(function);
        
        List<Double> X = new ArrayList<>();
        List<Double> Y = new ArrayList<>();
        List<Double> D = new ArrayList<>();
        
        for (int i = -10; i < 50; i+=2) {
            double x = i / 100.0;
            double y = function.value(x);
            DerivativeStructure xDS = new DerivativeStructure(1, 1, 0, x);
            DerivativeStructure yDS = derivative.value(xDS);
            X.add(x);
            Y.add(y); 
            D.add(yDS.getPartialDerivative(1));
        }
        
        System.out.println(X);
        System.out.println(Y);
        System.out.println(D);
        
        assertTrue(false);
    }    
    

    
}
