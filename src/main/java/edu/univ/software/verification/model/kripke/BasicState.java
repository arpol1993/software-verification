
package edu.univ.software.verification.model.kripke;

import com.google.common.collect.ImmutableSet;

import edu.univ.software.verification.model.KripkeState;
import edu.univ.software.verification.model.ltl.LtlAtom;

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
    protected Set<LtlAtom> atoms = DEFAULT_ATOMS;
    
    /**
     * Unique formula identifier
     */
    protected String label;
    
    //<editor-fold defaultstate="collapsed" desc="Controllers">
    public BasicState() {}
    
    public BasicState(String label) {
        this(label, DEFAULT_ATOMS);
    }
    
    public BasicState(String label, Collection<? extends LtlAtom> atoms) {
        this(label, atoms, DEFAULT_INITIAL);
    }
    
    public BasicState(String label, Collection<? extends LtlAtom> atoms, boolean intital) {
        this.label = label;
        this.atoms = ImmutableSet.copyOf(atoms);
        this.initial = intital;
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
    public Set<LtlAtom> getAtoms() {
        return atoms;
    }
    
    public void setAtoms(Set<LtlAtom> atoms) {
        this.atoms = atoms;
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