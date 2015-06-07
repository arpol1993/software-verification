package edu.univ.software.verification.model.ltl;

import edu.univ.software.verification.model.LtlFormula;
import java.util.HashSet;

import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Pocomaxa
 */
public class Atom implements LtlFormula {

    public static enum AtomType {

        VAR,
        _0,
        _1
    }

    //Special case Atoms
    public final static Atom _0 = new Atom(AtomType._0);
    public final static Atom _1 = new Atom(AtomType._1);

    private final String name;
    private final AtomType type;

    public static Atom forName(String name) {
        return new Atom(name);
    }

    public Atom(String name) {
        this.name = name.toLowerCase();
        this.type = AtomType.VAR;
    }

    private Atom(AtomType type) {
        this.type = type;
        this.name = type.toString();
    }

    /**
     * @return the type
     */
    public AtomType getType() {
        return type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.name)
                + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final Atom other = (Atom) obj;

        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return Objects.equals(this.name, other.name);
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public LtlFormula invert() {
        switch (type) {
            case _0:
                return _1;
            case _1:
                return _0;
            case VAR:
                return UnaryOp.build(UnaryOp.OpType.NEG, this.clone());
        }
        return this.clone();
    }

    @Override
    public boolean evaluate(Set<String> values) {
        switch (type) {
            case _0:
                return false;
            case _1:
                return true;
            case VAR:
                return values.contains(name);
            default:
                throw new AssertionError(type.toString());
        }
    }

    @Override
    public Set<String> fetchSymbols() {
        Set<String> res = new HashSet<>();
        
        if (type == AtomType.VAR) {
            res.add(name);
        }
        
        return res;
    }

    @Override
    public LtlFormula normalized() {
        return this.clone();
    }

    @Override
    public LtlFormula clone() {
        return this;
    }

    @Override
    public String toString() {
        switch (type) {
            case _0:
                return "0";
            case _1:
                return "1";
            case VAR:
                return name;
        }

        return "LtlAtom{" + "name=" + name + ", type=" + type + '}';
    }
}
