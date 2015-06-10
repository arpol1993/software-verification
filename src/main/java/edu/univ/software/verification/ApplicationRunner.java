/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification;

import edu.univ.software.verification.model.kripke.BasicState;
import edu.univ.software.verification.model.kripke.BasicStructure;
import edu.univ.software.verification.model.ltl.Atom;
import edu.univ.software.verification.serializers.AtomSerializer;
import edu.univ.software.verification.serializers.BasicStateSerializer;
import edu.univ.software.verification.serializers.BasicStructureSerializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.utils.AutomataUtils;
import edu.univ.software.verification.utils.LtlParser;
import edu.univ.software.verification.utils.LtlUtils;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class ApplicationRunner {
    private static final Gson serializer;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Atom.class, new AtomSerializer());
        gsonBuilder.registerTypeAdapter(BasicState.class, new BasicStateSerializer());
        gsonBuilder.registerTypeAdapter(BasicStructure.class, new BasicStructureSerializer());
        gsonBuilder.setPrettyPrinting();

        serializer = gsonBuilder.create();
    }
    public boolean verify(String kripkeStructureFileName, String ltlFormula) {
        KripkeStructure ks;
        try {
            Path importPath = Paths.get(kripkeStructureFileName);
            String data = new String(Files.readAllBytes(importPath));
            ks = serializer.fromJson(data, BasicStructure.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import automaton data from file", e);
        }
        BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem = AutomataUtils.INSTANCE.convert(ks);

        LtlFormula specification = LtlParser.parseString(ltlFormula);
        MullerAutomaton<Set<Atom>> ma = LtlUtils.INSTANCE.convertToAutomata(specification.invert().normalized());
        BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification = AutomataUtils.INSTANCE.convert(ma);

        BuchiAutomaton<Set<Atom>> productResult = AutomataUtils.INSTANCE.product(buchiAutomatonForSystem, 
                                                                         buchiAutomatonForSpecification);
        return AutomataUtils.INSTANCE.emptinessCheck(productResult, new HashSet<>());

    }
    
}
