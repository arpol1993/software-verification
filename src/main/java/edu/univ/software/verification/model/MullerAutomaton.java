
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
    public Set<Set<String>> getFinalStateSets();
    
    public interface Builder<T> extends Automaton.Builder<T> {
        public Builder<T> withState(String label);
        public Builder<T> withState(String label, boolean initial);
        public Builder<T> withStates(String... labels);
        public Builder<T> withStates(boolean initial, String... labels);
        public Builder<T> withStates(boolean initial, Collection<String> labels);
        public Builder<T> withStates(Collection<AutomatonState> states);
        public Builder<T> withTransition(String from, String to, T symbol);
        public Builder<T> withTransition(String from, String to, Collection<T> symbols);
        public Builder<T> withTransitions(Table<String, String, Set<T>> transitions);
        public Builder<T> withFinalStateSet(String... states);
        public Builder<T> withFinalStateSet(Collection<String> states) throws IllegalArgumentException;
        public Builder<T> withFinalStateSets(Collection<Set<String>> finalStateSets);
        public MullerAutomaton<T> build();
    }
}
