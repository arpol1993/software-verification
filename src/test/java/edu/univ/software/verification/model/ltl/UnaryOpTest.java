package edu.univ.software.verification.model.ltl;

import com.google.common.collect.Sets;
import edu.univ.software.verification.model.LtlFormula;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnaryOpTest {

    @Test
    public void testIsAtomicWhenNegAndAtomReturnsTrue() throws Exception {
        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.NEG, Atom._0);
        assertEquals(true, unaryOp.isAtomic());
    }

    @Test
    public void testIsAtomicWhenNotNegAndAtomReturnsFalse() throws Exception {
        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.F, Atom._0);
        assertEquals(false, unaryOp.isAtomic());

        UnaryOp unaryOp2 = UnaryOp.build(UnaryOp.OpType.G, Atom._0);
        assertEquals(false, unaryOp2.isAtomic());

        UnaryOp unaryOp3 = UnaryOp.build(UnaryOp.OpType.X, Atom._0);
        assertEquals(false, unaryOp3.isAtomic());
    }

    @Test
    public void testInvertWithNegOperation() throws Exception {
        //given
        LtlFormula mockedLtlFormula = mock(LtlFormula.class);
        //when(mockedLtlFormula).thenReturn(mockedLtlFormula);

        //when
        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.NEG, mockedLtlFormula);
        LtlFormula inverted = unaryOp.invert();
        // then
        assertEquals(mockedLtlFormula, inverted);

    }

    @Test
    public void testInvertWithFOperation() throws Exception {
        //given
        LtlFormula mockedLtlFormula = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula = mock(LtlFormula.class);
        when(mockedLtlFormula.invert()).thenReturn(invertedMockedLtlFormula);

        //when
        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.F, mockedLtlFormula);
        LtlFormula inverted = unaryOp.invert();
        // then
        assertEquals(UnaryOp.build(UnaryOp.OpType.G, invertedMockedLtlFormula), inverted);

    }

    @Test
    public void testInvertWithGOperation() throws Exception {
        //given
        LtlFormula mockedLtlFormula = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula = mock(LtlFormula.class);
        when(mockedLtlFormula.invert()).thenReturn(invertedMockedLtlFormula);

        //when
        UnaryOp unaryOp2 = UnaryOp.build(UnaryOp.OpType.G, mockedLtlFormula);
        LtlFormula inverted2 = unaryOp2.invert();
        // then
        assertEquals(UnaryOp.build(UnaryOp.OpType.F, invertedMockedLtlFormula), inverted2);

    }

    @Test
    public void testInvertWithXOperation() throws Exception {
        //given
        LtlFormula mockedLtlFormula = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula = mock(LtlFormula.class);
        when(mockedLtlFormula.invert()).thenReturn(invertedMockedLtlFormula);

        //when
        UnaryOp unaryOp3 = UnaryOp.build(UnaryOp.OpType.X, mockedLtlFormula);
        LtlFormula inverted3 = unaryOp3.invert();
        // then
        assertEquals(UnaryOp.build(UnaryOp.OpType.X, invertedMockedLtlFormula), inverted3);

    }

    @Test
    public void testNormalizedWithG() throws Exception {
        //given
        LtlFormula mockedLtlFormula = mock(LtlFormula.class);
        LtlFormula normalizedMockedLtlFormula = mock(LtlFormula.class);
        when(mockedLtlFormula.normalized()).thenReturn(normalizedMockedLtlFormula);

        //when
        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.G, mockedLtlFormula);
        LtlFormula actual = unaryOp.normalized();
        LtlFormula expected = BinaryOp.build(BinaryOp.OpType.R, Atom._0, normalizedMockedLtlFormula);

        //then
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizedWithF() throws Exception {
        //given
        LtlFormula mockedLtlFormula = mock(LtlFormula.class);
        LtlFormula normalizedMockedLtlFormula = mock(LtlFormula.class);
        when(mockedLtlFormula.normalized()).thenReturn(normalizedMockedLtlFormula);

        //when
        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.F, mockedLtlFormula);
        LtlFormula actual = unaryOp.normalized();
        LtlFormula expected = BinaryOp.build(BinaryOp.OpType.U, Atom._1, normalizedMockedLtlFormula);

        //then
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizedWithNeitherGNorF() throws Exception {
        //given
        LtlFormula mockedLtlFormula = Atom._0;//  mock(LtlFormula.class);

        //when
        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.NEG, mockedLtlFormula);
        LtlFormula actual = unaryOp.normalized();

        //then
        assertEquals(unaryOp, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateWithNotNegativeThrowsException() throws Exception {
        LtlFormula mockedLtlFormula = mock(LtlFormula.class);

        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.X, mockedLtlFormula);
        unaryOp.evaluate(Collections.<String>emptySet());
    }

    @Test
    public void testEvaluate() throws Exception {
        //given
        LtlFormula mockedLtlFormula = mock(LtlFormula.class);
        when(mockedLtlFormula.evaluate(Collections.<String>emptySet())).thenReturn(true);

        //when
        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.NEG, mockedLtlFormula);
        boolean actual = unaryOp.evaluate(Collections.<String>emptySet());

        //then
        assertEquals(false, actual);
    }

    @Test
    public void testFetchSymbols() throws Exception {
        //given
        LtlFormula mockedLtlFormula = mock(LtlFormula.class);
        when(mockedLtlFormula.getPropositions(false)).thenReturn(Sets.newHashSet("a"));

        //when
        UnaryOp unaryOp = UnaryOp.build(UnaryOp.OpType.NEG, mockedLtlFormula);
        Set<String> actual = unaryOp.getPropositions(false);

        //then
        assertEquals(Sets.newHashSet("a"), actual);
    }
}