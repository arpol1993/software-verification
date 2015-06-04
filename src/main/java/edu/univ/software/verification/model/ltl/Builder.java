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
    }
    
    private class UnaryOpStub implements OpStub {
        
        private OpStub operand;
        private UnaryOp.OpType type;
        
        public UnaryOpStub(UnaryOp.OpType type) {
            this.type = type;
        }
        
        public void setOperand(OpStub operand) {
            this.operand = operand;
        }
        
        @Override
        public LtlFormula build() {
            return UnaryOp.build(type, operand.build());
        }
        
    }
    
    private class BinaryOpStub implements OpStub {
        
        private OpStub left, right;
        private BinaryOp.OpType type;
        
        public void setLeft(OpStub left) {
            this.left = left;
        }
        
        public void setRight(OpStub right) {
            this.left = right;
        }
        
        public void setType(BinaryOp.OpType type) {
            this.type = type;
        }
        
        @Override
        public LtlFormula build() {
            return BinaryOp.build(type, left.build(), right.build());
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
    
    public Builder startBinary() {
        BinaryOpStub stub = new BinaryOpStub();
        
        if (root == null) {
            root = stub;
        }
        
        depthOrder.push(stub);
        current++;
        
        return this;
    }
    
    public Builder withUnary(UnaryOp.OpType type) {
        if (root == null) {
            root = new UnaryOpStub(type);
        }
        
        return this;
    }
    
    public Builder withAtom(Atom.AtomType atype, String name) {
        if (root == null) {
            root = new AtomOpStub(atype, name);
        }
        
        return this;
    }
    
    public Builder withBinaryOpType(BinaryOp.OpType op) throws InvalidBuilderUsage {
        
        if (current < 0) {
            throw new InvalidBuilderUsage("Invalid builder usage order!");
        }
        
        return this;
    }
    
    public LtlFormula build() {
        if (root == null) {
            return Atom._0;
        } else {
            return root.build();
        }
    }
}
