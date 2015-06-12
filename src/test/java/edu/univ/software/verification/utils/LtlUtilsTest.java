package edu.univ.software.verification.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.fa.BasicMullerAutomaton;
import edu.univ.software.verification.model.ltl.Atom;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author arthur
 */
public class LtlUtilsTest {

    @Test
    public void testComplexFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("!(G (p -> X F q) && F p)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withStates(false, "12", "23", "3")
                .withTransition("init", "12", ImmutableSet.of(symbol("p"), symbol("p", "q")))
                .withTransition("init", "23", ImmutableSet.of(symbol(), symbol("q")))
                .withTransition("init", "3", ImmutableSet.of(symbol(), symbol("p"), symbol("q"), symbol("p", "q")))
                .withTransition("12", "12", ImmutableSet.of(symbol(), symbol("p")))
                .withTransition("23", "23", ImmutableSet.of(symbol(), symbol("q")))
                .withTransition("3", "3", ImmutableSet.of(symbol(), symbol("p"), symbol("q"), symbol("p", "q")))
                .withTransition("3", "12", ImmutableSet.of(symbol("p"), symbol("p", "q")))
                .withFinalStateSet("12", "23")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testBoxVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("G a ");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("1", false)
                .withTransition("init", "1", symbol("a"))
                .withTransition("1", "1", symbol("a"))
                .withFinalStateSet("1")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testUntilVarVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("(a U b)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withStates(false, "1", "5")
                .withTransition("init", "1", ImmutableSet.of(symbol("a"), symbol("a", "b")))
                .withTransition("init", "5", ImmutableSet.of(symbol("b"), symbol("a", "b")))
                .withTransition("1", "1", ImmutableSet.of(symbol("a"), symbol("a", "b")))
                .withTransition("1", "5", ImmutableSet.of(symbol("b"), symbol("a", "b")))
                .withTransition("5", "5", ImmutableSet.of(symbol(), symbol("a"), symbol("b"), symbol("a", "b")))
                .withFinalStateSet("5")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testUntilVarFalseFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("(a U 0)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("1", false)
                .withTransition("init", "1", symbol("a"))
                .withTransition("1", "1", symbol("a"))
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testUntilFalseVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("(0 U a)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("2")
                .withTransition("init", "2", symbol("a"))
                .withTransition("2", "2", ImmutableSet.of(symbol(), symbol("a")))
                .withFinalStateSet("2")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testUntilFalseFalseFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("(0 U 0)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testReleaseVarVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("(a R b)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withStates(false, "1", "5")
                .withTransition("init", "1", ImmutableSet.of(symbol("b"), symbol("a", "b")))
                .withTransition("init", "5", ImmutableSet.of(symbol("a", "b")))
                .withTransition("1", "1", ImmutableSet.of(symbol("b"), symbol("a", "b")))
                .withTransition("1", "5", ImmutableSet.of(symbol("a", "b")))
                .withTransition("5", "5", ImmutableSet.of(symbol(), symbol("a"), symbol("b"), symbol("a", "b")))
                .withFinalStateSet("1", "5")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testReleaseVarFalseFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("(a R 0)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testReleaseFalseVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("(0 R a)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("1")
                .withTransition("init", "1", symbol("a"))
                .withTransition("1", "1", symbol("a"))
                .withFinalStateSet("1")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testNextVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("X a ");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withStates(false, "0", "1")
                .withTransition("init", "0", ImmutableSet.of(symbol(), symbol("a")))
                .withTransition("0", "1", symbol("a"))
                .withTransition("1", "1", ImmutableSet.of(symbol(), symbol("a")))
                .withFinalStateSet("0", "1")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testNextFalseFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("X 0 ");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("0")
                .withTransition("init", "0", symbol())
                .withFinalStateSet("0")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testDiamondVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("F a ");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withStates(false, "1", "5")
                .withTransition("init", "1", ImmutableSet.of(symbol(), symbol("a")))
                .withTransition("init", "5", symbol("a"))
                .withTransition("1", "1", ImmutableSet.of(symbol(), symbol("a")))
                .withTransition("1", "5", symbol("a"))
                .withTransition("5", "5", ImmutableSet.of(symbol(), symbol("a")))
                .withFinalStateSet("5")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testDiamondTrueFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("F 1 ");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withStates(false, "1", "5")
                .withTransition("init", "1", symbol())
                .withTransition("init", "5", symbol())
                .withTransition("1", "1", symbol())
                .withTransition("1", "5", symbol())
                .withTransition("5", "5", symbol())
                .withFinalStateSet("1", "5")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testDiamondFalseFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("F 0 ");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("1")
                .withTransition("init", "1", symbol())
                .withTransition("1", "1", symbol())
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testBoxOrVarVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("G (a || b)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("3")
                .withTransition("init", "3", ImmutableSet.of(symbol("a"), symbol("b"), symbol("a", "b")))
                .withTransition("3", "3", ImmutableSet.of(symbol("a"), symbol("b"), symbol("a", "b")))
                .withFinalStateSet("3")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testBoxOrVarFalseFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("G (a || 0)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("3")
                .withTransition("init", "3", symbol("a"))
                .withTransition("3", "3", symbol("a"))
                .withFinalStateSet("3")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testBoxAndVarVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("G (a && b)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("1")
                .withTransition("init", "1", symbol("a", "b"))
                .withTransition("1", "1", symbol("a", "b"))
                .withFinalStateSet("1")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testBoxAndVarFalseFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("G (a && 0)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testBoxImplVarVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("G (a -> b)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("3")
                .withTransition("init", "3", ImmutableSet.of(symbol(), symbol("b"), symbol("a", "b")))
                .withTransition("3", "3", ImmutableSet.of(symbol(), symbol("b"), symbol("a", "b")))
                .withFinalStateSet("3")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testBoxImplVarFalseFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("G (a -> 0)");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("3")
                .withTransition("init", "3", symbol())
                .withTransition("3", "3", symbol())
                .withFinalStateSet("3")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    @Test
    public void testBoxNegVarFormula() {
        MullerAutomaton<Set<Atom>> actual = convertToAutomata("G !a ");

        MullerAutomaton<Set<Atom>> expected = BasicMullerAutomaton.<Set<Atom>>builder()
                .withState("init", true)
                .withState("1")
                .withTransition("init", "1", symbol())
                .withTransition("1", "1", symbol())
                .withFinalStateSet("1")
                .build();

        Assert.assertEquals("Expected and actual automata are different", expected, actual);
    }

    private MullerAutomaton<Set<Atom>> convertToAutomata(String formula) {
        return LtlUtils.INSTANCE.convertToAutomata(LtlParser.parseString(formula).invert().invert().normalized());
    }

    private Set<Atom> symbol(String... atoms) {
        return Sets.newHashSet(atoms).stream().map(Atom::forName).collect(Collectors.toSet());
    }
}
