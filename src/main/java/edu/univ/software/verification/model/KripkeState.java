
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
    public static final boolean DEFAULT_INITIAL = false;
    
    /**
     * Default atoms set
     */
    public static final Set<Atom> DEFAULT_ATOMS = ImmutableSet.of();
    
    /**
     * Unique state identifier
     * 
     * @return kripke state label
     */
    public String getLabel();
    
    /**
     * Set of atomic expressions, that are true in this state
     * 
     * @return set of true atom expressions
     */
    public Set<Atom> getAtoms();
    
    /**
     * Checks whether state is initial
     * 
     * @return true if state is initial, false otherwise
     */
    public boolean isInitial();
}
