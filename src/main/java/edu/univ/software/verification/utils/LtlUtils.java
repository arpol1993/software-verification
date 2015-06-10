package edu.univ.software.verification.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.MullerAutomaton;
import edu.univ.software.verification.model.fa.BasicMullerAutomaton;
import edu.univ.software.verification.model.ltl.Atom;
import edu.univ.software.verification.model.ltl.BinaryOp;
import edu.univ.software.verification.model.ltl.UnaryOp;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author arthur
 */
public enum LtlUtils {

    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(LtlUtils.class);

    /**
     * ID for extra initial graph node
     */
    public static final String INITIAL_GRAPH_NODE_ID = "init";

    /**
     * Maximum number of predicate symbols allowed in single LTL formula for
     * Boolean (set of all subsets) calculation
     */
    public static final int MAX_PROPOSITIONAL_SYMBOLS_ALLOWED = 32;

    /**
     * Transition by empty set
     */
    public static final String EMPTY_TRANSITION_SYMBOL = Atom._1.toString();

    public static LtlUtils getInstance() {
        return INSTANCE;
    }

    public <T extends Serializable> MullerAutomaton<T> convertToAutomata(LtlFormula formula) {
        Set<GraphNode> nodes = new LinkedHashSet<>();
        AtomicInteger idGen = new AtomicInteger(0);

        // build node graph via formula expansion
        processNode(GraphNode.builder(Integer.toString(idGen.getAndIncrement()))
                .addIncoming(INITIAL_GRAPH_NODE_ID)
                .addNewFormula(formula)
                .build(), nodes, idGen);

        // convert nodes graph to automata
        return nodesToAutomata(formula, nodes);
    }

    public boolean isUnaryOp(LtlFormula formula, Set<UnaryOp.OpType> options) {
        return (formula instanceof UnaryOp) && options.contains(((UnaryOp) formula).getOpType());
    }

    public boolean isBinaryOp(LtlFormula formula, Set<BinaryOp.OpType> options) {
        return (formula instanceof BinaryOp) && options.contains(((BinaryOp) formula).getOpType());
    }

    public boolean isAtom(LtlFormula formula, Set<Atom.AtomType> options) {
        return (formula instanceof Atom) && options.contains(((Atom) formula).getType());
    }

    private void processNode(GraphNode node, Set<GraphNode> nodes, AtomicInteger idGen) {
        if (node.getNewFormulas().isEmpty()) {
            Optional<GraphNode> optNode = nodes.stream().filter(n -> n.getOldFormulas().equals(node.getOldFormulas())
                    && n.getNextFormulas().equals(node.getNextFormulas())).findFirst();

            if (optNode.isPresent()) {
                // update incoming edges for matching node
                optNode.get().builder().addIncoming(node.getIncoming()).update();
            } else {
                // add this node to created graph
                nodes.add(node);

                // continue processing with neXt formulas
                processNode(GraphNode.builder(Integer.toString(idGen.getAndIncrement()))
                        .addIncoming(node.getId())
                        .addNewFormulas(node.getNextFormulas())
                        .build(), nodes, idGen);
            }
        } else {
            // get the first new formula to work with
            LtlFormula formula = node.getNewFormulas().iterator().next();

            // remove it from formula queue
            node.builder().removeNewFormula(formula).update();

            // process selected formula
            processFormula(formula, node, nodes, idGen);
        }
    }

