package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 11-abr-2007
 * Time: 22:39:31
 * To change this template use File | Settings | File Templates.
 */
public class PuntuacionLibros {
    private short tipoLibro;
    private short nivel;
    private int cantidad;
    private double puntuacion;


    public PuntuacionLibros(short tipoLibro, short nivel, int cantidad, double puntuacion) {
        this.tipoLibro = tipoLibro;
        this.nivel = nivel;
        this.cantidad = cantidad;
        this.puntuacion = puntuacion;
    }


    public short getTipoLibro() {
        return tipoLibro;
    }

    public void setTipoLibro(short tipoLibro) {
        this.tipoLibro = tipoLibro;
    }

    public short getNivel() {
        return nivel;
    }

    public void setNivel(short nivel) {
        this.nivel = nivel;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }
}
