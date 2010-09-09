/***********************************************************************
 * Module:  Utilidad.java
 * Author:  Benja & John
 * Purpose: Defines the Class Utilidad
 ***********************************************************************/
package org.abbatia.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class Utilidad {
    private long idDeUtilidad;
    private String descripcion;
    private ArrayList utilidadDeLosTiposDeEdificio;

    private Hashtable utilidadDeLosTiposDeEdificioHT;

    // --------------------------------------------------------------------------
    // ID de Utilidad
    // --------------------------------------------------------------------------
    public void setIdDeUtilidad(long newIdDeUtilidad) {
        this.idDeUtilidad = newIdDeUtilidad;
    }

    public long getIdDeUtilidad() {
        return this.idDeUtilidad;
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
    // Asociacion Link A: Utilidad de los Tipos de Edificio - Determina la utilidad de cada tipo de edificios.
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

    public void addTipoDeEdificio(TipoDeEdificio tipoDeEdificio) {
        if (this.utilidadDeLosTiposDeEdificio == null)
            this.utilidadDeLosTiposDeEdificio = new ArrayList();
        this.utilidadDeLosTiposDeEdificio.add(tipoDeEdificio);
    }

    public boolean removeTipoDeEdificio(TipoDeEdificio tipoDeEdificio) {
        if (this.utilidadDeLosTiposDeEdificio != null)
            return this.utilidadDeLosTiposDeEdificio.remove(tipoDeEdificio);
        return false;
    }
    // --------------------------------------------------------------------------


}


