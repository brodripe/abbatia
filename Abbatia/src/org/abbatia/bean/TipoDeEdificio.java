/***********************************************************************
 * Module:  TipoDeEdificio.java
 * Author:  Benja & John
 * Purpose: Defines the Class TipoDeEdificio
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class TipoDeEdificio {
    private long idDeTipoDeEdificio;
    private String descripcion;
    private double nivel;
    private long tiempoDeConstruccion;
    private ArrayList utilidadDeLosTiposDeEdificio;

    private Hashtable utilidadDeLosTiposDeEdificioHT;
    private ArrayList tiposDeEdificio;

    private Hashtable tiposDeEdificioHT;

    // --------------------------------------------------------------------------
    // ID de Tipo de Edificio
    // --------------------------------------------------------------------------
    public void setIdDeTipoDeEdificio(long newIdDeTipoDeEdificio) {
        this.idDeTipoDeEdificio = newIdDeTipoDeEdificio;
    }

    public long getIdDeTipoDeEdificio() {
        return this.idDeTipoDeEdificio;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Descripción
    // --------------------------------------------------------------------------

    public void setDescripcion(String newDescripcion) {
        this.descripcion = newDescripcion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Nivel
    // --------------------------------------------------------------------------

    public void setNivel(double newNivel) {
        this.nivel = newNivel;
    }

    public double getNivel() {
        return this.nivel;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Tiempo de Construcción - Tiempo de construcción del edificio en días
    // --------------------------------------------------------------------------

    public void setTiempoDeConstruccion(long newTiempoDeConstruccion) {
        this.tiempoDeConstruccion = newTiempoDeConstruccion;
    }

    public long getTiempoDeConstruccion() {
        return this.tiempoDeConstruccion;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Utilidad de los Tipos de Edificio - Determina la utilidad de cada tipo de edificios.
    // --------------------------------------------------------------------------

    public ArrayList getUtilidadDeLosTiposDeEdificio() {
        return utilidadDeLosTiposDeEdificio;
    }

    public void setUtilidadDeLosTiposDeEdificio(ArrayList utilidadDeLosTiposDeEdificio) {
        this.utilidadDeLosTiposDeEdificio = utilidadDeLosTiposDeEdificio;
    }

    public Hashtable getUtilidadDeLosTiposDeEdificioHT() {
        return utilidadDeLosTiposDeEdificioHT;
    }

    public void setUtilidadDeLosTiposDeEdificioHT(Hashtable utilidadDeLosTiposDeEdificioHT) {
        this.utilidadDeLosTiposDeEdificioHT = utilidadDeLosTiposDeEdificioHT;
    }

    public void addUtilidad(Utilidad utilidad) {
        if (this.utilidadDeLosTiposDeEdificio == null)
            this.utilidadDeLosTiposDeEdificio = new ArrayList();
        this.utilidadDeLosTiposDeEdificio.add(utilidad);
    }

    public boolean removeUtilidad(Utilidad utilidad) {
        if (this.utilidadDeLosTiposDeEdificio != null)
            return this.utilidadDeLosTiposDeEdificio.remove(utilidad);
        return false;
    }
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Asociacion Link B: Tipos de Edificio
    // --------------------------------------------------------------------------

    public ArrayList getTiposDeEdificio() {
        return tiposDeEdificio;
    }

    public void setTiposDeEdificio(ArrayList tiposDeEdificio) {
        this.tiposDeEdificio = tiposDeEdificio;
    }

    public Hashtable getTiposDeEdificioHT() {
        return tiposDeEdificioHT;
    }

    public void setTiposDeEdificioHT(Hashtable tiposDeEdificioHT) {
        this.tiposDeEdificioHT = tiposDeEdificioHT;
    }

    public void addEdificio(Edificio edificio) {
        if (this.tiposDeEdificio == null)
            this.tiposDeEdificio = new ArrayList();
        this.tiposDeEdificio.add(edificio);
    }

    public boolean removeEdificio(Edificio edificio) {
        if (this.tiposDeEdificio != null)
            return this.tiposDeEdificio.remove(edificio);
        return false;
    }
    // --------------------------------------------------------------------------


}


