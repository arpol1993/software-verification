
package edu.univ.software.verification.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Muller automaton representation
 *
 * @param <T> automaton state data type
 * @author arthur
 */
public interface MullerAutomaton<T extends Serializable> extends Automaton<T> {
    /**
     * Retrieves all final state sets
     * 
     * @return set of final state sets
     */
    public Set<Set<String>> getFinalStateSets();
    
    public interface Builder<T extends Serializable> extends Automaton.Builder<T> {
        public Builder<T> withState(String label);
        public Builder<T> withState(String label, boolean initial);
        public Builder<T> withState(String label, boolean initial, T data);
        public Builder<T> withTransition(String from, String to, String symbol) throws IllegalArgumentException;
        public Builder<T> withTransition(String from, String to, String... symbols) throws IllegalArgumentException;
        public Builder<T> withTransition(String from, String to, Collection<String> symbols) throws IllegalArgumentException;
        public Builder<T> withFinalStateSet(String... states);
        public Builder<T> withFinalStateSet(Collection<? extends String> states) throws IllegalArgumentException;
        public MullerAutomaton<T> build();
    }
}
