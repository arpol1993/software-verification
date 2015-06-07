/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.univ.software.verification.model.fa;

import java.io.Serializable;

/**
 *
 * @author Serhii Biletskij
 */
public class ProductsAutomatonClass implements Serializable {

    protected String v1, v2;
    protected Integer iter;

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public String getV2() {
        return v2;
    }

    public void setV2(String v2) {
        this.v2 = v2;
    }

    public Integer getIter() {
        return iter;
    }

    public void setIter(Integer iter) {
        this.iter = iter;
    }

}
