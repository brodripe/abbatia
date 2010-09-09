package org.abbatia.bean;

import java.io.Serializable;

public class AbadiaBasic implements Serializable {
    private int idDeAbadia;
    private String nombre;
    private String puntuacion;
    private int posicion;


    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getIdDeAbadia() {
        return idDeAbadia;
    }

    public void setIdDeAbadia(int idDeAbadia) {
        this.idDeAbadia = idDeAbadia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}
