package edu.univ.software.verification.model.ltl;

import com.google.common.collect.Sets;
import edu.univ.software.verification.model.LtlFormula;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BinaryOpTest {

    @Test
    public void testIsAtomic() throws Exception {
        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.AND, mockedLtlFormula1, mockedLtlFormula2);

        assertEquals(false, binaryOp.isAtomic());
    }

    @Test
    public void testInvertAnd() throws Exception {
        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.invert()).thenReturn(invertedMockedLtlFormula1);

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.invert()).thenReturn(invertedMockedLtlFormula2);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.AND, mockedLtlFormula1, mockedLtlFormula2);
        LtlFormula actual = binaryOp.invert();

        assertEquals(BinaryOp.build(BinaryOp.OpType.OR, invertedMockedLtlFormula1, invertedMockedLtlFormula2), actual);
    }

    @Test
    public void testInvertOr() throws Exception {
        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.invert()).thenReturn(invertedMockedLtlFormula1);

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.invert()).thenReturn(invertedMockedLtlFormula2);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.OR, mockedLtlFormula1, mockedLtlFormula2);
        LtlFormula actual = binaryOp.invert();

        assertEquals(BinaryOp.build(BinaryOp.OpType.AND, invertedMockedLtlFormula1, invertedMockedLtlFormula2), actual);
    }

    @Test
    public void testInvertR() throws Exception {
        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.invert()).thenReturn(invertedMockedLtlFormula1);

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.invert()).thenReturn(invertedMockedLtlFormula2);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.R, mockedLtlFormula1, mockedLtlFormula2);
        LtlFormula actual = binaryOp.invert();

        assertEquals(BinaryOp.build(BinaryOp.OpType.U, invertedMockedLtlFormula1, invertedMockedLtlFormula2), actual);
    }

    @Test
    public void testInvertU() throws Exception {
        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.invert()).thenReturn(invertedMockedLtlFormula1);

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.invert()).thenReturn(invertedMockedLtlFormula2);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.U, mockedLtlFormula1, mockedLtlFormula2);
        LtlFormula actual = binaryOp.invert();

        assertEquals(BinaryOp.build(BinaryOp.OpType.R, invertedMockedLtlFormula1, invertedMockedLtlFormula2), actual);
    }

    @Test
    public void testInvert() throws Exception {
        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.clone()).thenReturn(mockedLtlFormula1);

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.invert()).thenReturn(invertedMockedLtlFormula2);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.IMPL, mockedLtlFormula1, mockedLtlFormula2);
        LtlFormula actual = binaryOp.invert();

        assertEquals(BinaryOp.build(BinaryOp.OpType.AND, mockedLtlFormula1, invertedMockedLtlFormula2), actual);
    }

    @Test
    public void testNormalizedImpl() throws Exception {
        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        LtlFormula invertedMockedLtlFormula1 = mock(LtlFormula.class);
        LtlFormula invertedNormalizedMockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.invert()).thenReturn(invertedMockedLtlFormula1);
        when(invertedMockedLtlFormula1.normalized()).thenReturn(invertedNormalizedMockedLtlFormula1);

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        LtlFormula normalizedMockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.normalized()).thenReturn(normalizedMockedLtlFormula2);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.IMPL, mockedLtlFormula1, mockedLtlFormula2);
        LtlFormula actual = binaryOp.normalized();

        assertEquals(BinaryOp.build(BinaryOp.OpType.OR, invertedNormalizedMockedLtlFormula1, normalizedMockedLtlFormula2), actual);
    }

    @Test
    public void testEvaluateAnd() throws Exception {
        Set<String> values = new HashSet<>();

        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.evaluate(values)).thenReturn(true);

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.evaluate(values)).thenReturn(false);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.AND, mockedLtlFormula1, mockedLtlFormula2);

        assertEquals(false, binaryOp.evaluate(values));
    }

    @Test
    public void testEvaluateOr() throws Exception {
        Set<String> values = new HashSet<>();

        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.evaluate(values)).thenReturn(true);

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.evaluate(values)).thenReturn(false);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.OR, mockedLtlFormula1, mockedLtlFormula2);

        assertEquals(true, binaryOp.evaluate(values));
    }

    @Test
    public void testEvaluateImpl() throws Exception {
        Set<String> values = new HashSet<>();

        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.evaluate(values)).thenReturn(true);

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.evaluate(values)).thenReturn(false);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.IMPL, mockedLtlFormula1, mockedLtlFormula2);

        assertEquals(false, binaryOp.evaluate(values));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateWithIllegalBinaryOp() throws Exception {
        Set<String> values = new HashSet<>();

        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.R, mockedLtlFormula1, mockedLtlFormula2);

        binaryOp.evaluate(values);
    }

    @Test
    public void testFetchSymbols() throws Exception {
        LtlFormula mockedLtlFormula1 = mock(LtlFormula.class);
        when(mockedLtlFormula1.getPropositions(true)).thenReturn(Sets.newHashSet("a", "b"));

        LtlFormula mockedLtlFormula2 = mock(LtlFormula.class);
        when(mockedLtlFormula2.getPropositions(true)).thenReturn(Sets.newHashSet("a", "c"));

        BinaryOp binaryOp = BinaryOp.build(BinaryOp.OpType.R, mockedLtlFormula1, mockedLtlFormula2);
        Set<String> actual = binaryOp.getPropositions(true);
        assertEquals(Sets.newHashSet("a", "b", "c"), actual);
    }
}