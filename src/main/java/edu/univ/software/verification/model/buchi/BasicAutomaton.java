/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.model.buchi;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.BuchiState;
import edu.univ.software.verification.model.BuchiTransition;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Buchi automaton representation
 * 
 * @author Oksana
 */
public class BasicAutomaton implements BuchiAutomaton {

    protected Map<String, BuchiState> states = ImmutableMap.of();
    
    public BasicAutomaton() {}
    
    public BasicAutomaton(Map<String, BuchiState> states) {
        states = ImmutableMap.copyOf(states);
    }
    
    public static Builder builder() {
        return new BasicBuilder();
    }
    
    @Override
    public BuchiState getState(String label) {
        return states.get(label);
    }

    @Override
    public Set<BuchiState> getStates() {
        return ImmutableSet.copyOf(states.values());
    }

    @Override
    public Set<BuchiState> getFinishingStates() {
        Set<BuchiState> states = new LinkedHashSet<>();
        for (BuchiState state : this.states.values()) {
            if (state.isFinishing()) {
                states.add(state);
            }
        }
        return ImmutableSet.copyOf(states);
    }

    @Override
    public Set<BuchiState> getInitialStates() {
        Set<BuchiState> states = new LinkedHashSet<>();
        for (BuchiState state : this.states.values()) {
            if (state.isInitial()) {
                states.add(state);
            }
        }
        return ImmutableSet.copyOf(states);
    }

    @Override
    public boolean hasTransition(BuchiState from, BuchiState to) {
        Set<BuchiTransition> transitions = states.get(from.getLabel()).getTransitions();
        for (BuchiTransition transition : transitions) {
            if (transition.getTo().equals(to)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTransition(BuchiState from, Character x) {
        Set<BuchiTransition> transitions = states.get(from.getLabel()).getTransitions();
        for (BuchiTransition transition : transitions) {
            if (transition.getLabel().equals(x)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTransition(BuchiState from, BuchiState to, Character x) {
        Set<BuchiTransition> transitions = states.get(from.getLabel()).getTransitions();
        for (BuchiTransition transition : transitions) {
            if (transition.getTo().equals(to) && transition.getLabel().equals(x)) {
                return true;
            }
        }
        return false;
    }
    
    public static class BasicBuilder implements Builder {

        private final Map<String, BuchiState> states = new LinkedHashMap<>();
        
        private BasicBuilder() {}
        
        @Override
        public Builder withState(String label) {
            return withState(label, BasicState.DEFAULT_INITIAL, BasicState.DEFAULT_FINAL);
        }

        @Override
        public Builder withState(String label, boolean initial) {
            return withState(label, initial, BasicState.DEFAULT_FINAL);
        }

        @Override
        public Builder withState(String label, boolean initial, boolean finishing) {
            this.states.put(label, new BasicState(label, initial, finishing));
            
            return this;
        }

        @Override
        public Builder withTransition(BuchiState from, BuchiState to, Character x) {
            if (!this.states.containsKey(from.getLabel())) {
                this.states.put(from.getLabel(), from);
            }
            
            if (!this.states.containsKey(to.getLabel())) {
                this.states.put(to.getLabel(), to);
            }
            
            BuchiTransition transition = new BasicTransition(from, to, x);            
            from.addTransition(transition);
            
            return this;
        }

        @Override
        public BuchiAutomaton build() {
            return new BasicAutomaton(this.states);
        }
        
    }
    
}
