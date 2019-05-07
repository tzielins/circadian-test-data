/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.function.DoubleUnaryOperator;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import static org.apache.commons.math3.util.FastMath.*;

/**
 *
 * @author Zielu
 */
public class MathCheats {
    
    final double EPS = 1E-6;
    final double xEPS = 0.001;
    
    public double findXForMaxInArc(UnivariateFunction function, double start, 
            double end) {
    
        if (start >= end)
            throw new IllegalArgumentException("Start must be before end");
        
        
        Derivative derivative = new Derivative(function);
        UnivariateSolver solver   = new BrentSolver(EPS/1000);
        
        double eX = solver.solve(100,derivative, start,end);
        
        eX = tweakMaxPosition(eX, function, start, end);
        /*
        if (abs(derivative.value(eX)) > EPS)
            throw new IllegalStateException("Found "+eX+" as extreme candidate but derivative is not 0 at this point: "+derivative.value(eX));

        if (!couldBeMax(eX, function, start, end))
            throw new IllegalStateException("Found "+eX+" as extreme candidate but it is not a max");
        */
        return eX;
        
        
    }

    double tweakMaxPosition(double eX, UnivariateFunction function, double start, double end) {
        
        
        double l1 = eX - xEPS;
        double r1 = eX + xEPS;

        double max = function.value(eX);
        
        if (l1 < start) l1 = start;
        if (r1 > end) r1 = end;
        
        if (max < function.value(l1)) {
            
            while(l1 > start && function.value(l1) > max) {
                max = function.value(l1);
                eX = l1;
                l1 = l1 - xEPS;
            }
        } else if (max < function.value(r1)){
            
            while(r1 < end && function.value(r1) > max) {
                max = function.value(r1);
                eX = r1;
                r1 = r1 + xEPS;
            }
        } 
        return eX;
    }
    
    static class Derivative implements UnivariateFunction {
        
        UnivariateDifferentiableFunction derivative;
        
        Derivative(UnivariateFunction function) {
            
            FiniteDifferencesDifferentiator differentiator = new FiniteDifferencesDifferentiator(5, 0.01);

            derivative = differentiator.differentiate(function);
        }
        
        @Override
        public double value(double x) {
            DerivativeStructure xDS = new DerivativeStructure(1, 1, 0, x);
            DerivativeStructure yDS = derivative.value(xDS);
            return yDS.getPartialDerivative(1);
        }
    }

    boolean couldBeMax(double eX, UnivariateFunction function, double start, double end) {
        
        double l1 = eX - xEPS;
        double l2 = eX - 10*xEPS;
        
        double r1 = eX + xEPS;
        double r2 = eX + 10*xEPS;
        
        double max = function.value(eX);
        if (l1 >= start) {
            if (function.value(l1) > max) {
                throw new IllegalStateException("Found "+eX+" as extreme candidate but it is not a max l1 >");                
                //return false;
            }
            if (l2 >= start && (function.value(l2) >= max))
                return false;
        }
        
        if (r1 <= end) {
            if (function.value(r1) > max) {
                throw new IllegalStateException("Found "+eX+" as extreme candidate but it is not a max r1 >");                
                //return false;                
            }
            if (r2 <= end && (function.value(r2) >= max))
                return false;
        }
        
        return true;
    }
    
}
