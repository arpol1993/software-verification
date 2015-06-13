package edu.univ.software.verification.manager;

import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.ltl.Atom;
import edu.univ.software.verification.utils.AutomataUtils;
import edu.univ.software.verification.utils.LtlUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Daryna_Ragimova on 6/12/2015.
 */
public class ApplicationRunner {

    public static VerificationResult verify(KripkeStructure kripkeStructure, LtlFormula ltlFormula) {
        BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem = AutomataUtils.INSTANCE.convert(kripkeStructure);
        MullerAutomaton<Set<Atom>> ma = LtlUtils.INSTANCE.convertToAutomata(ltlFormula.invert().normalized());
        BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification = AutomataUtils.INSTANCE.convert(ma);

        BuchiAutomaton<Set<Atom>> productResult = AutomataUtils.INSTANCE.product(buchiAutomatonForSystem,
                buchiAutomatonForSpecification);
        Set<String> counterexamples = new HashSet<>();
        boolean result = AutomataUtils.INSTANCE.emptinessCheck(productResult, counterexamples);

        VerificationResultBuilder builder = new VerificationResultBuilder();
        builder.withKripkeStructure(kripkeStructure)
                .withBuchiAutomatonForSystem(buchiAutomatonForSystem)
                .withSpecification(ltlFormula)
                .withMullerAutomatonForSpecification(ma)
                .withBuchiAutomatonForSpecification(buchiAutomatonForSpecification)
                .withProductResult(productResult)
                .withAnswer(result)
                .withCounterExamples(counterexamples);
        return builder.build();
    }



}
