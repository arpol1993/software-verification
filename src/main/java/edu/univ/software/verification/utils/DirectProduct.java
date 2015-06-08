/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.utils;

import edu.univ.software.verification.Application;
import edu.univ.software.verification.model.AutomatonState;
import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.fa.BasicBuchiAutomaton;
import edu.univ.software.verification.model.fa.BasicMullerAutomaton;
import edu.univ.software.verification.model.fa.BasicState;
import edu.univ.software.verification.model.fa.ProductsAutomatonClass;
import edu.univ.software.verification.model.ltl.BinaryOp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Serhii Biletskij
 */
public class DirectProduct {

    /**
     * For saving all transitins of future automaton, using to not override
     * exiting chart betweens states
     */
    private static HashMap<String, HashMap<String, Set<String>>> allSaveTansitins;
    /**
     * using to not add exiting vertex into automaton and get jung once all
     * outgoing transitins
     */
    private static ArrayList<String> allVertex;
    /**
     * using to not add exiting vertex into automaton
     */
    private static ArrayList<String> allFinal;
    /**
     * using to temprorary save all initian states in result automaton and later
     * to transform it into simple state and dublicate transitins from (0,0,0)
     * to out states from this initial state
     */
    private static ArrayList<String> allInitial;

    private static ArrayList<ProductsAutomatonClass> stack;

    private static BuchiAutomaton.Builder<?> resultBuilder;

    public static BuchiAutomaton<?> product(BuchiAutomaton<Serializable> A, BuchiAutomaton<Serializable> B) {
        resultBuilder = BasicBuchiAutomaton.builder();
        stack = new ArrayList<ProductsAutomatonClass>();
        allInitial = new ArrayList<String>();
        allFinal = new ArrayList<String>();
        allVertex = new ArrayList<String>();
        allSaveTansitins = new HashMap<String, HashMap<String, Set<String>>>();

        //need to check if automata A and Automata B don't have 0 state
        
        if (A.getInitialStates().size() > 0 && B.getInitialStates().size() > 0) {
            /**
             * save initial states for future add it to stack for looking
             * transitions
             */
            for (AutomatonState<Serializable> stateA : A.getInitialStates()) {
                for (AutomatonState<Serializable> stateB : B.getInitialStates()) {
                    ProductsAutomatonClass initial = new ProductsAutomatonClass();
                    initial.setV1(stateA.getLabel());
                    initial.setV2(stateB.getLabel());
                    initial.setIter(0);
                    stack.add(initial);
                    String to = String.format("(%s,%s,%d)", stateA.getLabel(), stateB.getLabel(), 0);
                    resultBuilder.withState(to, false);
                    allInitial.add(to);
                    allVertex.add(to);
                }
            }

            while (stack.size() > 0) {
                ProductsAutomatonClass current = stack.remove(0);
                Map<String, Set<String>> toA = A.getTransitionsFrom(current.getV1());
                Map<String, Set<String>> toB = B.getTransitionsFrom(current.getV2());

                for (String checkVertexA : toA.keySet()) {
                    for (String checkVariableA : toA.get(checkVertexA)) {
                        for (String checkVertexB : toB.keySet()) {
                            for (String checkVariableB : toB.get(checkVertexB)) {
                                /**
                                 * checkVertexA - it's now looking state in
                                 * automata A
                                 *
                                 * checkVertexB - it's now looking state in
                                 * automata B
                                 *
                                 * checkVariableA - it's now looking char
                                 * between current state and now looking state
                                 * in automata A
                                 *
                                 * checkVariableB - it's now looking char
                                 * between current state and now looking state
                                 * in automata B
                                 */
                                /**
                                 * Find transitions with same chars in automata
                                 * A and automata B
                                 */
                                if (checkVariableA == checkVariableB) {
                                    /**
                                     * get iteration number
                                     */
                                    Integer iter = current.getIter();
                                    if (current.getIter() == 2) {
                                        iter = 0;
                                    } else if (current.getIter() == 0 && A.getFinalStates().contains(checkVertexA)) {
                                        iter = 1;
                                    } else if (current.getIter() == 1 && B.getFinalStates().contains(checkVertexB)) {
                                        iter = 2;
                                    }

                                    String to = String.format("(%s,%s,%d)", checkVertexA, checkVertexB, iter);
                                    String from = String.format("(%s,%s,%d)", current.getV1(), current.getV2(), current.getIter());

                                    /**
                                     * using for comfortable work with stack
                                     */
                                    ProductsAutomatonClass temp = new ProductsAutomatonClass();
                                    temp.setV1(checkVertexA);
                                    temp.setV2(checkVertexB);
                                    temp.setIter(iter);

                                    /**
                                     * is new state
                                     */
                                    if (!allVertex.contains(to)) {
                                        stack.add(temp);
                                        resultBuilder.withState(to, false);
                                        allVertex.add(to);
                                    }

                                    if (!allSaveTansitins.containsKey(from)) {
                                        HashMap<String, Set<String>> newTransition = new HashMap<String, Set<String>>();
                                        Set<String> symbols = new HashSet<String>();
                                        symbols.add(checkVariableA);
                                        newTransition.put(to, symbols);
                                        allSaveTansitins.put(from, newTransition);
                                        resultBuilder.withTransition(from, to, checkVariableA);
                                    } else if (!allSaveTansitins.get(from).containsKey(to)) {
                                        Set<String> symbols = new HashSet<String>();
                                        symbols.add(checkVariableA);
                                        allSaveTansitins.get(from).put(to, symbols);
                                        resultBuilder.withTransition(from, to, symbols);
                                    } else if (!allSaveTansitins.get(from).get(to).contains(checkVariableA)) {
                                        allSaveTansitins.get(from).get(to).add(checkVariableA);
                                        resultBuilder.withTransition(from, to, allSaveTansitins.get(from).get(to));
                                    }

                                    /**
                                     * is new final dtate
                                     */
                                    if (iter == 2 && !allFinal.contains(to)) {
                                        resultBuilder.withFinalState(to);
                                        allFinal.add(to);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /**
             * create new initial state (0,0,0) and connect this state with old
             * initial
             */
            String superInit = String.format("(%s,%s,%d)", "0", "0", 0);
            resultBuilder.withState(superInit, true);
            for (String current : allInitial) {
                for (String vertexTo : allSaveTansitins.get(current).keySet()) {
                    resultBuilder.withTransition(superInit, vertexTo, allSaveTansitins.get(current).get(vertexTo));
                }
            }
        } else {
            //need to write exeption
        }

        return resultBuilder.build();
    }

}
