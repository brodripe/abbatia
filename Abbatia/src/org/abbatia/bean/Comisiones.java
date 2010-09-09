package org.abbatia.bean;

import java.io.Serializable;

public class Comisiones implements Serializable {
    private int regionid;
    private double total;
    private int obispado;
    private int cancelacion;
    private int venta5;
    private int venta10;
    private int venta15;
    private int transito;
    private int cardenales;
    private int papado;
    private int total1;


    public int getTransito() {
        return transito;
    }

    public void setTransito(int transito) {
        this.transito = transito;
    }

    public int getCancelacion() {
        return cancelacion;
    }

    public int getObispado() {
        return obispado;
    }

    public int getVenta15() {
        return venta15;
    }

    public int getVenta5() {
        return venta5;
    }

    public void setVenta10(int venta10) {
        if (venta10 < 0) this.venta10 = 0;
        else if (venta10 > 50) this.venta10 = 50;
        else
            this.venta10 = venta10;
    }

    public void setCancelacion(int cancelacion) {
        if (cancelacion < 0) this.cancelacion = 0;
        else if (cancelacion > 50) this.cancelacion = 50;
        else
            this.cancelacion = cancelacion;
    }

    public void setObispado(int obispado) {
        if (obispado < 0) this.obispado = 0;
        else if (obispado > 50) this.obispado = 50;
        else
            this.obispado = obispado;
    }

    public void setVenta15(int venta15) {
        if (venta15 < 0) this.venta15 = 0;
        else if (venta15 > 50) this.venta15 = 50;
        else
            this.venta15 = venta15;
    }

    public void setVenta5(int venta5) {
        if (venta5 < 0) this.venta5 = 0;
        else if (venta5 > 50) this.venta5 = 50;
        else
            this.venta5 = venta5;
    }

    public void setRegionid(int regionid) {
        this.regionid = regionid;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getVenta10() {
        return venta10;
    }

    public int getRegionid() {
        return regionid;
    }

    public double getTotal() {
        return total;
    }

    public int getCardenales() {
        return cardenales;
    }

    public void setCardenales(int cardenales) {
        this.cardenales = cardenales;
    }

    public int getPapado() {
        return papado;
    }

    public void setPapado(int papado) {
        this.papado = papado;
    }
}
