package edu.univ.software.verification.model.ltl;

import edu.univ.software.verification.exceptions.InvalidBuilderUsage;
import edu.univ.software.verification.model.LtlFormula;

import java.util.LinkedList;

/**
 *
 * @author Pocomaxa
 */
public class Builder {

    private interface OpStub {

        public LtlFormula build();

        public boolean tryPassOperand(OpStub op);

        public boolean isCompleted();
    }

    //<editor-fold defaultstate="collapsed" desc="Stubs implementations">
    private class AtomOpStub implements OpStub {

        private Atom.AtomType type;
        private String name;

        public AtomOpStub(Atom.AtomType type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public LtlFormula build() {
            switch (type) {
                case VAR:
                    return Atom.forName(name);
                case _0:
                    return Atom._0;
                case _1:
                    return Atom._1;
                default:
                    throw new AssertionError(type.name());

            }
        }

        @Override
        public boolean tryPassOperand(OpStub op) {
            return false;
        }

        @Override
        public boolean isCompleted() {
            return true;
        }
    }

    private class UnaryOpStub implements OpStub {

        private OpStub operand;
        private UnaryOp.OpType type;

        public UnaryOpStub(UnaryOp.OpType type) {
            this.type = type;
        }

        @Override
        public LtlFormula build() {
            return UnaryOp.build(type, operand.build());
        }

        @Override
        public boolean tryPassOperand(OpStub op) {
            if (operand == null) {
                this.operand = op;
                return true;
            }

            return false;
        }

        @Override
        public boolean isCompleted() {
            return operand != null;
        }

    }

    private class BinaryOpStub implements OpStub {

        private int current;
        private OpStub left, right;
        private BinaryOp.OpType type;

        public BinaryOpStub() {
            current = 0;
        }

        public void setType(BinaryOp.OpType type) throws InvalidBuilderUsage {
            if (current != 1) {
                throw new InvalidBuilderUsage("Invalid binary operator initializing order!");
            }

            this.type = type;
            current++;
        }

        @Override
        public LtlFormula build() {
            return BinaryOp.build(type, left.build(), right.build());
        }

        @Override
        public boolean tryPassOperand(OpStub op) {
            if (current != 0 && current != 2) {
                return false;
            }

            if (current == 0) {
                left = op;
                current++;
                return true;
            }

            right = op;
            current++;
            return true;
        }

        @Override
        public boolean isCompleted() {
            return current > 2;
        }
    }
    //</editor-fold>

    private OpStub root;

    private LinkedList<OpStub> depthOrder;
    private int current;

    public Builder() {

        depthOrder = new LinkedList<>();
        current = -1;
    }  

    public Builder withBinary() {
        BinaryOpStub stub = new BinaryOpStub();

        if (root == null) {
            root = stub;
        }

        depthOrder.offer(stub);
        current++;

        return this;
    }

    public Builder withUnary(UnaryOp.OpType type) {
        UnaryOpStub stub = new UnaryOpStub(type);

        if (root == null) {
            root = stub;
        }

        depthOrder.offer(stub);
        current++;

        return this;
    }

    public Builder placeAtom(Atom.AtomType atype, String name) throws InvalidBuilderUsage {
        AtomOpStub stub = new AtomOpStub(atype, name);
        if (root == null) {
            root = stub;
            return this;
        }

        if (depthOrder.get(current).isCompleted()) {
            throw new InvalidBuilderUsage("Current operator is already finished!");
        }

        depthOrder.get(current).tryPassOperand(stub);

        //Push tree if operator is completed
        if (depthOrder.get(current).isCompleted()) {
            pushTree();
        }

        return this;
    }

    public Builder placeBinaryOperator(BinaryOp.OpType op) throws InvalidBuilderUsage {

        if (current < 0) {
            throw new InvalidBuilderUsage("Invalid builder usage order!");
        }

        if (!(depthOrder.get(current) instanceof BinaryOpStub)) {
            throw new InvalidBuilderUsage("Current operator is not binary!");
        }

        ((BinaryOpStub) depthOrder.get(current)).setType(op);

        return this;
    }

    public LtlFormula build() {
        if (root == null) {
            return Atom._0;
        } else {
            return root.build();
        }
    }

    private void pushTree() throws InvalidBuilderUsage {
        current--;
        
        if (current >= 0) {
            if (!depthOrder.get(current).tryPassOperand(depthOrder.pollLast())) {
                throw new InvalidBuilderUsage("Invalid formula structure!");
            }
            
            if (depthOrder.get(current).isCompleted()) {
                pushTree();
            }
        }
    }
}
