package edu.univ.software.verification.utils;

import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.ltl.Atom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pocomaxa
 */
public class LtlParserTest {
    
    public LtlParserTest() {
    }

    /**
     * Test of parseString method, of class LtlParser.
     */
    @Test
    public void testParseStringEmpty() {
        System.out.println("parseString empty");
        String formula = "";
        LtlFormula expResult = Atom._0;
        LtlFormula result = LtlParser.parseString(formula);
        assertEquals(expResult, result);
    }
    
}