    private void processFormula(LtlFormula formula, GraphNode node, Set<GraphNode> nodes, AtomicInteger idGen) {
        if (formula instanceof Atom || isInvertedPredicateSymbol(formula)) {
            if (isAtom(formula, ImmutableSet.of(Atom.AtomType._0)) || node.getOldFormulas().contains(formula.invert().normalized())) {
                // discard current node
                nodes.remove(node);
            } else {
                // mark this formula processed
                node.builder().addOldFormula(formula).update();

                // continue node processing
                processNode(node, nodes, idGen);
            }
        } else if (isBinaryOp(formula, ImmutableSet.of(BinaryOp.OpType.U, BinaryOp.OpType.R, BinaryOp.OpType.OR))) {
            BinaryOp binaryOp = (BinaryOp) formula;

            Set<LtlFormula> newExtraFormulas1 = newFormulas1(binaryOp);
            newExtraFormulas1.removeAll(node.getOldFormulas());

            Set<LtlFormula> newExtraFormulas2 = newFormulas2(binaryOp);
            newExtraFormulas1.removeAll(node.getOldFormulas());

            // two nodes are created and processed instead of the current one
            GraphNode q1 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                    .addNewFormulas(newExtraFormulas1)
                    .addOldFormula(formula)
                    .addNextFormulas(nextFormulas1(binaryOp))
                    .build();

            GraphNode q2 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                    .addNewFormulas(newExtraFormulas2)
                    .addOldFormula(formula)
                    .build();

            processNode(q1, nodes, idGen);
            processNode(q2, nodes, idGen);
        } else if (isBinaryOp(formula, ImmutableSet.of(BinaryOp.OpType.AND))) {
            BinaryOp binaryOp = (BinaryOp) formula;

            Set<LtlFormula> newExtraFormulas = Sets.newHashSet(binaryOp.getOpLeft(), binaryOp.getOpRight());
            newExtraFormulas.removeAll(node.getOldFormulas());

            // single operand node is created and processed
            GraphNode q = node.builder()
                    .addNewFormulas(newExtraFormulas)
                    .addOldFormula(formula)
                    .build();

            processNode(q, nodes, idGen);
        } else if (isUnaryOp(formula, ImmutableSet.of(UnaryOp.OpType.X))) {
            UnaryOp unaryOp = (UnaryOp) formula;

            // single operand node is created and processed
            GraphNode q = node.builder()
                    .addOldFormula(formula)
                    .addNextFormula(unaryOp.getOperand())
                    .build();

            processNode(q, nodes, idGen);
        }
    }

    private <T extends Serializable> MullerAutomaton<T> nodesToAutomata(LtlFormula formula, Set<GraphNode> nodes) {

        // create automata builder and setup initial states
        MullerAutomaton.Builder<T> automatonBuilder = BasicMullerAutomaton.<T>builder()
                .withState(INITIAL_GRAPH_NODE_ID, true)
                .withStates(nodes.stream().map(GraphNode::getId).collect(Collectors.toSet()));

        // set of atomic propositions for given LTL formula
        Set<String> atomicPs = formula.getPropositions(null);

        //transitions definition
        for (GraphNode current : nodes) {
            Set<String> positivePs = getPositivePropositions(current);
            Set<String> negativePs = getNegativePropositions(current);

            Set<String> insignificantPs = Sets.newLinkedHashSet(atomicPs);
            insignificantPs.removeAll(positivePs);
            insignificantPs.removeAll(negativePs);

            Set<String> transitions = getPropositionsBoolean(insignificantPs).stream().map((Set<String> x) -> {
                x.addAll(positivePs);

                return x.stream().map(s -> (LtlFormula) Atom.forName(s)).collect(Collectors.toList());
            }).map((List<LtlFormula> x) -> {
                return x.isEmpty() ? EMPTY_TRANSITION_SYMBOL : BinaryOp.concat(BinaryOp.OpType.AND, x).toString();
            }).collect(Collectors.toSet());

            // add transitions to automaton
            if (!transitions.isEmpty()) {
                current.getIncoming().forEach(p -> automatonBuilder.withTransition(p, current.getId(), transitions));
            }
        }

        //final states
        nodes.stream().flatMap(n -> n.getOldFormulas().stream()).distinct().filter(f -> isBinaryOp(f, ImmutableSet.of(BinaryOp.OpType.U, BinaryOp.OpType.R))).map(f -> (BinaryOp) f).forEach((BinaryOp f) -> {
            Set<String> finalStateSet = nodes.stream().filter((GraphNode n) -> {
                return !n.getOldFormulas().contains(f) || n.getOldFormulas().contains(f.getOpRight());
            }).map(GraphNode::getId).collect(Collectors.toSet());

            if (!finalStateSet.isEmpty()) {
                automatonBuilder.withFinalStateSet(finalStateSet);
            }
        });

        return automatonBuilder.build();
    }

    private boolean isInvertedPredicateSymbol(LtlFormula formula) {
        boolean result = false;

        if (isUnaryOp(formula, ImmutableSet.of(UnaryOp.OpType.NEG))) {
            LtlFormula operand = ((UnaryOp) formula).getOperand();

            if (isAtom(operand, ImmutableSet.of(Atom.AtomType.VAR))) {
                result = true;
            }
        }

        return result;
    }

    private Set<LtlFormula> newFormulas1(BinaryOp formula) {
        switch (formula.getOpType()) {
            case R:
                return Sets.newHashSet(formula.getOpRight());
            case U:
            case OR:
                return Sets.newHashSet(formula.getOpLeft());
            default:
                throw new IllegalArgumentException(String.format("Unsupported formula '%s' supplied to New1", formula));
        }
    }

