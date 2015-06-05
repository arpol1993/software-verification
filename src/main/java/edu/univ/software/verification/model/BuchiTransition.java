/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.model;

import java.util.Set;

/**
 * Buchi automaton transition representation
 * 
 * @author Oksana
 */
public interface BuchiTransition {
    
    /**
     * Output state of the transition in automaton
     * 
     * @return output state
     */
    public BuchiState getFrom();
    
    /**
     * Input state of the transition in automaton
     * 
     * @return input state
     */
    public BuchiState getTo();
    
    /**
     * Character for working current transition
     * 
     * @return character
     */
    public Character getLabel();
}
