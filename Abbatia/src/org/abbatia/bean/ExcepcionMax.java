package org.abbatia.bean;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 04-sep-2005
 * Time: 1:49:08
 * To change this template use File | Settings | File Templates.
 */
public class ExcepcionMax implements Serializable {
    private String nombreExcepcion;
    private int max;
    private int accion;

    public String getNombreExcepcion() {
        return nombreExcepcion;
    }

    public void setNombreExcepcion(String nombreExcepcion) {
        this.nombreExcepcion = nombreExcepcion;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }
}
