
package edu.univ.software.verification.model.fa;

import edu.univ.software.verification.model.AutomatonState;
import java.util.Objects;

/**
 * Represents single ordinary automaton state
 * 
 * @author Oksana
 */
public class BasicState implements AutomatonState {
    /**
     * Controls whether state is initial (starting)
     */
    protected boolean initial = DEFAULT_INITIAL;
    
    /**
     * Unique state identifier
     */
    protected String label;
        
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public BasicState() {}
    
    public BasicState(String label) {
        this(label, DEFAULT_INITIAL);
    }
    
    public BasicState(String label, boolean initial) {
        this.label = label;
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
        int hash = 5;
        
        hash = 59 * hash + Objects.hashCode(this.label);
        
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
        return "BasicState{" + "label=" + label + ", initial=" + initial + '}';
    }
    //</editor-fold>
}
