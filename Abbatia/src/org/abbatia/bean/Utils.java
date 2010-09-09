package org.abbatia.bean;

import java.io.Serializable;

public class Utils implements Serializable {
    private String target = "";


    public void setTarget(String target) {
        this.target = target;
    }


    public String getTarget() {
        return target;
    }
}