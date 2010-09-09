package org.abbatia.bean;

import java.io.Serializable;

public class DatosAbadia implements Serializable {
    private long idAbadia;
    private String nombre_abadia;
    private String nombre_orden;
    private String nombre_region;


    public void setIdAbadia(long idAbadia) {
        this.idAbadia = idAbadia;
    }


    public long getIdAbadia() {
        return idAbadia;
    }


    public void setNombre_abadia(String nombre_abadia) {
        this.nombre_abadia = nombre_abadia;
    }


    public String getNombre_abadia() {
        return nombre_abadia;
    }


    public void setNombre_orden(String nombre_orden) {
        this.nombre_orden = nombre_orden;
    }


    public String getNombre_orden() {
        return nombre_orden;
    }


    public void setNombre_region(String nombre_region) {
        this.nombre_region = nombre_region;
    }


    public String getNombre_region() {
        return nombre_region;
    }


}