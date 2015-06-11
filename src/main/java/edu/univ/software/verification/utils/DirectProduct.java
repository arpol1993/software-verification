package edu.univ.software.verification.utils;

import edu.univ.software.verification.model.AutomatonState;
import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.fa.BasicBuchiAutomaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Util class for the Buchi automata's direct product
 *
 * @author Serhii Biletskij
 * @param <T> transition symbol type
 */
public class DirectProduct<T> {

    private static class CurrentProductState {

        private String astate;
        private String bstate;
        private Integer iter;

        public CurrentProductState(String astate, String bstate, Integer iter) {
            this.astate = astate;
            this.bstate = bstate;
            this.iter = iter;
        }

        public String getAState() {
            return astate;
        }

        public void setAState(String v1) {
            this.astate = v1;
        }

        public String getBState() {
            return bstate;
        }

        public void setBState(String v2) {
            this.bstate = v2;
        }

        public Integer getIteration() {
            return iter;
        }

        public void setIteration(Integer iter) {
            this.iter = iter;
        }
    }

    /**
     * For saving all transitins of future automaton, using to not override
     * exiting chart betweens states
     */
    private final HashMap<String, HashMap<String, Set<T>>> createdTransitions;
    
    /**
     * using to not add exiting vertex into automaton and get jung once all
     * outgoing transitins
     */
    private final ArrayList<String> createdStates;
    
    /**
     * using to not add exiting vertex into automaton
     */
    private final ArrayList<String> allFinal;
    
    /**
     * using to temprorary save all initian states in result automaton and later
     * to transform it into simple state and dublicate transitins from (0,0,0)
     * to out states from this initial state
     */
    private final ArrayList<String> allInitial;

    private final ArrayList<CurrentProductState> stack;

    private final BuchiAutomaton.Builder<T> resultBuilder;

    private final BuchiAutomaton<T> A, B;

    private BuchiAutomaton<T> prod;

    private boolean isComputed;

    public DirectProduct(BuchiAutomaton<T> A, BuchiAutomaton<T> B) {
        this.A = A;
        this.B = B;

        //Init vars
        resultBuilder = BasicBuchiAutomaton.builder();
        stack = new ArrayList<>();
        allInitial = new ArrayList<>();
        allFinal = new ArrayList<>();
        createdStates = new ArrayList<>();
        createdTransitions = new HashMap<>();

        isComputed = false;
    }

    public BuchiAutomaton<T> product() throws IllegalArgumentException {

        if (isComputed) {
            return prod;
        }

        //need to check if automata A and Automata B don't have 0 state
        if (A.getInitialStates().size() > 0 && B.getInitialStates().size() > 0) {
            /**
             * save initial states for future add it to stack for looking
             * transitions
             */
            A.getInitialStates().stream().forEach((AutomatonState stateA) -> {
                for (AutomatonState stateB : B.getInitialStates()) {
                    CurrentProductState initial = new CurrentProductState(stateA.getLabel(), stateB.getLabel(), 0);
                    stack.add(initial);
                    String to = String.format("(%s,%s,%d)", stateA.getLabel(), stateB.getLabel(), 0);
                    resultBuilder.withState(to, false);
                    allInitial.add(to);
                    createdStates.add(to);
                }
            });

            while (stack.size() > 0) {
                CurrentProductState current = stack.remove(0);
                Map<String, Set<T>> toA = A.getTransitionsFrom(current.getAState());
                Map<String, Set<T>> toB = B.getTransitionsFrom(current.getBState());

                // Iterate througth all possible combinations of states
                toA.keySet().stream().forEach((fromA) -> {
                    toB.keySet().stream().forEach((fromB) -> {
                        toA.get(fromA).stream().forEach((symbolA) -> {
                            toB.get(fromB).stream().filter((symbolB) -> (symbolA.equals(symbolB))).forEach((_item) -> {
                                processTransition(symbolA, current, fromA, fromB);
                            });
                        });
                    });
                });
            }

            /**
             * create new initial state "init" and connect this state with old
             * initial
             */
            String superInit = "init";
            resultBuilder.withState(superInit, true);
            for (String current : allInitial) {
                for (String vertexTo : createdTransitions.get(current).keySet()) {
                    resultBuilder.withTransition(superInit, vertexTo, createdTransitions.get(current).get(vertexTo));
                }
            }
        } else {
            return BasicBuchiAutomaton.<T>builder().build();
        }

        prod = resultBuilder.build();
        isComputed = true;

        return prod;
    }

    /**
     * Direct product automaton transitions & states creator
     *
     * @param symbolA it's now looking state in automata A
     * @param current current node on stack
     * @param fromA it's now looking char between current state and now looking
     * state in automata A
     * @param fromB it's now looking char between current state and now looking
     * state in automata B
     * @throws IllegalArgumentException
     */
    private void processTransition(T symbolA,
            CurrentProductState current,
            String fromA, String fromB) {

        // get iteration number
        Integer iter = current.getIteration();
        if (current.getIteration() == 2) {
            iter = 0;
        } else if (current.getIteration() == 0 && A.getFinalStates().contains(fromA)) {
            iter = 1;
        } else if (current.getIteration() == 1 && B.getFinalStates().contains(fromB)) {
            iter = 2;
        }

        String to = String.format("(%s,%s,%d)", fromA, fromB, iter);
        String from = String.format("(%s,%s,%d)", current.getAState(), current.getBState(), current.getIteration());

        /**
         * using for comfortable work with stack
         */
        CurrentProductState temp = new CurrentProductState(fromA, fromB, iter);

        /**
         * is new state
         */
        if (!createdStates.contains(to)) {
            stack.add(temp);
            resultBuilder.withState(to, false);
            createdStates.add(to);
        }

        if (!createdTransitions.containsKey(from)) {
            HashMap<String, Set<T>> newTransition = new HashMap<>();
            Set<T> symbols = new HashSet<>();
            symbols.add(symbolA);
            newTransition.put(to, symbols);
            createdTransitions.put(from, newTransition);
            resultBuilder.withTransition(from, to, symbolA);
        } else if (!createdTransitions.get(from).containsKey(to)) {
            Set<T> symbols = new HashSet<>();
            symbols.add(symbolA);
            createdTransitions.get(from).put(to, symbols);
            resultBuilder.withTransition(from, to, symbolA);
        } else if (!createdTransitions.get(from).get(to).contains(symbolA)) {
            createdTransitions.get(from).get(to).add(symbolA);
            resultBuilder.withTransition(from, to, symbolA);
        }

        /**
         * is new final state
         */
        if (iter == 2 && !allFinal.contains(to)) {
            resultBuilder.withFinalState(to);
            allFinal.add(to);
        }
    }
}
