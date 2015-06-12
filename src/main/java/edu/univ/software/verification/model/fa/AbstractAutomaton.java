package edu.univ.software.verification.model.fa;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import edu.univ.software.verification.model.Automaton;
import edu.univ.software.verification.model.AutomatonState;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @param <T> automaton transition symbol data type
 *
 * @author arthur
 */
public abstract class AbstractAutomaton<T> implements Automaton<T> {

    /**
     * Automaton state definitions (label to state)
     */
    protected Map<String, AutomatonState> states = ImmutableMap.of();

    /**
     * Automaton transitions (from, to, symbol)
     */
    protected Table<String, String, Set<T>> transitions = ImmutableTable.of();

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public AbstractAutomaton() {
    }

    public AbstractAutomaton(Map<String, AutomatonState> states, Table<String, String, Set<T>> transitions) {
        this.states = ImmutableMap.copyOf(states);
        this.transitions = ImmutableTable.copyOf(transitions);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * {@inheritDoc}
     */
    @Override
    public Table<String, String, Set<T>> getTransitions() {
        return transitions;
    }
    //</editor-fold>

    /**
     * {@inheritDoc}
     */
    @Override
    public AutomatonState getState(String label) {
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
    public Set<AutomatonState> getStates() {
        return Sets.newLinkedHashSet(states.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AutomatonState> getInitialStates() {
        return states.values().stream().filter(s -> s.isInitial()).collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Set<T>> getTransitionsFrom(String from) {
        return transitions.row(from);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Set<T>> getTransitionsTo(String to) {
        return transitions.column(to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<T> getTransitionSymbols(String from, String to) {
        return transitions.get(from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasTransition(String from, String to) {
        return getTransitionSymbols(from, to) != null;
    }

    //<editor-fold defaultstate="collapsed" desc="hashCode + equals">
    @Override
    public int hashCode() {
        int hash = 7;

        hash = 59 * hash + Objects.hashCode(this.states);
        hash = 59 * hash + Objects.hashCode(this.transitions);

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof AbstractAutomaton)) {
            return false;
        }

        final AbstractAutomaton<?> other = (AbstractAutomaton<?>) o;

        return Objects.equals(this.states, other.states)
                && Objects.equals(this.transitions, other.transitions);
    }
    //</editor-fold>

    protected static abstract class AbstractBuilder<T, S extends Automaton.Builder<T>> {

        protected static final Logger logger = LoggerFactory.getLogger(AbstractBuilder.class);

        protected final Map<String, AutomatonState> states = new LinkedHashMap<>();
        protected final Table<String, String, Set<T>> transitions = HashBasedTable.create();

        public S withState(String label) {
            return withState(label, AutomatonState.DEFAULT_INITIAL);
        }

        public S withState(String label, boolean initial) {
            if (this.states.put(label, new BasicState(label, initial)) != null) {
                logger.debug("Attempt to add duplicate state '{}'", label);
            }

            return getBuilder();
        }

        public S withStates(String... labels) {
            return withStates(AutomatonState.DEFAULT_INITIAL, labels);
        }

        public S withStates(boolean initial, String... labels) {
            return withStates(initial, ImmutableSet.copyOf(labels));
        }

        public S withStates(boolean initial, Collection<String> labels) {
            labels.stream().forEach(s -> withState(s, initial));

            return getBuilder();
        }

        public S withStates(Collection<AutomatonState> states) {
            states.stream().forEach((AutomatonState s) -> {
                withState(s.getLabel(), s.isInitial());
            });

            return getBuilder();
        }

        public S withTransition(String from, String to, T symbol) {
            return withTransition(from, to, ImmutableSet.of(symbol));
        }

        public S withTransition(String from, String to, Collection<T> symbols) {
            if (states.get(from) == null || states.get(to) == null) {
                throw new IllegalArgumentException(String.format(
                        "Both states '%s' and '%s' must exist to add transition", from, to));
            }

            if (transitions.put(from, to, ImmutableSet.copyOf(symbols)) != null) {
                logger.info("Transition from '{}' to '{}' updated to use '{}' symbols", from, to, symbols);
            }

            return getBuilder();
        }

        public S withTransitions(Table<String, String, Set<T>> transitions) {
            transitions.cellSet().stream().forEach(cell -> {
                withTransition(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            });

            return getBuilder();
        }

        protected void optimize() {
            // execute state joins while possible
            while (true) {
                AutomatonState firstState = null;
                AutomatonState secondState = null;

                outer:
                for (AutomatonState s1 : states.values()) {
                    for (AutomatonState s2 : states.values()) {
                        if (!Objects.equals(s1, s2) && checkStatesJoinable(s1, s2)) {
                            firstState = s1;
                            secondState = s2;

                            break outer;
                        }
                    }
                }

                // check whether joinable states are present
                if (firstState != null && secondState != null) {
                    // join specified states into first one
                    joinStates(firstState.getLabel(), secondState.getLabel());
                } else {
                    // no states to join left
                    break;
                }
            }
        }

        protected boolean checkStatesJoinable(AutomatonState s1, AutomatonState s2) {
            // states must be either both initial or not and have same outgoing transitions 
            return s1.isInitial() == s2.isInitial() && Objects.equals(
                    transitions.row(s1.getLabel()), transitions.row(s2.getLabel()));
        }

        protected void joinStates(String s1, String s2) {
            // copy transitions into second state to transitions into first
            states.keySet().stream().forEach((s) -> {
                Set<T> newIncoming = !Objects.equals(s, s2) ? transitions.get(s, s2) : null;

                // check for additional incoming transitions
                if (newIncoming != null) {
                    Set<T> oldIncoming = transitions.get(s, s1);

                    // add new transitions to the first state
                    transitions.put(s, s1, Sets.union(oldIncoming != null
                            ? oldIncoming : ImmutableSet.of(), newIncoming).immutableCopy());
                }

                // remove transitions between s and s2
                transitions.remove(s, s2);
                transitions.remove(s2, s);
            });

            // remove second state
            states.remove(s2);
        }

        protected abstract S getBuilder();
    }
}
