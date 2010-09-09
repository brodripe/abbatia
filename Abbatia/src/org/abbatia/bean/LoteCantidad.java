package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 03-abr-2006
 * Time: 23:46:18
 * To change this template use File | Settings | File Templates.
 */
public class LoteCantidad {
    private int idLote;
    private double cantidad;

    public LoteCantidad(int idLote, double cantidad) {
        this.idLote = idLote;
        this.cantidad = cantidad;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
}
