package edu.univ.software.verification.model.fa;

import com.google.common.collect.Sets;
import edu.univ.software.verification.model.AutomatonState;
import edu.univ.software.verification.model.MullerAutomaton;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;


public class BasicMullerAutomatonTest {

    private MullerAutomaton<String> createMullerAutomation() {

        MullerAutomaton<String> mullerAutomaton = BasicMullerAutomaton.<String>builder()
                .withState("1", true)
                .withStates("2")
                .withStates("3")
                .withTransition("1", "2", Sets.newHashSet("a", "b"))
                .withTransition("2", "2", "a")
                .withTransition("2", "3", "b")
                .withTransition("3", "3", "b")
                .withTransition("3", "2", "a")
                .withFinalStateSet("3")
                .build();
        return mullerAutomaton;
    }

    @Test
    public void testBuilder() {
        MullerAutomaton<String> automata = createMullerAutomation();
        Set<Set<String>> finalStates = new HashSet<>();
        Set<String> state = new HashSet<>();
        state.add("3");
        finalStates.add(state);
        Set<AutomatonState> initialStates = new HashSet<>();
        initialStates.add(new BasicState("1"));
        Set<AutomatonState> allStates = new HashSet<>();
        allStates.add(new BasicState("1"));
        allStates.add(new BasicState("2"));
        allStates.add(new BasicState("3"));

        Assert.assertNotNull(automata);
        Assert.assertEquals(finalStates, automata.getFinalStateSets());
        Assert.assertEquals(initialStates, automata.getInitialStates());
        Assert.assertEquals(allStates, automata.getStates());
    }

}