
package edu.univ.software.verification.model;

import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Set;

/**
 * Buchi automaton representation
 *
 * @param <T> automaton transition symbol data type
 * @author arthur
 */
public interface BuchiAutomaton<T> extends Automaton<T> {
    /**
     * Retrieves all final state labels
     * 
     * @return list of final state identifiers
     */
    Set<String> getFinalStates();
    
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
        Builder<T> withFinalState(String label);
        Builder<T> withFinalStates(String... labels);
        Builder<T> withFinalStates(Collection<String> labels);
        BuchiAutomaton<T> build();
    }
}
