package edu.univ.software.verification.utils;

import com.google.common.collect.ImmutableSet;

import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.ltl.BinaryOp;
import edu.univ.software.verification.model.ltl.UnaryOp;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author arthur
 */
public enum LtlUtils {

    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(LtlUtils.class);

    /**
     * ID for extra initial graph node
     */
    public static final String INITIAL_GRAPH_NODE_ID = "init";

    public static LtlUtils getInstance() {
        return INSTANCE;
    }

    public /* Automata Type Here */ void convertToAutomata(LtlFormula formula) {
        Set<GraphNode> nodes = new LinkedHashSet<>();
        AtomicInteger idGen = new AtomicInteger(0);

        // build node graph via formula expansion
        processNode(GraphNode.builder(Integer.toString(idGen.getAndIncrement()))
                .addIncoming(INITIAL_GRAPH_NODE_ID)
                .addNewFormula(formula)
                .build(), nodes, idGen);

        // convert nodes graph to automata
        /* return */ nodesToAutomata(nodes);
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
            node.getNewFormulas().remove(formula);

            // check whether formula was already processed
            if (node.getOldFormulas().contains(formula)) {
                // ignore this formula and continue node processing 
                processNode(node, nodes, idGen);
            } else {
                // process selected formula
                processFormula(formula, node, nodes, idGen);
            }
        }
    }

    private void processFormula(LtlFormula formula, GraphNode node, Set<GraphNode> nodes, AtomicInteger idGen) {
        if (formula instanceof BinaryOp) {
            BinaryOp binaryOp = (BinaryOp) formula;
            
            if (BinaryOp.OpType.U.equals(binaryOp.getOpType())) {
                // two nodes are created and processed instead of the current one
                
                GraphNode q1 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                        .addOldFormula(formula)
                        .addNewFormula(binaryOp.getOpLeft())
                        .addNextFormula(formula)
                        .build();

                GraphNode q2 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                        .addOldFormula(formula)
                        .addNewFormula(binaryOp.getOpRight())
                        .build();

                processNode(q1, nodes, idGen);
                processNode(q2, nodes, idGen);
            } else if (BinaryOp.OpType.R.equals(binaryOp.getOpType())) {
                // two nodes are created and processed instead of the current one

                GraphNode q1 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                        .addNewFormula(binaryOp.getOpRight())
                        .build();

                GraphNode q2 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                        .addNewFormula(binaryOp.getOpRight())
                        .addNextFormula(formula)
                        .build();

                processNode(q1, nodes, idGen);
                processNode(q2, nodes, idGen);
            } else if (BinaryOp.OpType.AND.equals(binaryOp.getOpType())) {
                // replaced with single node containing both operands
                
                GraphNode q1 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                        .addOldFormula(formula)
                        .addNewFormula(binaryOp.getOpLeft())
                        .addNewFormula(binaryOp.getOpRight())
                        .build();

                processNode(q1, nodes, idGen);
            } else if (BinaryOp.OpType.OR.equals(binaryOp.getOpType())) {
                // two nodes for both operands are created and processed
                
                GraphNode q1 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                        .addOldFormula(formula)
                        .addNewFormula(binaryOp.getOpLeft())
                        .build();

                GraphNode q2 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                        .addOldFormula(formula)
                        .addNewFormula(binaryOp.getOpRight())
                        .build();

                processNode(q1, nodes, idGen);
                processNode(q2, nodes, idGen);
            }
        } else if (formula instanceof UnaryOp) {
            UnaryOp unaryOp = (UnaryOp) formula;
            
            if (UnaryOp.OpType.X.equals(unaryOp.getOpType())) {
                // neXt operand gets propagated
                
                GraphNode q1 = GraphNode.builder(Integer.toString(idGen.getAndIncrement()), node)
                        .addOldFormula(formula)
                        .addNextFormula(unaryOp.getOperand())
                        .build();

                processNode(q1, nodes, idGen);
            }
        }

    }

    private /* Automata Type Here */ void nodesToAutomata(Set<GraphNode> nodes) {
        // TODO
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
