package edu.univ.software.verification.model.fa;

import edu.univ.software.verification.model.AutomatonState;
import edu.univ.software.verification.model.BuchiAutomaton;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BasicBuchiAutomatonTest {

    private BuchiAutomaton<String> createBuchiAutomation() {
        BuchiAutomaton<String> buchiAutomaton = BasicBuchiAutomaton.<String>builder()
                .withState("1", true)
                .withStates("2")
                .withStates("3")
                .withTransition("1", "2", "a", "b")
                .withTransition("2", "2", "a")
                .withTransition("2", "3", "b")
                .withTransition("3", "3", "b")
                .withTransition("3", "2", "a")
                .withFinalState("3")
                .build();
        return buchiAutomaton;
    }

    @Test
    public void testBuilder() {
        BuchiAutomaton<String> automata = createBuchiAutomation();
        Set<String> finalStates = new HashSet<>();
        finalStates.add("3");
        Set<AutomatonState<String>> initialStates = new HashSet<>();
        initialStates.add(new BasicState<>("1"));
        Set<AutomatonState<String>> allStates = new HashSet<>();
        allStates.add(new BasicState<>("1"));
        allStates.add(new BasicState<>("2"));
        allStates.add(new BasicState<>("3"));

        Assert.assertNotNull(automata);
        Assert.assertEquals(finalStates, automata.getFinalStates());
        Assert.assertEquals(initialStates, automata.getInitialStates());
        Assert.assertEquals(allStates, automata.getStates());
    }

}