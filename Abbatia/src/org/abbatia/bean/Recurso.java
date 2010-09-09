package org.abbatia.bean;

public class Recurso {
    private int recursoID;
    private long abadiaID;
    private int estado;
    private double cantidad;
    private String cantidadF;
    private double volumen;
    private String descripcion;
    private String barra_HTML;

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }

    public String getBarra_HTML() {
        return barra_HTML;
    }

    public void setBarra_HTML(String barra_HTML) {
        this.barra_HTML = barra_HTML;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public long getAbadiaID() {
        return abadiaID;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setRecursoID(int recursoID) {
        this.recursoID = recursoID;
    }

    public void setAbadiaID(long abadiaID) {
        this.abadiaID = abadiaID;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getRecursoID() {
        return recursoID;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getDescripcion() {
        return descripcion;
    }


    public void setCantidadF(String cantidadF) {
        this.cantidadF = cantidadF;
    }


    public String getCantidadF() {
        return cantidadF;
    }

}


