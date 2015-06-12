package edu.univ.software.verification.manager;

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
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Daryna_Ragimova on 6/12/2015.
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

    public static VerificationResult verify(String kripkeStructureFileName, String ltlFormula) {
        KripkeStructure kripkeStructure = getKripkeStructureFromFile(kripkeStructureFileName);
        BuchiAutomaton<Set<Atom>> buchiAutomatonForSystem = AutomataUtils.INSTANCE.convert(kripkeStructure);
        LtlFormula specification = LtlParser.parseString(ltlFormula);
        MullerAutomaton<Set<Atom>> ma = LtlUtils.INSTANCE.convertToAutomata(specification.invert().normalized());
        BuchiAutomaton<Set<Atom>> buchiAutomatonForSpecification = AutomataUtils.INSTANCE.convert(ma);

        BuchiAutomaton<Set<Atom>> productResult = AutomataUtils.INSTANCE.product(buchiAutomatonForSystem,
                buchiAutomatonForSpecification);
        Set<String> counterexamples = new HashSet<>();
        boolean result = AutomataUtils.INSTANCE.emptinessCheck(productResult, counterexamples);

        VerificationResultBuilder builder = new VerificationResultBuilder();
        builder.withKripkeStructure(kripkeStructure);
        builder.withBuchiAutomatonForSystem(buchiAutomatonForSystem);
        builder.withSpecification(specification);
        builder.withMullerAutomatonForSpecification(ma);
        builder.withBuchiAutomatonForSpecification(buchiAutomatonForSpecification);
        builder.withProductResult(productResult);
        builder.withAnswer(result);
        builder.withCounterExamples(counterexamples);
        return builder.build();
    }

    private static KripkeStructure getKripkeStructureFromFile(String kripkeStructureFileName) {
        KripkeStructure kripkeStructure;
        try {
            Path importPath = Paths.get(kripkeStructureFileName);
            String data = new String(Files.readAllBytes(importPath));
            kripkeStructure = serializer.fromJson(data, BasicStructure.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import automaton data from file", e);
        }
        return kripkeStructure;
    }

}
