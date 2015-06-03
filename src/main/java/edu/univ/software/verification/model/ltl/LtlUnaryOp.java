/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.model.ltl;

import edu.univ.software.verification.model.LtlFormula;

/**
 *
 * @author Pocomaxa
 */
public class LtlUnaryOp implements LtlFormula {

    public static LtlUnaryOp build(UnaryOp opType, LtlFormula operand) {
        return new LtlUnaryOp(opType, operand);
    }

    public static enum UnaryOp {

        NEG, // logical negation
        X, // neXt
        G, // Globally (always)
        F // in Future (eventually)
    }
    private UnaryOp opType;
    private LtlFormula operand;
    

    private LtlUnaryOp(UnaryOp opType, LtlFormula operand) {
        this.opType = opType;
        this.operand = operand;
    }   
    
    /**
     * @return the opType
     */
    public UnaryOp getOpType() {
        return opType;
    }

    /**
     * @return the operand
     */
    public LtlFormula getOperand() {
        return operand;
    }

    /**
     * @param operand the operand to set
     */
    public void setOperand(LtlFormula operand) {
        this.operand = operand;
    }    
    
    @Override
    public boolean isAtomic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRedundant(boolean value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LtlFormula invert() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LtlFormula clone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
