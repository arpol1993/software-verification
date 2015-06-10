package edu.univ.software.verification.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import edu.univ.software.verification.model.AutomatonState;

import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.KripkeState;
import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.fa.BasicBuchiAutomaton;
import edu.univ.software.verification.model.fa.BasicMullerAutomaton;
import edu.univ.software.verification.model.ltl.Atom;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
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

        public static Circuit empty() {
            return new Circuit();
        }

        public static Circuit forState(String state) {
            return empty().enter(state);
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

        public String current() {
            return path.size() > 0 ? path.get(path.size() - 1) : null;
        }

        public Circuit clone() {
            return new Circuit(path);
        }
    }

    /**
     * Check wether specified Buchi automata accepts only empty language. Prints
     * ALL counter expamples into the logfile
     *
     * @param buchi Buchi automaton to check
     * @param counters Already initialized set where found counters added
     * @return true if only empty, false otherwise
     */
    public <T> boolean emptinessCheck(BuchiAutomaton<T> buchi, Set<String> counters) {

        //Stage 1. Find all initial circuits
        Table<String, String, Circuit> initialCircuits = HashBasedTable.create();
        buchi.getInitialStates().forEach((s) -> buildInitialTraces(buchi, Circuit.forState(s.getLabel()), initialCircuits));

        //Stage 2. Find all circular routes that includes final states
        boolean isEmpty = buchi.getFinalStates().stream().map((fin) -> {
            List<Circuit> acceptingCircuits = new LinkedList<>();
            buildAcceptingTraces(buchi, Circuit.forState(fin), acceptingCircuits);

            if (!acceptingCircuits.isEmpty() && initialCircuits.containsColumn(fin)) {

                initialCircuits.column(fin).forEach((start, initial) -> {
                    acceptingCircuits.forEach((acc) -> counters.add(printRoute(buchi, initial, acc)));
                });

                return false;
            } else {
                return true;
            }
        }).noneMatch((b) -> !b);

        return isEmpty;
    }

    /**
     * Converts specified Buchi automaton into Generalized Buchi (Muller)
     * automaton
     *
     * @param <T> Type of special state's data
     * @param buchi Buchi automaton to convert into LGBA
     * @return LGBA (Muller) automaton
     */
    public <T> MullerAutomaton<T> convert(BuchiAutomaton<T> buchi) {
        MullerAutomaton<T> mullerAutomaton = BasicMullerAutomaton.<T>builder()
                .withStates(buchi.getStates())
                .withTransitions(buchi.getTransitions())
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
    public <T> BuchiAutomaton<T> convert(MullerAutomaton<T> muller) {
        BuchiAutomaton.Builder<T> builder = BasicBuchiAutomaton.builder();

        Set<Set<String>> finalStates = muller.getFinalStateSets();
        Set<String> addedStates = new LinkedHashSet<>();

        for (int i = 0; i < finalStates.size(); i++) {

            for (Table.Cell<String, String, Set<T>> trans : muller.getTransitions().cellSet()) {
                int j = i;
                if (finalIndexOf(finalStates, trans.getRowKey()) == i) {
                    j = (i + 1) % finalStates.size();
                }

                String first = String.format("(%s, %d)", trans.getRowKey(), i);
                String second = String.format("(%s, %d)", trans.getColumnKey(), j);

                if (!addedStates.contains(first)) {
                    AutomatonState st = muller.getState(trans.getRowKey());

                    builder.withState(first, i == 0 && st.isInitial());
                    addedStates.add(first);

                    if (finalIndexOf(finalStates, trans.getRowKey()) == 0) {
                        builder.withFinalState(first);
                    }
                }

                if (!addedStates.contains(second)) {
                    AutomatonState st = muller.getState(trans.getColumnKey());

                    builder.withState(second, i == 0 && st.isInitial());
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
     * Converts Kripke structure into Buchi automaton
     *
     * @param <T> Type of special state's data
     * @param kripke Kripke structure to convert into Buchi automaton
     * @return Buchi automaton
     */
    public BuchiAutomaton<Set<Atom>> convert(KripkeStructure kripke) {
        BuchiAutomaton.Builder<Set<Atom>> builder = BasicBuchiAutomaton.<Set<Atom>>builder();
        String initStateForBuchiAutomaton = "0";
        Integer stateCounter = 1;
        builder.withState(initStateForBuchiAutomaton, true).withFinalState(initStateForBuchiAutomaton);
        for (KripkeState state : kripke.getStates()) {
            builder.withState(stateCounter.toString())
                    .withFinalState(stateCounter.toString());
            if (state.isInitial()) {
                builder.withTransition(initStateForBuchiAutomaton, stateCounter.toString(), state.getAtoms());
            }
            stateCounter++;
        }

        for (KripkeState stateFrom : kripke.getStates()) {
            for (KripkeState stateTo : kripke.getStates()) {
                if (kripke.hasTransition(stateFrom.getLabel(), stateTo.getLabel())) {
                    builder.withTransition(Integer.toString(Integer.parseInt(stateFrom.getLabel()) + 1),
                            Integer.toString(Integer.parseInt(stateTo.getLabel()) + 1),
                            stateTo.getAtoms());
                }
            }
        }

        return builder.build();
    }

    /**
     * Computes direct product of two Buchi automata
     *
     * @param <T> Type of special state's data
     * @param a first Buchi automaton
     * @param b second Buchi automaton
     * @return Buchi automaton with the result of the product
     */
    public <T> BuchiAutomaton<T> product(BuchiAutomaton<T> a, BuchiAutomaton<T> b) {
        return new DirectProduct<>(a, b).product();
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

    private void buildInitialTraces(BuchiAutomaton<?> buchi, Circuit cpath,
            Table<String, String, Circuit> allPathes) {

        String current = cpath.current();
        buchi.getTransitionsFrom(current).keySet().stream().forEach((dest) -> {
            Circuit npath = cpath.enter(dest);
            boolean isCycling = false;
            if (cpath.path.contains(dest)) {
                isCycling = true;
            }

            if (buchi.getFinalStates().contains(dest) && !allPathes.values().contains(npath)) {
                allPathes.put(cpath.path.get(0), dest, cpath.clone());
            }
            if (!isCycling) {
                buildInitialTraces(buchi, npath, allPathes);
            }
        });
    }

    private void buildAcceptingTraces(BuchiAutomaton<?> buchi, Circuit cpath,
            List<Circuit> allPathes) {

        String current = cpath.current();

        buchi.getTransitionsFrom(current).keySet().stream().forEach((dest) -> {
            Circuit npath = cpath.enter(dest);
            boolean isCycling = false, isClosed = false;
            if (cpath.path.contains(dest)) {
                isCycling = true;
                if (cpath.path.get(0).equals(dest)) {
                    isClosed = true;
                }
            }
            if (isClosed) {
                allPathes.add(npath.clone());
            } else if (!isCycling) {
                buildAcceptingTraces(buchi, npath, allPathes);
            }
        });
    }

    private String printRoute(BuchiAutomaton<?> buchi, Circuit initital, Circuit accpeting) {
        StringBuilder counter = new StringBuilder("Counterexpample: L = ");
        printRoutePart(initital, counter, buchi);
        counter.append('(');
        printRoutePart(accpeting, counter, buchi);
        String rt = counter.append(")").toString();

        logger.info(rt);
        return rt;
    }

    private void printRoutePart(Circuit route, StringBuilder strbuilder, BuchiAutomaton<?> buchi) {
        for (int i = 0; i < route.path.size() - 1; i++) {
            strbuilder.append(buchi.getTransitionSymbols(route.path.get(i), route.path.get(i + 1)).iterator().next());
        }
    }
}
