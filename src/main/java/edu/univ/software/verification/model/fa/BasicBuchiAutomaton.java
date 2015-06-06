
package edu.univ.software.verification.model.fa;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;

import edu.univ.software.verification.model.AutomatonState;
import edu.univ.software.verification.model.BuchiAutomaton;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @param <T> automaton state data type
 * @author arthur
 */
public class BasicBuchiAutomaton<T extends Serializable> extends AbstractAutomaton<T> implements BuchiAutomaton<T> {
    /**
     * Set of final states
     */
    protected Set<String> finalStates = ImmutableSet.of();

    public static <T extends Serializable> BuchiAutomaton.Builder<T> builder() {
        return new BasicBuilder<>();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public BasicBuchiAutomaton() {}
    
    public BasicBuchiAutomaton(Map<String, AutomatonState<T>> states, Collection<String> finalStates, Table<String, String, Set<String>> transitions) {
        super(states, transitions);
        
        this.finalStates = ImmutableSet.copyOf(finalStates);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getFinalStates() {
        return finalStates;
    }
    //</editor-fold>
    
    public static class BasicBuilder<T extends Serializable> extends AbstractAutomaton.AbstractBuilder<T, BasicBuilder<T>> implements BuchiAutomaton.Builder<T> {

        protected final Set<String> finalStates = new LinkedHashSet<>();
        
        @Override
        public BasicBuilder<T> withFinalState(String label) throws IllegalArgumentException {
            if (states.get(label) == null) {
                throw new IllegalArgumentException(String.format(
                        "Cannot mark non-existent state '%s' final", label));
            }
            
            if (!finalStates.add(label)) {
                logger.warn("Attempt to mark already final state '{}' final", label);
            }
            
            return getBuilder();
        }
        
        @Override
        public BasicBuilder<T> withFinalStates(String... labels) throws IllegalArgumentException {
            return withFinalStates(ImmutableSet.copyOf(labels));
        }
        
        @Override
        public BasicBuilder<T> withFinalStates(Collection<String> labels) throws IllegalArgumentException {
            labels.stream().forEach(this::withFinalState);
            
            return getBuilder();
        }
        
        @Override
        public BuchiAutomaton<T> build() {
            return new BasicBuchiAutomaton<>(states, finalStates, transitions);
        }
        
        @Override
        protected BasicBuilder<T> getBuilder() {
            return this;
        }
    }
}
