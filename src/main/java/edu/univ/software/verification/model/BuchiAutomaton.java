
package edu.univ.software.verification.model;

import java.util.Set;

/**
 * Buchi automaton representation
 * 
 * @author Oksana
 */
public interface BuchiAutomaton {
    
    /**
     * Gets buchi automaton state by it's label
     * 
     * @param label state identifier
     * @return corresponding state if present, null otherwise
     */
    public BuchiState getState(String label);
    
    /**
     * Retrieves all buchi automaton states
     * 
     * @return set of present states
     */
    public Set<BuchiState> getStates();
    
    /**
     * Retrieves all finishing buchi automaton states
     * 
     * @return set of finishing states
     */
    public Set<BuchiState> getFinishingStates();
    
    /**
     * Retrieves all initial buchi automaton states
     * 
     * @return set of initial states
     */
    public Set<BuchiState> getInitialStates();
    
    /**
     * Check for transition presence between <code>from</code>
     * and <code>to</code> states
     * 
     * @param from transition start
     * @param to transition end
     * 
     * @return whether transition is present
     */
    public boolean hasTransition(BuchiState from, BuchiState to);
    
    /**
     * Check for transition presence from <code>from</code> state
     * by character <code>x</code>
     * 
     * @param from transition start
     * @param x transition label
     * 
     * @return whether transition is present
     */
    public boolean hasTransition(BuchiState from, Character x);
    
    /**
     * Check for transition presence between <code>from</code>
     * and <code>to</code> states by character <code>x</code>
     * 
     * @param from transition start
     * @param to transition end
     * @param x transition label
     * 
     * @return whether transition is present
     */
    public boolean hasTransition(BuchiState from, BuchiState to, Character x);
    
    /**
     * Utilitarian builder for Buchi automaton
     */
    public static interface Builder {
        public Builder withState(String label);
        public Builder withState(String label, boolean initial);
        public Builder withState(String label, boolean initial, boolean finishing);
        public Builder withTransition(BuchiState from, BuchiState to, Character x);
        public BuchiAutomaton build();
    }
}
