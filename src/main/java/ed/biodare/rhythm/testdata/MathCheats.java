/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.rhythm.testdata;

import java.util.function.DoubleUnaryOperator;
import org.apache.commons.math3.analysis.UnivariateFunction;
import static org.apache.commons.math3.util.FastMath.*;

/**
 *
 * @author Zielu
 */
public class MathCheats {
    
    final double EPS = 1E-10;
    
    public double findXForMaxInArc(UnivariateFunction function, double start, 
            double end, double xEps) {
    
        if (start >= end)
            throw new IllegalArgumentException("Start must be before end");
        
        final double dEPS = max(EPS,xEps/1000);
        
        if (!rUp(function, start, dEPS))
            throw new IllegalArgumentException("Function must raise at start");
        if (!lUp(function, end, dEPS))
            throw new IllegalArgumentException("Function must fall at end");
        
        double left = start;
        double right = end;
        double maxL = function.value(left);
        double maxR = function.value(right);
        
        double maxV, maxX;
        if (maxR >= maxL ) {
            maxV = maxR;
            maxX = right;
        } else {
            maxV = maxL;
            maxX = left;
        }
        
        for (int i =0;i<1000;i++) {
            double mid = (left+right)/2;
            double v = function.value(mid);
            
            if (v > maxV) {
                maxV = v;
                maxX = mid;
            }
            
            if (rUp(function, mid, dEPS)) {
                left = mid;
            } else {
                right = mid;
            }
            
            if ((right - left) < xEps) 
                break;
            
        }
        
        
        double maxV = Double.MIN_VALUE;
        double maxX = Double.NaN;
        
        return 1;
    }
    
    final boolean rUp(UnivariateFunction function, double x, double EPS) {
        return function.value(x+EPS) >= function.value(x); 
    }
    
    final boolean lUp(UnivariateFunction function, double x, double EPS) {
        return function.value(x-EPS) >= function.value(x); 
    }    
}
