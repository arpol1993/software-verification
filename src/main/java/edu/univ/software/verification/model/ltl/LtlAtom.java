/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.model.ltl;

import edu.univ.software.verification.model.LtlFormula;
import java.util.Objects;

/**
 *
 * @author Pocomaxa
 */
public class LtlAtom implements LtlFormula {

    public static enum AtomType {

        VAR,
        _0,
        _1
    }

    //Special case Atoms
    public final static LtlAtom _0 = new LtlAtom(AtomType._0);
    public final static LtlAtom _1 = new LtlAtom(AtomType._1);

    private String name;
    private final AtomType type;

    public static LtlAtom forName(String name) {
        return new LtlAtom(name);
    }

    public LtlAtom(String name) {
        this.name = name;
        this.type = AtomType.VAR;
    }

    private LtlAtom(AtomType type) {
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

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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

        final LtlAtom other = (LtlAtom) obj;

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
                return LtlUnaryOp.build(LtlUnaryOp.UnaryOp.NEG, this.clone());
        }
        return this.clone();
    }

    @Override
    public LtlFormula normalized() {
        return this.clone();
    }

    @Override
    public LtlFormula clone() {
        if (type == AtomType.VAR) {
            return new LtlAtom(name);
        } else {
            return this;
        }
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
