
package edu.univ.software.verification.model;

public interface LtlFormula extends Cloneable {
    /**
     * Check the syntax equality of two formulas
     * @param o other formula
     * @return result of the check
     */
    @Override
    public boolean equals(Object o);

    /**
     * Get the hash code of the formula
     */
    @Override
    public int hashCode();
    
    /**
     * Check wether this formula is atomic preposition or its negation
     * @return result of the check
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




