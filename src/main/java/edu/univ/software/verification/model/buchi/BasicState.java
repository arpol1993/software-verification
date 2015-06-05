/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.model.buchi;

import com.google.common.collect.ImmutableSet;
import edu.univ.software.verification.model.BuchiState;
import edu.univ.software.verification.model.BuchiTransition;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents single Buchi automaton state
 * 
 * @author Oksana
 */
public class BasicState implements BuchiState {

    /**
     * Controls whether state is initial (starting)
     */
    protected boolean initial = DEFAULT_INITIAL;
    
    /**
     * Controls whether state is final
     */
    protected boolean finishing = DEFAULT_FINAL;
    
    /**
     * Unique state identifier
     */
    protected String label;
    
    /**
     * Set of output transitions from this state
     */
    protected Set<BuchiTransition> transitions;
    
    public BasicState() {}
    
    public BasicState(String label) {
        this(label, DEFAULT_INITIAL, DEFAULT_FINAL);
    }
    
    public BasicState(String label, boolean initial) {
        this(label, initial, DEFAULT_FINAL);
    }
    
    public BasicState(String label, boolean initial, boolean finishing) {
        this.label = label;
        this.transitions = new LinkedHashSet<>();
        this.initial = initial;
        this.finishing = finishing;
    }
    
    public BasicState(String label, Set<BuchiTransition> transitions) {
        this(label, transitions, DEFAULT_INITIAL, DEFAULT_FINAL);
    }
    
    public BasicState(String label, Set<BuchiTransition> transitions, boolean initial) {
        this(label, transitions, initial, DEFAULT_FINAL);
    }
    
    public BasicState(String label, Set<BuchiTransition> transitions, boolean initial,
            boolean finishing) {
        this.label = label;
        this.transitions = transitions;
        this.initial = initial;
        this.finishing = finishing;
    }
    
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isInitial() {
        return initial;
    }

    @Override
    public boolean isFinishing() {
        return finishing;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public void setFinishing(boolean finishing) {
        this.finishing = finishing;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.initial ? 1 : 0);
        hash = 31 * hash + (this.finishing ? 1 : 0);
        hash = 31 * hash + Objects.hashCode(this.label);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BasicState other = (BasicState) obj;
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        return true;
    }

    @Override
    public Set<BuchiTransition> getTransitions() {
        return transitions;
    }
    
    public void setTransitions(Set<BuchiTransition> transitions) {
        this.transitions = transitions;
    }    
    
    @Override
    public void addTransition(BuchiTransition transition) {
        this.transitions.add(transition);
    }
}
