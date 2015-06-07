package edu.univ.software.verification.utils;

import com.google.common.collect.Table;
import edu.univ.software.verification.model.AutomatonState;

import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.KripkeState;
import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.fa.BasicBuchiAutomaton;
import edu.univ.software.verification.model.fa.BasicMullerAutomaton;
import edu.univ.software.verification.model.fa.BasicState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Set of automata utils such as convertion and cross product
 *
 * @author Pocomaxa
 */
public enum AutomataUtils {

    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(LtlUtils.class);

    private static class Circuit {

        private static List<Circuit> totalCircuits = new LinkedList<>();

        public static Circuit empty() {
            return new Circuit();
        }

        private final List<String> path;

        public Circuit() {
            path = new ArrayList<>();
        }

        private Circuit(List<String> path) {
            this.path = new ArrayList<>(path);
        }

        public Circuit enter(String newState) {
            Circuit cl = new Circuit(path);

            cl.path.add(newState);

            return cl;
        }

    }

    /**
     * Check wether specified Buchi automata accepts only empty language. Prints
     * counter expamples into the logfile
     *
     * @param buchi Buchi automaton to check
     * @return true if only empty, false otherwise
     */
    public boolean isEmptyLanguage(BuchiAutomaton<?> buchi) {

        //Some additional junk init (XXX: rewrite this)
        Circuit.totalCircuits.clear();

        //Stage 1. Find all circular routes that includes all final states
        Set<String> finalStates = buchi.getFinalStates();

        Set<String> acceptingCStates = new HashSet<>();
        buildRoute(buchi, finalStates, Circuit.empty().enter(finalStates.iterator().next()),
                finalStates.iterator().next(), acceptingCStates, true, true);
        
        //Any mathing circuit found
        if (Circuit.totalCircuits.isEmpty()) {
            return true;
        }

        //Stage 2. Find initial circuit for the some of accepting circuit state's set
        for (AutomatonState<?> ist : buchi.getInitialStates()) {
            Circuit route = buildRoute(buchi, acceptingCStates, Circuit.empty().enter(ist.getLabel()),
                    ist.getLabel(), null, false, false);
            if (route == null) {
                continue;
            }

            Circuit.totalCircuits.stream().filter((circuit) -> (circuit.path.contains(route.path.get(route.path.size() - 1)))).map((circuit) -> {
                StringBuilder counter = new StringBuilder("Counterexpample: L = ");
                printRoute(route, counter, buchi);
                counter.append('(');
                printRoute(circuit, counter, buchi);
                return counter;
            }).forEach((counter) -> {
                logger.info(counter.append(")").toString());
            });

            return false;

        }

        return true;
    }

    /**
     * Converts specified Buchi automaton into Generalized Buchi (Muller)
     * automaton
     *
     * @param <T> Type of special state's data
     * @param buchi Buchi automaton to convert into LGBA
     * @return LGBA (Muller) automaton
     */
    public <T extends Serializable> MullerAutomaton<T> convert(BuchiAutomaton<T> buchi) {
        MullerAutomaton<T> mullerAutomaton = BasicMullerAutomaton.<T>builder()
                .importStates(buchi.getStates())
                .importTransitions(buchi.getTransitions())
                .withFinalStateSet(buchi.getFinalStates())
                .build();

        return mullerAutomaton;
    }

    /**
     * Converts specified Generalized Buchi (Muller) automaton into Buchi
     * automaton (degeneralization algorythm)
     *
     * @param <T> Type of special state's data
     * @param muller LGBA (Muller) automaton to convert into Buchi
     * (non-deterministic)
     * @return Buchi (non-deterministic) automaton
     */
    public <T extends Serializable> BuchiAutomaton<T> convert(MullerAutomaton<T> muller) {
        BuchiAutomaton.Builder<T> builder = BasicBuchiAutomaton.builder();

        Set<Set<String>> finalStates = muller.getFinalStateSets();
        Set<String> addedStates = new LinkedHashSet<>();

        for (int i = 0; i < finalStates.size(); i++) {

            for (Table.Cell<String, String, Set<String>> trans : muller.getTransitions().cellSet()) {
                int j = i;
                if (finalIndexOf(finalStates, trans.getRowKey()) == i) {
                    j = (i + 1) % finalStates.size();
                }

                String first = String.format("(%s, %d)", trans.getRowKey(), i);
                String second = String.format("(%s, %d)", trans.getColumnKey(), j);

                if (!addedStates.contains(first)) {
                    BasicState<T> st = (BasicState<T>) muller.getState(trans.getRowKey());

                    builder.withState(first, i == 0 && st.isInitial(), st.getData());
                    addedStates.add(first);

                    if (finalIndexOf(finalStates, trans.getRowKey()) == 0) {
                        builder.withFinalState(first);
                    }
                }

                if (!addedStates.contains(second)) {
                    BasicState<T> st = (BasicState<T>) muller.getState(trans.getColumnKey());

                    builder.withState(second, i == 0 && st.isInitial(), st.getData());
                    addedStates.add(second);

                    if (finalIndexOf(finalStates, trans.getColumnKey()) == 0) {
                        builder.withFinalState(second);
                    }
                }

                builder.withTransition(first, second, trans.getValue());
            }
        }

        return builder.build();
    }

