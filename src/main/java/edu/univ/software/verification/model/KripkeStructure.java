
package edu.univ.software.verification.model;

import edu.univ.software.verification.model.ltl.Atom;

import java.util.Collection;
import java.util.Set;

/**
 * Kripke structure representation
 *
 * @author arthur
 */
public interface KripkeStructure {
    
    /**
     * Gets kripke structure state by it's label
     * 
     * @param label state identifier
     * @return corresponding state if present, null otherwise
     */
    KripkeState getState(String label);
    
    /**
     * Retrieves all kripke structure states
     * 
     * @return set of present states
     */
    Set<KripkeState> getStates();
    
    /**
     * Check for transition presence between <code>from</code>
     * and <code>to</code> states
     * 
     * @param from transition start
     * @param to transition end
     * 
     * @return whether transition is present
     */
    boolean hasTransition(String from, String to);
    
    /**
     * Utilitarian builder for Kripke structure
     */
    interface Builder {
        Builder withState(String label);
        Builder withState(String label, Collection<? extends Atom> atoms);
        Builder withState(String label, Collection<? extends Atom> atoms, boolean initial);
        Builder withTransition(String from, String to);
        KripkeStructure build();
    }
}