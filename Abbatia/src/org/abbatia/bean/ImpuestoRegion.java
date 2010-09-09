package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 19-oct-2005
 * Time: 23:43:33
 * To change this template use File | Settings | File Templates.
 */
public class ImpuestoRegion {
    private int idRegion;
    private String nombreRegion;
    private int valorImpuesto;

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String nombreRegion) {
        this.nombreRegion = nombreRegion;
    }

    public int getValorImpuesto() {
        return valorImpuesto;
    }

    public void setValorImpuesto(int valorImpuesto) {
        this.valorImpuesto = valorImpuesto;
    }
}
