
package edu.univ.software.verification.model;

import java.util.Set;

/**
 * Linear-time logic formula representations
 * @author Pocomaxa
 */
public interface LtlFormula extends Cloneable {
    /**
     * Check the syntax equality of two formulas
     * @param o other formula
     * @return result of the check
     */
    @Override
    boolean equals(Object o);

    /**
     * Get the hash code of the formula
     */
    @Override
    int hashCode();
    
    /**
     * Check whether this formula is atomic preposition or its negation
     * @return result of the check
     */
    boolean isAtomic();
    
    /**
     * Invert LTL formula
     * @return inverted LTL formula
     */
    LtlFormula invert();
    
    /**
     * Normalize LTL formula
     * @return normalized LTL formula
     */
    LtlFormula normalized();
    
    /**
     * Evaluate formula's value. Doesn't compute temporal logic modal operators
     * @param values map of concrete values of labeled variables
     * @return result of the formula evaluation
     */
    boolean evaluate(Set<String> values);
    
    /**
     * Fetch all used positive propositional symbols in formula
     * @param isPositive fetch negative or positive propositions if not null, all otherwise
     * @return Set of symbols
     */
    Set<String> getPropositions(Boolean isPositive);
}




