
package edu.univ.software.verification.model;

import com.google.common.collect.Table;

import java.util.Map;
import java.util.Set;

/**
 * Abstract finite automaton representation
 * 
 * @param <T> automaton transition symbol type
 * @author arthur
 */
public interface Automaton<T> {
    /**
     * Retrieves automaton state by it's label
     * 
     * @param label unique state identifier
     * @return corresponding state if one is present, null otherwise
     */
    AutomatonState getState(String label);
    
    /**
     * Checks for automaton state presence
     * 
     * @param label unique state identifier
     * @return whether state with given label is present
     */
    boolean hasState(String label);
    
    /**
     * Get all automaton state mappings
     * 
     * @return list of automaton states
     */
    Set<AutomatonState> getStates();
    
    /**
     * Get initial automaton states
     * 
     * @return set of all initial states 
     */
    Set<AutomatonState> getInitialStates();
    
    /**
     * Gets outgoing transitions for specified state 
     * 
     * @param from transition starting state
     * @return mapping of ending states to transition symbols
     */
    Map<String, Set<T>> getTransitionsFrom(String from);
    
    /**
     * Gets incoming transitions for specified state
     * WARN: very inefficient, use with care
     * 
     * @param to transition ending state
     * @return mapping of starting states to transition symbols
     */
    Map<String, Set<T>> getTransitionsTo(String to);
    
    /**
     * Gets transition symbols for particular pair of states
     * 
     * @param from starting state
     * @param to ending state
     * 
     * @return transition symbols if transition is defined, null otherwise
     */
    Set<T> getTransitionSymbols(String from, String to);
    
    /**
     * Checks for transition presence between a pair of states
     * 
     * @param from starting state
     * @param to ending state
     * 
     * @return whether transition is defined
     */
    boolean hasTransition(String from, String to);
    
    /**
     * Gets all automaton transition mappings
     * 
     * @return table of automaton transitions
     */
    Table<String, String, Set<T>> getTransitions();
    
    interface Builder<T> {
        // no methods yet
    }
}
