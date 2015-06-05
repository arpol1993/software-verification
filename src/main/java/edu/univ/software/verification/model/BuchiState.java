/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.model;

import java.util.Set;

/**
 * Buchi automaton state representation
 * 
 * @author Oksana
 */
public interface BuchiState {
    /**
     * Default state initial type
     */
    public static final boolean DEFAULT_INITIAL = false;
    
    /**
     * Default state final type
     */
    public static final boolean DEFAULT_FINAL = false;
    
    /**
     * Unique state identifier
     * 
     * @return automaton state label
     */
    public String getLabel();
    
    /**
     * Checks whether state is initial
     * 
     * @return true if state is initial, false otherwise
     */
    public boolean isInitial();
    
    /**
     * Checks whether state is final
     * 
     * @return true if state is final, false otherwise
     */
    public boolean isFinishing();
    
    /**
     * Set of output transitions from current state
     * 
     * @return transitions
     */
    public Set<BuchiTransition> getTransitions();
    
    /**
     * Add new output transition to this state
     * 
     * @param transition 
     */
    public void addTransition(BuchiTransition transition);
}
