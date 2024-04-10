package calculator;

import org.mariuszgromada.math.mxparser.*;

public class Solver {

    public boolean degMod = false;

    public Solver(){
        License.iConfirmNonCommercialUse("Oleg B");
        mXparser.disableImpliedMultiplicationMode();
        //mXparser.setDegreesMode();
    }

    public void setDegMod(boolean mod){
        degMod = mod;
        if (degMod) mXparser.setDegreesMode();
        else mXparser.setRadiansMode();
    }

    public String Calculate(String input){
        Expression expression = new Expression(input);
        double value = expression.calculate();
        return String.valueOf(value);
    }
}
