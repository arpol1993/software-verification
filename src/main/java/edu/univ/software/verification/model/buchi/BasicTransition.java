/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.model.buchi;

import edu.univ.software.verification.model.BuchiState;
import edu.univ.software.verification.model.BuchiTransition;
import java.util.Objects;

/**
 * Represents single Buchi automaton transition
 * 
 * @author Oksana
 */
public class BasicTransition implements BuchiTransition {
    
    /**
     * Output state for this transition
     */
    protected BuchiState from;
    
    /**
     * Input state for this transition
     */
    protected BuchiState to;
    
    /**
     * Set of symbols for working current transition
     */
    protected Character label;

    public BasicTransition() {}
    
    public BasicTransition(BuchiState from, BuchiState to, Character label) {
        this.from = from;
        this.to = to;
        this.label = label;
    }
    
    @Override
    public BuchiState getFrom() {
        return from;
    }

    @Override
    public BuchiState getTo() {
        return to;
    }

    @Override
    public Character getLabel() {
        return label;
    }

    public void setFrom(BuchiState from) {
        this.from = from;
    }

    public void setTo(BuchiState to) {
        this.to = to;
    }

    public void setLabel(Character label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.from);
        hash = 97 * hash + Objects.hashCode(this.to);
        hash = 97 * hash + Objects.hashCode(this.label);
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
        final BasicTransition other = (BasicTransition) obj;
        if (!Objects.equals(this.from, other.from)) {
            return false;
        }
        if (!Objects.equals(this.to, other.to)) {
            return false;
        }
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        return true;
    }
    
}
