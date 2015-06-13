package edu.univ.software.verification.manager;

import com.google.common.collect.ImmutableSet;
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

    private KripkeStructure kripkeStructure;
    private BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem;
    private LtlFormula specification;
    private MullerAutomaton<Set<Atom>> mullerAutomatonForSpecification;
    private BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification;
    private BuchiAutomaton<Set<Atom>> productResult;
    private boolean confirmed;
    private ImmutableSet<String> counterExamples;

    public VerificationResultBuilder withKripkeStructure(KripkeStructure kripkeStructure) {
        this.kripkeStructure = kripkeStructure;
        return this;
    }

    public VerificationResultBuilder withBuchiAutomatonForSystem(BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem) {
        this.buchiAutomatonForSystem = buchiAutomatonForSystem;
        return this;
    }

    public VerificationResultBuilder withSpecification(LtlFormula specification) {
        this.specification = specification;
        return this;
    }

    public VerificationResultBuilder withMullerAutomatonForSpecification(MullerAutomaton<Set<Atom>> mullerAutomatonForSpecification) {
        this.mullerAutomatonForSpecification = mullerAutomatonForSpecification;
        return this;
    }

    public VerificationResultBuilder withBuchiAutomatonForSpecification(BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification) {
        this.buchiAutomatonForSpecification = buchiAutomatonForSpecification;
        return this;
    }

    public VerificationResultBuilder withProductResult(BuchiAutomaton<Set<Atom>> productResult) {
        this.productResult = productResult;
        return this;
    }

    public VerificationResultBuilder withAnswer(boolean answer) {
        this.confirmed = answer;
        return this;
    }

    public VerificationResultBuilder withCounterExamples(ImmutableSet<String> counterExamples) {
        this.counterExamples = counterExamples;
        return this;
    }

    public VerificationResultBuilder withCounterExamples(Set<String> counterExamples) {
        this.counterExamples = ImmutableSet.copyOf(counterExamples);
        return this;
    }

    public VerificationResult build() {
        return new VerificationResult(
                kripkeStructure,
                buchiAutomatonForSystem,
                specification,
                mullerAutomatonForSpecification,
                buchiAutomatonForSpecification,
                productResult,
                confirmed,
                counterExamples);
    }
}
