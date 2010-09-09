package org.abbatia.bean;

public class LibroProceso {
    private int idLibroTipo;
    private int idLibro;
    private long idAbadia;
    private int idIdioma;
    private String nombre;
    private int estado;
    private double deterioro;
    private short nivel;


    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public short getNivel() {
        return nivel;
    }

    public void setNivel(short nivel) {
        this.nivel = nivel;
    }

    public int getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(int idIdioma) {
        this.idIdioma = idIdioma;
    }

    public int getIdLibroTipo() {
        return idLibroTipo;
    }

    public void setIdLibroTipo(int idLibroTipo) {
        this.idLibroTipo = idLibroTipo;
    }

    public long getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(long idAbadia) {
        this.idAbadia = idAbadia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getDeterioro() {
        return deterioro;
    }

    public void setDeterioro(double deterioro) {
        this.deterioro = deterioro;
    }
}
