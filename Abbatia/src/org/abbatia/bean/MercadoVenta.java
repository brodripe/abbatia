package org.abbatia.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MercadoVenta implements Serializable {
    private ArrayList lstVenta;
    private ArrayList lstVenta2;
    private ArrayList mercanciaTipo;

    public ArrayList getMercanciaTipo() {
        return mercanciaTipo;
    }

    public void setMercanciaTipo(ArrayList mercanciaTipo) {
        this.mercanciaTipo = mercanciaTipo;
    }

    public ArrayList getLstVenta2() {
        return lstVenta2;
    }

    public void setLstVenta2(ArrayList lstVenta2) {
        this.lstVenta2 = lstVenta2;
    }

    public void setLstVenta(ArrayList lstVenta) {
        this.lstVenta = lstVenta;
    }

    public ArrayList getLstVenta() {
        return lstVenta;
    }

}
