package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 01-jul-2006
 * Time: 0:10:44
 * To change this template use File | Settings | File Templates.
 */
public class EdificioSaturado {
    private int idAbadia;
    private int idEdificio;
    private int idTipoEdificio;
    private int nivel;
    private double almacenamientoMaximo;
    private double almacenamientoActual;
    private int idIdioma;
    private String nombre;


    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getIdTipoEdificio() {
        return idTipoEdificio;
    }

    public void setIdTipoEdificio(int idTipoEdificio) {
        this.idTipoEdificio = idTipoEdificio;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }

    public double getAlmacenamientoMaximo() {
        return almacenamientoMaximo;
    }

    public void setAlmacenamientoMaximo(double almacenamientoMaximo) {
        this.almacenamientoMaximo = almacenamientoMaximo;
    }

    public double getAlmacenamientoActual() {
        return almacenamientoActual;
    }

    public void setAlmacenamientoActual(double almacenamientoActual) {
        this.almacenamientoActual = almacenamientoActual;
    }

    public int getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(int idIdioma) {
        this.idIdioma = idIdioma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
