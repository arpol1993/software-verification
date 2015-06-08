package edu.univ.software.verification.model.ltl;

import com.google.common.collect.Sets;
import edu.univ.software.verification.model.LtlFormula;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class AtomTest {

    @Test
    public void testIsAtomic() throws Exception {
        assertEquals(true, Atom._0.isAtomic());
    }

    @Test
    public void testInvert() throws Exception {
        assertEquals(Atom._1, Atom._0.invert());
        assertEquals(Atom._0, Atom._1.invert());
        assertEquals(UnaryOp.build(UnaryOp.OpType.NEG, Atom.forName("a")), Atom.forName("a").invert());
    }

    @Test
    public void testEvaluate() throws Exception {
        assertEquals(false, Atom._0.evaluate(Collections.<String>emptySet()));
        assertEquals(true, Atom._1.evaluate(Collections.<String>emptySet()));
        assertEquals(false, Atom.forName("a").evaluate(Collections.<String>emptySet()));
    }

    @Test
    public void testFetchSymbols() throws Exception {
        assertEquals(Collections.<String>emptySet(), Atom._0.fetchSymbols());
        assertEquals(Collections.<String>emptySet(), Atom._1.fetchSymbols());
        assertEquals(Sets.newHashSet("a"), Atom.forName("a").fetchSymbols());
    }

    @Test
    public void testNormalized() throws Exception {
        assertEquals(Atom._0, Atom._0.normalized());
    }
}