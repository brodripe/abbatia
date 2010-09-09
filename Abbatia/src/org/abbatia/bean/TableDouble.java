package org.abbatia.bean;

import java.io.Serializable;

public class TableDouble implements Serializable {
    private int param1;
    private double param2;

    public TableDouble(int param1, double param2) {
        this.setParam1(param1);
        this.setParam2(param2);
    }

    public int getParam1() {
        return param1;
    }

    public void setParam1(int param1) {
        this.param1 = param1;
    }

    public double getParam2() {
        return param2;
    }

    public void setParam2(double param2) {
        this.param2 = param2;
    }
}
