package edu.univ.software.verification.manager;

import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.ltl.Atom;

import java.util.Set;

/**
 * Created by Daryna_Ragimova on 6/12/2015.
 */
public class VerificationResultBuilder {

    private VerificationResult result = new VerificationResult();

    public VerificationResultBuilder withKripkeStructure(KripkeStructure kripkeStructure) {
        result.setKripkeStructure(kripkeStructure);
        return this;
    }

    public VerificationResultBuilder withBuchiAutomatonForSystem(BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem) {
        result.setBuchiAutomatonForSystem(buchiAutomatonForSystem);
        return this;
    }

    public VerificationResultBuilder withSpecification(LtlFormula specification) {
        result.setSpecification(specification);
        return this;
    }

    public VerificationResultBuilder withMullerAutomatonForSpecification(MullerAutomaton<Set<Atom>> mullerAutomatonForSpecification) {
        result.setMullerAutomatonForSpecification(mullerAutomatonForSpecification);
        return this;
    }

    public VerificationResultBuilder withBuchiAutomatonForSpecification(BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification) {
        result.setBuchiAutomatonForSpecification(buchiAutomatonForSpecification);
        return this;
    }

    public VerificationResultBuilder withProductResult(BuchiAutomaton<Set<Atom>> productResult) {
        result.setProductResult(productResult);
        return this;
    }

    public VerificationResultBuilder withAnswer(boolean answer) {
        result.setConfirmed(answer);
        return this;
    }

    public VerificationResultBuilder withCounterExamples(Set<String> counterExamples) {
        result.setCounterExamples(counterExamples);
        return this;
    }

    public VerificationResult build() {
        return result;
    }
}
