package edu.univ.software.verification.utils;

import com.google.common.collect.Table;

import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.fa.BasicBuchiAutomaton;
import edu.univ.software.verification.model.fa.BasicMullerAutomaton;
import edu.univ.software.verification.model.fa.BasicState;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Set of automata utils such as convertion and cross product
 *
 * @author Pocomaxa
 */
public enum AutomataUtils {

    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(LtlUtils.class);

    /**
     * Converts specified Buchi automaton into Generalized Buchi (Muller)
     * automaton
     *
     * @param <T> Type of special state's data
     * @param buchi Buchi automaton to convert into LGBA
     * @return LGBA (Muller) automaton
     */
    public <T extends Serializable> MullerAutomaton<T> convert(BuchiAutomaton<T> buchi) {
        MullerAutomaton.Builder<T> builder = BasicMullerAutomaton.builder();

        for (Table.Cell<String, String, Set<String>> trans : buchi.getTransitions().cellSet()) {
            builder.withTransition(trans.getRowKey(), trans.getColumnKey(), trans.getValue());
        }

        buchi.getStates().stream().forEach((state) -> {
            builder.withState(state.getLabel(), state.isInitial(), ((BasicState<T>) state).getData());
        });

        builder.withFinalStateSet(buchi.getFinalStates());

        return builder.build();
    }

    /**
     * Converts specified Generalized Buchi (Muller) automaton into Buchi
     * automaton (degeneralization algorythm)
     *
     * @param <T> Type of special state's data
     * @param muller LGBA (Muller) automaton to convert into Buchi
     * (non-deterministic)
     * @return Buchi (non-deterministic) automaton
     */
    public <T extends Serializable> BuchiAutomaton<T> convert(MullerAutomaton<T> muller) {
        BuchiAutomaton.Builder<T> builder = BasicBuchiAutomaton.builder();

        Set<Set<String>> finalStates = muller.getFinalStateSets();
        Set<String> addedStates = new LinkedHashSet<>();

        for (int i = 0; i < finalStates.size(); i++) {

            for (Table.Cell<String, String, Set<String>> trans : muller.getTransitions().cellSet()) {
                int j = i;
                if (finalIndexOf(finalStates, trans.getRowKey()) == i) {
                    j = (i + 1) % finalStates.size();
                }

                String first = String.format("(%s, %d)", trans.getRowKey(), i);
                String second = String.format("(%s, %d)", trans.getColumnKey(), j);

                if (!addedStates.contains(first)) {
                    BasicState<T> st = (BasicState<T>) muller.getState(trans.getRowKey());

                    builder.withState(first, i == 0 && st.isInitial(), st.getData());
                    addedStates.add(first);

                    if (finalIndexOf(finalStates, trans.getRowKey()) == 0) {
                        builder.withFinalState(first);
                    }
                }

                if (!addedStates.contains(second)) {
                    BasicState<T> st = (BasicState<T>) muller.getState(trans.getColumnKey());

                    builder.withState(second, i == 0 && st.isInitial(), st.getData());
                    addedStates.add(second);

                    if (finalIndexOf(finalStates, trans.getColumnKey()) == 0) {
                        builder.withFinalState(second);
                    }
                }

                builder.withTransition(first, second, trans.getValue());
            }
        }

        return builder.build();
    }

    private int finalIndexOf(Set<Set<String>> finalStates, String state) {
        int i = 0;
        for (Set<String> finalStateSet : finalStates) {
            if (finalStateSet.contains(state)) {
                return i;
            }

            i++;
        }

        return -1;
    }
}
