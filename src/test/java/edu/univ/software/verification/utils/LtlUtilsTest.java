package edu.univ.software.verification.utils;

import com.google.common.collect.ImmutableSet;

import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.fa.BasicMullerAutomaton;
import edu.univ.software.verification.model.ltl.Atom;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;


/**
 *
 * @author arthur
 */
public class LtlUtilsTest {

    @Test
    public void testBoxFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("G a ");
        
        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("1", false)
                .withTransition("init", "1", ImmutableSet.of(Atom.forName("a")))
                .withTransition("1", "1", ImmutableSet.of(Atom.forName("a")))
                .withFinalStateSet("1")
                .build();
        
        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }
    
    private MullerAutomaton<Set<Atom>> convertToAutomata(String formula) {
        return LtlUtils.INSTANCE.convertToAutomata(LtlParser.parseString(formula).invert().invert().normalized());
    }
}
