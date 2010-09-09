package org.abbatia.bean;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 04-sep-2005
 * Time: 2:05:40
 * To change this template use File | Settings | File Templates.
 */
public class ExcepcionCount implements Serializable {
    private String nombreExcepcion;
    private int count;

    public ExcepcionCount(String nombreExcepcion, int count) {
        this.nombreExcepcion = nombreExcepcion;
        this.count = count;
    }

    public String getNombreExcepcion() {
        return nombreExcepcion;
    }

    public void setNombreExcepcion(String nombreExcepcion) {
        this.nombreExcepcion = nombreExcepcion;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
