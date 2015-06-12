package edu.univ.software.verification.model.fa;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import edu.univ.software.verification.model.AutomatonState;
import edu.univ.software.verification.model.MullerAutomaton;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    public BasicMullerAutomaton() {
    }

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

    //<editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 3;

        hash = 59 * hash + super.hashCode();
        hash = 59 * hash + Objects.hashCode(this.finalStateSets);

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof BasicMullerAutomaton) || !super.equals(o)) {
            return false;
        }

        final BasicMullerAutomaton<?> other = (BasicMullerAutomaton<?>) o;

        return Objects.equals(this.finalStateSets, other.finalStateSets);
    }

    @Override
    public String toString() {
        return "BasicMullerAutomaton{" + "states=" + states + ", transitions=" + transitions + ", finalStateSets=" + finalStateSets + '}';
    }
    //</editor-fold>

    public static class BasicBuilder<T> extends AbstractAutomaton.AbstractBuilder<T, BasicBuilder<T>> implements MullerAutomaton.Builder<T> {

        protected final Set<Set<String>> finalStateSets = new LinkedHashSet<>();

        @Override
        public BasicBuilder<T> withFinalStateSet(String... stateSet) {
            return withFinalStateSet(ImmutableSet.copyOf(stateSet));
        }

        @Override
        public BasicBuilder<T> withFinalStateSet(Collection<String> stateSet) throws IllegalArgumentException {
            Set<String> finalStateSet = ImmutableSet.copyOf(stateSet);
            Set<String> missingStates = Sets.difference(finalStateSet, this.states.keySet());

            if (!missingStates.isEmpty()) {
                throw new IllegalArgumentException(String.format("Invalid final state set. States '%s' are missing.", missingStates));
            }

            if (!finalStateSets.add(finalStateSet)) {
                logger.debug("Attempt to add duplicate final state set '{}'", finalStateSet);
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
            // run basic optimization
            optimize();

            // build resulting automaton
            return new BasicMullerAutomaton<>(states, finalStateSets, transitions);
        }

        @Override
        protected BasicBuilder<T> getBuilder() {
            return this;
        }

        @Override
        protected boolean checkStatesJoinable(AutomatonState s1, AutomatonState s2) {
            // specified states must belong to the same final state sets
            return finalStateSets.stream().allMatch(fss -> fss.contains(s1.getLabel()) == fss.contains(s2.getLabel()))
                    && super.checkStatesJoinable(s1, s2);
        }

        @Override
        protected void joinStates(String s1, String s2) {
            super.joinStates(s1, s2);

            // remove second state from all final state sets
            Set<Set<String>> newFinalStateSets = finalStateSets.stream().map((Set<String> fss) -> {
                Set<String> newFss = Sets.newLinkedHashSet(fss);
                newFss.remove(s2);

                return ImmutableSet.copyOf(newFss);
            }).collect(Collectors.toSet());

            // update final state sets
            finalStateSets.clear();
            finalStateSets.addAll(newFinalStateSets);
        }
    }
}
