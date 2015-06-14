
package edu.univ.software.verification.model.kripke;

import com.google.common.collect.ImmutableSet;

import edu.univ.software.verification.model.KripkeState;
import edu.univ.software.verification.model.ltl.Atom;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Represents single Kripke structure state
 *
 * @author arthur
 */
public class BasicState implements KripkeState {
    /**
     * Controls whether state is initial (starting)
     */
    protected boolean initial = DEFAULT_INITIAL;
    
    /**
     * Set of atomic formulas, that are true in this state
     */
    protected Set<Atom> atoms = DEFAULT_ATOMS;
    
    /**
     * Unique formula identifier
     */
    protected String label;
    
    //<editor-fold defaultstate="collapsed" desc="Controllers">
    public BasicState() {}
    
    public BasicState(String label) {
        this(label, DEFAULT_ATOMS);
    }
    
    public BasicState(String label, Collection<? extends Atom> atoms) {
        this(label, atoms, DEFAULT_INITIAL);
    }
    
    public BasicState(String label, Collection<? extends Atom> atoms, boolean initial) {
        this.label = label;
        this.atoms = ImmutableSet.copyOf(atoms);
        this.initial = initial;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    @Override
    public boolean isInitial() {
        return initial;
    }
    
    public void setInitial(boolean initial) {
        this.initial = initial;
    }
    
    @Override
    public Set<Atom> getAtoms() {
        return atoms;
    }
    
    public void setAtoms(Set<Atom> atoms) {
        this.atoms = ImmutableSet.copyOf(atoms);
    }
    
    @Override
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 3;
        
        hash = 79 * hash + Objects.hashCode(this.label);
        
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (!(o instanceof BasicState)) {
            return false;
        }
        
        final BasicState other = (BasicState) o;
        
        return Objects.equals(this.label, other.label);
    }
    
    @Override
    public String toString() {
        return "BasicState{" + "label=" + label + ", initial=" + initial + ", atoms=" + atoms + '}';
    }
    //</editor-fold>
}
