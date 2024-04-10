package calculator;

import org.mariuszgromada.math.mxparser.*;

public class Solver {
    public Solver(){
        License.iConfirmNonCommercialUse("Oleg B");
        mXparser.disableImpliedMultiplicationMode();
        //mXparser.setDegreesMode();
    }
    public String Calculate(String input){
        Expression expression = new Expression(input);
        double value = expression.calculate();
        return String.valueOf(value);
    }
}
