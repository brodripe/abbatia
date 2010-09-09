package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 16-sep-2005
 * Time: 16:26:33
 * To change this template use File | Settings | File Templates.
 */
public class EdificioProceso {
    private double volumen;
    private double almacenamiento;
    private int extresado;

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }

    public double getAlmacenamiento() {
        return almacenamiento;
    }

    public void setAlmacenamiento(double almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public int getExtresado() {
        return extresado;
    }

    public void setExtresado(int extresado) {
        this.extresado = extresado;
    }
}
