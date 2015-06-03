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
        return opType == UnaryOp.NEG && operand instanceof LtlAtom;
    }

    @Override
    public LtlFormula invert() {
        switch(opType){
            case F : return build(UnaryOp.G, operand.invert());
            case G : return build(UnaryOp.F, operand.invert());
            case X : return build(UnaryOp.X, operand.invert());
            case NEG : return operand;
        }
        return this;
    }

    @Override
    public LtlFormula clone() {
        return build(opType, operand.clone());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.opType);
        hash = 53 * hash + Objects.hashCode(this.operand);
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
//        final LtlUnaryOp other = (LtlUnaryOp) obj;
//        if (this.opType != other.opType) {
//            return false;
//        }
//        if (!Objects.equals(this.operand, other.operand)) {
//            return false;
//        }
//        return true;
    }
    
    @Override
    public String toString() {
        return "LtlUnaryOp{" + "opType=" + opType + ", operand=" + operand.toString() + '}';
    }
    
}
