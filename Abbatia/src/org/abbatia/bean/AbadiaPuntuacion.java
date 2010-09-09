package org.abbatia.bean;

/**
 * Created by IntelliJ IDEA.
 * User: Benjamin
 * Date: 14-abr-2007
 * Time: 16:14:43
 * To change this template use File | Settings | File Templates.
 */
public class AbadiaPuntuacion {
    private int idAbadia;
    private double monjesNivel = 0;
    private double monjesHabilidad = 0;
    private double monjesActividad = 0;
    private double monjesMuertos = 0;
    private double santos = 0;
    private double animales = 0;
    private double edificios = 0;
    private double libros = 0;
    private double total = 0;
    private int clasificacion = 0;
    private int clasificacionRegional = 0;


    public AbadiaPuntuacion() {
    }

    public AbadiaPuntuacion(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public int getIdAbadia() {
        return idAbadia;
    }

    public void setIdAbadia(int idAbadia) {
        this.idAbadia = idAbadia;
    }

    public double getMonjesNivel() {
        return monjesNivel;
    }

    public void setMonjesNivel(double monjesNivel) {
        this.monjesNivel = monjesNivel;
    }

    public double getMonjesHabilidad() {
        return monjesHabilidad;
    }

    public void setMonjesHabilidad(double monjesHabilidad) {
        this.monjesHabilidad = monjesHabilidad;
    }

    public double getMonjesActividad() {
        return monjesActividad;
    }

    public void setMonjesActividad(double monjesActividad) {
        this.monjesActividad = monjesActividad;
    }

    public double getMonjesMuertos() {
        return monjesMuertos;
    }

    public void setMonjesMuertos(double monjesMuertos) {
        this.monjesMuertos = monjesMuertos;
    }

    public double getSantos() {
        return santos;
    }

    public void setSantos(double santos) {
        this.santos = santos;
    }

    public double getAnimales() {
        return animales;
    }

    public void setAnimales(double animales) {
        this.animales = animales;
    }

    public double getEdificios() {
        return edificios;
    }

    public void setEdificios(double edificios) {
        this.edificios = edificios;
    }

    public double getLibros() {
        return libros;
    }

    public void setLibros(double libros) {
        this.libros = libros;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(int clasificacion) {
        this.clasificacion = clasificacion;
    }

    public int getClasificacionRegional() {
        return clasificacionRegional;
    }

    public void setClasificacionRegional(int clasificacionRegional) {
        this.clasificacionRegional = clasificacionRegional;
    }
}
