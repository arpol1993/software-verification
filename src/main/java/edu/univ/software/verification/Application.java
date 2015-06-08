package edu.univ.software.verification;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.univ.software.verification.model.BuchiAutomaton;
import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.fa.BasicBuchiAutomaton;
import edu.univ.software.verification.model.fa.BasicMullerAutomaton;
import edu.univ.software.verification.model.kripke.BasicStructure;
import edu.univ.software.verification.model.ltl.Atom;
import edu.univ.software.verification.model.ltl.BinaryOp;
import edu.univ.software.verification.model.ltl.UnaryOp;
import edu.univ.software.verification.serializers.AtomSerializer;
import edu.univ.software.verification.serializers.BasicStateSerializer;
import edu.univ.software.verification.serializers.BasicStructureSerializer;
import edu.univ.software.verification.utils.AutomataUtils;
import edu.univ.software.verification.utils.DirectProduct;
import edu.univ.software.verification.utils.LtlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 *
 * @author arthur
 */
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final Gson serializer;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Atom.class, new AtomSerializer());
        gsonBuilder.registerTypeAdapter(BasicState.class, new BasicStateSerializer());
        gsonBuilder.registerTypeAdapter(BasicStructure.class, new BasicStructureSerializer());
        gsonBuilder.setPrettyPrinting();

        serializer = gsonBuilder.create();
    }

    private static void kripkeStructureDemo() {
        // demonstrates creation of kripke model for p. 265
        KripkeStructure ks = BasicStructure.builder()
                .withState("0", ImmutableList.of(Atom.forName("p"), Atom.forName("q")))
                .withState("1", ImmutableList.of(Atom.forName("r"), Atom.forName("q")))
                .withState("2", ImmutableList.of(Atom.forName("p"), Atom.forName("u")))
                .withState("3", ImmutableList.of(Atom.forName("r"), Atom.forName("u")))
                .withState("4", ImmutableList.of(Atom.forName("w"), Atom.forName("q")))
                .withState("5", ImmutableList.of(Atom.forName("r"), Atom.forName("u")))
                .withState("6", ImmutableList.of(Atom.forName("p"), Atom.forName("z")))
                .withState("7", ImmutableList.of(Atom.forName("w"), Atom.forName("u")))
                .withState("8", ImmutableList.of(Atom.forName("r"), Atom.forName("z")))
                .withTransition("0", "0")
                .withTransition("0", "1")
                .withTransition("0", "2")
                .withTransition("1", "3")
                .withTransition("1", "4")
                .withTransition("2", "5")
                .withTransition("2", "6")
                .withTransition("3", "7")
                .withTransition("4", "7")
                .withTransition("5", "8")
                .withTransition("6", "8")
                .withTransition("7", "2")
                .withTransition("8", "1")
                .build();

        logger.info("---Kripke structure test---");
        logger.info("JSON-serialized Kripke structure:\n" + serializer.toJson(ks));

        // serialize to json-string
//        logger.info("JSON-serialized Kripke structure:\n" + serializer.toJson(ks));

        // deserialize with
        // serializer.fromJson(serializer.toJson(ks),BasicStructure.class)

        // export to file
//        try {
//            Path exportPath = Paths.get("src/main/resources/automaton_data/export_kripke.json");
//            Files.write(exportPath, serializer.toJson(ks).getBytes(), StandardOpenOption.CREATE);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to export automaton data", e);
//        }

        //import from file
//        try {
//            Path importPath = Paths.get("src/main/resources/automaton_data/export_kripke.json");
//            String data = new String(Files.readAllBytes(importPath));
//            KripkeStructure kripkeStructure = serializer.fromJson(data, BasicStructure.class);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to import automaton data from file", e);
//        }

        logger.info("Edge between {} and {}: {}", 0, 2, ks.hasTransition("0", "2"));
        logger.info("Edge between {} and {}: {}", 3, 8, ks.hasTransition("3", "8"));
        logger.info("---------------------------");
    }

    private static void buchiAutomatonDemo() {
        // demonstrates creation of Buchi automaton for p. 234a
        BuchiAutomaton<?> buchiAutomaton = BasicBuchiAutomaton.builder()
                .withState("1", true)
                .withStates("2", "3", "4")
                .withTransition("1", "2", "x")
                .withTransition("1", "4", "y")
                .withTransition("2", "2", "x")
                .withTransition("2", "3", "y")
                .withTransition("3", "4", "x", "y")
                .withTransition("4", "4", "x", "y")
                .withTransition("4", "3", "y")
                .withFinalState("3")
                .withFinalState("4")
                .build();

        logger.info("---Buchi automaton test---");
        logger.info("Transition between {} and {}: {}", 2, 3, buchiAutomaton.hasTransition("2", "3"));
        logger.info("Transition between {} and {}: {}", 3, 1, buchiAutomaton.hasTransition("3", "1"));
        logger.info("Ingoing edges for state {}: {}", 4, buchiAutomaton.getTransitionsTo("4"));
        logger.info("---------------------------");

        logger.info("Is that automaton accepts only empty language : {}",
                AutomataUtils.INSTANCE.emptinessCheck(buchiAutomaton));
        logger.info("---------------------------");
    }

    private static void productBuchiAutomatonDemo() {

        
        //example 1
        BuchiAutomaton<Serializable> buchiAutomatonA = BasicBuchiAutomaton.builder()
                .withState("1", true)
                .withStates("2")
                .withTransition("1", "2", "x", "y")
                .withTransition("2", "2", "y")
                .withTransition("2", "1", "y")
                .withFinalState("1")
                .build();
        BuchiAutomaton<Serializable> buchiAutomatonB = BasicBuchiAutomaton.builder()
                .withState("1", true)
                .withStates("2")
                .withTransition("1", "2", "x", "y")
                .withTransition("2", "2", "x", "y")
                .withFinalState("2")
                .build();

        BuchiAutomaton<?> buchiAutomatonResult = AutomataUtils.INSTANCE.product(buchiAutomatonA, buchiAutomatonB);

        logger.info("---Buchi product A automaton test---");
        logger.info("States: {}", buchiAutomatonA.getStates());
        logger.info("Transitions : {}", buchiAutomatonA.getTransitions());
        logger.info("Final : {}", buchiAutomatonA.getFinalStates());
        logger.info("---Buchi product B automaton test---");
        logger.info("States: {}", buchiAutomatonB.getStates());
        logger.info("Transitions : {}", buchiAutomatonB.getTransitions());
        logger.info("Final : {}", buchiAutomatonB.getFinalStates());
        logger.info("---Buchi product automaton test---");
        logger.info("States: {}", buchiAutomatonResult.getStates());
        logger.info("Transitions : {}", buchiAutomatonResult.getTransitions());
        logger.info("Final : {}", buchiAutomatonResult.getFinalStates());

        //example 2
        
        buchiAutomatonA = BasicBuchiAutomaton.builder()
                .withState("1", true)
                .withStates("2")
                .withStates("3")
                .withTransition("1", "2", "a", "b")
                .withTransition("2", "2", "a")
                .withTransition("2", "3", "b")
                .withTransition("3", "3", "b")
                .withTransition("3", "2", "a")
                .withFinalState("3")
                .build();
        buchiAutomatonB = BasicBuchiAutomaton.builder()
                .withState("1", true)
                .withStates("2")
                .withStates("3")
                .withTransition("1", "2", "a", "b")
                .withTransition("2", "2", "b")
                .withTransition("2", "3", "a", "b")
                .withTransition("3", "3", "a", "b")
                .withFinalState("3")
                .build();

        buchiAutomatonResult = AutomataUtils.INSTANCE.product(buchiAutomatonA, buchiAutomatonB);

        logger.info("---Buchi product A automaton test---");
        logger.info("States: {}", buchiAutomatonA.getStates());
        logger.info("Transitions : {}", buchiAutomatonA.getTransitions());
        logger.info("Final : {}", buchiAutomatonA.getFinalStates());
        logger.info("---Buchi product B automaton test---");
        logger.info("States: {}", buchiAutomatonB.getStates());
        logger.info("Transitions : {}", buchiAutomatonB.getTransitions());
        logger.info("Final : {}", buchiAutomatonB.getFinalStates());
        logger.info("---Buchi product automaton test---");
        logger.info("States: {}", buchiAutomatonResult.getStates());
        logger.info("Transitions : {}", buchiAutomatonResult.getTransitions());
        logger.info("Final : {}", buchiAutomatonResult.getFinalStates());

    }

    private static void mullerAutomatonDemo() {
        // demonstrates creation of Muller automaton for p. 253
        MullerAutomaton<?> mullerAutomaton = BasicMullerAutomaton.builder()
                .withState("0", true)
                .withState("1")
                .withTransition("0", "0", "x")
                .withTransition("0", "1", "y")
                .withTransition("1", "1", "y")
                .withTransition("1", "0", "x")
                .withFinalStateSet("1")
                .withFinalStateSet("0")
                .build();

        logger.info("---Muller automaton test---");
        logger.info("Transition between {} and {}: {}", 1, 0, mullerAutomaton.hasTransition("1", "0"));
        logger.info("Transition between {} and {}: {}", 2, 1, mullerAutomaton.hasTransition("2", "1"));
        logger.info("Outgoing edges for state {}: {}", 0, mullerAutomaton.getTransitionsFrom("0"));
        logger.info("---------------------------");

        BuchiAutomaton<?> buchiAutomaton = AutomataUtils.INSTANCE.convert(mullerAutomaton);

        logger.info("---Degeneralization (LGBA -> BA) automaton test---");
        logger.info("Transition between {} and {}: {}", 1, 0, buchiAutomaton.hasTransition("(1, 0)", "(0, 1)"));
        logger.info("Transition between {} and {}: {}", 2, 1, buchiAutomaton.hasTransition("(2, 0)", "(1, 0)"));
        logger.info("Outgoing edges for state {}: {}", "(1, 0)", buchiAutomaton.getTransitionsFrom("(1, 0)"));
        logger.info("Outgoing edges for state {}: {}", "(0, 1)", buchiAutomaton.getTransitionsFrom("(0, 1)"));
        logger.info("Outgoing edges for state {}: {}", "(0, 0)", buchiAutomaton.getTransitionsFrom("(0, 0)"));
        logger.info("Outgoing edges for state {}: {}", "(1, 1)", buchiAutomaton.getTransitionsFrom("(1, 1)"));
        logger.info("---------------------------");

        logger.info("Is that automaton accepts only empty language : {}", AutomataUtils.INSTANCE.emptinessCheck(buchiAutomaton));
    }

    private static void kripkeStructureToBuchiAutomatonDemo() {
        // demonstrates conversion (kripke model --> Buchi automaton) for p. 270
        KripkeStructure ks = BasicStructure.builder()
                .withState("0", ImmutableList.of(Atom.forName("p")), true)
                .withState("1", ImmutableList.of(Atom.forName("p"), Atom.forName("q")), true)
                .withState("2", ImmutableList.of(Atom.forName("q")))
                .withTransition("0", "1")
                .withTransition("1", "0")
                .withTransition("2", "0")
                .withTransition("1", "2")
                .build();

        BuchiAutomaton buchiAutomaton = AutomataUtils.INSTANCE.convert(ks);
        logger.info("---Kripke model --> Buchi automaton test---");


        // serialize to json-string
//        logger.info("JSON-serialized Kripke structure:\n" + serializer.toJson(ks));

        // deserialize with
        // serializer.fromJson(serializer.toJson(ks),BasicStructure.class)

        // export to file
//        try {
//            Path exportPath = Paths.get("src/main/resources/automaton_data/export_kripke.json");
//            Files.write(exportPath, serializer.toJson(ks).getBytes(), StandardOpenOption.CREATE);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to export automaton data", e);
//        }

        //import from file
//        try {
//            Path importPath = Paths.get("src/main/resources/automaton_data/export_kripke.json");
//            String data = new String(Files.readAllBytes(importPath));
//            KripkeStructure kripkeStructure = serializer.fromJson(data, BasicStructure.class);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to import automaton data from file", e);
//        }

        logger.info("Transition between {} and {}: {}", 0, 1, buchiAutomaton.hasTransition("0", "1"));
        logger.info("Transition between {} and {}: {}", 0, 2, buchiAutomaton.hasTransition("0", "2"));
        logger.info("Transition between {} and {}: {}", 1, 2, buchiAutomaton.hasTransition("1", "2"));
        logger.info("Transition between {} and {}: {}", 2, 1, buchiAutomaton.hasTransition("2", "1"));
        logger.info("Transition between {} and {}: {}", 2, 3, buchiAutomaton.hasTransition("2", "3"));
        logger.info("Transition between {} and {}: {}", 3, 1, buchiAutomaton.hasTransition("3", "1"));
        logger.info("Transition between {} and {}: {}", 3, 1, buchiAutomaton.hasTransition("3", "2"));
        logger.info("Transition between {} and {}: {}", 3, 1, buchiAutomaton.hasTransition("1", "3"));
        logger.info("Transition formula between {}: is {}", "(0, 1)", buchiAutomaton.getTransitionSymbols("0", "1"));
        logger.info("Transition formula between {}: is {}", "(0, 2)", buchiAutomaton.getTransitionSymbols("0", "2"));
        logger.info("Transition formula between {}: is {}", "(1, 2)", buchiAutomaton.getTransitionSymbols("1", "2"));
        logger.info("Transition formula between {}: is {}", "(2, 1)", buchiAutomaton.getTransitionSymbols("2", "1"));
        logger.info("Transition formula between {}: is {}", "(2, 3)", buchiAutomaton.getTransitionSymbols("2", "3"));
        logger.info("Transition formula between {}: is {}", "(3, 1)", buchiAutomaton.getTransitionSymbols("3", "1"));
        logger.info("---------------------------");
    }

    private static void ltlDemo() {
        Atom atomB = new Atom("b");
        Atom atomA = new Atom("a");
        Atom atomZero = Atom._0;
        Atom atomOne = Atom._1;
        BinaryOp implication = BinaryOp.build(BinaryOp.OpType.IMPL, atomA, atomB);
        BinaryOp union = BinaryOp.build(BinaryOp.OpType.OR, atomZero, atomOne);
        BinaryOp release = BinaryOp.build(BinaryOp.OpType.R, implication, union);
        UnaryOp f = UnaryOp.build(UnaryOp.OpType.F, release);

        logger.info("---LTL inversion test---");
        logger.info("Formula: {}", f);
        logger.info("Inverted: {}", f.invert());
        logger.info("Inverted & normalized: {}", f.invert().normalized());
        logger.info("------------------------");

        logger.info("---LTL parser test------");
        logger.info("Formula(REP): {}", LtlParser.parseString(f.toString()));
        logger.info("Inverted(REP): {}", LtlParser.parseString(f.invert().toString()));
        logger.info("Inv. & Norm.(REP): {}", LtlParser.parseString(f.invert().normalized().toString()));
        logger.info("------------------------");
    }

    public static void main(String[] args) {
        kripkeStructureDemo();
        ltlDemo();
        buchiAutomatonDemo();
        mullerAutomatonDemo();
        kripkeStructureToBuchiAutomatonDemo();
        productBuchiAutomatonDemo();
    }
}
