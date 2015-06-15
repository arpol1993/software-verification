package edu.univ.software.verification.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.kripke.BasicState;
import edu.univ.software.verification.model.kripke.BasicStructure;
import edu.univ.software.verification.model.ltl.Atom;
import edu.univ.software.verification.serializers.AtomSerializer;
import edu.univ.software.verification.serializers.BasicStateSerializer;
import edu.univ.software.verification.serializers.BasicStructureSerializer;
import edu.univ.software.verification.utils.LtlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Daryna_Ragimova on 6/12/2015.
 */
public class UsageExample {
    private static final Logger logger = LoggerFactory.getLogger(UsageExample.class);

    private static final Gson serializer;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Atom.class, new AtomSerializer());
        gsonBuilder.registerTypeAdapter(BasicState.class, new BasicStateSerializer());
        gsonBuilder.registerTypeAdapter(BasicStructure.class, new BasicStructureSerializer());
        gsonBuilder.setPrettyPrinting();

        serializer = gsonBuilder.create();
    }

    private static void verificationMicrowaveOven() {
        logger.info("---------------------MICROWAVE OVEN VERIFICATION-----------------");
        verify("src/main/resources/automaton_data/micro-oven.json", "G ((!close && start) -> F cooking)");
        verify("src/main/resources/automaton_data/micro-oven.json", "G ((close && start) -> F cooking)");
        logger.info("-----------------------------------------------------------------");
    }

    private static void verificationDevTeam() {
        logger.info("---------------------DEV TEAM VERIFICATION-----------------");
        verify("src/main/resources/automaton_data/dev-team.json", "(G F bug && (!test || !feature))");
        verify("src/main/resources/automaton_data/dev-team.json", "(G !bug && (!test || !feature))");
        verify("src/main/resources/automaton_data/dev-team.json", "(G (bug -> F (!bug && !test) && (!test || !feature))");
        logger.info("-----------------------------------------------------------------");
    }

    private static void verificationTwoStageElevator() {
        logger.info("---------------------TWO STAGE ELEVATOR VERIFICATION-----------------");
        verify("src/main/resources/automaton_data/two-stage-elevator.json", "G (move -> F (first || second))");
        verify("src/main/resources/automaton_data/two-stage-elevator.json", "G (move -> F (first && !second))");
        verify("src/main/resources/automaton_data/two-stage-elevator.json", "(G F move && (!first || !second))");
        verify("src/main/resources/automaton_data/two-stage-elevator.json", "(G F second && (!move || !first))");
        logger.info("-----------------------------------------------------------------");
    }

    private static void verify(String fileName, String specification) {
        KripkeStructure kripkeStructure = getKripkeStructureFromFile(fileName);
        LtlFormula ltlFormula = LtlParser.parseString(specification);
        logger.info("Formula: " + specification);
        logger.info("Verification result: " + (ApplicationRunner.verify(kripkeStructure, ltlFormula).isConfirmed() ? "confirmed" : "declined"));
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

    public static void main(String[] args) {
        verificationMicrowaveOven();
        verificationDevTeam();
        verificationTwoStageElevator();
    }
}
