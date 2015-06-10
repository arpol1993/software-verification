package edu.univ.software.verification.utils;

import edu.univ.software.verification.exceptions.InvalidBuilderUsage;
import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.ltl.Atom;
import edu.univ.software.verification.model.ltl.BinaryOp;
import edu.univ.software.verification.model.ltl.Builder;
import edu.univ.software.verification.model.ltl.UnaryOp;
import java.util.Set;

import org.junit.Test;

/**
 *
 * @author arthur
 */
public class LtlUtilsTest {

    @Test
    public void testBoxFormulaHandling() throws InvalidBuilderUsage {
        LtlFormula formula = (new Builder()).
                withUnary(UnaryOp.OpType.F).
                withBinary().
                placeAtom(Atom.AtomType.VAR, "a").
                placeBinaryOperator(BinaryOp.OpType.OR).
                placeAtom(Atom.AtomType.VAR, "b").
                build().normalized();

        MullerAutomaton<Set<Atom>> automaton = LtlUtils.INSTANCE.convertToAutomata(formula);
        
        //System.out.println(automaton);
    }
}
