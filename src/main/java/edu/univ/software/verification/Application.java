
package edu.univ.software.verification;

import com.google.common.collect.ImmutableList;

import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.kripke.BasicStructure;
import edu.univ.software.verification.model.ltl.LtlAtom;
import edu.univ.software.verification.model.ltl.LtlBinaryOp;
import edu.univ.software.verification.model.ltl.LtlUnaryOp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arthur
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    
    private static void kripkeStructureDemo() {
        // demonstrates creation of kripke model for p. 265
        KripkeStructure ks = BasicStructure.builder()
                .withState("0", ImmutableList.of(LtlAtom.forName("p"), LtlAtom.forName("q")))
                .withState("1", ImmutableList.of(LtlAtom.forName("r"), LtlAtom.forName("q")))
                .withState("2", ImmutableList.of(LtlAtom.forName("p"), LtlAtom.forName("u")))
                .withState("3", ImmutableList.of(LtlAtom.forName("r"), LtlAtom.forName("u")))
                .withState("4", ImmutableList.of(LtlAtom.forName("w"), LtlAtom.forName("q")))
                .withState("5", ImmutableList.of(LtlAtom.forName("r"), LtlAtom.forName("u")))
                .withState("6", ImmutableList.of(LtlAtom.forName("p"), LtlAtom.forName("z")))
                .withState("7", ImmutableList.of(LtlAtom.forName("w"), LtlAtom.forName("u")))
                .withState("8", ImmutableList.of(LtlAtom.forName("r"), LtlAtom.forName("z")))
                .withTransition("0", "0")
                .withTransition("0", "1")
                .withTransition("0", "2")
                .withTransition("1", "3")
                .withTransition("1", "4")
                .withTransition("2", "5")
                .withTransition("2", "6")
                .withTransition("3", "7")
                .withTransition("4", "7")
                .withTransition("5", "8")
                .withTransition("6", "8")
                .withTransition("7", "2")
                .withTransition("8", "1")
                .build();
        
        logger.info("---Kripke structure test---");
        logger.info("Edge between {} and {}: {}", 0, 2, ks.hasTransition("0", "2"));
        logger.info("Edge between {} and {}: {}", 3, 8, ks.hasTransition("3", "8"));
        logger.info("---------------------------");
    }
    
    private static void ltlInvertionDemo() {
        LtlAtom atomB = new LtlAtom("B");
        LtlAtom atomA = new LtlAtom("A");
        LtlAtom atomZero = LtlAtom._0;
        LtlAtom atomOne = LtlAtom._1;
        LtlBinaryOp implication = LtlBinaryOp.build(LtlBinaryOp.BinaryOp.IMPL,atomA,atomB);
        LtlBinaryOp union = LtlBinaryOp.build(LtlBinaryOp.BinaryOp.OR,atomZero,atomOne);
        LtlBinaryOp release = LtlBinaryOp.build(LtlBinaryOp.BinaryOp.R,implication,union);
        LtlUnaryOp f = LtlUnaryOp.build(LtlUnaryOp.UnaryOp.F,release);
        
        logger.info("---LTL invertion test---");
        logger.info("Formula: {}", f);
        logger.info("Inverted: {}", f.invert());
        logger.info("Inverted & normalized: {}", f.invert().normalized());
        logger.info("------------------------");
    }
    
    public static void main(String[] args) {
        kripkeStructureDemo();
        ltlInvertionDemo();
    }
}
