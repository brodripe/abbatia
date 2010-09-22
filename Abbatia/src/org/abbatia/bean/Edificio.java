/***********************************************************************
 * Module:  Edificio.java
 * Author:  Benja & John
 * Purpose: Defines the Class Edificio
 ***********************************************************************/
package org.abbatia.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;


public class Edificio implements Serializable {
    private int idDeEdificio;
    private String fechaDeConstruccion;
    private String nombre;
    private String descripcion;
    private int capacidadVital;
    private int tiempoConstruccion;
    private int almacenamiento;
    private int almacenamiento_plus;
    private int idDeAbadia;
    private int idDeTipoDeEdificio;
    private double estado;
    private int nivel;
    private int siguiente_nivel;
    private int map_x;
    private int map_y;
    private String enConstruccion;
    private String fechaFinPrevista;
    private double almacenamientoActual;
    private String almacenamientoActualStr;
    private ArrayList contenido;
    private ArrayList alimentosDelEdificio;
    private Hashtable alimentosDelEdificioHT;
    private String grafico_construccion;
    private String grafico_visualizacion;
    private String barraEstado;
    private int mantenimiento;
    private String mantenimientoDesc;
    private String costeMantenimiento;
    private long costeMantenimientoLong;
    private double costeMantenimientoD;
    private int numMonjesVelatorio = 0;
    private int numMonjesCementerio = 0;
    private int numMonjesOsario = 0;

    public double getCosteMantenimientoD() {
        return costeMantenimientoD;
    }

    public void setCosteMantenimientoD(double costeMantenimientoD) {
        this.costeMantenimientoD = costeMantenimientoD;
    }

    public String getAlmacenamientoActualStr() {
        return almacenamientoActualStr;
    }

    public void setAlmacenamientoActualStr(String almacenamientoActualStr) {
        this.almacenamientoActualStr = almacenamientoActualStr;
    }

    public int getAlmacenamiento_plus() {
        return almacenamiento_plus;
    }

    public void setAlmacenamiento_plus(int almacenamiento_plus) {
        this.almacenamiento_plus = almacenamiento_plus;
    }

    public long getCosteMantenimientoLong() {
        return costeMantenimientoLong;
    }

    public void setCosteMantenimientoLong(long costeMantenimientoLong) {
        this.costeMantenimientoLong = costeMantenimientoLong;
    }

    public String getCosteMantenimiento() {
        return costeMantenimiento;
    }

    public void setCosteMantenimiento(String costeMantenimiento) {
        this.costeMantenimiento = costeMantenimiento;
    }

    public String getMantenimientoDesc() {
        return mantenimientoDesc;
    }

    public void setMantenimientoDesc(String mantenimientoDesc) {
        this.mantenimientoDesc = mantenimientoDesc;
    }

    public int getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(int mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public String getBarraEstado() {
        return barraEstado;
    }

    public void setBarraEstado(String barraEstado) {
        this.barraEstado = barraEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdDeEdificio(int idDeEdificio) {
        this.idDeEdificio = idDeEdificio;
    }


    public int getIdDeEdificio() {
        return idDeEdificio;
    }


    public void setFechaDeConstruccion(String fechaDeConstruccion) {
        this.fechaDeConstruccion = fechaDeConstruccion;
    }


    public String getFechaDeConstruccion() {
        return fechaDeConstruccion;
    }


    public void setCapacidadVital(int capacidadVital) {
        this.capacidadVital = capacidadVital;
    }


    public int getCapacidadVital() {
        return capacidadVital;
    }


    public void setAlmacenamiento(int almacenamiento) {
        this.almacenamiento = almacenamiento;
    }


    public int getAlmacenamiento() {
        return almacenamiento;
    }


    public void setIdDeAbadia(int idDeAbadia) {
        this.idDeAbadia = idDeAbadia;
    }


    public int getIdDeAbadia() {
        return idDeAbadia;
    }


    public void setIdDeTipoDeEdificio(int idDeTipoDeEdificio) {
        this.idDeTipoDeEdificio = idDeTipoDeEdificio;
    }


    public int getIdDeTipoDeEdificio() {
        return idDeTipoDeEdificio;
    }


    public void setEstado(double estado) {
        this.estado = estado;
    }


    public double getEstado() {
        return estado;
    }


    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAlimentosDelEdificioHT(Hashtable alimentosDelEdificioHT) {
        this.alimentosDelEdificioHT = alimentosDelEdificioHT;
    }

    public void setAlimentosDelEdificio(ArrayList alimentosDelEdificio) {
        this.alimentosDelEdificio = alimentosDelEdificio;
    }

    public void setTiempoConstruccion(int tiempoConstruccion) {
        this.tiempoConstruccion = tiempoConstruccion;
    }

    public void setAlmacenamientoActual(double almacenamientoActual) {
        this.almacenamientoActual = almacenamientoActual;
    }

    public int getNivel() {
        return nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Hashtable getAlimentosDelEdificioHT() {
        return alimentosDelEdificioHT;
    }

    public ArrayList getAlimentosDelEdificio() {
        return alimentosDelEdificio;
    }

    public int getTiempoConstruccion() {
        return tiempoConstruccion;
    }

    public double getAlmacenamientoActual() {
        return almacenamientoActual;
    }


    public void setFechaFinPrevista(String fechaFinPrevista) {
        this.fechaFinPrevista = fechaFinPrevista;
    }


    public String getFechaFinPrevista() {
        return fechaFinPrevista;
    }


    public String getEnConstruccion() {
        return enConstruccion;
    }


    public void setEnConstruccion(String enConstruccion) {
        this.enConstruccion = enConstruccion;
    }


    public void setContenido(ArrayList contenido) {
        this.contenido = contenido;
    }

    public void setMap_x(int map_x) {
        this.map_x = map_x;
    }

    public void setMap_y(int map_y) {
        this.map_y = map_y;
    }

    public ArrayList getContenido() {
        return contenido;
    }

    public int getMap_x() {
        return map_x;
    }

    public int getMap_y() {
        return map_y;
    }


    public void setGrafico_construccion(String grafico_construccion) {
        this.grafico_construccion = grafico_construccion;
    }


    public String getGrafico_construccion() {
        return grafico_construccion;
    }


    public void setGrafico_visualizacion(String grafico_visualizacion) {
        this.grafico_visualizacion = grafico_visualizacion;
    }

    public void setSiguiente_nivel(int siguiente_nivel) {
        this.siguiente_nivel = siguiente_nivel;
    }

    public String getGrafico_visualizacion() {
        return grafico_visualizacion;
    }

    public int getSiguiente_nivel() {
        return siguiente_nivel;
    }


    public int getNumMonjesVelatorio() {
        return numMonjesVelatorio;
    }

    public void setNumMonjesVelatorio(int numMonjesVelatorio) {
        this.numMonjesVelatorio = numMonjesVelatorio;
    }

    public int getNumMonjesCementerio() {
        return numMonjesCementerio;
    }

    public void setNumMonjesCementerio(int numMonjesCementerio) {
        this.numMonjesCementerio = numMonjesCementerio;
    }

    public int getNumMonjesOsario() {
        return numMonjesOsario;
    }

    public void setNumMonjesOsario(int numMonjesOsario) {
        this.numMonjesOsario = numMonjesOsario;
    }
}


