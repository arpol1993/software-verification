
package edu.univ.software.verification.model.fa;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import edu.univ.software.verification.model.Automaton;
import edu.univ.software.verification.model.AutomatonState;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @param <T> type of data
 * 
 * @author arthur
 */
public abstract class AbstractAutomaton<T extends Serializable> implements Automaton<T> {
    /**
     * Automaton state definitions (label to state)
     */
    protected Map<String, AutomatonState<T>> states = ImmutableMap.of();
    
    /**
     * Automaton transitions (from, to, symbol)
     */
    protected Table<String, String, Set<String>> transitions = ImmutableTable.of();

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public AbstractAutomaton() {}
    
    public AbstractAutomaton(Map<String, AutomatonState<T>> states, Table<String, String, Set<String>> transitions) {
        this.states = ImmutableMap.copyOf(states);
        this.transitions = ImmutableTable.copyOf(transitions);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * {@inheritDoc}
     */
    @Override
    public Table<String, String, Set<String>> getTransitions() {
        return transitions;
    }
    //</editor-fold>
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AutomatonState<T> getState(String label) {
        return states.get(label);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasState(String label) {
        return getState(label) != null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AutomatonState<T>> getStates() {
        return Sets.newLinkedHashSet(states.values());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AutomatonState<T>> getInitialStates() {
        return states.values().stream().filter(s -> s.isInitial()).collect(Collectors.toSet());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Set<String>> getTransitionsFrom(String from) {
        return transitions.row(from);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Set<String>> getTransitionsTo(String to) {
        return transitions.column(to);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getTransitionSymbols(String from, String to) {
        return transitions.get(from, to);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasTransition(String from, String to) {
        return getTransitionSymbols(from, to) != null;
    }
    
    protected static abstract class AbstractBuilder<T extends Serializable, S extends Automaton.Builder<T>> {
        protected static final Logger logger = LoggerFactory.getLogger(AbstractBuilder.class);
        
        protected final Map<String, AutomatonState<T>> states = new LinkedHashMap<>();
        protected final Table<String, String, Set<String>> transitions = HashBasedTable.create();
        
        public S withState(String label) {
            return withState(label, AutomatonState.DEFAULT_INITIAL);
        }
        
        public S withState(String label, boolean initial) {
            return withState(label, initial, null);
        }
        
        public S withState(String label, boolean initial, T data) {
            if (this.states.put(label, new BasicState<>(label, initial, data)) != null) {
                logger.warn("Attempt to add duplicate state '{}'", label);
            }
            
            return getBuilder();
        }
        
        public S withStates(String... labels) {
            return withStates(AutomatonState.DEFAULT_INITIAL, labels);
        }
        
        public S withStates(boolean initial, String... labels) {
            return withStates(initial, ImmutableSet.copyOf(labels));
        }
        
        public S withStates(Collection<String> labels) {
            return withStates(AutomatonState.DEFAULT_INITIAL, labels);
        }
        
        public S withStates(boolean initial, Collection<String> labels) {
            labels.stream().forEach(s -> withState(s, initial));
            
            return getBuilder();
        }
        
        public S importStates(Collection<? extends AutomatonState<T>> states) {
            states.stream().forEach((AutomatonState<T> s) -> {
                withState(s.getLabel(), s.isInitial(), (s instanceof BasicState<?>) ? ((BasicState<T>) s).getData() : null);
            });
            
            return getBuilder();
        }
        
        public S withTransition(String from, String to, String symbol) throws IllegalArgumentException {
            return withTransition(from, to, ImmutableSet.of(symbol));
        }
        
        public S withTransition(String from, String to, String... symbols) throws IllegalArgumentException {
            return withTransition(from, to, ImmutableSet.copyOf(symbols));
        }
        
        public S withTransition(String from, String to, Collection<String> symbols) throws IllegalArgumentException {
            if (states.get(from) == null || states.get(to) == null) {
                throw new IllegalArgumentException(String.format(
                        "Both states '%s' and '%s' must exist to add transition", from, to));
            }
            
            if (transitions.put(from, to, ImmutableSet.copyOf(symbols)) != null) {
                logger.info("Transition from '{}' to '{}' updated to use '{}' symbols", from, to, symbols);
            }
            
            return getBuilder();
        }
        
        public S importTransitions(Table<String, String, Set<String>> transitions) {
            transitions.cellSet().stream().forEach(cell -> {
                withTransition(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            });
            
            return getBuilder();
        }
        
        protected abstract S getBuilder();
    }
}
