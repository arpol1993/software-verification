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

    public static BuchiAutomaton<ProductsAutomatonClass> product(BuchiAutomaton<Serializable> A, BuchiAutomaton<Serializable> B) {
        BuchiAutomaton.Builder<ProductsAutomatonClass> resultBuilder = BasicBuchiAutomaton.builder();
        Set<AutomatonState<Serializable>> initialA = A.getInitialStates();
        Set<AutomatonState<Serializable>> initialB = B.getInitialStates();
        Integer iter = 0;
        ArrayList<ProductsAutomatonClass> stack = new ArrayList<ProductsAutomatonClass>();
        ArrayList<String> visited = new ArrayList<String>();
        ArrayList<String> allInitial = new ArrayList<String>();
        ArrayList<String> allFinal = new ArrayList<String>();
        HashMap<String, HashMap<String, Set<String>>> allSaveTansitins = new HashMap<String, HashMap<String, Set<String>>>();

        if (initialA.size() > 0 && initialB.size() > 0) {
            ProductsAutomatonClass initial = new ProductsAutomatonClass();
            initial.setV1(initialA.iterator().next().getLabel());
            initial.setV2(initialB.iterator().next().getLabel());
            initial.setIter(iter);
            stack.add(initial);
            resultBuilder.withState(initialA.iterator().next().getLabel() + "," + initialB.iterator().next().getLabel() + "," + iter, true, initial);
            while (stack.size() > 0) {
                ProductsAutomatonClass current = stack.remove(0);
                Map<String, Set<String>> toA = A.getTransitionsFrom(current.getV1());
                Map<String, Set<String>> toB = B.getTransitionsFrom(current.getV2());
                for (String checkVertexA : toA.keySet()) {
                    for (String checkVariableA : toA.get(checkVertexA)) {
                        for (String checkVertexB : toB.keySet()) {
                            for (String checkVariableB : toB.get(checkVertexB)) {
                                if (checkVariableA == checkVariableB) {
                                    if (current.getIter() == 2) {
                                        iter = 0;
                                    } else if (current.getIter() == 0 && A.getFinalStates().contains(checkVertexA)) {
                                        iter = 1;
                                    } else if (current.getIter() == 1 && B.getFinalStates().contains(checkVertexB)) {
                                        iter = 2;
                                    } else {
                                        iter = current.getIter();
                                    }
                                    ProductsAutomatonClass temp = new ProductsAutomatonClass();
                                    temp.setV1(checkVertexA);
                                    temp.setV2(checkVertexB);
                                    temp.setIter(iter);
                                    if (!visited.contains(checkVertexA + "," + checkVertexB + "," + iter)) {
                                        stack.add(temp);
                                        visited.add(checkVertexA + "," + checkVertexB + "," + iter);
                                    }
                                    if (!resultBuilder.build().hasState(checkVertexA + "," + checkVertexB + "," + iter)) {
                                        resultBuilder.withState(checkVertexA + "," + checkVertexB + "," + iter, false, temp);
                                    }
                                    if (!resultBuilder.build().hasTransition(current.getV1() + "," + current.getV2() + "," + current.getIter(), checkVertexA + "," + checkVertexB + "," + iter)) {
                                        resultBuilder.withTransition(current.getV1() + "," + current.getV2() + "," + current.getIter(), checkVertexA + "," + checkVertexB + "," + iter, checkVariableA);
                                    } else {
                                        Set<String> symbols = new HashSet<String>();
                                        symbols.addAll(resultBuilder.build().getTransitionSymbols(current.getV1() + "," + current.getV2() + "," + current.getIter(), checkVertexA + "," + checkVertexB + "," + iter));
                                        if (!symbols.contains(checkVariableA)) {
                                            symbols.add(checkVariableA);
                                        }
                                        resultBuilder.withTransition(current.getV1() + "," + current.getV2() + "," + current.getIter(), checkVertexA + "," + checkVertexB + "," + iter, symbols);
                                    }
                                    if (iter == 2) {
                                        resultBuilder.withFinalState(checkVertexA + "," + checkVertexB + "," + iter);
                                    }

                                }
                            }
                        }
                    }
                }
            }

        } else {
            //exeption
        }
        //

        return resultBuilder.build();
    }

}
