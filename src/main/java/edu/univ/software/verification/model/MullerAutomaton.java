
package edu.univ.software.verification.model;

import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Set;

/**
 * Muller automaton representation
 *
 * @param <T> automaton transition symbol data type
 * @author arthur
 */
public interface MullerAutomaton<T> extends Automaton<T> {
    /**
     * Retrieves all final state sets
     * 
     * @return set of final state sets
     */
    Set<Set<String>> getFinalStateSets();
    
    interface Builder<T> extends Automaton.Builder<T> {
        Builder<T> withState(String label);
        Builder<T> withState(String label, boolean initial);
        Builder<T> withStates(String... labels);
        Builder<T> withStates(boolean initial, String... labels);
        Builder<T> withStates(boolean initial, Collection<String> labels);
        Builder<T> withStates(Collection<AutomatonState> states);
        Builder<T> withTransition(String from, String to, T symbol);
        Builder<T> withTransition(String from, String to, Collection<T> symbols);
        Builder<T> withTransitions(Table<String, String, Set<T>> transitions);
        Builder<T> withFinalStateSet(String... states);
        Builder<T> withFinalStateSet(Collection<String> states) throws IllegalArgumentException;
        Builder<T> withFinalStateSets(Collection<Set<String>> finalStateSets);
        MullerAutomaton<T> build();
    }
}
