/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.kripke.BasicState;
import edu.univ.software.verification.model.kripke.BasicStructure;
import edu.univ.software.verification.model.ltl.Atom;
import edu.univ.software.verification.serializers.AtomSerializer;
import edu.univ.software.verification.serializers.BasicStateSerializer;
import edu.univ.software.verification.serializers.BasicStructureSerializer;
import edu.univ.software.verification.utils.AutomataUtils;
import edu.univ.software.verification.utils.LtlParser;
import edu.univ.software.verification.utils.LtlUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class ApplicationRunner {
    private static final Gson serializer;

    private KripkeStructure kripkeStructure;
    private BuchiAutomaton buchiAutomatonForSpecification;
    private BuchiAutomaton buchiAutomatonForSystem;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Atom.class, new AtomSerializer());
        gsonBuilder.registerTypeAdapter(BasicState.class, new BasicStateSerializer());
        gsonBuilder.registerTypeAdapter(BasicStructure.class, new BasicStructureSerializer());
        gsonBuilder.setPrettyPrinting();

        serializer = gsonBuilder.create();
    }

    public void initKripkeModel(String kripkeStructureFileName) {
        try {
            Path importPath = Paths.get(kripkeStructureFileName);
            String data = new String(Files.readAllBytes(importPath));
            kripkeStructure = serializer.fromJson(data, BasicStructure.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import automaton data from file", e);
        }
    }

    public boolean verify(String ltlFormula, Set<String> counterexamples) {
        buchiAutomatonForSystem = AutomataUtils.INSTANCE.convert(kripkeStructure);

        LtlFormula specification = LtlParser.parseString(ltlFormula);
        MullerAutomaton<?> ma = LtlUtils.INSTANCE.convertToAutomata(specification);
        buchiAutomatonForSpecification = AutomataUtils.INSTANCE.convert(ma);

        BuchiAutomaton<?> productResult = AutomataUtils.INSTANCE.product(buchiAutomatonForSystem,
                buchiAutomatonForSpecification);
        return AutomataUtils.INSTANCE.emptinessCheck(productResult, counterexamples);
    }

    public BuchiAutomaton getKripkeAutomaton(){
        return buchiAutomatonForSystem;
    }

    public BuchiAutomaton getLastLTLAutomaton() {
        return  buchiAutomatonForSpecification;
    }


    
}
