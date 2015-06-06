
package edu.univ.software.verification.model;

import com.google.common.collect.Table;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Buchi automaton representation
 *
 * @param <T> automaton state data type
 * @author arthur
 */
public interface BuchiAutomaton<T extends Serializable> extends Automaton<T> {
    /**
     * Retrieves all final state labels
     * 
     * @return list of final state identifiers
     */
    public Set<String> getFinalStates();
    
    public interface Builder<T extends Serializable> extends Automaton.Builder<T> {
        public Builder<T> withState(String label);
        public Builder<T> withState(String label, boolean initial);
        public Builder<T> withState(String label, boolean initial, T data);
        public Builder<T> withStates(String... labels);
        public Builder<T> withStates(boolean initial, String... labels);
        public Builder<T> withStates(Collection<String> labels);
        public Builder<T> withStates(boolean initial, Collection<String> labels);
        public Builder<T> importStates(Collection<? extends AutomatonState<T>> states);
        public Builder<T> withTransition(String from, String to, String symbol) throws IllegalArgumentException;
        public Builder<T> withTransition(String from, String to, String... symbols) throws IllegalArgumentException;
        public Builder<T> withTransition(String from, String to, Collection<String> symbols) throws IllegalArgumentException;
        public Builder<T> importTransitions(Table<String, String, Set<String>> transitions);
        public Builder<T> withFinalState(String label) throws IllegalArgumentException;
        public Builder<T> withFinalStates(String... labels) throws IllegalArgumentException;
        public Builder<T> withFinalStates(Collection<String> labels) throws IllegalArgumentException;
        public BuchiAutomaton<T> build();
    }
}
