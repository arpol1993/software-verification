
package edu.univ.software.verification.model.fa;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import edu.univ.software.verification.model.AutomatonState;
import edu.univ.software.verification.model.MullerAutomaton;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @param <T> automaton transition symbol data type
 * @author arthur
 */
public class BasicMullerAutomaton<T> extends AbstractAutomaton<T> implements MullerAutomaton<T> {
    /**
     * Set of sets of final states (O_o)
     */
    protected Set<Set<String>> finalStateSets = ImmutableSet.of();

    public static <T> MullerAutomaton.Builder<T> builder() {
        return new BasicBuilder<>();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public BasicMullerAutomaton() {}
    
    public BasicMullerAutomaton(Map<String, AutomatonState> states, Set<Set<String>> finalStateSets, Table<String, String, Set<T>> transitions) {
        super(states, transitions);
        
        this.finalStateSets = ImmutableSet.copyOf(finalStateSets);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Set<String>> getFinalStateSets() {
        return finalStateSets;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "BasicMullerAutomaton{" + "states=" + states + ", transitions=" + transitions + ", finalStateSets=" + finalStateSets + '}';
    }
    
    public static class BasicBuilder<T> extends AbstractAutomaton.AbstractBuilder<T, BasicBuilder<T>> implements MullerAutomaton.Builder<T> {

        protected final Set<Set<String>> finalStateSets = new LinkedHashSet<>();
        
        @Override
        public BasicBuilder<T> withFinalStateSet(String... stateSet) {
            return withFinalStateSet(ImmutableSet.copyOf(stateSet));
        }
        
        @Override
        public BasicBuilder<T> withFinalStateSet(Collection<String> stateSet) throws IllegalArgumentException {
            Set<String> finalStateSet = ImmutableSet.copyOf(stateSet);
            Sets.SetView<String> missingStates = Sets.difference(finalStateSet, this.states.keySet());
            
            if (!missingStates.isEmpty()) {
                throw new IllegalArgumentException(String.format("Invalid final state set. States '%s' are missing.", missingStates));
            }
            
            if (!finalStateSets.add(finalStateSet)) {
                logger.warn("Attempt to add duplicate final state set '{}'", finalStateSet);
            }
            
            return getBuilder();
        }
        
        @Override
        public BasicBuilder<T> withFinalStateSets(Collection<Set<String>> finalStateSets) {
            finalStateSets.stream().forEach(this::withFinalStateSet);
            
            return getBuilder();
        }
        
        @Override
        public MullerAutomaton<T> build() {
            return new BasicMullerAutomaton<>(states, finalStateSets, transitions);
        }
        
        @Override
        protected BasicBuilder<T> getBuilder() {
            return this;
        }
    }
}
