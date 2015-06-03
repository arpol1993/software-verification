/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.model.ltl;

import edu.univ.software.verification.model.LtlFormula;
import java.util.Objects;

/**
 *
 * @author Pocomaxa
 */
public class LtlBinaryOp implements LtlFormula {

    public static LtlBinaryOp build(BinaryOp opType, LtlFormula opLeft, LtlFormula opRight) {
        return new LtlBinaryOp(opType, opLeft, opRight);
    }

    public static enum BinaryOp {

        OR, // logical or
        AND, // logical and
        U, // Untill
        R
    }
    private BinaryOp opType;
    private LtlFormula opLeft;
    private LtlFormula opRight;

    private LtlBinaryOp(BinaryOp opType, LtlFormula opLeft, LtlFormula opRight) {
        this.opType = opType;
        this.opLeft = opLeft;
        this.opRight = opRight;
    }

    /**
     * @return the opType
     */
    public BinaryOp getOpType() {
        return opType;
    }

    /**
     * @return the opLeft
     */
    public LtlFormula getOpLeft() {
        return opLeft;
    }

    /**
     * @param opLeft the opLeft to set
     */
    public void setOpLeft(LtlFormula opLeft) {
        this.opLeft = opLeft;
    }

    /**
     * @return the opRight
     */
    public LtlFormula getOpRight() {
        return opRight;
    }

    /**
     * @param opRight the opRight to set
     */
    public void setOpRight(LtlFormula opRight) {
        this.opRight = opRight;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public LtlFormula invert() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LtlFormula clone() {
        return build(opType, opLeft.clone(), opRight.clone());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.opType);
        hash = 53 * hash + Objects.hashCode(this.opLeft);
        hash = 53 * hash + Objects.hashCode(this.opRight);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        throw new UnsupportedOperationException("Not supported yet.");
        //TODO: Proper equality check
//        final LtlBinaryOp other = (LtlBinaryOp) obj;
//        if (this.opType != other.opType) {
//            return false;
//        }
//        if (!Objects.equals(this.opLeft, other.opLeft)) {
//            return false;
//        }
//        if (!Objects.equals(this.opRight, other.opRight)) {
//            return false;
//        }
//        return true;
    }
}
