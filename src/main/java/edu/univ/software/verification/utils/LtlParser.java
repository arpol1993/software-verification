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

    public static LtlFormula parseString(String formula) {
        LtlParser parser = new LtlParser(formula);

        return parser.parse();
    }

    private final String formulaString;

    public LtlParser(String formulaString) {
        this.formulaString = formulaString;
    }

    public LtlFormula parse() {

        Builder builder = new Builder();
        try {
            
            boolean isCurrId = false;
            int idStart = 0;
            int bracketsCount = 0;

            for (int i = 0; i <= formulaString.length(); i++) {
                
                if (i == formulaString.length()) {
                    if (isCurrId) { builder.placeAtom(Atom.AtomType.VAR, formulaString.substring(idStart, i)); }
                    continue;
                }
                
                char token = formulaString.charAt(i);
                boolean isLast = i == formulaString.length() - 1;
                
                if (isCurrId && !isId(token)) {
                    isCurrId = false;                    
                    
                    builder.placeAtom(Atom.AtomType.VAR, formulaString.substring(idStart, i));
                }

                switch (token) {
                    case LB:
                        bracketsCount++;
                        builder.withBinary();
                        break;
                    case RB:
                        bracketsCount--;
                        if (bracketsCount < 0) { System.out.println("Invalid brackets found!"); }
                        //builder.endBinary();
                        break;
                    case _0:
                        builder.placeAtom(Atom.AtomType._0, "0");
                        break;
                    case _1:
                        builder.placeAtom(Atom.AtomType._1, "1");
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
                        builder.placeBinaryOperator(BinaryOp.OpType.U);
                        break;
                    case R:
                        builder.placeBinaryOperator(BinaryOp.OpType.R);
                        break;
                    case OR:
                        if (!isLast && formulaString.charAt(i + 1) == OR) {
                            builder.placeBinaryOperator(BinaryOp.OpType.OR);
                        }
                        break;
                    case AND:
                        if (!isLast && formulaString.charAt(i + 1) == AND) {
                            builder.placeBinaryOperator(BinaryOp.OpType.AND);
                        }
                        break;
                    case IMPL:
                        if (!isLast && formulaString.charAt(i + 1) == IMPL2) {
                            builder.placeBinaryOperator(BinaryOp.OpType.IMPL);
                        }
                        break;
                    default:
                        if (!isCurrId && isId(token)) {
                            isCurrId = true;
                            idStart = i;
                        }
                }
            }
        } catch (InvalidBuilderUsage e) {
            throw new IllegalArgumentException("Invalid formula string! CAUSED BY: " + e.getMessage());
        }

        return builder.build();
    }

    private static boolean isId(char token) {
        return (token >= 'a' && token <= 'z') || token == '_';
    }

}
