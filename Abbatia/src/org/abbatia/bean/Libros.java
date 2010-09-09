package org.abbatia.bean;

public class Libros {
    private int idLibro;
    private int idLibroTipo;
    private int idEdificio;
    private long idAbadia;
    private String nombre;
    private int estado;
    private String fecha_adquisicion;
    private String fecha_creacion;

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

    public int getIdLibro() {
        return idLibro;
    }

    public int getIdLibroTipo() {
        return idLibroTipo;
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

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public void setIdLibroTipo(int idLibroTipo) {
        this.idLibroTipo = idLibroTipo;
    }
}
