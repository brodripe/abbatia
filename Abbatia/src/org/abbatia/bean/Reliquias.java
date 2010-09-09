package org.abbatia.bean;

public class Reliquias {
    private int idReliquia;
    private int idEdificio;
    private long idAbadia;
    private String nombre;
    private int estado;
    private String fecha_adquisicion;
    private String fecha_creacion;

    public int getIdReliquia() {
        return idReliquia;
    }

    public long getIdAbadia() {
        return idAbadia;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha_adquisicion() {
        return fecha_adquisicion;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public int getEstado() {
        return estado;
    }

    public void setIdReliquia(int idReliquia) {
        this.idReliquia = idReliquia;
    }

    public void setIdAbadia(long idAbadia) {
        this.idAbadia = idAbadia;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha_adquisicion(String fecha_adquisicion) {
        this.fecha_adquisicion = fecha_adquisicion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
