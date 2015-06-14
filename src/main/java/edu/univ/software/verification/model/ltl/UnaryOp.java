package edu.univ.software.verification.model.ltl;

import edu.univ.software.verification.model.LtlFormula;

import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Pocomaxa
 */
public class UnaryOp implements LtlFormula {

    public static UnaryOp build(OpType opType, LtlFormula operand) {
        return new UnaryOp(opType, operand);
    }

    public enum OpType {

        NEG, // logical negation
        X, // neXt
        G, // Globally (always)
        F // in Future (eventually)
    }
    private final OpType opType;
    private final LtlFormula operand;

    private UnaryOp(OpType opType, LtlFormula operand) {
        this.opType = opType;
        this.operand = operand;
    }

    /**
     * @return the opType
     */
    public OpType getOpType() {
        return opType;
    }

    /**
     * @return the operand
     */
    public LtlFormula getOperand() {
        return operand;
    }

    @Override
    public boolean isAtomic() {
        return opType == OpType.NEG && operand instanceof Atom;
    }

    @Override
    public LtlFormula invert() {
        switch (opType) {
            case F:
                return build(OpType.G, operand.invert());
            case G:
                return build(OpType.F, operand.invert());
            case X:
                return build(OpType.X, operand.invert());
            case NEG:
                return operand;
        }

        return this.clone();
    }

    @Override
    public LtlFormula normalized() {
        switch (opType) {
            case G:
                return BinaryOp.build(BinaryOp.OpType.R, Atom._0, operand.normalized());
            case F:
                return BinaryOp.build(BinaryOp.OpType.U, Atom._1, operand.normalized());
            default:
                return UnaryOp.build(opType, operand.normalized());
        }
    }

    @Override
    public boolean evaluate(Set<String> values) {
        if (opType != OpType.NEG) {
            throw new IllegalArgumentException("Invalid unary operator!");
        }

        return !operand.evaluate(values);
    }

    @Override
    public Set<String> getPropositions(Boolean isPositive) {
        return (isPositive != null && !isPositive && opType == OpType.NEG && operand instanceof Atom)
                ? operand.getPropositions(true) : operand.getPropositions(isPositive);
    }

    @Override
    public LtlFormula clone() {
        return this;
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

        final UnaryOp other = (UnaryOp) obj;

        return this.opType == other.opType && Objects.equals(this.operand, other.operand);
    }

    @Override
    public String toString() {
        switch (opType) {
            case NEG:
                return "!" + operand.toString();
            case X:
                return "X" + operand.toString();
            case G:
                return "G" + operand.toString();
            case F:
                return "F" + operand.toString();
            default:
                throw new AssertionError(opType.name());

        }
    }
}
