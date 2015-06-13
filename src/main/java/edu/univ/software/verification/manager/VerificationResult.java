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
public class VerificationResult {
    private final KripkeStructure kripkeStructure;
    private final BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem;
    private final LtlFormula specification;
    private final MullerAutomaton<Set<Atom>> mullerAutomatonForSpecification;
    private final BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification;
    private final BuchiAutomaton<Set<Atom>> productResult;
    private final boolean confirmed;
    private final ImmutableSet<String> counterExamples;

    public VerificationResult(KripkeStructure kripkeStructure,
                              BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem,
                              LtlFormula specification, MullerAutomaton<Set<Atom>> mullerAutomatonForSpecification,
                              BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification,
                              BuchiAutomaton<Set<Atom>> productResult,
                              boolean confirmed,
                              ImmutableSet<String> counterExamples) {
        this.kripkeStructure = kripkeStructure;
        this.buchiAutomatonForSystem = buchiAutomatonForSystem;
        this.specification = specification;
        this.mullerAutomatonForSpecification = mullerAutomatonForSpecification;
        this.buchiAutomatonForSpecification = buchiAutomatonForSpecification;
        this.productResult = productResult;
        this.confirmed = confirmed;
        this.counterExamples = counterExamples;
    }

    public KripkeStructure getKripkeStructure() {
        return kripkeStructure;
    }

    public BuchiAutomaton<Set<Atom>> getBuchiAutomatonForSystem() {
        return buchiAutomatonForSystem;
    }

    public LtlFormula getSpecification() {
        return specification;
    }

    public MullerAutomaton<Set<Atom>> getMullerAutomatonForSpecification() {
        return mullerAutomatonForSpecification;
    }

    public BuchiAutomaton<Set<Atom>> getBuchiAutomatonForSpecification() {
        return buchiAutomatonForSpecification;
    }

    public BuchiAutomaton<Set<Atom>> getProductResult() {
        return productResult;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public ImmutableSet<String> getCounterExamples() {
        return counterExamples;
    }
}
