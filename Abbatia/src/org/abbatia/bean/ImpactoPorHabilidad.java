package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 03-mar-2005
 * Time: 20:29:02
 * To change this template use File | Settings | File Templates.
 */
public class ImpactoPorHabilidad {
    private int idMonje;
    private int idHabilidad;
    private double impacto;

    public int getIdMonje() {
        return idMonje;
    }

    public void setIdMonje(int idMonje) {
        this.idMonje = idMonje;
    }

    public int getIdHabilidad() {
        return idHabilidad;
    }

    public void setIdHabilidad(int idHabilidad) {
        this.idHabilidad = idHabilidad;
    }

    public double getImpacto() {
        return impacto;
    }

    public void setImpacto(double impacto) {
        this.impacto = impacto;
    }
}
