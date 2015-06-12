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
public class VerificationResult {
    private KripkeStructure kripkeStructure;
    private BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem;
    private LtlFormula specification;
    private MullerAutomaton<Set<Atom>> mullerAutomatonForSpecification;
    private BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification;
    private BuchiAutomaton<Set<Atom>> productResult;
    private boolean confirmed;
    private Set<String> counterExamples;

    public KripkeStructure getKripkeStructure() {
        return kripkeStructure;
    }

    public void setKripkeStructure(KripkeStructure kripkeStructure) {
        this.kripkeStructure = kripkeStructure;
    }

    public BuchiAutomaton<Set<Atom>> getBuchiAutomatonForSystem() {
        return buchiAutomatonForSystem;
    }

    public void setBuchiAutomatonForSystem(BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem) {
        this.buchiAutomatonForSystem = buchiAutomatonForSystem;
    }

    public LtlFormula getSpecification() {
        return specification;
    }

    public void setSpecification(LtlFormula specification) {
        this.specification = specification;
    }

    public MullerAutomaton<Set<Atom>> getMullerAutomatonForSpecification() {
        return mullerAutomatonForSpecification;
    }

    public void setMullerAutomatonForSpecification(MullerAutomaton<Set<Atom>> mullerAutomatonForSpecification) {
        this.mullerAutomatonForSpecification = mullerAutomatonForSpecification;
    }

    public BuchiAutomaton<Set<Atom>> getBuchiAutomatonForSpecification() {
        return buchiAutomatonForSpecification;
    }

    public void setBuchiAutomatonForSpecification(BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification) {
        this.buchiAutomatonForSpecification = buchiAutomatonForSpecification;
    }

    public BuchiAutomaton<Set<Atom>> getProductResult() {
        return productResult;
    }

    public void setProductResult(BuchiAutomaton<Set<Atom>> productResult) {
        this.productResult = productResult;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Set<String> getCounterExamples() {
        return counterExamples;
    }

    public void setCounterExamples(Set<String> counterExamples) {
        this.counterExamples = counterExamples;
    }
}
