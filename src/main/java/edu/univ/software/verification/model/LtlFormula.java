
package edu.univ.software.verification.model;

public interface LtlFormula extends Cloneable {
    /**
     * Check the syntax equality of two formulas
     * @param o other formula
     * @return 
     */
    @Override
    public boolean equals(Object o);

    @Override
    /**
     * Get the hash code of the formula
     */
    public int hashCode();
    
    /**
     * Check wether this formula is atomic preposition or its negation
     */
    public boolean isAtomic();
    
    /**
     * Invert LTL formula
     * @return inverted LTL formula
     */
    public LtlFormula invert();
    
    /**
     * Normalize LTL formula
     * @return normalized LTL formula
     */
    public LtlFormula normalized();
    
    /**
     * Create clone of LTL formula
     * @return cloned LTL tree
     */
    public LtlFormula clone();
}




