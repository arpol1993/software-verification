
package edu.univ.software.verification.model.ltl;

import edu.univ.software.verification.model.LtlFormula;
import java.util.Objects;

/**
 *
 * @author Pocomaxa
 */
public class BinaryOp implements LtlFormula {

    public static BinaryOp build(OpType opType, LtlFormula opLeft, LtlFormula opRight) {
        return new BinaryOp(opType, opLeft, opRight);
    }

    public static enum OpType {

        OR, // logical or
        AND, // logical and
        IMPL,
        U, // Untill
        R
    }
    private final OpType opType;
    private final LtlFormula opLeft;
    private final LtlFormula opRight;

    private BinaryOp(OpType opType, LtlFormula opLeft, LtlFormula opRight) {
        this.opType = opType;
        this.opLeft = opLeft;
        this.opRight = opRight;
    }

    /**
     * @return the opType
     */
    public OpType getOpType() {
        return opType;
    }

    /**
     * @return the opLeft
     */
    public LtlFormula getOpLeft() {
        return opLeft;
    }

    /**
     * @return the opRight
     */
    public LtlFormula getOpRight() {
        return opRight;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public LtlFormula invert() {
        switch (opType) {
            case R:
                return build(OpType.U, opLeft.invert(), opRight.invert());
            case U:
                return build(OpType.R, opLeft.invert(), opRight.invert());
            case AND:
                return build(OpType.OR, opLeft.invert(), opRight.invert());
            case OR:
                return build(OpType.AND, opLeft.invert(), opRight.invert());
            case IMPL:
                return build(OpType.AND, opLeft.clone(), opRight.invert());
        }

        return this.clone();
    }

    @Override
    public LtlFormula normalized() {
        switch (opType) {
            case IMPL:
                return build(OpType.OR, opLeft.invert().normalized(), opRight.normalized());
            default:
                return this.clone();
        }
    }

    @Override
    public LtlFormula clone() {
        return this;
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

        final BinaryOp other = (BinaryOp) obj;
        if (this.opType != other.opType) {
            return false;
        }
        if (!Objects.equals(this.opLeft, other.opLeft)) {
            return false;
        }
        return Objects.equals(this.opRight, other.opRight);
    }

    @Override
    public String toString() {
        switch (opType) {
            case OR:
                return "(" + opLeft.toString() + " || " + opRight.toString() + ")";
            case AND:
                return "(" + opLeft.toString() + " && " + opRight.toString() + ")";
            case U:
                return "(" + opLeft.toString() + " U " + opRight.toString() + ")";
            case R:
                return "(" + opLeft.toString() + " R " + opRight.toString() + ")";
            case IMPL:
                return "(" + opLeft.toString() + " -> " + opRight.toString() + ")";
            default:
                throw new AssertionError(opType.name());

        }
        //return "LtlBinaryOp{" + "opType=" + opType + ", opLeft=" + opLeft.toString() + ", opRight=" + opRight.toString() + '}';
    }

}
