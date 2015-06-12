package edu.univ.software.verification.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Daryna_Ragimova on 6/12/2015.
 */
public class UsageExample {
    private static final Logger logger = LoggerFactory.getLogger(UsageExample.class);
    private static final ApplicationRunner runner = new ApplicationRunner();

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
        logger.info("Formula: " + specification);
        logger.info("Verification result: " + (runner.verify(fileName, specification).isConfirmed() ? "confirmed" : "declined"));
    }

    public static void main(String[] args) {
        verificationMicrowaveOven();
        verificationDevTeam();
        verificationTwoStageElevator();
    }
}
