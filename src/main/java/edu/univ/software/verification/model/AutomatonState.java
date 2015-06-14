
package edu.univ.software.verification.model;

/**
 * Ordinary automaton state representation
 * 
 * @author Oksana
 */
public interface AutomatonState {
    /**
     * Default state initial type
     */
    boolean DEFAULT_INITIAL = false;
    
    /**
     * Unique state identifier
     * 
     * @return automaton state label
     */
    String getLabel();
    
    /**
     * Checks whether state is initial
     * 
     * @return true if state is initial, false otherwise
     */
    boolean isInitial();
}
