package edu.univ.software.verification.utils;

import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.ltl.Atom;

/**
 *
 * @author Pocomaxa
 */
public class LtlParser {
    
    public final static String _0 = "0";
    public final static String _1 = "1";

    public static LtlFormula parseString(String formula) throws IllegalArgumentException {
        LtlParser parser = new LtlParser(formula);
        
        return parser.parse();
    }

    private final String formulaString;

    public LtlParser(String formulaString) {
        this.formulaString = formulaString;
    }

    public LtlFormula parse() throws IllegalArgumentException {

        for (int i = 0; i < formulaString.length(); i++) {
            
        }
        
        throw new IllegalArgumentException();
    }
}
