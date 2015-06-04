package edu.univ.software.verification.utils;

import edu.univ.software.verification.exceptions.InvalidBuilderUsage;
import edu.univ.software.verification.model.LtlFormula;
import edu.univ.software.verification.model.ltl.Atom;
import edu.univ.software.verification.model.ltl.BinaryOp;
import edu.univ.software.verification.model.ltl.Builder;
import edu.univ.software.verification.model.ltl.UnaryOp;

/**
 *
 * @author Pocomaxa
 */
public class LtlParser {

    private final static char _0 = '0';
    private final static char _1 = '1';

    private final static char LB = '(';
    private final static char RB = ')';

    //Unary
    private final static char F = 'F';
    private final static char G = 'G';
    private final static char X = 'X';
    private final static char NEG = '!';

    //Binary
    private final static char U = 'U';
    private final static char R = 'R';
    private final static char AND = '&';
    private final static char OR = '|';
    private final static char IMPL = '-';
    private final static char IMPL2 = '>';

    public static LtlFormula parseString(String formula) throws IllegalArgumentException {
        LtlParser parser = new LtlParser(formula);

        return parser.parse();
    }

    private final String formulaString;

    public LtlParser(String formulaString) {
        this.formulaString = formulaString;
    }

    public LtlFormula parse() throws IllegalArgumentException {

        Builder builder = new Builder();
        try {

            for (int i = 0; i < formulaString.length(); i++) {
                char token = formulaString.charAt(i);
                boolean isLast = i == formulaString.length() - 1;

                switch (token) {
                    case LB:
                        builder.startBinary();
                        break;
                    case RB:
                        builder.endBinary();
                        break;
                    case _0:
                        builder.withAtom(Atom.AtomType._0, "0");
                        break;
                    case _1:
                        builder.withAtom(Atom.AtomType._1, "1");
                        break;
                    case F:
                        builder.withUnary(UnaryOp.OpType.F);
                        break;
                    case G:
                        builder.withUnary(UnaryOp.OpType.G);
                        break;
                    case X:
                        builder.withUnary(UnaryOp.OpType.X);
                        break;
                    case NEG:
                        builder.withUnary(UnaryOp.OpType.NEG);
                        break;
                    case U:
                        builder.withBinaryOpType(BinaryOp.OpType.U);
                        break;
                    case R:
                        builder.withBinaryOpType(BinaryOp.OpType.R);
                        break;
                    case OR:
                        if (!isLast && formulaString.charAt(i + 1) == OR) {
                            builder.withBinaryOpType(BinaryOp.OpType.OR);
                        }
                        break;
                    case AND:
                        if (!isLast && formulaString.charAt(i + 1) == AND) {
                            builder.withBinaryOpType(BinaryOp.OpType.AND);
                        }
                        break;
                    case IMPL:
                        if (!isLast && formulaString.charAt(i + 1) == IMPL2) {
                            builder.withBinaryOpType(BinaryOp.OpType.IMPL);
                        }
                        break;
                    default:
                        if (token >= 'a' && token <= 'z') {
                            builder.withAtom(Atom.AtomType.VAR, String.valueOf(token));
                        }
                }
            }
        } catch (InvalidBuilderUsage e) {
            throw new IllegalArgumentException("Invalid formula string! CAUSED BY: " + e.getMessage());
        }

        return builder.build();
    }

}
