
package edu.univ.software.verification.model;

import com.google.common.collect.ImmutableSet;
import edu.univ.software.verification.model.ltl.Atom;
import java.util.Set;

/**
 * Kripke structure state representation
 *
 * @author arthur
 */
public interface KripkeState {
    /**
     * Default state type
     */
    boolean DEFAULT_INITIAL = false;
    
    /**
     * Default atoms set
     */
    Set<Atom> DEFAULT_ATOMS = ImmutableSet.of();
    
    /**
     * Unique state identifier
     * 
     * @return kripke state label
     */
    String getLabel();
    
    /**
     * Set of atomic expressions, that are true in this state
     * 
     * @return set of true atom expressions
     */
    Set<Atom> getAtoms();
    
    /**
     * Checks whether state is initial
     * 
     * @return true if state is initial, false otherwise
     */
    boolean isInitial();
}
