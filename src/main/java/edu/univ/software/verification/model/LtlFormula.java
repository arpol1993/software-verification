
package edu.univ.software.verification.model;

public interface LtlFormula extends Cloneable {
    @Override
    public boolean equals(Object o);

    @Override
    public int hashCode();
    
    public boolean isAtomic();
    
    public LtlFormula invert();
    public LtlFormula clone();
}




