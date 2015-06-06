
package edu.univ.software.verification.model;

import java.io.Serializable;

/**
 * Ordinary automaton state representation
 * 
 * @param <T> type of extra state data
 * @author Oksana
 */
public interface AutomatonState<T extends Serializable> {
    /**
     * Default state initial type
     */
    public static final boolean DEFAULT_INITIAL = false;
    
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
}