    private Set<LtlFormula> nextFormulas1(BinaryOp formula) {
        switch (formula.getOpType()) {
            case U:
            case R:
                return Sets.newHashSet(formula);
            case OR:
                return Sets.newHashSet();
            default:
                throw new IllegalArgumentException(String.format("Unsupported formula '%s' supplied to Next1", formula));
        }
    }

    private Set<LtlFormula> newFormulas2(BinaryOp formula) {
        switch (formula.getOpType()) {
            case R:
                return Sets.newHashSet(formula.getOpLeft(), formula.getOpRight());
            case U:
            case OR:
                return Sets.newHashSet(formula.getOpRight());
            default:
                throw new IllegalArgumentException(String.format("Unsupported formula '%s' supplied to New1", formula));
        }
    }
    
    private Set<String> getPositivePropositions(GraphNode node) {
        return node.getOldFormulas().stream().filter(f -> isAtom(f, ImmutableSet.of(Atom.AtomType.VAR))).map(f -> ((Atom) f).getName()).collect(Collectors.toSet());
    }
    
    private Set<String> getNegativePropositions(GraphNode node) {
        return node.getOldFormulas().stream().filter(this::isInvertedPredicateSymbol).map(f -> ((Atom) ((UnaryOp) f).getOperand()).getName()).collect(Collectors.toSet());
    }

    private Set<Set<String>> getPropositionsBoolean(Collection<String> propositions) throws IllegalArgumentException {
        List<String> symbols = Lists.newArrayList(propositions);

        if (symbols.size() > MAX_PROPOSITIONAL_SYMBOLS_ALLOWED) {
            throw new IllegalArgumentException(String.format(
                    "Too many propositional symbols (%d > %d)", symbols.size(), MAX_PROPOSITIONAL_SYMBOLS_ALLOWED));
        }

        Set<Set<String>> symbolsBoolean = new LinkedHashSet<>();

        for (int i = 0; i < (2 << Math.max(0, symbols.size() - 1)); i++) {
            Set<String> subset = new LinkedHashSet<>();

            for (int j = 0; j < symbols.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(symbols.get(j));
                }
            }

            symbolsBoolean.add(subset);
        }

        return symbolsBoolean;
    }
}

/**
 * Single graph node representation
 *
 * @author arthur
 */
class GraphNode {

    /**
     * Unique node identifier (immutable)
     */
    protected final String id;

    /**
     * List of incoming node IDs (having edge to this node)
     */
    protected Set<String> incoming;

    /**
     * List of formulas to process
     */
    protected Set<LtlFormula> newFormulas;

    /**
     * List of already processed formulas
     */
    protected Set<LtlFormula> oldFormulas;

    /**
     * List of formulas under neXt operator action
     */
    protected Set<LtlFormula> nextFormulas;

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static Builder builder(String id, GraphNode prototype) {
        return new Builder(id, prototype);
    }

    public Builder builder() {
        return GraphNode.builder(id, this);
    }

    //<editor-fold defaultstate="collapsed" desc="Getters">
    public String getId() {
        return id;
    }

    public Set<String> getIncoming() {
        return incoming;
    }

    public Set<LtlFormula> getNewFormulas() {
        return newFormulas;
    }

    public Set<LtlFormula> getOldFormulas() {
        return oldFormulas;
    }