       /**
     * Converts Kripke structure into Generalized Buchi automaton
     *
     * @param <T> Type of special state's data
     * @param kripke Kripke structure to convert into Buchi automaton
     * @return Buchi automaton
     */
    public <T extends Serializable> BuchiAutomaton<T> convert(KripkeStructure kripke) {
        BuchiAutomaton.Builder<T> builder = BasicBuchiAutomaton.<T>builder();
        String initStateForBuchiAutomaton = "0";
        Integer stateCounter = 1;
        builder.withState(initStateForBuchiAutomaton, true).withFinalState(initStateForBuchiAutomaton);
        for (KripkeState state : kripke.getStates()) {
            builder.withState(stateCounter.toString())
                    .withFinalState(stateCounter.toString());
            if (state.isInitial()) {
                builder.withTransition(initStateForBuchiAutomaton, stateCounter.toString(), state.getAtoms().toString());
            }
            stateCounter++;
        }
        
        for (KripkeState stateFrom : kripke.getStates()) {
            for (KripkeState stateTo : kripke.getStates()) {
                if (kripke.hasTransition(stateFrom.getLabel(), stateTo.getLabel())) {
                    builder.withTransition(Integer.toString(Integer.parseInt(stateFrom.getLabel()) + 1), 
                            Integer.toString(Integer.parseInt(stateTo.getLabel()) + 1), 
                            stateTo.getAtoms().toString());
                }
            }
        }
        
        return builder.build();
    }
    
    private int finalIndexOf(Set<Set<String>> finalStates, String state) {
        int i = 0;

        for (Set<String> finalStateSet : finalStates) {
            if (finalStateSet.contains(state)) {
                return i;
            }

            i++;
        }

        return -1;
    }   

    private Circuit buildRoute(BuchiAutomaton<?> buchi,
            Set<String> target, Circuit cpath, String current,
            Set<String> totalVisited, boolean findCycle, boolean findTotal) {

        for (String dest : buchi.getTransitionsFrom(current).keySet()) {

            boolean isCycling = false, isClosed = false;
            if (cpath.path.contains(dest)) {
                isCycling = true;
                if (cpath.path.get(0).equals(dest)) {
                    isClosed = true;
                }
            } else {
                cpath = cpath.enter(dest);
            }

            if ((findCycle && isClosed && containsTarget(target, cpath.path))
                    || (!findTotal && containsTarget(target, cpath.path))) {

                if (findCycle && isClosed) {
                    cpath = cpath.enter(dest);
                }

                if (findTotal) {
                    union(totalVisited, cpath.path);
                }
                return cpath;
            }

            if (!isCycling) {
                Circuit cr = buildRoute(buchi, target, cpath, dest, totalVisited, findCycle, findTotal);
                if (cr != null) {
                    if (!findTotal) {
                        return cr;
                    } else {
                        Circuit.totalCircuits.add(cr);
                    }
                }
            }
        }

        return null;
    }
    
    private void printRoute(Circuit route, StringBuilder strbuilder, BuchiAutomaton<?> buchi) {
        for (int i = 0; i < route.path.size() - 1; i++) {
            strbuilder.append(buchi.getTransitionSymbols(route.path.get(i), route.path.get(i + 1)).iterator().next());
        }
    }
    
    private void union(Set<String> dest, List<String> source) {
        source.stream().forEach((src) -> {
            dest.add(src);
        });
    }

    private boolean containsTarget(Set<String> targets, List<String> path) {
        return targets.stream().anyMatch((target) -> (path.contains(target)));
    }

}
