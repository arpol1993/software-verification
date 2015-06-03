package edu.univ.software.verification.model.ltl;


/**
 *
 * @author hp
 */
public class LtlInvertionExample {
    public static void main(String[] args) {
        LtlAtom atomB = new LtlAtom("B");
        LtlAtom atomA = new LtlAtom("A");
        LtlAtom atomZero = LtlAtom._0;
        LtlAtom atomOne = LtlAtom._1;
        LtlBinaryOp intersection = LtlBinaryOp.build(LtlBinaryOp.BinaryOp.AND,atomA,atomB);
        LtlBinaryOp union = LtlBinaryOp.build(LtlBinaryOp.BinaryOp.OR,atomZero,atomOne);
        LtlBinaryOp release = LtlBinaryOp.build(LtlBinaryOp.BinaryOp.R,intersection,union);
        LtlUnaryOp f = LtlUnaryOp.build(LtlUnaryOp.UnaryOp.F,release);
        System.out.println(f);
        System.out.println(f.invert());
    }
    
}