    public Set<LtlFormula> getNextFormulas() {
        return nextFormulas;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setters (private)">
    private void setIncoming(Collection<String> incoming) {
        this.incoming = ImmutableSet.copyOf(incoming);
    }

    private void setNewFormulas(Collection<? extends LtlFormula> newFormulas) {
        this.newFormulas = ImmutableSet.copyOf(newFormulas);
    }

    private void setOldFormulas(Collection<? extends LtlFormula> oldFormulas) {
        this.oldFormulas = ImmutableSet.copyOf(oldFormulas);
    }

    private void setNextFormulas(Collection<? extends LtlFormula> nextFormulas) {
        this.nextFormulas = ImmutableSet.copyOf(nextFormulas);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    private GraphNode(String id) {
        this.id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode + equals + toString">
    @Override
    public int hashCode() {
        int hash = 3;

        hash = 29 * hash + Objects.hashCode(this.id);

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof GraphNode)) {
            return false;
        }

        final GraphNode other = (GraphNode) o;

        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "GraphNode{" + "id=" + id + ", newFormulas=" + newFormulas + ", oldFormulas=" + oldFormulas + ", nextFormulas=" + nextFormulas + '}';
    }
    //</editor-fold>

    public static class Builder {

        private static final Logger logger = LoggerFactory.getLogger(LtlUtils.class);

        protected final GraphNode original;

        protected final String id;
        protected final Set<String> incoming = new LinkedHashSet<>();
        protected final Set<LtlFormula> newFormulas = new LinkedHashSet<>();
        protected final Set<LtlFormula> oldFormulas = new LinkedHashSet<>();
        protected final Set<LtlFormula> nextFormulas = new LinkedHashSet<>();

        public Builder addIncoming(String node) {
            if (!incoming.add(node)) {
                logger.warn("Attempt to add duplicate incoming edge from '{}' to '{}'", node, this.id);
            }

            return this;
        }

        public Builder addIncoming(Collection<String> nodes) {
            nodes.stream().forEach(this::addIncoming);

            return this;
        }

        public Builder addNewFormula(LtlFormula formula) {
            return addFormula(newFormulas, formula);
        }

        public Builder addNewFormulas(Collection<? extends LtlFormula> formula) {
            return addFormulas(newFormulas, formula);
        }

        public Builder addOldFormula(LtlFormula formula) {
            return addFormula(oldFormulas, formula);
        }

        public Builder addOldFormulas(Collection<? extends LtlFormula> formula) {
            return addFormulas(oldFormulas, formula);
        }

        public Builder addNextFormula(LtlFormula formula) {
            return addFormula(nextFormulas, formula);
        }

        public Builder addNextFormulas(Collection<? extends LtlFormula> formula) {
            return addFormulas(nextFormulas, formula);
        }

        public Builder removeNewFormula(LtlFormula formula) {
            return removeFormula(newFormulas, formula);
        }

        public Builder removeNewFormulas(Collection<? extends LtlFormula> formula) {
            return removeFormulas(newFormulas, formula);
        }

        public Builder removeOldFormula(LtlFormula formula) {
            return removeFormula(oldFormulas, formula);
        }

        public Builder removeOldFormulas(Collection<? extends LtlFormula> formula) {
            return removeFormulas(oldFormulas, formula);
        }

        public Builder removeNextFormula(LtlFormula formula) {
            return removeFormula(nextFormulas, formula);
        }

        public Builder removeNextFormulas(Collection<? extends LtlFormula> formula) {
            return removeFormulas(nextFormulas, formula);
        }

        public GraphNode build() {
            return updateNode(new GraphNode(id));
        }

        public GraphNode update() {
            GraphNode result = null;

            if (original != null) {
                result = updateNode(original);
            } else {
                logger.error("Update action invoked with no original node present");
            }

            return result;
        }

        protected Builder addFormula(Set<LtlFormula> container, LtlFormula formula) {
            if (!container.add(formula)) {
                logger.warn("Attempt to add duplicate formula '{}'", formula);
            }

            return this;
        }

        protected Builder addFormulas(Set<LtlFormula> container, Collection<? extends LtlFormula> formulas) {
            formulas.stream().forEach(f -> addFormula(container, f));

            return this;
        }

        protected Builder removeFormula(Set<LtlFormula> container, LtlFormula formula) {
            if (!container.remove(formula)) {
                logger.warn("Attempt to remove missing formula '{}'", formula);
            }

            return this;
        }

        protected Builder removeFormulas(Set<LtlFormula> container, Collection<? extends LtlFormula> formulas) {
            formulas.stream().forEach(f -> removeFormula(container, f));

            return this;
        }

        protected GraphNode updateNode(GraphNode node) {
            node.setIncoming(incoming);
            node.setNewFormulas(newFormulas);
            node.setOldFormulas(oldFormulas);
            node.setNextFormulas(nextFormulas);

            return node;
        }

        //<editor-fold defaultstate="collapsed" desc="Constructors">
        private Builder(String id) {
            this.id = id;

            // no prototype used
            this.original = null;
        }

        private Builder(String id, GraphNode prototype) {
            this.id = id;
            this.incoming.addAll(prototype.getIncoming());
            this.newFormulas.addAll(prototype.getNewFormulas());
            this.oldFormulas.addAll(prototype.getOldFormulas());
            this.nextFormulas.addAll(prototype.getNextFormulas());

            // save prototype reference if correct prototype id is used
            this.original = Objects.equals(id, prototype.getId()) ? prototype : null;
        }
        //</editor-fold>
    }
}
